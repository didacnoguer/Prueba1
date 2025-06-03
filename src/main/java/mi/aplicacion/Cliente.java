package mi.aplicacion;

import java.sql.Date; // Asegúrate de importar java.sql.Date

public class Cliente {
    private int id_cliente;
    private String nombre;
    private String email;
    private Date fecha_registro;
    public Cliente() {
    }

    public Cliente(String nombre, String email, Date fecha_registro) {
        this.nombre = nombre;
        this.email = email;
        this.fecha_registro = fecha_registro;
    }

    public Cliente(int id_cliente, String nombre, String email, Date fecha_registro) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.email = email;
        this.fecha_registro = fecha_registro;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id_cliente=" + id_cliente +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fecha_registro=" + fecha_registro +
                '}';
    }
}