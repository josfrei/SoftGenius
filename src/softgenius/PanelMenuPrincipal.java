package softgenius;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JPanel;

/**
 * En esta clase llamamos a los elementos que forman el panel general.
 * Las versiones del panel izquierdo se gestionan aquí.
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */

public class PanelMenuPrincipal extends JPanel {
    private final JPanel panelAuxiliar;
    private final LeftPanelMPv2 leftPanelMP;
    private final PanelCentral panelCentral;
    
/**
     * Constructor de la clase PanelMenuPrincipal.
     * 
     * Inicializa los componentes del panel general, configura su diseño y estilo,
     * y añade los paneles auxiliares y el panel izquierdo.
     * 
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
     public PanelMenuPrincipal() throws IOException, SQLException{
        panelAuxiliar = new JPanel(); //Quedará por debajo del MP, como una capa
        panelCentral = new PanelCentral();
        leftPanelMP = new LeftPanelMPv2(panelCentral);
        Style();
        setLayout(new BorderLayout());
        
        panelAuxiliar.add(panelCentral,BorderLayout.CENTER);
        //Añadimos al panelMP 
        add(panelAuxiliar,BorderLayout.CENTER);
        add(leftPanelMP, BorderLayout.WEST);
        
        
        this.setVisible(true);
    }
    /**
     * Aplica el estilo al panel principal.
     * 
     * Establece el fondo del panel y el diseño del panel auxiliar.
     */
    private void Style(){
        this.setBackground(Color.WHITE);
        panelAuxiliar.setLayout(new BorderLayout());
    }

}
