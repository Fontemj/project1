import java.time.LocalDate;
import java.time.LocalDateTime;

public class BloodRequest {
    public enum Urgency { ROUTINE, URGENT, EMERGENCY }
    public enum Status { PENDING, APPROVED, DISPATCHED, DELIVERED, REJECTED }

    private String id;
    private String hospitalId;
    private String hospitalName;
    private Donor.BloodType bloodType;
    private BloodUnit.Component component;
    private double quantityMl;
    private Urgency urgency;
    private LocalDate requiredBy;
    private Status status;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime lastUpdated;
    private String patientName;

    public BloodRequest(String id, String hospitalId, String hospitalName,
                        Donor.BloodType bloodType, BloodUnit.Component component,
                        double quantityMl, Urgency urgency, LocalDate requiredBy, String patientName) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.bloodType = bloodType;
        this.component = component;
        this.quantityMl = quantityMl;
        this.urgency = urgency;
        this.requiredBy = requiredBy;
        this.status = Status.PENDING;
        this.submittedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.patientName = patientName;
    }

    public String getId() { return id; }
    public String getHospitalId() { return hospitalId; }
    public String getHospitalName() { return hospitalName; }
    public Donor.BloodType getBloodType() { return bloodType; }
    public BloodUnit.Component getComponent() { return component; }
    public double getQuantityMl() { return quantityMl; }
    public Urgency getUrgency() { return urgency; }
    public LocalDate getRequiredBy() { return requiredBy; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; this.lastUpdated = LocalDateTime.now(); }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String r) { this.rejectionReason = r; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public String getPatientName() { return patientName; }
}