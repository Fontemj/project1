

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static DataStore instance;

    private final Map<String, User> users = new LinkedHashMap<>();
    private final List<BloodUnit> bloodUnits = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<BloodRequest> bloodRequests = new ArrayList<>();
    private final List<DonationRecord> donationRecords = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();
    private int idCounter = 1000;

    private DataStore() { seedData(); }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public String nextId() { return String.valueOf(++idCounter); }

    // ── Users ──────────────────────────────────────────────────────────────────
    public void addUser(User u) { users.put(u.getId(), u); }
    public User getUserById(String id) { return users.get(id); }
    public User authenticate(String email, String password) {
        return users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPasswordHash().equals(password)
                        && u.isActive())
                .findFirst().orElse(null);
    }
    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }
    public List<Donor> getAllDonors() {
        return users.values().stream()
                .filter(u -> u instanceof Donor)
                .map(u -> (Donor) u)
                .collect(Collectors.toList());
    }
    public List<Hospital> getAllHospitals() {
        return users.values().stream()
                .filter(u -> u instanceof Hospital)
                .map(u -> (Hospital) u)
                .collect(Collectors.toList());
    }
    public List<Doctor> getAllDoctors() {
        return users.values().stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
    }

    // ── Blood Units ────────────────────────────────────────────────────────────
    public void addBloodUnit(BloodUnit bu) { bloodUnits.add(bu); }
    public List<BloodUnit> getAllBloodUnits() { return new ArrayList<>(bloodUnits); }
    public List<BloodUnit> getAvailableUnits() {
        return bloodUnits.stream()
                .filter(u -> u.getStatus() == BloodUnit.Status.AVAILABLE && !u.isExpired())
                .collect(Collectors.toList());
    }
    public Map<Donor.BloodType, Long> getInventoryCounts() {
        return getAvailableUnits().stream()
                .collect(Collectors.groupingBy(BloodUnit::getBloodType, Collectors.counting()));
    }

    // ── Appointments ──────────────────────────────────────────────────────────
    public void addAppointment(Appointment a) { appointments.add(a); }
    public List<Appointment> getAllAppointments() { return new ArrayList<>(appointments); }
    public List<Appointment> getAppointmentsForDonor(String donorId) {
        return appointments.stream()
                .filter(a -> a.getDonorId().equals(donorId))
                .collect(Collectors.toList());
    }

    // ── Blood Requests ────────────────────────────────────────────────────────
    public void addBloodRequest(BloodRequest r) { bloodRequests.add(r); }
    public List<BloodRequest> getAllBloodRequests() { return new ArrayList<>(bloodRequests); }
    public List<BloodRequest> getRequestsForHospital(String hospitalId) {
        return bloodRequests.stream()
                .filter(r -> r.getHospitalId().equals(hospitalId))
                .collect(Collectors.toList());
    }

    // ── Donation Records ──────────────────────────────────────────────────────
    public void addDonationRecord(DonationRecord dr) { donationRecords.add(dr); }
    public List<DonationRecord> getAllDonationRecords() { return new ArrayList<>(donationRecords); }
    public List<DonationRecord> getDonationRecordsForDonor(String donorId) {
        return donationRecords.stream()
                .filter(dr -> dr.getDonorId().equals(donorId))
                .collect(Collectors.toList());
    }

    // ── Notifications ─────────────────────────────────────────────────────────
    public void addNotification(Notification n) { notifications.add(n); }
    public List<Notification> getNotificationsForUser(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparing(Notification::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
    public long getUnreadCount(String userId) {
        return getNotificationsForUser(userId).stream().filter(n -> !n.isRead()).count();
    }

    // ── Compatibility ─────────────────────────────────────────────────────────
    public List<Donor.BloodType> getCompatibleDonorTypes(Donor.BloodType recipientType) {
        Map<Donor.BloodType, List<Donor.BloodType>> compat = new HashMap<>();
        compat.put(Donor.BloodType.A_POS,  Arrays.asList(Donor.BloodType.A_POS, Donor.BloodType.A_NEG, Donor.BloodType.O_POS, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.A_NEG,  Arrays.asList(Donor.BloodType.A_NEG, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.B_POS,  Arrays.asList(Donor.BloodType.B_POS, Donor.BloodType.B_NEG, Donor.BloodType.O_POS, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.B_NEG,  Arrays.asList(Donor.BloodType.B_NEG, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.AB_POS, Arrays.asList(Donor.BloodType.values()));
        compat.put(Donor.BloodType.AB_NEG, Arrays.asList(Donor.BloodType.A_NEG, Donor.BloodType.B_NEG, Donor.BloodType.AB_NEG, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.O_POS,  Arrays.asList(Donor.BloodType.O_POS, Donor.BloodType.O_NEG));
        compat.put(Donor.BloodType.O_NEG,  Arrays.asList(Donor.BloodType.O_NEG));
        return compat.getOrDefault(recipientType, Collections.emptyList());
    }

    // ── Seed Data ─────────────────────────────────────────────────────────────
    private void seedData() {
        // Admin
        User admin = new User("A001", "System Admin", "admin@gmail.com", "admin123", User.Role.ADMIN);
        users.put(admin.getId(), admin);

        // Donors
        Donor d1 = new Donor("D001","Fontem junior","alice@email.com","pass123",
                LocalDate.of(1990,3,15), Donor.Gender.FEMALE, Donor.BloodType.O_NEG,
                "0241234567","Buea, cameroon",62,"No known conditions");
        d1.setLastDonationDate(LocalDate.now().minusDays(60));
        users.put(d1.getId(), d1);

        Donor d2 = new Donor("D002","Ebong thierry","kwame@gmail.com","pass123",
                LocalDate.of(1985,7,22), Donor.Gender.MALE, Donor.BloodType.A_POS,
                "0249876543","Buea, cameroon",75,"Mild hypertension");
        d2.setLastDonationDate(LocalDate.now().minusDays(30));
        users.put(d2.getId(), d2);

        Donor d3 = new Donor("D003","Dr Ines ","Ines@gmail.com","pass123",
                LocalDate.of(1995,11,8), Donor.Gender.FEMALE, Donor.BloodType.B_POS,
                "0261111222","Texas, United states",58,"None");
        users.put(d3.getId(), d3);

        Donor d4 = new Donor("D004","valantine","valantine@gmail.com","pass123",
                LocalDate.of(2026,5,12), Donor.Gender.MALE, Donor.BloodType.AB_POS,
                "0271234000","Douala, cameroon",80,"None");
        d4.setLastDonationDate(LocalDate.now().minusDays(90));
        users.put(d4.getId(), d4);

        Donor d5 = new Donor("D005","jane k","jane@gmail.com","pass123",
                LocalDate.of(2000,1,18), Donor.Gender.FEMALE, Donor.BloodType.O_POS,
                "0231000999","Cape Coast, Ghana",55,"None");
        users.put(d5.getId(), d5);

        // Hospitals
        Hospital h1 = new Hospital("H001","Dr. James Otieno","korle-bu@hospital.gh","hosp123",
                "Buea Regional hospital","Buea","0302666000","KBH-2024-001");
        h1.setVerified(true);
        users.put(h1.getId(), h1);

        Hospital h2 = new Hospital("H002","Nurse Mary Adjei","komfo@hospital.gh","hosp123",
                "Komfo Anokye Teaching Hospital","Kumasi, Ghana","0322022000","KATH-2024-002");
        h2.setVerified(true);
        users.put(h2.getId(), h2);

        Hospital h3 = new Hospital("H003","Admin Staff","ridge@hospital.gh","hosp123",
                "Ridge Hospital","Accra, Ghana","0302770000","RH-2024-003");
        users.put(h3.getId(), h3);

        // Doctors
        Doctor doc1 = new Doctor("DOC001","Dr. Samuel Aboagye","saboagye@bdms.org","doc123",
                "Haematology","GMA-2024-1001","0244556677","Korle-Bu Teaching Hospital");
        users.put(doc1.getId(), doc1);

        Doctor doc2 = new Doctor("DOC002","Dr. Priscilla Ntow","pntow@bdms.org","doc123",
                "Transfusion Medicine","GMA-2024-1002","0554433221","Komfo Anokye Teaching Hospital");
        users.put(doc2.getId(), doc2);

        User tester = new User("T001","Lab Tester","tester@gmail.com","test123", User.Role.TESTER);
        users.put(tester.getId(), tester);

        // Blood Units
        String[] locations = {"Buea","kumba","Tema Blood Center"};
        Donor.BloodType[] types = Donor.BloodType.values();
        BloodUnit.Component[] comps = BloodUnit.Component.values();
        Random rng = new Random(42);
        for (int i = 0; i < 40; i++) {
            Donor.BloodType bt = types[rng.nextInt(types.length)];
            BloodUnit.Component comp = comps[rng.nextInt(comps.length)];
            LocalDate collected = LocalDate.now().minusDays(rng.nextInt(20));
            int shelf = (comp == BloodUnit.Component.PLATELETS) ? 5 :
                        (comp == BloodUnit.Component.PLASMA)    ? 365 : 42;
            LocalDate expiry = collected.plusDays(shelf);
            BloodUnit bu = new BloodUnit("BU" + (1000+i), bt, comp,
                    450, collected, expiry, locations[rng.nextInt(locations.length)], "D001");
            if (rng.nextInt(10) < 2) bu.setStatus(BloodUnit.Status.RESERVED);
            bloodUnits.add(bu);
        }

        // Donation records
        DonationRecord dr1 = new DonationRecord("DR001","D001",
                LocalDateTime.now().minusDays(60),450,"Korle-Bu Blood Bank", Donor.BloodType.O_NEG);
        donationRecords.add(dr1);
        d1.getDonationHistory().add(dr1);

        DonationRecord dr2 = new DonationRecord("DR002","D002",
                LocalDateTime.now().minusDays(30),450,"Komfo Anokye Blood Bank", Donor.BloodType.A_POS);
        donationRecords.add(dr2);
        d2.getDonationHistory().add(dr2);

        DonationRecord dr3 = new DonationRecord("DR003","D004",
                LocalDateTime.now().minusDays(90),450,"Tema Blood Center", Donor.BloodType.AB_POS);
        donationRecords.add(dr3);
        d4.getDonationHistory().add(dr3);

        // Appointments
        appointments.add(new Appointment("APT001","D003",
                LocalDateTime.now().plusDays(2).withHour(9).withMinute(0),"Korle-Bu Blood Bank"));
        appointments.add(new Appointment("APT002","D005",
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(30),"Tema Blood Center"));
        Appointment past = new Appointment("APT003","D001",
                LocalDateTime.now().minusDays(60),"Korle-Bu Blood Bank");
        past.setStatus(Appointment.Status.COMPLETED);
        appointments.add(past);

        // Blood Requests
        BloodRequest br1 = new BloodRequest("REQ001","H001","Korle-Bu Teaching Hospital",
                Donor.BloodType.O_NEG, BloodUnit.Component.RED_CELLS, 900,
                BloodRequest.Urgency.EMERGENCY, LocalDate.now().plusDays(1),"Emergency Patient A");
        bloodRequests.add(br1);

        BloodRequest br2 = new BloodRequest("REQ002","H002","Komfo Anokye Teaching Hospital",
                Donor.BloodType.A_POS, BloodUnit.Component.WHOLE_BLOOD, 450,
                BloodRequest.Urgency.ROUTINE, LocalDate.now().plusDays(5),"Patient B - Scheduled Surgery");
        br2.setStatus(BloodRequest.Status.APPROVED);
        bloodRequests.add(br2);

        BloodRequest br3 = new BloodRequest("REQ003","H001","Korle-Bu Teaching Hospital",
                Donor.BloodType.B_POS, BloodUnit.Component.PLASMA, 450,
                BloodRequest.Urgency.URGENT, LocalDate.now().plusDays(2),"ICU Patient C");
        bloodRequests.add(br3);

        // Notifications
        notifications.add(new Notification("N001","D001","Eligible to Donate",
                "You are now eligible to donate blood again. Book your appointment today!",
                Notification.Type.INFO));
        notifications.add(new Notification("N002","H001","Emergency Request Received",
                "Your emergency blood request REQ001 has been received and is being processed.",
                Notification.Type.URGENT));
        notifications.add(new Notification("N003","A001","Low Stock Alert",
                "O- blood type is critically low. Only 2 units remaining.",
                Notification.Type.WARNING));
        notifications.add(new Notification("N004","DOC001","Compatibility Override Logged",
                "A compatibility override was recorded for patient case #PC-2024-441.",
                Notification.Type.INFO));
    }
}