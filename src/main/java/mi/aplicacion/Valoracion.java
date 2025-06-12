package mi.aplicacion;

import java.time.LocalDate;
import java.util.Objects;

public class Valoracion {
    private int id_valoracion;
    private int puntuacion;
    private String comentario;
    private LocalDate fecha_valoracion;

    public Valoracion() {
    }

    // Constructor para valoraciones existentes con ID
    public Valoracion(int id_valoracion, int puntuacion, String comentario, LocalDate fecha_valoracion) {
        this.id_valoracion = id_valoracion;
        // Inicialización directa y validación usando métodos privados
        validarYSetearPuntuacion(puntuacion);
        validarYSetearComentario(comentario);
        validarYSetearFechaValoracion(fecha_valoracion);
    }

    // Constructor para nuevas valoraciones (ID se asignará automáticamente o en BBDD)
    public Valoracion(int puntuacion, String comentario, LocalDate fecha_valoracion) {
        this.id_valoracion = 0; // O -1, dependiendo de tu convención para nuevos registros
        // Inicialización directa y validación usando métodos privados
        validarYSetearPuntuacion(puntuacion);
        validarYSetearComentario(comentario);
        validarYSetearFechaValoracion(fecha_valoracion);
    }

    // Métodos privados para validación e inicialización en el constructor
    private void validarYSetearPuntuacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }
        this.puntuacion = puntuacion;
    }

    private void validarYSetearComentario(String comentario) {
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario de la valoración no puede estar vacío.");
        }
        this.comentario = comentario.trim();
    }

    private void validarYSetearFechaValoracion(LocalDate fecha_valoracion) {
        if (fecha_valoracion != null && fecha_valoracion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de valoración no puede ser futura.");
        }
        this.fecha_valoracion = fecha_valoracion;
    }

    // Getters
    public int getId_valoracion() {
        return id_valoracion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDate getFecha_valoracion() {
        return fecha_valoracion;
    }

    // Setters que usan los métodos de validación privados
    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public void setPuntuacion(int puntuacion) {
        validarYSetearPuntuacion(puntuacion);
    }

    public void setComentario(String comentario) {
        validarYSetearComentario(comentario);
    }

    public void setFecha_valoracion(LocalDate fecha_valoracion) {
        validarYSetearFechaValoracion(fecha_valoracion);
    }

    @Override
    public String toString() {
        return "Valoracion{" +
                "id_valoracion=" + id_valoracion +
                ", puntuacion=" + puntuacion +
                ", comentario='" + comentario + '\'' +
                ", fecha_valoracion=" + fecha_valoracion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Valoracion that = (Valoracion) o;
        // Si ambos IDs son válidos (no 0 o el valor por defecto para nuevo), compáralos
        // Si no, compara por un campo único como el comentario y la puntuación,
        // aunque para Valoracion es más común que el ID o la combinación producto/cliente/fecha
        // sea lo que define la unicidad si no hay ID. Para este ejemplo, solo el ID.
        return id_valoracion != 0 && id_valoracion == that.id_valoracion;
    }

    @Override
    public int hashCode() {
        // Si el ID es válido, usa el ID para el hash.
        // Si no, una combinación de campos que identifiquen la valoración.
        // Para este ejemplo, solo el ID.
        return Objects.hash(id_valoracion);
    }
}