package TestIG;

import Calendario.Calendario;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/*Esta clase representa el panel izquierdo del menú principal, que contiene una imagen con el logo
    Comboboxes desplegables y 2 botones de acción.*/
    
/**
 *
 * @author Iago
 * Falta modificar estilos
 * Solicitar nuevo Logo
 */

public class LeftPanelMPv3 extends JPanel implements ActionListener{
    
    private final JLabel lblLogo = new JLabel();
    private ImageIcon iconLogo = new ImageIcon("recursos/Pruebalogo.png"); // Añadir logo minimalista
    private final JLabel lblRRHH = new JLabel("RR.HH");
    private final JComboBox<String> cbRecursosHumanos = new JComboBox<>();
    private final JLabel lblComercial = new JLabel("Comercial");
    private final JComboBox<String> cbComercial = new JComboBox<>();
    private final JLabel lblInformes = new JLabel("Informes");
    private final JComboBox<String> cbInformes = new JComboBox<>();
    private final JLabel lblSesionActiva = new JLabel("Sesión activa");
    private final JLabel lblCalendario= new JLabel();
    
    private PanelCentral panelCentral;
    private final Color colorFondo = new Color(248, 248, 248 );
    private final Color colorBotones = new Color(237, 204, 223); //Color original de la gama de colores
    private final Color colorCombobox = new Color(248, 248, 248);
    private final Color colorLetraCB = Color.BLACK;
    private int ancho = 0;

    public LeftPanelMPv3(PanelCentral panelCentral) {
        this.panelCentral = panelCentral;
        style();
        initComp();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        // Set option to 0 and repaint panelCentral
        panelCentral.setOpcion(0);
        panelCentral.repaint();
        // Timer para el calendario (actualza fecha y hora
        Timer timer = new Timer(1000, e -> actualizarHoraFecha());
        timer.start();
        
    }
    
    
    private void style() {
        this.setBackground(colorFondo);
        this.setLayout(new GridBagLayout());
    }

    // Iniciamos componentes
    private void initComp() {
        opcionesComboBox();
        styleComponents();
        iniciarImagen();
        
        // Crear GridBagConstraints para la etiqueta del logo
        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.weightx = 1.0;
        gbcLogo.weighty = 0.15;
        gbcLogo.anchor = GridBagConstraints.NORTH;
        gbcLogo.insets = new Insets(25, 10, 10, 10); // padding
        gbcLogo.fill = GridBagConstraints.HORIZONTAL; // Hace que el panel ocupe todo el ancho disponible
        this.add(lblLogo, gbcLogo);
        
        // Crear GridBagConstraints para el panel de JComboBoxes
        GridBagConstraints gbcComboBoxes = new GridBagConstraints();
        gbcComboBoxes.gridx = 0;
        gbcComboBoxes.gridy = 1;
        gbcComboBoxes.weightx = 1.0;
        gbcComboBoxes.weighty = 0.5;
        gbcComboBoxes.anchor = GridBagConstraints.NORTH;
        gbcComboBoxes.fill = GridBagConstraints.HORIZONTAL;
        gbcComboBoxes.insets = new Insets(10, 10, 10, 10); // Añadir espacio alrededor de los componentes
        JPanel comboBoxPanel = createComboBoxPanel();
        this.add(comboBoxPanel, gbcComboBoxes);
        
        // Crear GridBagConstraints para el panel de botones
        GridBagConstraints gbcLabels = new GridBagConstraints();
        gbcLabels.gridx = 0;
        gbcLabels.gridy = 2;
        gbcLabels.weightx = 1.0;
        gbcLabels.weighty = 0.15;
        gbcLabels.anchor = GridBagConstraints.SOUTH;
        gbcLabels.fill = GridBagConstraints.HORIZONTAL;
        gbcLabels.insets = new Insets(20, 20,20, 20);
        JPanel createLabelPanel = createLabelPanel();
        this.add(createLabelPanel, gbcLabels);
        lblCalendario.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            mostrarCalendario(); // Call the method to show the calendar dialog
        }
    });
    }
    
    // Estilizamos los componentes
    private void styleComponents() {
        // Remove the original border of the JComboBox
        cbRecursosHumanos.setBorder(null);
        cbComercial.setBorder(null);
        cbInformes.setBorder(null);


        UIManager.put("ComboBox.padding", new Insets(3, 8, 3, 0));
        lblLogo.setPreferredSize(new Dimension(100, 100));
        lblLogo.setMaximumSize(new Dimension(200, 200));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        Font lblFont = new Font("Montserrat", Font.PLAIN, 14);
        Font cbBoxFont = new Font("Montserrat", Font.PLAIN, 12) ;
        
        
        lblRRHH.setFont(lblFont);
        lblComercial.setFont(lblFont);
        lblInformes.setFont(lblFont);
        //lblRRHH.setHorizontalAlignment(JLabel.CENTER);
        //lblComercial.setHorizontalAlignment(JLabel.CENTER);
        //lblInformes.setHorizontalAlignment(JLabel.CENTER);
        
        //Fuentes combobox
        int fontSize = 14; // Set the desired font size
        cbRecursosHumanos.setFont(cbBoxFont);
        cbComercial.setFont(cbBoxFont);
        cbInformes.setFont(cbBoxFont);
        
        //Colores fondo
        cbRecursosHumanos.setBackground(colorCombobox);
        cbComercial.setBackground(colorCombobox);
        cbInformes.setBackground(colorCombobox);
        
        //Color letra
        cbRecursosHumanos.setForeground(colorLetraCB);
        cbComercial.setForeground(colorLetraCB);
        cbInformes.setForeground(colorLetraCB);
        
        //Padding
        
        
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        lblSesionActiva.setFont(buttonFont);
        lblCalendario.setFont(buttonFont);
        
        
        lblSesionActiva.setForeground(Color.BLACK);
        lblCalendario.setForeground(Color.BLACK);
    }

    // Método para cambiar las distintas opciones de ComboBox
    public void opcionesComboBox() {
        
        // Configura el renderizador de los JComboBox
        Insets margenes = new Insets(2, 4, 2, 0);
        configurarRenderizadorComboBox(cbRecursosHumanos, margenes);
        configurarRenderizadorComboBox(cbComercial, margenes);
        configurarRenderizadorComboBox(cbInformes, margenes);
        cbRecursosHumanos.addItem("Empleados");
        cbRecursosHumanos.addItem("Sucursales");
        cbRecursosHumanos.addItem("Incidencias");
        cbComercial.addItem("Stock");
        cbComercial.addItem("Ventas");
        cbComercial.addItem("Productos");
        cbComercial.addItem("Clientes");
        cbComercial.addItem("Facturas");
        cbInformes.addItem("Informes1");
        cbInformes.addItem("Informes2");
        cbInformes.addItem("Informes3");
        
    }
    
    // Método para crear y configurar el renderizador para el JComboBox
    public static void configurarRenderizadorComboBox(JComboBox<String> comboBox, Insets margenes) {
        comboBox.setRenderer(new ListCellRenderer<String>() {
            private final BasicComboBoxRenderer renderizadorPorDefecto = new BasicComboBoxRenderer();

            @Override
            public Component getListCellRendererComponent(JList<? extends String> lista, String valor, int indice,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel renderizador = (JLabel) renderizadorPorDefecto.getListCellRendererComponent(lista, valor, indice, isSelected, cellHasFocus);
                renderizador.setBorder(BorderFactory.createEmptyBorder(margenes.top, margenes.left, margenes.bottom, margenes.right));
                return renderizador;
            }
        });
    }
    //Método que añade actionListener a los comboBoxes
    public void actionListComboBox(){
    cbRecursosHumanos.addActionListener(this);
    cbComercial.addActionListener(this);
    cbInformes.addActionListener(this);
    }
    
    // Método para crear el panel de JComboBoxes
    private JPanel createComboBoxPanel() {
    actionListComboBox();
    JPanel panel = new JPanel(new GridLayout(6, 1, 0, 10)); // 3 filas, 1 columna, espacio vertical de 10 pixels entre componentes
    panel.setBackground(colorFondo); // Ajustar a color de fondo
    
    // Add empty border with top padding to each label
    lblRRHH.setBorder(BorderFactory.createEmptyBorder(20, 4, 0, 0));
    lblComercial.setBorder(BorderFactory.createEmptyBorder(20, 4, 0, 0));
    lblInformes.setBorder(BorderFactory.createEmptyBorder(20, 4, 0, 0));
    
    panel.add(lblRRHH);
    panel.add(cbRecursosHumanos);
    panel.add(lblComercial);
    panel.add(cbComercial);
    panel.add(lblInformes);
    panel.add(cbInformes);
    return panel;
}

    
    // Método para crear el panel de botones
    private JPanel createLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10)); // 1 columna 2 filas
        panel.setBackground(colorFondo); // Puedes establecer el color de fondo según tus necesidades
        panel.add(lblSesionActiva);
        panel.add(lblCalendario);
        return panel;
    }
    private void actualizarHoraFecha() {
            Calendar calendario = Calendar.getInstance();
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            String hora = formatoHora.format(calendario.getTime());
            String fecha = formatoFecha.format(calendario.getTime());

            lblCalendario.setText("<html>" + hora + "&nbsp;&nbsp;&nbsp;" + fecha + "</html>");
    }
    private void mostrarCalendario() {
    // Find the top-level container (usually a JFrame)
    Container parentContainer = this.getParent();
    while (!(parentContainer instanceof JFrame) && parentContainer != null) {
        parentContainer = parentContainer.getParent();
    }

    if (parentContainer instanceof JFrame) {
        Calendario dialog = new Calendario((JFrame) parentContainer);
        dialog.setVisible(true);
    } else {
        System.err.println("Error: Unable to find a JFrame ancestor.");
    }
}

    
    // Override el método getPreferredSize para definir el tamaño preferido del panel
    @Override
    public Dimension getPreferredSize() {
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width * 0.12); // El 15% del ancho del frame
        int height = parentSize.height;
        ancho = (int) (parentSize.width * 0.12);
        return new Dimension(width, height);
    }
    
    // Método para cargar la imagen del logo
    private void iniciarImagen() {
        try {
            Image image = iconLogo.getImage().getScaledInstance(200,175,Image.SCALE_SMOOTH);
            iconLogo = new ImageIcon(image);
            lblLogo.setIcon(iconLogo);
        } catch (Exception e) {
            e.printStackTrace(); // Falta manejar excepcion
        }
    }
    @Override
  
    // ActionPerformed para los comboBox 
    public void actionPerformed(ActionEvent e) {
        String cbRRHH = (String) cbRecursosHumanos.getSelectedItem();    //get the selected item
        Object source = e.getSource();
         if (source == cbRecursosHumanos) {
            switch (cbRRHH) {
                case "Empleados":
                    panelCentral.setOpcion(2);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado empleados");
                    break;
                case "Sucursales":
                    panelCentral.setOpcion(2);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado sucursales");
                    break;
                case "Incidencias":
                    panelCentral.setOpcion(2);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado incidencias");
                    break;
                default:

                    break;
            }
         }
         if (source == cbComercial) {
            String cbPrd = (String) cbComercial.getSelectedItem();    //get the selected item

            switch (cbPrd) {
                case "Stock":
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                    break;
                case "Ventas":
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                    break;
                case "Productos":
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                    break;
                case "Clientes":
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                    break;
                case "Facturas":
                    panelCentral.setOpcion(3);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado Facturas");
                    break;
                default:

                    break;
            } 
         }
         if (source == cbInformes) {
            String cbInforms = (String) cbInformes.getSelectedItem();    //get the selected item

            switch (cbInforms) {
                case "Informes1":
                    panelCentral.setOpcion(1);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado Informes1");
                    break;
                case "Informes2":
                    panelCentral.setOpcion(1);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado Informes2");
                    break;
                case "Informes3":
                    panelCentral.setOpcion(1);
                    panelCentral.repaint();
                    requestFocus();
                    System.out.println("Seleccionado Informes3");
                    break;
                default:

                    break;
            } 
         }
           
    }
    

}