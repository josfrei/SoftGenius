package softgenius;

import calendario.Calendario;
import config.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.plaf.ColorUIResource;
import config.NivelUsuario;
import fichado.SistemaFichado;
import idioma.Idiomas;
import javax.swing.*;

/**
 * Esta clase representa el panel izquierdo del menú principal, que contiene una
 * imagen con el logo, botones con iconos personalizados y los botones de
 * acción. Gestiona la apariencia y funcionalidad del panel izquierdo del menú
 * principal. Implementa ActionListener para manejar eventos de acción de los
 * botones.
 *
 * @autor Grupo 1 - 1º DAM Colexio Karbo
 */
public class LeftPanelMPv2 extends JPanel implements ActionListener {

    // Componentes de la interfaz de usuario
    public final JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/Pruebalogo.png"); // Añadir logo minimalista
//*************Esta variable es la que obtiene el nombre de la carpegta del metodo cambiarRecursosPorNombreDeCarpeta() de la clase FondoBotonesRutaBBDD*************//
    private String carpetaImagenes = new FondoBotonesRutaBaseDeDatos().cambiarRecursosPorNombreDeCarpeta();
    //*************Demás Variables*************//
    private final JButton btnConfiguracion = new JButtonHover("recursos/herramientas-hover.png","recursos/"+carpetaImagenes+"/herramientas.png");
    private final JButton btnDatabase = new JButtonHover( "recursos/database-hover.png","recursos/"+carpetaImagenes+"/database.png");
    private final JButton btnInformes = new JButtonHover("recursos/informes-hover.png","recursos/"+carpetaImagenes+"/informes.png" );
    private final JButton btnCalculadora = new JButtonHover( "recursos/calculadora-hover.png","recursos/"+carpetaImagenes+"/calculadora.png");
    
    //********ColorFondo***********//
//    private final Color colorFondo = new Color(240, 240, 240);
    private Color colorFondo  = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();
    
    private final Color colorBotones = new Color(237, 204, 223); //Color original de la gama de colores, usado en LeftPanel(v1)
    private int ancho = 0;
    private PanelCentral panelCentral;

    String usuarioActivo = NivelUsuario.nombreUsuario;
    // Llamo al método de obtención de nivel de privilegios pasándole como parámetro
    //el usuario activo
    int nivelUsuario = NivelUsuario.obtenerNivelUsuario(NivelUsuario.nombreUsuario);

    private final JLabel lblSesionActiva = new JLabel(usuarioActivo);
    private final JLabel lblCalendario = new JLabel();

    /**
     * Constructor de la clase LeftPanelMPv2.
     *
     * @param panelCentral Referencia al PanelCentral que se actualizará con las
     * acciones de los botones.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public LeftPanelMPv2(PanelCentral panelCentral) throws IOException, SQLException {
        this.panelCentral = panelCentral;
        style();
        initComp();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        Timer timer = new Timer(1000, e -> actualizarHoraFecha());
        timer.start();
        CambioImagenCorporativa cambioImagen = new CambioImagenCorporativa();
        cambioImagen.initializeLogoClickListener(this);
        // Set option to 0 and repaint panelCentral
        panelCentral.setOpcion(0);
        panelCentral.repaint();
    }

    /**
     * Aplica estilos al panel principal.
     */
    private void style() {
        this.setBackground(colorFondo);
        this.setLayout(new GridBagLayout());
    }

    /**
     * Inicializa los componentes del panel.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    private void initComp() throws SQLException {
        styleComponents();
        iniciarImagen();
        // Crear GridBagConstraints para la etiqueta del logo
        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.weightx = 1.0;
        gbcLogo.weighty = 0.15;
        gbcLogo.anchor = GridBagConstraints.NORTH;
        gbcLogo.insets = new Insets(25, 10, 10, 10); // padding
        gbcLogo.fill = GridBagConstraints.HORIZONTAL; // Hace que el panel ocupe todo el ancho disponible
        this.add(lblLogo, gbcLogo);

        // Crear GridBagConstraints para el panel de botones
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 1;
        gbcButtons.weightx = 0.7;
        gbcButtons.weighty = 0.85;
        gbcButtons.anchor = GridBagConstraints.NORTH;
        gbcButtons.fill = GridBagConstraints.HORIZONTAL;
        gbcButtons.insets = new Insets(20, 20, 20, 20); // Añadir espacio alrededor de los componentes
        JPanel buttonPanel = createButtonPanel();
        this.add(buttonPanel, gbcButtons);

        // Crear GridBagConstraints para el panel de botones
        GridBagConstraints gbcLabels = new GridBagConstraints();
        gbcLabels.gridx = 0;
        gbcLabels.gridy = 2;
        gbcLabels.weightx = 1.0;
        gbcLabels.weighty = 0.15;
        gbcLabels.anchor = GridBagConstraints.PAGE_END;
        gbcLabels.fill = GridBagConstraints.BOTH;
        gbcLabels.insets = new Insets(0, 10, 10, 10);
        JPanel createLabelPanel = createLabelPanel();
        this.add(createLabelPanel, gbcLabels);

        lblCalendario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    mostrarCalendario(); // Call the method to show the calendar dialog
                } catch (SQLException ex) {
                    Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * Aplica estilos a los componentes del panel.
     */
    private void styleComponents() {

        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(100, 100));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);

        setToolTipManager();
        btnInformes.setBackground(colorFondo);
        btnInformes.setBorderPainted(false);
        btnInformes.setToolTipText("Informes");
        btnInformes.addActionListener(this);

        setToolTipManager();
        btnConfiguracion.setBackground(colorFondo);
        btnConfiguracion.setBorderPainted(false);
        btnConfiguracion.setToolTipText("Configuración");
        btnConfiguracion.addActionListener(this);

        setToolTipManager();
        btnDatabase.setBackground(colorFondo);
        btnDatabase.setBorderPainted(false);
        btnDatabase.setToolTipText("Database");
        btnDatabase.addActionListener(this); // Add ActionListener to btnDatabase

        setToolTipManager();
        btnCalculadora.setBackground(colorFondo);
        btnCalculadora.setBorderPainted(false);
        btnCalculadora.setToolTipText("Calculadora");
        btnCalculadora.addActionListener(this);

    }

    /**
     * Configura las propiedades del ToolTip.
     */
    private void setToolTipManager() {
        ToolTipManager.sharedInstance().setInitialDelay(800);
        ToolTipManager.sharedInstance().setDismissDelay(1000);
        UIManager.put("ToolTip.background", new ColorUIResource(colorFondo));
        UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());
    }

    /**
     * Crea y retorna un panel con los botones del menú.
     *
     * @return JPanel con los botones del menú.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 15)); // 5 filas, 1 columna, espacio vertical de 10 pixels entre componentes
        panel.setBackground(colorFondo); // Ajustar a color de fondo

        // Agregar botones al panel
        panel.add(btnDatabase);
        panel.add(btnInformes);
        panel.add(btnCalculadora);
        if (nivelUsuario < 2) {
            panel.add(btnConfiguracion);
        }
        return panel;
    }

    /**
     * Override del método getPreferredSize para definir el tamaño preferido del
     * panel.
     *
     * @return Dimension con el tamaño preferido del panel.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width * 0.07); // El 15% del ancho del frame
        int height = parentSize.height;
        ancho = (int) (parentSize.width * 0.15);
        return new Dimension(width, height);
    }

    /**
     * Método para cargar la imagen del logo.
     */
    private void iniciarImagen() {
        try {
            Image image = iconLogo.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            iconLogo = new ImageIcon(image);
            lblLogo.setIcon(iconLogo);
        } catch (Exception e) {
            e.printStackTrace(); // Falta manejar excepcion
        }
    }

    /**
     * **********************************************
     * /////////////////////////////////////////////
     * ///////////////////////////////////////////// Configuración de las
     * imágenes de los botones /////////////////////////////////////////////
     * /////////////////////////////////////////////
     */
    private void imagenesBotones() {
        ImagenBotonConfiguracion();
        ImagenBotonDatabase();
        ImagenBotonInformes();
        ImagenBotonCalculadora();

    }

    private void ImagenBotonConfiguracion() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/herramientas.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnConfiguracion.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
        }
    }

    private void ImagenBotonDatabase() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/database.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnDatabase.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ImagenBotonInformes() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/informes.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnInformes.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ImagenBotonCalculadora() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/calculadora.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnCalculadora.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * **********************************************
     * /////////////////////////////////////////////
     * ///////////////////////////////////////////// FIN Configuración de las
     * imágenes de los botones /////////////////////////////////////////////
     * /////////////////////////////////////////////
     */
    /**
     * Crea y retorna un panel con etiquetas.
     *
     * @return JPanel con etiquetas de sesión activa y calendario.
     */
    private JPanel createLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 0));
        panel.setBackground(colorFondo);
        panel.add(lblSesionActiva);
        panel.add(lblCalendario);
        return panel;
    }

    /**
     * Actualiza la hora y la fecha en la etiqueta del calendario.
     */
    private void actualizarHoraFecha() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        String hora = formatoHora.format(calendario.getTime());
        String fecha = formatoFecha.format(calendario.getTime());

        lblCalendario.setText("<html>" + fecha + "<br>" + hora + "</html>");
    }

    /**
     * Muestra el calendario en un diálogo.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    private void mostrarCalendario() throws SQLException {
        String idiomaSeleccionado = obtenerIdiomaActual();
        // Find the top-level container (usually a JFrame)
        Container parentContainer = this.getParent();
        while (!(parentContainer instanceof JFrame) && parentContainer != null) {
            parentContainer = parentContainer.getParent();
        }

        if (parentContainer instanceof JFrame) {
            Calendario dialog = new Calendario((JFrame) parentContainer, idiomaSeleccionado);
            dialog.setVisible(true);
        } else {
            System.err.println("Error: Unable to find a JFrame ancestor.");
        }
    }
    private String idiomaActual = "Spanish";

    /**
     * Obtiene el idioma actual configurado en la aplicación.
     *
     * @return El idioma actual como una cadena.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

    /**
     * Maneja los eventos de acción de los botones del menú.
     *
     * @param e El evento de acción generado por los botones.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        SistemaFichado sistemaFichado = new SistemaFichado();
        boolean activacion = false;
        try {
            activacion = sistemaFichado.activacionLeftPanel();
        } catch (SQLException ex) {
            Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (e.getSource() == btnInformes) {
            try {
                if (activacion) {
                    panelCentral.removeAll();
                    panelCentral.setOpcion(1);
                    panelCentral.repaint();
                    requestFocus();
                } else {
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Login not detected", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Fichaje no detectado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (e.getSource() == btnDatabase) {

            try {
                if (activacion) {
                    panelCentral.removeAll();
                    panelCentral.setOpcion(4);
                    panelCentral.repaint();
                    requestFocus();
                } else {
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Login not detected", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Fichaje no detectado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == btnConfiguracion) {
            try {
                if (activacion) {
                    panelCentral.removeAll();
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                } else {
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Login not detected", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Fichaje no detectado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == btnCalculadora) {
            try {
                Runtime.getRuntime().exec("calc");
            } catch (IOException ex) {
                Logger.getLogger(LeftPanelMPv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
