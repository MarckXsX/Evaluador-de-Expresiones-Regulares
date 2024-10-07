import java.util.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String bandera = "s";
        do {
            AutomataFN automataFN = new AutomataFN();
            // Introducción
            System.out.println("Bienvenido al evaluador de autómatas finitos no deterministas con transiciones ε.");
            System.out.println("Ingrese los símbolos del alfabeto, estados, estados finales y transiciones.");

            // Leer símbolos del alfabeto
            Set<Character> simbolos = leerSimbolos(scanner);
            if (simbolos == null) continue; // Reiniciar si hay error

            // Agregar símbolos al autómata
            for (Character simbolo : simbolos) {
                automataFN.agregarAlfabeto(simbolo);
            }

            // Leer estados
            List<Estado> estados = leerEstados(scanner);
            if (estados == null) continue; // Reiniciar si hay error

            // Leer estados finales
            List<String> estadosFinales = leerEstadosFinales(scanner);
            if (estadosFinales == null) continue; // Reiniciar si hay error

            // Crear estados y agregar al autómata
            for (Estado estado : estados) {
                estado.setEsEstadoFinal(estadosFinales.contains(estado.getNombre()));
                automataFN.agregarEstados(estado);
            }

            // Leer transiciones
            List<Transicion> transiciones = leerTransiciones(scanner, automataFN);
            if (transiciones == null) continue; // Reiniciar si hay error

            // Agregar transiciones al autómata
            for (Transicion transicion : transiciones) {
                automataFN.agregarTransiciones(transicion);
            }

            // Se crea una instancia del evaluador que va a recibir al Autómata
            Evaluador evaluador = new Evaluador();

            // Verifica el autómata
            if (evaluador.verficarAutomata(automataFN)) {
                System.out.println("El quíntuplo del Autómata recibido es correcto.\n");
            } else {
                System.out.println("Los parámetros del quíntuplo no son correctos.");
                continue;
            }

            evaluador.detalleEstados();

            // Evaluar múltiples palabras
            String continuar;
            do {
                System.out.print("Ingrese una palabra para evaluar (o 'q' para salir): ");
                String palabra = scanner.nextLine();
                if (palabra.equals("q")) break;

                char resultado = evaluador.Evaluacion(palabra);
                switch (resultado) {
                    case 'e':
                        System.out.println("Error, debido a que no se ha definido el AF que define la e.r");
                        break;
                    case 'p':
                        System.out.println("La palabra: '" + palabra + "' de entrada contiene símbolos que no pertenecen al alfabeto usado por el Autómata.");
                        break;
                    case 's':
                        System.out.println("Palabra: '" + palabra + "' de entrada ha satisfecho la e.r");
                        break;
                    case 'n':
                        System.out.println("Palabra: '" + palabra + "' de entrada no cumplió la e.r");
                        break;
                }

                System.out.print("¿Desea evaluar otra palabra? (s/n): ");
                continuar = scanner.nextLine().trim().toLowerCase();
            } while (continuar.equals("s"));

            System.out.print("¿Desea evaluar otro Autómata? (s/n): ");
            bandera = scanner.nextLine().trim().toLowerCase();
            if (bandera.equals("s")) {
                evaluador.reset();
            }
        } while (bandera.equals("s"));

        scanner.close();
    }

    // Métodos para el ingreso del alfabeto
    private static Set<Character> leerSimbolos(Scanner scanner) {
        System.out.print("Ingrese los símbolos del alfabeto (separados por comas): ");
        String[] simbolosInput = scanner.nextLine().split(",");
        Set<Character> simbolos = new HashSet<>();

        for (String simbolo : simbolosInput) {
            simbolo = simbolo.trim();
            if (simbolo.isEmpty()) {
                System.out.println("No se permiten caracteres vacíos!");
                return null;
            }
            simbolos.add(simbolo.charAt(0));
        }
        return simbolos;
    }

    //Metodo para el ingreso de estados
    private static List<Estado> leerEstados(Scanner scanner) {
        System.out.print("Ingrese los estados (separados por comas): ");
        String[] estadosInput = scanner.nextLine().split(",");
        List<Estado> estados = new ArrayList<>();

        for (String nombre : estadosInput) {
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                System.out.println("No se permiten estados vacíos!");
                return null;
            }
            boolean esInicial = estados.isEmpty(); // Solo el primer estado es inicial
            estados.add(new Estado(nombre, false, esInicial));
        }
        return estados;
    }

    //Metodo para el ingreso de estados finales
    private static List<String> leerEstadosFinales(Scanner scanner) {
        System.out.print("Ingrese los estados finales (separados por comas): ");
        String[] finalesInput = scanner.nextLine().split(",");
        return List.of(finalesInput);
    }

    //metodo para el ingreso de transiciones
    private static List<Transicion> leerTransiciones(Scanner scanner, AutomataFN automataFN) {
        List<Transicion> transiciones = new ArrayList<>();
        System.out.println("Ingrese las transiciones (origen,símbolo,destino), una por línea. Escriba 'fin' para terminar:");

        while (true) {
            String transicionInput = scanner.nextLine().trim();
            if (transicionInput.equals("fin")) {
                break;
            }

            if (transicionInput.isEmpty()) {
                System.out.println("Error en las transiciones!");
                return null;
            }

            String[] partes = transicionInput.split(",");
            if (partes.length != 3) {
                System.out.println("Formato incorrecto para las transiciones.");
                return null;
            }

            String origen = partes[0].trim();
            Character simbolo = partes[1].trim().equals("ε") || partes[1].trim().isEmpty() ? null : partes[1].trim().charAt(0);
            String destino = partes[2].trim();

            Estado estadoOrigen = automataFN.getEstados().stream().filter(e -> e.getNombre().equals(origen)).findFirst().orElse(null);
            Estado estadoDestino = automataFN.getEstados().stream().filter(e -> e.getNombre().equals(destino)).findFirst().orElse(null);

            if (estadoOrigen != null && estadoDestino != null) {
                Transicion nuevaTransicion = new Transicion(simbolo, estadoDestino, estadoOrigen);
                estadoOrigen.agregarTransicion(nuevaTransicion);
                transiciones.add(nuevaTransicion);
            } else {
                System.out.println("Error en el ingreso de transiciones!");
                return null;
            }
        }
        return transiciones;
    }
}
