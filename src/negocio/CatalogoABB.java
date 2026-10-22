package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

/**
 * Árbol Binario de Búsqueda (ABB) de libros ordenado por código.
 * Menores a la izquierda, mayores a la derecha y sin repetidos.
 * Todas las operaciones son recursivas.
 */
public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    // ================================================================ insertar

    /** Inserta un libro por su código. Devuelve false si el código ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null.");
        }
        if (buscarNodo(raiz, libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);                   // caso base: lugar vacío
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ================================================================== buscar

    /** Busca un libro por código. Devuelve null si no existe. */
    public Libro buscar(int codigo) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        return nodo == null ? null : nodo.getDato();
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;                                   // caso base: no existe
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo;                                   // caso base: encontrado
        }
        return codigo < actual
                ? buscarNodo(nodo.getIzquierdo(), codigo)
                : buscarNodo(nodo.getDerecho(), codigo);
    }

    // ================================================================== contar

    /** Número de libros del catálogo. */
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // =============================================================== recorrer

    /** Recorrido en preorden, inorden o posorden. */
    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> resultado) {
        if (nodo == null) {
            return;                                        // caso base
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.add(nodo.getDato());                 // nodo - izq - der
        }
        recorrer(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) {
            resultado.add(nodo.getDato());                 // izq - nodo - der
        }
        recorrer(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.add(nodo.getDato());                 // izq - der - nodo
        }
    }

    // ================================================================== traza

    /**
     * Traza del recorrido:
     * "entra X" al entrar al nodo, "VISITA X" al procesarlo y "null -> regresa" en un hijo vacío,
     * con 2 espacios por nivel. La última línea es "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        traza(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(": ").append(secuencia.toString().trim());
        return traza.toString();
    }

    private void traza(NodoLibro nodo, TipoRecorrido tipo, int nivel,
                       StringBuilder traza, StringBuilder secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");       // caso base
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append('\n');

        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getIzquierdo(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getDerecho(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append('\n');
        secuencia.append(codigo).append(' ');
    }

    // =============================================================== modificar

    /** Modifica título, autor y año (el código no cambia). Devuelve false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            return false;
        }
        // Se validan los datos antes de cambiar nada, para no dejar el libro a medias.
        new Libro(codigo, titulo, autor, anio);
        nodo.getDato().setTitulo(titulo);
        nodo.getDato().setAutor(autor);
        nodo.getDato().setAnio(anio);
        return true;
    }

    // ================================================================ eliminar

    /**
     * Elimina un libro. Cubre los tres casos:
     * hoja, nodo con un hijo y nodo con dos hijos (se reemplaza por el sucesor inorden).
     */
    public boolean eliminar(int codigo) {
        if (buscarNodo(raiz, codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            // Caso 1 (hoja) y caso 2 (un hijo): sube el hijo que exista, o null.
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // Caso 3 (dos hijos): sucesor inorden = menor del subárbol derecho.
            Libro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor);
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getCodigo()));
        }
        return nodo;
    }

    private Libro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo.getDato();
        }
        return minimo(nodo.getIzquierdo());
    }

    // =================================================================== RETOS

    /** Cuenta los códigos entre a y b (incluidos), con poda de ramas que no pueden aportar. */
    public int contarEnRango(int a, int b) {
        return contarEnRango(raiz, Math.min(a, b), Math.max(a, b));
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRango(nodo.getDerecho(), a, b);    // poda: la izquierda es aún menor
        }
        if (codigo > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);  // poda: la derecha es aún mayor
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde se termina la búsqueda). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo == actual) {
            return;
        }
        caminoBusqueda(codigo < actual ? nodo.getIzquierdo() : nodo.getDerecho(), codigo, camino);
    }

    /** Verifica todo el ABB con límites mínimo y máximo (no solo contra los hijos directos). */
    public boolean esABBValido() {
        return esABBValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo <= minimo || codigo >= maximo) {
            return false;
        }
        return esABBValido(nodo.getIzquierdo(), minimo, codigo)
                && esABBValido(nodo.getDerecho(), codigo, maximo);
    }

    // =================================================================== EXTRA

    /** Reconstruye el ABB a partir de su preorden, sin insertar uno por uno. O(N). */
    public static CatalogoABB desdePreorden(int[] preorden) {
        CatalogoABB catalogo = new CatalogoABB();
        if (preorden != null) {
            int[] indice = {0};
            catalogo.raiz = construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] preorden, int[] indice, long minimo, long maximo) {
        if (indice[0] >= preorden.length) {
            return null;
        }
        int codigo = preorden[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null;                                   // no pertenece a este subárbol
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Reconstruido", Libro.ANIO_MAXIMO));
        nodo.setIzquierdo(construir(preorden, indice, minimo, codigo));
        nodo.setDerecho(construir(preorden, indice, codigo, maximo));
        return nodo;
    }
}
