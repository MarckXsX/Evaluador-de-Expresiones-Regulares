import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Introducción
        System.out.println("Bienvenido al evaluador de autómatas finitos no deterministas con transiciones ε.");
        System.out.println("Ingrese los símbolos del alfabeto, estados, estados finales y transiciones.");

        // Leer símbolos del alfabeto
        System.out.print("Ingrese los símbolos del alfabeto (separados por comas): ");
        String[] simbolosInput = scanner.nextLine().split(",");
        Set<Character> simbolos = new HashSet<>();
        for (String simbolo : simbolosInput) {
            simbolos.add(simbolo.trim().charAt(0));
        }

        // Leer estados
        System.out.print("Ingrese los estados (separados por comas): ");
        String[] estadosInput = scanner.nextLine().split(",");
        List<Estado> estados = new ArrayList<>();

        // Leer estados finales
        System.out.print("Ingrese los estados finales (separados por comas): ");
        String[] finalesInput = scanner.nextLine().split(",");
        List<String> estadosFinales = List.of(finalesInput);

        // Crear estados
        for (String nombre : estadosInput) {
            boolean esFinal = estadosFinales.contains(nombre.trim());
            boolean esInicial = estados.size() == 0; // Solo el primer estado es inicial
            estados.add(new Estado(nombre.trim(), esFinal, esInicial));
        }

        // Leer transiciones
        List<Transicion> transiciones = new ArrayList<>();
        String transicionInput;
        System.out.println("Ingrese las transiciones (origen,símbolo,destino), una por línea. Escriba 'fin' para terminar:");

        while (true) {
            transicionInput = scanner.nextLine().trim();
            if (transicionInput.equals("fin")) {
                break;
            }

            String[] partes = transicionInput.split(",");
            String origen = partes[0].trim();
            Character simbolo = partes[1].trim().equals("ε") ? null : partes[1].trim().charAt(0);
            String destino = partes[2].trim();

            Estado estadoOrigen = estados.stream().filter(e -> e.getNombre().equals(origen)).findFirst().orElse(null);
            Estado estadoDestino = estados.stream().filter(e -> e.getNombre().equals(destino)). findFirst().orElse(null);

            if (estadoOrigen != null && estadoDestino != null) {
                Transicion nuevaTransicion = new Transicion(simbolo, estadoDestino, estadoOrigen);
                estadoOrigen.agregarTransicion(nuevaTransicion);
                transiciones.add(nuevaTransicion);
            }
        }

        // Crear el autómata
        Estado estadoInicial = estados.stream().filter(Estado::esEstadoInicial).findFirst().orElse(null);
        Automata automata = new Automata(estadoInicial);

        // Evaluar múltiples palabras
        String continuar;
        do {
            System.out.print("Ingrese una palabra para evaluar (o 'q' para salir): ");
            String palabra = scanner.nextLine();
            if (palabra.equals("q")) break;

            boolean resultado = automata.evaluar(palabra);
            System.out.println(resultado ? "La palabra satisface la expresión regular." : "La palabra no satisface la expresión regular.");

            System.out.print("¿Desea evaluar otra palabra? (s/n): ");
            continuar = scanner.nextLine().trim().toLowerCase();
        } while (continuar.equals("s"));

        scanner.close();
    }
}
