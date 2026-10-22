package enums;

/**
 * Tipos de unidad de la Biblioteca FISEI.
 * La jerarquía permitida es BIBLIOTECA -> AREA -> ESTANTE.
 */
public enum TipoUnidad {
    BIBLIOTECA,
    AREA,
    ESTANTE;

    /**
     * Indica si una unidad de este tipo puede tener como hijo a una unidad del tipo recibido.
     * BIBLIOTECA solo acepta AREA, AREA solo acepta ESTANTE y ESTANTE no acepta hijos.
     */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        switch (this) {
            case BIBLIOTECA:
                return hijo == AREA;
            case AREA:
                return hijo == ESTANTE;
            default:
                return false;
        }
    }
}
