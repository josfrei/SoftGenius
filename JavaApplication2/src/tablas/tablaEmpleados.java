/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tablas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class tablaEmpleados extends JPanel {

    // Declare components
    private JPanel panel;
    private JTable table;
    private JButton btnFirst, btnModify, btnDelete, btnExtract, btnAdd, btnNext, btnPrevious, btnLast, btnEdit;
    private JRadioButton rBNombre, rBApellido, rBDni, rBDepartamento;
    private JTextField searchField;
    private int currentPage = 1;
    private JLabel contadorRegistros;

    private final int recordsPerPage = 15;

    public tablaEmpleados() {
        // Initialize components and show data
        initComponents();
        showData();
    }

    private void initComponents() {
        /* // Set frame properties
        setTitle("BBDD empleados");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

        // Create main panel
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(200, 230, 201));
        add(panel);

        // Initialize components
        createTable();
        createSearchBar();
        createButtons();
        gestionBotonesInferiores(false);
        calcularTotalRegistros();

        /* // Set the preferred size of the JFrame
        setPreferredSize(new Dimension(1000, 700)); // Ajusta las dimensiones según sea necesario
        pack(); // Empaquetar los componentes para que se ajusten al tamaño preferido*/
    }

    private void calcularTotalRegistros() {
        // Set the total number of records in the label
        int totalRecords = getTotalRecords();
        contadorRegistros.setText("Total registros: " + totalRecords + " ");
    }

    private void createSearchBar() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(searchPanel, BorderLayout.NORTH);

        // Create search field
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        // Create search button
        JButton searchButton = new JButton("Buscar");
        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> filterTable());
        searchPanel.add(searchButton);

        // Creamos label del contador
        contadorRegistros = new JLabel();
        searchPanel.add(contadorRegistros);

        // Add action listener to search field
        searchField.addActionListener(e -> filterTable());
    }

    /**
     * Contador de registros que coincide con la búsqueda
     *
     * @param searchText
     * @return
     */
    private int getTotalMatchingRecords(String searchText, String objetoABuscar) {
        // Recuperar el total de registros coincidentes
        int totalRecords = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "")) {
            String sql = "SELECT COUNT(*) AS total FROM empleado WHERE " + objetoABuscar + " LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%" + searchText + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalRecords = resultSet.getInt("total");
                    }
                }
            }
        } catch (SQLException e) {
            // Handle the exception appropriately
        }
        return totalRecords;
    }

    /**
     * Método para mostrar el total de registros de la búsqueda
     *
     * @param totalRecords
     */
    private void showAllMatchingRecords(int totalRecords, String objetoABuscar) {
        // Show all matching records based on search text
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "")) {
            String sql = "SELECT * FROM empleado WHERE " + objetoABuscar + " LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%" + searchField.getText().trim() + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    while (resultSet.next()) {
                        // Retrieve data from the result set and add to the table model
                        int employeeID = resultSet.getInt("EmpleadoID");
                        String firstName = resultSet.getString("Nombre");
                        String lastName = resultSet.getString("Apellido");
                        String dni = resultSet.getString("DNI");
                        String department = resultSet.getString("Departamento");

                        // Add row to the table model
                        model.addRow(new Object[]{
                            employeeID,
                            firstName,
                            lastName,
                            dni,
                            department,});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filtra los datos de la tabla que se muestran por pantalla.
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
            if (rBNombre.isSelected()) {
                objetoABuscar = "Nombre";
            } else if (rBApellido.isSelected()) {
                objetoABuscar = "Apellido";
            } else if (rBDni.isSelected()) {
                objetoABuscar = "DNI";
            } else if (rBDepartamento.isSelected()) {
                objetoABuscar = "Departamento";
            }

            // Apply row filter with search criteria
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + text, getColumnIndex(objetoABuscar));
            sorter.setRowFilter(rowFilter);

            // Reset current page and update table with matching records
            currentPage = 1;
            int totalRecords = getTotalMatchingRecords(text, objetoABuscar);
            contadorRegistros.setText("Total registros: " + totalRecords + " ");
            showAllMatchingRecords(totalRecords, objetoABuscar);

            // Disable next and previous buttons if total records <= 15
            if (totalRecords <= 15) {
                activacionBotonesSuperiores(false);
            }
        }
    }
// Método para obtener el índice de la columna en función del objeto a buscar

    private int getColumnIndex(String objetoABuscar) {
        switch (objetoABuscar) {
            case "Nombre":
                return 1;
            case "Apellido":
                return 2;
            case "DNI":
                return 3;
            case "Departamento":
                return 4;
            default:
                return -1;
        }
    }

    /**
     * Método para crear la tabla que se muestra en pantalla
     */
    private void createTable() {
        // Create the table with specified column classes and properties
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Define column classes
                if (columnIndex == 0) {
                    return Integer.class;
                } else if (columnIndex >= 7 && columnIndex <= 8) {
                    return Double.class;
                } else if (columnIndex == 4 || columnIndex == 9 || columnIndex == 10) {
                    return java.sql.Date.class;
                } else {
                    return String.class;
                }
            }
        };

        // Define column names
        String[] columnNames = {"Empleado", "Nombre", "Apellido", "DNI", "Departmento"};

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
        }

        // Set font and row height for the table
        Font font = new Font("Arial", Font.PLAIN, 14);
        table.setFont(font);
        table.setRowHeight(30);

        // Set preferred column widths
        TableColumnModel columnModel = table.getColumnModel();
        int[] columnWidths = {100, 150, 150, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Set default cell renderer for center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Calcular el ancho total de la tabla más el ancho del botón de radio
        int totalWidth = table.getPreferredSize().width + 300; // Ajusta el 100 según sea necesario

        // Set auto resize mode and preferred size for scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(totalWidth, 300)); // Ajusta el alto según sea necesario
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    private void createButtons() {
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // Create buttons
        btnFirst = new JButton("Primera página");
        btnEdit = new JButton("Activar edición");
        btnModify = new JButton("Modificar");
        btnDelete = new JButton("Borrar");
        btnExtract = new JButton("Extraer");
        btnAdd = new JButton("Añadir");
        JButton btnRefresh = new JButton("Limpiar búsqueda");
        createNavigationButtons();
        btnLast = new JButton("Última página");
        // Set button backgrounds and foregrounds
        btnFirst.setBackground(Color.YELLOW);
        btnModify.setBackground(new Color(60, 179, 113));
        btnDelete.setBackground(new Color(220, 20, 60));
        btnExtract.setBackground(new Color(65, 105, 225));
        btnAdd.setBackground(new Color(255, 140, 0));
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnLast.setBackground(Color.ORANGE);
        btnFirst.setForeground(Color.BLACK);
        btnModify.setForeground(Color.WHITE);
        btnDelete.setForeground(Color.WHITE);
        btnExtract.setForeground(Color.WHITE);
        btnAdd.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);
        btnLast.setForeground(Color.BLACK);
        // Add action listeners to buttons
        btnFirst.addActionListener(e -> showFirstPage());
        btnEdit.addActionListener(e -> toggleBotones());
        btnModify.addActionListener(e -> {
            try {
                modifyRecord();
            } catch (SQLException ex) {
                Logger.getLogger(tablaEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnDelete.addActionListener(e -> {
            try {
                deleteRecord();
            } catch (SQLException ex) {
                Logger.getLogger(tablaEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnExtract.addActionListener(e -> extractRecord());
        btnAdd.addActionListener(e -> insertData());
        btnRefresh.addActionListener(e -> refreshTableData());
        btnLast.addActionListener(e -> showLastPage());
        // Add buttons to button panel
        buttonPanel.add(btnFirst);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnExtract);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLast);
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

    /**
     * Método para la ventana de añadir persona.
     */
    /*private void showAddPersonDialog() {
        // Display dialog for adding a new person
        JDialog dialog = new JDialog(this, "Añadir persona", true);
        dialog.setLayout(new GridLayout(0, 2));

        // Create labels and text fields for input
        JLabel lblFirstName = new JLabel("Nombre:");
        JTextField txtFirstName = new JTextField();
        JLabel lblLastName = new JLabel("Apellido:");
        JTextField txtLastName = new JTextField();
        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();
        JLabel lblDepartment = new JLabel("Department:");
        JTextField txtDepartment = new JTextField();

        // Create confirm button and add action listener
        JButton btnConfirm = new JButton("Add");
        btnConfirm.addActionListener((ActionEvent e) -> {
            String firstName1 = txtFirstName.getText();
            String lastName1 = txtLastName.getText();
            String dni = txtDni.getText();
            String department1 = txtDepartment.getText();

            addPersonToTable(firstName1, lastName1, dni, department1);
            dialog.dispose();
        });

        // Add labels, text fields, and confirm button to dialog
        dialog.add(lblFirstName);
        dialog.add(txtFirstName);
        dialog.add(lblLastName);
        dialog.add(txtLastName);
        dialog.add(lblDni);
        dialog.add(txtDni);
        dialog.add(lblDepartment);
        dialog.add(txtDepartment);
        dialog.add(btnConfirm);

        // Set dialog size and display it
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addPersonToTable(String firstName, String lastName, String dni, String department) {
        // Validate input fields
        if (!validateInput(firstName, lastName, dni, department)) {
            // Show error message if input validation fails
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add new row to the table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{null, firstName, lastName, dni, department});

        // Call stored procedure to add employee to database
        callAddEmployeeProcedure(firstName, lastName, dni, department);
    }*/
    private void createNavigationButtons() {
        // Create navigation buttons for paging
        btnNext = new JButton("Siguiente");
        btnPrevious = new JButton("Anterior");
        rBNombre = new JRadioButton("Nombre");
        rBApellido = new JRadioButton("Apellido");
        rBDni = new JRadioButton("DNI");
        rBDepartamento = new JRadioButton("Departamento");

        // Add action listeners to navigation buttons
        btnNext.addActionListener(e -> nextPage());
        btnPrevious.addActionListener(e -> previousPage());

        rBNombre.setSelected(true);

        // Agrupar los botones de radio en un ButtonGroup
        ButtonGroup group = new ButtonGroup();
        group.add(rBNombre);
        group.add(rBApellido);
        group.add(rBDni);
        group.add(rBDepartamento);

        // Add navigation buttons to the button panel
        JPanel buttonPanel = (JPanel) panel.getComponent(1);
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnNext);
        buttonPanel.add(rBNombre);
        buttonPanel.add(rBApellido);
        buttonPanel.add(rBDni);
        buttonPanel.add(rBDepartamento);
    }

    private int getTotalRecords() {
        // Get the total number of records in the database
        int totalRecords = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "")) {
            String sql = "SELECT COUNT(*) AS total FROM empleado";
            try (PreparedStatement statement = conn.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "")) {
            String sql = "SELECT * FROM empleado LIMIT ? OFFSET ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, recordsPerPage);
                statement.setInt(2, offset);
                try (ResultSet resultSet = statement.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    while (resultSet.next()) {

                        // Add row to the table model
                        model.addRow(new Object[]{
                            resultSet.getInt("EmpleadoID"),
                            resultSet.getString("Nombre"),
                            resultSet.getString("Apellido"),
                            resultSet.getString("DNI"),
                            resultSet.getString("Departamento"),});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "");
            // Obtener los nombres de las columnas de la tabla
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, "empleado", null);
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

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(columnNames[i] + ":"));
                JTextField textField = new JTextField();
                inputPanel.add(textField);
                inputValues[i] = textField;
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Ingrese los datos", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // Insertar los valores en la tabla, excluyendo el primer campo
                String insertQuery = "INSERT INTO empleado (";
                for (int i = 1; i < columnCount; i++) { // Comenzar desde el segundo campo
                    insertQuery += columnNames[i];
                    if (i < columnCount - 1) {
                        insertQuery += ",";
                    }
                }
                insertQuery += ") VALUES (";
                for (int i = 1; i < columnCount; i++) { // Comenzar desde el segundo campo
                    insertQuery += "?,";
                }
                insertQuery = insertQuery.substring(0, insertQuery.length() - 1); // Eliminar la última coma
                insertQuery += ")";

                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                for (int i = 1; i < columnCount; i++) { // Comenzar desde el segundo campo
                    pstmt.setObject(i, ((JTextField) inputValues[i]).getText());
                }
                pstmt.executeUpdate();
                pstmt.close();

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
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "");
        // Obtener la fila seleccionada
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Implementar lógica para editar el campo seleccionado
            String columnName = table.getColumnName(table.getSelectedColumn());
            Object newValue = JOptionPane.showInputDialog(this, "Nuevo valor para " + columnName + ":");
            if (newValue != null) {
                int id = (int) table.getValueAt(selectedRow, 0);
                String updateQuery = "UPDATE clientes_informáticos SET " + columnName + " = ? WHERE ID_Cliente = ?";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setObject(1, newValue);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    pstmt.close();
                    showData(); // Actualizar la tabla después de la edición
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para editar.");
        }
    }

    private void deleteRecord() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "");

        // Obtener la fila seleccionada
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Implementar lógica para eliminar la fila seleccionada
            int id = (int) table.getValueAt(selectedRow, 0);
            String deleteQuery = "DELETE FROM empleado WHERE EmpleadoID = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                pstmt.close();
                showData(); // Actualizar la tabla después de la eliminación
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
        }
    }

    private void callDeleteEmployeeProcedure(int employeeID) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_empleados_softgenius", "root", "")) {
            String sql = "CALL DeleteEmployee(?)";
            try (CallableStatement statement = conn.prepareCall(sql)) {
                statement.setInt(1, employeeID);
                statement.execute();
                System.out.println("Empleado con ID " + employeeID + " ha sido eliminado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    // Main method to launch the application
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new tablaEmpleados().setVisible(true));
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
        // Cambiar el estado de los botones y actualizar el texto del botón btnEdit
        botonesActivados = !botonesActivados; // Cambiar el estado

        if (botonesActivados) {
            gestionBotonesInferiores(true); // Activar los botones
            btnEdit.setText("Desactivar edición"); // Cambiar texto del botón
        } else {
            gestionBotonesInferiores(false); // Desactivar los botones
            btnEdit.setText("Activar edición"); // Cambiar texto del botón
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
}
