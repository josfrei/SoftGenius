package config;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
  * Esta clase proporciona una funcionalidad para aplicar estilos a un conjunto de botones 
 * de tipo JButton en una interfaz gráfica de usuario en Java utilizando Swing.
 * @author josef
 */
public class ColorBotones {

        public void aplicarEstilosBotones(JButton[] botones, Color colorElegido, Color colorFondo, Color colorBorde) {
        try {
            // Cargar la fuente Montserrat-Regular desde un archivo
            Font fuenteMontserrat = Font.createFont(Font.TRUETYPE_FONT, new File("./recursos/Montserrat-Regular.ttf")).deriveFont(Font.PLAIN, 14);

            // Asignar los estilos a cada botón del array
            for (JButton boton : botones) {
                boton.setFont(fuenteMontserrat);
                boton.setBackground(colorFondo); 
                boton.setForeground(colorElegido);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(colorBorde), // Color del borde
                        BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Padding interior

                // Agregar el evento de hover al botón
                boton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // No necesitamos implementar esto para hover
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // No necesitamos implementar esto para hover
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // No necesitamos implementar esto para hover
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Cambiar el color de fondo cuando el ratón entra
                        boton.setBackground(colorElegido);
                        boton.setForeground(colorFondo);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Restaurar el color de fondo cuando el ratón sale
                        boton.setBackground(colorFondo);
                        boton.setForeground(colorElegido);
                    }
                });
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Si hay algún error al cargar la fuente, se utilizará la fuente predeterminada
            Font fuentePredeterminada = new Font("Arial", Font.PLAIN, 14);
            for (JButton boton : botones) {
                boton.setFont(fuentePredeterminada);
                boton.setBackground(colorElegido); // Windows blue color
                boton.setForeground(Color.WHITE);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            }
        }
    }

}
