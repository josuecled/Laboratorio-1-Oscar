import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Proporciona ocho fichas de ejemplo; las estadísticas pertenecen a este juego. */
public final class Catalogo {
    /** Impide instanciar una clase que solo ofrece la fábrica estática. */
    private Catalogo() { }

    /** @return lista de solo lectura con ocho Digimon y sus habilidades */
    public static List<Digimon> crear() {
        return Collections.unmodifiableList(Arrays.asList(
            new Digimon("Agumon", Tipo.FUEGO, 60, 30,
                new Digievolucion("Adult", Efecto.ATAQUE, 15, 30)),
            new Digimon("Guilmon", Tipo.FUEGO, 65, 25,
                new Digievolucion("Ultimate", Efecto.DANIO_DIRECTO, 10, 35)),
            new Digimon("Gomamon", Tipo.AGUA, 55, 40,
                new Digievolucion("Mega", Efecto.DEFENSA, 20, 30)),
            new Digimon("Betamon", Tipo.AGUA, 58, 35,
                new Digievolucion("Adult", Efecto.ATAQUE, 15, 35)),
            new Digimon("Palmon", Tipo.PLANTA, 52, 42,
                new Digievolucion("Mega", Efecto.DEFENSA, 20, 40)),
            new Digimon("Floramon", Tipo.PLANTA, 57, 33,
                new Digievolucion("Ultimate", Efecto.DANIO_DIRECTO, 10, 35)),
            new Digimon("Tentomon", Tipo.ELECTRICO, 59, 34,
                new Digievolucion("Adult", Efecto.ATAQUE, 15, 40)),
            new Digimon("Elecmon", Tipo.ELECTRICO, 62, 28,
                new Digievolucion("Ultimate", Efecto.DANIO_DIRECTO, 10, 30))
        ));
    }
}
