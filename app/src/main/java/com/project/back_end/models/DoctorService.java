package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorService {

    private static final String URL = "jdbc:mysql://localhost:3306/hms";
    private static final String USER = "root";
    private static final String PASSWORD = "zahid";

    /**
     * Checks if a doctor with the given username and password exists in the database.
     *
     * @param doctorId doctor's ID or username
     * @param password doctor's password
     * @return true if login is valid, false otherwise
     */
    public boolean validateLogin(String doctorId, String password) {
        String query = "SELECT * FROM doctor_record WHERE doctor_id = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctorId);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a doctor is available on a given date.
     *
     * @param doctorId doctor's ID
     * @param date appointment date in YYYY-MM-DD format
     * @return true if available, false otherwise
     */
    public boolean isDoctorAvailable(String doctorId, String date) {
        String query = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctorId);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                // if no appointment exists for this date, doctor is available
                return !rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a doctor to the database.
     *
     * @param doctorId doctor's ID
     * @param name doctor's name
     * @param specialization doctor's specialization
     * @param password doctor's login password
     * @return true if doctor was added successfully, false otherwise
     */
    public boolean addDoctor(String doctorId, String name, String specialization, String password) {
        String sql = "INSERT INTO doctor_record (doctor_id, name, specialization, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            stmt.setString(2, name);
            stmt.setString(3, specialization);
            stmt.setString(4, password);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
