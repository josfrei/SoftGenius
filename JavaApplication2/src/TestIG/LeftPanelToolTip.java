/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JToolTip;

/**
 *
 * @author Usuario
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

