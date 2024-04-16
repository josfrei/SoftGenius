package TestIG;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
//Falta implementar redimensión y añadir detalles / Modificar colores

public class Login extends JFrame {
    private final JPanel panelLogin = new PanelLogin();
    private int anchoFrame;
    private int altoFrame;

    public Login() {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setSize(800, 800);
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

    private void obtenerDimensionesPanel() {
        anchoFrame = this.getWidth();
        altoFrame = this.getHeight();
    }
    
    private void recolocarPanel() {
        int posX = (int) (anchoFrame * 0.23);
        int posY = (int) (altoFrame * 0.28);
        int anchoPanel = (int) (anchoFrame * 0.52);
        int altoPanel = (int) (altoFrame * 0.42);
        panelLogin.setBounds(posX, posY, anchoPanel, altoPanel);
    }

}


