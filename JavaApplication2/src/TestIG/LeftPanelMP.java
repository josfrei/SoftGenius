package TestIG;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.*;
import java.awt.color.ColorSpace;

/*Esta clase representa el panel izquierdo del menú principal, que contiene una imagen con el logo
    Comboboxes desplegables y 2 botones de acción.*/

public class LeftPanelMP extends JPanel {
    private final JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/imagenSoftGenius.png"); // Añadir logo minimalista
    private final JComboBox<String> cbFinanzas = new JComboBox<>();
    private final JComboBox<String> cbRecursosHumanos = new JComboBox<>();
    private final JComboBox<String> cbOtros = new JComboBox<>();
    private final JButton btnIr = new JButton("Ir");
    private final JButton btnSalir = new JButton("Salir");
    private final Color colorFondo = Color.LIGHT_GRAY; 
    private int ancho = 0;

    public LeftPanelMP() {
        style();
        initComp();
    }
    
    private void style() {
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new GridBagLayout());
    }

    // Iniciamos componentes
    private void initComp() {
        opcionesComboBox();
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
        
        // Crear GridBagConstraints para el panel de JComboBoxes
        GridBagConstraints gbcComboBoxes = new GridBagConstraints();
        gbcComboBoxes.gridx = 0;
        gbcComboBoxes.gridy = 1;
        gbcComboBoxes.weightx = 1.0;
        gbcComboBoxes.weighty = 0.35;
        gbcComboBoxes.anchor = GridBagConstraints.NORTH;
        gbcComboBoxes.fill = GridBagConstraints.HORIZONTAL;
        gbcComboBoxes.insets = new Insets(10, 10, 10, 10); // Añadir espacio alrededor de los componentes
        JPanel comboBoxPanel = createComboBoxPanel();
        this.add(comboBoxPanel, gbcComboBoxes);
        
        // Crear GridBagConstraints para el panel de botones
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 2;
        gbcButtons.weightx = 1.0;
        gbcButtons.weighty = 0.1;
        gbcButtons.anchor = GridBagConstraints.NORTH;
        gbcButtons.fill = GridBagConstraints.HORIZONTAL;
        gbcButtons.insets = new Insets(10, 10, 10, 10); // Añadir espacio alrededor de los componentes
        JPanel buttonPanel = createButtonPanel();
        this.add(buttonPanel, gbcButtons);
    }
    
    // Estilizamos los componentes
    private void styleComponents() {
        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(100, 100));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnIr.setFont(buttonFont);
        btnSalir.setFont(buttonFont);
        
        btnIr.setBackground(new Color(204, 102, 255));
        btnIr.setForeground(Color.WHITE);
        btnIr.setFocusPainted(false);
        btnSalir.setBackground(new Color(204, 102, 255));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
    }

    // Método para cambiar las distintas opciones de ComboBox
    public void opcionesComboBox() {
        cbFinanzas.addItem("Finanzas1");
        cbFinanzas.addItem("Finanzas2");
        cbFinanzas.addItem("Finanzas3");
        cbRecursosHumanos.addItem("RH1");
        cbRecursosHumanos.addItem("RH2");
        cbRecursosHumanos.addItem("RH3");
        cbOtros.addItem("Otros1");
        cbOtros.addItem("Otros2");
    }
    
    // Método para crear el panel de JComboBoxes
    private JPanel createComboBoxPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10)); // 3 filas, 1 columna, espacio vertical de 10 pixels entre componentes
        panel.setBackground(colorFondo); // Ajustar a color de fondo
        panel.add(cbFinanzas);
        panel.add(cbRecursosHumanos);
        panel.add(cbOtros);
        return panel;
    }
    
    // Método para crear el panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 fila, 2 columnas, espacio horizontal de 10 pixels entre componentes
        panel.setBackground(colorFondo); // Puedes establecer el color de fondo según tus necesidades
        panel.add(btnIr);
        panel.add(btnSalir);
        return panel;
    }
    
    // Override el método getPreferredSize para definir el tamaño preferido del panel
    @Override
    public Dimension getPreferredSize() {
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width * 0.15); // El 15% del ancho del frame
        int height = parentSize.height;
        ancho = (int) (parentSize.width * 0.15);
        return new Dimension(width, height);
    }
    
    // Método para cargar la imagen del logo
    private void iniciarImagen() {
        try {
            iconLogo = new ImageIcon("recursos/imagenSoftGenius.png");
            Image image = iconLogo.getImage().getScaledInstance(200,150,Image.SCALE_SMOOTH);
            iconLogo = new ImageIcon(image);
            lblLogo.setIcon(iconLogo);
        } catch (Exception e) {
            e.printStackTrace(); // Falta manejar excepcion
        }
    }
}