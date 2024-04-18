/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

/**
 *
 * @author Usuario
 */
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonHover extends JButton {

    private ImageIcon iconDefault;
    private ImageIcon iconHover;

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

    // Método para escalar un ImageIcon al tamaño deseado
    private ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
