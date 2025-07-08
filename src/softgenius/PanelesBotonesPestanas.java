package softgenius;

import config.ClaseCambiarColorDeFondoInterfaz;
import idioma.*;
import config.ColorBotones;
import informes.*;
import copiasSeguridad.CopiaSeguridad;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.*;
import tablas.*;
import fichado.SistemaFichado;
import static libsql3.softgenius.GestorTablas.actualizarCampoPorID;
import tablas.PanelCrearClaveForanea;

/**
 * Esta clase gestiona las ventanas del centro, con las pestañas, configuración
 * y demás. También el botón de fichar.
 *
 * Proporciona una interfaz para mostrar diferentes paneles según la opción
 * seleccionada. Además, gestiona eventos de clic para los botones y proporciona
 * métodos para actualizar el idioma y comprobar el estado de fichaje.
 *
 * Los paneles disponibles son: - Bienvenida - Informes - Configuración - Base
 * de Datos
 *
 * Los botones de acción incluyen: - Fichar - Cerrar sesión - Cambiar idioma -
 * Cambiar logo
 *
 * Esta clase implementa la interfaz ActionListener para manejar los eventos de
 * clic de los botones.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelesBotonesPestanas extends JPanel implements ActionListener {

    private JPanel PC_Informes = new JPanel();
    private JPanel PC_Config = new JPanel();
    private JPanel PC_BBDD = new JPanel();
    private JPanel panelBienvenida = new JPanel();
    private JLabel lblCambiarIdioma = new JLabel("Cambiar idioma a:");
    private JLabel lblCambiarColor = new JLabel("Cambiar color");
    private JButton btnCambioIdioma = new JButton();
    private JButton btnColorAmarillo, btnColorRosa, btnColorVioleta, btnColorPredeterminado;
    private JButton btnFichar = new JButton();
    private JButton btnCerrarSesion = new JButton("Cerrar sesion");
    private Color colorFondoBotones = Color.WHITE;
    private Color colorBoton = new Color(147, 81, 22);
    private Connection conexionBBDD;
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private String idiomaActual = "Spanish";

    /**
     * Constructor de la clase PanelesBotonesPestanas.
     *
     * Crea una instancia de la clase con la opción seleccionada y realiza las
     * configuraciones iniciales.
     *
     * @param opcion La opción seleccionada para mostrar el panel
     * correspondiente.
     * @throws SQLException Si ocurre un error al interactuar con la base de
     * datos.
     */
    public PanelesBotonesPestanas(int opcion) throws SQLException {

        comprobarEstadoFichaje();
        Style();
        addSubPanels(opcion);
        gestionBanderaBotonIdioma();
    }

    /**
     * Establece el estilo para el panel central principal.
     */
    private void Style() {
        setLayout(new GridBagLayout());
        setVisible(true);
    }

    /**
     * Agrega los sub-paneles según la opción proporcionada.
     *
     * @param opcion Opción que indica qué paneles mostrar.
     * @throws SQLException Si hay un error al acceder a la base de datos.
     */
    private void addSubPanels(int opcion) throws SQLException {
        removeAll();
        revalidate();
        repaint();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Occupy all available horizontal space
        gbc.weighty = 1.0; // Occupy all available vertical space
        switch (opcion) {
            case 0:
                panelBienvenida();
                add(panelBienvenida, gbc);
                break;
            case 1:
                PC_Informes();
                add(PC_Informes, gbc);
                break;
            case 3:
                PC_Config();
                add(PC_Config, gbc);
                // Implement logic for PC_Stock panel
                break;
            case 4:
                PC_BBDD();
                add(PC_BBDD, gbc);
                break;
            default:
                break;
        }
    }

    /**
     * Configura el panel de bienvenida.
     */
    private void panelBienvenida() throws SQLException {
        panelBienvenida.setBackground(colorPanel);
        panelBienvenida.setLayout(new GridBagLayout()); // Establece el diseño GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // Crea las restricciones de diseño

        if (btnFichar == null) {
            btnFichar = new JButton("Fichar");
        }
        if (btnCerrarSesion == null) {
            btnCerrarSesion = new JButton("Cerrar sesion");
        }
        if (btnCambioIdioma == null) {
            btnCambioIdioma = new JButton();
        }
        if (btnColorAmarillo == null) {
            btnColorAmarillo = new JButton();
        }
        if (btnColorRosa == null) {
            btnColorRosa = new JButton();
        }
        if (btnColorVioleta == null) {
            btnColorVioleta = new JButton();
        }
        if (btnColorPredeterminado == null) {
            btnColorPredeterminado = new JButton();
        }
        GraficaDeDatos grafica1 = new GraficaDeDatos(); // Crear una instancia de la primera gráfica

        // Configuración de restricciones para la primera gráfica
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.gridwidth = 1; // Ancho de 1 celda
        gbc.gridheight = 1; // Alto de 1 celda
        gbc.weightx = 0.5; // Peso horizontal
        gbc.weighty = 1.0; // Peso vertical
        gbc.anchor = GridBagConstraints.CENTER; // Ancla al centro
        gbc.fill = GridBagConstraints.BOTH; // Llenar horizontal y verticalmente
        gbc.insets = new Insets(10, 10, 10, 5); // Espacio alrededor de la primera gráfica
        panelBienvenida.add(grafica1, gbc); // Agregar la primera gráfica al panel con restricciones

        // Configuración de restricciones para los botones
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 1; // Fila 1
        gbc.gridwidth = 2; // Ancho de 2 celdas
        gbc.weightx = 1.0; // Peso horizontal
        gbc.weighty = 0.0; // Sin expansión vertical
        gbc.fill = GridBagConstraints.NONE; // Llenar horizontalmente
        gbc.anchor = GridBagConstraints.WEST; // Anclar al centro
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio alrededor de los botones
        panelBienvenida.add(btnFichar, gbc); // Agregar el botón Fichar al panel con restricciones

        btnFichar.addActionListener((ActionListener) this);

        gbc.gridy = 3; // Fila 3
        panelBienvenida.add(btnCerrarSesion, gbc); // Agregar el botón Cerrar sesión al panel con restricciones
        btnCerrarSesion.addActionListener(this);

        ColorBotones colorBotones = new ColorBotones();
        JButton[] tusBotones = {btnFichar/*, btnConfiguracion*/, btnCerrarSesion};
        Color miColorElegido = new Color(147, 81, 22); // Color elegido
        Color miColorFondo = Color.white;
        colorBotones.aplicarEstilosBotones(tusBotones, miColorElegido, miColorFondo, miColorElegido);

    }

    /**
     * Pestañas correspondientes al apartado de Database que aparecen al pulsar
     * el botón Database.
     */
    private void PC_BBDD() throws SQLException {
        // Create an instance of TableWithInsert 
        TableWithInsert tableWithInsert = new TableWithInsert();

        // Create the PC_BBDD panel
        PC_BBDD = new JPanel();
        PC_BBDD.setBackground(colorPanel);

        // Set the layout of the PC_BBDD panel
        PC_BBDD.setLayout(new BorderLayout());

        // Add the JTabbedPane to the PC_BBDD panel
        PC_BBDD.add(tableWithInsert, BorderLayout.CENTER);
    }

    /**
     * Pestañas correspondientes al apartado de informes que aparecen al pulsar
     * el botón informes.
     */
    private void PC_Informes() throws SQLException {

        PC_Informes.setBackground(colorPanel);
        this.setBackground(colorPanel);
        JTabbedPane PCInform = new JTabbedPane();

        // Create an instance of TableWithInsert 
        PanelImprimirDatos imprimirDatos = new PanelImprimirDatos();
        // Instancia de BucadorDeInformes
        PanelBuscadorDeInformes panelBuscadorDeInformes = new PanelBuscadorDeInformes();
        // Instancia de BucadorDeInformes
        PanelRedactorDeInformes panelRedactorDeInformes = new PanelRedactorDeInformes();
        // Instancias de Generar gráficos
        PanelGenerarGraficosRedondos panelGenerarGraficosRedondos = new PanelGenerarGraficosRedondos();
        PanelGenerarGraficosBarras panelGenerarGraficosBarras = new PanelGenerarGraficosBarras();

        String buscarInformeString, imprimirDatosString, redactarInformesString, generarGraficosString, generarbarrasString;

        String idiomaActual = obtenerIdiomaActual();

        if (idiomaActual.equals("Spanish")) {
            buscarInformeString = "Buscar informes";
            imprimirDatosString = "Imprimir datos";
            redactarInformesString = "Redactar informes";
            generarGraficosString = "Generar gráficos";
            generarbarrasString = "Generar gráficos barras";
        } else {
            buscarInformeString = "Search reports";
            imprimirDatosString = "Print data";
            redactarInformesString = "Write reports";
            generarGraficosString = "Generate graphs";
            generarbarrasString = "Generate bar charts";
        }

        // Add components to the JTabbedPanes
        PCInform.addTab(buscarInformeString, panelBuscadorDeInformes);
        PCInform.addTab(imprimirDatosString, imprimirDatos);
        PCInform.addTab(redactarInformesString, panelRedactorDeInformes);
        PCInform.addTab(generarGraficosString, panelGenerarGraficosRedondos);
        PCInform.addTab(generarbarrasString, panelGenerarGraficosBarras);

        for (int i = 0; i < PCInform.getTabCount(); i++) {
            PCInform.setBackgroundAt(i, colorPanel);
        }

        this.PC_Informes.setLayout(new BorderLayout());
        this.PC_Informes.add(PCInform, BorderLayout.CENTER);
    }

    /**
     * Pestañas correspondientes al apartado de configuracion que aparecen al
     * pulsar el botón correspondiente.
     */
    private void PC_Config() throws SQLException {
        // Instancia del creador de sentencias SQL
        SQLPanel sqlPanel = new SQLPanel();
        PC_Config.setBackground(colorPanel);
        // Instancia del creador de bbdd y tablas
        CreadorBbddTablas panelCreadorBbddTablas = new CreadorBbddTablas();

        this.setBackground(colorPanel);
        JTabbedPane PCConfig = new JTabbedPane();
        PCConfig.setBackground(colorPanel);

        // Instancia de insertarEmpleado
        InsertarEmpleado panelInsertarEmpleado = new InsertarEmpleado();
        // Instancia de copiaSeguridad
        CopiaSeguridad copiaSeguridad = new CopiaSeguridad();
        // Instancia de clave foránea
        PanelCrearClaveForanea panelCrearFK = new PanelCrearClaveForanea();

        String crearTablasString, crearClaveForaneaString, idiomaColorString, copiaSeguridadString, insertarEmpleadoString, sqlManualString;

        String idiomaActual = obtenerIdiomaActual();

        if (idiomaActual.equals("Spanish")) {
            crearTablasString = "Crear tablas";
            crearClaveForaneaString = "Crear clave foránea";
            idiomaColorString = "Idioma y color";
            copiaSeguridadString = "Copia de seguridad";
            insertarEmpleadoString = "Insertar empleado";
            sqlManualString = "SQL manual";
        } else {
            crearTablasString = "Create tables";
            crearClaveForaneaString = "Create foreign key";
            idiomaColorString = "Language and color";
            copiaSeguridadString = "Backup";
            insertarEmpleadoString = "Insert employee";
            sqlManualString = "Manual SQL";
        }

        // Add components to the JTabbedPane
        PCConfig.addTab(crearTablasString, panelCreadorBbddTablas);
        PCConfig.addTab(crearClaveForaneaString, panelCrearFK);
        PCConfig.addTab(idiomaColorString, new JPanel());
        PCConfig.addTab(copiaSeguridadString, copiaSeguridad);
        PCConfig.addTab(insertarEmpleadoString, panelInsertarEmpleado);
        PCConfig.addTab(sqlManualString, sqlPanel);

        //Creamos panel para dentro del stock1, index 0
        JPanel panel = new JPanel();
        panel.setBackground(colorPanel);

        // Agregar contenido a la segunda pestaña 
        btnCambioIdioma = new JButton();
        btnCambioIdioma.setPreferredSize(new Dimension(50, 50));
        btnCambioIdioma.addActionListener((ActionListener) this);

        btnColorAmarillo = new JButton();
        btnColorAmarillo.setPreferredSize(new Dimension(50, 50));
        btnColorAmarillo.addActionListener((ActionListener) this);
        btnColorAmarillo.setBackground(new Color(249, 231, 159));

        btnColorRosa = new JButton();
        btnColorRosa.setPreferredSize(new Dimension(50, 50));
        btnColorRosa.addActionListener((ActionListener) this);
        btnColorRosa.setBackground(new Color(250, 199, 229));

        btnColorVioleta = new JButton();
        btnColorVioleta.setPreferredSize(new Dimension(50, 50));
        btnColorVioleta.addActionListener((ActionListener) this);
        btnColorVioleta.setBackground(new Color(168, 139, 175));

        btnColorPredeterminado = new JButton();
        btnColorPredeterminado.setPreferredSize(new Dimension(50, 50));
        btnColorPredeterminado.addActionListener((ActionListener) this);
        btnColorPredeterminado.setBackground(new Color(240, 240, 240));

        //Añadimos el botón al panel
        panel.add(lblCambiarIdioma);
        panel.add(btnCambioIdioma);
        panel.add(lblCambiarColor);
        panel.add(btnColorAmarillo);
        panel.add(btnColorRosa);
        panel.add(btnColorVioleta);
        panel.add(btnColorPredeterminado);

        //El index 0 indica que es la primera pestaña, el index 1 indica la segunda
        PCConfig.setComponentAt(2, panel);
        for (int i = 0; i < PCConfig.getTabCount(); i++) {
            PCConfig.setBackgroundAt(i, colorPanel);
        }

        this.PC_Config.setLayout(new BorderLayout());
        this.PC_Config.setBackground(colorPanel);
        this.PC_Config.add(PCConfig, BorderLayout.CENTER);
    }

    /**
     * Método que aplica estilos a los botones para fichar configurar y Cerrar
     * sesion.
     *
     * @param botones
     */
    private void aplicarEstilosBotones(JButton[] botones) {
        try {
            // Cargar la fuente Montserrat-Regular desde un archivo
            Font fuenteMontserrat = Font.createFont(Font.TRUETYPE_FONT, new File("./recursos/Montserrat-Regular.ttf")).deriveFont(Font.PLAIN, 14);

            // Asignar los estilos a cada botón del array
            for (JButton boton : botones) {
                boton.setFont(fuenteMontserrat);
                boton.setBackground(Color.WHITE); // Windows blue color
                boton.setForeground(colorBoton);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorBoton), // Color del borde
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
                        boton.setBackground(colorBoton);
                        boton.setForeground(colorFondoBotones);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Restaurar el color de fondo cuando el ratón sale
                        boton.setBackground(colorFondoBotones);
                        boton.setForeground(colorBoton);
                    }
                });
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Si hay algún error al cargar la fuente, se utilizará la fuente predeterminada
            Font fuentePredeterminada = new Font("Arial", Font.PLAIN, 14);
            for (JButton boton : botones) {
                boton.setFont(fuentePredeterminada);
                boton.setBackground(new Color(204, 102, 255));
                boton.setForeground(Color.WHITE);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCambioIdioma) {
            CambiarIdioma cambiarIdioma = new CambiarIdioma();
            if (idiomaActual.equals("English")) {
                ventanaCambioIdiomaRealizado("language");
            } else {
                ventanaCambioIdiomaRealizado("idioma");
            }

        }
        if (e.getSource() == btnColorAmarillo) {
            cambiarColorInterfaz("Amarillo");
            ventanaCambioIdiomaRealizado("color");
        }
        if (e.getSource() == btnColorVioleta) {
            cambiarColorInterfaz("Violeta");
            ventanaCambioIdiomaRealizado("color");
        }
        if (e.getSource() == btnColorRosa) {
            cambiarColorInterfaz("Rosa");
            ventanaCambioIdiomaRealizado("color");
        }
        if (e.getSource() == btnColorPredeterminado) {
            cambiarColorInterfaz("Predeterminado");
            ventanaCambioIdiomaRealizado("color");
        }
        if (e.getSource() == btnCerrarSesion) {
            // Dialog window pop up to ask the user if they want to close the application
            int option;
            if (idiomaActual.equals("English")) {
                option = JOptionPane.showConfirmDialog(this, "Do you want to close the application?", "Confirm Close", JOptionPane.YES_NO_OPTION);
            } else {
                option = JOptionPane.showConfirmDialog(this, "¿Desea cerrar la aplicación?", "Confirmar cierre", JOptionPane.YES_NO_OPTION);
            }
            if (option == JOptionPane.YES_OPTION) {
                // Close the application
                System.exit(0);
            } else {
                // No hace nada
            }
        }
        if (e.getSource() == btnFichar) {
            SistemaFichado sistemaFichado = new SistemaFichado();
            try {
                sistemaFichado.insertarFechaHoraFichado();
                comprobarEstadoFichaje();
            } catch (SQLException ex) {
                System.out.println("Problemas al fichar en panelesBotonesPestanas: " + ex);
            }
        }
    }

    private void cambiarColorInterfaz(String color) {

        try {
            // Actualiza el campo del idioma en la base de datos
            actualizarCampoPorID("bbdd_config_softgenius", "configuracion", 1, "color", color);
            // Muestra un mensaje de confirmación
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar idioma en cambiarIdioma: " + e.getMessage());
        }
    }

    /**
     * Método para comprobar el estado de fichaje
     */
    private void comprobarEstadoFichaje() throws SQLException {
        SistemaFichado sistemaFichado = new SistemaFichado();
        boolean resultado = sistemaFichado.comprobarSiFechaSalidaEsNull();
        actualizarIdioma(idiomaActual, resultado);
    }
    /**
     * ***************************************************************************
     * ///////////////////////////////////////////////////////////////////////////
     *
     * GESTIÓN IDIOMAS
     *
     * //////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */


    /**
     * Actualiza el idioma en los elementos de la interfaz de usuario.
     *
     * @param idioma El idioma al que se actualizará la interfaz de usuario.
     * @param isFicharSalida Estado de fichaje actual.
     */
    private void actualizarIdioma(String idioma, boolean isFicharSalida) {
        // Cargar el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar el texto de btnFichar según el estado de fichaje
        if (isFicharSalida) {
            btnFichar.setText(resourceBundle.getString("btnFicharSalida"));
        } else {
            btnFichar.setText(resourceBundle.getString("btnFicharEntrada"));
        }
        btnCerrarSesion.setText(resourceBundle.getString("btnCerrarSesion"));
        lblCambiarIdioma.setText(resourceBundle.getString("lblCambiarIdioma"));
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

    /**
     * Este método configura la imagen del botón dependiendo del idioma que esté
     * seleccionado.
     */
    private void gestionBanderaBotonIdioma() {
        try {
            idiomaActual = obtenerIdiomaActual();
            boolean estadoFichaje = new SistemaFichado().comprobarSiFechaSalidaEsNull();
            actualizarIdioma(idiomaActual, estadoFichaje);

            //String idiomaActual = obtenerIdiomaActual();
            ImageIcon iconoIdioma;
            if (idiomaActual.equals("Spanish")) {
                iconoIdioma = new ImageIcon("recursos/banderas/English.png");
            } else {
                iconoIdioma = new ImageIcon("recursos/banderas/Spanish.png");
            }

            btnCambioIdioma.setIcon(iconoIdioma);
            btnCambioIdioma.setOpaque(false);
            btnCambioIdioma.setContentAreaFilled(false);
            btnCambioIdioma.setBorderPainted(false);
        } catch (SQLException ex) {
            // Manejar la excepción adecuadamente
            ex.printStackTrace();
        }
    }

    /**
     * Método que muestra una ventana de confirmación para el cambio de idioma
     */
    private void ventanaCambioIdiomaRealizado(String clave) {
        int opcion;
        if (idiomaActual.equals("English")) {
            opcion = JOptionPane.showConfirmDialog(null, "The " + clave + " will be modified upon restarting the program. Do you want to exit the program to update?", "Exit Program", JOptionPane.YES_NO_OPTION);
        } else {
            opcion = JOptionPane.showConfirmDialog(null, "El " + clave + " se modificará al reiniciar el programa. ¿Quiere salir del programa para actualizar?", "Salir del Programa", JOptionPane.YES_NO_OPTION);
        }
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
