import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class UserDashboard extends JFrame {
    private final User loggedInUser;
    private final DataStore dataStore = DataStore.getInstance();

    public UserDashboard(User user) {
        this.loggedInUser = user;
        setTitle("BDMS - " + user.getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        Theme.applyGlobalDefaults();
        getContentPane().setBackground(Theme.BG_DARK);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createDashboardPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel welcome = new JLabel("Welcome, " + loggedInUser.getFullName());
        welcome.setFont(Theme.FONT_TITLE);
        welcome.setForeground(Theme.TEXT_PRIMARY);

        JLabel roleLabel = new JLabel("Role: " + loggedInUser.getRole());
        roleLabel.setFont(Theme.FONT_BODY);
        roleLabel.setForeground(Theme.TEXT_SECONDARY);

        JButton logout = Theme.secondaryButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        JPanel left = new JPanel(new GridLayout(0, 1, 4, 4));
        left.setOpaque(false);
        left.add(welcome);
        left.add(roleLabel);

        header.add(left, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        return header;
    }

    private Component createDashboardPanel() {
        switch (loggedInUser.getRole()) {
            case DONOR:
                return createDonorView((Donor) loggedInUser);
            case DOCTOR:
                return createDoctorView((Doctor) loggedInUser);
            case HOSPITAL_STAFF:
                return createHospitalStaffView((Hospital) loggedInUser);
            case ADMIN:
                return createAdminView();
            case TESTER:
                return createTesterView(loggedInUser);
            default:
                return createAdminView();
        }
    }

    private JPanel createAdminView() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.BG_DARK);
        tabs.setForeground(Theme.TEXT_PRIMARY);

        tabs.addTab("Overview", createAdminOverview());
        tabs.addTab("Users", createAdminUsersPanel());
        tabs.addTab("Requests", createAdminRequestsPanel());
        tabs.addTab("Inventory", createAdminInventoryPanel());
        tabs.addTab("Notify Donor", createAdminNotifyPanel());
        tabs.addTab("Appointments", createAdminAppointmentsPanel());

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(tabs, BorderLayout.CENTER);
        return container;
    }

    private JPanel createAdminOverview() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 24, 24));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        panel.add(createStatCard("Total Users", String.valueOf(dataStore.getAllUsers().size())));
        panel.add(createStatCard("Available Units", String.valueOf(dataStore.getAvailableUnits().size())));
        panel.add(createStatCard("Open Requests", String.valueOf(dataStore.getAllBloodRequests().size())));

        return panel;
    }
//function to create a new user
    private JPanel createAdminUsersPanel() {
        String[] columns = {"ID", "Name", "Email", "Role", "Active"};
        List<User> users = dataStore.getAllUsers();
        Object[][] rows = new Object[users.size()][columns.length];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            rows[i] = new Object[] {u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.isActive()};
        }
        return createTabbedContentPanel("System Users", columns, rows);
    }
// to register a blood type
    private JPanel createAdminRequestsPanel() {
        String[] columns = {"ID", "Hospital", "Blood Type", "Component", "Quantity", "Urgency", "Status"};
        List<BloodRequest> requests = dataStore.getAllBloodRequests();
        Object[][] rows = new Object[requests.size()][columns.length];
        for (int i = 0; i < requests.size(); i++) {
            BloodRequest req = requests.get(i);
            rows[i] = new Object[] {
                req.getId(), req.getHospitalName(), Donor.bloodTypeDisplay(req.getBloodType()),
                BloodUnit.componentDisplay(req.getComponent()), req.getQuantityMl(), req.getUrgency(), req.getStatus()
            };
        }
        return createTabbedContentPanel("Request Management", columns, rows);
    }

    private JPanel createAdminInventoryPanel() {
        String[] columns = {"Blood Unit", "Type", "Component", "Status", "Expiry", "Location"};
        List<BloodUnit> units = dataStore.getAllBloodUnits();
        Object[][] rows = new Object[units.size()][columns.length];
        for (int i = 0; i < units.size(); i++) {
            BloodUnit unit = units.get(i);
            rows[i] = new Object[] {
                unit.getId(), Donor.bloodTypeDisplay(unit.getBloodType()), BloodUnit.componentDisplay(unit.getComponent()),
                unit.getStatus(), unit.getExpiryDate(), unit.getStorageLocation()
            };
        }
        return createTabbedContentPanel("Blood Inventory", columns, rows);
    }

    private JPanel createAdminNotifyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 10, 0);

        JLabel heading = new JLabel("Notify Donor");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);
        panel.add(heading, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Select Donor:"), gbc);

        List<Donor> donors = dataStore.getAllDonors();
        JComboBox<Donor> donorCombo = Theme.comboBox();
        donorCombo.setPreferredSize(new Dimension(340, 32));
        for (Donor donor : donors) {
            donorCombo.addItem(donor);
        }

        gbc.gridy++;
        panel.add(donorCombo, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Notification Title:"), gbc);
        JTextField titleField = Theme.field();
        titleField.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(titleField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Message:"), gbc);
        JTextArea messageArea = Theme.textArea();
        messageArea.setRows(5);
        messageArea.setPreferredSize(new Dimension(340, 100));
        gbc.gridy++;
        panel.add(Theme.scrollPane(messageArea), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Type:"), gbc);
        JComboBox<String> typeCombo = Theme.comboBox();
        typeCombo.addItem("INFO");
        typeCombo.addItem("WARNING");
        typeCombo.addItem("URGENT");
        typeCombo.addItem("SUCCESS");
        typeCombo.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(typeCombo, gbc);

        JLabel status = new JLabel(" ");
        status.setForeground(Theme.ACCENT_RED);
        status.setFont(Theme.FONT_SMALL);
        gbc.gridy++;
        panel.add(status, gbc);

        JButton send = Theme.primaryButton("Send Notification");
        gbc.gridy++;
        panel.add(send, gbc);

        send.addActionListener(e -> {
            Donor selectedDonor = (Donor) donorCombo.getSelectedItem();
            String title = titleField.getText().trim();
            String message = messageArea.getText().trim();
            String typeText = (String) typeCombo.getSelectedItem();

            if (selectedDonor == null || title.isEmpty() || message.isEmpty()) {
                status.setText("Please fill in all fields before sending.");
                return;
            }

            Notification.Type type = Notification.Type.valueOf(typeText);
            Notification note = new Notification("N" + dataStore.nextId(), selectedDonor.getId(), title, message, type);
            dataStore.addNotification(note);
            status.setForeground(Theme.GREEN);
            status.setText("Notification sent to " + selectedDonor.getFullName() + ".");
            titleField.setText("");
            messageArea.setText("");
        });

        return panel;
    }

    private JPanel createAdminAppointmentsPanel() {
        String[] columns = {"ID", "Donor", "Scheduled", "Location", "Status"};
        List<Appointment> list = dataStore.getAllAppointments();
        Object[][] rows = new Object[list.size()][columns.length];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (int i = 0; i < list.size(); i++) {
            Appointment appointment = list.get(i);
            rows[i] = new Object[] {
                appointment.getId(), appointment.getDonorId(),
                appointment.getScheduledTime().format(formatter), appointment.getLocation(), appointment.getStatus()
            };
        }
        return createTabbedContentPanel("Appointments", columns, rows);
    }

    private JPanel createDoctorView(Doctor doctor) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);
        tabs.setBackground(Theme.BG_DARK);

        tabs.addTab("Requests", createAdminRequestsPanel());
        tabs.addTab("Appointments", createAdminAppointmentsPanel());
        tabs.addTab("Lab Testing", createDoctorTestingPanel(doctor));
        tabs.addTab("Profile", createDoctorProfilePanel(doctor));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDoctorProfilePanel(Doctor doctor) {
        JPanel profile = Theme.card();
        profile.setLayout(new GridLayout(0, 1, 10, 10));
        profile.add(createDetailLabel("Name", doctor.getFullName()));
        profile.add(createDetailLabel("Specialisation", doctor.getSpecialisation()));
        profile.add(createDetailLabel("Hospital", doctor.getHospitalAffiliation()));
        profile.add(createDetailLabel("License", doctor.getLicenseNumber()));
        profile.add(createDetailLabel("Email", doctor.getEmail()));
        return profile;
    }

    private JPanel createDoctorTestingPanel(Doctor doctor) {
        String[] columns = {"ID", "Donor", "Type", "Component", "Volume", "Collected", "Expiry", "Tested", "Status"};
        List<BloodUnit> bloodUnits = dataStore.getAllBloodUnits();
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        for (BloodUnit unit : bloodUnits) {
            model.addRow(new Object[] {
                unit.getId(), unit.getDonorId(), Donor.bloodTypeDisplay(unit.getBloodType()),
                BloodUnit.componentDisplay(unit.getComponent()), unit.getVolumeMl(), unit.getCollectionDate(),
                unit.getExpiryDate(), unit.isTested() ? "Yes" : "No", unit.getStatus()
            });
        }

        JTable table = Theme.styledTable(model);
        JScrollPane scroll = Theme.scrollPane(table);
        scroll.setBorder(null);

        JButton markTested = Theme.primaryButton("Mark Selected Unit Tested");
        JLabel status = new JLabel("Select a blood unit and mark it tested.");
        status.setForeground(Theme.TEXT_SECONDARY);
        status.setFont(Theme.FONT_BODY);

        markTested.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    status.setText("Please select a blood unit first.");
                    return;
                }
                BloodUnit unit = bloodUnits.get(selectedRow);
                if (unit.isTested()) {
                    status.setText("This blood unit has already been tested.");
                    return;
                }
                unit.setTested(true);
                unit.setStatus(BloodUnit.Status.AVAILABLE);
                model.setValueAt("Yes", selectedRow, 7);
                model.setValueAt(unit.getStatus(), selectedRow, 8);
                status.setText("Unit " + unit.getId() + " has been marked as tested.");
            }
        });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setOpaque(false);
        controls.add(markTested);
        controls.add(status);

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHospitalProfilePanel(Hospital hospital) {
        JPanel profile = Theme.card();
        profile.setLayout(new GridLayout(0, 1, 10, 10));
        profile.add(createDetailLabel("Hospital", hospital.getHospitalName()));
        profile.add(createDetailLabel("Contact", hospital.getFullName()));
        profile.add(createDetailLabel("Email", hospital.getEmail()));
        profile.add(createDetailLabel("Phone", hospital.getPhone()));
        profile.add(createDetailLabel("Verified", hospital.isVerified() ? "Yes" : "No"));
        return profile;
    }

    private JPanel createHospitalRequestsPanel(Hospital hospital) {
        String[] columns = {"ID", "Blood Type", "Component", "Quantity", "Urgency", "Status", "Patient"};
        List<BloodRequest> requests = dataStore.getRequestsForHospital(hospital.getId());
        Object[][] rows = new Object[requests.size()][columns.length];
        for (int i = 0; i < requests.size(); i++) {
            BloodRequest request = requests.get(i);
            rows[i] = new Object[] {
                request.getId(), Donor.bloodTypeDisplay(request.getBloodType()),
                BloodUnit.componentDisplay(request.getComponent()), request.getQuantityMl(),
                request.getUrgency(), request.getStatus(), request.getPatientName()
            };
        }
        return createTabbedContentPanel("Hospital Requests", columns, rows);
    }

    private JPanel createHospitalStaffView(Hospital hospital) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);
        tabs.setBackground(Theme.BG_DARK);

        tabs.addTab("My Hospital", createHospitalProfilePanel(hospital));
        tabs.addTab("Requests", createHospitalRequestsPanel(hospital));
        tabs.addTab("Request Blood", createBloodRequestFormPanel(hospital));
        tabs.addTab("Inventory", createAdminInventoryPanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDonorView(Donor donor) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);
        tabs.setBackground(Theme.BG_DARK);

        tabs.addTab("Profile", createDonorProfilePanel(donor));
        tabs.addTab("Appointments", createDonorAppointmentsPanel(donor));
        tabs.addTab("Donations", createDonorDonationPanel(donor));
        tabs.addTab("Donate Blood", createDonorDonatePanel(donor));
        tabs.addTab("Request Blood", createBloodRequestFormPanel(donor));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDonorProfilePanel(Donor donor) {
        JPanel profile = Theme.card();
        profile.setLayout(new BorderLayout(0, 16));

        JPanel details = new JPanel(new GridLayout(0, 1, 10, 10));
        details.setOpaque(false);
        details.add(createDetailLabel("Name", donor.getFullName()));
        details.add(createDetailLabel("Blood Type", Donor.bloodTypeDisplay(donor.getBloodType())));
        details.add(createDetailLabel("Gender", donor.getGender().toString()));
        details.add(createDetailLabel("Last Donation", donor.getLastDonationDate() != null ? donor.getLastDonationDate().toString() : "Never"));
        details.add(createDetailLabel("Eligibility", donor.isEligible() ? "Eligible" : "Not eligible"));
        details.add(createDetailLabel("Medical History", donor.getMedicalHistory()));

        JButton updateHistory = Theme.secondaryButton("Update Medical History");
        updateHistory.addActionListener(e -> {
            String newHistory = JOptionPane.showInputDialog(this, "Update your medical history:", donor.getMedicalHistory());
            if (newHistory != null) {
                donor.setMedicalHistory(newHistory.trim());
                JOptionPane.showMessageDialog(this, "Medical history updated.", "Saved", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(() -> new UserDashboard(donor).setVisible(true));
            }
        });

        profile.add(details, BorderLayout.CENTER);
        profile.add(updateHistory, BorderLayout.SOUTH);
        return profile;
    }

    private JPanel createDonorDonatePanel(Donor donor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 0);

        panel.add(new JLabel("Donation Volume (mL):"), gbc);
        JTextField volumeField = Theme.field();
        volumeField.setPreferredSize(new Dimension(280, 32));
        gbc.gridy++;
        panel.add(volumeField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        JTextField locationField = Theme.field();
        locationField.setPreferredSize(new Dimension(280, 32));
        gbc.gridy++;
        panel.add(locationField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Component:"), gbc);
        JComboBox<BloodUnit.Component> componentCombo = Theme.comboBox();
        for (BloodUnit.Component component : BloodUnit.Component.values()) {
            componentCombo.addItem(component);
        }
        componentCombo.setPreferredSize(new Dimension(280, 32));
        gbc.gridy++;
        panel.add(componentCombo, gbc);

        gbc.gridy++;
        JLabel status = new JLabel("Enter your donation details and submit.");
        status.setForeground(Theme.TEXT_SECONDARY);
        panel.add(status, gbc);

        JButton donateButton = Theme.primaryButton("Donate Blood");
        gbc.gridy++;
        panel.add(donateButton, gbc);

        donateButton.addActionListener(e -> {
            String volumeText = volumeField.getText().trim();
            String location = locationField.getText().trim();
            BloodUnit.Component component = (BloodUnit.Component) componentCombo.getSelectedItem();

            if (volumeText.isEmpty() || location.isEmpty() || component == null) {
                status.setForeground(Theme.ACCENT_RED);
                status.setText("Complete all donation fields before submitting.");
                return;
            }

            try {
                double volume = Double.parseDouble(volumeText);
                DonationRecord record = donor.donateBlood(dataStore, volume, location, component);
                if (record == null) {
                    status.setForeground(Theme.ACCENT_RED);
                    status.setText("You are not currently eligible to donate.");
                } else {
                    status.setForeground(Theme.GREEN);
                    status.setText("Donation recorded successfully.");
                }
            } catch (NumberFormatException ex) {
                status.setForeground(Theme.ACCENT_RED);
                status.setText("Volume must be a valid number.");
            }
        });

        return panel;
    }

    private JPanel createBloodRequestFormPanel(User user) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel heading = new JLabel("Request Blood");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);
        panel.add(heading, gbc);

        List<Hospital> hospitals = dataStore.getAllHospitals();
        JComboBox<Hospital> hospitalCombo = Theme.comboBox();
        hospitalCombo.setPreferredSize(new Dimension(340, 32));
        for (Hospital hospital : hospitals) {
            hospitalCombo.addItem(hospital);
        }
        if (user instanceof Hospital) {
            hospitalCombo.setSelectedItem(user);
            hospitalCombo.setEnabled(false);
        }

        gbc.gridy++;
        panel.add(new JLabel("Hospital:"), gbc);
        gbc.gridy++;
        panel.add(hospitalCombo, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Blood Type:"), gbc);
        JComboBox<String> bloodTypeCombo = Theme.comboBox();
        for (Donor.BloodType type : Donor.BloodType.values()) {
            bloodTypeCombo.addItem(Donor.bloodTypeDisplay(type));
        }
        bloodTypeCombo.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(bloodTypeCombo, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Component:"), gbc);
        JComboBox<BloodUnit.Component> componentCombo = Theme.comboBox();
        for (BloodUnit.Component component : BloodUnit.Component.values()) {
            componentCombo.addItem(component);
        }
        componentCombo.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(componentCombo, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Quantity (mL):"), gbc);
        JTextField quantityField = Theme.field();
        quantityField.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(quantityField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Urgency:"), gbc);
        JComboBox<BloodRequest.Urgency> urgencyCombo = Theme.comboBox();
        for (BloodRequest.Urgency urgency : BloodRequest.Urgency.values()) {
            urgencyCombo.addItem(urgency);
        }
        urgencyCombo.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(urgencyCombo, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Required By (YYYY-MM-DD):"), gbc);
        JTextField requiredByField = Theme.field();
        requiredByField.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(requiredByField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Patient Name:"), gbc);
        JTextField patientField = Theme.field();
        patientField.setPreferredSize(new Dimension(340, 32));
        gbc.gridy++;
        panel.add(patientField, gbc);

        JLabel status = new JLabel("Fill in the request details and submit.");
        status.setForeground(Theme.TEXT_SECONDARY);
        status.setFont(Theme.FONT_BODY);
        gbc.gridy++;
        panel.add(status, gbc);

        JButton submit = Theme.primaryButton("Submit Request");
        gbc.gridy++;
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            Hospital hospital = (Hospital) hospitalCombo.getSelectedItem();
            String bloodType = (String) bloodTypeCombo.getSelectedItem();
            BloodUnit.Component component = (BloodUnit.Component) componentCombo.getSelectedItem();
            String quantityText = quantityField.getText().trim();
            BloodRequest.Urgency urgency = (BloodRequest.Urgency) urgencyCombo.getSelectedItem();
            String requiredByText = requiredByField.getText().trim();
            String patientName = patientField.getText().trim();

            if (hospital == null || bloodType == null || component == null || quantityText.isEmpty() || requiredByText.isEmpty() || patientName.isEmpty()) {
                status.setForeground(Theme.ACCENT_RED);
                status.setText("Please complete all fields.");
                return;
            }
            try {
                double quantity = Double.parseDouble(quantityText);
                LocalDate requiredBy = LocalDate.parse(requiredByText);
                String requestId = "REQ" + dataStore.nextId();
                BloodRequest request = new BloodRequest(requestId, hospital.getId(), hospital.getHospitalName(), Donor.fromDisplay(bloodType), component, quantity, urgency, requiredBy, patientName);
                dataStore.addBloodRequest(request);
                dataStore.addNotification(new Notification("N" + dataStore.nextId(), hospital.getId(), "New Blood Request", "A new request " + requestId + " has been submitted.", Notification.Type.INFO));
                status.setForeground(Theme.GREEN);
                status.setText("Request submitted successfully.");
                quantityField.setText("");
                requiredByField.setText("");
                patientField.setText("");
            } catch (NumberFormatException ex) {
                status.setForeground(Theme.ACCENT_RED);
                status.setText("Quantity must be a number.");
            } catch (DateTimeParseException ex) {
                status.setForeground(Theme.ACCENT_RED);
                status.setText("Required by date must be YYYY-MM-DD.");
            }
        });

        return panel;
    }

    private JPanel createDonorAppointmentsPanel(Donor donor) {
        String[] columns = {"ID", "Scheduled", "Location", "Status"};
        List<Appointment> appointments = dataStore.getAppointmentsForDonor(donor.getId());
        Object[][] rows = new Object[appointments.size()][columns.length];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            rows[i] = new Object[] {
                appointment.getId(), appointment.getScheduledTime().format(formatter), appointment.getLocation(), appointment.getStatus()
            };
        }
        return createTabbedContentPanel("My Appointments", columns, rows);
    }

    private JPanel createDonorDonationPanel(Donor donor) {
        String[] columns = {"ID", "Date", "Volume", "Location", "Blood Type"};
        List<DonationRecord> records = dataStore.getDonationRecordsForDonor(donor.getId());
        Object[][] rows = new Object[records.size()][columns.length];
        for (int i = 0; i < records.size(); i++) {
            DonationRecord record = records.get(i);
            rows[i] = new Object[] {
                record.getId(), record.getDonationDate().toLocalDate(), record.getVolumeMl(), record.getLocation(), Donor.bloodTypeDisplay(record.getBloodType())
            };
        }
        return createTabbedContentPanel("My Donation History", columns, rows);
    }

    private JPanel createTesterView(User tester) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);
        tabs.setBackground(Theme.BG_DARK);
        tabs.addTab("Blood Units", createAdminInventoryPanel());
        tabs.addTab("Requests", createAdminRequestsPanel());
        tabs.addTab("Overview", createAdminOverview());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 8));
        JLabel label = new JLabel(title);
        label.setForeground(Theme.TEXT_SECONDARY);
        label.setFont(Theme.FONT_BODY);

        JLabel number = new JLabel(value);
        number.setForeground(Theme.ACCENT_RED);
        number.setFont(Theme.FONT_BIG_NUM);

        card.add(label, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);
        return card;
    }

    private JLabel createDetailLabel(String name, String value) {
        JLabel label = new JLabel(name + ": " + value);
        label.setForeground(Theme.TEXT_PRIMARY);
        label.setFont(Theme.FONT_BODY);
        return label;
    }

    private JPanel createTabbedContentPanel(String headingText, String[] columns, Object[][] rows) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JLabel heading = new JLabel(headingText);
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = Theme.styledTable(model);
        JScrollPane scroll = Theme.scrollPane(table);
        scroll.setBorder(null);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}
