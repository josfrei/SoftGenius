package TestIG;

import Calendario.Calendario;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/* Esta clase representa el panel izquierdo del menú principal, que contiene una imagen con el logo
    Botones con iconos personalizados y 2 botones de acción. */

public class LeftPanelMPv2 extends JPanel implements ActionListener{
    
    public final JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/Pruebalogo.png"); // Añadir logo minimalista
    private final JButton btnVentas = new JButtonHover("recursos/venta-hover2.png","recursos/venta.png");
    private final JButton btnPersonal = new JButtonHover("recursos/Personas-hover2.png","recursos/personal.png");
    private final JButton btnStock = new JButtonHover("recursos/Stock-hover2.png","recursos/Stock.png");
    private final JButton  btnOtroBoton = new JButton("Ph");
    private final JButton btnIniciar = new JButton("Ph");
    private final JButton btnSalir = new JButtonHover("recursos/salir-hover.png","recursos/Salir.png");
    private final Color colorFondo = new Color(240, 240, 240);
    private final Color colorBotones = new Color(237, 204, 223); //Color original de la gama de colores, usado en LeftPanel(v1)
    private int ancho = 0;
    private PanelCentral panelCentral;
    private final JLabel lblSesionActiva = new JLabel("Sesión activa");
    private final JLabel lblCalendario= new JLabel();
    
    
    //Constructor
    public LeftPanelMPv2(PanelCentral panelCentral) throws IOException{
        this.panelCentral = panelCentral;
        style();
        initComp();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        Timer timer = new Timer(1000, e -> actualizarHoraFecha());
        timer.start();
    }
    //Estilos que aplican al panel
    private void style() {
        this.setBackground(colorFondo);
        this.setLayout(new GridBagLayout());
    }

    // Iniciamos componentes
    private void initComp() {
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
        
        /*Antiguo boton salir
        GridBagConstraints gbcSalir = new GridBagConstraints();
        gbcSalir.gridx = 0;
        gbcSalir.gridy = 2;
        gbcSalir.weightx = 1;
        gbcSalir.anchor = GridBagConstraints.PAGE_END;
        gbcSalir.fill = GridBagConstraints.HORIZONTAL;
        gbcSalir.insets = new Insets(10, 15, 20, 15);
        this.add(btnSalir, gbcSalir);*/
        
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
            mostrarCalendario(); // Call the method to show the calendar dialog
        }
    });
        
    }
    
    // Estilizamos los componentes
    private void styleComponents() {
        

        
        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(100, 100));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        
        setToolTipManager();
        btnVentas.setBackground(colorFondo);
        btnVentas.setBorderPainted(false);
        btnVentas.setToolTipText("Ventas");
        btnVentas.addActionListener(this);
        
        
        setToolTipManager();
        btnPersonal.setBackground(colorFondo); 
        btnPersonal.setBorderPainted(false);
        btnPersonal.setToolTipText("Personal");
        btnPersonal.addActionListener(this);
        
        setToolTipManager();
        btnStock.setBackground(colorFondo);
        btnStock.setBorderPainted(false);
        btnStock.setToolTipText("Stock");
        btnStock.addActionListener(this);
        
        
        btnOtroBoton.setBackground(colorFondo);
        btnVentas.setToolTipText("Ventas");
        btnIniciar.setBackground(colorFondo);
        btnSalir.setBackground(colorFondo);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(this);
        

    }
    
    private void setToolTipManager(){
        ToolTipManager.sharedInstance().setInitialDelay(800);
        ToolTipManager.sharedInstance().setDismissDelay(1000);
        UIManager.put("ToolTip.background", new ColorUIResource(colorFondo));
        UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());
    }
    // Método para crear el panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0,15)); // 5 filas, 1 columna, espacio vertical de 10 pixels entre componentes
        panel.setBackground(colorFondo); // Ajustar a color de fondo
        

        
        // Agregar botones al panel
        panel.add(btnVentas);
        panel.add(btnPersonal);
        panel.add(btnStock);
        panel.add(btnOtroBoton);
        panel.add(btnIniciar);
        
        
        return panel;
    }
    
    // Override el método getPreferredSize para definir el tamaño preferido del panel
    @Override
    public Dimension getPreferredSize() {
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width * 0.07); // El 15% del ancho del frame
        int height = parentSize.height;
        ancho = (int) (parentSize.width * 0.15);
        return new Dimension(width, height);
    }
    
    // Método para cargar la imagen del logo
    private void iniciarImagen() {
        try {
            Image image = iconLogo.getImage().getScaledInstance(200,150,Image.SCALE_SMOOTH);
            iconLogo = new ImageIcon(image);
            lblLogo.setIcon(iconLogo);
        } catch (Exception e) {
            e.printStackTrace(); // Falta manejar excepcion
        }
    }   
    private void imagenesBotones(){
        ImagenBotonVentas();
        ImagenBotonPersonal();
        ImagenBotonStock();
    }
    private void ImagenBotonVentas() {
    try {
        // Cargar la imagen
        BufferedImage img = ImageIO.read(new File("recursos/venta.png"));

        // Redimensionar la imagen
        Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);

        // Asignar la imagen redimensionada al botón
        btnVentas.setIcon(new ImageIcon(scaledImage));

    } catch (IOException e) {
        e.printStackTrace();
    }}
    private void ImagenBotonPersonal() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/personal.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnPersonal.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void ImagenBotonStock() {
        try {
            BufferedImage img = ImageIO.read(new File("recursos/Stock.png"));
            Image scaledImage = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            btnStock.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
        }
    }
    // Métodos auxiliares para el funcionamiento del calendario
    private JPanel createLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 0));
        panel.setBackground(colorFondo); 
        panel.add(lblSesionActiva);
        panel.add(lblCalendario);
        return panel;
    }
    private void actualizarHoraFecha() {
            Calendar calendario = Calendar.getInstance();
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            String hora = formatoHora.format(calendario.getTime());
            String fecha = formatoFecha.format(calendario.getTime());
            
            lblCalendario.setText("<html>" + fecha + "<br>" + hora + "</html>");
    }
    private void mostrarCalendario() {
    // Find the top-level container (usually a JFrame)
    Container parentContainer = this.getParent();
    while (!(parentContainer instanceof JFrame) && parentContainer != null) {
        parentContainer = parentContainer.getParent();
    }

    if (parentContainer instanceof JFrame) {
        Calendario dialog = new Calendario((JFrame) parentContainer);
        dialog.setVisible(true);
    } else {
        System.err.println("Error: Unable to find a JFrame ancestor.");
    }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSalir){
            //Lógica para salir del programa, cerrar sesión, ¿volver a menu principal?
            System.exit(0);
        }
        if(e.getSource() == btnVentas){
            //Lógica para mostrar ventas en el futuro panel central
            panelCentral.setOpcion(1);
            panelCentral.repaint();
            requestFocus();
        }
        if(e.getSource() == btnPersonal){
            panelCentral.setOpcion(2);
            panelCentral.repaint();
            requestFocus();
        }
        if(e.getSource() == btnStock){
            panelCentral.setOpcion(3);
            panelCentral.repaint();
            requestFocus();
        }
    }

}

