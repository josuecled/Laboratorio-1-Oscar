import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/** Contiene toda la interacción con el usuario; delega las reglas a los objetos. */
public final class Principal {
    /** Lector compartido para nombres y opciones, sin mezclar nextInt y nextLine. */
    private static final Scanner ENTRADA = new Scanner(System.in, "UTF-8");

    /** Impide crear una instancia de la clase de entrada. */
    private Principal() { }

    /**
     * Registra equipos, solicita decisiones y presenta los resultados.
     * @param args argumentos de consola, no utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("LABORATORIO 1 - BATALLA DIGIMON");
            System.out.println("Dos entrenadores, cuatro Digimon distintos y cuatro rondas.");
            System.out.println("Las habilidades duran la ronda actual y la siguiente para su equipo.");
            System.out.println("Digievolucionar intenta añadir un efecto al poder base; fallar no lo elimina.");
            List<Digimon> catalogo = Catalogo.crear();
            Entrenador primero = registrarEntrenador(1, catalogo);
            Entrenador segundo = registrarEntrenador(2, catalogo);
            Partida partida = new Partida(primero, segundo, new Random());
            while (!partida.haTerminado()) {
                System.out.println("\n=== RONDA " + partida.getRondaActual() + " DE 4 ===");
                int indice1 = elegirDigimon(primero);
                Accion accion1 = elegirAccion(primero, primero.getEquipo().get(indice1));
                int indice2 = elegirDigimon(segundo);
                Accion accion2 = elegirAccion(segundo, segundo.getEquipo().get(indice2));
                ResultadoRonda resultado = partida.jugarRonda(indice1, accion1, indice2, accion2);
                mostrarResultado(resultado, primero, segundo);
            }
            System.out.println("\n=== RESULTADO FINAL ===");
            System.out.println(primero.getNombre() + ": " + primero.getRondasGanadas() + " victorias.");
            System.out.println(segundo.getNombre() + ": " + segundo.getRondasGanadas() + " victorias.");
            int empates = Partida.TOTAL_RONDAS - primero.getRondasGanadas() - segundo.getRondasGanadas();
            System.out.println("Rondas empatadas: " + empates);
            int ganador = partida.getGanadorFinal();
            if (ganador == 0) {
                System.out.println("La partida terminó en empate.");
            } else {
                System.out.println("Ganador: " + (ganador == 1 ? primero.getNombre() : segundo.getNombre()));
            }
        } catch (NoSuchElementException excepcion) {
            System.out.println("\nEntrada finalizada. La partida se cerró antes de completarse.");
        }
    }

    /**
     * Solicita un nombre y cuatro selecciones distintas del catálogo.
     * @param numero número visible del participante
     * @param catalogo fichas disponibles
     * @return entrenador con equipo completo y sin fichas utilizadas
     */
    private static Entrenador registrarEntrenador(int numero, List<Digimon> catalogo) {
        Entrenador entrenador = new Entrenador(leerTexto("Nombre del entrenador " + numero + ": "));
        mostrarCatalogo(catalogo);
        while (!entrenador.equipoCompleto()) {
            int eleccion = leerEntero(entrenador.getNombre() + ", elija Digimon "
                    + (entrenador.getEquipo().size() + 1) + "/4: ", 1, catalogo.size());
            try {
                entrenador.agregarDigimon(catalogo.get(eleccion - 1));
            } catch (IllegalArgumentException excepcion) {
                System.out.println(excepcion.getMessage());
            }
        }
        return entrenador;
    }

    /**
     * Presenta todas las fichas y sus probabilidades antes de elegir equipo.
     * @param catalogo lista completa de opciones
     */
    private static void mostrarCatalogo(List<Digimon> catalogo) {
        System.out.println("\nCATÁLOGO (estadísticas de ejemplo)");
        for (int i = 0; i < catalogo.size(); i++) {
            Digimon digimon = catalogo.get(i);
            Digievolucion habilidad = digimon.getDigievolucion();
            System.out.println((i + 1) + ". " + digimon.getNombre() + " | " + digimon.getTipo()
                    + " | ataque " + digimon.getAtaque() + " | defensa " + digimon.getDefensa()
                    + " | " + habilidad.getNombre() + ": " + habilidad.getEfecto()
                    + " " + habilidad.getPotencia() + " puntos, " + habilidad.getProbabilidad() + "%");
        }
    }

    /**
     * Repite la consulta hasta seleccionar una ficha disponible.
     * @param entrenador participante que debe decidir
     * @return índice desde cero de un Digimon todavía no utilizado
     */
    private static int elegirDigimon(Entrenador entrenador) {
        System.out.println("\nEquipo de " + entrenador.getNombre() + ":");
        List<Digimon> equipo = entrenador.getEquipo();
        for (int i = 0; i < equipo.size(); i++) {
            System.out.println((i + 1) + ". " + equipo.get(i).getNombre()
                    + (entrenador.estaDisponible(i) ? " [disponible]" : " [ya utilizado]"));
        }
        int indice = -1;
        boolean valido = false;
        while (!valido) {
            indice = leerEntero(entrenador.getNombre() + ", elija combatiente: ", 1, equipo.size()) - 1;
            valido = entrenador.estaDisponible(indice);
            if (!valido) {
                System.out.println("Ese Digimon ya participó. Seleccione otro.");
            }
        }
        return indice;
    }

    /**
     * Explica las dos acciones y recibe una opción válida.
     * @param entrenador participante que decide
     * @param digimon ficha elegida para conocer su habilidad
     * @return acción correspondiente a la opción 1 o 2
     */
    private static Accion elegirAccion(Entrenador entrenador, Digimon digimon) {
        Digievolucion habilidad = digimon.getDigievolucion();
        System.out.println("1. Atacar con los efectos que ya estén activos.");
        System.out.println("2. Intentar " + habilidad.getNombre() + " (" + habilidad.getProbabilidad()
                + "%, " + habilidad.getEfecto() + " " + habilidad.getPotencia() + " puntos).");
        int opcion = leerEntero(entrenador.getNombre() + ", elija acción: ", 1, 2);
        return opcion == 1 ? Accion.ATACAR : Accion.DIGIEVOLUCIONAR;
    }

    /**
     * Presenta habilidades, puntuaciones, resultado y marcador acumulado.
     * @param resultado ronda recién resuelta
     * @param primero primer participante
     * @param segundo segundo participante
     */
    private static void mostrarResultado(ResultadoRonda resultado, Entrenador primero, Entrenador segundo) {
        System.out.println("\n" + resultado.getDigimon1().getNombre() + ": "
                + describirAccion(resultado.getAccion1(), resultado.isActivo1(), resultado.getDigimon1()));
        System.out.println(resultado.getDigimon2().getNombre() + ": "
                + describirAccion(resultado.getAccion2(), resultado.isActivo2(), resultado.getDigimon2()));
        System.out.println("AtaqueTotal de " + primero.getNombre() + ": " + resultado.getTotal1());
        System.out.println("AtaqueTotal de " + segundo.getNombre() + ": " + resultado.getTotal2());
        if (resultado.getGanador() == 0) {
            System.out.println("La ronda terminó en empate.");
        } else {
            System.out.println("Ganó la ronda: "
                    + (resultado.getGanador() == 1 ? primero.getNombre() : segundo.getNombre()));
        }
        System.out.println("Marcador: " + primero.getRondasGanadas() + " - " + segundo.getRondasGanadas());
    }

    /**
     * Traduce el resultado de una acción a un texto para la consola.
     * @param accion decisión tomada por el participante
     * @param activa éxito o fracaso del intento
     * @param digimon ficha cuya habilidad se describe
     * @return descripción del ataque normal o del intento de habilidad
     */
    private static String describirAccion(Accion accion, boolean activa, Digimon digimon) {
        if (accion == Accion.ATACAR) {
            return "ataque normal; conserva los efectos vigentes.";
        }
        return digimon.getDigievolucion().getNombre()
                + (activa ? " activada para esta ronda y la siguiente." : " no se activó; mantiene su poder base.");
    }

    /**
     * Repite la lectura hasta recibir un nombre no vacío.
     * @param mensaje indicación que se muestra al usuario
     * @return texto ingresado sin espacios exteriores
     */
    private static String leerTexto(String mensaje) {
        String valor = "";
        while (valor.isEmpty()) {
            System.out.print(mensaje);
            valor = ENTRADA.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Ingrese un texto no vacío.");
            }
        }
        return valor;
    }

    /**
     * Valida números enteros y límites sin utilizar break.
     * @param mensaje indicación que acompaña la lectura
     * @param minimo menor entero permitido
     * @param maximo mayor entero permitido
     * @return entero dentro del intervalo inclusivo solicitado
     */
    private static int leerEntero(String mensaje, int minimo, int maximo) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String texto = ENTRADA.nextLine().trim();
            try {
                valor = Integer.parseInt(texto);
                valido = valor >= minimo && valor <= maximo;
                if (!valido) {
                    System.out.println("Ingrese un número entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException excepcion) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
        return valor;
    }
}
