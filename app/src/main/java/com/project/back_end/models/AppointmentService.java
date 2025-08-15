package service;

import database.Scheduler;
import mainpackage.Patient;
import mainpackage.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    /**
     * Books a new appointment for the given patient with the specified doctor.
     *
     * @param patient          the patient booking the appointment
     * @param doctorUsername   the username of the doctor
     * @param appointmentTime  the desired appointment date and time
     * @return true if the appointment was successfully booked, false otherwise
     */
    public boolean bookAppointment(Patient patient, String doctorUsername, LocalDateTime appointmentTime) {
        if (patient == null || doctorUsername == null || appointmentTime == null) {
            throw new IllegalArgumentException("Patient, doctor username, and appointment time must not be null");
        }

        String appointmentDateStr = appointmentTime.toString(); // adapt format as needed for Scheduler

        if (!Scheduler.isDoctorAvailable(doctorUsername, appointmentDateStr)) {
            return false;
        }

        return Scheduler.scheduleAppointment(patient, doctorUsername, appointmentDateStr);
    }

    /**
     * Retrieves all appointments for the given patient.
     *
     * @param patient the patient whose appointments to retrieve
     * @return list of Appointment objects
     */
    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient must not be null");
        }
        return Scheduler.getAppointmentsForPatient(patient);
    }

    /**
     * Retrieves all appointments for a given doctor on a specific date.
     *
     * @param doctorUsername the doctor's username
     * @param date           the date to check
     * @return list of Appointment objects
     */
    public List<Appointment> getAppointmentsForDoctorOnDate(String doctorUsername, LocalDateTime date) {
        if (doctorUsername == null || date == null) {
            throw new IllegalArgumentException("Doctor username and date must not be null");
        }
        return Scheduler.getAppointmentsForDoctorOnDate(doctorUsername, date.toString());
    }
}
