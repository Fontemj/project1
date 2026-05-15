

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

public class Theme {
    // ── Palette ────────────────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(0x0D1117);
    public static final Color BG_SURFACE   = new Color(0x161B22);
    public static final Color BG_CARD      = new Color(0x1C2128);
    public static final Color BG_HOVER     = new Color(0x21262D);
    public static final Color ACCENT_RED   = new Color(0xE53E3E);
    public static final Color ACCENT_ROSE  = new Color(0xF56565);
    public static final Color ACCENT_LIGHT = new Color(0xFC8181);
    public static final Color BLOOD_DARK   = new Color(0x9B2335);
    public static final Color BLOOD_MID    = new Color(0xC53030);
    public static final Color GREEN        = new Color(0x38A169);
    public static final Color YELLOW       = new Color(0xD69E2E);
    public static final Color BLUE         = new Color(0x3182CE);
    public static final Color PURPLE       = new Color(0x805AD5);
    public static final Color TEXT_PRIMARY = new Color(0xF0F6FC);
    public static final Color TEXT_SECONDARY = new Color(0x8B949E);
    public static final Color TEXT_MUTED   = new Color(0x484F58);
    public static final Color BORDER       = new Color(0x30363D);
    public static final Color BORDER_FOCUS = new Color(0xE53E3E);

    // ── Typography ─────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE    = new Font("Serif", Font.BOLD, 26);
    public static final Font FONT_HEADING  = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_SUBHEAD  = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_BODY     = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO     = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FONT_NAV      = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_BIG_NUM  = new Font("SansSerif", Font.BOLD, 32);

    // ── Helpers ────────────────────────────────────────────────────────────────
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel subheading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SUBHEAD);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    public static JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(BG_SURFACE);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(BG_SURFACE);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return f;
    }

    public static JTextArea textArea() {
        JTextArea ta = new JTextArea();
        ta.setBackground(BG_SURFACE);
        ta.setForeground(TEXT_PRIMARY);
        ta.setCaretColor(TEXT_PRIMARY);
        ta.setFont(FONT_BODY);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return ta;
    }

    public static <T> JComboBox<T> comboBox() {
        JComboBox<T> cb = new JComboBox<>();
        cb.setBackground(BG_SURFACE);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_BODY);
        cb.setBorder(new LineBorder(BORDER, 1));
        return cb;
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(BLOOD_DARK);
                else if (getModel().isRollover()) g2.setColor(ACCENT_ROSE);
                else g2.setColor(ACCENT_RED);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_SUBHEAD);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(BG_SURFACE);
                else if (getModel().isRollover()) g2.setColor(BG_HOVER);
                else g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_SUBHEAD);
        b.setForeground(TEXT_PRIMARY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = secondaryButton(text);
        b.setForeground(ACCENT_RED);
        return b;
    }

    public static JButton greenButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? GREEN.darker() : GREEN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_SUBHEAD);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        return b;
    }

    public static JScrollPane scrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBackground(BG_DARK);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_DARK);
        sp.getVerticalScrollBar().setBackground(BG_SURFACE);
        sp.getHorizontalScrollBar().setBackground(BG_SURFACE);
        return sp;
    }

    public static JTable styledTable(javax.swing.table.TableModel model) {
        JTable t = new JTable(model) {
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? BG_CARD : BG_SURFACE);
                c.setForeground(TEXT_PRIMARY);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(0xE53E3E, false).darker().darker());
                }
                return c;
            }
        };
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(FONT_BODY);
        t.setRowHeight(32);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(BLOOD_MID);
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(BG_SURFACE);
        t.getTableHeader().setForeground(TEXT_SECONDARY);
        t.getTableHeader().setFont(FONT_SUBHEAD);
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        t.setFillsViewportHeight(true);
        return t;
    }

    public static JLabel badge(String text, Color bg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(FONT_SMALL);
        l.setForeground(Color.WHITE);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(3, 8, 3, 8));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    public static Color urgencyColor(String urgency) {
        switch (urgency.toUpperCase()) {
            case "EMERGENCY": return ACCENT_RED;
            case "URGENT":    return YELLOW;
            default:          return GREEN;
        }
    }

    public static Color statusColor(String status) {
        switch (status.toUpperCase()) {
            case "APPROVED": case "DELIVERED": case "COMPLETED": case "AVAILABLE": return GREEN;
            case "PENDING": case "SCHEDULED":   return YELLOW;
            case "REJECTED": case "EXPIRED": case "DISCARDED":  return ACCENT_RED;
            case "DISPATCHED":                  return BLUE;
            case "RESERVED":                    return PURPLE;
            default: return TEXT_SECONDARY;
        }
    }

    public static void applyGlobalDefaults() {
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", BG_CARD);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.background", BG_SURFACE);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_SURFACE);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Viewport.background", BG_DARK);
    }
}