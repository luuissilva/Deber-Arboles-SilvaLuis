package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/**
 * Árbol general n-ario que representa la jerarquía de la Biblioteca FISEI.
 * Todas las operaciones son recursivas: caso base, procesar el nodo y llamada recursiva a los hijos.
 */
public class ArbolBiblioteca {

    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    // ------------------------------------------------------------------ agregar

    /**
     * Agrega una unidad como hija de codigoPadre.
     * Devuelve false si el padre no existe, si el código ya existe o si se rompe
     * la jerarquía BIBLIOTECA -> AREA -> ESTANTE.
     */
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null) {
            return false;
        }
        if (buscar(nueva.getCodigo()) != null) {
            return false;
        }
        if (!padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    // ------------------------------------------------------------------- buscar

    /** Busca una unidad por código sin distinguir mayúsculas. Devuelve null si no existe. */
    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim().toUpperCase());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo == null) {
            return null;                                   // caso base
        }
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nodo;                                   // encontrado
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            NodoUnidad encontrado = buscar(hijo, codigo);  // llamada recursiva
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------- altura

    /** Altura del árbol en aristas. Un árbol con solo la raíz tiene altura 0. */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 0;                                      // caso base
        }
        int mayor = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            mayor = Math.max(mayor, altura(hijo));         // llamada recursiva
        }
        return 1 + mayor;
    }

    // -------------------------------------------------------------------- hojas

    /** Número de unidades sin hijos. */
    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 1;                                      // caso base
        }
        int total = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarHojas(hijo);
        }
        return total;
    }

    // -------------------------------------------------------------------- nivel

    /** Nivel de la unidad (raíz = 0). Devuelve -1 si no existe. */
    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim().toUpperCase(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nivelActual;                            // caso base: encontrado
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            int resultado = nivel(hijo, codigo, nivelActual + 1);
            if (resultado != -1) {
                return resultado;
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------- jerarquía

    /** Jerarquía en preorden con 2 espacios por nivel. */
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarJerarquia(raiz, 0, sb);
        return sb.toString().trim();
    }

    private void mostrarJerarquia(NodoUnidad nodo, int nivelActual, StringBuilder sb) {
        sb.append("  ".repeat(nivelActual)).append(nodo.getDato());   // visita (preorden)
        if (nodo.getCatalogo() != null) {
            sb.append(" (").append(nodo.getCatalogo().contar()).append(" libros)");
        }
        sb.append('\n');
        for (NodoUnidad hijo : nodo.getHijos()) {
            mostrarJerarquia(hijo, nivelActual + 1, sb);
        }
    }

    // ------------------------------------------------------------- total libros

    /** Total de libros de todos los estantes. */
    public int contarLibrosTotal() {
        return contarLibrosTotal(raiz);
    }

    private int contarLibrosTotal(NodoUnidad nodo) {
        int total = nodo.getCatalogo() != null ? nodo.getCatalogo().contar() : 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarLibrosTotal(hijo);
        }
        return total;
    }
}
