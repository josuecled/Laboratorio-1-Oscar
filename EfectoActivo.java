import java.util.Objects;

/** Registra una activación, independiente de la ficha del Digimon que la originó. */
public final class EfectoActivo {
    /** Habilidad cuyos puntos se conservarán temporalmente. */
    private final Digievolucion habilidad;
    /** Primera ronda en la que se aplica el efecto. */
    private final int rondaInicio;
    /** Última ronda válida: la inmediatamente siguiente a la activación. */
    private final int rondaFin;

    /**
     * Crea una activación válida en la ronda indicada y en la siguiente.
     * @param habilidad habilidad que se activó
     * @param rondaInicio ronda positiva de activación
     */
    public EfectoActivo(Digievolucion habilidad, int rondaInicio) {
        if (rondaInicio < 1 || rondaInicio == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("La ronda inicial es inválida.");
        }
        this.habilidad = Objects.requireNonNull(habilidad, "La habilidad es obligatoria.");
        this.rondaInicio = rondaInicio;
        this.rondaFin = rondaInicio + 1;
    }

    /** @return habilidad que originó el efecto */
    public Digievolucion getHabilidad() { return habilidad; }

    /** @return primera ronda de vigencia */
    public int getRondaInicio() { return rondaInicio; }

    /** @return última ronda de vigencia */
    public int getRondaFin() { return rondaFin; }

    /**
     * Comprueba los dos extremos del intervalo de duración.
     * @param ronda ronda que se consulta
     * @return verdadero únicamente en la ronda inicial o la siguiente
     */
    public boolean estaVigente(int ronda) {
        return ronda >= rondaInicio && ronda <= rondaFin;
    }
}
