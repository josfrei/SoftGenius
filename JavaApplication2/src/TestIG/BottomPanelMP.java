package TestIG;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
/**
 *
 * @author Iago
 */
/*Clase que representa el panel inferior del menu principal, con un label para el usuario activo y un panel en el que se introducirá el calendario*/
public class BottomPanelMP extends JPanel implements ActionListener{
    private int ancho;
    private final Color colorFondo = new Color(240, 240, 240);
    private final String nombreUA= "Iago";
    private final JLabel lblUsuarioActivo = new JLabel("Sesión iniciada como: "+ nombreUA);
    private final JLabel labelCalendario = new JLabel("Placeholder");
    
    
    public BottomPanelMP() {
        style();
        initComp();
        setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
    }
    
    private void style() {
        setBackground(colorFondo);
        setLayout(new GridBagLayout()); 
    }
    
    
    private void initComp() {
        lblUsuarioActivo();
        labelCalendario();

        // Crear spacer 1
        JPanel spacer1 = new JPanel();
        spacer1.setPreferredSize(new Dimension(0, 0));
        GridBagConstraints gbcSpacer1 = new GridBagConstraints();
        gbcSpacer1.gridx = 0; // Posición x en la primera columna
        gbcSpacer1.gridy = 0; // Posición y en la única fila
        gbcSpacer1.weightx = 0.25; // Porcentaje de espacio horizontal
        gbcSpacer1.weighty = 0.5; // Porcentaje de espacio vertical
        gbcSpacer1.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbcSpacer1.anchor = GridBagConstraints.CENTER; // Alinear al centro horizontalmente
        gbcSpacer1.insets = new Insets(0, 10, 0, 10); // Márgenes

        // Crear spacer 2
        JPanel spacer2 = new JPanel();
        spacer2.setPreferredSize(new Dimension(0, 0));
        GridBagConstraints gbcSpacer2 = new GridBagConstraints();
        gbcSpacer2.gridx = 1; // Posición x en la segunda columna
        gbcSpacer2.gridy = 0; // Posición y en la única fila
        gbcSpacer2.weightx = 0.25; // Porcentaje de espacio horizontal
        gbcSpacer2.weighty = 0.5; // Porcentaje de espacio vertical
        gbcSpacer2.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbcSpacer2.anchor = GridBagConstraints.CENTER; // Alinear al centro horizontalmente
        gbcSpacer2.insets = new Insets(0, 10, 0, 10); // Márgenes

        // Crear constraints para lblUsuarioActivo
        GridBagConstraints gbcUsuarioActivo = new GridBagConstraints();
        gbcUsuarioActivo.gridx = 2; // Posición x en la tercera columna
        gbcUsuarioActivo.gridy = 0; // Posición y en la única fila
        gbcUsuarioActivo.weightx = 0.25; // Porcentaje de espacio horizontal
        gbcUsuarioActivo.weighty = 0.5; // Porcentaje de espacio vertical
        gbcUsuarioActivo.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbcUsuarioActivo.anchor = GridBagConstraints.WEST; // Alinear al inicio horizontalmente
        gbcUsuarioActivo.insets = new Insets(0, 10, 0, 10); // Márgenes

        // Crear constraints para labelCalendario
        GridBagConstraints gbcCalendario = new GridBagConstraints();
        gbcCalendario.gridx = 4; // Posición x en la quinta columna
        gbcCalendario.gridy = 0; // Posición y en la única fila
        gbcCalendario.weightx = 0.2; // Porcentaje de espacio horizontal
        gbcCalendario.weighty = 0.5; // Porcentaje de espacio vertical
        gbcCalendario.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbcCalendario.anchor = GridBagConstraints.CENTER; // Alinear al centro horizontalmente
        gbcCalendario.insets = new Insets(0, 0, 0, 40); // Márgenes

        // Agregar spacers, lblUsuarioActivo y labelCalendario con los constraints
        add(spacer1, gbcSpacer1);
        add(spacer2, gbcSpacer2);
        add(lblUsuarioActivo, gbcUsuarioActivo);
        add(labelCalendario, gbcCalendario);
}
    
    private void lblUsuarioActivo(){
        lblUsuarioActivo.setForeground(new Color(100, 100, 100)); // Color pastel para la etiqueta
        Font font = new Font("Arial", Font.PLAIN, 14); // Fuente Arial, tamaño 14, estilo plano
        lblUsuarioActivo.setFont(font); // Aplicar la fuente al JLabel
    }
    
    private void labelCalendario() {
        
        labelCalendario.setHorizontalAlignment(SwingConstants.CENTER);

        //labelCalendario.setSize(200, 100);
        Border calendarioBorder = BorderFactory.createLineBorder(Color.lightGray);

        // Creamos un borde redondeado con un radio de 10
        Border roundedBorder = BorderFactory.createLineBorder(Color.lightGray, 1);

        // Creamos un borde compuesto que incluya ambos bordes
        Border compoundBorder = BorderFactory.createCompoundBorder(calendarioBorder, roundedBorder);

        // Asignamos el borde compuesto al panel
        labelCalendario.setBorder(compoundBorder);
    }

    
    // Override del método getPreferredSize para definir el tamaño preferido del panel
    @Override
    public Dimension getPreferredSize() {
        // Obtener el tamaño del contenedor padre
        Dimension parentSize = getParent().getSize();
        int width = (int) (parentSize.width);
        // La altura del 5%
        int height = (int) (parentSize.width *0.05);
        // Guardar el ancho calculado si es necesario
        ancho = width;
        // Devolver la dimensión calculada
        return new Dimension(width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==labelCalendario){
        //Lógica tras pulsar el calendario
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
