package mi.aplicacion;

import java.util.ArrayList;
import java.util.List;

public class ClienteGestor {
    private BBDD gestorDB; // Añadimos una referencia a la clase BBDD para interactuar con la base de datos

    // Constructor que acepta una instancia de BBDD
    public ClienteGestor(BBDD gestorDB) {
        if (gestorDB == null) {
            throw new IllegalArgumentException("La instancia de BBDD no puede ser null.");
        }
        this.gestorDB = gestorDB;
        // Al iniciar el gestor, podríamos cargar todos los clientes de la DB
        // Pero para simplificar, los métodos individuales interactuarán con la DB
    }

    // El método addCliente ahora inserta en la base de datos y luego actualiza el ID
    public boolean addCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("Error: El cliente no puede ser null.");
            return false;
        }

        // Antes de añadir, verifica si el email ya existe en la DB (para evitar duplicados)
        if (gestorDB.findClienteByEmail(cliente.getEmail()) != null) {
            System.out.println("Error: Ya existe un cliente con el email " + cliente.getEmail() + " en la base de datos.");
            return false;
        }

        // Intenta persistir el cliente en la base de datos
        // El método persist(Cliente) en BBDD debería devolver el ID generado o true/false
        boolean insertado = gestorDB.persist(cliente);

        if (insertado) {
            // Opcional: si BBDD.persist(cliente) actualiza el id_cliente del objeto pasado, no necesitas esto.
            // Si no lo hace, deberías recuperarlo de alguna forma (por ejemplo, con findClienteByEmail después de insertar).
            // Para este ejemplo, asumimos que persist() ya actualiza el ID o lo manejarás en la App.
            System.out.println("Cliente " + cliente.getNombre() + " añadido a la base de datos con éxito.");
            return true;
        } else {
            System.out.println("Error: No se pudo añadir el cliente a la base de datos.");
            return false;
        }
    }

    public Cliente getClienteById(int id) {
        // Busca el cliente directamente en la base de datos
        return gestorDB.findClienteById(id);
    }

    public List<Cliente> getAllClientes() {
        // Obtiene todos los clientes de la base de datos
        return gestorDB.findAllClientes();
    }

    public boolean updateCliente(Cliente clienteActualizado) {
        if (clienteActualizado == null || clienteActualizado.getId_cliente() <= 0) {
            System.out.println("Error: Cliente a actualizar inválido o sin ID.");
            return false;
        }

        // Verificar email duplicado para otros clientes en la DB
        Cliente clienteExistenteConEmail = gestorDB.findClienteByEmail(clienteActualizado.getEmail());
        if (clienteExistenteConEmail != null && clienteExistenteConEmail.getId_cliente() != clienteActualizado.getId_cliente()) {
            System.out.println("Error: El email " + clienteActualizado.getEmail() + " ya está en uso por otro cliente en la base de datos.");
            return false;
        }

        // Intenta actualizar el cliente en la base de datos
        boolean actualizado = gestorDB.merge(clienteActualizado);
        if (actualizado) {
            System.out.println("Cliente " + clienteActualizado.getNombre() + " actualizado en la base de datos con éxito.");
            return true;
        } else {
            System.out.println("Error: No se pudo actualizar el cliente en la base de datos.");
            return false;
        }
    }

    public boolean deleteCliente(int id) {
        if (id <= 0) {
            System.out.println("Error: ID de cliente inválido para eliminar.");
            return false;
        }
        // Intenta eliminar el cliente de la base de datos
        boolean eliminado = gestorDB.removeCliente(id); // Asumiendo que BBDD.removeCliente(int id) existe
        if (eliminado) {
            System.out.println("Cliente con ID " + id + " eliminado de la base de datos con éxito.");
            return true;
        } else {
            System.out.println("Error: No se pudo eliminar el cliente con ID " + id + " de la base de datos.");
            return false;
        }
    }
}