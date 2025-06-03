package mi.aplicacion;

import java.sql.Date;
public class Valoracion {
    private int id_valoracion;
    private int puntuacion;
    private String comentario;
    private Date fecha_valoracion;
    public Valoracion() {
    }

    public Valoracion(int puntuacion, String comentario, Date fecha_valoracion) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha_valoracion = fecha_valoracion;
    }

    public Valoracion(int id_valoracion, int puntuacion, String comentario, Date fecha_valoracion) {
        this.id_valoracion = id_valoracion;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha_valoracion = fecha_valoracion;
    }

    public int getId_valoracion() {
        return id_valoracion;
    }

    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) { // Corregido 'void' a 'public' en una revisión anterior
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha_valoracion() {
        return fecha_valoracion;
    }

    public void setFecha_valoracion(Date fecha_valoracion) {
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
}