/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PanelCentralPrincipal extends JPanel {
    private JPanel PC_Ventas = new JPanel();
    private JPanel PC_Personal = new JPanel();
    private JPanel PC_Stock = new JPanel();
    private JPanel placeholder1 = new JPanel();
    private JPanel placeholder2 = new JPanel();
    private JPanel PC_VentasTab1 = new JPanel();// Cambiar al panel de la clase de adrián ej: new tablaPanelVentas1();

    public PanelCentralPrincipal(int opcion) {
        Style();
        addSubPanels(opcion);
    }

    // Estilos para el panel central principal
    private void Style() {
        this.setBackground(Color.red);
        setLayout(new GridBagLayout());
        
        setVisible(true);
    }

    // Add sub-panels based on the provided option
    private void addSubPanels(int opcion) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.anchor= GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Occupy all available horizontal space
        gbc.weighty = 1.0; // Occupy all available vertical space
        switch (opcion) {
            case 0:
                PCVentas();
                add(PC_Ventas, gbc);
                
                break;
            case 1:
                add(PC_Personal);
                
                break;
            case 2:
                add(PC_Stock);
                // Implement logic for PC_Stock panel
                break;
            default:
                // Handle unexpected option
                break;
        }
    }

    // Logic for the PC_Ventas panel
     private void PCVentas() {
        this.setBackground(Color.BLACK);
        JTabbedPane ventas1 = new JTabbedPane();


        // Add components to the JTabbedPanes
        ventas1.addTab("Ventas 1", new JPanel());
        ventas1.addTab("Ventas 2", new JPanel());
    
        // Agregar contenido a la segunda pestaña --> Aquí agregamos los paneles generados por Adrián
        JLabel label1 = new JLabel("Contenido de la pestaña 1");
        JLabel label2 = new JLabel("Contenido de la pestaña 2");
        ventas1.setComponentAt(0, label1);
        ventas1.setComponentAt(1, label2);
        // Add JTabbedPanes to the PC_Ventas panel
        PC_Ventas.setLayout(new BorderLayout());
        PC_Ventas.add(ventas1, BorderLayout.CENTER);
    }
}

