import java.util.Objects;
import java.util.Random;

/** Define una habilidad inmutable, su potencia y su probabilidad. */
public final class Digievolucion {
    /** Nombre de la habilidad que se presenta al jugador. */
    private final String nombre;
    /** Magnitud que aumenta o reducción que aplica al rival. */
    private final Efecto efecto;
    /** Cantidad positiva de puntos que aporta el efecto. */
    private final int potencia;
    /** Porcentaje entero de activación, entre 0 y 100 inclusive. */
    private final int probabilidad;

    /**
     * Construye una habilidad y valida sus datos.
     * @param nombre nombre no vacío
     * @param efecto clase de modificación
     * @param potencia puntos positivos del efecto
     * @param probabilidad porcentaje de 0 a 100
     */
    public Digievolucion(String nombre, Efecto efecto, int potencia, int probabilidad) {
        if (nombre == null || nombre.trim().isEmpty() || potencia <= 0
                || probabilidad < 0 || probabilidad > 100) {
            throw new IllegalArgumentException("Datos de digievolución inválidos.");
        }
        this.nombre = nombre.trim();
        this.efecto = Objects.requireNonNull(efecto, "El efecto es obligatorio.");
        this.potencia = potencia;
        this.probabilidad = probabilidad;
    }

    /** @return nombre de la habilidad, sin modificarla */
    public String getNombre() { return nombre; }

    /** @return magnitud que modifica la habilidad */
    public Efecto getEfecto() { return efecto; }

    /** @return puntos que aporta o resta al oponente */
    public int getPotencia() { return potencia; }

    /** @return porcentaje de activación */
    public int getProbabilidad() { return probabilidad; }

    /**
     * Realiza un intento independiente con exactamente p resultados favorables de 100.
     * @param azar generador compartido por la partida
     * @return verdadero cuando un entero de 0 a 99 es menor que la probabilidad
     */
    public boolean intentarActivar(Random azar) {
        Objects.requireNonNull(azar, "El generador es obligatorio.");
        return azar.nextInt(100) < probabilidad;
    }
}
