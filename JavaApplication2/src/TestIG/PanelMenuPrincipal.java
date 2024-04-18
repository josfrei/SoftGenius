/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Iago
 */
// Finalizar left panel
// Realizar panel inferior
// Añadir detalles

public class PanelMenuPrincipal extends JPanel {
    private final JPanel panelAuxiliar;
    private final LeftPanelMPv2 leftPanelMP;
    private final BottomPanelMP bottomPanelMP;
    

     public PanelMenuPrincipal() throws IOException{
        panelAuxiliar = new JPanel(); //Quedará por debajo del MP, como una capa
        leftPanelMP = new LeftPanelMPv2();
        bottomPanelMP = new BottomPanelMP();
        Style();
        setLayout(new BorderLayout());
        
        panelAuxiliar.add(bottomPanelMP,BorderLayout.SOUTH);
        add(panelAuxiliar,BorderLayout.CENTER);
        add(leftPanelMP, BorderLayout.WEST);
        
        
        this.setVisible(true);
    }
    
    private void Style(){
        this.setBackground(Color.WHITE);
        panelAuxiliar.setLayout(new BorderLayout());
    }
    
    //Método para añadir panel Lateral izquierdo
    
    //Método para añadir barra inferior

}
