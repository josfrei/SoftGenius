package softgenius;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JToolTip;

/**
 *
 * @author Grupo1 - 1º DAM Colexio Karbo
 */
public class LeftPanelToolTip extends JToolTip{
    Font fuenteTT = new Font("Arial", Font.PLAIN, 14); 

    public LeftPanelToolTip(JComponent component, Color colorFondo, Color colorLetra) {
        super();
        setFont(fuenteTT);
        setComponent(component);
        setBackground(colorFondo);
        setForeground(colorLetra);
    }
}

