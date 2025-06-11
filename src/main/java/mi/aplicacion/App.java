package mi.aplicacion;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        BBDD gestorDB = new BBDD();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        // Asegúrate de usar conectar() si ese es el método para establecer la conexión
        if (gestorDB.conectar()) {
            System.out.println("Conexión a la base de datos establecida correctamente.");

            // Inicializar ClienteGestor pasándole la instancia de BBDD
            ClienteGestor clienteGestor = new ClienteGestor(gestorDB);

            while (!salir) {
                System.out.println("\n--- MENÚ PRINCIPAL ---");
                System.out.println("1. Gestionar Productos");
                System.out.println("2. Gestionar Clientes");
                System.out.println("3. Gestionar Valoraciones");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                try {
                    int opcionPrincipal = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea

                    switch (opcionPrincipal) {
                        case 1:
                            gestionarProductos(gestorDB, scanner); // Pasa gestorDB
                            break;
                        case 2:
                            // Pasa el gestor de clientes actualizado
                            gestionarClientes(clienteGestor, scanner);
                            break;
                        case 3:
                            gestionarValoraciones(gestorDB, scanner); // Pasa gestorDB
                            break;
                        case 0:
                            salir = true;
                            System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, introduzca un número.");
                    scanner.nextLine(); // Consumir la entrada inválida
                }
            }
        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos. La aplicación no puede iniciar.");
        }
        // Cerrar la conexión y el scanner al salir
        gestorDB.desconectar();
        scanner.close();
    }

    // --- Métodos de Gestión ---

    private static void gestionarProductos(BBDD gestorDB, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- MENÚ DE PRODUCTOS ---");
            System.out.println("1. Añadir Producto");
            System.out.println("2. Buscar Producto(s)");
            System.out.println("3. Actualizar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("5. Ver Todos los Productos");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcionProducto = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (opcionProducto) {
                    case 1: // Añadir Producto
                        System.out.print("Nombre del producto: ");
                        String nombreProd = scanner.nextLine();
                        System.out.print("Precio: ");
                        double precioProd = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Categoría: ");
                        String categoriaProd = scanner.nextLine();

                        Producto nuevoProducto = new Producto(nombreProd, precioProd, categoriaProd);
                        if (gestorDB.persist(nuevoProducto)) { // Usa persist() de BBDD
                            System.out.println("Producto añadido con éxito. ID: " + nuevoProducto.getId_producto());
                        } else {
                            System.out.println("Error al añadir producto.");
                        }
                        break;
                    case 2: // Buscar Producto(s)
                        System.out.println("Buscar productos:");
                        System.out.println("1. Buscar por ID");
                        System.out.println("2. Ver todos");
                        System.out.print("Seleccione opción: ");
                        int opcionBusquedaProd = scanner.nextInt();
                        scanner.nextLine();

                        List<Producto> productosEncontrados = null;
                        if (opcionBusquedaProd == 1) {
                            System.out.print("ID del producto a buscar: ");
                            int idProdBuscar = scanner.nextInt();
                            scanner.nextLine();
                            Producto p = gestorDB.findProductoById(idProdBuscar); // Asumiendo findProductoById
                            if (p != null) {
                                productosEncontrados = List.of(p);
                            }
                        } else if (opcionBusquedaProd == 2) {
                            productosEncontrados = gestorDB.findAllProductos(); // Asumiendo findAllProductos
                        } else {
                            System.out.println("Opción de búsqueda no válida.");
                        }

                        if (productosEncontrados != null && !productosEncontrados.isEmpty()) {
                            System.out.println("\n--- Productos encontrados ---");
                            productosEncontrados.forEach(System.out::println);
                        } else {
                            System.out.println("No se encontraron productos.");
                        }
                        break;
                    case 3: // Actualizar Producto
                        System.out.print("ID del producto a actualizar: ");
                        int idUpdateProd = scanner.nextInt();
                        scanner.nextLine();
                        Producto prodActualizar = gestorDB.findProductoById(idUpdateProd); // Obtener el producto existente

                        if (prodActualizar != null) {
                            System.out.println("Producto actual: " + prodActualizar);
                            System.out.print("Nuevo nombre (dejar vacío para no cambiar): ");
                            String nuevoNombre = scanner.nextLine();
                            if (!nuevoNombre.isEmpty()) prodActualizar.setNombre_producto(nuevoNombre);

                            System.out.print("Nuevo precio (0 para no cambiar): ");
                            String precioStr = scanner.nextLine(); // Leer como String
                            if (!precioStr.isEmpty() && Double.parseDouble(precioStr) > 0) {
                                prodActualizar.setPrecio(Double.parseDouble(precioStr));
                            }

                            System.out.print("Nueva categoría (dejar vacío para no cambiar): ");
                            String nuevaCategoria = scanner.nextLine();
                            if (!nuevaCategoria.isEmpty()) prodActualizar.setCategoria(nuevaCategoria);

                            if (gestorDB.merge(prodActualizar)) { // Usa merge() de BBDD
                                System.out.println("Producto actualizado con éxito.");
                            } else {
                                System.out.println("Error al actualizar producto.");
                            }
                        } else {
                            System.out.println("Producto no encontrado.");
                        }
                        break;
                    case 4: // Eliminar Producto
                        System.out.print("ID del producto a eliminar: ");
                        int idDeleteProd = scanner.nextInt();
                        scanner.nextLine();
                        if (gestorDB.removeProducto(idDeleteProd)) { // Usa removeProducto() de BBDD
                            System.out.println("Producto eliminado con éxito.");
                        } else {
                            System.out.println("Error al eliminar producto o no encontrado.");
                        }
                        break;
                    case 5: // Ver Todos los Productos (ya está en buscar, pero para acceso directo)
                        List<Producto> todosProductos = gestorDB.findAllProductos();
                        if (todosProductos != null && !todosProductos.isEmpty()) {
                            System.out.println("\n--- Todos los Productos ---");
                            todosProductos.forEach(System.out::println);
                        } else {
                            System.out.println("No hay productos registrados.");
                        }
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número o el formato correcto.");
                scanner.nextLine(); // Consumir la entrada inválida
            } catch (IllegalArgumentException e) {
                System.out.println("Error de datos del producto: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void gestionarClientes(ClienteGestor clienteGestor, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- MENÚ DE CLIENTES ---");
            System.out.println("1. Añadir Cliente");
            System.out.println("2. Buscar Cliente(s)");
            System.out.println("3. Actualizar Cliente");
            System.out.println("4. Eliminar Cliente");
            System.out.println("5. Ver Todos los Clientes");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcionCliente = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (opcionCliente) {
                    case 1: // Añadir Cliente
                        System.out.print("Nombre del cliente: ");
                        String nombreCli = scanner.nextLine();
                        System.out.print("Email del cliente: ");
                        String emailCli = scanner.nextLine();
                        System.out.print("Fecha de registro (YYYY-MM-DD, dejar vacío para hoy): ");
                        String fechaRegStr = scanner.nextLine();
                        LocalDate fechaReg = fechaRegStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaRegStr);

                        Cliente nuevoCliente = new Cliente(0, nombreCli, emailCli, fechaReg); // ID 0 para nuevo
                        if (clienteGestor.addCliente(nuevoCliente)) { // Usa ClienteGestor
                            System.out.println("Cliente añadido con éxito.");
                        } else {
                            System.out.println("Error al añadir cliente.");
                        }
                        break;
                    case 2: // Buscar Cliente(s)
                        System.out.println("Buscar clientes:");
                        System.out.println("1. Buscar por ID");
                        System.out.println("2. Ver todos");
                        System.out.print("Seleccione opción: ");
                        int opcionBusquedaCli = scanner.nextInt();
                        scanner.nextLine();

                        List<Cliente> clientesEncontrados = null;
                        if (opcionBusquedaCli == 1) {
                            System.out.print("ID del cliente a buscar: ");
                            int idCliBuscar = scanner.nextInt();
                            scanner.nextLine();
                            Cliente c = clienteGestor.getClienteById(idCliBuscar); // Usa ClienteGestor
                            if (c != null) {
                                clientesEncontrados = List.of(c);
                            }
                        } else if (opcionBusquedaCli == 2) {
                            clientesEncontrados = clienteGestor.getAllClientes(); // Usa ClienteGestor
                        } else {
                            System.out.println("Opción de búsqueda no válida.");
                        }

                        if (clientesEncontrados != null && !clientesEncontrados.isEmpty()) {
                            System.out.println("\n--- Clientes encontrados ---");
                            clientesEncontrados.forEach(System.out::println);
                        } else {
                            System.out.println("No se encontraron clientes.");
                        }
                        break;
                    case 3: // Actualizar Cliente
                        System.out.print("ID del cliente a actualizar: ");
                        int idUpdateCli = scanner.nextInt();
                        scanner.nextLine();
                        Cliente clienteActualizar = clienteGestor.getClienteById(idUpdateCli); // Usa ClienteGestor

                        if (clienteActualizar != null) {
                            System.out.println("Cliente actual: " + clienteActualizar);
                            System.out.print("Nuevo nombre (dejar vacío para no cambiar): ");
                            String nuevoNombre = scanner.nextLine();
                            if (!nuevoNombre.isEmpty()) clienteActualizar.setNombre(nuevoNombre);

                            System.out.print("Nuevo email (dejar vacío para no cambiar): ");
                            String nuevoEmail = scanner.nextLine();
                            if (!nuevoEmail.isEmpty()) clienteActualizar.setEmail(nuevoEmail);

                            System.out.print("Nueva fecha de registro (YYYY-MM-DD, dejar vacío para no cambiar): ");
                            String nuevaFechaRegStr = scanner.nextLine();
                            if (!nuevaFechaRegStr.isEmpty()) {
                                try {
                                    clienteActualizar.setFecha_registro(LocalDate.parse(nuevaFechaRegStr));
                                } catch (DateTimeParseException e) {
                                    System.out.println("Formato de fecha inválido. Manteniendo fecha original.");
                                }
                            }

                            if (clienteGestor.updateCliente(clienteActualizar)) { // Usa ClienteGestor
                                System.out.println("Cliente actualizado con éxito.");
                            } else {
                                System.out.println("Error al actualizar cliente.");
                            }
                        } else {
                            System.out.println("Cliente no encontrado.");
                        }
                        break;
                    case 4: // Eliminar Cliente
                        System.out.print("ID del cliente a eliminar: ");
                        int idDeleteCli = scanner.nextInt();
                        scanner.nextLine();
                        if (clienteGestor.deleteCliente(idDeleteCli)) { // Usa ClienteGestor
                            System.out.println("Cliente eliminado con éxito.");
                        } else {
                            System.out.println("Error al eliminar cliente o no encontrado.");
                        }
                        break;
                    case 5: // Ver Todos los Clientes
                        List<Cliente> todosClientes = clienteGestor.getAllClientes(); // Usa ClienteGestor
                        if (todosClientes != null && !todosClientes.isEmpty()) {
                            System.out.println("\n--- Todos los Clientes ---");
                            todosClientes.forEach(System.out::println);
                        } else {
                            System.out.println("No hay clientes registrados.");
                        }
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número o el formato correcto.");
                scanner.nextLine(); // Consumir la entrada inválida
            } catch (IllegalArgumentException e) {
                System.out.println("Error de datos del cliente: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void gestionarValoraciones(BBDD gestorDB, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- MENÚ DE VALORACIONES ---");
            System.out.println("1. Añadir Valoración");
            System.out.println("2. Buscar Valoración(es)");
            System.out.println("3. Actualizar Valoración");
            System.out.println("4. Eliminar Valoración");
            System.out.println("5. Ver Todas las Valoraciones");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcionValoracion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (opcionValoracion) {
                    case 1: // Añadir Valoración
                        System.out.print("Puntuación (1-5): ");
                        int puntuacionVal = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Comentario: ");
                        String comentarioVal = scanner.nextLine();
                        System.out.print("Fecha de valoración (YYYY-MM-DD, dejar vacío para hoy): ");
                        String fechaValStr = scanner.nextLine();
                        LocalDate fechaVal = fechaValStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaValStr);

                        Valoracion nuevaValoracion = new Valoracion(0, puntuacionVal, comentarioVal, fechaVal); // ID 0 para nuevo
                        if (gestorDB.persist(nuevaValoracion)) { // Usa persist() de BBDD
                            System.out.println("Valoración añadida con éxito. ID: " + nuevaValoracion.getId_valoracion());
                        } else {
                            System.out.println("Error al añadir valoración.");
                        }
                        break;
                    case 2: // Buscar Valoración(es)
                        System.out.println("Buscar valoraciones:");
                        System.out.println("1. Buscar por ID");
                        System.out.println("2. Ver todas");
                        System.out.print("Seleccione opción: ");
                        int opcionBusquedaVal = scanner.nextInt();
                        scanner.nextLine();

                        List<Valoracion> valoracionesEncontradas = null;
                        if (opcionBusquedaVal == 1) {
                            System.out.print("ID de la valoración a buscar: ");
                            int idValBuscar = scanner.nextInt();
                            scanner.nextLine();
                            Valoracion v = gestorDB.findValoracionById(idValBuscar); // Asumiendo findValoracionById
                            if (v != null) {
                                valoracionesEncontradas = List.of(v);
                            }
                        } else if (opcionBusquedaVal == 2) {
                            valoracionesEncontradas = gestorDB.findAllValoraciones(); // Asumiendo findAllValoraciones
                        } else {
                            System.out.println("Opción de búsqueda no válida.");
                        }

                        if (valoracionesEncontradas != null && !valoracionesEncontradas.isEmpty()) {
                            System.out.println("\n--- Valoraciones encontradas ---");
                            valoracionesEncontradas.forEach(System.out::println);
                        } else {
                            System.out.println("No se encontraron valoraciones.");
                        }
                        break;
                    case 3: // Actualizar Valoración
                        System.out.print("ID de la valoración a actualizar: ");
                        int idUpdateVal = scanner.nextInt();
                        scanner.nextLine();
                        Valoracion valActualizar = gestorDB.findValoracionById(idUpdateVal); // Obtener la valoración existente

                        if (valActualizar != null) {
                            System.out.println("Valoración actual: " + valActualizar);
                            System.out.print("Nueva puntuación (1-5, 0 para no cambiar): ");
                            int nuevaPuntuacion = scanner.nextInt();
                            scanner.nextLine();
                            if (nuevaPuntuacion >= 1 && nuevaPuntuacion <= 5) {
                                valActualizar.setPuntuacion(nuevaPuntuacion);
                            }

                            System.out.print("Nuevo comentario (dejar vacío para no cambiar): ");
                            String nuevoComentario = scanner.nextLine();
                            if (!nuevoComentario.isEmpty()) valActualizar.setComentario(nuevoComentario);

                            System.out.print("Nueva fecha de valoración (YYYY-MM-DD, dejar vacío para no cambiar): ");
                            String nuevaFechaValStr = scanner.nextLine();
                            if (!nuevaFechaValStr.isEmpty()) {
                                try {
                                    valActualizar.setFecha_valoracion(LocalDate.parse(nuevaFechaValStr));
                                } catch (DateTimeParseException e) {
                                    System.out.println("Formato de fecha inválido. Manteniendo fecha original.");
                                }
                            }

                            if (gestorDB.merge(valActualizar)) { // Usa merge() de BBDD
                                System.out.println("Valoración actualizada con éxito.");
                            } else {
                                System.out.println("Error al actualizar valoración.");
                            }
                        } else {
                            System.out.println("Valoración no encontrada.");
                        }
                        break;
                    case 4: // Eliminar Valoración
                        System.out.print("ID de la valoración a eliminar: ");
                        int idDeleteVal = scanner.nextInt();
                        scanner.nextLine();
                        if (gestorDB.removeValoracion(idDeleteVal)) { // Usa removeValoracion() de BBDD
                            System.out.println("Valoración eliminada con éxito.");
                        } else {
                            System.out.println("Error al eliminar valoración o no encontrada.");
                        }
                        break;
                    case 5: // Ver Todas las Valoraciones
                        List<Valoracion> todasValoraciones = gestorDB.findAllValoraciones();
                        if (todasValoraciones != null && !todasValoraciones.isEmpty()) {
                            System.out.println("\n--- Todas las Valoraciones ---");
                            todasValoraciones.forEach(System.out::println);
                        } else {
                            System.out.println("No hay valoraciones registradas.");
                        }
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número o el formato correcto.");
                scanner.nextLine(); // Consumir la entrada inválida
            } catch (IllegalArgumentException e) {
                System.out.println("Error de datos de la valoración: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}