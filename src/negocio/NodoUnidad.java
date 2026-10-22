package negocio;

import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.List;
import modelo.UnidadBiblioteca;

/**
 * Nodo del árbol general n-ario. Solo los estantes tienen catálogo (ABB de libros).
 */
public class NodoUnidad {

    private UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException("La unidad no puede ser null.");
        }
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public void setDato(UnidadBiblioteca dato) {
        this.dato = dato;
    }

    public List<NodoUnidad> getHijos() {
        return hijos;
    }

    public void agregarHijo(NodoUnidad hijo) {
        hijos.add(hijo);
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }

    /** Devuelve el catálogo del estante, o null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() {
        return catalogo;
    }
}
