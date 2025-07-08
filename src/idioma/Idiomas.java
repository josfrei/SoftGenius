package idioma;

/**
 * Obtiene el idioma desde la base de datos. 
 * Por seguridad toma como idioma inicial "Spanish".
 *
 * @author Grupo 1
 */
import java.sql.SQLException;
import static libsql3.softgenius.GestorTablas.obtenerFila;

public class Idiomas {
    
    /**
     * Aquí obtenermos el idioma que consta en la bbdd para iniciar el programa.
     * @return
     * @throws SQLException 
     */
    public String obtenerIdiomaActual() throws SQLException {
        String idiomaActual = "Spanish"; // Valor predeterminado
        try {
            String[] fila = obtenerFila(1, "bbdd_config_softgenius", "configuracion");
            idiomaActual = fila[2];
        } catch (Exception e) {
            System.out.println("Falla clase Idiomas.java: "+e);
        }
        return idiomaActual;
    }

}
