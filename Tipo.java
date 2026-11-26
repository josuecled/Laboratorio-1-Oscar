/** Representa los cuatro elementos y resuelve sus ventajas. */
public enum Tipo {
    FUEGO, AGUA, PLANTA, ELECTRICO;

    /**
     * Calcula el modificador del elemento atacante contra el rival.
     * @param rival elemento del oponente
     * @return 20 si es efectivo, -10 si es débil o 0 si es neutral
     */
    public int bonificacionContra(Tipo rival) {
        if (rival == null) {
            throw new IllegalArgumentException("El tipo rival es obligatorio.");
        }
        if (esFuerteContra(rival)) {
            return 20;
        }
        return rival.esFuerteContra(this) ? -10 : 0;
    }

    /**
     * Consulta las cuatro relaciones de efectividad del enunciado.
     * @param rival elemento que recibe el ataque
     * @return verdadero si este elemento tiene ventaja directa
     */
    private boolean esFuerteContra(Tipo rival) {
        return (this == FUEGO && rival == PLANTA)
                || (this == PLANTA && rival == AGUA)
                || (this == AGUA && rival == FUEGO)
                || (this == ELECTRICO && rival == AGUA);
    }
}
