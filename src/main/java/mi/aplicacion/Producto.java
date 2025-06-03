package mi.aplicacion;

public class Producto {
    private int id_producto;
    private String nombre_producto;
    private double precio;
    private String categoria;

    public Producto() {
    }


    public Producto(String nombre_producto, double precio, String categoria) {
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Producto(int id_producto, String nombre_producto, double precio, String categoria) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.categoria = categoria;
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
        this.nombre_producto = nombre_producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
}