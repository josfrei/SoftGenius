package softgenius;

import idioma.Idiomas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa la ventana de inicio de sesión de la aplicación
 * SoftGenius.
 *
 * Esta clase extiende JFrame y configura la ventana principal de inicio de
 * sesión. Incluye la configuración de tamaño, icono, y comportamiento al cerrar
 * la ventana.
 *
 * @autor Grupo 1 -1º DAM Colecio Karbo
 */
public class Login extends JFrame {

    private final JPanel panelLogin = new PanelLogin();
    private static int anchoFrame = 1600;
    private static int altoFrame = 900;
    private String idiomaActual = "Spanish";

    /**
     * Constructor de la clase Login.
     *
     * Configura la ventana de inicio de sesión con título, tamaño, icono y
     * añade el panel de inicio de sesión. También establece el comportamiento
     * al intentar cerrar la ventana, mostrando una confirmación al usuario.
     */
    public Login() {
        super("SoftGenius Solutions - Gestor administración");
        añadirIcono();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setSize(anchoFrame, altoFrame); //Se modifica la resolución a la estándar de laptops low-cost
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        // Método para redimensionar el panel con la ventana
        // Se añade un WindowListener para manejar el evento de cierre de la ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    String idiomaActual = obtenerIdiomaActual();
                    int option;
                    if (idiomaActual.equals("English")) {
                        option = JOptionPane.showConfirmDialog(null, "Do you want to close the application?", "Confirm Close", JOptionPane.YES_NO_OPTION);
                    } else {
                        option = JOptionPane.showConfirmDialog(null, "¿Desea cerrar la aplicación?", "Confirmar cierre", JOptionPane.YES_NO_OPTION);
                    }
                    if (option == JOptionPane.YES_OPTION) {
                        // Close the application
                        System.exit(0);
                    }else {
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        añadirPanelLogin();

        this.setVisible(
                true);
    }

    /**
     * Añade el panel de inicio de sesión a la ventana.
     *
     * Configura el panel de inicio de sesión para que sea opaco y lo añade al
     * JFrame.
     */
    private void añadirPanelLogin() {
        this.add(panelLogin);
        panelLogin.setOpaque(true);
    }

    /**
     * Añade el icono a la ventana.
     *
     * Carga el icono desde un archivo de imagen y lo establece como el icono
     * del JFrame.
     */
    private void añadirIcono() {
        // Carga el ícono desde un archivo de imagen
        ImageIcon icono = new ImageIcon("recursos/imagenSoftGenius.png");

        // Establece el ícono del JFrame
        setIconImage(icono.getImage());
    }
    
        /**
     * Obtiene el idioma actual del sistema.
     *
     * @return El idioma actual del sistema.
     * @throws SQLException Si ocurre un error al interactuar con la base de
     * datos.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

}
