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
    private final PanelCentral panelCentral;
    

     public PanelMenuPrincipal() throws IOException{
        panelAuxiliar = new JPanel(); //Quedará por debajo del MP, como una capa
        bottomPanelMP = new BottomPanelMP();
        panelCentral = new PanelCentral();
        leftPanelMP = new LeftPanelMPv2(panelCentral);
        Style();
        setLayout(new BorderLayout());
        
        panelAuxiliar.add(bottomPanelMP,BorderLayout.SOUTH); //Se añade al panelAuxiliar para que no se superponga al panel izquierdo
        panelAuxiliar.add(panelCentral,BorderLayout.CENTER);
        //Añadimos al panelMP 
        add(panelAuxiliar,BorderLayout.CENTER);
        add(leftPanelMP, BorderLayout.WEST);
        
        
        this.setVisible(true);
    }
    
    private void Style(){
        this.setBackground(Color.WHITE);
        panelAuxiliar.setLayout(new BorderLayout());
    }
    
    //añadir panel Lateral izquierdo


}
