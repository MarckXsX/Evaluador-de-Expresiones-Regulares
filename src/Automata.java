import java.util.List;

public class Automata {
    private Estado estadoInicial;

    public Automata(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public boolean evaluar(String palabra) {
        return evaluarDesdeEstado(estadoInicial, palabra, 0);
    }

    private boolean evaluarDesdeEstado(Estado estadoActual, String palabra, int posicion) {
        // Si se ha llegado al final de la palabra
        if (posicion == palabra.length()) {
            // Devuelve true solo si el estado actual es final
            return estadoActual.esEstadoFinal();
        }

        // Itera a través de las transiciones del estado actual
        for (Transicion transicion : estadoActual.getTransiciones()) {
            // Comprueba si la transición es ε o si coincide con el carácter actual
            if (transicion.getSimbolo() == 'ε' || transicion.getSimbolo().equals(palabra.charAt(posicion))) {
                // Llama recursivamente a la función
                if (evaluarDesdeEstado(transicion.getEstadoDestino(), palabra, posicion + (transicion.getSimbolo() == 'ε' ? 0 : 1))) {
                    return true; // Si se encuentra un camino válido, devuelve true
                }
            }
        }

        return false; // Si no se encontró ningún camino válido, devuelve false
    }


    private boolean hayTransiciones(Estado estado) {
        return !estado.getTransiciones().isEmpty();
    }
}

