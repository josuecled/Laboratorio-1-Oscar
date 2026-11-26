/** Conserva un resultado inmutable para que la interfaz lo presente. */
public final class ResultadoRonda {
    /** Número de la ronda resuelta. */
    private final int numero;
    /** Combatiente elegido por el primer entrenador. */
    private final Digimon digimon1;
    /** Combatiente elegido por el segundo entrenador. */
    private final Digimon digimon2;
    /** Decisión del primer entrenador. */
    private final Accion accion1;
    /** Decisión del segundo entrenador. */
    private final Accion accion2;
    /** Indica si se activó la habilidad del primer combatiente. */
    private final boolean activo1;
    /** Indica si se activó la habilidad del segundo combatiente. */
    private final boolean activo2;
    /** Puntuación ofensiva final del primer combatiente. */
    private final int total1;
    /** Puntuación ofensiva final del segundo combatiente. */
    private final int total2;

    /**
     * Guarda las decisiones y los cálculos efectuados por la partida.
     * @param numero ronda resuelta, de 1 a 4
     * @param digimon1 primera ficha participante
     * @param digimon2 segunda ficha participante
     * @param accion1 primera acción seleccionada
     * @param accion2 segunda acción seleccionada
     * @param activo1 éxito de la primera habilidad
     * @param activo2 éxito de la segunda habilidad
     * @param total1 primera puntuación final
     * @param total2 segunda puntuación final
     */
    public ResultadoRonda(int numero, Digimon digimon1, Digimon digimon2,
                          Accion accion1, Accion accion2, boolean activo1,
                          boolean activo2, int total1, int total2) {
        this.numero = numero;
        this.digimon1 = digimon1;
        this.digimon2 = digimon2;
        this.accion1 = accion1;
        this.accion2 = accion2;
        this.activo1 = activo1;
        this.activo2 = activo2;
        this.total1 = total1;
        this.total2 = total2;
    }

    /** @return número de ronda resuelta */
    public int getNumero() { return numero; }

    /** @return Digimon del primer entrenador */
    public Digimon getDigimon1() { return digimon1; }

    /** @return Digimon del segundo entrenador */
    public Digimon getDigimon2() { return digimon2; }

    /** @return acción del primer entrenador */
    public Accion getAccion1() { return accion1; }

    /** @return acción del segundo entrenador */
    public Accion getAccion2() { return accion2; }

    /** @return verdadero si la primera habilidad se activó */
    public boolean isActivo1() { return activo1; }

    /** @return verdadero si la segunda habilidad se activó */
    public boolean isActivo2() { return activo2; }

    /** @return ataqueTotal del primer combatiente */
    public int getTotal1() { return total1; }

    /** @return ataqueTotal del segundo combatiente */
    public int getTotal2() { return total2; }

    /** @return 1 si gana el primero, 2 si gana el segundo o 0 si empatan */
    public int getGanador() {
        return total1 == total2 ? 0 : (total1 > total2 ? 1 : 2);
    }
}
