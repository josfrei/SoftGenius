package tablas;

import com.mysql.cj.jdbc.DatabaseMetaData;
import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static libsql3.softgenius.GestorTablas.obtenerNombresColumnas;
import static libsql3.softgenius.GestorTablas.obtenerNombresTablas;
import softgenius.ConexionBD;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelCrearClaveForanea extends JPanel implements ActionListener {

    private JButton btnCrearClaveForanea;
    private JLabel lblListaBbddPK, lblListaTablasPK, lblListaBbddFK, lblListaTablasFK, lblCrearClaveForanea, lblListaPK, lblListaFK, lblClave;
    private JPanel panel, panel1, panel2;
    private static JComboBox<String> listaBbddPK;
    private static JComboBox<String> listaBbddFK;
    private JTextField txtClave;
    private JComboBox<String> listaPK;
    private JComboBox<String> listaCamposFK;
    private JComboBox<String> comboTablasPK;
    private JComboBox<String> comboTablasFK;
    private String idiomaActual = "Spanish";
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Creamos los elementos necesarios.
     */
    public PanelCrearClaveForanea() throws SQLException {

        crearElementos();
        cargarBbddPK();
        cargarBbddFK();
        listaBbddPK.addActionListener(this);
        listaBbddFK.addActionListener(this);
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
    }

    /**
     * Obtiene las bases de datos que constan en el sistema pero solo muestra
     * aquellos que empiezan por bbdd_ Al usuario los muestra sin bbdd_ y sin
     * _sofgenius.
     */
    private void cargarBbddPK() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(" ");

        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();

        while (resultSet.next()) {
            String databaseName = resultSet.getString("TABLE_CAT");
            if (databaseName.startsWith("bbdd_")) {
                databaseName = databaseName.replaceFirst("bbdd_", "").replaceFirst("_softgenius", "");
                listaBbddPK.addItem(databaseName);
            }
        }
        resultSet.close();
        conexionBBDD.close();
    }

    /**
     * Obtiene las bases de datos que constan en el sistema pero solo muestra
     * aquellos que empiezan por bbdd_ Al usuario los muestra sin bbdd_ y sin
     * _sofgenius.
     */
    private void cargarBbddFK() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(" ");

        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();

        while (resultSet.next()) {
            String databaseName = resultSet.getString("TABLE_CAT");
            if (databaseName.startsWith("bbdd_")) {
                databaseName = databaseName.replaceFirst("bbdd_", "").replaceFirst("_softgenius", "");
                listaBbddFK.addItem(databaseName);
            }
        }
        resultSet.close();
        conexionBBDD.close();
    }

    /**
     * Obtiene lastablas que constan en la base de datos elegida.
     */
    private void cargarTablasPK(String bbddSeleccionada) throws SQLException {

        comboTablasPK.removeAllItems();
        String db = bbddSeleccionada;
        String[] nombresTablas = obtenerNombresTablas(db);

        if (nombresTablas != null) {
            for (String nombreTabla : nombresTablas) {
                comboTablasPK.addItem(nombreTabla);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontraron tablas en la base de datos seleccionada.");
        }
    }

    /**
     * Obtiene las tablas que constan en la base de datos elegida.
     */
    private void cargarTablasFK(String bbddSeleccionada) throws SQLException {

        btnCrearClaveForanea.setEnabled(true);
        comboTablasFK.removeAllItems();
        String db = bbddSeleccionada;
        String[] nombresTablas = obtenerNombresTablas(db);

        if (nombresTablas != null) {
            for (String nombreTabla : nombresTablas) {
                comboTablasFK.addItem(nombreTabla);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontraron tablas en la base de datos seleccionada.");
        }
    }

    private void buscarPK(String bbddSeleccionada, String tablaSeleccionada) throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbddSeleccionada);
        if (conexionBBDD == null) {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión con la base de datos.");
            return;
        }
        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, tablaSeleccionada);

        listaPK.removeAllItems();

        boolean hasPK = false;
        while (pkResultSet.next()) {
            hasPK = true;
            String columnName = pkResultSet.getString("COLUMN_NAME");
            listaPK.addItem(columnName);
        }

        if (!hasPK) {
            JOptionPane.showMessageDialog(null, "No se encontraron claves primarias en la tabla seleccionada.");
        }

        pkResultSet.close();
        conexionBBDD.close();
    }

    private void buscarFK(String bbddSeleccionada, String tablaSeleccionada) throws SQLException {
        String[] campos = obtenerNombresColumnas(bbddSeleccionada, tablaSeleccionada);

        if (campos != null) {
            for (String nombreColumna : campos) {
                listaCamposFK.addItem(nombreColumna);
                System.out.println(nombreColumna);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontraron columnas en la tabla seleccionada.");
        }
    }

    private void crearFK() {
        String bbddPk = "bbdd_" + (String) listaBbddPK.getSelectedItem() + "_softgenius";
        String tablaPk = (String) comboTablasPK.getSelectedItem();
        String pk = (String) listaPK.getSelectedItem(); // Asegúrate de que este es el correcto para PK
        String bbddFk = "bbdd_" + (String) listaBbddFK.getSelectedItem() + "_softgenius";
        String tablaFk = (String) comboTablasFK.getSelectedItem();
        String fk = (String) listaCamposFK.getSelectedItem(); // Esto debería ser listaFK no listaPK
        String clave = txtClave.getText(); // No necesitas hacer cast a String

        if (bbddPk == null || tablaPk == null || pk == null || bbddFk == null || tablaFk == null || fk == null || clave == null || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            crearClaveForanea(bbddPk, bbddFk, tablaPk, pk, tablaFk, fk, clave);
            JOptionPane.showMessageDialog(this, "Clave foránea creada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear la clave foránea: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearClaveForanea(String bbddPk, String bbddFk, String tablaPk, String pk, String tablaFk, String fk, String clave) throws SQLException {

        String sql = String.format(
                "ALTER TABLE %s.%s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s.%s(%s)",
                bbddFk, tablaFk, clave, fk, bbddPk, tablaPk, pk
        );

        Connection conexionBBDD = null;
        Statement statement = null;
        try {
            conexionBBDD = ConexionBD.obtenerConexion(" ");
            statement = conexionBBDD.createStatement();
            statement.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Clave foránea creada correctamente", "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lanza la excepción para que sea manejada en un nivel superior
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (conexionBBDD != null) {
                conexionBBDD.close();
            }
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

        // Acción una vez se selecciona bbdd de la PK
        if (e.getSource() == listaBbddPK) {
            String bbddSeleccionadaPK = (String) listaBbddPK.getSelectedItem();
            if (!"- ↓↓↓↓↓ -".equals(bbddSeleccionadaPK)) {
                try {
                    cargarTablasPK("bbdd_" + bbddSeleccionadaPK + "_softgenius");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar las tablas: " + ex.getMessage());
                }
            }
            comboTablasPK.addActionListener(this);
        }

        // Acción una vez se selecciona bbdd de la FK
        if (e.getSource() == listaBbddFK) {
            String bbddSeleccionadaFK = (String) listaBbddFK.getSelectedItem();
            if (!"- ↓↓↓↓↓ -".equals(bbddSeleccionadaFK)) {
                try {
                    cargarTablasFK("bbdd_" + bbddSeleccionadaFK + "_softgenius");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar las tablas: " + ex.getMessage());
                }
            }
            comboTablasFK.addActionListener(this);
        }

        // Acción una vez se selecciona tabla de la PK
        if (e.getSource() == comboTablasPK) {
            String tablaSeleccionadaPK = (String) comboTablasPK.getSelectedItem();
            if (!"- ↓↓↓↓ -".equals(tablaSeleccionadaPK)) {

                String baseDeDatosPK = "bbdd_" + (String) listaBbddPK.getSelectedItem() + "_softgenius";
                try {
                    buscarPK(baseDeDatosPK, tablaSeleccionadaPK);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar la PK: " + ex.getMessage());
                }
            }
        }

        // Acción una vez se selecciona tabka de la FK
        if (e.getSource() == comboTablasFK) {
            String tablaSeleccionadaFK = (String) comboTablasFK.getSelectedItem();
            if (!"- ↓↓↓↓ -".equals(tablaSeleccionadaFK)) {
                String baseDeDatosFK = "bbdd_" + (String) listaBbddFK.getSelectedItem() + "_softgenius";
                try {
                    buscarFK(baseDeDatosFK, tablaSeleccionadaFK);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los campos: " + ex.getMessage());
                }
            }
        }

        if (e.getSource() == btnCrearClaveForanea) {
            crearFK();
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
        panel = new JPanel(new GridLayout(0, 1));
        panel.setBackground(colorPanel);

        panel1 = new JPanel();
        panel1.setBackground(colorPanel);

        panel2 = new JPanel();
        panel2.setBackground(colorPanel);

        // Crear los botones
        btnCrearClaveForanea = new JButton("Crear clave foránea");
        btnCrearClaveForanea.setEnabled(false);
        btnCrearClaveForanea.addActionListener(this);

        // Damos formato y color a los botones
        Color colorAmarillo = new Color(241, 196, 15);
        Color colorVerde = new Color(39, 174, 96);
        Color colorAzul = new Color(33, 97, 140);
        Color colorRojo = new Color(255, 0, 0);

        Color fondoBlanco = Color.white;
        JButton[] botonCrearCopia = {btnCrearClaveForanea};

        colorBotones.aplicarEstilosBotones(botonCrearCopia, colorVerde, fondoBlanco, colorVerde);

        lblListaBbddPK = new JLabel("Escoge una BBDD primaria");
        lblListaTablasPK = new JLabel("Nombre tabla primaria");
        lblListaPK = new JLabel("Clave primaria");
        txtClave = new JTextField(10);

        lblListaBbddFK = new JLabel("Escoge una BBDD foránea");
        lblListaTablasFK = new JLabel("Nombre tabla foránea");
        lblListaFK = new JLabel("Escoge clave foránea");
        lblCrearClaveForanea = new JLabel("CREAR CLAVE FORÁNEA");

        lblClave = new JLabel("Introduzca nombre para la clave foránea");
        listaBbddPK = new JComboBox<>();
        listaBbddPK.setBackground(colorSelect);
        listaBbddPK.addItem("- ↓↓↓↓↓ -");

        listaBbddFK = new JComboBox<>();
        listaBbddFK.setBackground(colorSelect);
        listaBbddFK.addItem("- ↓↓↓↓↓ -");

        comboTablasPK = new JComboBox<>();
        comboTablasPK.setBackground(colorSelect);
        comboTablasPK.addItem("- ↓↓↓↓ -");

        comboTablasFK = new JComboBox<>();
        comboTablasFK.setBackground(colorSelect);
        comboTablasFK.addItem("- ↓↓↓↓ -");

        listaPK = new JComboBox<>();
        listaPK.setBackground(colorSelect);

        listaCamposFK = new JComboBox<>();
        listaCamposFK.setBackground(colorSelect);
        listaCamposFK.addItem("- ↓↓↓ -");

        panel.add(panel1);
        panel.add(panel2);
        // Agregamos los componentes al panel

        panel1.add(lblListaBbddPK);
        panel1.add(listaBbddPK);
        panel1.add(lblListaTablasPK);
        panel1.add(comboTablasPK);
        panel1.add(lblListaPK);
        panel1.add(listaPK);

        panel2.add(lblListaBbddFK);
        panel2.add(listaBbddFK);
        panel2.add(lblListaTablasFK);
        panel2.add(comboTablasFK);
        panel2.add(lblListaFK);
        panel2.add(listaCamposFK);
        panel2.add(lblClave);
        panel2.add(txtClave);
        panel2.add(lblCrearClaveForanea);
        panel2.add(btnCrearClaveForanea);

        this.setBackground(colorPanel);
        // Agregar el panel al marco
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
    private void actualizarIdioma(String idioma) throws SQLException {
        idiomaActual = obtenerIdiomaActual();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblListaBbddPK.setText(resourceBundle.getString("lblListaBbddPK"));
        lblListaTablasPK.setText(resourceBundle.getString("lblListaTablasPK"));
        lblCrearClaveForanea.setText(resourceBundle.getString("lblCrearClaveForanea"));
        btnCrearClaveForanea.setText(resourceBundle.getString("btnCrearClaveForanea"));
        lblListaBbddFK.setText(resourceBundle.getString("lblListaBbddFK"));
        lblListaTablasFK.setText(resourceBundle.getString("lblListaTablasFK"));
        lblListaPK.setText(resourceBundle.getString("lblListaPK"));
        lblListaFK.setText(resourceBundle.getString("lblListaFK"));
        lblClave.setText(resourceBundle.getString("lblClave"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
