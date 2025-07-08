package config;

import java.sql.*;
import softgenius.ConexionBD;

/**
 * Esta clase almacena el usuario activo. También busca el nivel de privilegios
 * a la hora de acceder a las tablas: - No deja insertar, eliminar o editar las
 * tablas empleados, pero sí el resto.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class NivelUsuario {

    // Variable estática para almacenar el nombre de usuario
    public static String nombreUsuario = "";

    // Método estático para obtener el nivel de usuario
    public static int obtenerNivelUsuario(String usuario) {
        // Definir la conexión a la base de datos
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int nivelUsuario = 0;

        try ( Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_cuentas_softgenius")) {

            // Consulta SQL para obtener el nivel de usuario según el usuario introducido
            String sql = "SELECT PrivilegioID FROM cuenta WHERE Usuario = ?";
            statement = conexionBBDD.prepareStatement(sql);
            statement.setString(1, usuario);

            // Ejecutar la consulta
            resultSet = statement.executeQuery();

            // Si se encontró un resultado, obtener el nivel de usuario
            if (resultSet.next()) {
                nivelUsuario = resultSet.getInt("PrivilegioID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión y liberar recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Devolver el nivel de usuario obtenido de la base de datos
        return nivelUsuario;
    }
    /**
     * Este método se conecta a la base de datos y 
     * obtiene el nombre del empleado asociado al usuario proporcionado 
     */
    public static String obtenerNombre(String usuario) {
        String nombreEmpleado = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_cuentas_softgenius")) {

            // Consulta SQL para obtener el nombre del empleado asociado al usuario
            String sql = "SELECT e.Nombre FROM empleado e "
                    + "INNER JOIN cuenta c ON e.CuentaID = c.CuentaIDcuenta "
                    + "WHERE c.Usuario = ?";
            statement = conexionBBDD.prepareStatement(sql);
            statement.setString(1, usuario);

            // Ejecutar la consulta
            resultSet = statement.executeQuery();

            // Verificar si se encontró un resultado
            if (resultSet.next()) {
                nombreEmpleado = resultSet.getString("Nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión y liberar recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Devolver el nombre del empleado encontrado
        return nombreEmpleado;
    }
}
