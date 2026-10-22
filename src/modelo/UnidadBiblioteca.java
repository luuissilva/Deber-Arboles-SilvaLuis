package modelo;

import enums.TipoUnidad;

/**
 * Unidad de la biblioteca: la biblioteca, un área o un estante.
 * El código se guarda siempre en mayúsculas.
 */
public class UnidadBiblioteca {

    private final String codigo;
    private String nombre;
    private final TipoUnidad tipo;

    public UnidadBiblioteca(String codigo, String nombre, TipoUnidad tipo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la unidad no puede estar vacío.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de la unidad es obligatorio.");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.tipo = tipo;
        this.nombre = validarNombre(nombre);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad no puede estar vacío.");
        }
        return nombre.trim();
    }

    public TipoUnidad getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " [" + tipo + "]";
    }
}
