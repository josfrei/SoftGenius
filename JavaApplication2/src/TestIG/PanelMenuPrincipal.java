/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Usuario
 */
// Finalizar left panel
// Realizar panel inferior
// Añadir detalles

public class PanelMenuPrincipal extends JPanel{
    JPanel panelIzquierdo = new LeftPanelMP();
    
    public PanelMenuPrincipal(){
        Style();
        setLayout(new BorderLayout());
        this.add(panelIzquierdo, BorderLayout.WEST);
        this.setVisible(true);
    }
    
    private void Style(){
        this.setBackground(Color.WHITE);
    }
    
    //Método para añadir panel Lateral izquierdo
    
    //Método para añadir barra inferior
    
    
}
