package mi.aplicacion;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        BBDD gestorDB = new BBDD();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        if (gestorDB.conectar()) {
            System.out.println("Conexión a la base de datos establecida correctamente.");

            while (!salir) {
                System.out.println("\n--- MENÚ PRINCIPAL ---");
                System.out.println("1. Gestionar Productos");
                System.out.println("2. Gestionar Clientes");
                System.out.println("3. Gestionar Valoraciones");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                try {
                    int opcionPrincipal = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcionPrincipal) {
                        case 1:
                            gestionarProductos(gestorDB, scanner);
                            break;
                        case 2:
                            gestionarClientes(gestorDB, scanner);
                            break;
                        case 3:
                            gestionarValoraciones(gestorDB, scanner);
                            break;
                        case 0:
                            salir = true;
                            System.out.println("Saliendo de la aplicación.");
                            break;
                        default:
                            System.out.println("Opción no válida. Inténtelo de nuevo.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, introduzca un número.");
                    scanner.nextLine();
                }
            }

            gestorDB.unLoad();
            System.out.println("Conexión a la base de datos cerrada.");
        } else {
            System.out.println("Error al conectar a la base de datos. Verifica la configuración.");
        }
        scanner.close();
    }

    private static void gestionarProductos(BBDD gestorDB, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
            System.out.println("1. Persistir (Añadir) Producto");
            System.out.println("2. Fusionar (Actualizar) Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Buscar Producto(s)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Nombre del producto: ");
                        String nombreProducto = scanner.nextLine();
                        System.out.print("Precio: ");
                        double precioProducto = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Categoría: ");
                        String categoriaProducto = scanner.nextLine();

                        Producto nuevoProducto = new Producto(nombreProducto, precioProducto, categoriaProducto);
                        if (gestorDB.persist(nuevoProducto)) {
                            System.out.println("Producto guardado: " + nuevoProducto.getNombre_producto() + " con ID: " + nuevoProducto.getId_producto());
                        } else {
                            System.out.println("Error al guardar el producto.");
                        }
                        break;
                    case 2:
                        System.out.println("Lógica de 'Fusionar Producto' (Merge) aún no implementada.");
                        break;
                    case 3:
                        System.out.println("Lógica de 'Eliminar Producto' (Remove) aún no implementada.");
                        break;
                    case 4:
                        System.out.println("Lógica de 'Buscar Producto(s)' (Find) aún no implementada.");
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número o el formato correcto.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void gestionarClientes(BBDD gestorDB, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Persistir (Añadir) Cliente");
            System.out.println("2. Fusionar (Actualizar) Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Buscar Cliente(s)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Nombre del cliente: ");
                        String nombreCliente = scanner.nextLine();
                        System.out.print("Email del cliente: ");
                        String emailCliente = scanner.nextLine();
                        System.out.print("Fecha de registro (YYYY-MM-DD): ");
                        String fechaRegistroStr = scanner.nextLine();
                        Date fechaRegistro = null;
                        try {
                            fechaRegistro = Date.valueOf(fechaRegistroStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Formato de fecha inválido. Use YYYY-MM-DD. No se persistirá el cliente.");
                            break;
                        }

                        Cliente nuevoCliente = new Cliente(nombreCliente, emailCliente, fechaRegistro);
                        if (gestorDB.persist(nuevoCliente)) {
                            System.out.println("Cliente guardado: " + nuevoCliente.getNombre() + " con ID: " + nuevoCliente.getId_cliente());
                        } else {
                            System.out.println("Error al guardar el cliente.");
                        }
                        break;
                    case 2:
                        System.out.println("Lógica de 'Fusionar Cliente' (Merge) aún no implementada.");
                        break;
                    case 3:
                        System.out.println("Lógica de 'Eliminar Cliente' (Remove) aún no implementada.");
                        break;
                    case 4:
                        System.out.println("Lógica de 'Buscar Cliente(s)' (Find) aún no implementada.");
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void gestionarValoraciones(BBDD gestorDB, Scanner scanner) {
        boolean volverMenuPrincipal = false;
        while (!volverMenuPrincipal) {
            System.out.println("\n--- GESTIÓN DE VALORACIONES ---");
            System.out.println("1. Persistir (Añadir) Valoración");
            System.out.println("2. Fusionar (Actualizar) Valoración");
            System.out.println("3. Eliminar Valoración");
            System.out.println("4. Buscar Valoración(es)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Puntuación (1-5): ");
                        int puntuacion = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Comentario: ");
                        String comentario = scanner.nextLine();
                        System.out.print("Fecha de valoración (YYYY-MM-DD): ");
                        String fechaValoracionStr = scanner.nextLine();
                        Date fechaValoracion = null;
                        try {
                            fechaValoracion = Date.valueOf(fechaValoracionStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Formato de fecha inválido. Use YYYY-MM-DD. No se persistirá la valoración.");
                            break;
                        }

                        Valoracion nuevaValoracion = new Valoracion(puntuacion, comentario, fechaValoracion);
                        if (gestorDB.persist(nuevaValoracion)) {
                            System.out.println("Valoración guardada (Puntuación: " + nuevaValoracion.getPuntuacion() + ") con ID: " + nuevaValoracion.getId_valoracion());
                        } else {
                            System.out.println("Error al guardar la valoración.");
                        }
                        break;
                    case 2:
                        System.out.println("Lógica de 'Fusionar Valoración' (Merge) aún no implementada.");
                        break;
                    case 3:
                        System.out.println("Lógica de 'Eliminar Valoración' (Remove) aún no implementada.");
                        break;
                    case 4:
                        System.out.println("Lógica de 'Buscar Valoración(es)' (Find) aún no implementada.");
                        break;
                    case 0:
                        volverMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número o el formato correcto.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}