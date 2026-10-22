package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/**
 * Datos de la estructura inicial de la Biblioteca FISEI.
 */
public final class DatosPrueba {

    private DatosPrueba() {
    }

    /** Biblioteca solo con la raíz. */
    public static ArbolBiblioteca crearSoloRaiz() {
        return new ArbolBiblioteca(new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
    }

    /** Biblioteca inicial: 7 unidades, 4 hojas, altura 2 y 13 libros. */
    public static ArbolBiblioteca crearInicial() {
        ArbolBiblioteca biblioteca = crearSoloRaiz();

        biblioteca.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        biblioteca.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));

        // SW-E1: 60, 35, 80, 20, 45, 70, 90, 40, 75 (en ese orden)
        CatalogoABB swE1 = biblioteca.buscar("SW-E1").getCatalogo();
        swE1.insertar(new Libro(60, "Estructuras de datos en Java", "Mark Allen Weiss", 2013));
        swE1.insertar(new Libro(35, "Introducción a los algoritmos", "Thomas Cormen", 2009));
        swE1.insertar(new Libro(80, "Código limpio", "Robert C. Martin", 2008));
        swE1.insertar(new Libro(20, "Java: cómo programar", "Paul Deitel", 2016));
        swE1.insertar(new Libro(45, "Algoritmos", "Robert Sedgewick", 2011));
        swE1.insertar(new Libro(70, "Patrones de diseño", "Erich Gamma", 1994));
        swE1.insertar(new Libro(90, "El programador pragmático", "Andrew Hunt", 1999));
        swE1.insertar(new Libro(40, "Ingeniería de software", "Ian Sommerville", 2011));
        swE1.insertar(new Libro(75, "Refactorización", "Martin Fowler", 1999));

        // SW-E2: 15, 10, 25
        CatalogoABB swE2 = biblioteca.buscar("SW-E2").getCatalogo();
        swE2.insertar(new Libro(15, "Fundamentos de bases de datos", "Abraham Silberschatz", 2006));
        swE2.insertar(new Libro(10, "Sistemas operativos modernos", "Andrew Tanenbaum", 2009));
        swE2.insertar(new Libro(25, "Python para todos", "Charles Severance", 2016));

        // TI-E1: 50
        CatalogoABB tiE1 = biblioteca.buscar("TI-E1").getCatalogo();
        tiE1.insertar(new Libro(50, "Gestión de proyectos de TI", "Kathy Schwalbe", 2015));

        return biblioteca;
    }
}
