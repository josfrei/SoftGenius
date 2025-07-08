package config;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import softgenius.ConexionBD;

/**
 * Esta clase se crea para poder cambiar la gama de colores del programa.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class ClaseCambiarColorDeFondoInterfaz {

    /**
     * Constructor de la clase
     */
    public ClaseCambiarColorDeFondoInterfaz() {
        initComponents();
    }

    private void initComponents() {

    }

    public Color CambiarColorDeFondoOscuro() {
        //Inicializamos la variable de tipo String colorNombre
        String colorNombre = "";
        //Inicializamos la variable objeto de tipo color a null
        Color color = null;

        //Declaramos la sentencia sql
        String sql = "SELECT color FROM configuracion where color = 'Amarillo' or color = 'Rosa' or color = 'Violeta' or color = 'Predeterminado'";

        //Realizamos la conexion
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius")) {
            //Preparamos la consulta
            try (PreparedStatement consultaColorActual = conexionBBDD.prepareStatement(sql)) {
                //La ejecutamos
                try (ResultSet resultado = consultaColorActual.executeQuery()) {
                    if (resultado.next()) {

                        //Le asignamos lo que obtenemos de la consulta a la variable de tipo String colorNombre
                        colorNombre = resultado.getString("COLOR");
                    }

                    //Capturamos las excepciones
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //Segun el valor de la varible colorNombre elegimos un color u otro

        switch (colorNombre) {
            //Amarillo en rgb
            case "Amarillo":
                color = new Color(249, 231, 159);
                break;
            //Rosa en rgb    
            case "Rosa":
                color = new Color(250, 199, 229);
                break;
            //Violeta en rgb
            case "Violeta":
                color = new Color(196, 166, 203);
                break;
            //Predeterminado en rgb
            case "Predeterminado":
                color = new Color(200, 200, 200);
                break;
            default:
                break;
        }

        //Devolvemos el color obtenido
        return color;
    }

    public Color CambiarColorDeFondo() {
        //Inicializamos la variable de tipo String colorNombre
        String colorNombre = "";
        //Inicializamos la variable objeto de tipo color a null
        Color color = null;

        //Declaramos la sentencia sql
        String sql = "SELECT color FROM configuracion where color = 'Amarillo' or color = 'Rosa' or color = 'Violeta' or color = 'Predeterminado'";

        //Realizamos la conexion
        try (Connection conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius")) {
            //Preparamos la consulta
            try (PreparedStatement consultaColorActual = conexionBBDD.prepareStatement(sql)) {
                //La ejecutamos
                try (ResultSet resultado = consultaColorActual.executeQuery()) {
                    if (resultado.next()) {

                        //Le asignamos lo que obtenemos de la consulta a la variable de tipo String colorNombre
                        colorNombre = resultado.getString("COLOR");
                    }

                    //Capturamos las excepciones
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //Segun el valor de la varible colorNombre elegimos un color u otro

        switch (colorNombre) {
            //Amarillo en rgb
            case "Amarillo":
                color = new Color(255, 245, 206);
                break;
            //Rosa en rgb    
            case "Rosa":
                color = new Color(255, 228, 244);
                break;
            //Violeta en rgb
            case "Violeta":
                color = new Color(248, 220, 255);
                break;
            //Predeterminado en rgb
            case "Predeterminado":
                color = new Color(240, 240, 240);
                break;
            default:
                break;
        }

        //Devolvemos el color obtenido
        return color;
    }
}
