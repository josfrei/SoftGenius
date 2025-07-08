package idioma;

/**
 *
 * @author josef
 */
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static libsql3.softgenius.GestorTablas.actualizarCampoPorID;

public class CambiarIdioma {

    //Instancia para la conexión a la base de datos
    private String idioma;

    /**
     * Constructor de la clase CambiarIdioma.
     * Obtiene el idioma actual desde la base de datos y realiza el cambio de idioma.
     */
    public CambiarIdioma() {
        try {
            idioma = obtenerIdiomaActual();
            cambioIdioma();

        } catch (SQLException ex) {
            // Manejar la excepción adecuadamente
            System.out.println("Problemas en la clase CambiarIdioma: " + ex);
        }
    }

    /**
     * Obtenemos el idioma que consta en la base de datos
     *
     * @return el idioma actual como una cadena de texto.
     * @throws SQLException  si ocurre un error al acceder a la base de datos.
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

    /**
     * Realiza el cambio de idioma alternando entre español e inglés.
     *
     * @throws SQLException si ocurre un error al actualizar el idioma en la base de datos.
     */
    public void cambioIdioma() throws SQLException {
        if (idioma.equals("Spanish")) {
            insertarNuevoIdioma("English");
        } else if (idioma.equals("English")) {
            insertarNuevoIdioma("Spanish");
        }
    }

    /**
     * Inserta el nuevo idioma en la base de datos y muestra un mensaje de confirmación para reiniciar el programa.
     *
     * @param idiomaNuevo el nuevo idioma a establecer.
     * @throws SQLException si ocurre un error al actualizar el idioma en la base de datos.
     */
    private void insertarNuevoIdioma(String idiomaNuevo) throws SQLException {
        try {
            // Actualiza el campo del idioma en la base de datos
            actualizarCampoPorID("bbdd_config_softgenius", "configuracion", 1, "idioma", idiomaNuevo);
            // Muestra un mensaje de confirmación
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar idioma en cambiarIdioma: " + e.getMessage());
        }
    }
}
