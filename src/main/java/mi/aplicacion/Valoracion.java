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

    public Valoracion(int id_valoracion, int puntuacion, String comentario, LocalDate fecha_valoracion) {
        this.id_valoracion = id_valoracion;
        setPuntuacion(puntuacion);
        setComentario(comentario);
        setFecha_valoracion(fecha_valoracion);
    }

    public Valoracion(int puntuacion, String comentario, LocalDate fecha_valoracion) {
        this.id_valoracion = 0;
        setPuntuacion(puntuacion);
        setComentario(comentario);
        setFecha_valoracion(fecha_valoracion);
    }

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

    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public void setPuntuacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }
        this.puntuacion = puntuacion;
    }

    public void setComentario(String comentario) {
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario de la valoración no puede estar vacío.");
        }
        this.comentario = comentario.trim();
    }

    public void setFecha_valoracion(LocalDate fecha_valoracion) {
        if (fecha_valoracion != null && fecha_valoracion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de valoración no puede ser futura.");
        }
        this.fecha_valoracion = fecha_valoracion;
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
        return id_valoracion != 0 && id_valoracion == that.id_valoracion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_valoracion);
    }
}