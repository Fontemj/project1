

public class Doctor extends User {
    private String specialisation;
    private String licenseNumber;
    private String phone;
    private String hospitalAffiliation;

    public Doctor(String id, String fullName, String email, String passwordHash,
                  String specialisation, String licenseNumber, String phone, String hospitalAffiliation) {
        super(id, fullName, email, passwordHash, Role.DOCTOR);
        this.specialisation = specialisation;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.hospitalAffiliation = hospitalAffiliation;
    }

    public String getSpecialisation() { return specialisation; }
    public void setSpecialisation(String s) { this.specialisation = s; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }
    public String getHospitalAffiliation() { return hospitalAffiliation; }
    public void setHospitalAffiliation(String h) { this.hospitalAffiliation = h; }
}