package controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DoctorService;
import service.TokenService;
import dto.AvailabilitySlot;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorController {

    private final TokenService tokenService;
    private final DoctorService doctorService;

    public DoctorController(TokenService tokenService, DoctorService doctorService) {
        this.tokenService = tokenService;
        this.doctorService = doctorService;
    }

    /**
     * GET /api/doctor/{doctorId}/availability
     * Required params: user, date (yyyy-MM-dd), token
     * Returns availability slots for the given doctor on the given date
     */
    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestParam("user") String user,
            @PathVariable("doctorId") Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("token") String token
    ) {
        // Basic parameter validation
        if (user == null || user.isBlank() || token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing required parameters: user and token must be provided.");
        }

        // Token validation per assignment requirement
        if (!tokenService.isValid(user, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }

        // Fetch availability from service
        List<AvailabilitySlot> slots = doctorService.getAvailabilityForDate(doctorId, date);
        return ResponseEntity.ok(slots);
    }
}
