import java.util.Objects;

/** Almacena la ficha inmutable de un combatiente. */
public final class Digimon {
    /** Nombre que identifica al Digimon dentro de un equipo. */
    private final String nombre;
    /** Elemento utilizado en la tabla de efectividad. */
    private final Tipo tipo;
    /** Puntos ofensivos antes de modificadores. */
    private final int ataque;
    /** Puntos que se restan al ataque total del adversario. */
    private final int defensa;
    /** Habilidad disponible al elegir digievolucionar. */
    private final Digievolucion digievolucion;

    /**
     * Crea una ficha completa, con estadísticas no negativas.
     * @param nombre identificador no vacío
     * @param tipo elemento del combatiente
     * @param ataque puntos ofensivos base
     * @param defensa puntos defensivos base
     * @param digievolucion habilidad propia del combatiente
     */
    public Digimon(String nombre, Tipo tipo, int ataque, int defensa,
                   Digievolucion digievolucion) {
        if (nombre == null || nombre.trim().isEmpty() || ataque < 0 || defensa < 0) {
            throw new IllegalArgumentException("Datos del Digimon inválidos.");
        }
        this.nombre = nombre.trim();
        this.tipo = Objects.requireNonNull(tipo, "El tipo es obligatorio.");
        this.ataque = ataque;
        this.defensa = defensa;
        this.digievolucion = Objects.requireNonNull(digievolucion, "La habilidad es obligatoria.");
    }

    /** @return nombre identificador del combatiente */
    public String getNombre() { return nombre; }

    /** @return elemento del combatiente */
    public Tipo getTipo() { return tipo; }

    /** @return puntos ofensivos base */
    public int getAtaque() { return ataque; }

    /** @return puntos defensivos base */
    public int getDefensa() { return defensa; }

    /** @return habilidad inmutable asociada al combatiente */
    public Digievolucion getDigievolucion() { return digievolucion; }
}
