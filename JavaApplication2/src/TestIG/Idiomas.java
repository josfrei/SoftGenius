package TestIG;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author David L
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Idiomas {

    private Connection conexionBBDD;

    public Idiomas(Connection conexionBBDD) {
        this.conexionBBDD = conexionBBDD;
    }

    public String obtenerIdiomaActual() throws SQLException {
        String idiomaActual = "Spanish"; // Valor predeterminado

        PreparedStatement statement = conexionBBDD.prepareStatement("SELECT Idioma FROM configuracion");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            idiomaActual = resultSet.getString("Idioma");
        }

        return idiomaActual;
    }

}
