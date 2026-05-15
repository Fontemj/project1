import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Donor extends User {
    public enum BloodType { A_POS, A_NEG, B_POS, B_NEG, AB_POS, AB_NEG, O_POS, O_NEG }
    public enum Gender { MALE, FEMALE, OTHER }

    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodType bloodType;
    private String phone;
    private String address;
    private double weightKg;
    private String medicalHistory;
    private LocalDate lastDonationDate;
    private List<DonationRecord> donationHistory;
    private boolean eligible;

    public Donor(String id, String fullName, String email, String passwordHash,
                 LocalDate dob, Gender gender, BloodType bloodType, String phone,
                 String address, double weightKg, String medicalHistory) {
        super(id, fullName, email, passwordHash, Role.DONOR);
        this.dateOfBirth = dob;
        this.gender = gender;
        this.bloodType = bloodType;
        this.phone = phone;
        this.address = address;
        this.weightKg = weightKg;
        this.medicalHistory = medicalHistory;
        this.donationHistory = new ArrayList<>();
        this.eligible = true;
    }

    public static String bloodTypeDisplay(BloodType bt) {
        switch (bt) {
            case A_POS: return "A+";
            case A_NEG: return "A-";
            case B_POS: return "B+";
            case B_NEG: return "B-";
            case AB_POS: return "AB+";
            case AB_NEG: return "AB-";
            case O_POS: return "O+";
            case O_NEG: return "O-";
            default: return "?";
        }
    }

    public static BloodType fromDisplay(String s) {
        switch (s.trim()) {
            case "A+": return BloodType.A_POS;
            case "A-": return BloodType.A_NEG;
            case "B+": return BloodType.B_POS;
            case "B-": return BloodType.B_NEG;
            case "AB+": return BloodType.AB_POS;
            case "AB-": return BloodType.AB_NEG;
            case "O+": return BloodType.O_POS;
            case "O-": return BloodType.O_NEG;
            default: return BloodType.O_POS;
        }
    }

    public boolean checkEligibility() {
        LocalDate today = LocalDate.now();
        int age = today.getYear() - dateOfBirth.getYear();
        if (dateOfBirth.withYear(today.getYear()).isAfter(today)) age--;
        if (age < 18 || age > 65) return false;
        if (weightKg < 50) return false;
        if (lastDonationDate != null && lastDonationDate.plusDays(56).isAfter(today)) return false;
        return isActive();
    }

    public DonationRecord donateBlood(DataStore store, double volumeMl, String location, BloodUnit.Component component) {
        if (!checkEligibility()) {
            return null;
        }

        String recordId = "DR" + store.nextId();
        DonationRecord record = new DonationRecord(recordId, getId(), LocalDateTime.now(), volumeMl, location, bloodType);
        store.addDonationRecord(record);
        donationHistory.add(record);
        setLastDonationDate(LocalDate.now());

        int shelfDays = (component == BloodUnit.Component.PLATELETS) ? 5 :
                        (component == BloodUnit.Component.PLASMA) ? 365 : 42;
        LocalDate expiry = LocalDate.now().plusDays(shelfDays);
        String unitId = "BU" + store.nextId();
        BloodUnit unit = new BloodUnit(unitId, bloodType, component, volumeMl, LocalDate.now(), expiry, location, getId());
        store.addBloodUnit(unit);

        return record;
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public BloodType getBloodType() { return bloodType; }
    public void setBloodType(BloodType bloodType) { this.bloodType = bloodType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }
    public List<DonationRecord> getDonationHistory() { return donationHistory; }
    public boolean isEligible() { return checkEligibility(); }
}