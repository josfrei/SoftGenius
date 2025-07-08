package informes;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import org.jdatepicker.impl.*;

/**
 * Esta clase sirve para hacer una búsqueda de los informes que se encuentran en
 * una carpeta seleccionada. Se puede buscar por una fecha en concreto como por
 * un rango de fechas.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelBuscadorDeInformes extends JPanel {

    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFin;
    private JButton buttonAceptar;
    private JButton buttonInformeDia;
    private JButton buttonInformeRango;
    private JButton reset;
    private JLabel labelFechaInicio, labelFechaFin, labelFechaInicioR, labelFechaInicioI, labelFechaFinRa;
    private JPanel panelBase, panel, panelBotones, panelResultados;
    private GridBagConstraints constraints;
    private String idiomaActual = "Spanish";
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private String userProfile = System.getenv("USERPROFILE");

    public PanelBuscadorDeInformes() throws SQLException {


        labelFechaInicio = new JLabel("Fecha de inicio:");
        labelFechaInicio.setFont(new Font("Arial", Font.BOLD, 18));
        datePickerInicio = createDatePicker();
        labelFechaInicioI = new JLabel("Fecha del informe:");
        labelFechaInicioR = new JLabel("Fecha de inicio:");
        labelFechaFin = new JLabel("Fecha de fin:");
        labelFechaFin.setFont(new Font("Arial", Font.BOLD, 18));
        labelFechaFinRa = new JLabel("Fecha de fin:");

        datePickerFin = createDatePicker();
        buttonInformeDia = new JButton("Informe de un día");
        buttonInformeRango = new JButton("Informe de un rango de fechas");
        buttonInformeRango.setEnabled(false);
        buttonAceptar = new JButton("Aceptar");
        reset = new JButton("Limpiar Búsqueda");
        reset.setVisible(false); // Initially hide the reset button

        ColorBotones colorBotones = new ColorBotones();
        Color buttonBackgroundColor = new Color(230, 230, 230); // Light gray
        colorBotones.aplicarEstilosBotones(new JButton[]{buttonInformeDia},
                buttonBackgroundColor, new Color(30, 130, 120), new Color(30, 130, 120)); // Teal

        colorBotones.aplicarEstilosBotones(new JButton[]{buttonInformeRango},
                buttonBackgroundColor, new Color(182, 163, 195), new Color(75, 0, 130)); // Indigo

        colorBotones.aplicarEstilosBotones(new JButton[]{buttonAceptar},
                buttonBackgroundColor, new Color(135, 206, 250), new Color(135, 206, 250)); // Sky Blue

        colorBotones.aplicarEstilosBotones(new JButton[]{reset},
                buttonBackgroundColor, new Color(255, 140, 0), new Color(255, 140, 0)); // Orange

        Dimension preferredSize = new Dimension(260, 40);
        buttonInformeDia.setPreferredSize(preferredSize);
        buttonInformeRango.setPreferredSize(preferredSize);
        reset.setPreferredSize(preferredSize);

        panelBase = new JPanel(new GridLayout(0, 1));
        panelResultados = new JPanel(new GridLayout(0, 3));
        panel = new JPanel(new GridBagLayout());
        panelBotones = new JPanel(new FlowLayout());

        panelBotones.setBackground(colorPanel);
        panelBase.setBackground(colorPanel);
        panelResultados.setBackground(colorPanel);
        panel.setBackground(colorPanel);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        panel.add(labelFechaInicio, constraints);
        constraints.gridx = 1;
        panel.add(datePickerInicio, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(labelFechaFin, constraints);
        constraints.gridx = 1;
        panel.add(datePickerFin, constraints);

        panelBotones.add(buttonInformeDia);
        panelBotones.add(buttonInformeRango);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(panelBotones, constraints);

        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(buttonAceptar, constraints);

        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(reset, constraints);

        buttonInformeDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDatePickerDia();
                labelFechaFin.setText("");
                labelFechaInicioI.setText("");
                buttonInformeDia.setEnabled(false);
                buttonInformeRango.setEnabled(true);
            }
        });

        buttonInformeRango.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDatePickerRango();
                labelFechaInicio.setText("");
                labelFechaInicioI.setText("");
                buttonInformeRango.setEnabled(false);
                buttonInformeDia.setEnabled(true);
            }
        });

        buttonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (datePickerInicio.getModel().getValue() != null || datePickerFin.getModel().getValue() != null) {
                    reset.setVisible(true);
                }
                if (datePickerFin.isVisible()) {
                    buscarInformesPorRango();
                } else {
                    buscarInformesPorDia();
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datePickerInicio.getModel().setValue(null);
                datePickerFin.getModel().setValue(null);
                reset.setVisible(false);
                panelResultados.removeAll();
                panelResultados.revalidate();
                panelResultados.repaint();
            }
        });

        this.setBackground(colorPanel);

        panelBase.add(panel);
        panelBase.add(panelResultados);
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
        add(panelBase);
        setVisible(true);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void mostrarDatePickerDia() {
        datePickerInicio.setVisible(true);
        datePickerFin.setVisible(false);
        buttonAceptar.setVisible(true);
        labelFechaInicioI.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel panel = (JPanel) datePickerInicio.getParent();
        GridBagConstraints constraints = ((GridBagLayout) panel.getLayout()).getConstraints(datePickerInicio);
        constraints.gridx = 0;
        constraints.gridy = 0;
        eliminarEtiqueta(panel, "Fecha de inicio:");
        eliminarEtiqueta(panel, "Fecha de fin:");
        eliminarEtiqueta(panel, "End date");
        panel.add(labelFechaInicioI, constraints);
    }

    private void mostrarDatePickerRango() {
        datePickerInicio.setVisible(true);
        datePickerFin.setVisible(true);
        buttonAceptar.setVisible(true);
        labelFechaInicioR.setFont(new Font("Arial", Font.BOLD, 18));
        labelFechaFinRa.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel panel = (JPanel) datePickerInicio.getParent();
        GridBagConstraints constraintsInicio = ((GridBagLayout) panel.getLayout()).getConstraints(datePickerInicio);
        GridBagConstraints constraintsFin = ((GridBagLayout) panel.getLayout()).getConstraints(datePickerFin);
        constraintsInicio.gridx = 0;
        constraintsInicio.gridy = 0;
        constraintsFin.gridx = 0;
        constraintsFin.gridy = 1;
        eliminarEtiqueta(panel, "Fecha del informe:");
        panel.add(labelFechaInicioR, constraintsInicio);
        panel.add(labelFechaFinRa, constraintsFin);
    }

    private void eliminarEtiqueta(JPanel panel, String texto) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && ((JLabel) component).getText().equals(texto)) {
                panel.remove(component);
                break;
            }
        }
    }

    private void buscarInformesPorDia() {
        Date fechaSeleccionada = (Date) datePickerInicio.getModel().getValue();
        if (fechaSeleccionada != null) {
            LocalDate localDate = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String fechaStr = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            List<String> informesEnFecha = obtenerInformesEnFecha(fechaStr);
            mostrarResultados(informesEnFecha);
            buttonInformeRango.setEnabled(true);
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Please select a date", "No date selected", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha", "Sin fecha marcada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarInformesPorRango() {
        Date fechaInicio = (Date) datePickerInicio.getModel().getValue();
        Date fechaFin = (Date) datePickerFin.getModel().getValue();

        if (fechaInicio != null && fechaFin != null && !fechaFin.before(fechaInicio)) {
            List<String> informesEnRango = new ArrayList<>();
            LocalDate fechaActual = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaFinal = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            while (!fechaActual.isAfter(fechaFinal)) {
                String fechaStr = fechaActual.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                List<String> informesDelDia = obtenerInformesEnFecha(fechaStr);
                informesEnRango.addAll(informesDelDia);
                fechaActual = fechaActual.plusDays(1);
            }
            mostrarResultados(informesEnRango);
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "Please select a valid date range", "Invalid Range", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un rango de fechas válido", "Rango erróneo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<String> obtenerInformesEnFecha(String fecha) {

        // Crear directorio si no existe
        File carpetaInformes = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius");
        if (!carpetaInformes.exists()) {
            carpetaInformes.mkdirs(); // Crear directorio si no existe
        }

        List<String> informesEnFecha = new ArrayList<>();

        if (carpetaInformes.exists() && carpetaInformes.isDirectory()) {
            File[] archivos = carpetaInformes.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile() && archivo.getName().startsWith("informe_" + fecha)) {
                        informesEnFecha.add(archivo.getName());
                    }
                }
            }
        }
        return informesEnFecha;
    }

    private void mostrarResultados(List<String> resultados) {
        if (!resultados.isEmpty()) {
            int espacioEntreColumnas = 10;
            int espacioEntreFilas = 10;
            ((GridLayout) panelResultados.getLayout()).setHgap(espacioEntreColumnas);
            ((GridLayout) panelResultados.getLayout()).setVgap(espacioEntreFilas);
            for (String informe : resultados) {
                JButton botonInforme = new JButton(informe);
                botonInforme.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        abrirInforme(informe);
                    }
                });
                panelResultados.add(botonInforme);
            }
            JScrollPane scrollPane = new JScrollPane(panelResultados);
            add(scrollPane);
        } else {
            if (idiomaActual.equals("English")) {
                JOptionPane.showMessageDialog(this, "No reports to display", "No reports", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay informes a mostrar", "No informes", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método para abrir los informes pdf en la ruta donde se encuentra la
     * carpeta y mandándole por parámetro el nombre del informe.
     *
     * @param nombreInforme
     */
    private void abrirInforme(String nombreInforme) {

        // Obtener la ruta completa del archivo
        File archivoInforme = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius" + File.separator + nombreInforme);

        // Verificar si el archivo existe y es un archivo válido
        if (archivoInforme.exists() && archivoInforme.isFile()) {
            try {
                // Abrir el archivo con el visor de PDF predeterminado
                Desktop.getDesktop().open(archivoInforme);
            } catch (IOException ex) {
                // Manejar cualquier error que ocurra al abrir el archivo
                ex.printStackTrace();
            }
        } else {
            // El archivo no existe o no es válido
            System.err.println("No se pudo encontrar el informe o el archivo no es válido: " + nombreInforme);
        }
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
        labelFechaInicio.setText(resourceBundle.getString("labelFechaInicio"));
        labelFechaInicioI.setText(resourceBundle.getString("labelFechaInicioI"));
        labelFechaInicioR.setText(resourceBundle.getString("labelFechaInicio"));
        labelFechaFin.setText(resourceBundle.getString("labelFechaFin"));
        labelFechaFinRa.setText(resourceBundle.getString("labelFechaFin"));
        buttonAceptar.setText(resourceBundle.getString("searchButton"));
        buttonInformeDia.setText(resourceBundle.getString("buttonInformeDia"));
        buttonInformeRango.setText(resourceBundle.getString("buttonInformeRango"));
        reset.setText(resourceBundle.getString("btnLimpiarDatos"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

}
