const express = require('express'), { Pool } = require('pg'), amqp = require('amqplib'), { Eureka } = require('eureka-js-client');
const PORT = 8087, HOST = process.env.EUREKA_INSTANCE_HOST || 'localhost';
const ROLES = ['VOYAGEUR_ETUDIANT', 'PROPRIETAIRE', 'EQUIPE_TECHNIQUE'], STATUTS = ['ACTIF', 'SUSPENDU', 'DESACTIVE'];
const pool = new Pool({ connectionString: process.env.DB_URL || 'postgresql://stayeasy:stayeasy@localhost:5432/user_db' });
const app = express(); app.use(express.json());
let channel = null;
const wrap = f => (q, s) => f(q, s).catch(e => { console.error(e); s.status(500).json({ error: e.message }); });
const one = async (id) => (await pool.query('SELECT * FROM users WHERE id=$1', [id])).rows[0];
const view = u => ({ id: u.id, keycloakId: u.keycloak_id, nom: u.nom, prenom: u.prenom, email: u.email, telephone: u.telephone, role: u.role, statut: u.statut, dateInscription: u.date_inscription });

app.get('/health', (q, s) => s.json({ status: 'UP' }));
app.post('/api/users', wrap(async (q, s) => {
  const b = q.body;
  if (!b.keycloakId || !b.email || !ROLES.includes(b.role)) return s.status(400).json({ error: 'Données invalides' });
  if ((await pool.query('SELECT 1 FROM users WHERE keycloak_id=$1', [b.keycloakId])).rowCount) return s.status(409).json({ error: 'Utilisateur existant' });
  const r = await pool.query('INSERT INTO users(keycloak_id,nom,prenom,email,telephone,role) VALUES($1,$2,$3,$4,$5,$6) RETURNING *', [b.keycloakId, b.nom, b.prenom, b.email, b.telephone, b.role]);
  if (channel) channel.publish('stayeasy.events', 'user.registered', Buffer.from(JSON.stringify({ userId: b.keycloakId, email: b.email, role: b.role })), { contentType: 'application/json' });
  s.status(201).json(view(r.rows[0]));
}));
app.get('/api/users/keycloak/:kid', wrap(async (q, s) => { const u = (await pool.query('SELECT * FROM users WHERE keycloak_id=$1', [q.params.kid])).rows[0]; u ? s.json(view(u)) : s.sendStatus(404); }));
app.get('/api/users/:id', wrap(async (q, s) => { const u = await one(q.params.id); u ? s.json(view(u)) : s.sendStatus(404); }));
app.put('/api/users/:id', wrap(async (q, s) => {
  const u = await one(q.params.id); if (!u) return s.sendStatus(404);
  const b = q.body; if (b.role && !ROLES.includes(b.role)) return s.status(400).json({ error: 'Rôle invalide' });
  const r = await pool.query('UPDATE users SET nom=$1,prenom=$2,email=$3,telephone=$4,role=$5 WHERE id=$6 RETURNING *', [b.nom ?? u.nom, b.prenom ?? u.prenom, b.email ?? u.email, b.telephone ?? u.telephone, b.role ?? u.role, u.id]);
  s.json(view(r.rows[0]));
}));
app.get('/api/users/:id/profil', wrap(async (q, s) => { const u = await one(q.params.id); u ? s.json({ ...view(u), profilVoyageur: u.profil_voyageur, profilProprietaire: u.profil_proprietaire }) : s.sendStatus(404); }));
for (const [route, col] of [['profil-voyageur', 'profil_voyageur'], ['profil-proprietaire', 'profil_proprietaire']])
  app.put('/api/users/:id/' + route, wrap(async (q, s) => {
    if (!q.body || typeof q.body !== 'object') return s.sendStatus(400);
    const r = await pool.query(`UPDATE users SET ${col}=$1 WHERE id=$2 RETURNING *`, [q.body, q.params.id]);
    r.rowCount ? s.json(r.rows[0][col]) : s.sendStatus(404);
  }));
app.put('/api/users/:id/statut', wrap(async (q, s) => {
  if (!STATUTS.includes(q.body.statut)) return s.status(400).json({ error: 'Statut invalide' });
  const r = await pool.query('UPDATE users SET statut=$1 WHERE id=$2 RETURNING *', [q.body.statut, q.params.id]);
  r.rowCount ? s.json(view(r.rows[0])) : s.sendStatus(404);
}));

(async () => {
  await pool.query(`CREATE TABLE IF NOT EXISTS users(id SERIAL PRIMARY KEY, keycloak_id TEXT UNIQUE NOT NULL, nom TEXT, prenom TEXT, email TEXT, telephone TEXT,
    role TEXT NOT NULL, statut TEXT DEFAULT 'ACTIF', date_inscription TIMESTAMP DEFAULT now(), profil_voyageur JSONB, profil_proprietaire JSONB)`);
  try { const c = await amqp.connect(process.env.RABBIT_URL || 'amqp://guest:guest@localhost:5672'); channel = await c.createChannel(); await channel.assertExchange('stayeasy.events', 'topic', { durable: true }); }
  catch (e) { console.error('RabbitMQ indisponible :', e.message); }
  app.listen(PORT, () => console.log('USER sur ' + PORT));
  new Eureka({
    instance: { app: 'USER', instanceId: `user:${HOST}:${PORT}`, hostName: HOST, ipAddr: HOST, port: { $: PORT, '@enabled': true }, vipAddress: 'user',
      statusPageUrl: `http://${HOST}:${PORT}/health`, healthCheckUrl: `http://${HOST}:${PORT}/health`, dataCenterInfo: { '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo', name: 'MyOwn' } },
    eureka: { host: process.env.EUREKA_HOST || 'localhost', port: 8761, servicePath: '/eureka/apps/' }
  }).start();
})();
