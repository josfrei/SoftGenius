package TestIG;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Usuario
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;

// Clase personalizada para el UI del tooltip sin fondo ni borde
public class CustomToolTipUI extends BasicToolTipUI {

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the text color of the tooltip
        g2d.setPaint(Color.BLACK);

        // Draw the tooltip text
        String text = ((JToolTip) c).getTipText();
        int x = 5;
        int y = 5;
        int width = c.getWidth() - 10;
        int height = c.getHeight() - 10;
        drawString(g2d, text, x, y, width, height);
    }

    private void drawString(Graphics2D g, String text, int x, int y, int width, int height) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getHeight();

        if (textWidth > width) {
            String textLine1 = text.substring(0, text.length() / 2);
            String textLine2 = text.substring(text.length() / 2);
            textWidth = g.getFontMetrics().stringWidth(textLine1);
            int textHeightLine1 = g.getFontMetrics().getHeight();
            int textHeightLine2 = g.getFontMetrics().getHeight();
            y = y + (height - textHeightLine1 - textHeightLine2) / 2;
            g.drawString(textLine1, x, y);
            g.drawString(textLine2, x, y + textHeightLine2);
        } else {
            y = y + (height - textHeight) / 2;
            g.drawString(text, x, y);
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        String tipText = ((JToolTip) c).getTipText();
        FontMetrics fm = c.getFontMetrics(c.getFont());
        int width = Math.max(fm.stringWidth(tipText), 50);
        int height = fm.getHeight() + 10;
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
}