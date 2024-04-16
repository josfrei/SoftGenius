package TestIG;
import java.awt.Color;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

//Clase para el frame principal donde se muestra el Login
public class Login extends JFrame{
    private final JPanel panelLogin = new PanelLogin(); 
    private int anchoFrame;
    private int altoFrame;
    //Constructor del frame
    Login(){
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setSize(500, 500);
        this.add(panelLogin);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        obtenerDimensionesPanel();
        recolocarPanel();
        this.setVisible(true);
    }
    
    private void obtenerDimensionesPanel(){
        anchoFrame = this.getWidth();
        altoFrame = this.getHeight();
    }
    
    private void recolocarPanel(){
        
        int posX = (int) (anchoFrame*0.23);
        int posY = (int) (altoFrame*0.28);
        int anchoPanel = (int) (anchoFrame*0.52);
        int altoPanel = (int) (altoFrame*0.42);
        panelLogin.setBounds(posX, posY, anchoPanel, altoPanel);
    }
}


