/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
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
    private final LeftPanelMPv3 leftPanelMP;
    //private final BottomPanelMP bottomPanelMP;
    private final PanelCentral panelCentral;
    

     public PanelMenuPrincipal() throws IOException{
        panelAuxiliar = new JPanel(); //Quedará por debajo del MP, como una capa
        //bottomPanelMP = new BottomPanelMP();
        panelCentral = new PanelCentral();
        leftPanelMP = new LeftPanelMPv3(panelCentral);
        Style();
        setLayout(new BorderLayout());
        
        //panelAuxiliar.add(bottomPanelMP,BorderLayout.SOUTH); //Se elimina momentaneamente el uso del panel inferior
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

}
