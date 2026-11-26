import java.util.Objects;
import java.util.Random;

/** Coordina las cuatro rondas y aplica las reglas del combate. */
public final class Partida {
    /** Cantidad fija de enfrentamientos que deben resolverse. */
    public static final int TOTAL_RONDAS = 4;
    /** Primer participante, con su equipo y estado. */
    private final Entrenador entrenador1;
    /** Segundo participante, con su equipo y estado. */
    private final Entrenador entrenador2;
    /** Generador inyectado que permite probar las probabilidades. */
    private final Random azar;
    /** Próxima ronda a resolver; vale cinco cuando termina la partida. */
    private int rondaActual;

    /**
     * Vincula dos participantes distintos y un generador aleatorio.
     * @param entrenador1 primer participante
     * @param entrenador2 segundo participante
     * @param azar generador de los intentos de habilidad
     */
    public Partida(Entrenador entrenador1, Entrenador entrenador2, Random azar) {
        this.entrenador1 = Objects.requireNonNull(entrenador1, "Falta el primer entrenador.");
        this.entrenador2 = Objects.requireNonNull(entrenador2, "Falta el segundo entrenador.");
        if (entrenador1 == entrenador2) {
            throw new IllegalArgumentException("Se necesitan dos entrenadores distintos.");
        }
        this.azar = Objects.requireNonNull(azar, "El generador es obligatorio.");
        this.rondaActual = 1;
    }

    /** @return ronda pendiente, o cinco si ya se resolvieron las cuatro */
    public int getRondaActual() { return rondaActual; }

    /** @return verdadero exclusivamente después de la cuarta ronda */
    public boolean haTerminado() { return rondaActual > TOTAL_RONDAS; }

    /**
     * Valida ambas decisiones antes de consumir fichas y resuelve simultáneamente.
     * @param indice1 posición disponible del primer equipo, desde cero
     * @param accion1 atacar o intentar digievolucionar
     * @param indice2 posición disponible del segundo equipo, desde cero
     * @param accion2 atacar o intentar digievolucionar
     * @return datos de la ronda y su ganador; también actualiza equipos y marcador
     */
    public ResultadoRonda jugarRonda(int indice1, Accion accion1, int indice2, Accion accion2) {
        if (haTerminado()) {
            throw new IllegalStateException("La partida ya terminó.");
        }
        Objects.requireNonNull(accion1, "Falta la primera acción.");
        Objects.requireNonNull(accion2, "Falta la segunda acción.");
        if (!entrenador1.equipoCompleto() || !entrenador2.equipoCompleto()
                || !entrenador1.estaDisponible(indice1) || !entrenador2.estaDisponible(indice2)) {
            throw new IllegalArgumentException("Ambos equipos deben estar completos y las fichas disponibles.");
        }
        Digimon d1 = entrenador1.usarDigimon(indice1);
        Digimon d2 = entrenador2.usarDigimon(indice2);
        entrenador1.descartarEfectosVencidos(rondaActual);
        entrenador2.descartarEfectosVencidos(rondaActual);

        boolean activo1 = accion1 == Accion.DIGIEVOLUCIONAR
                && d1.getDigievolucion().intentarActivar(azar);
        boolean activo2 = accion2 == Accion.DIGIEVOLUCIONAR
                && d2.getDigievolucion().intentarActivar(azar);
        if (activo1) {
            entrenador1.activarEfecto(d1.getDigievolucion(), rondaActual);
        }
        if (activo2) {
            entrenador2.activarEfecto(d2.getDigievolucion(), rondaActual);
        }

        int total1 = calcularAtaqueTotal(d1, d2, entrenador1, entrenador2);
        int total2 = calcularAtaqueTotal(d2, d1, entrenador2, entrenador1);
        ResultadoRonda resultado = new ResultadoRonda(rondaActual, d1, d2,
                accion1, accion2, activo1, activo2, total1, total2);
        if (resultado.getGanador() == 1) {
            entrenador1.registrarVictoria();
        } else if (resultado.getGanador() == 2) {
            entrenador2.registrarVictoria();
        }
        rondaActual++;
        return resultado;
    }

    /**
     * Aplica la fórmula documentada, conservando puntuaciones negativas.
     * @param propio combatiente cuyo ataque total se calcula
     * @param rival combatiente contrario
     * @param dueno entrenador que recibe los bonos ofensivos
     * @param contrario entrenador que aporta defensa y daño directo
     * @return ataque base más tipo y bonos, menos defensa y reducciones rivales
     */
    private int calcularAtaqueTotal(Digimon propio, Digimon rival,
                                     Entrenador dueno, Entrenador contrario) {
        return propio.getAtaque() + propio.getTipo().bonificacionContra(rival.getTipo())
                + dueno.sumarEfectos(Efecto.ATAQUE, rondaActual)
                - rival.getDefensa()
                - contrario.sumarEfectos(Efecto.DEFENSA, rondaActual)
                - contrario.sumarEfectos(Efecto.DANIO_DIRECTO, rondaActual);
    }

    /**
     * Compara las victorias después de resolver las cuatro rondas.
     * @return 1 si gana el primero, 2 si gana el segundo o 0 en empate final
     */
    public int getGanadorFinal() {
        if (!haTerminado()) {
            throw new IllegalStateException("Aún faltan rondas por jugar.");
        }
        int victorias1 = entrenador1.getRondasGanadas();
        int victorias2 = entrenador2.getRondasGanadas();
        return victorias1 == victorias2 ? 0 : (victorias1 > victorias2 ? 1 : 2);
    }
}
