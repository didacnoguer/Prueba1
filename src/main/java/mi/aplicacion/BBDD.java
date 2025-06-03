    package mi.aplicacion;

    import java.io.IOException;
    import java.io.InputStream;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.ResultSet;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Properties;

    public class BBDD {
        private Connection con = null;
        private String url = null;
        private String user = null;
        private String pass = null;

        public boolean conectar() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Properties p = loadPropertiesFile();
                if (p == null) return false;

                url = p.getProperty("db.string_connection");
                user = p.getProperty("db.user");
                pass = p.getProperty("db.password");

                con = DriverManager.getConnection(url, user, pass);
                return true;

            } catch (SQLException e) {
                showError(e);
                unLoad();
                return false;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se encontró el driver JDBC de MySQL.");
                e.printStackTrace();
                return false;
            }
        }

        private Properties loadPropertiesFile() {
            Properties p = new Properties();
            try {
                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties");

                if (resourceAsStream != null) {
                    p.load(resourceAsStream);
                } else {
                    System.err.println("Error: Archivo config.properties no encontrado en el classpath.");
                    return null;
                }
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo de propiedades: " + e.getMessage());
                return null;
            }
            return p;
        }

        private void showError(SQLException e) {
            System.err.println("Mensaje de error: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
        }

        public void unLoad() {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        public boolean persist(Producto producto) {

            String sql = "INSERT INTO Productos (nombre_producto, precio, categoria) VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Establecer los valores de los parámetros en la sentencia SQL
                ps.setString(1, producto.getNombre_producto());
                ps.setDouble(2, producto.getPrecio());
                ps.setString(3, producto.getCategoria());

                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            producto.setId_producto(rs.getInt(1));
                        }
                    }
                    System.out.println("DEBUG: Producto '" + producto.getNombre_producto() + "' persistido con éxito. ID: " + producto.getId_producto());
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Error al persistir Producto:");
                showError(e);
            }
            return false;
        }

        public boolean merge(Producto producto) {
            System.out.println("DEBUG: Fusionar Producto - Lógica no implementada.");
            return false;
        }

        public boolean remove(Producto producto) {
            System.out.println("DEBUG: Eliminar Producto - Lógica no implementada.");
            return false;
        }

        public List<Producto> find(Producto producto) {
            System.out.println("DEBUG: Buscar Producto - Lógica no implementada.");
            return null;
        }

        public boolean persist(Cliente cliente) {
            System.out.println("DEBUG: Persistir Cliente - Lógica no implementada.");
            return false;
        }

        public boolean merge(Cliente cliente) {
            System.out.println("DEBUG: Fusionar Cliente - Lógica no implementada.");
            return false;
        }

        public boolean remove(Cliente cliente) {
            System.out.println("DEBUG: Eliminar Cliente - Lógica no implementada.");
            return false;
        }

        public List<Cliente> find(Cliente cliente) {
            System.out.println("DEBUG: Buscar Cliente - Lógica no implementada.");
            return null;
        }
        public boolean persist(Valoracion valoracion) {
            System.out.println("DEBUG: Persistir Valoracion - Lógica no implementada.");
            return false;
        }

        public boolean merge(Valoracion valoracion) {
            System.out.println("DEBUG: Fusionar Valoracion - Lógica no implementada.");
            return false;
        }

        public boolean remove(Valoracion valoracion) {
            System.out.println("DEBUG: Eliminar Valoracion - Lógica no implementada.");
            return false;
        }

        public List<Valoracion> find(Valoracion valoracion) {
            System.out.println("DEBUG: Buscar Valoracion - Lógica no implementada.");
            return null;
        }
    }