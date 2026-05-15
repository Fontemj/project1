import java.time.LocalDate;
import java.time.LocalDateTime;

public class DonationRecord {
    private String id;
    private String donorId;
    private LocalDateTime donationDate;
    private double volumeMl;
    private String location;
    private Donor.BloodType bloodType;

    public DonationRecord(String id, String donorId, LocalDateTime date, double volumeMl,
                          String location, Donor.BloodType bloodType) {
        this.id = id;
        this.donorId = donorId;
        this.donationDate = date;
        this.volumeMl = volumeMl;
        this.location = location;
        this.bloodType = bloodType;
    }

    public String getId() { return id; }
    public String getDonorId() { return donorId; }
    public LocalDateTime getDonationDate() { return donationDate; }
    public double getVolumeMl() { return volumeMl; }
    public String getLocation() { return location; }
    public Donor.BloodType getBloodType() { return bloodType; }
}