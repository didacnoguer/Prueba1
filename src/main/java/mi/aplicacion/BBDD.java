package mi.aplicacion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BBDD {
    private Connection con = null;
    private String url = null;
    private String user = null;
    private String pass = null;

    /**
     * Establece la conexión con la base de datos MySQL.
     * Carga las propiedades de conexión desde 'database.properties'.
     *
     * @return true si la conexión se establece con éxito, false en caso contrario.
     */
    public boolean conectar() {
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Cargar las propiedades de conexión
            Properties p = loadPropertiesFile();
            if (p == null) return false;

            url = p.getProperty("db.string_connection");
            user = p.getProperty("db.user");
            pass = p.getProperty("db.password");

            // Establecer la conexión
            con = DriverManager.getConnection(url, user, pass);
            return true;

        } catch (SQLException e) {
            showError(e);
            unLoad(); // Limpiar recursos si hay un error de SQL
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver JDBC de MySQL. Asegúrate de que el conector MySQL está en tus dependencias de Maven (pom.xml).");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga las propiedades de conexión desde el archivo 'database.properties'.
     * Se espera que este archivo esté en 'src/main/resources'.
     *
     * @return Objeto Properties con las configuraciones, o null si no se puede cargar el archivo.
     */
    private Properties loadPropertiesFile() {
        Properties p = new Properties();
        // Usar getClass().getClassLoader().getResourceAsStream para buscar el archivo en el classpath
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (in == null) {
                System.err.println("Error: No se pudo encontrar database.properties en src/main/resources.");
                System.err.println("Asegúrate de que el archivo existe y que Maven lo está copiando a target/classes.");
                return null;
            }
            p.load(in);
        } catch (IOException e) {
            System.err.println("Error al cargar database.properties: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return p;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public void desconectar() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                showError(e);
            }
        }
        unLoad(); // Limpiar variables de conexión
    }

    /**
     * Muestra información detallada sobre una excepción SQLException.
     *
     * @param e La excepción SQLException a mostrar.
     */
    private void showError(SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Vendor Error Code: " + e.getErrorCode());
        e.printStackTrace();
    }

    /**
     * Limpia las variables de conexión.
     */
    private void unLoad() {
        con = null;
        url = null;
        user = null;
        pass = null;
    }

    // --- Métodos CRUD genéricos (pueden ser sobrecargados para cada entidad) ---

    /**
     * Persiste (inserta) un objeto en la base de datos.
     * Soporta Cliente, Producto y Valoracion.
     *
     * @param obj El objeto a persistir.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean persist(Object obj) {
        if (obj instanceof Cliente) {
            return persistCliente((Cliente) obj);
        } else if (obj instanceof Producto) {
            return persistProducto((Producto) obj);
        } else if (obj instanceof Valoracion) {
            return persistValoracion((Valoracion) obj);
        }
        System.err.println("Tipo de objeto no soportado para persistir: " + obj.getClass().getName());
        return false;
    }

    /**
     * Actualiza (merge) un objeto en la base de datos.
     * Soporta Cliente, Producto y Valoracion.
     *
     * @param obj El objeto a actualizar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean merge(Object obj) {
        if (obj instanceof Cliente) {
            return mergeCliente((Cliente) obj);
        } else if (obj instanceof Producto) {
            return mergeProducto((Producto) obj);
        } else if (obj instanceof Valoracion) {
            return mergeValoracion((Valoracion) obj);
        }
        System.err.println("Tipo de objeto no soportado para merge: " + obj.getClass().getName());
        return false;
    }

    // --- Métodos CRUD específicos para CLIENTE ---

    /**
     * Inserta un nuevo cliente en la base de datos.
     * Asigna el ID generado por la base de datos al objeto Cliente.
     *
     * @param cliente El objeto Cliente a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean persistCliente(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nombre, email, fecha_registro) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setDate(3, java.sql.Date.valueOf(cliente.getFecha_registro()));
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId_cliente(rs.getInt(1)); // Asignar el ID generado
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Actualiza un cliente existente en la base de datos.
     *
     * @param cliente El objeto Cliente con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean mergeCliente(Cliente cliente) {
        String sql = "UPDATE Clientes SET nombre = ?, email = ?, fecha_registro = ? WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setDate(3, java.sql.Date.valueOf(cliente.getFecha_registro()));
            ps.setInt(4, cliente.getId_cliente());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     *
     * @param id El ID del cliente a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean removeCliente(int id) {
        String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Busca un cliente en la base de datos por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return El objeto Cliente si se encuentra, o null si no existe.
     */
    public Cliente findClienteById(int id) {
        String sql = "SELECT id_cliente, nombre, email, fecha_registro FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getDate("fecha_registro").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            showError(e);
        }
        return null;
    }

    /**
     * Busca un cliente en la base de datos por su dirección de email.
     * Utilizado para validar si ya existe un email.
     *
     * @param email La dirección de email a buscar.
     * @return El objeto Cliente si se encuentra, o null si no existe.
     */
    public Cliente findClienteByEmail(String email) {
        String sql = "SELECT id_cliente, nombre, email, fecha_registro FROM Clientes WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getDate("fecha_registro").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            showError(e);
        }
        return null;
    }

    /**
     * Obtiene una lista de todos los clientes en la base de datos.
     *
     * @return Una lista de objetos Cliente. Puede estar vacía si no hay clientes.
     */
    public List<Cliente> findAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, email, fecha_registro FROM Clientes";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getDate("fecha_registro").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            showError(e);
        }
        return clientes;
    }

    /**
     * Elimina todos los registros de la tabla `Clientes`.
     * Util para la configuración y limpieza de tests.
     *
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean clearClientesTable() {
        String sql = "DELETE FROM Clientes";
        try (Statement stmt = con.createStatement()) {
            int affectedRows = stmt.executeUpdate(sql);
            System.out.println("DEBUG: Se eliminaron " + affectedRows + " clientes de la tabla.");
            // Resetear AUTO_INCREMENT si es necesario en MySQL
            // ALTER TABLE Clientes AUTO_INCREMENT = 1;
            // No lo haremos aquí para no acoplarlo, pero es una consideración para el testing
            return true;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    // --- Métodos CRUD específicos para PRODUCTO ---

    /**
     * Inserta un nuevo producto en la base de datos.
     * Asigna el ID generado por la base de datos al objeto Producto.
     *
     * @param producto El objeto Producto a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean persistProducto(Producto producto) {
        String sql = "INSERT INTO Productos (nombre_producto, precio, categoria) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre_producto());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getCategoria());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId_producto(rs.getInt(1)); // Asignar el ID generado
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param producto El objeto Producto con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean mergeProducto(Producto producto) {
        String sql = "UPDATE Productos SET nombre_producto = ?, precio = ?, categoria = ? WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre_producto());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getCategoria());
            ps.setInt(4, producto.getId_producto());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param id El ID del producto a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean removeProducto(int id) {
        String sql = "DELETE FROM Productos WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Busca un producto en la base de datos por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El objeto Producto si se encuentra, o null si no existe.
     */
    public Producto findProductoById(int id) {
        String sql = "SELECT id_producto, nombre_producto, precio, categoria FROM Productos WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre_producto"),
                            rs.getDouble("precio"),
                            rs.getString("categoria")
                    );
                }
            }
        } catch (SQLException e) {
            showError(e);
        }
        return null;
    }

    /**
     * Obtiene una lista de todos los productos en la base de datos.
     *
     * @return Una lista de objetos Producto. Puede estar vacía si no hay productos.
     */
    public List<Producto> findAllProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, precio, categoria FROM Productos";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getDouble("precio"),
                        rs.getString("categoria")
                ));
            }
        } catch (SQLException e) {
            showError(e);
        }
        return productos;
    }

    // --- Métodos CRUD específicos para VALORACION ---

    /**
     * Inserta una nueva valoración en la base de datos.
     * Asigna el ID generado por la base de datos al objeto Valoracion.
     *
     * @param valoracion El objeto Valoracion a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean persistValoracion(Valoracion valoracion) {
        String sql = "INSERT INTO Valoraciones (puntuacion, comentario, fecha_valoracion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, valoracion.getPuntuacion());
            ps.setString(2, valoracion.getComentario());
            ps.setDate(3, java.sql.Date.valueOf(valoracion.getFecha_valoracion()));
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        valoracion.setId_valoracion(rs.getInt(1)); // Asignar el ID generado
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Actualiza una valoración existente en la base de datos.
     *
     * @param valoracion El objeto Valoracion con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean mergeValoracion(Valoracion valoracion) {
        String sql = "UPDATE Valoraciones SET puntuacion = ?, comentario = ?, fecha_valoracion = ? WHERE id_valoracion = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valoracion.getPuntuacion());
            ps.setString(2, valoracion.getComentario());
            ps.setDate(3, java.sql.Date.valueOf(valoracion.getFecha_valoracion()));
            ps.setInt(4, valoracion.getId_valoracion());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Elimina una valoración de la base de datos por su ID.
     *
     * @param id El ID de la valoración a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean removeValoracion(int id) {
        String sql = "DELETE FROM Valoraciones WHERE id_valoracion = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showError(e);
        }
        return false;
    }

    /**
     * Busca una valoración en la base de datos por su ID.
     *
     * @param id El ID de la valoración a buscar.
     * @return El objeto Valoracion si se encuentra, o null si no existe.
     */
    public Valoracion findValoracionById(int id) {
        String sql = "SELECT id_valoracion, puntuacion, comentario, fecha_valoracion FROM Valoraciones WHERE id_valoracion = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Valoracion(
                            rs.getInt("id_valoracion"),
                            rs.getInt("puntuacion"),
                            rs.getString("comentario"),
                            rs.getDate("fecha_valoracion").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            showError(e);
        }
        return null;
    }

    /**
     * Obtiene una lista de todas las valoraciones en la base de datos.
     *
     * @return Una lista de objetos Valoracion. Puede estar vacía si no hay valoraciones.
     */
    public List<Valoracion> findAllValoraciones() {
        List<Valoracion> valoraciones = new ArrayList<>();
        String sql = "SELECT id_valoracion, puntuacion, comentario, fecha_valoracion FROM Valoraciones";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                valoraciones.add(new Valoracion(
                        rs.getInt("id_valoracion"),
                        rs.getInt("puntuacion"),
                        rs.getString("comentario"),
                        rs.getDate("fecha_valoracion").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            showError(e);
        }
        return valoraciones;
    }
}