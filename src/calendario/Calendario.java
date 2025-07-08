package calendario;

import com.toedter.calendar.JCalendar;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.util.ResourceBundle;

/**
 * Clase para mostrar un diálogo de calendario.
 * @author Grupo 1 - 1º DAM Colexio Karbo.
 */
public class Calendario extends JDialog {
    /**
     * Crea un nuevo calendario.
     * @param parent El frame padre del diálogo.
     * @param idioma El idioma seleccionado.
     */
    public Calendario(JFrame parent, String idioma) {
        super(parent, "", true); // Título vacío temporalmente
        setSize(400, 400);
        setLocationRelativeTo(parent);
        // Carga el archivo de propiedades correspondiente al idioma
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);
        // Obtiene el título del diálogo de calendario del archivo de propiedades
        String titulo = resourceBundle.getString("tituloCalendario");
        // Establece el título del diálogo
        setTitle(titulo);
        // Crea el calendario y agregarlo al diálogo
        JCalendar calendar = new JCalendar();
        add(calendar);
    }
}

