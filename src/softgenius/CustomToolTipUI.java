package softgenius;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicToolTipUI;

/**
 * Clase personalizada para el UI del tooltip sin fondo ni borde. Extiende
 * BasicToolTipUI para personalizar la apariencia del tooltip. Se encarga de
 * pintar el texto del tooltip y definir su tamaño preferido y mínimo.
 *
 * @author Gurpo 1 - 1º DAM Colexio Karbo
 */
public class CustomToolTipUI extends BasicToolTipUI {

    /**
     * Método para pintar el tooltip.
     *
     * @param g El objeto Graphics utilizado para pintar el tooltip.
     * @param c El componente que representa el tooltip.
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.BLACK);
        String text = ((JToolTip) c).getTipText();
        int x = 5;
        int y = 5;
        int width = c.getWidth() - 10;
        int height = c.getHeight() - 10;
        drawString(g2d, text, x, y, width, height);
    }

    /**
     * Método para dibujar el texto del tooltip.
     *
     * @param g El objeto Graphics2D utilizado para dibujar el texto.
     * @param text El texto del tooltip.
     * @param x La coordenada x de inicio del texto.
     * @param y La coordenada y de inicio del texto.
     * @param width El ancho disponible para el texto.
     * @param height La altura disponible para el texto.
     */
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

    /**
     * Método para obtener el tamaño preferido del tooltip.
     *
     * @param c El componente que representa el tooltip.
     * @return El tamaño preferido del tooltip.
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        String tipText = ((JToolTip) c).getTipText();
        FontMetrics fm = c.getFontMetrics(c.getFont());
        int width = Math.max(fm.stringWidth(tipText), 50);
        int height = fm.getHeight() + 10;
        return new Dimension(width, height);
    }

    /**
     * Método para obtener el tamaño mínimo del tooltip.
     *
     * @param c El componente que representa el tooltip.
     * @return El tamaño mínimo del tooltip.
     */
    @Override
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
}
