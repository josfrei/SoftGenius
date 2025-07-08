package softgenius;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clase JButtonHover para crear botones con efecto hover. Los botones pueden
 * tener diferentes iconos cuando el mouse está o no sobre ellos. Extiende la
 * funcionalidad de JButton.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class JButtonHover extends JButton {

    private ImageIcon iconDefault;
    private ImageIcon iconHover;

    /**
     * Constructor de JButtonHover.
     *
     * @param defaultIconPath Ruta del archivo de icono predeterminado.
     * @param hoverIconPath Ruta del archivo de icono cuando el mouse está sobre
     * el botón.
     */
    public JButtonHover(String defaultIconPath, String hoverIconPath) {
        // Configura el tamaño preferido del botón
        setPreferredSize(new Dimension(50, 50));

        // Carga los iconos y escala al tamaño deseado
        this.iconDefault = getScaledIcon(defaultIconPath, 50, 50);
        this.iconHover = getScaledIcon(hoverIconPath, 50, 50);

        // Establece el icono predeterminado
        setIcon(iconDefault);

        // Establece el comportamiento de hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setIcon(iconHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(iconDefault);
            }
        });
    }

    /**
     * Método privado para escalar un ImageIcon al tamaño deseado.
     *
     * @param path Ruta del archivo de icono.
     * @param width Ancho deseado del icono escalado.
     * @param height Altura deseada del icono escalado.
     * @return ImageIcon escalado al tamaño especificado.
     */
    private ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
