package config;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import softgenius.LeftPanelMPv2;

/**
 * Class created for changing the logo.
 * @author Grupo 1 - 1º DAM Colecio Karbo
 */
public class CambioImagenCorporativa {
    
    private List<String> logoPaths;
    private int currentIndex;

    public CambioImagenCorporativa() {
        this.logoPaths = new ArrayList<>();
        this.currentIndex = 0;
        initializeLogoPaths();
    }

    /**
     * Method to change the logo image.
     * @param panel The panel where the logo is displayed.
     * @param imagePath The path to the new image.
     */
    public void cambiarImagenLogo(LeftPanelMPv2 panel, String imagePath) {
        try {
            ImageIcon newIconLogo = new ImageIcon(imagePath);
            Image newImage = newIconLogo.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            newIconLogo = new ImageIcon(newImage);
            panel.lblLogo.setIcon(newIconLogo);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Failed to change logo image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialize the list of logo paths.
     */
    private void initializeLogoPaths() {
        File resourcesFolder = new File("recursos/");
        if (resourcesFolder.exists() && resourcesFolder.isDirectory()) {
            File[] files = resourcesFolder.listFiles((dir, name) -> name.toLowerCase().contains("logo"));
            if (files != null) {
                for (File file : files) {
                    logoPaths.add(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Method to be called by the action that triggers the logo change.
     * @param panel The panel where the logo is displayed.
     */
    public void accionCambiarLogo(LeftPanelMPv2 panel) {
        if (!logoPaths.isEmpty()) {
            currentIndex = (currentIndex + 1) % logoPaths.size();
            cambiarImagenLogo(panel, logoPaths.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(panel, "No logo images found.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Method to initialize the click listener on the logo label.
     * @param panel The panel containing the logo label.
     */
    public void initializeLogoClickListener(LeftPanelMPv2 panel) {
        panel.lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accionCambiarLogo(panel);
            }
        });
    }
}
