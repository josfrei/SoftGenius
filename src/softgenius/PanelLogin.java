package softgenius;

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

/**
 * Clase que gestiona la apariencia de los elementos de la ventana del login.
 *
 * Esta clase extiende JPanel y configura los componentes de la ventana de
 * inicio de sesión, incluyendo paneles y un logo que se redimensiona
 * automáticamente.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelLogin extends JPanel {

    private final JPanel panelIniciarSesion = new PanelCuadroLogin();
    private final JPanel panelIzquierda = new JPanel();
    private final JPanel panelDerecha = new JPanel();
    private final JLabel labelImagen = new JLabel();
    private final ImageIcon iconLogo = new ImageIcon("recursos/imagenSoftGenius.png");

    /**
     * Constructor de la clase PanelLogin.
     *
     * Configura el tamaño, el fondo y el layout del panel de login.
     */
    public PanelLogin() {
        setSize(1920, 1080);
        setBackground(Color.WHITE);
        configurarLayout();
    }

    /**
     * Configura el layout del PanelLogin.
     *
     * Divide el panel en dos columnas y configura los paneles izquierdo y
     * derecho.
     */
    private void configurarLayout() {
        setLayout(new GridLayout(1, 2)); // Divide el panel en 2 columnas
        configurarPanelDerecha();
        add(panelIzquierda);
        add(panelDerecha);
        configurarPanelIzquierda();
    }

    /**
     * Configura el panel izquierdo del login.
     *
     * Establece el fondo blanco, configura el layout y añade un temporizador
     * para redimensionar la imagen del logo cuando el panel es redimensionado.
     */

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

        panelIzquierda.add(labelImagen);
    }

    /**
     * Configura el panel derecho del login.
     *
     * Establece el fondo blanco, configura el layout y añade el panel de inicio
     * de sesión en el centro.
     */
    private void configurarPanelDerecha() {
        panelDerecha.setLayout(new BorderLayout());
        panelDerecha.setBackground(Color.WHITE);
        panelDerecha.add(panelIniciarSesion, BorderLayout.CENTER);
    }
}
