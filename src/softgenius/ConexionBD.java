package softgenius;

import java.sql.SQLException;
import libsql.*;
import java.sql.Connection;

/**
 * Esta clase proporciona métodos para establecer una conexión con una base de
 * datos. Utiliza un objeto GestorBaseDatos para gestionar la conexión.
 *
 * El método estático obtenerConexion permite obtener una conexión a una base de
 * datos específica proporcionando su nombre.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class ConexionBD {

    private static final GestorBaseDatos gestorBD;

    static {
        gestorBD = new GestorBaseDatos();
    }

    /**
     * Método estático para obtener una conexión a una base de datos específica.
     *
     * @param nombreBD El nombre de la base de datos a la que se desea conectar.
     * @return Una conexión a la base de datos especificada.
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    public static Connection obtenerConexion(String nombreBD) throws SQLException {
        return gestorBD.conectarBaseDatos(nombreBD);
    }
}
