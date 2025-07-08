package tablas;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.NivelUsuario;
import config.ColorBotones;
import idioma.Idiomas;
import softgenius.ConexionBD;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import libsql3.softgenius.GestorTablas;
import static libsql3.softgenius.GestorTablas.*;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class FormatoTablaGeneral extends JPanel {

    // Declare components
    private JPanel panel;
    private JTable table;
    private JButton btnFirst, btnModify, btnDelete, btnExtract, btnAdd, btnNext, btnPrevious, btnLast, btnEdit, btnRefresh, searchButton;
    private JTextField searchField;
    private int currentPage = 1;
    private JLabel lblContadorRegistros;
    private String idiomaActual = "Spanish";
    private final int recordsPerPage = 15;
    private String bbdd;
    private String tabla;
    private int totalRecords;
    private List<JRadioButton> radioButtonsList = new ArrayList<>();
    private Color colorFondoPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();//Linea 346

    // Llamo al método de obtención de nivel de privilegios pasándole como parámetro
    //el usuario activo
    int nivelUsuario = NivelUsuario.obtenerNivelUsuario(NivelUsuario.nombreUsuario);

    public FormatoTablaGeneral(String bbdd, String tabla) throws SQLException {
        // Assign database and table names
        this.bbdd = bbdd;
        this.tabla = tabla;
        // Initialize components and show data
        initComponents();
        showData();
    }

    private void initComponents() throws SQLException {

        // Create main panel
        panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondoPanel);

        add(panel);

        // Initialize components
        createTable();
        createSearchBar();
        createButtons();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
        gestionBotonesInferiores(false);
        calcularTotalRegistros();
        cambiarColor();

    }

    private void calcularTotalRegistros() {
        // Set the total number of records in the label
        totalRecords = getTotalRecords();
        lblContadorRegistros.setText("Total: " + totalRecords + " ");
    }

    private void createSearchBar() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(colorFondoPanel);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Create search field
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        // Create search button
        searchButton = new JButton("Buscar");

        // Obtener los nombres de las columnas de la base de datos
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

        // Crear un array de JRadioButton para almacenar los botones
        JRadioButton[] radioButtons = new JRadioButton[columnNames.length];

        searchButton.addActionListener(e -> filterTable());
        searchPanel.add(searchButton);

        // Creamos label del contador
        lblContadorRegistros = new JLabel();
        searchPanel.add(lblContadorRegistros);

        // Add action listener to search field
        searchField.addActionListener(e -> filterTable());
    }

    /**
     * Método para mostrar el total de registros de la búsqueda.
     *
     * @param totalRecords
     */
    private void showAllMatchingRecords(int totalRecords, String objetoABuscar) {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd)) {
            // Obtener los nombres de las columnas de la tabla
            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tabla, null);
            List<String> columnNames = new ArrayList<>();
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }

            // Construir la consulta SQL
            String sql = "SELECT * FROM " + tabla + " WHERE " + objetoABuscar + " LIKE ?";
            try (PreparedStatement statement = conexionBBDD.prepareStatement(sql)) {
                statement.setString(1, "%" + searchField.getText().trim() + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    while (resultSet.next()) {
                        // Crear una lista para almacenar los valores de cada fila
                        List<Object> rowData = new ArrayList<>();
                        for (String columnName : columnNames) {
                            Object value = resultSet.getObject(columnName);
                            rowData.add(value);
                        }
                        // Agregar la fila al modelo de la tabla
                        model.addRow(rowData.toArray());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca los datos de la búsqueda requerida. Filtra según el radio que esté
     * seleccionado.
     */
    private void filterTable() {
        // Filter the table based on search text
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        String text = searchField.getText().trim();

        if (text.length() == 0) {
            // If search text is empty, show all data
            sorter.setRowFilter(null);
            showData();
            calcularTotalRegistros();
            activacionBotonesSuperiores(true);
            desactivacionPasaPagina(true);
        } else {
            desactivacionPasaPagina(false);
            // Determine the search criteria based on the selected radio button
            String objetoABuscar = "";
            for (JRadioButton radioButton : radioButtonsList) {
                if (radioButton.isSelected()) {
                    objetoABuscar = radioButton.getText();
                    break;
                }
            }

            // Get column names dynamically from the database
            String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

            // Apply row filter with search criteria
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + text, getColumnIndex(objetoABuscar, columnNames));
            sorter.setRowFilter(rowFilter);

            // Reset current page and update table with matching records
            currentPage = 1;
            int totalRecords = contadorRegistrosPorBusqueda(bbdd, tabla, text, objetoABuscar);
            lblContadorRegistros.setText("Total registros: " + totalRecords + " ");
            showAllMatchingRecords(totalRecords, objetoABuscar);

            // Disable next and previous buttons if total records <= 15
            if (totalRecords <= 15) {
                activacionBotonesSuperiores(false);
            }
        }
    }

    /**
     * Método para obtener el índice de la columna en función del objeto a
     * buscar
     *
     * @param objetoABuscar
     * @param columnNames
     * @return
     */
    private int getColumnIndex(String objetoABuscar, String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(objetoABuscar)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Método para crear la tabla que se muestra en pantalla
     */
    private void createTable() {
        // Create the table with specified column classes and properties
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que todas las celdas no sean editables
            }
        };
        // Retrieve column names from the database (Assuming you have a method to fetch column names)
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

        // Add columns to the table model
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }

        // Create the table
        table = new JTable(model);
        table.setRowSorter(new TableRowSorter<>(model));
        table.setBackground(new Color(240, 240, 240));

        // Set cell renderer for alternating row colors
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255));
                    } else {
                        c.setBackground(new Color(255, 250, 250));
                    }
                }
                return c;
            }
        };

        // Apply cell renderer to table columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            table.getColumnModel().getColumn(i).setPreferredWidth(150); // Set preferred width to 150 for all columns
        }

        // Set font and row height for the table
        Font font = new Font("Arial", Font.PLAIN, 14);
        table.setFont(font);
        table.setRowHeight(30);

        // Set default cell renderer for center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Set auto resize mode and preferred size for scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Método para refrescar la página.
     */
    private void refreshTableData() {
        // Refresh table data and clear search field
        showData();
        searchField.setText("");
        calcularTotalRegistros();
        activacionBotonesSuperiores(true);
        desactivacionPasaPagina(true);
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        sorter.setRowFilter(null);
    }

    private void createNavigationButtons() {
        // Create navigation buttons for paging
        btnNext = new JButton("Siguiente");
        btnPrevious = new JButton("Anterior");

        // Obtener los nombres de las columnas de la base de datos
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

        // Crear un array de JRadioButton para almacenar los botones
        JRadioButton[] radioButtons = new JRadioButton[columnNames.length];

        // Crear un ButtonGroup para agrupar los botones de radio
        ButtonGroup group = new ButtonGroup();

        // Crear JRadioButton para cada columna y agregarlos al grupo de botones
        for (int i = 0; i < columnNames.length; i++) {
            radioButtons[i] = new JRadioButton(columnNames[i]);
            radioButtons[i].setBackground(colorFondoPanel);  // Establecer el color de fondo
            radioButtons[i].setOpaque(true);
            group.add(radioButtons[i]);
        }

        // Seleccionar el primer botón por defecto
        radioButtons[0].setSelected(true);

        // Add action listeners to navigation buttons
        btnNext.addActionListener(e -> nextPage());
        btnPrevious.addActionListener(e -> previousPage());

        // Add navigation buttons and radio buttons to the button panel
        JPanel buttonPanel = (JPanel) panel.getComponent(1);
        buttonPanel.setBackground(colorFondoPanel);
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnNext);
        for (JRadioButton radioButton : radioButtons) {
            buttonPanel.add(radioButton);
        }

        // Add radio buttons to the list
        for (JRadioButton radioButton : radioButtons) {
            radioButtonsList.add(radioButton);
            buttonPanel.add(radioButton);
        }
    }

    private int getTotalRecords() {
        // Get the total number of records in the database
        int totalRecords = 0;
        try (Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd)) {
            String sql = "SELECT COUNT(*) AS total FROM " + tabla;
            try (PreparedStatement statement = conexionBBDD.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalRecords = resultSet.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    private void nextPage() {
        // Navigate to the next page of records
        int totalRecords = getTotalRecords();
        int maxPage = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage < maxPage) {
            currentPage++;
            showData();
        } else {
            JOptionPane.showMessageDialog(this, "Estás en la última página");
        }
    }

    private void previousPage() {
        // Navigate to the previous page of records
        if (currentPage > 1) {
            currentPage--;
            showData();
        } else {
            JOptionPane.showMessageDialog(this, "Estás en la primera página");
        }
    }

    private void showData() {
        // Display the data in the table
        int offset = (currentPage - 1) * recordsPerPage;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        String[][] filasAMostrar = obtenerRangoDeFilas(bbdd, tabla, recordsPerPage, offset);

        for (String[] fila : filasAMostrar) {
            model.addRow(fila);
        }
    }

    private void insertData() {
        try {
            Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd);
            // Obtener los nombres de las columnas de la tabla
            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tabla, null);
            int columnCount = 0;
            while (rs.next()) {
                columnCount++;
            }
            String[] columnNames = new String[columnCount];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                columnNames[index++] = rs.getString("COLUMN_NAME");
            }
            // Pedir al usuario los valores para cada columna
            Object[] inputValues = new Object[columnCount];

            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2)); // Panel para organizar los campos y los valores ingresados
            inputPanel.setBackground(colorFondoPanel);

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(columnNames[i] + ":"));
                JTextField textField = new JTextField();
                inputPanel.add(textField);
                inputValues[i] = textField;
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Ingrese los datos", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // Verificar si el registro está duplicado
                String registroSegundaColumna = ((JTextField) inputValues[1]).getText(); // Obtener el valor de la segunda columna
                if (comprobarRegistroDuplicado(bbdd, tabla, registroSegundaColumna, columnNames[1])) {

                    // El registro ya existe, mostrar un mensaje de confirmación
                    int opcion = JOptionPane.showConfirmDialog(this, "El registro ya existe en la base de datos. ¿Desea continuar con la inserción?", "Registro Existente", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.NO_OPTION) {
                        return; // Salir del método si el usuario elige "No"
                    }
                }

                /*                PreparedStatement pstmt = conexionBBDD.prepareStatement(insertQuery);*/
                String[] datosAInsertar = new String[obtenerNumeroColumnasTabla(bbdd, tabla)];
                datosAInsertar[0] = "0";
                for (int i = 1; i < columnCount; i++) { // Comenzar desde el segundo campo
                    datosAInsertar[i] = formatFirstLetter(((JTextField) inputValues[i]).getText());
                }

                insertarFilaCompleta(bbdd, tabla, datosAInsertar);

                // Actualizar la tabla después de la inserción
                showData();
            } else {
                JOptionPane.showMessageDialog(this, "No se han insertado datos.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void modifyRecord() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd);
        // Obtener la fila seleccionada
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Implementar lógica para editar el campo seleccionado
            String columnName = table.getColumnName(table.getSelectedColumn());
            Object newValue = JOptionPane.showInputDialog(this, "Nuevo valor para " + columnName + ":");
            if (newValue != null) {
                // Obtener el nombre de la columna de la clave primaria
                String primaryKeyColumn = findPrimaryKeyColumn(conexionBBDD, tabla);
                if (primaryKeyColumn != null) {
                    // Obtener el valor de la clave primaria
                    Object idValue = table.getValueAt(selectedRow, 0);
                    int id = -1;

                    if (idValue instanceof Integer) {
                        id = (Integer) idValue;
                    } else if (idValue instanceof String) {
                        try {
                            id = Integer.parseInt((String) idValue);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "El valor de la clave primaria no es un entero válido.");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El valor de la clave primaria no es ni un entero ni una cadena.");
                        return;
                    }

                    String updateQuery = "UPDATE " + tabla + " SET " + columnName + " = ? WHERE " + primaryKeyColumn + " = ?";
                    try (PreparedStatement pstmt = conexionBBDD.prepareStatement(updateQuery)) {
                        pstmt.setObject(1, formatFirstLetter(newValue.toString()));
                        pstmt.setInt(2, id);
                        pstmt.executeUpdate();
                        showData(); // Actualizar la tabla después de la edición
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo encontrar la columna de la clave primaria.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para editar.");
        }
    }

    private String findPrimaryKeyColumn(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
        if (rs.next()) {
            return rs.getString("COLUMN_NAME");
        } else {
            return null;
        }
    }

    private void deleteRecord() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd);
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Obtener el valor de la clave primaria
            Object idValue = table.getValueAt(selectedRow, 0);
            int id = -1;
            if (idValue instanceof Integer) {
                id = (Integer) idValue;
            } else if (idValue instanceof String) {
                try {
                    id = Integer.parseInt((String) idValue);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "El valor de la clave primaria no es un entero válido.");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "El valor de la clave primaria no es ni un entero ni una cadena.");
                return;
            }

            String primaryKeyColumn = findPrimaryKeyColumn(conexionBBDD, tabla);
            if (primaryKeyColumn != null) {
                // Desactiva la comprobación de claves foráneas
                String desactivarClaveForanea = "SET FOREIGN_KEY_CHECKS = 0;";
                // Activa la comprobación de claves foráneas
                String activaClaveForanea = "SET FOREIGN_KEY_CHECKS = 1;";

                try (Statement stmt = conexionBBDD.createStatement()) {
                    // Desactivar comprobación de claves foráneas
                    stmt.executeUpdate(desactivarClaveForanea);

                    // Eliminar las filas dependientes primero (si es necesario)
                    String deleteDependentsQuery = "DELETE FROM " + tabla + " WHERE " + primaryKeyColumn + " = ?";
                    try (PreparedStatement pstmtDependents = conexionBBDD.prepareStatement(deleteDependentsQuery)) {
                        pstmtDependents.setInt(1, id);
                        pstmtDependents.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Vuelve a activar la comprobación de claves foráneas en caso de error
                        stmt.executeUpdate(activaClaveForanea);
                        return;
                    }

                    // Ahora eliminar la fila en la tabla principal
                    String deleteQuery = "DELETE FROM " + tabla + " WHERE " + primaryKeyColumn + " = ?";
                    try (PreparedStatement pstmt = conexionBBDD.prepareStatement(deleteQuery)) {
                        pstmt.setInt(1, id);
                        pstmt.executeUpdate();
                        showData(); // Actualizar la tabla después de la eliminación
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        // Vuelve a activar la comprobación de claves foráneas
                        stmt.executeUpdate(activaClaveForanea);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar la columna de la clave primaria.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
        }
    }

    private void extractRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Extraer datos de la fila seleccionada
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < table.getColumnCount(); i++) {
                data.append(table.getColumnName(i)).append(": ").append(table.getValueAt(selectedRow, i)).append("\n");
            }
            JOptionPane.showMessageDialog(this, data.toString(), "Datos del Cliente", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para extraer los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to validate input fields
    private boolean validateInput(String firstName, String lastName, String dni, String department) {
        return !(firstName.isEmpty() || lastName.isEmpty() || dni.isEmpty() || department.isEmpty());
    }

    // Method to get the index of the selected row
    private int getSelectedRow() {
        return table.getSelectedRow();
    }

    // Method to show the last page of records
    private void showLastPage() {
        int totalRecords = getTotalRecords();
        int maxPage = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (maxPage > 0) {
            currentPage = maxPage;
            showData();
        } else {
            JOptionPane.showMessageDialog(this, "No records found.", "No Data", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method to show the first page of records
    private void showFirstPage() {
        if (currentPage != 1) {
            currentPage = 1;
            showData();
        } else {
            JOptionPane.showMessageDialog(this, "Already on the first page.", "First Page", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private boolean botonesActivados = false; // Variable para rastrear el estado de los botones

    private void toggleBotones() {
        ColorBotones colorBotones = new ColorBotones();
        Color fondoGris = Color.GRAY;
        Color colorGris = new Color(140, 140, 140);
        Color miColorFondo = Color.white;
        JButton[] botonEdit = {btnEdit};

        // Cambiar el estado de los botones y actualizar el texto del botón btnEdit
        botonesActivados = !botonesActivados; // Cambiar el estado

        if (botonesActivados) {
            gestionBotonesInferiores(true); // Activar los botones
            btnEdit.setText("Desactivar edición"); // Cambiar texto del botón

            Color colorVerde = new Color(39, 174, 96);
            Color colorRojo = new Color(255, 0, 0);
            Color colorAzul = new Color(33, 97, 140);
            Color colorNaranja = new Color(255, 140, 0);

            JButton[] botonModify = {btnModify};
            JButton[] botonDelete = {btnDelete};
            JButton[] botonExtract = {btnExtract};
            JButton[] botonAdd = {btnAdd};

            colorBotones.aplicarEstilosBotones(botonModify, colorVerde, miColorFondo, colorVerde);
            colorBotones.aplicarEstilosBotones(botonDelete, colorRojo, miColorFondo, colorRojo);
            colorBotones.aplicarEstilosBotones(botonExtract, colorAzul, miColorFondo, colorAzul);
            colorBotones.aplicarEstilosBotones(botonAdd, colorNaranja, miColorFondo, colorNaranja);
            colorBotones.aplicarEstilosBotones(botonEdit, miColorFondo, colorGris, miColorFondo);

        } else {
            gestionBotonesInferiores(false); // Desactivar los botones
            btnEdit.setText("Activar edición"); // Cambiar texto del botón

            JButton[] botonGray = {btnModify, btnDelete, btnExtract, btnAdd};
            colorBotones.aplicarEstilosBotones(botonEdit, colorGris, miColorFondo, colorGris);
            colorBotones.aplicarEstilosBotones(botonGray, colorGris, fondoGris, colorGris);

        }
    }

    private void gestionBotonesInferiores(boolean orden) {
        btnAdd.setEnabled(orden);
        btnDelete.setEnabled(orden);
        btnExtract.setEnabled(orden);
        btnModify.setEnabled(orden);
    }

    private void activacionBotonesSuperiores(boolean ad) {
        btnNext.setEnabled(ad);
        btnPrevious.setEnabled(ad);
    }

    private void desactivacionPasaPagina(boolean tf) {
        btnFirst.setEnabled(tf);
        btnLast.setEnabled(tf);
    }

    private void showInformation_ES() {
        String informationText = "<html><body style='width: 400px;'>"
                + "<h2>Instrucciones de uso.</h2>"
                + "<p>Este apartado te permite visualizar, editar, agregar y eliminar registros.</p>"
                + "<h3>Funcionalidades Principales</h3>"
                + "<h4>Paginación de Registros</h4>"
                + "<ul>"
                + "<li><b>Siguiente y Anterior:</b> Puedes navegar entre las páginas de registros utilizando los botones \"Siguiente\" y \"Anterior\".</li>"
                + "<li><b>Primera página y Última página:</b> Los botones \"Primera página\" y \"Última página\" te llevan a la primera y última página de registros, respectivamente.</li>"
                + "</ul>"
                + "<h4>Ejemplo de búsqueda:</h4>"
                + "<p>Buscar por Nombre, Apellido, DNI o Departamento: Utiliza el campo de búsqueda en la parte superior para buscar empleados por su nombre, apellido, DNI o departamento. Puedes seleccionar el criterio de búsqueda utilizando los botones de radio.</p>"
                + "<h4>Edición de Registros</h4>"
                + "<ul>"
                + "<li><b>Editar:</b> Haz clic en el botón \"Activar edición\" para activar la edición de registros.</li>"
                + "<li><b>Modificar:</b> Selecciona un campo en la tabla y haz clic en el botón \"Modificar\" para cambiar su valor.</li>"
                + "<li><b>Eliminar:</b> Selecciona un registro en la tabla y haz clic en el botón \"Borrar\" para eliminarlo de la base de datos.</li>"
                + "</ul>"
                + "<h4>Inserción de Registros</h4>"
                + "<p>Añadir: Haz clic en el botón \"Añadir\" para agregar un nuevo registro a la base de datos. Completa los campos requeridos en el formulario emergente y confirma la inserción.</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(this, informationText, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInformation_EN() {
        String informationText = "<html><body style='width: 400px;'>"
                + "<h2>Instructions for Use.</h2>"
                + "<p>This section allows you to view, edit, add, and delete records.</p>"
                + "<h3>Main Functionalities</h3>"
                + "<h4>Record Pagination</h4>"
                + "<ul>"
                + "<li><b>Next and Previous:</b> You can navigate between record pages using the \"Next\" and \"Previous\" buttons.</li>"
                + "<li><b>First Page and Last Page:</b> The \"First Page\" and \"Last Page\" buttons take you to the first and last record pages, respectively.</li>"
                + "</ul>"
                + "<h4>Search Example:</h4>"
                + "<p>Search by Name, Last Name, ID Number, or Department: Use the search field at the top to search for employees by their name, last name, ID number, or department. You can select the search criteria using the radio buttons.</p>"
                + "<h4>Record Editing</h4>"
                + "<ul>"
                + "<li><b>Edit:</b> Click the \"Enable Editing\" button to activate record editing.</li>"
                + "<li><b>Modify:</b> Select a field in the table and click the \"Modify\" button to change its value.</li>"
                + "<li><b>Delete:</b> Select a record in the table and click the \"Delete\" button to remove it from the database.</li>"
                + "</ul>"
                + "<h4>Record Insertion</h4>"
                + "<p>Add: Click the \"Add\" button to add a new record to the database. Fill in the required fields in the popup form and confirm the insertion.</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(this, informationText, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método para poner la primera letra en mayúsculas y el resto en
     * minúsculas.
     *
     * @param inputText
     * @return
     */
    public static String formatFirstLetter(String inputText) {
        if (!inputText.isEmpty()) {
            String firstChar = inputText.substring(0, 1).toUpperCase();
            String restOfString = inputText.substring(1).toLowerCase();
            return firstChar + restOfString;
        } else {
            return "";
        }
    }

    private void createButtons() {
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(colorFondoPanel);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Create buttons
        btnFirst = new JButton("Primera página");
        btnEdit = new JButton("Activar edición");
        btnModify = new JButton("Modificar");
        btnDelete = new JButton("Borrar");
        btnExtract = new JButton("Extraer");
        btnAdd = new JButton("Añadir");
        btnRefresh = new JButton("Limpiar búsqueda");
        createNavigationButtons();
        btnLast = new JButton("Última página");

        // Add action listeners to buttons
        btnFirst.addActionListener(e -> showFirstPage());
        btnEdit.addActionListener(e -> toggleBotones());
        btnModify.addActionListener(e -> {
            try {
                modifyRecord();
            } catch (SQLException ex) {
                Logger.getLogger(FormatoTablaGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnDelete.addActionListener(e -> {
            try {
                deleteRecord();
            } catch (SQLException ex) {
                Logger.getLogger(FormatoTablaGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnExtract.addActionListener(e -> extractRecord());
        btnAdd.addActionListener(e -> insertData());
        btnRefresh.addActionListener(e -> refreshTableData());
        btnLast.addActionListener(e -> showLastPage());

        // Add buttons to button panel
        buttonPanel.add(btnFirst);
        buttonPanel.add(btnExtract);
        buttonPanel.add(btnEdit);

        // Al decir si no es bbdd_empleados_softgenius conseguimos que no
        // aparezcan los botones de editar en esa bbdd.
        if (nivelUsuario < 2 || !bbdd.equals("bbdd_empleados_softgenius")) {
            if (!tabla.equals("empleado")) {
                buttonPanel.add(btnModify);
                buttonPanel.add(btnDelete);
                buttonPanel.add(btnAdd);
            } else {
                buttonPanel.add(btnEdit);
                buttonPanel.add(btnModify);
                buttonPanel.add(btnDelete);
            }
        }

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLast);

        // Create info label
        ImageIcon infoIcon = new ImageIcon("recursos/info.png");
        Image scaledInfoImage = infoIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledInfoIcon = new ImageIcon(scaledInfoImage);
        JLabel infoLabel = new JLabel();
        infoLabel.setIcon(scaledInfoIcon);
        infoLabel.setToolTipText("Información");
        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (idiomaActual.equals("English")) {
                    showInformation_EN();
                } else {
                    showInformation_ES();
                }
            }
        });

        // Add info label to the right of other buttons
        buttonPanel.add(infoLabel);
    }

    /**
     * Con este método damos formato y color a los botones llamando a la clase
     * colorBotones del paquete config.
     */
    private void cambiarColor() {
        ColorBotones colorBotones = new ColorBotones();
        Color miColorFondo = Color.white;
        Color fondoGris = Color.GRAY;
        Color colorNaranja = Color.ORANGE;
        Color colorGris = new Color(140, 140, 140);
        Color colorAzul = new Color(70, 130, 180);

        JButton[] botonGray = {btnModify, btnDelete, btnExtract, btnAdd};
        JButton[] botonRefresh = {btnRefresh};
        JButton[] botonesGenerales = {btnEdit};
        JButton[] botonLast = {btnFirst, btnLast};
        JButton[] botonBuscar = {searchButton};
        JButton[] botones = {btnNext, btnPrevious};

        colorBotones.aplicarEstilosBotones(botonGray, colorGris, fondoGris, colorGris);
        colorBotones.aplicarEstilosBotones(botonRefresh, colorAzul, miColorFondo, colorGris);
        colorBotones.aplicarEstilosBotones(botonLast, miColorFondo, colorNaranja, miColorFondo);
        colorBotones.aplicarEstilosBotones(botonesGenerales, colorGris, miColorFondo, colorGris);
        colorBotones.aplicarEstilosBotones(botonBuscar, miColorFondo, colorAzul, colorAzul);
        colorBotones.aplicarEstilosBotones(botones, miColorFondo, colorGris, miColorFondo);
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

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        btnFirst.setText(resourceBundle.getString("btnFirst"));
        btnModify.setText(resourceBundle.getString("btnModify"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnExtract.setText(resourceBundle.getString("btnExtract"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        btnNext.setText(resourceBundle.getString("btnNext"));
        btnPrevious.setText(resourceBundle.getString("btnPrevious"));
        btnLast.setText(resourceBundle.getString("btnLast"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnRefresh.setText(resourceBundle.getString("btnLimpiarDatos"));
        searchButton.setText(resourceBundle.getString("searchButton"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
