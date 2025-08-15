package controller;

import model.Prescription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    // In a real app, you would inject a PrescriptionService here
    // @Autowired
    // private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<?> savePrescription(@RequestBody Prescription prescription) {
        if (prescription == null || prescription.getPatientId() <= 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Invalid prescription data"));
        }

        // Here we would call a service to save to DB, e.g.:
        // Prescription saved = prescriptionService.save(prescription);

        // For now, just simulate a successful save
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Prescription saved successfully"));
    }

    // Simple inner class for consistent API responses
    static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
