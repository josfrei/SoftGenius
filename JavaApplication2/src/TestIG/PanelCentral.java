package TestIG;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import javax.swing.*;

public class PanelCentral extends JPanel {
    private final JPanel panelCentralPrincipal;
    private final JPanel esquinaTopLeft = new JPanel();
    private final JPanel esquinaBotLeft = new JPanel();
    private final JPanel panelCentral_Top = new JPanel();
    private final JPanel panelCentral_Bottom = new JPanel();
    private final JPanel panelCentral_Left = new JPanel();
    private final JPanel panelCentral_Right = new JPanel();
    private final JPanel esquinaTopRight = new JPanel();
    private final JPanel esquinaBotRight = new JPanel();
    private final JButton btnMenu= new JButton("Menú");
    private final JButton btnAbrir = new JButton("Abrir");
    private final Color colorFondoP_Central = new Color(250, 250, 250);
    private final Color colorBordeP_Central = Color.white;
    private final ArrayList<JPanel> panelesBorde = new ArrayList<>();
    private int opcion = 0;

    public PanelCentral() {
        setLayout(new GridBagLayout());
        panelCentralPrincipal = new PanelCentralPrincipal(opcion);
        setPanelBottom();
        GBC();
        ajustarColorBordes();
        
        setVisible(true);
    }
    // Este método añade otros paneles si es necesario
    public void panelesAux() {
        panelCentral_Top.setBackground(Color.WHITE);
        panelCentral_Left.setBackground(Color.WHITE);
        panelCentral_Right.setBackground(Color.WHITE);
    }
    
    // Método que se encarga de posicionar los distintos elementos en todo el panel, representa una cuadrícula 3x3
    private void GBC(){
        
        //Parte IZQUIERDA   gbc.gridx=0; gbc.weightx=0.1;
        // Esquina superior izquierda 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=0.05;
        gbc.weighty=0.1;
        this.add(esquinaTopLeft,gbc);
        
        //Añadimos panelIzquierdo
        gbc.gridy=1;
        gbc.weighty=0.8;
        this.add(panelCentral_Left,gbc);
        
        // Esquina inferior izquierda
        gbc.gridy=2;
        gbc.weighty=0.1;
        this.add(esquinaBotLeft, gbc); 
        
        //PARTE CENTRAL
        // Panel superior gridx=1; gbc.weightx=0.8;
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.weightx=0.9;
        gbc.weighty=0.1;
        this.add(panelCentral_Top, gbc);
        
        // Panel Central
        gbc.gridy=1;
        gbc.weighty=0.8;
        this.add(panelCentralPrincipal, gbc); 
        
        // Panel inferior
        gbc.gridy=2;
        gbc.weighty=0.1;
        this.add(panelCentral_Bottom, gbc);
        
        // PARTE Derecha gridx=2 gbc.weightx = 0.1
        // Panel superior
        gbc.gridx=2;
        gbc.gridy=0;
        gbc.weightx=0.05;
        gbc.weighty=0.1;
        this.add(esquinaTopRight, gbc);
        
        // Panel Central
        gbc.gridy=1;
        gbc.weighty=0.8;
        this.add(panelCentral_Right, gbc);
        
        // Panel inferior
        gbc.gridy=2;
        gbc.weighty=0.1;
        this.add(esquinaBotRight, gbc);
        
        
        
    }
    //Estilos botones
    private void styleBotones(){
        //Lógica para modificar los estilos de los botones
    }
    
    //Panel inferior que contiene los botones y un panel entre ambos
    private void setPanelBottom(){
        styleBotones();
        panelCentral_Bottom.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx=0;
        gbc2.weightx = 0.3;
        panelCentral_Bottom.add(btnMenu,gbc2);
        gbc2.gridx=1;
        JPanel panelAux = new JPanel();
        panelAux.setBackground(colorBordeP_Central);
        gbc2.fill=GridBagConstraints.BOTH;
        panelCentral_Bottom.add(panelAux,gbc2);
        gbc2.fill=GridBagConstraints.NONE;
        gbc2.gridx=2;
        gbc2.weightx=0.3;
        panelCentral_Bottom.add(btnAbrir,gbc2);
        styleBotones();
    }
    
    // Métodos para ajustar el color de todos los paneles que forman el borde
    private void setPanelesBorde() {
        panelesBorde.add(esquinaTopLeft);
        panelesBorde.add(panelCentral_Left);
        panelesBorde.add(esquinaBotLeft);
        panelesBorde.add(panelCentral_Top);
        panelesBorde.add(panelCentral_Bottom);
        panelesBorde.add(esquinaTopRight);
        panelesBorde.add(panelCentral_Right);
        panelesBorde.add(esquinaBotRight);
}
    
    private void setColorBorde(Color colorBordeP_Central){
        for (int i = 0; i < panelesBorde.size(); i++) {
            panelesBorde.get(i).setBackground(colorBordeP_Central);
        }
    }
    
    private void ajustarColorBordes(){
        setPanelesBorde();
        setColorBorde(colorBordeP_Central);}
    
    public void setOpcion(int opcionElegida){
        opcion = opcionElegida;
    }
}

