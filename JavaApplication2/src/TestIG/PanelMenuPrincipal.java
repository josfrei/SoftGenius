/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Iago
 */
// Finalizar left panel
// Realizar panel inferior
// Añadir detalles

public class PanelMenuPrincipal extends JPanel{
    private LeftPanelMP leftPanelMP;
    private BottomPanelMP bottomPanelMP;

     public PanelMenuPrincipal() {
        Style();
        setLayout(new BorderLayout());
        leftPanelMP = new LeftPanelMP();
        add(leftPanelMP, BorderLayout.WEST);

        bottomPanelMP = new BottomPanelMP();
        add(bottomPanelMP, BorderLayout.SOUTH);
        
        
        setComponentZOrder(leftPanelMP, 0);// Coloca panelIzquierdo encima del inferior
        this.setVisible(true);
    }
    
    private void Style(){
        this.setBackground(Color.WHITE);
    }
    
    //Método para añadir panel Lateral izquierdo
    
    //Método para añadir barra inferior

}
