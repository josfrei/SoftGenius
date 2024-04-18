package TestIG;

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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/* Esta clase representa el panel izquierdo del menú principal, que contiene una imagen con el logo
    Botones con iconos personalizados y 2 botones de acción. */

public class LeftPanelMPv2 extends JPanel {
    private final JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/Pruebalogo.png"); // Añadir logo minimalista
    private final JButton btnVentas = new JButtonHover("recursos/venta.png","recursos/venta-hover.png");
    private final JButton btnPersonal = new JButton();
    private final JButton btnStock = new JButton();
    private final JButton btnOtroBoton = new JButton();
    private final JButton btnIniciar = new JButton("Iniciar");
    private final JButton btnSalir = new JButton("Salir");
    private final Color colorFondo = new Color(255, 240, 250);
    private final Color colorBotones = new Color(237, 204, 223); //Color original de la gama de colores
    private final ImageIcon imgBtnVentas = new ImageIcon("recursos/venta.png");
    private final ImageIcon imgBtnPersonal = new ImageIcon("recursos/personal.png");
    private final ImageIcon imgBtnStock = new ImageIcon("recursos/Stock.png");
    private int ancho = 0;
    
    
    //Constructor
    public LeftPanelMPv2() throws IOException{
        style();
        initComp();
        imagenesBotones();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
    }
    //Estilos que aplican al panel
    private void style() {
        this.setBackground(Color.WHITE);
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
        
        GridBagConstraints gbcSalir = new GridBagConstraints();
        gbcSalir.gridx = 0;
        gbcSalir.gridy = 2;
        gbcSalir.weightx = 1;
        gbcSalir.anchor = GridBagConstraints.PAGE_END;
        gbcSalir.fill = GridBagConstraints.HORIZONTAL;
        gbcSalir.insets = new Insets(10, 15, 20, 15);
        this.add(btnSalir, gbcSalir);
        
    }
    
    // Estilizamos los componentes
    private void styleComponents() {
        
        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(100, 100));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);

        btnVentas.setBackground(colorFondo);    
        btnPersonal.setBackground(colorFondo); 
        btnStock.setBackground(colorFondo);
        btnOtroBoton.setBackground(colorFondo);
        btnIniciar.setBackground(colorFondo);
        btnSalir.setBackground(colorFondo);

    }

    // Método para crear el panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0,15)); // 5 filas, 1 columna, espacio vertical de 10 pixels entre componentes
        panel.setBackground(Color.WHITE); // Ajustar a color de fondo
        

        
        // Agregar botones al panel
        panel.add(btnVentas);
        panel.add(btnPersonal);
        panel.add(btnStock);
        panel.add(btnOtroBoton);
        panel.add(btnIniciar);
        
        
        return panel;
    }
    
    //Método para las imágenes de los botones
    private void setImagenesBotones(){
        btnVentas.setIcon(imgBtnVentas);
        btnPersonal.setIcon(imgBtnPersonal);
        btnStock.setIcon(imgBtnStock);

}
    // Override el método getPreferredSize para definir el tamaño preferido del panel
    @Override
    public Dimension getPreferredSize() {
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width * 0.06); // El 15% del ancho del frame
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
    
    //Métodos para asignar las imágenes a los botones
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
}

