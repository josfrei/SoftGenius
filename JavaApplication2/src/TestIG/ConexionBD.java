/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

/**
 *
 * @author josef
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import libsql.*;
import java.sql.Connection;

public class ConexionBD {
    private static final GestorBaseDatos gestorBD;

    static {
        gestorBD = new GestorBaseDatos();
    }

    public static Connection obtenerConexion(String nombreBD) throws SQLException {
        return gestorBD.conectarBaseDatos(nombreBD);
    }
}

    