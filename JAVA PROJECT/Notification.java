

import java.time.LocalDateTime;

public class Notification {
    public enum Type { INFO, WARNING, URGENT, SUCCESS }

    private String id;
    private String userId;
    private String title;
    private String message;
    private Type type;
    private boolean read;
    private LocalDateTime timestamp;

    public Notification(String id, String userId, String title, String message, Type type) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = false;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Type getType() { return type; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getTimestamp() { return timestamp; }
}