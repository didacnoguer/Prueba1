package mi.aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ValoracionTest {

    @Test
    void testValoracionConstructorCompletoAndGetters() {
        LocalDate fecha = LocalDate.of(2024, 1, 20);
        Valoracion valoracion = new Valoracion(1, 4, "Buen producto en general.", fecha);

        assertEquals(1, valoracion.getId_valoracion());
        assertEquals(4, valoracion.getPuntuacion());
        assertEquals("Buen producto en general.", valoracion.getComentario());
        assertEquals(fecha, valoracion.getFecha_valoracion());
    }

    @Test
    void testValoracionConstructorSinIdAndGetters() {
        LocalDate fecha = LocalDate.of(2024, 2, 25);
        Valoracion valoracion = new Valoracion(5, "Excelente, me encantó!", fecha);

        assertEquals(0, valoracion.getId_valoracion());
        assertEquals(5, valoracion.getPuntuacion());
        assertEquals("Excelente, me encantó!", valoracion.getComentario());
        assertEquals(fecha, valoracion.getFecha_valoracion());
    }

    @Test
    void testSetPuntuacionValida() {
        Valoracion valoracion = new Valoracion();
        valoracion.setPuntuacion(3);
        assertEquals(3, valoracion.getPuntuacion());
    }

    @Test
    void testSetPuntuacionInvalidaThrowsException() {
        Valoracion valoracion = new Valoracion();
        assertThrows(IllegalArgumentException.class, () -> valoracion.setPuntuacion(0));
        assertThrows(IllegalArgumentException.class, () -> valoracion.setPuntuacion(6));
    }

    @Test
    void testSetComentarioValido() {
        Valoracion valoracion = new Valoracion();
        valoracion.setComentario("Nuevo comentario.");
        assertEquals("Nuevo comentario.", valoracion.getComentario());
    }

    @Test
    void testSetComentarioVacioThrowsException() {
        Valoracion valoracion = new Valoracion();
        assertThrows(IllegalArgumentException.class, () -> valoracion.setComentario(""));
    }

    @Test
    void testSetComentarioNullThrowsException() {
        Valoracion valoracion = new Valoracion();
        assertThrows(IllegalArgumentException.class, () -> valoracion.setComentario(null));
    }

    @Test
    void testSetFechaValoracionValida() {
        Valoracion valoracion = new Valoracion();
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        valoracion.setFecha_valoracion(fechaPasada);
        assertEquals(fechaPasada, valoracion.getFecha_valoracion());
    }

    @Test
    void testSetFechaValoracionFuturaThrowsException() {
        Valoracion valoracion = new Valoracion();
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            valoracion.setFecha_valoracion(fechaFutura);
        });
        assertEquals("La fecha de valoración no puede ser futura.", exception.getMessage());
    }

    @Test
    void testConstructorConPuntuacionInvalidaThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Valoracion(0, "Comentario", LocalDate.of(2024, 1, 1));
        });
        assertEquals("La puntuación debe estar entre 1 y 5.", exception.getMessage());
    }

    @Test
    void testConstructorConComentarioVacioThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Valoracion(3, " ", LocalDate.of(2024, 1, 1));
        });
        assertEquals("El comentario de la valoración no puede estar vacío.", exception.getMessage());
    }

    @Test
    void testConstructorConFechaFuturaThrowsException() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Valoracion(3, "Comentario", fechaFutura);
        });
        assertEquals("La fecha de valoración no puede ser futura.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate fecha1 = LocalDate.of(2024, 1, 1);
        LocalDate fecha2 = LocalDate.of(2024, 1, 1);

        Valoracion v1 = new Valoracion(1, 3, "Comentario 1", fecha1);
        Valoracion v2 = new Valoracion(1, 3, "Comentario 1", fecha2);
        Valoracion v3 = new Valoracion(2, 4, "Comentario 2", fecha1);

        assertTrue(v1.equals(v2));
        assertEquals(v1.hashCode(), v2.hashCode());
        assertFalse(v1.equals(v3));
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }
}