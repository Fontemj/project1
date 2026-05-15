

import java.time.LocalDate;

public class BloodUnit {
    public enum Component { WHOLE_BLOOD, PLASMA, PLATELETS, RED_CELLS }
    public enum Status { AVAILABLE, RESERVED, DISPATCHED, DISCARDED, EXPIRED }

    private String id;
    private Donor.BloodType bloodType;
    private Component component;
    private double volumeMl;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    private String storageLocation;
    private Status status;
    private String donorId;
    private boolean tested;

    public BloodUnit(String id, Donor.BloodType bloodType, Component component,
                     double volumeMl, LocalDate collectionDate, LocalDate expiryDate,
                     String storageLocation, String donorId) {
        this.id = id;
        this.bloodType = bloodType;
        this.component = component;
        this.volumeMl = volumeMl;
        this.collectionDate = collectionDate;
        this.expiryDate = expiryDate;
        this.storageLocation = storageLocation;
        this.status = Status.AVAILABLE;
        this.donorId = donorId;
        this.tested = false;
    }

    public boolean isExpired() { return LocalDate.now().isAfter(expiryDate); }
    public boolean isExpiringSoon() {
        return !isExpired() && LocalDate.now().plusDays(3).isAfter(expiryDate);
    }

    public String getId() { return id; }
    public Donor.BloodType getBloodType() { return bloodType; }
    public Component getComponent() { return component; }
    public double getVolumeMl() { return volumeMl; }
    public LocalDate getCollectionDate() { return collectionDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getStorageLocation() { return storageLocation; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public boolean isTested() { return tested; }
    public void setTested(boolean tested) { this.tested = tested; }
    public String getDonorId() { return donorId; }

    public static String componentDisplay(Component c) {
        switch (c) {
            case WHOLE_BLOOD: return "Whole Blood";
            case PLASMA: return "Plasma";
            case PLATELETS: return "Platelets";
            case RED_CELLS: return "Red Cells";
            default: return c.toString();
        }
    }
}