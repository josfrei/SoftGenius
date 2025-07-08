package fichado;

import config.NivelUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import softgenius.ConexionBD;

/**
 * En esta clase vamos a gestionar el sistema de fichado y también el permitir o
 * no acceder al programa si está fichado o no.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class SistemaFichado {

    public static boolean activarLeftPanel;
    String usuarioActivo = NivelUsuario.nombreUsuario;

    /**
     * Obtenemos la CuentaID del usuario activo a partir del usuario introducido
     * por el empleado.
     *
     * @return
     * @throws SQLException
     */
    private int obtenerCuentaId() throws SQLException {
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_cuentas_softgenius")) {
            String query = "SELECT CuentaID FROM cuenta WHERE Usuario = ?";
            try (PreparedStatement statement = conexionBBDD.prepareStatement(query)) {
                statement.setString(1, usuarioActivo);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("CuentaID");
                    } else {
                        return -1;
                    }
                }
            }
        }
    }

    /**
     * Método para obtener el EmpleadoID de la tabla empleado a través de su
     * CuentaID.
     *
     * @return
     * @throws SQLException
     */
    public int obtenerEmpleadoId() throws SQLException {
        int cuentaID = obtenerCuentaId();

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
            String query = "SELECT EmpleadoID FROM empleado WHERE CuentaID = ?";
            try (PreparedStatement statement = conexionBBDD.prepareStatement(query)) {
                statement.setInt(1, cuentaID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("EmpleadoID");
                    } else {
                        return -1;
                    }
                }
            }
        }
    }

    /**
     * Boleano para comprobar si hay una fecha salida NULL buscando por
     * EmpleadoID. Si no hay ninguna fecha de salida en null significa que no se
     * fichó la entrada, por lo cual será lo que se fiche. Si ya hay una fecha
     * salida NULL, al momento de fichar se ficha la salida.
     *
     * @return
     * @throws SQLException
     */
    public boolean comprobarSiFechaSalidaEsNull() throws SQLException {
        int empleadoID = obtenerEmpleadoId();
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
            String query = "SELECT 1 FROM fichar WHERE EmpleadoID = ? AND Fecha_salida IS NULL";
            try (PreparedStatement statement = conexionBBDD.prepareStatement(query)) {
                statement.setInt(1, empleadoID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
    }

    public boolean activacionLeftPanel() throws SQLException {
        boolean esFechaSalidaNull = comprobarSiFechaSalidaEsNull();

        // Actualizamos la variable que gestiona el activar o desactivar el leftPanelv2
        activarLeftPanel = !esFechaSalidaNull;
        return esFechaSalidaNull;
    }

    /**
     * Método que gestiona la inserción en la tabla fichar. Si hay una fecha
     * salida null significa que ya hay una entrada, por lo cual actualizará la
     * salida. Si no hay una fecha salida null significa que no hay entrada, por
     * lo cual inserta entrada. Formato fecha YYYY-MM-DD Formato hora HH:mm:SS
     * Si el empleado no fichó salida, al iniciar una nueva jornada laboral, el
     * personal con nivel 0 o 1 debe modificar la salida.
     *
     * @throws SQLException
     */
    public void insertarFechaHoraFichado() throws SQLException {
        boolean esFechaSalidaNull = comprobarSiFechaSalidaEsNull();

        // Obtener la fecha y hora actuales
        java.util.Date fechaActual = new java.util.Date();
        java.sql.Date fecha = new java.sql.Date(fechaActual.getTime());
        java.sql.Time hora = new java.sql.Time(fechaActual.getTime());

        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_empleados_softgenius")) {
            String query;
            if (esFechaSalidaNull) {
                // Insertar la fecha y hora de salida
                query = "UPDATE fichar SET Fecha_salida = ?, Hora_salida = ? WHERE EmpleadoID = ? AND Fecha_salida IS NULL";
            } else {
                // Insertar la fecha y hora de entrada
                query = "INSERT INTO fichar (EmpleadoID, Fecha_entrada, Hora_entrada) VALUES (?, ?, ?)";
            }

            try (PreparedStatement statement = conexionBBDD.prepareStatement(query)) {
                if (esFechaSalidaNull) {
                    statement.setDate(1, fecha);
                    statement.setTime(2, hora);
                    statement.setInt(3, obtenerEmpleadoId());
                } else {
                    statement.setInt(1, obtenerEmpleadoId());
                    statement.setDate(2, fecha);
                    statement.setTime(3, hora);
                }
                statement.executeUpdate();
            }        

        }
    }
}
