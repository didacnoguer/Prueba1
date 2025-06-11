package mi.aplicacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteGestorTest {

    private ClienteGestor gestor;
    private BBDD bbdd;

    @BeforeEach
    void setUp() {
        bbdd = new BBDD();

        assertTrue(bbdd.conectar(), "La conexión a la BBDD ha funcionado para las pruebas.");
        assertTrue(bbdd.clearClientesTable(), "La tabla de clientes ha de limpiarse antes de cada test.");
        gestor = new ClienteGestor(bbdd);
    }

    @AfterEach
    void tearDown() {
        if (bbdd != null) {
            bbdd.clearClientesTable();
            bbdd.desconectar();
        }
    }

    @Test
    void addClienteAddsClientSuccessfully() {
        Cliente cliente = new Cliente("Test Nombre", "test@example.com", LocalDate.now());
        assertTrue(gestor.addCliente(cliente), " añadir el cliente exitosamente.");
        assertNotEquals(0, cliente.getId_cliente(), "El ID del cliente ha ser asignado.");
        Cliente foundCliente = bbdd.findClienteById(cliente.getId_cliente());
        assertNotNull(foundCliente, "El cliente se ha   encontrado en la BBDD.");
        assertEquals("Test Nombre", foundCliente.getNombre());
        assertEquals("test@example.com", foundCliente.getEmail());
    }

    @Test
    void addClienteRejectsDuplicateEmail() {
        Cliente cliente1 = new Cliente("Test Nombre 1", "duplicate@example.com", LocalDate.now());
        gestor.addCliente(cliente1);

        Cliente cliente2 = new Cliente("Test Nombre 2", "duplicate@example.com", LocalDate.now());
        assertFalse(gestor.addCliente(cliente2), "No debería añadir cliente con email duplicado.");
    }

    @Test
    void getClienteByIdReturnsCorrectClient() {
        Cliente cliente = new Cliente("Buscar Nombre", "buscar@example.com", LocalDate.now());
        gestor.addCliente(cliente);

        Cliente found = gestor.getClienteById(cliente.getId_cliente());
        assertNotNull(found, "Debería encontrar el cliente por ID.");
        assertEquals("Buscar Nombre", found.getNombre());
    }

    @Test
    void getClienteByIdReturnsNullForNonExistentClient() {
        assertNull(gestor.getClienteById(9999), "Debería retornar null para un ID no existente.");
    }

    @Test
    void getAllClientesReturnsAllClients() {
        gestor.addCliente(new Cliente("Cliente A", "a@example.com", LocalDate.now()));
        gestor.addCliente(new Cliente("Cliente B", "b@example.com", LocalDate.now()));

        List<Cliente> clientes = gestor.getAllClientes();
        assertNotNull(clientes, "La lista de clientes no debería ser null.");
        assertEquals(2, clientes.size(), "Debería retornar todos los clientes añadidos.");
    }

    @Test
    void getAllClientesReturnsEmptyListWhenNoClients() {
        List<Cliente> clientes = gestor.getAllClientes();
        assertNotNull(clientes, "La lista de clientes no debería ser null.");
        assertTrue(clientes.isEmpty(), "Debería retornar una lista vacía si no hay clientes.");
    }

    @Test
    void updateClienteUpdatesClientSuccessfully() {
        Cliente original = new Cliente("Original Nombre", "original@example.com", LocalDate.now());
        gestor.addCliente(original);

        Cliente updated = new Cliente(original.getId_cliente(), "Updated Nombre", "updated@example.com", LocalDate.now());
        assertTrue(gestor.updateCliente(updated), "Debería actualizar el cliente exitosamente.");

        Cliente found = gestor.getClienteById(original.getId_cliente());
        assertNotNull(found);
        assertEquals("Updated Nombre", found.getNombre());
        assertEquals("updated@example.com", found.getEmail());
        assertEquals(LocalDate.now(), found.getFecha_registro());
    }

    @Test
    void updateClienteRejectsUpdateWithDuplicateEmail() {
        Cliente cliente1 = new Cliente("Cliente 1", "email1@example.com", LocalDate.now());
        gestor.addCliente(cliente1);

        Cliente cliente2 = new Cliente("Cliente 2", "email2@example.com", LocalDate.now());
        gestor.addCliente(cliente2);

        Cliente updatedCliente2 = new Cliente(cliente2.getId_cliente(), "Cliente 2 Modificado", "email1@example.com", LocalDate.now());
        assertFalse(gestor.updateCliente(updatedCliente2), "No debería permitir actualizar con email duplicado.");
    }

    @Test
    void updateClienteRejectsNonExistentClient() {
        Cliente nonExistent = new Cliente(9999, "Non Existent", "nonexistent@example.com", LocalDate.now());
        assertFalse(gestor.updateCliente(nonExistent), "No debería actualizar un cliente no existente.");
    }

    @Test
    void deleteClienteDeletesClientSuccessfully() {
        Cliente cliente = new Cliente("Borrar Nombre", "borrar@example.com", LocalDate.now());
        gestor.addCliente(cliente);

        assertTrue(gestor.deleteCliente(cliente.getId_cliente()), "Debería eliminar el cliente exitosamente.");
        assertNull(gestor.getClienteById(cliente.getId_cliente()), "El cliente no debería ser encontrado después de eliminar.");
    }

    @Test
    void deleteClienteReturnsFalseForNonExistentClient() {
        assertFalse(gestor.deleteCliente(9999), "Debería retornar false para un ID no existente.");
    }
}