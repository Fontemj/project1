import java.time.LocalDateTime;

public class User {
    public enum Role { DONOR, DOCTOR, HOSPITAL_STAFF, TESTER, ADMIN }

    private String id;
    private String fullName;
    private String email;
    private String passwordHash;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User(String id, String fullName, String email, String passwordHash, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() { return fullName + " (" + role + ")"; }
}