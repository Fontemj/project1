

public class Hospital extends User {
    private String hospitalName;
    private String address;
    private String phone;
    private String licenseNumber;
    private boolean verified;

    public Hospital(String id, String contactPerson, String email, String passwordHash,
                    String hospitalName, String address, String phone, String licenseNumber) {
        super(id, contactPerson, email, passwordHash, Role.HOSPITAL_STAFF);
        this.hospitalName = hospitalName;
        this.address = address;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.verified = false;
    }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String n) { this.hospitalName = n; }
    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }
    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }
    public String getLicenseNumber() { return licenseNumber; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean v) { this.verified = v; }
}