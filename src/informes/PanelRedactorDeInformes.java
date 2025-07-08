package informes;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelRedactorDeInformes extends JPanel implements ActionListener {

    private JTextPane textPaneInforme;
    private JButton fontSizeButtonSmall, fontSizeButtonMedium, fontSizeButtonLarge, imprimirPDF;
    private String idiomaActual = "Spanish";
    private int currentFontSize = 12;
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    public PanelRedactorDeInformes() throws SQLException {
        initializeComponents();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Top Area: Text Input
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(colorPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textPaneInforme = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPaneInforme);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Area: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(colorPanel);
        imprimirPDF = new JButton("Imprimir PDF");
        imprimirPDF.addActionListener(this);

        fontSizeButtonSmall = new JButton("Tamaño 10");
        fontSizeButtonSmall.addActionListener(e -> setCurrentFontSize(10));
        fontSizeButtonMedium = new JButton("Tamaño 12");
        fontSizeButtonMedium.addActionListener(e -> setCurrentFontSize(12));
        fontSizeButtonLarge = new JButton("Tamaño 16");
        fontSizeButtonLarge.addActionListener(e -> setCurrentFontSize(16));

        buttonPanel.add(fontSizeButtonSmall);
        buttonPanel.add(fontSizeButtonMedium);
        buttonPanel.add(fontSizeButtonLarge);
        buttonPanel.add(imprimirPDF);

        this.setBackground(colorPanel);
        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        cambiarColor();
    }

    private void setCurrentFontSize(int size) {
        currentFontSize = size;
        textPaneInforme.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, currentFontSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imprimirPDF) {
            String texto = textPaneInforme.getText();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                // Cargar fuente TTF desde un archivo
                PDType0Font font;
                try (InputStream fontStream = new FileInputStream(new File("fuente.ttf"))) {
                    font = PDType0Font.load(document, fontStream);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Error loading the font", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al cargar la fuente", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(font, currentFontSize);
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.showText(texto);
                    contentStream.endText();
                    contentStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Error writing to the PDF", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al escribir en el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return; // Detiene la ejecución si hay un error en la escritura del PDF
                }

                // Crear directorio si no existe
                File directory = new File("informes");
                if (!directory.exists()) {
                    directory.mkdir();
                }

                // Generar nombre de archivo con fecha
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileName = "Informe_manual_" + dateFormat.format(new Date()) + ".pdf";
                String userProfile = System.getenv("USERPROFILE");

                // Guardar el PDF
                document.save(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius" + File.separator + fileName);
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "PDF generated successfully: " + fileName, "PDF Generated", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "PDF generado exitosamente: " + fileName, "PDF Generado", JOptionPane.INFORMATION_MESSAGE);
                }
                //Abrimos el informe
                try {
                    File informeFile = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius" + File.separator + fileName);
                    if (informeFile.exists()) {
                        Desktop.getDesktop().open(informeFile);
                    } else {
                        System.out.println("El archivo de informe no existe.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "Error generating the PDF", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al generar el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
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
        Color colorGris = new Color(140, 140, 140);
        Color colorAzul = new Color(70, 130, 180);
        Color colorRojo = new Color(255, 0, 0);
        Color colorVioleta = new Color(91, 44, 111);

        JButton[] botonFontSizeButtonLarge = {fontSizeButtonLarge};
        JButton[] botonFontSizeButtonMedium = {fontSizeButtonMedium};
        JButton[] botonFontSizeButtonSmall = {fontSizeButtonSmall};
        JButton[] botonImprimirPDF = {imprimirPDF};

        colorBotones.aplicarEstilosBotones(botonFontSizeButtonLarge, colorVioleta, miColorFondo, colorVioleta);
        colorBotones.aplicarEstilosBotones(botonFontSizeButtonMedium, colorVerde, miColorFondo, colorVerde);
        colorBotones.aplicarEstilosBotones(botonFontSizeButtonSmall, colorRojo, miColorFondo, colorRojo);
        colorBotones.aplicarEstilosBotones(botonImprimirPDF, colorAzul, miColorFondo, colorGris);
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
        fontSizeButtonSmall.setText(resourceBundle.getString("fontSizeButtonSmall"));
        fontSizeButtonMedium.setText(resourceBundle.getString("fontSizeButtonMedium"));
        fontSizeButtonLarge.setText(resourceBundle.getString("fontSizeButtonLarge"));
        imprimirPDF.setText(resourceBundle.getString("imprimirPDF"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
