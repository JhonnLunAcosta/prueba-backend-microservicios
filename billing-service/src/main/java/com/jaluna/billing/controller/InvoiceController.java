package com.jaluna.billing.controller;

import com.jaluna.billing.model.Invoice;
import com.jaluna.billing.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/billing")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/invoices")
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoiceDetails) {
        Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(id);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setCustomerName(invoiceDetails.getCustomerName());
            invoice.setTotalAmount(invoiceDetails.getTotalAmount());
            invoice.setDate(invoiceDetails.getDate());

            invoiceService.saveInvoice(invoice);
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(id);

        if (optionalInvoice.isPresent()) {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
