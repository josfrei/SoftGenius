package softgenius;

import config.ClaseCambiarColorDeFondoInterfaz;
import org.jfree.chart.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.*;
import fichado.SistemaFichado;

/**
 * Esta clase está hecha para generar los gráficos que se presentan en el menú
 * principal. Dichos gráficos devuelve la siguiente información sacada de una
 * base de datos: -Horas trabajadas. -Días de vacaciones totales. -Días de
 * vacaciones pendientes.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class GraficaDeDatos extends JPanel {

    private Connection conexionBBDD;

    private Color colorGrafico = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();

    /**
     * Constructor de la clase. Crea los gráficos y los añade al panel.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public GraficaDeDatos() throws SQLException {
        setLayout(new BorderLayout());

        JPanel graficosPanel = new JPanel(new GridLayout(1, 3));

        ChartPanel chartPanel2 = createChartPanelHorasDiarias();
        ChartPanel chartPanel3 = createChartPanelHorasTotales();
        ChartPanel chartPanel4 = createChartPanelDiasVacaciones();

        chartPanel2.setPreferredSize(new Dimension(400, 300));
        chartPanel3.setPreferredSize(new Dimension(200, 150));
        chartPanel4.setPreferredSize(new Dimension(200, 150));

        graficosPanel.add(chartPanel2, BorderLayout.WEST);
        JPanel subPanel = new JPanel(new GridLayout(2, 1));
        subPanel.add(chartPanel3);
        subPanel.add(chartPanel4);
        graficosPanel.add(subPanel, BorderLayout.EAST);

        add(graficosPanel, BorderLayout.CENTER);

        setBackground(colorGrafico);
        graficosPanel.setBackground(colorGrafico);
    }

    /**
     * Método privado para crear el panel de gráfico de horas diarias.
     *
     * @return El panel de gráfico de horas diarias.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    private ChartPanel createChartPanelHorasDiarias() throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();

            SistemaFichado sistemaFichado = new SistemaFichado();
            int fichajeID = sistemaFichado.obtenerEmpleadoId();

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
                String queryHoras = "SELECT Horas_trabajadas FROM fichar WHERE FicharID = ?";
                try (PreparedStatement stmtHoras = conexionBBDD.prepareStatement(queryHoras)) {
                    stmtHoras.setInt(1, fichajeID);
                    try (ResultSet rsHoras = stmtHoras.executeQuery()) {
                        if (rsHoras.next()) {
                            double horasTrabajadas = rsHoras.getDouble("Horas_trabajadas");
                            dataset.setValue("Horas Trabajadas", horasTrabajadas);
                        }
                    }
                }

                String queryVacaciones = "SELECT Vacaciones_totales, Vacaciones_disfrutadas FROM empleado WHERE EmpleadoID = ?";
                try (PreparedStatement stmtVacaciones = conexionBBDD.prepareStatement(queryVacaciones)) {
                    stmtVacaciones.setInt(1, fichajeID);
                    try (ResultSet rsVacaciones = stmtVacaciones.executeQuery()) {
                        if (rsVacaciones.next()) {
                            int diasVacacionesTotales = rsVacaciones.getInt("Vacaciones_totales");
                            int diasVacacionesDisfrutados = rsVacaciones.getInt("Vacaciones_disfrutadas");
                            dataset.setValue("Días de Vacaciones Totales", diasVacacionesTotales);
                            dataset.setValue("Días de Vacaciones Disfrutados", diasVacacionesDisfrutados);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
            applyCustomTheme(chart);

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelGenerator(null);
            plot.setBackgroundPaint(colorGrafico); // Fondo del área del gráfico
            plot.setOutlineVisible(false);
            plot.setShadowPaint(null);

            TextTitle title = chart.getTitle();
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setPaint(Color.BLACK);

            chart.setBackgroundPaint(colorGrafico); // Fondo del gráfico completo

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBackground(colorGrafico); // Fondo del panel del gráfico
            return chartPanel;
        }
        /**
         * Método privado para crear el panel de gráfico de horas totales.
         *
         * @return El panel de gráfico de horas totales.
         */

    private ChartPanel createChartPanelHorasTotales() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int fichajeID = 5;

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
            String query = "SELECT SUM(Horas_trabajadas) AS total_horas_trabajadas FROM fichar WHERE FicharID = ?";
            try (PreparedStatement stmt = conexionBBDD.prepareStatement(query)) {
                stmt.setInt(1, fichajeID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        double totalHorasTrabajadas = rs.getDouble("total_horas_trabajadas");
                        dataset.setValue("Horas Totales Trabajadas", totalHorasTrabajadas);
                        dataset.setValue("Horas Restantes", 200 - totalHorasTrabajadas);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        applyCustomTheme(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setBackgroundPaint(colorGrafico); // Fondo del área del gráfico
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);

        TextTitle title = chart.getTitle();
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setPaint(Color.BLACK);

        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));

        chart.setBackgroundPaint(colorGrafico); // Fondo del gráfico completo

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(colorGrafico); // Fondo del panel del gráfico
        return chartPanel;
    }

    /**
     * Método privado para crear el panel de gráfico de días de vacaciones.
     *
     * @return El panel de gráfico de días de vacaciones.
     */
    private ChartPanel createChartPanelDiasVacaciones() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int fichajeID = 5;

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
            String query = "SELECT Vacaciones_totales, Vacaciones_disfrutadas FROM empleado WHERE EmpleadoID = ?";
            try (PreparedStatement stmt = conexionBBDD.prepareStatement(query)) {
                stmt.setInt(1, fichajeID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int diasVacacionesTotales = rs.getInt("Vacaciones_totales");
                        int diasVacacionesDisfrutados = rs.getInt("Vacaciones_disfrutadas");
                        dataset.setValue("Días Disfrutados", diasVacacionesDisfrutados);
                        dataset.setValue("Días Restantes", diasVacacionesTotales - diasVacacionesDisfrutados);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        applyCustomTheme(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setBackgroundPaint(colorGrafico); // Fondo del área del gráfico
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);

        TextTitle title = chart.getTitle();
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setPaint(Color.BLACK);

        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));

        chart.setBackgroundPaint(colorGrafico); // Fondo del gráfico completo

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(colorGrafico); // Fondo del panel del gráfico
        return chartPanel;
    }

    /**
     * Método privado para aplicar un tema personalizado al gráfico.
     *
     * @param chart El gráfico al que se aplicará el tema.
     */
    private void applyCustomTheme(JFreeChart chart) {
        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setTitlePaint(Color.decode("#4572a7"));
        theme.setExtraLargeFont(new Font("Lucida Sans", Font.PLAIN, 16));
        theme.setLargeFont(new Font("Lucida Sans", Font.BOLD, 15));
        theme.setRegularFont(new Font("Lucida Sans", Font.PLAIN, 11));
        theme.setPlotBackgroundPaint(colorGrafico);

        theme.setPlotBackgroundPaint(Color.white);
        theme.setChartBackgroundPaint(Color.white);
        theme.apply(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelGenerator(new CustomPieSectionLabelGenerator());
    }

    class CustomPieSectionLabelGenerator extends StandardPieSectionLabelGenerator {

        @Override
        public String generateSectionLabel(PieDataset dataset, Comparable key) {
            double value = dataset.getValue(key).doubleValue();
            return super.generateSectionLabel(dataset, key) + " (" + value + ")";
        }
    }
}
