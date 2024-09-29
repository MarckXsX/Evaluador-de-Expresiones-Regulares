import java.util.HashSet;
import java.util.Set;

public class Automata {
    private Estado estadoInicial;

    public Automata(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public boolean evaluar(String palabra) {
        Set<Estado> estadosActuales = new HashSet<>();
        estadosActuales.add(estadoInicial);
        return evaluarDesdeEstados(estadosActuales, palabra, 0);
    }

    private boolean evaluarDesdeEstados(Set<Estado> estadosActuales, String palabra, int posicion) {
        Set<Estado> nuevosEstados = new HashSet<>();

        // Proceso de transición para cada estado actual
        for (Estado estado : estadosActuales) {
            // Evaluar transiciones ε
            if (hayTransicionesEpsilon(estado)) {
                nuevosEstados.addAll(obtenerEstadosEpsilon(estado));
            }

            if (posicion < palabra.length()) {
                for (Transicion transicion : estado.getTransiciones()) {
                    if (transicion.getSimbolo() == null) continue; // Ignorar transiciones ε

                    if (transicion.getSimbolo().equals(palabra.charAt(posicion))) {
                        nuevosEstados.add(transicion.getEstadoDestino());
                    }
                }
            }
        }

        // Verificar si hay un estado final alcanzado
        boolean hayEstadoFinal = nuevosEstados.stream().anyMatch(Estado::esEstadoFinal);

        // Si se ha llegado al final de la palabra
        if (posicion == palabra.length()) {
            return hayEstadoFinal;
        }

        // Evaluar desde los nuevos estados en la siguiente posición
        return evaluarDesdeEstados(nuevosEstados, palabra, posicion + 1) || hayEstadoFinal;
    }

    private boolean hayTransicionesEpsilon(Estado estado) {
        return estado.getTransiciones().stream().anyMatch(transicion -> transicion.getSimbolo() == null);
    }

    private Set<Estado> obtenerEstadosEpsilon(Estado estado) {
        Set<Estado> estadosEpsilon = new HashSet<>();
        for (Transicion transicion : estado.getTransiciones()) {
            if (transicion.getSimbolo() == null) {
                estadosEpsilon.add(transicion.getEstadoDestino());
                estadosEpsilon.addAll(obtenerEstadosEpsilon(transicion.getEstadoDestino())); // Transiciones ε recursivas
            }
        }
        return estadosEpsilon;
    }
}
