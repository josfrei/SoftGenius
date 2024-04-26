/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

// Importamos la tabla Empleados
import tablas.tablaEmpleados;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PanelCentralPrincipal extends JPanel implements ActionListener {

    private JPanel PC_Ventas = new JPanel();
    private JPanel PC_Personal = new JPanel();
    private JPanel PC_Stock = new JPanel();
    private JPanel placeholder1 = new JPanel();
    private JPanel placeholder2 = new JPanel();
    private JPanel PC_VentasTab1 = new JPanel();// Cambiar al panel de la clase de adrián ej: new tablaPanelVentas1();
    private JButton btnCambiarIdioma;

    private Connection conexionBBDD;

// Creamos una instancia del panel tablaEmpleados
    tablaEmpleados panelTablaEmpleados = new tablaEmpleados();

    public PanelCentralPrincipal(int opcion) {
        Style();
        addSubPanels(opcion);
        try {
            conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");
            idiomaActual = obtenerIdiomaActual();
            actualizarIdioma(idiomaActual);
        } catch (SQLException ex) {
            // Manejar la excepción adecuadamente
            ex.printStackTrace();
        }
    }

    // Estilos para el panel central principal
    private void Style() {
        this.setBackground(Color.red);
        setLayout(new GridBagLayout());

        setVisible(true);
    }

    // Add sub-panels based on the provided option
    private void addSubPanels(int opcion) {
        removeAll();
        revalidate();
        repaint();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Occupy all available horizontal space
        gbc.weighty = 1.0; // Occupy all available vertical space
        switch (opcion) {
            case 0:
                // Placeholder for future implementation
                add(placeholder1, gbc);
                break;
            case 1:
                PCVentas();
                add(PC_Ventas, gbc);
                break;
            case 2:
                PCPersonal();
                add(PC_Personal, gbc);
                break;
            case 3:
                PCStock();
                add(PC_Stock, gbc);
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
        //Add JTabbedPanes to the PC_Ventas panel
        PC_Ventas.setLayout(new BorderLayout());
        PC_Ventas.add(ventas1, BorderLayout.CENTER);
    }

    private void PCPersonal() {
        this.setBackground(Color.BLACK);
        JTabbedPane personal1 = new JTabbedPane();

        // Add components to the JTabbedPanes
        personal1.addTab("Personal 1", panelTablaEmpleados);
        personal1.addTab("Personal 2", new JPanel());

        // Agregar contenido a la segunda pestaña --> Aquí agregamos los paneles generados por Adrián
        //JLabel label1 = new JLabel("Contenido de la pestaña 1");
        JLabel label2 = new JLabel("Contenido de la pestaña 2");
        //personal1.setComponentAt(0, label1);
        personal1.setComponentAt(1, label2);
        // Add JTabbedPanes to the PC_Ventas panel
        PC_Personal.setLayout(new BorderLayout());
        PC_Personal.add(personal1, BorderLayout.CENTER);
    }

    private void PCStock() {
        this.setBackground(Color.BLACK);
        JTabbedPane Stock1 = new JTabbedPane();

        // Add components to the JTabbedPanes
        Stock1.addTab("Stock 1", new JPanel());
        Stock1.addTab("Stock 2", new JPanel());

        //Creamos panel para dentro del stock1, index 0
        JPanel panel = new JPanel();

        // Agregar contenido a la segunda pestaña --> Aquí agregamos los paneles generados por Adrián
        btnCambiarIdioma = new JButton("Cambiar idioma");
        btnCambiarIdioma.addActionListener((ActionListener) this);
        JLabel label2 = new JLabel("Contenido de la pestaña 2");

        //Añadimos el botón al panel
        panel.add(btnCambiarIdioma);

        Stock1.setComponentAt(0, panel);
        Stock1.setComponentAt(1, label2);
        // Add JTabbedPanes to the PC_Ventas panel
        PC_Stock.setLayout(new BorderLayout());
        PC_Stock.add(Stock1, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCambiarIdioma) {
            
            //Lógica para cambiar el idioma e insertarlo en la bbdd
        }
    }
    //************************************************************************//
    // Cambio de Idioma
    //************************************************************************//
    private String idiomaActual = "Spanish";

    private void actualizarIdioma(String idioma) {
        // Cargar el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Indicamos lo que cambiamos y referenciamos
        //XXXXXXXX.setText(resourceBundle.getString("label1"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas(conexionBBDD);
        return idiomas.obtenerIdiomaActual();
    }
}
