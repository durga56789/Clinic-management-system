package com.clinicmanagement.controller;

import com.clinicmanagement.model.Prescription;
import com.clinicmanagement.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PrescriptionController handles all prescription-related API endpoints.
 * 
 * Roles & Access:
 *  - Doctor: Can create and update prescriptions for patients.
 *  - Patient: Can view their own prescriptions.
 *  - Admin: Can view all prescriptions (for audit/reporting).
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * Create a new prescription (Doctor only).
     */
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription savedPrescription = prescriptionService.createPrescription(prescription);
        return ResponseEntity.ok(savedPrescription);
    }

    /**
     * Get all prescriptions (Admin).
     */
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Get prescriptions by patient ID (Doctor/Patient).
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Update a prescription (Doctor).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id,
                                                            @RequestBody Prescription prescription) {
        Prescription updated = prescriptionService.updatePrescription(id, prescription);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a prescription (Admin/Doctor).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
