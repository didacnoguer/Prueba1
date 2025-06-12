package mi.aplicacion;

import java.util.Objects;

public class Producto {
    private int id_producto;
    private String nombre_producto;
    private double precio;
    private String categoria;

    public Producto() {
    }

    // Constructor para nuevos productos (ID se asignará automáticamente o en BBDD)
    public Producto(String nombre_producto, double precio, String categoria) {
        this.id_producto = 0; // O -1, dependiendo de tu convención para nuevos registros
        // Inicialización directa y validación usando métodos privados
        validarYSetearNombreProducto(nombre_producto);
        validarYSetearPrecio(precio);
        validarYSetearCategoria(categoria);
    }

    // Constructor para productos existentes con ID
    public Producto(int id_producto, String nombre_producto, double precio, String categoria) {
        this.id_producto = id_producto;
        // Inicialización directa y validación usando métodos privados
        validarYSetearNombreProducto(nombre_producto);
        validarYSetearPrecio(precio);
        validarYSetearCategoria(categoria);
    }

    // Métodos privados para validación
    private void validarYSetearNombreProducto(String nombre_producto) {
        if (nombre_producto == null || nombre_producto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.nombre_producto = nombre_producto.trim();
    }

    private void validarYSetearPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor positivo.");
        }
        this.precio = precio;
    }

    private void validarYSetearCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría del producto no puede estar vacía.");
        }
        this.categoria = categoria.trim();
    }

    // Getters
    public int getId_producto() { return id_producto; }
    public String getNombre_producto() { return nombre_producto; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }

    // Setters que usan los métodos de validación privados
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }
    public void setNombre_producto(String nombre_producto) { validarYSetearNombreProducto(nombre_producto); }
    public void setPrecio(double precio) { validarYSetearPrecio(precio); }
    public void setCategoria(String categoria) { validarYSetearCategoria(categoria); }

    @Override
    public String toString() {
        return "Producto{" +
                "id_producto=" + id_producto +
                ", nombre_producto='" + nombre_producto + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        // Para equals, si ambos IDs son válidos (no 0), compáralos por ID.
        // Si uno o ambos IDs son 0, podrías comparar por nombre y categoría
        // para identificar "productos conceptualmente iguales". Aquí solo se usa ID.
        return id_producto != 0 && id_producto == producto.id_producto;
    }

    @Override
    public int hashCode() {
        // Si el ID es válido, usa el ID para el hash.
        // Si no, una combinación de campos que identifiquen el producto.
        // Para este ejemplo, solo el ID.
        return Objects.hash(id_producto);
    }
}