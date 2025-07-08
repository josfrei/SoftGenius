package copiasSeguridad;

import static BasesDatosNoRelacionales.DB4OMetodos.RelizarCopiaBaseDatosSQLtoDB4O;
import static BasesDatosNoRelacionales.ParadoxMetodos.RealizarCopiaGeneralSoftgenius;
import softgenius.ConexionBD;
import com.mysql.cj.jdbc.DatabaseMetaData;
import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JPanel;
import static libsql3.softgenius.GestorTablas.obtenerNombresTablas;

/**
 * Esta clase gestiona la creación de copias de seguridad local. Por una parte
 * da la opción de hacer copia de una tabla en concreto y usando db4o que es una
 * base de datos no relacional. Por otra parte hace un guardado total de las
 * bases de datos usando Paradox, que también es una base de datos no
 * relacional. A mayores, y a la vez que guarda el archivo Paradox, hace una
 * copia de seguridad total de db4o y Paradox usando un script creado para ello.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class CopiaSeguridad extends JPanel implements ActionListener {

    private JButton btnCrearCopiaIndividual;
    private JButton btnCopiaSeguridadTotal = new JButton();
    private JLabel lblCrearCopiaIndividual, lblListaBbdd, lblListaTablas, lblCopiaSeguridadTotal;
    private JPanel panel, panel1, panel2;
    private static JComboBox<String> listaBbdd;
    private JComboBox<String> comboTablas;
    private String idiomaActual = "Spanish";
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Creamos los elementos necesarios.
     */
    public CopiaSeguridad() throws SQLException {

        crearElementos();
        cargarBbdd();
        listaBbdd.addActionListener(this);
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
    }

    /**
     * Obtiene las bases de datos que constan en el sistema pero solo muestra
     * aquellos que empiezan por bbdd_ Al usuario los muestra sin bbdd_ y sin
     * _sofgenius.
     */
    private void cargarBbdd() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(" ");

        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();

        while (resultSet.next()) {
            String databaseName = resultSet.getString("TABLE_CAT");
            if (databaseName.startsWith("bbdd_")) {
                databaseName = databaseName.replaceFirst("bbdd_", "").replaceFirst("_softgenius", "");
                listaBbdd.addItem(databaseName);
            }
        }
        resultSet.close();
        conexionBBDD.close();
    }

    /**
     * Obtiene las tablas que constan en la base de datos elegida.
     */
    private void cargarTablas(String bbddSeleccionada) throws SQLException {

        btnCrearCopiaIndividual.setEnabled(true);
        comboTablas.removeAllItems();
        String db = bbddSeleccionada;
        String[] nombresTablas = obtenerNombresTablas(db);

        if (nombresTablas != null) {
            for (String nombreTabla : nombresTablas) {
                comboTablas.addItem(nombreTabla);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontraron tablas en la base de datos seleccionada.");
        }
    }

    /**
     * ***************************************************************
     */
    ///////////////////////////////////////////////////////////////////
    //////////////       ACCION DE LOS BOTONES          //////////////
    //////////////////////////////////////////////////////////////////
    /**
     * ***************************************************************
     */
    /**
     * Método para las acciones de los botones.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == listaBbdd) {
            String bbddSeleccionada = (String) listaBbdd.getSelectedItem();
            if (!"- ↓↓↓↓↓ -".equals(bbddSeleccionada)) {
                try {
                    cargarTablas("bbdd_" + bbddSeleccionada + "_softgenius");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar las tablas: " + ex.getMessage());
                }
            }
        }

        // Copia db4o
        if (e.getSource() == btnCrearCopiaIndividual) {
            String nombreBaseDeDatos = "bbdd_" + (String) listaBbdd.getSelectedItem() + "_softgenius";
            String nombreTabla = (String) comboTablas.getSelectedItem();
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "The backup is going to be performed" + nombreTabla + ". It may take a few seconds", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Se va a realizar la copia de seguridad de" + nombreTabla + ". Puede demorar unos segundos", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
            try {
                RelizarCopiaBaseDatosSQLtoDB4O(nombreBaseDeDatos, nombreTabla);
            } catch (SQLException ex) {
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "Error, copy not made", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error, copia no realizada", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            System.out.println("Copia seguridad");
        }

        // Copia Paradox y backup total
        if (e.getSource() == btnCopiaSeguridadTotal) {

            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "The backup is going to be performed. It may take a few seconds", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Se va a realizar la copia de seguridad. Puede demorar unos segundos", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
            try {
                RealizarCopiaGeneralSoftgenius();
            } catch (SQLException ex) {
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "Error, not connected to the database", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error, no conectado con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                Logger.getLogger(CopiaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Inicia copia general
            try {
                String appDataPath = System.getenv("AppData");
                String scriptPath = appDataPath + "/softgenius/4o/backup.bat";
                Process process = Runtime.getRuntime().exec(scriptPath);
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Backup done correctly", "Error", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Copia se seguridad realizada correctamente", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * **************************************************************
     */
    ///////////////////////////////////////////////////////////////////
    //////////////       CREAMOS LOS ELEMENTOS          //////////////
    //////////////////////////////////////////////////////////////////
    /**
     * ***************************************************************
     */
    private void crearElementos() {
        ColorBotones colorBotones = new ColorBotones();

        // Creamos los paneles
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel1 = new JPanel();
        panel2 = new JPanel();

        panel.setBackground(colorPanel);
        panel1.setBackground(colorPanel);
        panel2.setBackground(colorPanel);

        // Crear los botones
        btnCrearCopiaIndividual = new JButton("Crear copia");
        btnCrearCopiaIndividual.setEnabled(false);
        btnCrearCopiaIndividual.addActionListener(this);

        ImageIcon iconoCopiaSeguridad;
        iconoCopiaSeguridad = new ImageIcon("recursos/copiaSeguridad.png");
        btnCopiaSeguridadTotal.setIcon(iconoCopiaSeguridad);
        btnCopiaSeguridadTotal.setOpaque(false);
        btnCopiaSeguridadTotal.setContentAreaFilled(false);
        btnCopiaSeguridadTotal.setBorderPainted(false);
        btnCopiaSeguridadTotal.addActionListener(this);

        // Damos formato y color a los botones
        Color colorAmarillo = new Color(241, 196, 15);
        Color colorVerde = new Color(39, 174, 96);
        Color colorAzul = new Color(33, 97, 140);
        Color colorRojo = new Color(255, 0, 0);

        Color fondoBlanco = Color.white;
        JButton[] botonCrearCopia = {btnCrearCopiaIndividual};

        colorBotones.aplicarEstilosBotones(botonCrearCopia, colorVerde, fondoBlanco, colorVerde);

        lblListaBbdd = new JLabel("Escoge una BBDD");
        lblListaTablas = new JLabel("Nombre tabla");
        lblCopiaSeguridadTotal = new JLabel("CREAR COPIA DE SEGURIDAD TOTAL");
        lblCrearCopiaIndividual = new JLabel("CREAR COPIA INDIVIDUAL");

        listaBbdd = new JComboBox<>();
        listaBbdd.setBackground(colorSelect);
        listaBbdd.addItem("- ↓↓↓↓↓ -");

        comboTablas = new JComboBox<>();
        comboTablas.setBackground(colorSelect);
        comboTablas.addItem("- ↓↓↓↓↓ -");

        panel.add(panel1);
        panel.add(panel2);
        // Agregamos los componentes al panel
        panel1.add(lblCrearCopiaIndividual);
        panel1.add(lblListaBbdd);
        panel1.add(listaBbdd);
        panel1.add(lblListaTablas);
        panel1.add(comboTablas); // Agregar el comboTablas al panel
        panel1.add(btnCrearCopiaIndividual);
        panel2.add(lblCopiaSeguridadTotal);
        panel2.add(btnCopiaSeguridadTotal);

        // Agregar el panel al marco
        this.setBackground(colorPanel);
        add(panel);
        setVisible(true);
    }

    /**
     * ***************************************************************************
     * ///////////////////////////////////////////////////////////////////////////
     * ///////////////////////////////////////////////////////////////////////////
     * GESTIÓN IDIOMAS
     * ///////////////////////////////////////////////////////////////////////////
     * //////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */
    /**
     * Obteniendo el idioma actual actualiza todos los elementos en el idioma
     * elegido.
     *
     * @param idioma
     * @throws SQLException
     */
    private void actualizarIdioma(String idioma) throws SQLException {
        idiomaActual = obtenerIdiomaActual();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblListaBbdd.setText(resourceBundle.getString("lblSelectDatabase"));
        lblListaTablas.setText(resourceBundle.getString("lblCrearTabla"));
        lblCopiaSeguridadTotal.setText(resourceBundle.getString("lblCopiaSeguridadTotal"));
        lblCrearCopiaIndividual.setText(resourceBundle.getString("lblCrearCopiaIndividual"));
        btnCrearCopiaIndividual.setText(resourceBundle.getString("btnCrearCopiaIndividual"));

    }

    /**
     * Obtiene el idioma actual configurado en la aplicación.
     *
     * @return una cadena que representa el idioma actual.
     * @throws SQLException si ocurre un error al acceder a la base de datos
     * para obtener el idioma.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
