package informes;

import com.mysql.cj.jdbc.DatabaseMetaData;
import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;
import softgenius.ConexionBD;
import config.NivelUsuario;
import idioma.Idiomas;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelGenerarGraficosRedondos extends JPanel implements ActionListener {

    private JButton btnGenerarGraficos = new JButton("Generar gráfico");
    private JButton btnGenerarPDF = new JButton("Generar PDF");
    private JLabel lblListaBbdd, lblListaTablas;
    private JPanel panel1, panel2, panel3, panel4;
    private static JComboBox<String> listaBbdd;
    private JComboBox<String> comboTablas;
    private Map<String, JCheckBox> columnasCheckBoxes = new HashMap<>();
    private String idiomaActual = "Spanish";
    private BufferedImage chartImage;
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Creamos los elementos necesarios.
     */
    public PanelGenerarGraficosRedondos() throws SQLException {
        crearElementos();
        cambiarColor();
        cargarBbdd();
        idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
        listaBbdd.addActionListener(this);
        comboTablas.addActionListener(this);
        btnGenerarGraficos.addActionListener(this);
        btnGenerarPDF.addActionListener(this);
    }

    /**
     * Obtiene las bases de datos que constan en el sistema pero solo muestra
     * aquellos que empiezan por bbdd_ Al usuario los muestra sin bbdd_ y sin
     * _sofgenius. En caso de los niveles =>2 no muestra las tablas config y
     * datos.
     */
    private void cargarBbdd() throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(" ");

        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();

        // Obteniendo el nivel de usuario
        int nivelUsuario = NivelUsuario.obtenerNivelUsuario(NivelUsuario.nombreUsuario);

        while (resultSet.next()) {
            String databaseName = resultSet.getString("TABLE_CAT");
            if (databaseName.startsWith("bbdd_")) {
                // Verificando si el nivel de usuario es mayor o igual a 2 y no está en la lista de exclusión
                if (nivelUsuario >= 2 && !excludedDatabases.contains(databaseName)) {
                    databaseName = databaseName.replaceFirst("bbdd_", "").replaceFirst("_softgenius", "");
                    listaBbdd.addItem(databaseName);
                } else if (nivelUsuario < 2) { // Si el nivel de usuario es menor que 2, no se aplican filtros
                    databaseName = databaseName.replaceFirst("bbdd_", "").replaceFirst("_softgenius", "");
                    listaBbdd.addItem(databaseName);
                }
            }
        }
        resultSet.close();
        conexionBBDD.close();
    }

    /**
     * Lista de bases de datos a excluir en los niveles que nos interesen.
     */
    private List<String> excludedDatabases = Arrays.asList(
            "bbdd_config_softgenius",
            "bbdd_cuentas_softgenius"
    );

    /**
     * Obtiene las tablas que constan en la base de datos elegida.
     */
    private void cargarTablas(String bbddSeleccionada) throws SQLException {
        comboTablas.removeAllItems();
        String db = bbddSeleccionada;
        String[] nombresTablas = obtenerNombresTablas(db);

        if (nombresTablas != null) {
            for (String nombreTabla : nombresTablas) {
                comboTablas.addItem(nombreTabla);
            }
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(null, "No tables found in the selected database.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron tablas en la base de datos seleccionada.");
            }
        }
    }

    /**
     * Obtiene los nombres de las tablas de la base de datos seleccionada.
     */
    private String[] obtenerNombresTablas(String bbddSeleccionada) throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbddSeleccionada);
        DatabaseMetaData metaData = (DatabaseMetaData) conexionBBDD.getMetaData();
        ResultSet resultSet = metaData.getTables(bbddSeleccionada, null, null, new String[]{"TABLE"});

        List<String> nombresTablas = new ArrayList<>();
        while (resultSet.next()) {
            nombresTablas.add(resultSet.getString("TABLE_NAME"));
        }

        resultSet.close();
        conexionBBDD.close();

        return nombresTablas.toArray(new String[0]);
    }

    /**
     * Obtiene los nombres de las columnas de una tabla seleccionada.
     */
    private String[] obtenerNombresColumnas(String bbddSeleccionada, String tablaSeleccionada) throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbddSeleccionada);
        ResultSet resultSet = conexionBBDD.getMetaData().getColumns(null, null, tablaSeleccionada, null);

        List<String> nombresColumnas = new ArrayList<>();
        while (resultSet.next()) {
            nombresColumnas.add(resultSet.getString("COLUMN_NAME"));
        }

        resultSet.close();
        conexionBBDD.close();

        return nombresColumnas.toArray(new String[0]);
    }

    /**
     * Se encarga de obtener los datos de las columnas seleccionadas.
     *
     * @param bbddSeleccionada
     * @param tablaSeleccionada
     * @param columnasSeleccionadas
     * @return
     * @throws SQLException
     */
    private Map<String, Integer> obtenerDatos(String bbddSeleccionada, String tablaSeleccionada, String[] columnasSeleccionadas) throws SQLException {
        Connection conexionBBDD = ConexionBD.obtenerConexion(bbddSeleccionada);
        StringBuilder queryBuilder = new StringBuilder("SELECT ");

        for (int i = 0; i < columnasSeleccionadas.length; i++) {
            queryBuilder.append(columnasSeleccionadas[i]);
            if (i < columnasSeleccionadas.length - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(" FROM ").append(tablaSeleccionada);

        ResultSet resultSet = conexionBBDD.createStatement().executeQuery(queryBuilder.toString());

        Map<String, Integer> data = new HashMap<>();
        while (resultSet.next()) {
            for (String columna : columnasSeleccionadas) {
                String valor = resultSet.getString(columna);
                data.put(valor, data.getOrDefault(valor, 0) + 1);
            }
        }

        resultSet.close();
        conexionBBDD.close();

        return data;
    }

    private void cargarColumnas(String bbddSeleccionada, String tablaSeleccionada) throws SQLException {
        panel2.removeAll(); // Limpiar panel2 antes de añadir nuevos checkboxes
        columnasCheckBoxes.clear();
        String[] nombresColumnas = obtenerNombresColumnas(bbddSeleccionada, tablaSeleccionada);
        if (nombresColumnas != null) {
            for (String nombreColumna : nombresColumnas) {
                JCheckBox checkBox = new JCheckBox(nombreColumna);
                checkBox.setBackground(colorPanel);
                columnasCheckBoxes.put(nombreColumna, checkBox);
                panel2.add(checkBox);
            }
        }
        panel2.revalidate();
        panel2.repaint();
    }

    private void generarGrafico() throws SQLException {
        String bbddSeleccionada = (String) listaBbdd.getSelectedItem();
        String tablaSeleccionada = (String) comboTablas.getSelectedItem();

        if (bbddSeleccionada != null && tablaSeleccionada != null) {
            List<String> columnasSeleccionadas = new ArrayList<>();
            for (Map.Entry<String, JCheckBox> entry : columnasCheckBoxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    columnasSeleccionadas.add(entry.getKey());
                }
            }

            if (!columnasSeleccionadas.isEmpty()) {
                Map<String, Integer> data = obtenerDatos("bbdd_" + bbddSeleccionada + "_softgenius", tablaSeleccionada, columnasSeleccionadas.toArray(new String[0]));
                DefaultPieDataset dataset = new DefaultPieDataset();

                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    dataset.setValue(entry.getKey(), entry.getValue());
                }

                JFreeChart chart = ChartFactory.createPieChart("Gráfico de " + tablaSeleccionada, dataset, true, true, false);
                chart.setBackgroundPaint(colorPanel);
                applyCustomTheme(chart);

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setBackgroundPaint(colorSelect);
                plot.setLabelGenerator(null);

                TextTitle title = chart.getTitle();
                title.setFont(new Font("Arial", Font.BOLD, 14));
                title.setPaint(Color.BLACK);

                plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setBackground(colorSelect);
                panel4.removeAll();
                panel4.add(chartPanel, BorderLayout.CENTER);
                panel4.revalidate();
                panel4.repaint();

                // Guardar la imagen del gráfico
                chartImage = chart.createBufferedImage(600, 400);
            } else {
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(null, "Select at least one column to generate the chart.");
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione al menos una columna para generar el gráfico.");
                }
            }
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(null, "Select a database and a table.");
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una base de datos y una tabla.");
            }
        }
    }

    private BufferedImage captureChartAsImage(JFreeChart chart) {
        int width = panel4.getWidth();
        int height = panel4.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
        g2.dispose();
        return image;
    }

    private void generarPDF() throws IOException {
        String userProfile = System.getenv("USERPROFILE");

        if (chartImage != null) {
            // Crear una carpeta para los PDF si no existe
            File folder = new File(userProfile + File.separator + "Documents" + File.separator + "Graficos_redondos_pdf");
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Generar un nombre único para el archivo PDF
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String pdfFileName = userProfile + File.separator + "Documents" + File.separator + "Graficos_redondos_pdf" + File.separator + "grafico_" + timestamp + ".pdf";

            // Crear el documento PDF y agregar la imagen
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, chartImage);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 50, 500, pdImage.getWidth() / 2, pdImage.getHeight() / 2); // Ajustar la posición y tamaño según sea necesario
            contentStream.close();

            // Guardar el documento PDF
            document.save(pdfFileName);
            document.close();

            // Mostrar un mensaje de éxito
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(null, "PDF generated successfully: " + pdfFileName);
            } else {
                JOptionPane.showMessageDialog(null, "PDF generado exitosamente: " + pdfFileName);
            }
            try {
                File informeFile = new File(pdfFileName);
                if (informeFile.exists()) {
                    Desktop.getDesktop().open(informeFile);
                } else {
                    System.out.println("El archivo de informe no existe.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(null, "First generate a chart before exporting to PDF.");
            } else {
                JOptionPane.showMessageDialog(null, "Primero genere un gráfico antes de exportar a PDF.");
            }
        }
    }

    private void applyCustomTheme(JFreeChart chart) {
        // Implementar los cambios de tema personalizados aquí, si es necesario
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == listaBbdd) {
            String bbddSeleccionada = (String) listaBbdd.getSelectedItem();
            if (!"- Escoge BBDD -".equals(bbddSeleccionada)) {
                try {
                    cargarTablas("bbdd_" + bbddSeleccionada + "_softgenius");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == comboTablas) {
            String tablaSeleccionada = (String) comboTablas.getSelectedItem();
            String bbddSeleccionada = (String) listaBbdd.getSelectedItem();
            if (tablaSeleccionada != null && bbddSeleccionada != null) {
                try {
                    cargarColumnas("bbdd_" + bbddSeleccionada + "_softgenius", tablaSeleccionada);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == btnGenerarGraficos) {
            try {
                generarGrafico();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnGenerarPDF) {
            try {
                generarPDF();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void crearElementos() {
        setLayout(new BorderLayout());
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();

        panel1.setBackground(colorPanel);
        panel2.setBackground(colorPanel);
        panel3.setBackground(colorPanel);
        panel4.setBackground(colorPanel);

        panel1.setLayout(new GridLayout(3, 2));
        panel2.setLayout(new GridLayout(0, 1));
        panel3.setLayout(new GridLayout(1, 1));
        panel4.setLayout(new BorderLayout());

        lblListaBbdd = new JLabel("- Escoge BBDD - ");
        listaBbdd = new JComboBox<>();
        listaBbdd.setBackground(colorSelect);
        lblListaTablas = new JLabel("- Escoge tabla - ");
        comboTablas = new JComboBox<>();
        comboTablas.setBackground(colorSelect);

        panel1.add(lblListaBbdd);
        panel1.add(listaBbdd);
        panel1.add(lblListaTablas);
        panel1.add(comboTablas);

        panel3.add(btnGenerarGraficos);
        panel3.add(btnGenerarPDF); // Agregar el botón para generar PDF

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        add(panel4, BorderLayout.EAST);

    }

    private void cambiarColor() {
        ColorBotones colorBotones = new ColorBotones();
        Color miColorFondo = Color.white;
        Color colorRojo = new Color(255, 0, 0);
        Color colorAzul = new Color(70, 130, 180);
        JButton[] botonGenerarGrafico = {btnGenerarGraficos};
        JButton[] botonExportarPDF = {btnGenerarPDF};
        colorBotones.aplicarEstilosBotones(botonGenerarGrafico, colorAzul, miColorFondo, colorAzul);
        colorBotones.aplicarEstilosBotones(botonExportarPDF, colorRojo, miColorFondo, colorRojo);
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

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblListaBbdd.setText(resourceBundle.getString("lblSelectDatabase"));
        lblListaTablas.setText(resourceBundle.getString("lblListaTablas"));
        btnGenerarGraficos.setText(resourceBundle.getString("btnGenerarGraficos"));
        btnGenerarPDF.setText(resourceBundle.getString("imprimirPDF"));
        
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
