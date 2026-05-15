
import java.time.LocalDateTime;

public class Appointment {
    public enum Status { SCHEDULED, COMPLETED, CANCELLED, WALK_IN }

    private String id;
    private String donorId;
    private LocalDateTime scheduledTime;
    private String location;
    private Status status;
    private String notes;

    public Appointment(String id, String donorId, LocalDateTime scheduledTime, String location) {
        this.id = id;
        this.donorId = donorId;
        this.scheduledTime = scheduledTime;
        this.location = location;
        this.status = Status.SCHEDULED;
    }

    public String getId() { return id; }
    public String getDonorId() { return donorId; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime t) { this.scheduledTime = t; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public Status getStatus() { return status; }
    public void setStatus(Status s) { this.status = s; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
}