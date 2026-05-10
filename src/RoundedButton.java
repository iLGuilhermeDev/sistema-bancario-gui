import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius = 20;
    private Color normalColor = new Color(41, 128, 185);
    private Color hoverColor = new Color(52, 152, 219);

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g2.setColor(normalColor.darker());
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }

    public void setCustomColors(Color normal, Color hover) {
        this.normalColor = normal;
        this.hoverColor = hover;
    }
}