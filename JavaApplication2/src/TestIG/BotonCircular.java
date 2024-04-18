/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

/**
 *
 * @author Iago
 */
import javax.swing.*;
import java.awt.*;

public class BotonCircular extends JButton {
    private Color colorFondo;
    private Color colorBorde;
    
    public BotonCircular() {
        super();
        setContentAreaFilled(false); // Para que no se dibuje el fondo por defecto
        setFocusPainted(false); // Para que no se dibuje el borde de enfoque
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4)); // Espacio entre el texto y el borde del botón
        setPreferredSize(new Dimension(60, 60)); // Dimensiones del botón (diámetro del círculo)
        //colorFondo = Color.BLUE; // Color de fondo del círculo
        //colorBorde = Color.BLACK; // Color del borde del círculo
    }
    public BotonCircular(Color color) {
        super();
        setContentAreaFilled(false); // Para que no se dibuje el fondo por defecto
        setFocusPainted(false); // Para que no se dibuje el borde de enfoque
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4)); // Espacio entre el texto y el borde del botón
        setPreferredSize(new Dimension(60, 60)); // Dimensiones del botón (diámetro del círculo)
        colorFondo = color; // Color de fondo del círculo
        colorBorde = Color.BLACK; // Color del borde del círculo
    }

    public BotonCircular(String texto) {
        super(texto);
        setContentAreaFilled(false); // Para que no se dibuje el fondo por defecto
        setFocusPainted(false); // Para que no se dibuje el borde de enfoque
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4)); // Espacio entre el texto y el borde del botón
        setPreferredSize(new Dimension(60, 60)); // Dimensiones del botón (diámetro del círculo)
        colorFondo = Color.BLUE; // Color de fondo del círculo
        colorBorde = Color.BLACK; // Color del borde del círculo
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el círculo
        g2.setColor(colorFondo);
        g2.fillOval(0, 0, getWidth(), getHeight());

        // Dibujar el borde
        g2.setColor(colorBorde);
        g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

        g2.dispose();

        // Llamar al método de la superclase para dibujar el texto
        super.paintComponent(g);
    }
}
