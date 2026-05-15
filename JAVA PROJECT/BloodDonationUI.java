import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class BloodDonationUI extends JFrame {
    private final DataStore dataStore = DataStore.getInstance();

    public BloodDonationUI() {
        setTitle("Blood Donation Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        Theme.applyGlobalDefaults();
        getContentPane().setBackground(Theme.BG_DARK);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        JLabel title = new JLabel("Blood Donation Management System");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Monitor donors, inventory, appointments, and blood requests in one place.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.BG_DARK);
        tabs.setForeground(Theme.TEXT_PRIMARY);

        tabs.addTab("Dashboard", createDashboardPanel());
        tabs.addTab("Donors", createDonorsPanel());
        tabs.addTab("Hospitals", createHospitalsPanel());
        tabs.addTab("Inventory", createInventoryPanel());
        tabs.addTab("Appointments", createAppointmentsPanel());
        tabs.addTab("Requests", createRequestsPanel());

        return tabs;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(24, 24));
        dashboard.setOpaque(false);
        dashboard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel cards = new JPanel(new GridLayout(1, 3, 24, 24));
        cards.setOpaque(false);

        cards.add(createStatCard("Donors", String.valueOf(dataStore.getAllDonors().size()), "Active blood donors registered."));
        cards.add(createStatCard("Hospitals", String.valueOf(dataStore.getAllHospitals().size()), "Hospital partners and request senders."));
        cards.add(createStatCard("Pending Requests", String.valueOf(dataStore.getAllBloodRequests().size()), "Blood requests needing attention."));

        dashboard.add(cards, BorderLayout.NORTH);
        dashboard.add(createRecentActivityPanel(), BorderLayout.CENTER);

        return dashboard;
    }

    private JPanel createStatCard(String title, String value, String subtitle) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 12));
        card.setBackground(Theme.BG_SURFACE);

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);

        JLabel number = new JLabel(value);
        number.setFont(Theme.FONT_BIG_NUM);
        number.setForeground(Theme.ACCENT_RED);

        JLabel desc = new JLabel(subtitle);
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_SECONDARY);

        card.add(heading, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);
        card.add(desc, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createRecentActivityPanel() {
        JPanel panel = Theme.card();
        panel.setLayout(new BorderLayout(0, 12));

        JLabel heading = new JLabel("Recent Activity");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);

        JTextArea activityArea = new JTextArea();
        activityArea.setEditable(false);
        activityArea.setFont(Theme.FONT_BODY);
        activityArea.setBackground(Theme.BG_DARK);
        activityArea.setForeground(Theme.TEXT_PRIMARY);
        activityArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        StringBuilder builder = new StringBuilder();
        builder.append("Donors: ").append(dataStore.getAllDonors().size()).append(" registered\n");
        builder.append("Hospitals: ").append(dataStore.getAllHospitals().size()).append(" registered\n");
        builder.append("Available Blood Units: ").append(dataStore.getAvailableUnits().size()).append("\n");
        builder.append("Appointments: ").append(dataStore.getAllAppointments().size()).append(" scheduled\n");
        builder.append("Blood Requests: ").append(dataStore.getAllBloodRequests().size()).append(" total\n");

        activityArea.setText(builder.toString());

        panel.add(heading, BorderLayout.NORTH);
        panel.add(Theme.scrollPane(activityArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDonorsPanel() {
        String[] columns = {"ID", "Name", "Blood Type", "Gender", "Phone", "Address", "Weight (kg)"};
        List<Donor> donors = dataStore.getAllDonors();
        Object[][] rows = new Object[donors.size()][columns.length];

        for (int i = 0; i < donors.size(); i++) {
            Donor donor = donors.get(i);
            rows[i] = new Object[]{
                donor.getId(),
                donor.getFullName(),
                Donor.bloodTypeDisplay(donor.getBloodType()),
                donor.getGender(),
                donor.getPhone(),
                donor.getAddress(),
                donor.getWeightKg()
            };
        }

        return createTablePanel("Donor Directory", columns, rows);
    }

    private JPanel createHospitalsPanel() {
        String[] columns = {"ID", "Hospital", "Contact", "Email", "Phone", "Address", "Verified"};
        List<Hospital> hospitals = dataStore.getAllHospitals();
        Object[][] rows = new Object[hospitals.size()][columns.length];

        for (int i = 0; i < hospitals.size(); i++) {
            Hospital hospital = hospitals.get(i);
            rows[i] = new Object[]{
                hospital.getId(),
                hospital.getHospitalName(),
                hospital.getFullName(),
                hospital.getEmail(),
                hospital.getPhone(),
                hospital.getAddress(),
                hospital.isVerified() ? "Yes" : "No"
            };
        }

        return createTablePanel("Hospital Partners", columns, rows);
    }

    private JPanel createInventoryPanel() {
        String[] columns = {"Blood Type", "Units Available"};
        Map<Donor.BloodType, Long> inventory = dataStore.getInventoryCounts();
        Object[][] rows = new Object[inventory.size()][columns.length];
        int i = 0;
        for (Map.Entry<Donor.BloodType, Long> entry : inventory.entrySet()) {
            rows[i++] = new Object[]{ Donor.bloodTypeDisplay(entry.getKey()), entry.getValue() };
        }
        return createTablePanel("Blood Inventory", columns, rows);
    }

    private JPanel createAppointmentsPanel() {
        String[] columns = {"ID", "Donor ID", "Scheduled Time", "Location", "Status"};
        List<Appointment> appointments = dataStore.getAllAppointments();
        Object[][] rows = new Object[appointments.size()][columns.length];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            rows[i] = new Object[]{
                appointment.getId(),
                appointment.getDonorId(),
                appointment.getScheduledTime().format(fmt),
                appointment.getLocation(),
                appointment.getStatus()
            };
        }

        return createTablePanel("Appointments", columns, rows);
    }

    private JPanel createRequestsPanel() {
        String[] columns = {"ID", "Hospital", "Blood Type", "Component", "Quantity (mL)", "Urgency", "Required By", "Status"};
        List<BloodRequest> requests = dataStore.getAllBloodRequests();
        Object[][] rows = new Object[requests.size()][columns.length];

        for (int i = 0; i < requests.size(); i++) {
            BloodRequest request = requests.get(i);
            rows[i] = new Object[]{
                request.getId(),
                request.getHospitalName(),
                Donor.bloodTypeDisplay(request.getBloodType()),
                BloodUnit.componentDisplay(request.getComponent()),
                request.getQuantityMl(),
                request.getUrgency(),
                request.getRequiredBy(),
                request.getStatus()
            };
        }

        return createTablePanel("Blood Requests", columns, rows);
    }

    private JPanel createTablePanel(String title, String[] columns, Object[][] rows) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = Theme.styledTable(model);
        JScrollPane scrollPane = Theme.scrollPane(table);
        scrollPane.setBorder(null);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}
