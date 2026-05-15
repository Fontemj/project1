import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    public LoginFrame() {
        setTitle("BDMS Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        Theme.applyGlobalDefaults();

        JPanel card = Theme.card();
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Sign In");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Enter your credentials to continue.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Theme.TEXT_PRIMARY);
        emailLabel.setFont(Theme.FONT_BODY);

        emailField = Theme.field();

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Theme.TEXT_PRIMARY);
        passwordLabel.setFont(Theme.FONT_BODY);

        passwordField = Theme.passwordField();

        JButton loginButton = Theme.primaryButton("Login");
        loginButton.addActionListener(new LoginAction());

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Theme.ACCENT_RED);
        statusLabel.setFont(Theme.FONT_SMALL);

        JLabel sampleLabel = new JLabel("Sample accounts: admin@bdms.org/admin123, alice@email.com/pass123");
        sampleLabel.setForeground(Theme.TEXT_SECONDARY);
        sampleLabel.setFont(Theme.FONT_SMALL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        card.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(emailLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(emailField, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(16, 0, 6, 0);
        card.add(passwordLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(passwordField, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(18, 0, 0, 0);
        card.add(loginButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(statusLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 0, 0);
        card.add(sampleLabel, gbc);

        add(card, BorderLayout.CENTER);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password.");
                return;
            }

            User user = DataStore.getInstance().authenticate(email, password);
            if (user == null) {
                statusLabel.setText("Invalid login credentials.");
                return;
            }

            SwingUtilities.invokeLater(() -> {
                dispose();
                new UserDashboard(user).setVisible(true);
            });
        }
    }
}
