package softgenius;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Esta clase gestiona el panel base de las ventanas al pulsar los botones
 * laterales en el que se encuentra el botón menú.
 *
 * @author jGrupo 1 - 1º DAM Colexio Karbo
 */
public class PanelCentral extends JPanel implements ActionListener {

    private JPanel panelCentralPrincipal;
    private final JPanel esquinaTopLeft = new JPanel();
    private final JPanel esquinaBotLeft = new JPanel();
    private final JPanel panelCentral_Top = new JPanel();
    private final JPanel panelCentral_Bottom = new JPanel();
    private final JPanel panelCentral_Left = new JPanel();
    private final JPanel panelCentral_Right = new JPanel();
    private final JPanel esquinaTopRight = new JPanel();
    private final JPanel esquinaBotRight = new JPanel();
    private final JButton btnMenu = new JButton("Menú");
    //private final Color colorFondoP_Central = Color.WHITE;
    private Color colorBordeP_Central = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorPanelesBorde = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();

    //private final Color colorBordeP_Central = Color.white;
    private final ArrayList<JPanel> panelesBorde = new ArrayList<>();
    private int opcion = 0;

    private Connection conexionBBDD;

    /**
     * Constructor de la clase PanelCentral.
     *
     * @throws SQLException si ocurre un error de SQL
     */
    public PanelCentral() throws SQLException {
        setLayout(new GridBagLayout());
        panelCentralPrincipal = new PanelesBotonesPestanas(opcion);
        setPanelBottom();
        GBC();
        ajustarColorBordes();
        setVisible(true);
        try {
            conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");
            idiomaActual = obtenerIdiomaActual();
            actualizarIdioma(idiomaActual);
        } catch (SQLException ex) {
            // Manejar la excepción adecuadamente
            ex.printStackTrace();
        }
    }

    /**
     * Este método añade otros paneles auxiliares.
     */
    public void panelesAux() {
        panelCentral_Top.setBackground(colorPanelesBorde);
        panelCentral_Left.setBackground(colorPanelesBorde);
        panelCentral_Right.setBackground(colorPanelesBorde);
    }

    /**
     * Método que se encarga de posicionar los distintos elementos en todo el
     * panel, representando una cuadrícula 3x3.
     */
    private void GBC() {

        // Esquina superior izquierda 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        gbc.weighty = 0.1;
        this.add(esquinaTopLeft, gbc);

        //Añadimos panelIzquierdo
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(panelCentral_Left, gbc);

        // Esquina inferior izquierda
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        this.add(esquinaBotLeft, gbc);

        // Panel superior gridx=1; gbc.weightx=0.8;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.9;
        gbc.weighty = 0.1;
        this.add(panelCentral_Top, gbc);

        // Panel Central
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(panelCentralPrincipal, gbc);
        panelCentralPrincipal.setBackground(Color.red);

        // Panel inferior
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        this.add(panelCentral_Bottom, gbc);

        // Panel superior
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        gbc.weighty = 0.1;
        this.add(esquinaTopRight, gbc);

        // Panel Central
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(panelCentral_Right, gbc);

        // Panel inferior
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        this.add(esquinaBotRight, gbc);

    }

    /**
     * Establece los estilos de los botones.
     */
    private void styleBotones() {
        //Lógica para modificar los estilos de los botones
    }

    /**
     * Configura el panel inferior que contiene los botones y un panel entre
     * ambos.
     */
    private void setPanelBottom() {
        styleBotones();
        btnMenu.addActionListener(this);
        panelCentral_Bottom.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.weightx = 0.3;

        ColorBotones colorBotones = new ColorBotones();
        Color colorGris = Color.gray;
        Color miColorFondo = Color.white;
        JButton[] botonBuscar = {btnMenu};

        colorBotones.aplicarEstilosBotones(botonBuscar, miColorFondo, colorGris, colorGris);

        panelCentral_Bottom.add(btnMenu, gbc2);
        gbc2.gridx = 1;
        JPanel panelAux = new JPanel();
        panelAux.setBackground(colorBordeP_Central);
        gbc2.fill = GridBagConstraints.BOTH;
        panelCentral_Bottom.add(panelAux, gbc2);
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridx = 2;
        gbc2.weightx = 0.3;
        styleBotones();
    }

    /**
     * Método para ajustar el color de todos los paneles que forman el borde.
     */
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

    /**
     * Establece el color del borde del panel.
     *
     * @param colorBordeP_Central el color del borde
     */
    private void setColorBorde(Color colorBordeP_Central) {
        for (int i = 0; i < panelesBorde.size(); i++) {
            panelesBorde.get(i).setBackground(colorBordeP_Central);
        }
    }

    /**
     * Ajusta el color de los bordes.
     */
    private void ajustarColorBordes() {
        setPanelesBorde();
        setColorBorde(colorBordeP_Central);
    }

    /**
     * Establece la opción seleccionada.
     *
     * @param opcionElegida la opción seleccionada
     * @throws SQLException si ocurre un error de SQL
     */
    public void setOpcion(int opcionElegida) throws SQLException { // PUEDE SER EL FALLO AQUÍ??
        this.remove(panelCentralPrincipal);
        opcion = opcionElegida;
        panelCentralPrincipal = new PanelesBotonesPestanas(opcion);

        // Add the panelCentralPrincipal to the PanelCentral component
        this.add(panelCentralPrincipal, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.8, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // Ajustar la visibilidad de los botones según el valor de opcion
        if (opcion == 0) {
            btnMenu.setVisible(false);
        } else {
            btnMenu.setVisible(true);
        }
        GBC();
        ajustarColorBordes();
        setVisible(true);
        this.revalidate();
        this.repaint();
    }

    /**
     * Obtiene la opción seleccionada.
     *
     * @return la opción seleccionada
     */
    public int getOpcion() {
        return opcion;
    }
//************************************************************************//
    // Cambio de Idioma
    //************************************************************************//
    private String idiomaActual = "Spanish";

    private void actualizarIdioma(String idioma) {
        // Carga el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);
        btnMenu.setText(resourceBundle.getString("btnMenú"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

    /**
     * Maneja las accion del btnMenu.
     *
     * @param e el evento que desencadenó la acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMenu) {
            try {
                setOpcion(0);
            } catch (SQLException ex) {
                Logger.getLogger(PanelCentral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Obtiene el nombre del panel actual.
     *
     * @return el nombre del panel actual
     */
    public String getNombrePanel() {
        switch (getOpcion()) {
            case 1:
                return "RR.HH";
            case 2:
                return "Comercial";
            case 3:
                return "Informes";
            default:
                return "SoftGenius";
        }
    }

}
