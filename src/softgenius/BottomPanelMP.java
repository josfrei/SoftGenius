package softgenius;

import idioma.Idiomas;
import com.toedter.calendar.JCalendar;
import config.ClaseCambiarColorDeFondoInterfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.swing.border.Border;

/**
 * Esta clase representa el panel inferior del menú principal, que contiene
 * información del usuario activo y un botón para mostrar el calendario.
 * Implementa ActionListener para manejar eventos de acción.
 *
 * @autor Grupo 1 - 1º DAM Colexio Karbo
 */
public class BottomPanelMP extends JPanel implements ActionListener {

    private Color colorFondo  = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private final String nombreUA = "Iago";
    private final JLabel lblUsuarioActivo = new JLabel();
    private final JButton botonMostrarCalendario = new JButton(new ImageIcon("clock_icon.png"));
    private final JLabel labelCalendario = new JLabel();

    private Connection conexionBBDD;

    /**
     * Constructor de la clase BottomPanelMP. Configura el estilo del panel,
     * inicializa los componentes y actualiza el idioma.
     */
    public BottomPanelMP() {
        style();
        initComp();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        try {

            idiomaActual = obtenerIdiomaActual();
            actualizarIdioma(idiomaActual);

        } catch (SQLException ex) {
            // Manejar la excepción adecuadamente
            ex.printStackTrace();
        }
    }

    /**
     * Aplica estilos al panel principal.
     */
    private void style() {
        setBackground(colorFondo);
        setLayout(new GridBagLayout());
    }

    /**
     * Inicializa los componentes del panel.
     */
    private void initComp() {
        lblUsuarioActivo();
        labelCalendario();

        botonMostrarCalendario.setHorizontalTextPosition(SwingConstants.CENTER);
        botonMostrarCalendario.setVerticalTextPosition(SwingConstants.TOP);
        botonMostrarCalendario.setContentAreaFilled(false);
        botonMostrarCalendario.setFocusPainted(false);
        botonMostrarCalendario.setBorderPainted(false);
        botonMostrarCalendario.setOpaque(false);
        botonMostrarCalendario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarHoraFecha();

        botonMostrarCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCalendario();
            }
        });

        botonMostrarCalendario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonMostrarCalendario.setContentAreaFilled(true);
                botonMostrarCalendario.setBackground(new Color(200, 200, 200)); // Color de fondo para el sombreado interior
                botonMostrarCalendario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonMostrarCalendario.setContentAreaFilled(false);
            }
        });

        GridBagConstraints gbcUsuarioActivo = new GridBagConstraints();
        gbcUsuarioActivo.gridx = 2; // Posición x en la tercera columna
        gbcUsuarioActivo.gridy = 0; // Posición y en la única fila
        gbcUsuarioActivo.weightx = 0.25; // Porcentaje de espacio horizontal
        gbcUsuarioActivo.weighty = 0.5; // Porcentaje de espacio vertical
        gbcUsuarioActivo.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbcUsuarioActivo.anchor = GridBagConstraints.WEST; // Alinear al inicio horizontalmente
        gbcUsuarioActivo.insets = new Insets(0, 10, 0, 10); // Márgenes

        GridBagConstraints gbcCalendario = new GridBagConstraints();
        gbcCalendario.gridx = GridBagConstraints.RELATIVE; // Usamos RELATIVE para colocarlo después del usuario activo
        gbcCalendario.gridy = 0; // Posición y en la única fila
        gbcCalendario.weightx = 0.25; // Porcentaje de espacio horizontal
        gbcCalendario.weighty = 0.5; // Porcentaje de espacio vertical
        gbcCalendario.fill = GridBagConstraints.NONE; // No rellenar
        gbcCalendario.anchor = GridBagConstraints.EAST; // Alinear al extremo derecho horizontalmente
        gbcCalendario.insets = new Insets(0, 0, 0, 10); // Márgenes a la derecha

        // Panel para contener el calendario
        JPanel panelCalendario = new JPanel(new BorderLayout());
        panelCalendario.add(labelCalendario, BorderLayout.CENTER); // Añadir el JLabel al panel

        // Agrega el panel del calendario al labelCalendario con los constraints
        add(panelCalendario, gbcCalendario);

        add(lblUsuarioActivo, gbcUsuarioActivo);
        add(botonMostrarCalendario, gbcCalendario);
    }

    private void lblUsuarioActivo() {
        lblUsuarioActivo.setForeground(new Color(100, 100, 100)); // Color pastel para la etiqueta
        Font font = new Font("Arial", Font.PLAIN, 14); // Fuente Arial, tamaño 14, estilo plano
        lblUsuarioActivo.setFont(font); // Aplicar la fuente al JLabel
    }

    private void labelCalendario() {
        labelCalendario.setHorizontalAlignment(SwingConstants.CENTER);
        Border calendarioBorder = BorderFactory.createLineBorder(Color.lightGray);
        Border roundedBorder = BorderFactory.createLineBorder(Color.lightGray, 1);
        Border compoundBorder = BorderFactory.createCompoundBorder(calendarioBorder, roundedBorder);
        labelCalendario.setBorder(compoundBorder);
    }

    /**
     * Actualiza la hora y la fecha en el botón de mostrar calendario.
     */
    private void actualizarHoraFecha() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        String hora = formatoHora.format(calendario.getTime());
        String fecha = formatoFecha.format(calendario.getTime());

        botonMostrarCalendario.setText("<html>" + hora + "&nbsp;&nbsp;&nbsp;" + fecha + "</html>");
    }

    /**
     * Muestra un calendario en un diálogo emergente.
     */
    private void mostrarCalendario() {
        // Obtener la posición del botón para colocar el diálogo encima
        Point location = botonMostrarCalendario.getLocationOnScreen();
        int x = (int) location.getX();
        int y = (int) location.getY();

        // Ajustar la posición para mover el diálogo un poco hacia la izquierda
        int xOffset = 280;
        x -= xOffset;

        // Crear el diálogo del calendario
        JDialog dialogo = new JDialog();
        dialogo.setTitle("Calendario");
        dialogo.setModal(true);
        dialogo.setSize(400, 400);

        // Establecer la posición justo arriba del botón
        dialogo.setLocation(x, y - dialogo.getHeight());

        // Crear el calendario y agregarlo al diálogo
        JCalendar calendario = new JCalendar();
        dialogo.add(calendario);

        dialogo.setVisible(true);
    }

    /**
     * Override del método getPreferredSize para definir el tamaño preferido del
     * panel.
     *
     * @return Dimension con el tamaño preferido del panel.
     */
    @Override
    public Dimension getPreferredSize() {
        // Obtener el tamaño del contenedor padre
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width);
        // La altura del 5%
        int height = (int) (parentSize.width * 0.05);
        // Devolver la dimensión calculada
        return new Dimension(width, height);
    }

    /**
     * Maneja los eventos de acción de los botones del panel.
     *
     * @param e El evento de acción generado por los botones.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lógica tras pulsar el calendario
    }
    //************************************************************************//
    // Cambio de Idioma
    //************************************************************************//
    private String idiomaActual = "Spanish";

    /**
     * Actualiza el idioma de los textos del panel.
     *
     * @param idioma El idioma a actualizar.
     */
    private void actualizarIdioma(String idioma) {
        // Carga el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);
        // Obtiene el texto de "Sesión iniciada como:" del archivo de propiedades
        String textoSesionIniciadaComo = resourceBundle.getString("lblUsuarioActivo");
        // Concatena el nombre de usuario actual al texto obtenido
        String textoTraducido = textoSesionIniciadaComo + nombreUA;
        // Establece el texto traducido en el JLabel
        lblUsuarioActivo.setText(textoTraducido);
    }

    /**
     * Obtiene el idioma actual configurado en la aplicación.
     *
     * @return El idioma actual como una cadena.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

}
