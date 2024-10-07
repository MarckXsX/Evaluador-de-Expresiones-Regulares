import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String bandera;
        outerLoop:
        do {

                AutomataFN automataFN = new AutomataFN();
                // Introducción
                System.out.println("Bienvenido al evaluador de autómatas finitos no deterministas con transiciones ε.");
                System.out.println("Ingrese los símbolos del alfabeto, estados, estados finales y transiciones.");

                // Leer símbolos del alfabeto
                System.out.print("Ingrese los símbolos del alfabeto (separados por comas): ");
                String[] simbolosInput = scanner.nextLine().split(",");
                Set<Character> simbolos = new HashSet<>();
                for (String simbolo : simbolosInput) {
                    //simbolos.add(simbolo.trim().charAt(0));
                    if(simbolo.equals("")){
                        System.out.println("No se permiten caracteres vacios!");
                        break outerLoop;
                    }
                    automataFN.agregarAlfabeto(simbolo.trim().charAt(0));
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
                    if(nombre.equals("")){
                        System.out.println("No se permiten estados vacios!");
                        break outerLoop;
                    }
                    boolean esFinal = estadosFinales.contains(nombre.trim());
                    boolean esInicial = estados.size() == 0; // Solo el primer estado es inicial
                    estados.add(new Estado(nombre.trim(), esFinal, esInicial)); //Se alamacenan en la lista
                    automataFN.agregarEstados(new Estado(nombre.trim(), esFinal, esInicial)); //se almacenan en el objeto Automata
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
                    if (transicionInput.isEmpty()){
                        System.out.println("Error en las transiciones!");
                        break outerLoop;
                    }

                    String[] partes = transicionInput.split(",");
                    String origen = partes[0].trim().isEmpty() ? null : partes[0].trim();
                    Character simbolo = partes[1].trim().equals("ε") || partes[1].trim().equals("") ? null : partes[1].trim().charAt(0);
                    String destino = partes[2].trim().isEmpty() ? null : partes[2].trim();

                    Estado estadoOrigen = automataFN.getEstados().stream().filter(e -> e.getNombre().equals(origen)).findFirst().orElse(null);
                    Estado estadoDestino = automataFN.getEstados().stream().filter(e -> e.getNombre().equals(destino)).findFirst().orElse(null);

                    if (estadoOrigen != null && estadoDestino != null) {
                        Transicion nuevaTransicion = new Transicion(simbolo, estadoDestino, estadoOrigen);
                        estadoOrigen.agregarTransicion(nuevaTransicion);
                        //transiciones.add(nuevaTransicion);
                        automataFN.agregarTransiciones(nuevaTransicion);
                    }
                    else{
                        System.out.println("Error en el ingreso de transiciones!");
                        break outerLoop;
                    }
                }

            //Se crea una instancia del evaluador que va a recibir al Automata
            Evaluador evaluador = new Evaluador();

            //Verifica el automata
            if (evaluador.verficarAutomata(automataFN)) {
                System.out.println("El quintuplo del Automata recibido es correcto.\n");
            } else {
                System.out.println("Los parametros del quintuplo no es correcto.");
                break;
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
                        System.out.println("La palabra: '" + palabra + "' de entrada contiene simbolos que no pertenecen al alfabeto usado por el Automata.");
                        break;
                    case 's':
                        System.out.println("Palabra: '" + palabra + "' de entrada ha satisfecho la e.r");
                        break;
                    case 'n':
                        System.out.println("Palabra: '" + palabra + "' de entrada no cumplio la e.r");
                        break;
                }

                System.out.print("¿Desea evaluar otra palabra? (s/n): ");
                continuar = scanner.nextLine().trim().toLowerCase();
            } while (continuar.equals("s"));
            System.out.print("¿Desea evaluar otro Automata? (s/n): ");
            bandera = scanner.nextLine().trim().toLowerCase();
            if(bandera.equals("s")){
                evaluador.reset();
            }
        }while (bandera.equals("s"));
        scanner.close();
    }
}
