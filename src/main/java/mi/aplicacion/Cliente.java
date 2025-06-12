package mi.aplicacion;

import java.time.LocalDate;
import java.util.Objects;

public class Cliente {
    private int id_cliente;
    private String nombre;
    private String email;
    private LocalDate fecha_registro;

    public Cliente() {
    }

    // Constructor para clientes existentes con ID
    public Cliente(int id_cliente, String nombre, String email, LocalDate fecha_registro) {
        this.id_cliente = id_cliente;
        // Inicialización directa y validación usando métodos privados
        validarYSetearNombre(nombre);
        validarYSetearEmail(email);
        validarYSetearFechaRegistro(fecha_registro);
    }

    // Constructor para nuevos clientes (ID se asignará automáticamente o en BBDD)
    public Cliente(String nombre, String email, LocalDate fecha_registro) {
        this.id_cliente = 0; // O -1, dependiendo de tu convención para nuevos registros
        // Inicialización directa y validación usando métodos privados
        validarYSetearNombre(nombre);
        validarYSetearEmail(email);
        validarYSetearFechaRegistro(fecha_registro);
    }

    // Métodos privados para validación e inicialización en el constructor
    private void validarYSetearNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    private void validarYSetearEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email del cliente no es válido.");
        }
        this.email = email.trim();
    }

    private void validarYSetearFechaRegistro(LocalDate fecha_registro) {
        if (fecha_registro != null && fecha_registro.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de registro no puede ser futura.");
        }
        this.fecha_registro = fecha_registro;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setNombre(String nombre) {
        validarYSetearNombre(nombre);
    }

    public void setEmail(String email) {
        validarYSetearEmail(email);
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        validarYSetearFechaRegistro(fecha_registro);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;

        if (id_cliente != 0 && cliente.id_cliente != 0) {
            return id_cliente == cliente.id_cliente;
        } else {
            return Objects.equals(email, cliente.email);
        }
    }

    @Override
    public int hashCode() {

        if (id_cliente != 0) {
            return Objects.hash(id_cliente);
        } else {
            return Objects.hash(email);
        }
    }
}