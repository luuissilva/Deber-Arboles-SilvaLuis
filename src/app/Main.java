package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

/**
 * Menú interactivo del Catálogo de la Biblioteca FISEI.
 */
public class Main {

    private static final Scanner ENTRADA = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = DatosPrueba.crearSoloRaiz();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== CATÁLOGO BIBLIOTECA FISEI =====");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultar altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro");
        System.out.println(" 8. Modificar libro");
        System.out.println(" 9. Eliminar libro");
        System.out.println("10. Recorridos con traza");
        System.out.println("11. Retos");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1:
                biblioteca = DatosPrueba.crearInicial();
                System.out.println("Estructura inicial cargada.");
                break;
            case 2:
                System.out.println(biblioteca.mostrarJerarquia());
                break;
            case 3:
                agregarUnidad();
                break;
            case 4:
                buscarUnidad();
                break;
            case 5:
                consultarEstructura();
                break;
            case 6:
                insertarLibro();
                break;
            case 7:
                buscarLibro();
                break;
            case 8:
                modificarLibro();
                break;
            case 9:
                eliminarLibro();
                break;
            case 10:
                recorridos();
                break;
            case 11:
                retos();
                break;
            case 0:
                System.out.println("Fin del programa.");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ------------------------------------------------------------- unidades

    private static void agregarUnidad() {
        String padre = leerTexto("Código del padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        TipoUnidad tipo = leerTipoUnidad();
        if (tipo == null) {
            System.out.println("Tipo no válido.");
            return;
        }
        boolean ok = biblioteca.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(ok ? "Unidad agregada." : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        String codigo = leerTexto("Código de la unidad: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null) {
            System.out.println("No existe la unidad " + codigo + ".");
            return;
        }
        System.out.println("Encontrada: " + nodo.getDato() + " | nivel " + biblioteca.nivel(codigo)
                + " | hijos " + nodo.getHijos().size()
                + (nodo.getCatalogo() != null ? " | libros " + nodo.getCatalogo().contar() : ""));
    }

    private static void consultarEstructura() {
        System.out.println("Altura (aristas): " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar el nivel: ");
        System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
    }

    // --------------------------------------------------------------- libros

    private static void insertarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año: ");
        boolean ok = catalogo.insertar(new Libro(codigo, titulo, autor, anio));
        System.out.println(ok ? "Libro insertado." : "El código " + codigo + " ya existe.");
    }

    private static void buscarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        Libro libro = catalogo.buscar(codigo);
        System.out.println(libro == null ? "null (no existe el libro " + codigo + ")" : libro.toString());
    }

    private static void modificarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        if (catalogo.buscar(codigo) == null) {
            System.out.println("false (no existe el libro " + codigo + ")");
            return;
        }
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año: ");
        System.out.println(catalogo.modificar(codigo, titulo, autor, anio) ? "Libro modificado." : "false");
    }

    private static void eliminarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        boolean ok = catalogo.eliminar(codigo);
        System.out.println(ok ? "Libro " + codigo + " eliminado." : "false (no existe el libro " + codigo + ")");
        if (ok) {
            System.out.println("Preorden: " + codigos(catalogo.recorrer(TipoRecorrido.PREORDEN)));
        }
    }

    // ----------------------------------------------------------- recorridos

    private static void recorridos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        System.out.println("Preorden: " + codigos(catalogo.recorrer(TipoRecorrido.PREORDEN)));
        System.out.println("Inorden:  " + codigos(catalogo.recorrer(TipoRecorrido.INORDEN)));
        System.out.println("Posorden: " + codigos(catalogo.recorrer(TipoRecorrido.POSORDEN)));
        System.out.println("Traza (1 = PREORDEN, 2 = INORDEN, 3 = POSORDEN, 0 = ninguna)");
        int tipo = leerEntero("Opción: ");
        if (tipo >= 1 && tipo <= 3) {
            System.out.println(catalogo.trazaRecorrido(TipoRecorrido.values()[tipo - 1]));
        }
    }

    // ---------------------------------------------------------------- retos

    private static void retos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        System.out.println("1. contarEnRango  2. caminoBusqueda  3. esABBValido  4. desdePreorden (extra)");
        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1:
                int a = leerEntero("a: ");
                int b = leerEntero("b: ");
                System.out.println("Libros en [" + a + ", " + b + "]: " + catalogo.contarEnRango(a, b));
                break;
            case 2:
                int codigo = leerEntero("Código a buscar: ");
                System.out.println("Camino: " + catalogo.caminoBusqueda(codigo));
                break;
            case 3:
                System.out.println("esABBValido: " + catalogo.esABBValido());
                break;
            case 4:
                List<Libro> pre = catalogo.recorrer(TipoRecorrido.PREORDEN);
                int[] preorden = new int[pre.size()];
                for (int i = 0; i < preorden.length; i++) {
                    preorden[i] = pre.get(i).getCodigo();
                }
                CatalogoABB copia = CatalogoABB.desdePreorden(preorden);
                System.out.println("Reconstruido desde el preorden");
                System.out.println("Preorden: " + codigos(copia.recorrer(TipoRecorrido.PREORDEN)));
                System.out.println("Inorden:  " + codigos(copia.recorrer(TipoRecorrido.INORDEN)));
                System.out.println("esABBValido: " + copia.esABBValido());
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // -------------------------------------------------------------- ayudas

    private static CatalogoABB pedirCatalogo() {
        String codigo = leerTexto("Código del estante: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null || nodo.getCatalogo() == null) {
            System.out.println("No existe el estante " + codigo + ".");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static TipoUnidad leerTipoUnidad() {
        String texto = leerTexto("Tipo (AREA / ESTANTE / BIBLIOTECA): ").toUpperCase();
        for (TipoUnidad tipo : TipoUnidad.values()) {
            if (tipo.name().equals(texto)) {
                return tipo;
            }
        }
        return null;
    }

    static String codigos(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : libros) {
            sb.append(libro.getCodigo()).append(' ');
        }
        return sb.toString().trim();
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!ENTRADA.hasNextLine()) {
            return "";
        }
        return ENTRADA.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty() && !ENTRADA.hasNextLine()) {
                return 0;                                  // fin de la entrada: salir
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");
            }
        }
    }
}
