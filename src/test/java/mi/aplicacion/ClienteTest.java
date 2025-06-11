package mi.aplicacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach; // Importar si lo necesitas para @BeforeEach
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate; // Importar LocalDate

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testClienteConstructorCompleto() {
        LocalDate fecha = LocalDate.of(2023, 1, 15);
        Cliente c = new Cliente(1, "Ana Garcia", "ana.garcia@example.com", fecha);
        assertEquals(1, c.getId_cliente());
        assertEquals("Ana Garcia", c.getNombre());
        assertEquals("ana.garcia@example.com", c.getEmail());
        assertEquals(fecha, c.getFecha_registro());
    }

    @Test
    void testClienteConstructorSinId() {
        LocalDate fecha = LocalDate.of(2023, 2, 20);
        Cliente c = new Cliente("Luis Perez", "luis.perez@example.com", fecha);
        assertEquals(0, c.getId_cliente()); // Asumiendo que se inicializa a 0
        assertEquals("Luis Perez", c.getNombre());
        assertEquals("luis.perez@example.com", c.getEmail());
        assertEquals(fecha, c.getFecha_registro());
    }

    @Test
    void testSetNombreValido() {
        Cliente c = new Cliente();
        c.setNombre("Carlos Ruiz");
        assertEquals("Carlos Ruiz", c.getNombre());
    }

    @Test
    void testSetNombreVacioThrowsException() {
        Cliente c = new Cliente();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            c.setNombre("");
        });
        assertEquals("El nombre del cliente no puede estar vacío.", exception.getMessage());
    }

    @Test
    void testSetNombreNullThrowsException() {
        Cliente c = new Cliente();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            c.setNombre(null);
        });
        assertEquals("El nombre del cliente no puede estar vacío.", exception.getMessage());
    }

    @Test
    void testSetEmailValido() {
        Cliente c = new Cliente();
        c.setEmail("test@example.com");
        assertEquals("test@example.com", c.getEmail());
    }

    @Test
    void testSetEmailInvalidoThrowsException() {
        Cliente c = new Cliente();
        String emailInvalido = "invalido_sin_arroba_ni_punto";

        System.out.println("DEBUG: Intentando establecer email: \"" + emailInvalido + "\"");

        try {
            c.setEmail(emailInvalido);
            fail("Se esperaba IllegalArgumentException para el email: \"" + emailInvalido + "\", pero NO se lanzó ninguna excepción.");
        } catch (IllegalArgumentException e) {
            assertEquals("El email del cliente no es válido.", e.getMessage(), "El mensaje de la excepción no coincide.");
            System.out.println("DEBUG: Se lanzó correctamente IllegalArgumentException con mensaje: \"" + e.getMessage() + "\"");
        } catch (Exception e) {
            fail("Se lanzó una excepción inesperada para el email: \"" + emailInvalido + "\". Tipo: " + e.getClass().getSimpleName() + " - Mensaje: " + e.getMessage());
        }
    }
        @Test
    void testSetFechaRegistroValida() {
        Cliente c = new Cliente();
        LocalDate fecha = LocalDate.of(2022, 5, 10);
        c.setFecha_registro(fecha);
        assertEquals(fecha, c.getFecha_registro());
    }

    @Test
    void testSetFechaRegistroFuturaThrowsException() {
        Cliente c = new Cliente();
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            c.setFecha_registro(fechaFutura);
        });
        assertEquals("La fecha de registro no puede ser futura.", exception.getMessage());
    }

    @Test
    void testToStringContainsDetails() {
        LocalDate fecha = LocalDate.of(2023, 3, 1);
        Cliente c = new Cliente(2, "Laura Blanco", "laura.b@example.com", fecha);
        String expectedToString = "Cliente{id_cliente=2, nombre='Laura Blanco', email='laura.b@example.com', fecha_registro=" + fecha + "}";
        assertEquals(expectedToString, c.toString());
    }

    @Test
    void testEqualsById() {
        Cliente c1 = new Cliente(10, "A", "a@a.com", LocalDate.now());
        Cliente c2 = new Cliente(10, "B", "b@b.com", LocalDate.now());
        assertTrue(c1.equals(c2)); // Deberían ser iguales por ID
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsByEmailIfNoId() {
        Cliente c1 = new Cliente("A", "a@a.com", LocalDate.now());
        Cliente c2 = new Cliente("B", "a@a.com", LocalDate.now());
        assertTrue(c1.equals(c2)); // Deberían ser iguales por email si ID es 0
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testNotEquals() {
        Cliente c1 = new Cliente(1, "A", "a@a.com", LocalDate.now());
        Cliente c2 = new Cliente(2, "B", "b@b.com", LocalDate.now());
        assertFalse(c1.equals(c2));
    }
}