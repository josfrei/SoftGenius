package TestIG;
import javax.swing.*;
import java.awt.*;

//Falta implementar redimensión y añadir detalles / Modificar colores
/**
 *
 * @author Iago
 */
public class Login extends JFrame {
    private final JPanel panelLogin = new PanelLogin();
    private static int anchoFrame;
    private static int altoFrame;

    public Login() {
        super("SoftGenius Solutions");
        añadirIcono();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setSize(1366, 768); //Se modifica la resolución a la estándar de laptops low-cost
        this.setLocationRelativeTo(null);
        this.setResizable(true);

       // Método para redimensionar el panel con la ventana

        añadirPanelLogin();
        
        this.setVisible(true);
    }

    private void añadirPanelLogin() {
        this.add(panelLogin);
        panelLogin.setOpaque(true);
    }
    private void añadirIcono(){
    // Carga el ícono desde un archivo de imagen
        ImageIcon icono = new ImageIcon("recursos/imagenSoftGenius.png");
        
        // Establece el ícono del JFrame
        setIconImage(icono.getImage());}
}


