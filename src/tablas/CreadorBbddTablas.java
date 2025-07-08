package tablas;

import com.mysql.cj.jdbc.DatabaseMetaData;
import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import softgenius.ConexionBD;

/**
 * Clase para crear bases de datos y/o tablas con los campos que necesite. Si se
 * mantiene el check activado, ese campo se utiliza como PK con autoincrement.
 * En caso de que se desactive se deniega la escritura y no se tiene en cuenta
 * lo que hubiese escrito.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class CreadorBbddTablas extends JPanel implements ActionListener {

    private JButton btnCrearBbdd, btnCrearTabla, btnAnadirCampos, btnQuitarCampos;
    private JLabel lblCrearBbdd, lblCrearTabla, lblAnadirCampos, lblBbddExistentes;
    private JTextField txtCrearBbdd, txtCrearTabla, txtCampoID;
    private JPanel panel;
    private JComboBox<String> listaBbdd;
    private JComboBox<String> tipoDatosCombo, longitudCombo;
    private JCheckBox chkPK;
    private int camposAgregados = 0;
    private String nombreBbddSeleccionada;
    private List<JTextField> camposDeTabla = new ArrayList<>();
    private JPanel panel1, panel2, panel3, panel4;
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Creamos los elementos necesarios.
     */
    public CreadorBbddTablas() {
        try {

            ColorBotones colorBotones = new ColorBotones();

            // Crear el panel
            panel = new JPanel(new GridLayout(0, 1));
            panel1 = new JPanel();
            panel2 = new JPanel();
            panel3 = new JPanel();
            panel4 = new JPanel();
            panel4.setLayout(new GridLayout(0, 6)); //Agrupamos en columnas de 6 elementos

            panel.setBackground(colorPanel);
            panel1.setBackground(colorPanel);
            panel2.setBackground(colorPanel);
            panel3.setBackground(colorPanel);
            panel4.setBackground(colorPanel);

            // Crear los botones
            btnCrearBbdd = new JButton("Siguiente");
            btnCrearBbdd.addActionListener(this);

            btnCrearTabla = new JButton("Crear tabla");
            btnCrearTabla.addActionListener(this);

            btnAnadirCampos = new JButton("+");
            btnAnadirCampos.addActionListener(this);
            btnAnadirCampos.setPreferredSize(new Dimension(50, 30));

            btnQuitarCampos = new JButton("-");
            btnQuitarCampos.addActionListener(this);
            btnQuitarCampos.setPreferredSize(new Dimension(50, 30));

            // Damos formato y color a los botones
            Color colorAmarillo = new Color(241, 196, 15);
            Color colorVerde = new Color(39, 174, 96);
            Color colorAzul = new Color(33, 97, 140);
            Color colorRojo = new Color(255, 0, 0);

            Color miColorFondo = Color.white;
            JButton[] botonCrearBbdd = {btnCrearBbdd};
            JButton[] botonCrearTabla = {btnCrearTabla};
            JButton[] botonAnadir = {btnAnadirCampos};
            JButton[] botonQuitar = {btnQuitarCampos};

            colorBotones.aplicarEstilosBotones(botonCrearBbdd, miColorFondo, colorAmarillo, colorAmarillo);
            colorBotones.aplicarEstilosBotones(botonCrearTabla, miColorFondo, colorVerde, colorVerde);
            colorBotones.aplicarEstilosBotones(botonAnadir, colorAzul, miColorFondo, colorAzul);
            colorBotones.aplicarEstilosBotones(botonQuitar, colorRojo, miColorFondo, colorRojo);

            lblBbddExistentes = new JLabel("Escoge una BBDD existente");
            lblCrearBbdd = new JLabel("o bien \ncrea una nueva BBDD");
            lblCrearTabla = new JLabel("Nombre tabla");
            lblAnadirCampos = new JLabel("Añadir campos");

            txtCrearBbdd = new JTextField(10);
            txtCrearTabla = new JTextField(10);

            listaBbdd = new JComboBox<>();
            listaBbdd.setBackground(colorSelect);
            listaBbdd.addItem("- BBDD existentes -");

            chkPK = new JCheckBox("Insertar PK");
            chkPK.setBackground(colorPanel);
            chkPK.setSelected(true);
            chkPK.addActionListener(this);
            txtCampoID = new JTextField(10);
            txtCampoID.setEnabled(true);

            cargarBbdd();
            // Agrega panel1 y panel2 a panel
            panel.add(panel1);
            panel.add(panel2);
            panel.add(panel3);
            panel.add(panel4);

            // Agregamos los componentes
            panel1.add(lblBbddExistentes);
            panel1.add(listaBbdd);
            panel1.add(lblCrearBbdd);
            panel1.add(txtCrearBbdd);
            panel1.add(btnCrearBbdd);

            panel2.add(lblCrearTabla);
            panel2.add(txtCrearTabla);

            panel2.add(lblAnadirCampos);
            panel2.add(btnAnadirCampos);
            panel2.add(btnQuitarCampos);

            panel2.add(btnCrearTabla);

            panel3.add(chkPK);
            panel3.add(txtCampoID);

            this.setBackground(colorPanel);
            // Agrega el panel al marco
            add(panel);

            setVisible(true);
            ocultarBotones(false);
        } catch (SQLException ex) {
            Logger.getLogger(CreadorBbddTablas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para la creación de campos.
     */
    private void agregarCampos() {
        // Crear los nuevos campos de texto y JComboBox
        JTextField nuevoCampo = new JTextField(10);
        tipoDatosCombo = new JComboBox<>(new String[]{"varchar", "int", "double", "date"});
        tipoDatosCombo.setBackground(colorSelect);

        longitudCombo = new JComboBox<>(new String[]{"50", "100", "255"});
        longitudCombo.setBackground(colorSelect);

        // Incrementar el contador de campos agregados
        camposAgregados++;
        // Agrega el nuevo campo al panel
        panel4.add(nuevoCampo);
        panel4.add(tipoDatosCombo);
        panel4.add(longitudCombo);
        // Agrega el nuevo campo a la lista de campos de tabla
        camposDeTabla.add(nuevoCampo);
        // Repinta el panel para que se actualice la vista
        panel4.revalidate();
        panel4.repaint();

        // Agrega una validación para hacer el campo de texto obligatorio
        nuevoCampo.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                if (textField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El campo es obligatorio");
                    return false; // Indica que la entrada no es válida
                }
                return true; // Indica que la entrada es válida
            }
        });
    }

    private void quitarCampo() {
        // Verifica si hay campos para quitar
        if (camposAgregados > 0) {
            // Obtiene el índice del último campo agregado
            int indiceUltimoCampo = panel4.getComponentCount() - 1;

            // Eliminar los últimos tres componentes del panel (JTextField, JComboBox, JComboBox)
            panel4.remove(panel4.getComponent(indiceUltimoCampo));
            panel4.remove(panel4.getComponent(indiceUltimoCampo - 1));
            panel4.remove(panel4.getComponent(indiceUltimoCampo - 2));

            // Decrementar el contador de campos agregados
            camposAgregados--;

            // Repintar el panel para que se actualice la vista
            panel4.revalidate();
            panel4.repaint();
        } else {
            JOptionPane.showMessageDialog(null, "No hay campos para quitar.");
        }
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
        // Cerrar la conexión y el ResultSet
        resultSet.close();
        conexionBBDD.close();
    }

    /**
     * Método para las acciones de los botones.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCrearBbdd) {
            seleccionarBbddDeLista();

        }

        if (e.getSource() == chkPK) {
            if (chkPK.isSelected()) {
                txtCampoID.setEnabled(true);
            } else {
                txtCampoID.setEnabled(false);
            }
        }

        if (e.getSource() == btnAnadirCampos) {
            agregarCampos();
        } else if (e.getSource() == btnQuitarCampos) {
            quitarCampo();
        }

        if (e.getSource() == btnCrearTabla) {
            String nombreTabla = txtCrearTabla.getText(); // Obtener el nombre de la tabla del campo de texto
            if (!nombreTabla.isEmpty()) {
                try {
                    crearTabla(nombreTabla);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al crear la tabla: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "El nombre de la tabla no puede estar vacío");
            }
        }
    }

    /**
     * Obtenemos la lista de bbdd que hay existentes por si queremos introducir
     * ahí la tabla nueva.
     */
    private void seleccionarBbddDeLista() {
        String nombreBbddCombo = (String) listaBbdd.getSelectedItem();
        String nombreBbddText = (String) txtCrearBbdd.getText();

        if (nombreBbddCombo != "- BBDD existentes -" || !nombreBbddText.isEmpty()) {
            if (nombreBbddCombo != "- BBDD existentes -") {
                ocultarBotones(true);
                agregarCampos();
                nombreBbddSeleccionada = nombreBbddCombo; // Almacenar el nombre de la base de datos seleccionada
                System.out.println(nombreBbddCombo);
            } else {
                if (nombreBbddText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No puede estar vacío");
                    System.out.println("El nombre BBDD no puede estar vacío");
                } else {
                    crearBBDD(nombreBbddText);
                    nombreBbddSeleccionada = "bbdd_" + nombreBbddText + "_softgenius"; // Almacenar el nombre de la base de datos creada
                    System.out.println(nombreBbddText);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Escoge bbdd o crea una nueva");
        }
    }

    /**
     * Método para crear una base de datos. Traemos el nombre de la base por
     * parámetros.
     *
     * @param nombreBBDD
     */
    private void crearBBDD(String nombreBBDD) {

        // Verificar si el nombre de la base de datos es numérico
        if (nombreBBDD.matches("\\d+")) { // \\d+ verifica si la cadena contiene solo dígitos
            nombreBBDD += "_db"; // Añadir un sufijo para hacer el nombre válido
        }

        try {
            Connection conexionBBDD = ConexionBD.obtenerConexion("");
            // Consulta SQL para obtener todos los registros.0
            String sql = "CREATE DATABASE bbdd_" + nombreBBDD + "_softgenius";
            Statement statement = conexionBBDD.createStatement();
            statement.executeUpdate(sql);
            System.out.println("BBDD creada");
            ocultarBotones(true);
            agregarCampos();
            conexionBBDD.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ya existe una base de datos con ese nombre");
            System.out.println("No se puede crear la bbdd o ya está creada");
        }
    }

    /**
     * Método para crear la tabla con los datos recibidos por parte del usuario.
     *
     * @throws SQLException
     */
    private void crearTabla(String nombreTabla) throws SQLException {
        // Conexión a la base de datos
        try {
            Connection conexionBBDD = ConexionBD.obtenerConexion(nombreBbddSeleccionada);

            // Consulta SQL para crear la tabla
            StringBuilder sql = new StringBuilder("CREATE TABLE " + nombreTabla + " (");

            // Variable para indicar si se ha agregado la clave primaria
            boolean seAgregoPK = false;

            // Si el checkbox chkPK está seleccionado, agregar el campo especificado por txtCampoID como clave primaria
            if (chkPK.isSelected() && !txtCampoID.getText().isEmpty()) {
                sql.append(txtCampoID.getText()).append(" INT PRIMARY KEY, ");
                seAgregoPK = true;
            }

            // Iterar sobre la lista de campos de tabla para obtener los nombres de los campos
            for (JTextField campo : camposDeTabla) {
                String nombreCampo = campo.getText();
                String tipoDato = (String) tipoDatosCombo.getSelectedItem();
                String longitud = (String) longitudCombo.getSelectedItem();
                if (!nombreCampo.isEmpty()) {
                    sql.append(nombreCampo).append(" ").append(tipoDato);
                    if (!longitud.isEmpty()) {
                        sql.append("(").append(longitud).append(")");
                    }
                    sql.append(", ");
                }
            }
            // Eliminar la coma y el espacio final
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            // Ejecutar la consulta SQL para crear la tabla
            Statement statement = conexionBBDD.createStatement();
            statement.executeUpdate(sql.toString());

            JOptionPane.showMessageDialog(null, "Tabla creada correctamente");

            // Restablecer la interfaz
            resetearInterfaz();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la tabla: " + e.getMessage());
        }
    }

    private void resetearInterfaz() {
        txtCrearBbdd.setText("");
        txtCrearTabla.setText("");

        // Eliminar todos los componentes del panel excepto el JLabel lblBbddExistentes
        panel1.removeAll();
        panel2.removeAll();
        panel3.removeAll();
        panel4.removeAll();
        panel1.add(lblBbddExistentes);

        // Restablecer la selección del JComboBox listaBbdd a la opción por defecto
        listaBbdd.setSelectedItem("- BBDD existentes -");

        // Agregar de nuevo el JComboBox listaBbdd al panel
        panel1.add(listaBbdd);
        panel1.add(lblCrearBbdd);
        panel1.add(txtCrearBbdd);
        panel1.add(btnCrearBbdd);

        panel2.add(lblCrearTabla);
        panel2.add(txtCrearTabla);

        panel2.add(lblAnadirCampos);
        panel2.add(btnAnadirCampos);
        panel2.add(btnQuitarCampos);

        panel2.add(btnCrearTabla);

        panel3.add(chkPK);
        panel3.add(txtCampoID);
        txtCampoID.setEnabled(true);

        // Repintar el panel para que se actualice la vista
        panel1.revalidate();
        panel1.repaint();
        panel2.revalidate();
        panel2.repaint();
        panel3.revalidate();
        panel3.repaint();
    }

    private void ocultarBotones(boolean orden) {
        lblCrearTabla.setVisible(orden);
        txtCrearTabla.setVisible(orden);
        lblCrearTabla.setVisible(orden);
        lblAnadirCampos.setVisible(orden);
        btnAnadirCampos.setVisible(orden);
        btnQuitarCampos.setVisible(orden);
        btnCrearTabla.setVisible(orden);
        panel3.setVisible(orden);

    }
}
