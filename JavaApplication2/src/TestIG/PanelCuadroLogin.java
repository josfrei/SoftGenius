
package TestIG;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author Iago
 */
public class PanelCuadroLogin extends JPanel implements ActionListener{
    private final JLabel lblTitulo = new JLabel("Inicio de Sesión");
    private final JLabel lblUsuario = new JLabel("Usuario:");
    private final JLabel lblContrasenia = new JLabel("Contraseña:");
    private final JTextField tfUsuario = new JTextField(20);
    private final JPasswordField pfContrasenia = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Iniciar Sesión");

    @SuppressWarnings("LeakingThisInConstructor")
    public PanelCuadroLogin() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.bottom = 10;
        add(lblTitulo, gbc);

        gbc.gridy++;
        add(lblUsuario, gbc);

        gbc.gridy++;
        add(tfUsuario, gbc);

        gbc.gridy++;
        add(lblContrasenia, gbc);

        gbc.gridy++;
        add(pfContrasenia, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);
        btnLogin.addActionListener(this);
        // Agregar borde al panel
        Border borde = BorderFactory.createLineBorder(Color.GRAY); // Borde gris fino
        setBorder(BorderFactory.createCompoundBorder(borde, BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Agrega borde con márgenes
        
        // Estilos
        estilizarComponentes();
    }
    // Estilos para componentes
    private void estilizarComponentes() {
        Font tituloFont = new Font("Arial", Font.BOLD, 24);
        lblTitulo.setFont(tituloFont);
        lblTitulo.setForeground(Color.BLACK);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        lblUsuario.setFont(labelFont);
        lblContrasenia.setFont(labelFont);

        btnLogin.setBackground(new Color(204, 102, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));

        tfUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        pfContrasenia.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            try {
                // Obtén el JFrame padre del PanelLogin
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                
                // Crea y muestra el panelMenuPrincipal
                JPanel panelMP = new PanelMenuPrincipal();
                frame.getContentPane().removeAll(); // Limpia todos los componentes actuales del JFrame
                frame.getContentPane().add(panelMP); // Agrega el nuevo panel al JFrame
                frame.revalidate(); // Vuelve a validar el contenido del JFrame para que se muestre el nuevo panel
            } catch (IOException ex) {
                Logger.getLogger(PanelCuadroLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
