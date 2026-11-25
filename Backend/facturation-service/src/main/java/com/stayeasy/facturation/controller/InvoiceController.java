package com.stayeasy.facturation.controller;

import com.stayeasy.facturation.dto.InvoiceResponse;
import com.stayeasy.facturation.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Factures")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<InvoiceResponse> all(@RequestParam(required = false) String userId,
                                     @RequestParam(required = false) String ownerId) {
        return service.findAll(userId, ownerId);
    }

    @GetMapping("/{id}")
    public InvoiceResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/reservation/{rid}")
    public InvoiceResponse byReservation(@PathVariable Long rid) {
        return service.findByReservation(rid);
    }

    @PostMapping("/{id}/verify")
    public InvoiceResponse verify(@PathVariable Long id) {
        return service.verify(id);
    }

    @PatchMapping("/{id}/cancel")
    public InvoiceResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}
