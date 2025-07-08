package tablas;

import config.ClaseCambiarColorDeFondoInterfaz;
import config.ColorBotones;
import idioma.Idiomas;
import softgenius.ConexionBD;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static libsql3.softgenius.GestorTablas.comprobarRegistroDuplicado;

/**
 * Esta clase se encarga de la creación e inserción de empleados usando un
 * Trigger.
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class InsertarEmpleado extends JPanel implements ActionListener {

    JButton btnConfirmDni, btnCrearEmpleado, btnLimpiar;
    JLabel lblDNI, lblNombre, lblApellido, lblDepartamento, lblUsu, lblPass;
    JTextField txtDNI, txtNombre, txtApellido, txtUsu, txtPass;
    JCheckBox chkEspanhol;
    JComboBox<String> listaPaisesComboBox;
    Map<String, Integer> departamentoMap;
    JComboBox<String> listaDepartamento;
    private JPanel panelBase;
    private JPanel panel1, panel1a, panel1b, panel2;
    private JPanel panelNombre, panelApellidos, panelDepartamento, panelBtnCrearEmpleado, panelUsu, panelPass;
    private Connection conexionBBDD;
    private static char letraDNI;
    private String bbdd = "bbdd_empleados_softgenius";
    private String idiomaActual = "Spanish";
    private Color colorPanel = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondo();
    private Color colorSelect = new ClaseCambiarColorDeFondoInterfaz().CambiarColorDeFondoOscuro();

    /**
     * Constructor.
     */
    public InsertarEmpleado() throws SQLException {
        crearElementos();
        departamentoMap = new HashMap<>();
        rellenarComboDepartamentos();
        gestionCheckBox();
        String idiomaActual = obtenerIdiomaActual();
        actualizarIdioma(idiomaActual);
    }

    /*
    *
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    * Relleno del ComboBox y seleccionamos los ID de las selección correspondiente.
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *
     */
    /**
     *
     * Rellenamos el ComboBox con los departamentos que haya para que así el
     * usuario escoja. Se almacena todo en un HashMap para que visualmente el
     * usuario vea los diferentes departamentos escritos pero a la hora de
     * insertar se inserte el ID del departamento.
     */
    private void rellenarComboDepartamentos() {

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conexionBBDD = ConexionBD.obtenerConexion(bbdd);
            statement = conexionBBDD.createStatement();

            String sql = "SELECT DepartamentoID, Seccion FROM Departamento";
            resultSet = statement.executeQuery(sql);

            // Agregamos los resultados al JComboBox
            while (resultSet.next()) {
                int departamentoID = resultSet.getInt("DepartamentoID");
                String nombreDepartamento = resultSet.getString("Seccion");
                listaDepartamento.addItem(nombreDepartamento);
                departamentoMap.put(nombreDepartamento, departamentoID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexionBBDD != null) {
                try {
                    conexionBBDD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método para obtener el departamentoID asociado al nombre del departamento
     * seleccionado en el JComboBox.
     */
    private int obtenerDepartamentoIDSeleccionado() {
        String nombreDepartamentoSeleccionado = (String) listaDepartamento.getSelectedItem();
        return departamentoMap.get(nombreDepartamentoSeleccionado);
    }

    /**
     * Rellenamos el ComboBox de los países para que el usuario escoja.
     */
    private void rellenarComboPaises() {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");
            statement = conexionBBDD.createStatement();

            String sql = "SELECT Pais FROM paises";
            resultSet = statement.executeQuery(sql);

            // Agregar los resultados al JComboBox
            while (resultSet.next()) {
                String pais = resultSet.getString("Pais");
                // Agrega el nombre del país al JComboBox
                listaPaisesComboBox.addItem(pais);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexionBBDD != null) {
                try {
                    conexionBBDD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sentencia para buscar la abreviatura del país pasándole por parámetro el
     * país resultante de la elección del usuario.
     *
     * @param paisSeleccionado
     * @return
     */
    private String buscarAbreviacionPorPais(String paisSeleccionado) {
        Statement statement = null;
        ResultSet resultSet = null;
        String abreviacion = null;

        try {
            conexionBBDD = ConexionBD.obtenerConexion("bbdd_config_softgenius");
            statement = conexionBBDD.createStatement();

            String sql = "SELECT Abreviacion FROM paises WHERE Pais = '" + paisSeleccionado + "'";
            resultSet = statement.executeQuery(sql);

            // Obtener la abreviación del país
            if (resultSet.next()) {
                abreviacion = resultSet.getString("Abreviacion");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexionBBDD != null) {
                try {
                    conexionBBDD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return abreviacion;
    }


    /*
    *
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *                           Acciones de los botones.
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *
     */
    /**
     * Método para las acciones de los botones.
     *
     * btnLimpiar: Limpia todos los valores introducidos. btnConfirmDni:
     * Confirmar el DNI. Si es español confirma si está bien introducido, si no
     * es, sigue con el proceso. btnCrearEmpleado: una vez introducidos todos
     * los datos crea al empleado.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnLimpiar) {
            resetearInterfaz();
        }

        if (e.getSource() == btnConfirmDni) {
            String dni = txtDNI.getText();

            if (chkEspanhol.isSelected()) {
                comprobarSiExiste("ES_" + dni);
                confirmarDni();
            } else {
                if (txtDNI.getText().isEmpty()) {
                    ventanaError("Introduce un documento", "Campo vacío");
                } else {
                    txtDNI.setEditable(false);
                    chkEspanhol.setEnabled(false);
                    listaPaisesComboBox.setEnabled(false);
                    panel2.setVisible(true);
                    String paisSeleccionado = (String) listaPaisesComboBox.getSelectedItem();
                    buscarAbreviacionPorPais(paisSeleccionado);
                    String abrev = buscarAbreviacionPorPais(paisSeleccionado);
                    comprobarSiExiste(abrev + "_" + dni);
                }
            }
        }

        if (e.getSource() == btnCrearEmpleado) {

            try {
                sentenciaCrearUsuario();

            } catch (SQLException ex) {
                System.out.println("Error en la acción de crear usuario sin el check seleccionado: " + ex);
            }

        }
    }

    /**
     * Gestióna la llamada al desplegable de los países al desmarcar el check.
     */
    private void gestionCheckBox() {
        chkEspanhol.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (chkEspanhol.isSelected()) {
                    listaPaisesComboBox.setVisible(false);
                } else {
                    rellenarComboPaises();
                    listaPaisesComboBox.setVisible(true);

                }
            }
        });
    }

    /*
    *
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *                                   Métodos
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *
     */
    /**
     * Método para crear el usuario tomando los datos introducidos. Si el check
     * está marcado introduce la abreviatura de españa, sino el país elegido.
     *
     * @throws SQLException
     */
    private void sentenciaCrearUsuario() throws SQLException {
        // Conexión a la base de datos

        String nombre = formatFirstLetter(txtNombre.getText());
        String apellido = formatFirstLetter(txtApellido.getText());
        String dni = txtDNI.getText().toUpperCase();
        int departamentoID = obtenerDepartamentoIDSeleccionado();
        String departamento = String.valueOf(departamentoID);
        String usuario = txtUsu.getText();
        String pass = txtPass.getText();
        String pais = (String) listaPaisesComboBox.getSelectedItem();
        String abreviatura;

        if (chkEspanhol.isSelected()) {
            abreviatura = "ES_";
        } else {
            abreviatura = buscarAbreviacionPorPais(pais) + "_";
        }
        String dniAbrev = abreviatura + dni;

        try (Connection conexion = ConexionBD.obtenerConexion(bbdd)) {
            // Prepara la llamada a la rutina almacenada
            String sql = "{CALL Crear_Empleado_Y_Cuenta(?, ?, ?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(sql);

            // Establecer los parámetros de la rutina
            llamada.setString(1, nombre);
            llamada.setString(2, apellido);
            llamada.setString(3, dniAbrev);
            llamada.setInt(4, departamentoID);
            llamada.setString(5, usuario);
            llamada.setString(6, pass);
            // Ejecutar la rutina
            llamada.execute();

            JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Restablecer la interfaz
            resetearInterfaz();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + e.getMessage());
        }
    }

    /**
     * Método para poner la primera letra en mayúsculas y el resto en
     * minúsculas.
     *
     * @param inputText
     * @return
     */
    public static String formatFirstLetter(String inputText) {
        if (!inputText.isEmpty()) {
            String firstChar = inputText.substring(0, 1).toUpperCase();
            String restOfString = inputText.substring(1).toLowerCase();
            return firstChar + restOfString;
        } else {
            return "";
        }
    }

    /**
     * En este método comprobamos si el campo DNI está vacío o no. En caso de NO
     * estar vacío llama al método de validar el DNI, si es correcto muestra los
     * otros campos a introducir, sino lanza mensaje de error con la letra
     * correcta.
     *
     * Paso la letra introducida a mayúscula para la comprobación.
     */
    private void confirmarDni() {
        String dni = txtDNI.getText().toUpperCase();
        if (txtDNI.getText().isEmpty()) {
            ventanaError("Introduce el DNI", "Campo Vacío");
        } else {
            if (validarDNI(dni)) {
                panel2.setVisible(true);
                txtDNI.setEditable(false);
                chkEspanhol.setEnabled(false);

            } else {
                panel2.setVisible(false);
                ventanaError("El DNI no es válido, la letra correcta es la: " + letraDNI, "Error");
            }
        }
    }

    /**
     * Validamos si ya existe el dni/documento que vamos a insertar en la tabla.
     * Si el check está marcado valida como DNI y añade la extensión ES_, si no,
     * busca el país para añadirle la extensión correspondiente y comprobarlo.
     */
    private void comprobarSiExiste(String dni_abrev) {
        String registroABuscar = dni_abrev;
        String campoABuscar = "dni";
        String nombreTablaABuscar = "empleado";

        // Llamar al método registroDuplicado
        boolean existe = comprobarRegistroDuplicado(bbdd, nombreTablaABuscar, registroABuscar, campoABuscar);
        if (existe) {
            // Mostrar cuadro de diálogo para confirmar si desea insertar el DNI nuevamente
            int opcion = JOptionPane.showConfirmDialog(this, "El DNI ya existe. ¿Desea insertarlo de nuevo?", "DNI Existente", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                //No hacemos nada si pulsa "SÍ"
            } else {
                resetearInterfaz(); // Si no quiere insertarlo de nuevo, se resetea la interfaz
            }
        }
    }

    /**
     * Método para comprobar si la letra del DNI está correcta. Devuelve true si
     * está correcta, y false si no está correcta.
     *
     * @param dni
     * @return
     */
    public static boolean validarDNI(String dni) {

        // Verifica que el DNI tiene longitud válida
        if (dni == null || dni.length() != 9) {
            return false;
        }
        // Extrae el número y la letra del DNI
        String numero = dni.substring(0, 8);
        char letra = dni.charAt(8);

        // Convierte la letra a un número para calcular la letra del DNI
        int numeroDNI = Integer.parseInt(numero);

        letraDNI = calcularLetraDNI(numeroDNI);
        try {
            // Comparar la letra calculada con la letra del DNI
            return letra == letraDNI;
        } catch (NumberFormatException e) {
            // El número no es un entero válido
            return false;
        }
    }

    /**
     * Método para calcular la letra del DNI y devolver el resultado.
     *
     * @param dni
     * @return
     */
    public static char calcularLetraDNI(int dni) {
        // Array que contiene las letras asociadas a cada número de DNI
        char[] letrasDNI = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        // Calcula el índice de la letra en el array
        int indiceLetra = dni % 23;

        // Devuelve la letra correspondiente al índice
        return letrasDNI[indiceLetra];
    }

    /*
    *
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *                      Limpiar cajas de texto y ventana de error.
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *
     */
    /**
     * Método para resetar todos los valores introducidos a los valores
     * iniciales.
     */
    private void resetearInterfaz() {
        txtDNI.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
////////////////////////////////////
        panel1.removeAll();
        panel1a.removeAll();
        panel1b.removeAll();
        panel2.removeAll();
        panelNombre.removeAll();
        panelApellidos.removeAll();
        panelDepartamento.removeAll();
        panelUsu.removeAll();
        panelPass.removeAll();
        panelBtnCrearEmpleado.removeAll();
////////////////////////////////////
        anadirElementos();
////////////////////////////////////
        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);
        panelApellidos.add(lblApellido);
        panelApellidos.add(txtApellido);
        panelDepartamento.add(lblDepartamento);
        panelDepartamento.add(listaDepartamento);
        panelUsu.add(lblUsu);
        panelUsu.add(txtUsu);
        panelPass.add(lblPass);
        panelPass.add(txtPass);
        panelBtnCrearEmpleado.add(btnCrearEmpleado);
////////////////////////////////////
        panel1.revalidate();
        panel1.repaint();
        panel1a.revalidate();
        panel1a.repaint();
        panel1b.revalidate();
        panel1b.repaint();
        panel2.revalidate();
        panel2.repaint();
        panelNombre.revalidate();
        panelNombre.repaint();
        panelApellidos.revalidate();
        panelApellidos.repaint();
        panelDepartamento.revalidate();
        panelDepartamento.repaint();
        panelUsu.revalidate();
        panelUsu.repaint();
        panelPass.revalidate();
        panelPass.repaint();
        panelBtnCrearEmpleado.revalidate();
        panelBtnCrearEmpleado.repaint();
////////////////////////////////////
        txtDNI.setEditable(true);
        chkEspanhol.setEnabled(true);
        chkEspanhol.setSelected(true);
        listaPaisesComboBox.setEnabled(true);
////////////////////////////////////
        panel2.setVisible(false);
        listaPaisesComboBox.setVisible(false);
    }

    /**
     * Ventana flotante de mensaje de error. Pasamos por parámetro el mensaje
     * que queremos mostrar y el título de la ventana.
     *
     * @param mensaje
     * @param titulo
     */
    private static void ventanaError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    /*
    *
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *                      Creamos elementos y el Main.
    *
    * /////////////////////////////////////////////////////////////////////////////////
    *
    *
     */
    private void crearElementos() {

        // Crear el panel
        panelBase = new JPanel(new GridLayout(0, 1));
        panel1 = new JPanel();
        panel1 = new JPanel(new GridLayout(0, 1));
        panel1a = new JPanel(); // Panel para el DNI y botones confirmar y limpiar
        panel1b = new JPanel();// Panel para los países.
        panel2 = new JPanel();//Panel para el resto de datos

        panelBase.setBackground(colorPanel);
        panel1.setBackground(colorPanel);
        panel1a.setBackground(colorPanel);
        panel1b.setBackground(colorPanel);
        panel2.setBackground(colorPanel);

        panelNombre = new JPanel();
        panelApellidos = new JPanel();
        panelDepartamento = new JPanel();
        panelUsu = new JPanel();
        panelPass = new JPanel();
        panelBtnCrearEmpleado = new JPanel();

        panelNombre.setBackground(colorPanel);
        panelApellidos.setBackground(colorPanel);
        panelDepartamento.setBackground(colorPanel);
        panelPass.setBackground(colorPanel);
        panelUsu.setBackground(colorPanel);
        panelBtnCrearEmpleado.setBackground(colorPanel);

        // Creamos los elementos del panel1
        txtDNI = new JTextField(10);
        lblDNI = new JLabel("Documento identidad");
        chkEspanhol = new JCheckBox("DNI español");
        chkEspanhol.setBackground(colorPanel);
        chkEspanhol.setSelected(true);
        listaPaisesComboBox = new JComboBox<>();
        listaPaisesComboBox.setBackground(colorSelect);
        btnConfirmDni = new JButton("Validar documento");
        btnConfirmDni.addActionListener(this);
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(this);
        // Creamos los elementos del panel2
        lblNombre = new JLabel("Nombre");
        txtNombre = new JTextField(10);
        lblApellido = new JLabel("Apellido");
        txtApellido = new JTextField(10);
        listaDepartamento = new JComboBox<>();
        listaDepartamento.setBackground(colorSelect);
        lblDepartamento = new JLabel("Departamento");
        lblUsu = new JLabel("Usuario");
        txtUsu = new JTextField(10);
        lblPass = new JLabel("Contraseña");
        txtPass = new JTextField(10);
        btnCrearEmpleado = new JButton("Crear empleado");
        btnCrearEmpleado.addActionListener(this);
        ////////////////////////////////////
        //Damos color y formato a los botones
        ////////////////////////////////////

        ColorBotones colorBotones = new ColorBotones();
        Color colorAmarillo = new Color(241, 196, 15);
        Color colorVerde = new Color(39, 174, 96);
        Color colorAzul = new Color(33, 97, 140);
        Color miColorFondo = Color.white;
        JButton[] botonConfirmDni = {btnConfirmDni};
        JButton[] botonLimpiar = {btnLimpiar};
        JButton[] botonCrearEmpleado = {btnCrearEmpleado};
        colorBotones.aplicarEstilosBotones(botonConfirmDni, miColorFondo, colorAmarillo, colorAmarillo);
        colorBotones.aplicarEstilosBotones(botonLimpiar, miColorFondo, colorVerde, colorVerde);
        colorBotones.aplicarEstilosBotones(botonCrearEmpleado, miColorFondo, colorAzul, colorAzul);

        anadirElementos();

        this.setBackground(colorPanel);
        // Agrega el panel al marco
        add(panelBase);
        setVisible(true);
        panel2.setVisible(false);
        listaPaisesComboBox.setVisible(false);
    }

    /**
     * Método para añadir los elementos a la interfaz, se usará al inicio y al
     * limpiar la pantalla.
     */
    private void anadirElementos() {
        // Agregar panel1 y panel2 a panel
        panelBase.add(panel1);
        panelBase.add(panel2);

        // Agregamos los subpaneles a panel1
        panel1.add(panel1a);
        panel1.add(panel1b);

        //Agregamos los elementos de los paneles 1a y 1b
        panel1a.add(lblDNI);
        panel1a.add(txtDNI);
        panel1a.add(chkEspanhol);
        panel1a.add(btnConfirmDni);
        panel1a.add(btnLimpiar);
        panel1b.add(listaPaisesComboBox);

        //Agregamos los subpaneles del panel2
        panel2.add(panelNombre);
        panel2.add(panelApellidos);
        panel2.add(panelDepartamento);
        panel2.add(panelUsu);
        panel2.add(panelPass);
        panel2.add(panelBtnCrearEmpleado);

        // Agregamos los elementos de los subpaneles
        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);
        panelApellidos.add(lblApellido);
        panelApellidos.add(txtApellido);
        panelDepartamento.add(lblDepartamento);
        panelDepartamento.add(listaDepartamento);
        panelUsu.add(lblUsu);
        panelUsu.add(txtUsu);
        panelPass.add(lblPass);
        panelPass.add(txtPass);
        panelBtnCrearEmpleado.add(btnCrearEmpleado);
    }

    /**
     * ***************************************************************************
     * ///////////////////////////////////////////////////////////////////////////
     *
     * GESTIÓN IDIOMAS
     *
     * //////////////////////////////////////////////////////////////////////////
     * ************************************************************************
     */
    private void actualizarIdioma(String idioma) throws SQLException {
        idiomaActual = obtenerIdiomaActual();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idioma);

        // Actualizar los textos de los componentes con los valores del archivo de propiedades
        btnConfirmDni.setText(resourceBundle.getString("btnConfirmDni"));
        btnCrearEmpleado.setText(resourceBundle.getString("btnCrearEmpleado"));
        lblDNI.setText(resourceBundle.getString("lblDNI"));
        lblNombre.setText(resourceBundle.getString("lblNombre"));
        lblApellido.setText(resourceBundle.getString("lblApellido"));
        btnLimpiar.setText(resourceBundle.getString("btnLimpiarDatos"));
        lblUsu.setText(resourceBundle.getString("lblUsu"));
        lblPass.setText(resourceBundle.getString("lblPass"));
        lblDepartamento.setText(resourceBundle.getString("lblDepartamento"));
        chkEspanhol.setText(resourceBundle.getString("chkEspanhol"));
    }

    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }
}
