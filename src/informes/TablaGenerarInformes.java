package informes;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import softgenius.ConexionBD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import libsql3.softgenius.GestorTablas;
import static libsql3.softgenius.GestorTablas.contadorRegistrosPorBusqueda;
import static libsql3.softgenius.GestorTablas.obtenerRangoDeFilas;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class TablaGenerarInformes extends JPanel {

    private JPanel panel;
    private JTable table;
    private JButton btnFirst, btnNext, btnPrevious, btnLast, btnRefresh, btnExportPDF, searchButton, btnCrearInforme, btnInformeVacaciones;
    private JTextField searchField;
    private int currentPage = 1;
    private JLabel contadorRegistros;
    private String idiomaActual = "Spanish";
    private final int recordsPerPage = 15;
    private List<JRadioButton> radioButtonsList = new ArrayList<>();
    private String bbdd;
    private String tabla;
    private Color colorFondoPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();//Linea 346

    public TablaGenerarInformes(String bbdd, String tabla) throws SQLException {
        this.bbdd = bbdd;
        this.tabla = tabla;
        initComponents();
        showData();
    }

    private void initComponents() throws SQLException {

        panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondoPanel);
        add(panel);

        createTable();
        createSearchBar();
        createButtons();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
        calcularTotalRegistros();
        cambiarColor();
    }

    private void calcularTotalRegistros() {
        int totalRecords = getTotalRecords();
        contadorRegistros.setText("Total registros: " + totalRecords + " ");
    }

    private void createSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(colorFondoPanel);
        panel.add(searchPanel, BorderLayout.NORTH);
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Buscar");

        // Obtenemos los nombres de las columnas de la base de datos
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

        // Creamos un array de JRadioButton para almacenar los botones
        JRadioButton[] radioButtons = new JRadioButton[columnNames.length];

        searchButton.addActionListener(e -> filterTable());
        searchPanel.add(searchButton);

        contadorRegistros = new JLabel();
        searchPanel.add(contadorRegistros);
        searchField.addActionListener(e -> filterTable());
    }

    /**
     * Método para mostrar el total de registros de la búsqueda.
     *
     * @param totalRecords
     */
    private void showAllMatchingRecords(int totalRecords, String objetoABuscar) {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion(bbdd)) {
            DatabaseMetaData metaData = conexionBBDD.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tabla, null);
            List<String> columnNames = new ArrayList<>();
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
            String sql = "SELECT * FROM " + tabla + " WHERE " + objetoABuscar + " LIKE ?";
            try (PreparedStatement statement = conexionBBDD.prepareStatement(sql)) {
                statement.setString(1, "%" + searchField.getText().trim() + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    while (resultSet.next()) {
                        List<Object> rowData = new ArrayList<>();
                        for (String columnName : columnNames) {
                            Object value = resultSet.getObject(columnName);
                            rowData.add(value);
                        }
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
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        String text = searchField.getText().trim();

        if (text.length() == 0) {
            sorter.setRowFilter(null);
            showData();
            calcularTotalRegistros();
            activacionBotonesSuperiores(true);
            desactivacionPasaPagina(true);
        } else {
            desactivacionPasaPagina(false);
            String objetoABuscar = "";
            for (JRadioButton radioButton : radioButtonsList) {
                if (radioButton.isSelected()) {
                    objetoABuscar = radioButton.getText();
                    break;
                }
            }
            String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + text, getColumnIndex(objetoABuscar, columnNames));
            sorter.setRowFilter(rowFilter);

            currentPage = 1;
            int totalRecords = contadorRegistrosPorBusqueda(bbdd, tabla, text, objetoABuscar);
            contadorRegistros.setText("Total registros: " + totalRecords + " ");
            showAllMatchingRecords(totalRecords, objetoABuscar);

            if (totalRecords <= 15) {
                activacionBotonesSuperiores(false);
            }
        }
    }

    /**
     * Método para obtener el índice de la columna en función del objeto a
     * buscar.
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
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que todas las celdas no sean editables
            }
        };
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);

        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        table = new JTable(model);
        table.setRowSorter(new TableRowSorter<>(model));
        table.setBackground(new Color(240, 240, 240));

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
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            table.getColumnModel().getColumn(i).setPreferredWidth(150); // Set preferred width to 150 for all columns
        }
        Font font = new Font("Arial", Font.PLAIN, 14);
        table.setFont(font);
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Método en el que se crean los elementos.
     */
    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(colorFondoPanel);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnExportPDF = new JButton("Imprimir tabla");
        btnCrearInforme = new JButton("Imprimir registro");
        btnInformeVacaciones = new JButton("Informe vacaciones");
        btnFirst = new JButton("Primera página");
        btnRefresh = new JButton("Limpiar búsqueda");
        createNavigationButtons();
        btnLast = new JButton("Última página");

        btnExportPDF.addActionListener(e -> exportarTabla());
        btnCrearInforme.addActionListener(e -> exportarFila());
        btnInformeVacaciones.addActionListener(e -> {
            try {
                imprimirInformeVacaciones();
            } catch (IOException ex) {
                Logger.getLogger(TablaGenerarInformes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TablaGenerarInformes.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnFirst.addActionListener(e -> showFirstPage());
        btnRefresh.addActionListener(e -> refreshTableData());
        btnLast.addActionListener(e -> showLastPage());

        buttonPanel.add(btnFirst);
        buttonPanel.add(btnExportPDF);
        buttonPanel.add(btnCrearInforme);
        if (tabla.equals("empleado")) {
            buttonPanel.add(btnInformeVacaciones);
        }
        buttonPanel.add(btnLast);
        buttonPanel.add(btnRefresh);

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
        buttonPanel.add(infoLabel);
    }

    /**
     * Método para imprimir un registro en concreto.
     */
    private void exportarFila() {
        String userProfile = System.getenv("USERPROFILE");

        try {
            // Crear directorio si no existe
            File directory = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius");
            if (!directory.exists()) {
                directory.mkdirs(); // Crear directorio si no existe
            }

            // Crear un nombre único para el archivo del informe con fecha y hora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(Calendar.getInstance().getTime());
            String fileName = userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius" + File.separator + "informe_" + timestamp + "_fila.pdf";

            // Obtener nombres de columnas y datos de la tabla
            String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numColumns = model.getColumnCount();

            // Obtener la fila seleccionada
            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada == -1) {
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "Please select a row to generate the report.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para generar el informe.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                return; // Salir del método si no hay fila seleccionada

            }

            // Inicializar el documento PDF con una página más ancha
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(1100, 600)); // Ancho: 800, Alto: 600
                document.addPage(page);

                // Cargar la fuente personalizada
                PDType0Font font = PDType0Font.load(document, new File("fuente.ttf"));

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(font, 12);

                    // Escribir encabezado en el informe PDF
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 550); // Ajusta según tus necesidades
                    contentStream.showText("Informe generado el " + timestamp);
                    contentStream.newLine();
                    contentStream.newLine();

                    // Escribir datos de la fila seleccionada como una frase en el informe PDF
                    StringBuilder rowData = new StringBuilder("Registro " + (filaSeleccionada + 1) + ": ");
                    for (int col = 0; col < numColumns; col++) {
                        Object cellValue = model.getValueAt(filaSeleccionada, col);
                        String columnName = columnNames[col];
                        String cellText = (cellValue != null) ? cellValue.toString() : "";

                        // Agregar el nombre de la columna y el valor a la frase
                        rowData.append(columnName).append(": ").append(cellText);

                        // Si no es la última columna, agregar una coma y un espacio
                        if (col < numColumns - 1) {
                            rowData.append(", ");
                        }
                    }

                    // Escribir la fila seleccionada como un párrafo de texto
                    contentStream.newLineAtOffset(0, -20); // Saltar a la siguiente línea
                    contentStream.showText(rowData.toString());

                    contentStream.endText();
                }

                document.save(fileName);

                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "The report has been generated successfully in: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Se ha generado el informe correctamente en: " + fileName, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                // Abrir el archivo de informe después de que el usuario acepte el mensaje de éxito
                try {
                    File informeFile = new File(fileName);
                    if (informeFile.exists()) {
                        Desktop.getDesktop().open(informeFile);
                    } else {
                        if (idiomaActual.equals("English")) {
                            JOptionPane.showMessageDialog(this, "Report file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "El archivo de informe no existe", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Error generating the report.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al generar el informe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método para imprimir en PDF una tabla seleccionada.
     */
    private void exportarTabla() {
        String userProfile = System.getenv("USERPROFILE");

        try {
            // Crear directorio si no existe
            File directory = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius");
            if (!directory.exists()) {
                directory.mkdirs(); // Crear directorio y subdirectorios si no existen
            }

            // Crear un nombre único para el archivo PDF con fecha y hora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(Calendar.getInstance().getTime());
            String fileName = userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius" + File.separator + "informe_" + timestamp + "_tabla.pdf";

            // Definir el tamaño de la página del PDF
            PDRectangle maxPageSize = new PDRectangle(1500, 1200); // Ajusta según tus necesidades

            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();

            // Cargar fuente personalizada
            PDType0Font font = PDType0Font.load(document, new File("fuente.ttf"));

            // Obtener nombres de columnas y datos de la tabla
            String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numRows = model.getRowCount();
            int numColumns = model.getColumnCount();

            // Definir el ancho mínimo y máximo de las columnas
            float minWidth = 155; // Ancho mínimo de la columna
            float maxWidth = maxPageSize.getWidth() / numColumns; // Ancho máximo de la columna

            // Definir los anchos de las columnas basados en el contenido más largo
            float[] columnWidths = new float[numColumns];
            for (int col = 0; col < numColumns; col++) {
                float maxTextWidth = minWidth; // Inicializar el ancho máximo de texto como el ancho mínimo de columna
                // Calcular el ancho máximo de texto en la columna actual
                for (int row = 0; row < numRows; row++) {
                    Object cellValue = model.getValueAt(row, col);
                    String cellText = (cellValue != null) ? cellValue.toString() : "";
                    float textWidth = font.getStringWidth(cellText) / 1000 * 12; // Tamaño de fuente 12pt
                    maxTextWidth = Math.max(maxTextWidth, textWidth);
                }
                // Establecer el ancho de columna como el máximo entre el ancho mínimo y el ancho máximo de texto
                columnWidths[col] = Math.min(Math.max(minWidth, maxTextWidth + 20), maxWidth); // Ajusta según tus necesidades
            }

            // Definir la altura de fila para la tabla
            float rowHeight = 25; // Ajusta según tus necesidades

            // Inicializar PDPageContentStream y PDDocument
            PDPage page = new PDPage(maxPageSize);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Establecer la posición inicial de la tabla más abajo en la página
            float startY = maxPageSize.getHeight() - 500; // Ajusta esta posición según tus necesidades

            // Dibujar encabezados de columna en la primera página
            float currentX = 20;
            int maxColumnsPerPage = 8; // Máximo de columnas por página
            for (int col = 0; col < Math.min(numColumns, maxColumnsPerPage); col++) {
                drawCell(contentStream, font, columnNames[col], currentX, startY, columnWidths[col], rowHeight, true);
                currentX += columnWidths[col];
            }

            // Dibujar datos de la tabla en la primera página
            for (int row = 0; row < numRows; row++) {
                currentX = 20;
                startY -= rowHeight; // Avanzar a la siguiente fila
                for (int col = 0; col < Math.min(numColumns, maxColumnsPerPage); col++) {
                    Object cellValue = model.getValueAt(row, col);
                    String cellText = (cellValue != null) ? cellValue.toString() : "";
                    drawCell(contentStream, font, cellText, currentX, startY, columnWidths[col], rowHeight, false);
                    currentX += columnWidths[col];
                }
            }

            // Si hay más de ocho columnas, crear una nueva página y dibujar las columnas adicionales
            if (numColumns > maxColumnsPerPage) {
                page = new PDPage(maxPageSize);
                document.addPage(page);
                contentStream.close(); // Cerrar el PDPageContentStream actual

                contentStream = new PDPageContentStream(document, page);
                currentX = 20;
                startY = maxPageSize.getHeight() - 30;

                // Dibujar encabezados de columna en la segunda página
                for (int col = maxColumnsPerPage; col < numColumns; col++) {
                    drawCell(contentStream, font, columnNames[col], currentX, startY, columnWidths[col], rowHeight, true);
                    currentX += columnWidths[col];
                }

                // Dibujar datos de la tabla en la segunda página
                for (int row = 0; row < numRows; row++) {
                    currentX = 20;
                    startY -= rowHeight; // Avanzar a la siguiente fila
                    for (int col = maxColumnsPerPage; col < numColumns; col++) {
                        Object cellValue = model.getValueAt(row, col);
                        String cellText = (cellValue != null) ? cellValue.toString() : "";
                        drawCell(contentStream, font, cellText, currentX, startY, columnWidths[col], rowHeight, false);
                        currentX += columnWidths[col];
                    }
                }
            }

            // Cerrar el PDPageContentStream
            contentStream.close();

            // Cerrar el documento y guardar el archivo PDF
            document.save(fileName);
            document.close();

            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "The PDF has been generated correctly in: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Se ha generado el PDF correctamente en: " + fileName, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            // Abrir el archivo PDF después de que el usuario acepte el mensaje de éxito
            try {
                File pdfFile = new File(fileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Report file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "El archivo de informe no existe", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Error generating the PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método para hacer le informe de vacaciones en una redacción tomando los
     * datos personales.
     */
    private void imprimirInformeVacaciones() throws IOException, SQLException {

        String selectedCity = "";
        String selectedSeccion = "";

        /////////////////////////////////////////////////////
        // Array para rellenar las ciudades//
        /////////////////////////////////////////////////////
        ArrayList<String> ciudades = new ArrayList<>();
        String baseDeDatos = "bbdd_config_softgenius";
        // Conectar a la base de datos y obtener las ciudades
        try (Connection conexionBBDD = ConexionBD.obtenerConexion(baseDeDatos)) {
            String query = "SELECT ciudad FROM sedes";
            try (PreparedStatement pstmt = conexionBBDD.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String ciudad = rs.getString("ciudad");
                    ciudades.add(ciudad);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Crear el JComboBox con las ciudades obtenidas
        JComboBox<String> comboBoxCiudades = new JComboBox<>(ciudades.toArray(new String[0]));

        // Crear el panel para el JOptionPane
        JPanel panelCiudades = new JPanel(new BorderLayout());
        panelCiudades.add(new JLabel("Selecciona una ciudad:"), BorderLayout.NORTH);
        panelCiudades.add(comboBoxCiudades, BorderLayout.CENTER);

        // Mostrar el JOptionPane para seleccionar ciudad
        int resultCiudad;
        if (idiomaActual.equals("English")) {
            resultCiudad = JOptionPane.showConfirmDialog(null, panelCiudades, "City Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        } else {
            resultCiudad = JOptionPane.showConfirmDialog(null, panelCiudades, "Selección de Ciudad", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        }
        if (resultCiudad == JOptionPane.OK_OPTION) {
            selectedCity = (String) comboBoxCiudades.getSelectedItem();

            // Obtener las secciones de la base de datos bbdd_empleados_softgenius
            ArrayList<String> secciones = new ArrayList<>();
            try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
                String query = "SELECT seccion FROM departamento";
                try (PreparedStatement pstmt = conexionBBDD.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String seccion = rs.getString("seccion");
                        secciones.add(seccion);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            /////////////////////////////////////////////////////
            // Array para rellenar los departamentos //
            /////////////////////////////////////////////////////
            // Crear el JComboBox con las secciones obtenidas
            JComboBox<String> comboBoxSecciones = new JComboBox<>(secciones.toArray(new String[0]));

            // Crear el panel para el JOptionPane
            JPanel panelSecciones = new JPanel(new BorderLayout());
            panelSecciones.add(new JLabel("Selecciona una sección:"), BorderLayout.NORTH);
            panelSecciones.add(comboBoxSecciones, BorderLayout.CENTER);

            // Mostrar el JOptionPane para seleccionar sección
            int resultSeccion;
            if (idiomaActual.equals("English")) {
                resultSeccion = JOptionPane.showConfirmDialog(null, panelSecciones, "Section Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            } else {
                resultSeccion = JOptionPane.showConfirmDialog(null, panelSecciones, "Selección de Sección", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            }
            if (resultSeccion == JOptionPane.OK_OPTION) {
                selectedSeccion = (String) comboBoxSecciones.getSelectedItem();
            } else {
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "No section selected", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se seleccionó ninguna sección", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "No city selected", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se seleccionó ninguna ciudad", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Obtener nombres de columnas y datos de la tabla
        String[] columnNames = GestorTablas.obtenerNombresColumnas(bbdd, tabla);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int numColumns = model.getColumnCount();

        // Obtener la fila seleccionada
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada == -1) {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Please select a row to generate the report.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para generar el informe.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
            return; // Salir del método si no hay fila seleccionada
        }

        /////////////////////////////////////////////////////
        // Obtener datos de la fila elegida //
        /////////////////////////////////////////////////////
        // Obtener los valores específicos de la fila seleccionada
        String nombreString = "";
        String apellidosString = "";
        String dniString = "";
        String vacaciones_totalesString = "";
        String vacaciones_disfrutadasString = "";

        for (int col = 0; col < numColumns; col++) {
            String columnName = columnNames[col];
            Object cellValue = model.getValueAt(filaSeleccionada, col);
            String cellText = (cellValue != null) ? cellValue.toString() : "";

            switch (columnName) {
                case "Nombre":
                    nombreString = cellText;
                    break;
                case "Apellido":
                    apellidosString = cellText;
                    break;
                case "DNI":
                    dniString = cellText;
                    break;
                case "Vacaciones_totales":
                    vacaciones_totalesString = cellText;
                    break;
                case "Vacaciones_disfrutadas":
                    vacaciones_disfrutadasString = cellText;
                    break;
            }
        }

        InformeVacaciones informeVacaciones = new InformeVacaciones();
        informeVacaciones.informeVacaciones(selectedCity, selectedSeccion, nombreString, apellidosString, dniString, vacaciones_totalesString, vacaciones_disfrutadasString);

    }

    private void drawCell(PDPageContentStream contentStream, PDType0Font font, String text, float x, float y, float width, float height, boolean isHeader) throws IOException {
        // Establecer color y estilo de borde
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(1f);

        // Dibujar borde de la celda
        contentStream.addRect(x, y, width, height);
        contentStream.stroke();

        // Ajustar posición para centrar el texto verticalmente en la celda
        float textX = x + 2; // Margen izquierdo
        float textY = y + height / 2 - 5; // Alineación vertical en el centro

        // Establecer color de fondo si es un encabezado de columna
        if (isHeader) {
            contentStream.setNonStrokingColor(Color.LIGHT_GRAY); // Color de fondo para encabezados de columna
            contentStream.addRect(x, y, width, height);
            contentStream.fill();
        }

        // Establecer el color de la fuente y el tamaño
        contentStream.setNonStrokingColor(Color.BLACK);
        if (isHeader) {
            contentStream.setFont(font, 12); // Tamaño de fuente para encabezados de columna
        } else {
            contentStream.setFont(font, 14); // Tamaño de fuente para datos de la tabla
        }

        // Escribir texto en la celda
        contentStream.beginText();
        contentStream.newLineAtOffset(textX, textY);
        contentStream.showText(text);
        contentStream.endText();
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
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "You are on the last page");
            } else {
                JOptionPane.showMessageDialog(this, "Estás en la última página");
            }
        }
    }

    private void previousPage() {
        // Navigate to the previous page of records
        if (currentPage > 1) {
            currentPage--;
            showData();
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "You are on the first page");
            } else {
                JOptionPane.showMessageDialog(this, "Estás en la primera página");
            }
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

    private String findPrimaryKeyColumn(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
        if (rs.next()) {
            return rs.getString("COLUMN_NAME");
        } else {
            return null;
        }
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
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "No records found.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron registros.", "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Method to show the first page of records
    private void showFirstPage() {
        if (currentPage != 1) {
            currentPage = 1;
            showData();
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Already on the first page.", "First Page", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ya estás en la primera página.", "Primera Página", JOptionPane.INFORMATION_MESSAGE);
            }
        }
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
        InformacionBotonInfo informacionBotonInfo = new InformacionBotonInfo();
        informacionBotonInfo.showInformation_ES();
    }

    private void showInformation_EN() {
        InformacionBotonInfo informacionBotonInfo = new InformacionBotonInfo();
        informacionBotonInfo.showInformation_EN();
    }

    /**
     * ***************************************************************************
     * /////////////////////////////////////////////////////////////////////////
     *
     * COLOR BOTONES
     *
     * /////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */
    /**
     * Con este método damos formato y color a los botones llamando a la clase
     * colorBotones del paquete config.
     */
    private void cambiarColor() {
        ColorBotones colorBotones = new ColorBotones();
        Color miColorFondo = Color.white;
        Color colorVerde = new Color(39, 174, 96);
        Color colorNaranja = Color.ORANGE;
        Color colorGris = new Color(140, 140, 140);
        Color colorAzul = new Color(87, 155, 212);
        Color colorRojo = new Color(255, 0, 0);
        Color colorVioleta = new Color(91, 44, 111);

        JButton[] botonExportPDF = {btnExportPDF};
        JButton[] botonRefresh = {btnRefresh};
        JButton[] botonImprimirUnInforme = {btnCrearInforme};
        JButton[] botonLast = {btnFirst, btnLast};
        JButton[] botonBuscar = {searchButton};
        JButton[] botones = {btnNext, btnPrevious};
        JButton[] botonDiasVacaciones = {btnInformeVacaciones};

        colorBotones.aplicarEstilosBotones(botonExportPDF, colorVioleta, miColorFondo, colorVioleta);
        colorBotones.aplicarEstilosBotones(botonImprimirUnInforme, colorVerde, miColorFondo, colorVerde);
        colorBotones.aplicarEstilosBotones(botonDiasVacaciones, colorRojo, miColorFondo, colorRojo);
        colorBotones.aplicarEstilosBotones(botonRefresh, colorAzul, miColorFondo, colorGris);
        colorBotones.aplicarEstilosBotones(botonLast, miColorFondo, colorNaranja, miColorFondo);
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
        btnNext.setText(resourceBundle.getString("btnNext"));
        btnPrevious.setText(resourceBundle.getString("btnPrevious"));
        btnLast.setText(resourceBundle.getString("btnLast"));
        btnRefresh.setText(resourceBundle.getString("btnLimpiarDatos"));
        searchButton.setText(resourceBundle.getString("searchButton"));
        btnCrearInforme.setText(resourceBundle.getString("btnCrearInforme"));
        btnInformeVacaciones.setText(resourceBundle.getString("btnInformeVacaciones"));
        btnExportPDF.setText(resourceBundle.getString("btnExportPDF"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
