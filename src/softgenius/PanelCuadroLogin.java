package softgenius;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.NivelUsuario;
import idioma.Idiomas;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Esta clase se centra en el cuadro del login donde el usuario introduce su
 * usuario y contraseña.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class PanelCuadroLogin extends JPanel implements ActionListener {

    private final JLabel lblTitulo = new JLabel("Inicio de Sesión");
    private final JLabel lblUsuario = new JLabel("Usuario:");
    private final JLabel lblContrasena = new JLabel("Contraseña:");
    private final JTextField tfUsuario = new JTextField(20);
    private final JPasswordField pfContrasenia = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Iniciar Sesión");
    //private JButton btnCambiarIdioma = new JButton(); /// Idioma
    private String idiomaActual = "Spanish";
    private Color colorFondo = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    private Connection conexionBBDD;

    /**
     * Constructor de la clase PanelCuadroLogin.
     *
     * Configura el panel de login, establece el layout, los estilos de los
     * componentes, la gestión de idiomas y la funcionalidad para presionar
     * Enter para iniciar sesión.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public PanelCuadroLogin() {
        setBackground(colorFondo);
        setLayout(new GridBagLayout());
        GBC();
        estilizarComponentes();
        gestionIdioma();
        implementarEnter();
    }

    /**
     * Configura el GridBagConstraints para el layout del panel.
     */
    private void GBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.bottom = 10;
        add(lblTitulo, gbc);

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy++;
        add(lblUsuario, gbc);

        gbc.gridy++;
        add(tfUsuario, gbc);

        gbc.gridy++;
        add(lblContrasena, gbc);

        gbc.gridy++;
        add(pfContrasenia, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);

        btnLogin.addActionListener(this);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
    }

    /**
     * Aplica estilos a los componentes del panel de login.
     */
    private void estilizarComponentes() {
        Font tituloFont = new Font("Arial", Font.BOLD, 24);
        lblTitulo.setFont(tituloFont);
        lblTitulo.setForeground(Color.BLACK);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        lblUsuario.setFont(labelFont);
        lblContrasena.setFont(labelFont);

        btnLogin.setBackground(new Color(204, 102, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));

        tfUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        pfContrasenia.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    /**
     * /////////////////////////////////////////////////////////////////////////
     *
     * Autentificación del Login
     *
     * /////////////////////////////////////////////////////////////////////////
     */
    /**
     * Maneja los eventos de acción en el panel de login.
     *
     * @param e El evento de acción que se disparó.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            // Get the entered username and password
            String username = tfUsuario.getText();
            String password = new String(pfContrasenia.getPassword());

            // Check if either username or password is empty
            if (username.isEmpty() || password.isEmpty()) {
                // Display an error message indicating that both username and password are required
                if (idiomaActual.equals("English")) {
                    JOptionPane.showMessageDialog(this, "Please enter both username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese tanto el nombre de usuario como la contraseña", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }
                return; // Return without further processing
            }

            try {
                // Authenticate the user against the database
                if (authenticateUser(username, password)) {
                    try {
                        // Guarda el nombre de usuario en la clase NivelUsuario
                        NivelUsuario.nombreUsuario = username;
                        
                        // Obtiene el JFrame padre del PanelLogin
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        
                        // Crea y muestra el panelMenuPrincipal
                        JPanel panelMP = new PanelMenuPrincipal();
                        frame.getContentPane().removeAll(); // Limpia todos los componentes actuales del JFrame
                        frame.getContentPane().add(panelMP); // Agrega el nuevo panel al JFrame
                        frame.revalidate(); // Vuelve a validar el contenido del JFrame para que se muestre el nuevo panel
                    } catch (IOException ex) {
                        Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // Authentication failed
                    if (idiomaActual.equals("English")) {
                        JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nombre de usuario o contraseña inválidos", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    }
                    pfContrasenia.setText("");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Autentica al usuario contra la base de datos.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    private boolean authenticateUser(String username, String password) throws SQLException {

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_cuentas_softgenius")) {
            String query = "SELECT * FROM cuenta WHERE Usuario = ? AND Contrasenha = ?";
            PreparedStatement statement = conexionBBDD.prepareStatement(query);

            // Set parameters for the query
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if any rows were returned
            return resultSet.next();
        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para aceptar credenciales pulsando ENTER
     */
    private void implementarEnter() {
        pfContrasenia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick();
            }
        });
    }

    /**
     * /////////////////////////////////////////////////////////////////////////
     *
     * Cambio de Idioma
     *
     * /////////////////////////////////////////////////////////////////////////
     */
    /**
     * Inicializa la gestión de idioma.
     */
    private void gestionIdioma() {
        conectarABDD();
        actualizarVariableIdioma();
        actualizarIdioma(idiomaActual);
    }

    /**
     * Obtiene el idioma actual.
     *
     * @return El idioma actual.
     */
    public String getIdiomaActual() {
        return idiomaActual;
    }

    /**
     * Establece el idioma actual.
     *
     * @param idiomaActual El idioma a establecer.
     */
    public void setIdiomaActual(String idiomaActual) {
        this.idiomaActual = idiomaActual;
    }

    /**
     * Actualiza los textos de los componentes al idioma especificado.
     *
     * @param idioma El idioma a utilizar.
     */
    private void actualizarIdioma(String idioma) {
        // Cargar el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);
        //setBotonCambiarIdioma();
        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        lblTitulo.setText(resourceBundle.getString("lblTitulo"));
        lblUsuario.setText(resourceBundle.getString("lblUsuario"));
        lblContrasena.setText(resourceBundle.getString("lblContrasena"));
        btnLogin.setText(resourceBundle.getString("btnLogin"));
    }

    /**
     * Obtiene el idioma actual desde la base de datos.
     *
     * @return El idioma actual.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

    /**
     * Conecta a la base de datos de configuración.
     */
    private void conectarABDD() {
        try {
            conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");
        } catch (SQLException ex) {
            Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Actualiza la variable del idioma actual según el valor en la base de
     * datos.
     */
    private void actualizarVariableIdioma() {
        try {
            setIdiomaActual(obtenerIdiomaActual());
        } catch (SQLException ex) {
            Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
