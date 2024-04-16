package TestIG;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

//PTE añadir logo minimalista
//Añadir funcionalidad a los botones y CB


public class LeftPanelMP extends JPanel {
    private JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/imagenSoftGenius.png"); // Añadir logo minimalista
    private JComboBox<String> cbFinanzas = new JComboBox<>();
    private JComboBox<String> cbRecursosHumanos = new JComboBox<>();
    private JComboBox<String> cbOtros = new JComboBox<>();
    private JButton btnIr = new JButton();
    private JButton btnSalir = new JButton();

    public LeftPanelMP() {
        style();
        initComp();
    }

    private void style() {
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridBagLayout());
    }

    // Iniciamos componentes
    private void initComp() {
        // Reescalar la imagen al tamaño deseado
        Image image = iconLogo.getImage();
        Image scaledImage = image.getScaledInstance(100, 120, Image.SCALE_SMOOTH);
        iconLogo = new ImageIcon(scaledImage);
        lblLogo.setIcon(iconLogo);
        opcionesComboBox();
        styleComponents();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets.bottom = 10;
        
        this.add(lblLogo, gbc);
        
        gbc.gridy++;
        this.add(cbFinanzas, gbc);
        
        gbc.gridy++;
        this.add(cbRecursosHumanos, gbc);
        
        gbc.gridy++;
        this.add(cbOtros, gbc);
        
        gbc.gridy++;
        this.add(btnIr, gbc);
        
        gbc.gridy++;
        gbc.insets.bottom = 0;
        this.add(btnSalir, gbc);
    }
    
    // Estilizamos los componentes
    private void styleComponents() {
        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(100, 100));
        
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnIr.setFont(buttonFont);
        btnSalir.setFont(buttonFont);
        
        btnIr.setBackground(new Color(101, 153, 200));
        btnIr.setForeground(Color.WHITE);
        btnIr.setFocusPainted(false);
        btnSalir.setBackground(new Color(101, 153, 200));
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
}
