import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** Administra el equipo, los turnos usados, los efectos temporales y las victorias. */
public final class Entrenador {
    /** Cantidad exacta de Digimon que debe elegir cada entrenador. */
    public static final int CAPACIDAD = 4;
    /** Nombre que identifica al participante. */
    private final String nombre;
    /** Fichas elegidas; admite cuatro nombres distintos como máximo. */
    private final List<Digimon> equipo;
    /** Nombres normalizados de los Digimon que ya participaron. */
    private final Set<String> utilizados;
    /** Activaciones que pueden beneficiar al siguiente integrante del equipo. */
    private final List<EfectoActivo> efectos;
    /** Rondas ganadas; los empates no incrementan el contador. */
    private int rondasGanadas;

    /**
     * Inicializa un participante con equipo y estado vacíos.
     * @param nombre nombre no vacío del entrenador
     */
    public Entrenador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El entrenador debe tener nombre.");
        }
        this.nombre = nombre.trim();
        this.equipo = new ArrayList<Digimon>();
        this.utilizados = new HashSet<String>();
        this.efectos = new ArrayList<EfectoActivo>();
        this.rondasGanadas = 0;
    }

    /** @return nombre del participante */
    public String getNombre() { return nombre; }

    /** @return copia de solo lectura del equipo, para consultar sin alterarlo */
    public List<Digimon> getEquipo() {
        return Collections.unmodifiableList(new ArrayList<Digimon>(equipo));
    }

    /** @return número de rondas ganadas hasta este momento */
    public int getRondasGanadas() { return rondasGanadas; }

    /**
     * Agrega una ficha antes de comenzar, sin duplicar su nombre.
     * @param digimon ficha no nula que se desea seleccionar
     */
    public void agregarDigimon(Digimon digimon) {
        Objects.requireNonNull(digimon, "El Digimon es obligatorio.");
        if (equipoCompleto() || !utilizados.isEmpty()) {
            throw new IllegalStateException("El equipo ya está cerrado.");
        }
        for (Digimon existente : equipo) {
            if (existente.getNombre().equalsIgnoreCase(digimon.getNombre())) {
                throw new IllegalArgumentException("No puede repetir un Digimon en su equipo.");
            }
        }
        equipo.add(digimon);
    }

    /** @return verdadero cuando ya se eligieron los cuatro Digimon */
    public boolean equipoCompleto() { return equipo.size() == CAPACIDAD; }

    /**
     * Valida el índice y consulta si la ficha todavía puede combatir.
     * @param indice posición del equipo, desde cero
     * @return falso si el índice no existe o el Digimon ya participó
     */
    public boolean estaDisponible(int indice) {
        return indice >= 0 && indice < equipo.size()
                && !utilizados.contains(equipo.get(indice).getNombre().toLowerCase(Locale.ROOT));
    }

    /**
     * Consume una selección para impedir usarla en otra ronda.
     * @param indice posición válida y disponible del equipo
     * @return ficha elegida para la ronda
     */
    public Digimon usarDigimon(int indice) {
        if (!equipoCompleto() || !estaDisponible(indice)) {
            throw new IllegalArgumentException("Debe elegir un Digimon disponible de un equipo completo.");
        }
        Digimon elegido = equipo.get(indice);
        utilizados.add(elegido.getNombre().toLowerCase(Locale.ROOT));
        return elegido;
    }

    /** Incrementa en uno las victorias cuando la partida adjudica una ronda. */
    public void registrarVictoria() { rondasGanadas++; }

    /**
     * Conserva una habilidad que acaba de activarse.
     * @param habilidad habilidad ganada por este entrenador
     * @param ronda ronda de activación
     */
    public void activarEfecto(Digievolucion habilidad, int ronda) {
        efectos.add(new EfectoActivo(habilidad, ronda));
    }

    /**
     * Elimina activaciones cuya última ronda ya pasó.
     * @param ronda ronda que va a resolverse
     */
    public void descartarEfectosVencidos(int ronda) {
        Iterator<EfectoActivo> iterador = efectos.iterator();
        while (iterador.hasNext()) {
            if (iterador.next().getRondaFin() < ronda) {
                iterador.remove();
            }
        }
    }

    /**
     * Acumula únicamente los efectos solicitados que siguen vigentes.
     * @param efecto magnitud que se quiere sumar
     * @param ronda ronda para la que se calcula el total
     * @return suma de potencias válidas, o cero si no existen
     */
    public int sumarEfectos(Efecto efecto, int ronda) {
        Objects.requireNonNull(efecto, "El efecto es obligatorio.");
        int total = 0;
        for (EfectoActivo activo : efectos) {
            if (activo.estaVigente(ronda) && activo.getHabilidad().getEfecto() == efecto) {
                total += activo.getHabilidad().getPotencia();
            }
        }
        return total;
    }
}
