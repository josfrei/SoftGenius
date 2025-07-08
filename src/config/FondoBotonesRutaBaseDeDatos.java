package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import softgenius.ConexionBD;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class FondoBotonesRutaBaseDeDatos {
    
    public FondoBotonesRutaBaseDeDatos(){
        
    }
    
    public String cambiarRecursosPorNombreDeCarpeta() throws SQLException{
        //Inicializamos la variable de tipo string nombre cadena a vacia
        String nombreCarpeta = "";
        //Declareamos ala sentencia sql
        String sql = "SELECT color FROM configuracion where color = 'Amarillo' or color = 'Rosa' or color = 'Violeta' or color = 'Predeterminado'";
        //Creamos la conexión
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");){
            //Preparamos la consulta
            PreparedStatement consultaColorActual = conexionBBDD.prepareStatement(sql);
            //La ejecutamos
            try (ResultSet resultado = consultaColorActual.executeQuery()){
                if(resultado.next()){
                    //Obtenemosel valor del campo seleccionado
                    nombreCarpeta = resultado.getString("color");
                }
                //Capturamos las excepciones
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return nombreCarpeta;
    }
}
