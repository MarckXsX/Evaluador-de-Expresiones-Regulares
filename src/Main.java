import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al evaluador de autómatas finitos no deterministas con transiciones ε.");
        System.out.println("Por favor, siga las instrucciones a continuación:");

        // Leer los símbolos del alfabeto
        System.out.print("Ingrese los símbolos del alfabeto (separados por comas): ");
        String[] simbolosInput = scanner.nextLine().split(",");
        Set<Character> simbolos = new HashSet<>();
        for (String simbolo : simbolosInput) {
            simbolos.add(simbolo.trim().charAt(0));
        }

        // Leer los estados
        System.out.print("Ingrese los estados (separados por comas, el primer estado será el inicial): ");
        String[] estadosInput = scanner.nextLine().split(",");
        List<Estado> estados = new ArrayList<>();

        // Leer los estados finales
        System.out.print("Ingrese los estados finales (separados por comas): ");
        String[] finalesInput = scanner.nextLine().split(",");
        List<String> estadosFinales = List.of(finalesInput);

        // Crear los estados
        for (String nombre : estadosInput) {
            boolean esFinal = estadosFinales.contains(nombre.trim());
            boolean esInicial = estados.size() == 0;
            estados.add(new Estado(nombre.trim(), esFinal, esInicial));
        }

        // Leer las transiciones
        List<Transicion> transiciones = new ArrayList<>();
        String transicionInput;
        System.out.println("Ingrese las transiciones (origen,símbolo,destino), una por línea. Escriba 'fin' para terminar:");

        while (true) {
            transicionInput = scanner.nextLine().trim();
            if (transicionInput.equals("fin")) {
                break;
            }

            String[] partes = transicionInput.split(",");
            if (partes.length != 3) {
                System.out.println("Error: La transición '" + transicionInput + "' no tiene el formato correcto.");
                continue;
            }

            String origen = partes[0].trim();
            Character simbolo = partes[1].trim().equals("ε") ? 'ε' : partes[1].trim().charAt(0);
            String destino = partes[2].trim();

            Estado estadoOrigen = estados.stream().filter(e -> e.getNombre().equals(origen)).findFirst().orElse(null);
            Estado estadoDestino = estados.stream().filter(e -> e.getNombre().equals(destino)).findFirst().orElse(null);

            if (estadoOrigen == null || estadoDestino == null) {
                System.out.println("Error: Estado de origen o destino no definido.");
                continue;
            }

            // Agregar la transición al estado de origen
            Transicion nuevaTransicion = new Transicion(simbolo, estadoDestino, estadoOrigen);
            estadoOrigen.agregarTransicion(nuevaTransicion);
            transiciones.add(nuevaTransicion);
        }

        // Determinar el estado inicial
        Estado estadoInicial = estados.stream().filter(Estado::esEstadoInicial).findFirst().orElse(null);
        Automata automata = new Automata(estadoInicial);

        // Evaluar palabras
        String continuar;
        do {
            System.out.print("Ingrese una palabra para evaluar: ");
            String palabra = scanner.nextLine();
            boolean resultado = automata.evaluar(palabra);
            System.out.println(resultado ? "La palabra satisface la expresión regular." : "La palabra no satisface la expresión regular.");

            System.out.print("¿Desea evaluar otra palabra? (s/n): ");
            continuar = scanner.nextLine().trim().toLowerCase();
        } while (continuar.equals("s"));

        scanner.close();
    }
}
