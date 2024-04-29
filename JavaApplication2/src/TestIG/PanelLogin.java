package TestIG;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class PanelLogin extends JPanel {
    private final JPanel panelIniciarSesion = new PanelCuadroLogin();
    private final JPanel panelIzquierda = new JPanel();
    private final JPanel panelDerecha = new JPanel();
    private final JLabel labelImagen = new JLabel();
    private final ImageIcon iconLogo = new ImageIcon("recursos/imagenSoftGenius.png");

    public PanelLogin() {
        setSize(1920, 1080);
        setBackground(Color.WHITE);
        configurarLayout();
    }

    private void configurarLayout() {
        setLayout(new GridLayout(1, 2)); // Divide el panel en 2 columnas
        configurarPanelDerecha();
        add(panelIzquierda);
        add(panelDerecha);
        configurarPanelIzquierda();
    }

    private void configurarPanelIzquierda() {
        panelIzquierda.setBackground(Color.WHITE);
        panelIzquierda.setLayout(new BorderLayout());

        // Temporizador para retrasar la operación de redimensionamiento de la imagen
        Timer timer = new Timer(100, (ActionEvent e) -> {
            // Obtener el tamaño actual del panel izquierdo
            int width1 = panelIzquierda.getWidth();
            int height1 = panelIzquierda.getHeight();
            // Escalar la imagen al nuevo tamaño del panel izquierdo
            Image imagenEscalada = iconLogo.getImage().getScaledInstance(width1, height1, Image.SCALE_SMOOTH);
            // Crear un nuevo ImageIcon con la imagen escalada
            ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
            // Establecer el icono en el JLabel
            labelImagen.setIcon(iconoEscalado);
        });

        // Añadir un listener para escuchar los eventos de redimensionamiento del panel izquierdo
        panelIzquierda.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Reiniciar el temporizador
                timer.restart();
            }
        });

        // Añadir el JLabel al panel izquierdo
        panelIzquierda.add(labelImagen);
    }




    private void configurarPanelDerecha() {
        panelDerecha.setLayout(new BorderLayout());
        panelDerecha.setBackground(Color.WHITE);
        panelDerecha.add(panelIniciarSesion, BorderLayout.CENTER);
    }
}
