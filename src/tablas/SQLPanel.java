package tablas;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import softgenius.ConexionBD;

/**
 * Esta clase representa un panel para ejecutar sentencias SQL. Permite al
 * administrador del sistema ejecutar sentencias SQL desde el programa. Se asume
 * que el administrador tiene control sobre las sentencias que ejecuta. La clase
 * incluye un área de texto para ingresar la consulta SQL, un botón para
 * ejecutarla, un área de texto para mostrar los resultados y un ComboBox para
 * seleccionar la base de datos. También muestra el nombre de la base de datos
 * seleccionada. Implementa la interfaz ActionListener para manejar eventos de
 * los componentes.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class SQLPanel extends JPanel implements ActionListener {

    private Connection connection;
    private JTextArea sqlTextArea;
    private JTextArea resultTextArea;
    private JComboBox<String> databaseComboBox;
    private JLabel lblSelectedDatabase, lblSeleccionaInfo;
    private String idiomaActual = "Spanish";
    private JButton executeButton;
    private Color colorFondo = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Constructor de la clase. Inicializa los componentes y carga las bases de
     * datos disponibles en el ComboBox.
     */
    public SQLPanel() throws SQLException {
        initializeComponents();
        populateDatabaseComboBox();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
    }

    /**
     * Inicializa los componentes del panel.
     */
    private void initializeComponents() {
        // Establece el diseño del panel como BorderLayout
        setLayout(new BorderLayout());

        lblSeleccionaInfo = new JLabel("Selecciona una base de datos:");
        // Crea un panel para el área de SQL y el botón de ejecución
        JPanel bottonPanel = new JPanel(new BorderLayout());
        bottonPanel.setBackground(colorFondo);
        bottonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añade relleno
        sqlTextArea = new JTextArea(5, 20); // Crea el área de texto para la consulta SQL
        sqlTextArea.setLineWrap(true); // Habilita el ajuste de línea para mejor legibilidad

        // Crea y estiliza el botón Ejecutar
        executeButton = new JButton("Ejecutar");
        ColorBotones colorBotones = new ColorBotones(); // Instancia de ColorBotones para estilizar el botón
        Color buttonBackgroundColor = new Color(230, 230, 230); // Color de fondo para botones
        Color textColor = new Color(63, 143, 33); // Color de texto
        colorBotones.aplicarEstilosBotones(new JButton[]{executeButton},
                buttonBackgroundColor, textColor, textColor); // Aplica estilos al botón

        executeButton.addActionListener(this); // Agrega ActionListener al botón
        bottonPanel.add(new JScrollPane(sqlTextArea), BorderLayout.CENTER); // Añade el área de texto con scroll al panel
        bottonPanel.add(executeButton, BorderLayout.SOUTH); // Añade el botón al panel en la parte inferior

        // Crea el área central para mostrar los resultados
        resultTextArea = new JTextArea(20, 40);
        resultTextArea.setEditable(false); // Hace que el área de texto de resultados no sea editable
        add(new JScrollPane(resultTextArea), BorderLayout.CENTER); // Añade el área de texto de resultados con scroll

        // Crea un panel para la parte superior que incluye el ComboBox para seleccionar la base de datos
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(colorPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añade relleno al panel
        databaseComboBox = new JComboBox<>(); // Crea un ComboBox
        databaseComboBox.setBackground(colorSelect);
        databaseComboBox.addActionListener(this); // Agrega ActionListener al ComboBox
        topPanel.add(lblSeleccionaInfo); // Añade una etiqueta al panel
        topPanel.add(databaseComboBox); // Añade el ComboBox al panel

        // Añade una etiqueta para mostrar la base de datos seleccionada
        lblSelectedDatabase = new JLabel(" ");
        topPanel.add(lblSelectedDatabase); // Añade la etiqueta al panel inferior

        add(topPanel, BorderLayout.NORTH); // Añade el panel inferior al norte del panel principal
        add(bottonPanel, BorderLayout.SOUTH); // Añade el panel superior al sur del panel principal

    }

    /**
     * Método para cargar las bases de datos disponibles en el ComboBox.
     */
    private void populateDatabaseComboBox() {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("")) {

            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet rs = metaData.getCatalogs(); // Obtiene los catálogos de la base de datos

            java.util.List<String> databaseNames = new ArrayList<>();
            while (rs.next()) {
                String dbName = rs.getString("TABLE_CAT");
                // Verifica si el nombre de la base de datos comienza con "bbdd_"
                if (dbName.startsWith("bbdd_")) {
                    databaseNames.add(dbName.replace("bbdd_", "").replace("_softgenius", ""));
                }
            }
            databaseComboBox.setModel(new DefaultComboBoxModel<>(databaseNames.toArray(new String[0])));

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching databases.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para ejecutar la consulta SQL ingresada por el usuario.
     */
    private void executeSQL() {
        String sql = sqlTextArea.getText(); // Obtiene la consulta SQL ingresada
        String selectedDatabase = (String) databaseComboBox.getSelectedItem(); // Obtiene la base de datos seleccionada

        if (selectedDatabase == null) {
            JOptionPane.showMessageDialog(this, "Please select a database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Establece la conexión con la base de datos seleccionada
            connection.setCatalog("bbdd_" + selectedDatabase + "_softgenius");

            // Ejecuta la consulta SQL
            Statement statement = connection.createStatement();
            boolean isResultSet = statement.execute(sql); // Ejecuta la consulta SQL

            if (isResultSet) {
                // Si es un conjunto de resultados, muestra los resultados en el área de texto
                ResultSet resultSet = statement.getResultSet();
                resultTextArea.setText(getResultSetAsString(resultSet)); // Muestra los resultados
            } else {
                // Si la consulta no devuelve un resultado, muestra el número de filas afectadas
                int rowsAffected = statement.getUpdateCount();
                resultTextArea.setText("Rows affected: " + rowsAffected);
            }
        } catch (SQLException ex) {
            resultTextArea.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Método para convertir un ResultSet a una cadena de texto.
     *
     * @param resultSet El conjunto de resultados a convertir.
     * @return Una cadena que representa los resultados del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del
     * ResultSet.
     */
    private String getResultSetAsString(ResultSet resultSet) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Agrega los nombres de las columnas
        for (int i = 1; i <= columnCount; i++) {
            result.append(metaData.getColumnName(i)).append("\t");
        }
        result.append("\n");

        // Agrega los datos de las filas
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                result.append(resultSet.getString(i)).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Método que maneja los eventos generados por los componentes.
     *
     * @param e El evento que se ha generado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == databaseComboBox) {
            // Si se selecciona una base de datos en el ComboBox
            updateSelectedDatabaseLabel();
            try {
                // Actualizar el idioma
                actualizarIdioma(idiomaActual);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar el idioma.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == executeButton) {
            executeSQL();

        }
    }

    private void updateSelectedDatabaseLabel() {
        String selectedDatabase = (String) databaseComboBox.getSelectedItem();
        lblSelectedDatabase.setText("Base de datos seleccionada: " + selectedDatabase);
    }

    /**
     * ***************************************************************************
     * /////////////////////////////////////////////////////////////////////////
     *
     * GESTIÓN IDIOMAS
     *
     * /////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */
    private void actualizarIdioma(String idioma) throws SQLException {
        idiomaActual = obtenerIdiomaActual();
        // Cargar el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);
        String selectedDatabase = (String) databaseComboBox.getSelectedItem();

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblSelectedDatabase.setText(resourceBundle.getString("lblSelectedDatabase") + "     " + selectedDatabase.toUpperCase());
        lblSeleccionaInfo.setText(resourceBundle.getString("lblSelectDatabase"));
        executeButton.setText(resourceBundle.getString("executeButton"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
