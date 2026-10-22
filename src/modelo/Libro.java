package modelo;

/**
 * Libro del catálogo. El código es la clave del ABB, por eso no tiene setter.
 */
public class Libro {

    public static final int ANIO_MINIMO = 1900;
    public static final int ANIO_MAXIMO = 2026;

    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un entero mayor que 0.");
        }
        this.codigo = codigo;
        this.titulo = validarTexto(titulo, "título");
        this.autor = validarTexto(autor, "autor");
        this.anio = validarAnio(anio);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = validarTexto(titulo, "título");
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = validarTexto(autor, "autor");
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = validarAnio(anio);
    }

    private static int validarAnio(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_MAXIMO) {
            throw new IllegalArgumentException(
                    "El año debe estar entre " + ANIO_MINIMO + " y " + ANIO_MAXIMO + ".");
        }
        return anio;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + titulo + " - " + autor + " (" + anio + ")";
    }
}
