package tablas;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.NivelUsuario;
import config.ColorBotones;
import idioma.Idiomas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import softgenius.ConexionBD;

/**
 *
 * @author jGrupo 1 - 1º DAM Colexio Karbo
 */
public class TableWithInsert extends JPanel {

    private JTabbedPane tabbedPane;
    private JComboBox<String> databaseComboBox;
    private JLabel lblSelectDatabase = new JLabel("Selecciona base de datos");
    private JButton closeButton;
    private String idiomaActual = "Spanish";
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();


    public TableWithInsert() throws SQLException {
        initializeComponents();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
        populateDatabaseComboBox();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(colorPanel);
        databaseComboBox = new JComboBox<>();
        controlPanel.add(lblSelectDatabase);
        controlPanel.add(databaseComboBox);
        add(controlPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(colorPanel);

        this.setBackground(colorPanel);

        add(tabbedPane, BorderLayout.CENTER);
        addCloseButton();
    }

    /**
     * Obtiene las bases de datos que constan en el sistema pero solo muestra
     * aquellos que empiezan por bbdd_ Al usuario los muestra sin bbdd_ y sin
     * _sofgenius.
     */
    private void populateDatabaseComboBox() {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("")) {
            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet rs = metaData.getCatalogs();

            List<String> databaseNames = new ArrayList<>();
            while (rs.next()) {
                String dbName = rs.getString("TABLE_CAT");
                if (dbName.startsWith("bbdd_")) {
                    // Obteniendo el nivel de usuario
                    int nivelUsuario = NivelUsuario.obtenerNivelUsuario(NivelUsuario.nombreUsuario);
                    // Verificando si el nivel de usuario es mayor o igual a 2 y no está en la lista de exclusión
                    if (nivelUsuario >= 2 && !excludedDatabases.contains(dbName)) {
                        databaseNames.add(dbName.replace("bbdd_", "").replace("_softgenius", ""));
                    } else if (nivelUsuario < 2) { // Si el nivel de usuario es menor que 2, no se aplican filtros
                        databaseNames.add(dbName.replace("bbdd_", "").replace("_softgenius", ""));
                    }
                }
            }
            databaseComboBox.setBackground(colorSelect);
            databaseComboBox.setModel(new DefaultComboBoxModel<>(databaseNames.toArray(new String[0])));

            rs.close();
            conexionBBDD.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching databases.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        databaseComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDatabase = (String) databaseComboBox.getSelectedItem();
                if (selectedDatabase != null) {
                    createTabsForDatabase("bbdd_" + selectedDatabase + "_softgenius");
                }
            }
        });
    }

// Lista de bases de datos a excluir
    private List<String> excludedDatabases = Arrays.asList(
            "bbdd_config_softgenius",
            "bbdd_cuentas_softgenius"
    );

    private void createTabsForDatabase(String databaseName) {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion(databaseName)) {
            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet tables = metaData.getTables(databaseName, null, null, new String[]{"TABLE"});

            tabbedPane.removeAll(); // Remove existing tabs

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                CloseableTab tabComponent = new CloseableTab(tableName, tabbedPane);
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(colorPanel);

                // Instead of creating a new table here, call the createTable method from formatoTablaGeneral
                FormatoTablaGeneral tablaGeneral = new FormatoTablaGeneral(databaseName, tableName);
                tablaGeneral.setBackground(colorPanel);
                panel.add(tablaGeneral, BorderLayout.CENTER);

                tabbedPane.addTab(null, panel);
                tabbedPane.setBackground(colorPanel);
                tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabComponent); // Establece el componente personalizado como el componente de la pestaña
            }

            conexionBBDD.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            String errorMessage;
            if (idiomaActual.equals("English")) {
                errorMessage = "Error occurred while creating tabs for database: " + databaseName;
            } else {
                errorMessage = "Se produjo un error al crear las pestañas para la base de datos: " + databaseName;
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCloseButton() {
        closeButton = new JButton("Cerrar todas las pestañas");

        ColorBotones colorBotones = new ColorBotones();
        Color colorAzul = new Color(33, 97, 140);
        Color miColorFondo = Color.white;
        JButton[] botonBuscar = {closeButton};

        colorBotones.aplicarEstilosBotones(botonBuscar, miColorFondo, colorAzul, colorAzul);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tabCount = tabbedPane.getTabCount();
                for (int i = tabCount - 1; i >= 0; i--) {
                    tabbedPane.remove(i);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(colorPanel);

        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * ***************************************************************************
     * ///////////////////////////////////////////////////////////////////////////
     *
     * GESTIÓN IDIOMAS
     *
     * //////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */
    private void actualizarIdioma(String idioma) throws SQLException {
        idiomaActual = obtenerIdiomaActual();
        // Cargar el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblSelectDatabase.setText(resourceBundle.getString("lblSelectDatabase"));
        closeButton.setText(resourceBundle.getString("closeButton"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
