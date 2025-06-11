package mi.aplicacion;

import java.util.Objects;

public class Producto {
    private int id_producto;
    private String nombre_producto;
    private double precio;
    private String categoria;

    public Producto() {
    }

    public Producto(String nombre_producto, double precio, String categoria) {
        setNombre_producto(nombre_producto);
        setPrecio(precio);
        setCategoria(categoria);
    }

    public Producto(int id_producto, String nombre_producto, double precio, String categoria) {
        this.id_producto = id_producto;
        setNombre_producto(nombre_producto);
        setPrecio(precio);
        setCategoria(categoria);
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        if (nombre_producto == null || nombre_producto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.nombre_producto = nombre_producto.trim();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor positivo.");
        }
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría del producto no puede estar vacía.");
        }
        this.categoria = categoria.trim();
    }

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
        return id_producto != 0 && id_producto == producto.id_producto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto);
    }
}