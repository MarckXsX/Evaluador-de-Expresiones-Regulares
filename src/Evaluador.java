import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Evaluador {
    private Estado estadoInicial;

    private AutomataFN automataFN;

    public Evaluador() {
        this.automataFN = null;
    }

    public boolean verficarAutomata(AutomataFN automataFN){
        if(automataFN.getInitialEstado() == null || automataFN.getFinalStates().isEmpty() || automataFN.getTransicions().isEmpty())return false;
        this.automataFN = automataFN;
        return true;
    }

    public char Evaluacion(String palabra){

        //Si el automata no esta definido
        if(automataFN == null){
            return 'e';
        }

        //si la palabra contiene simbolos que no estan en el alfabeto
        for (char caracter: palabra.toCharArray()){
            if(!automataFN.getAlfabeto().contains(caracter)){
                return 'p';
            }
        }

        Set<Estado> estados = new HashSet<>();
        estados.add(automataFN.getInitialEstado());

        //Obtengo los estados que puedo acceder por una transicion vacia'ε' desde el estado inicial.
        estados = transicionesE(estados);

        //Recorre los estados consumiendo los caracteres o transiciones vacias
        for(char caracter: palabra.toCharArray()){
            estados = move(estados, caracter);
            estados = transicionesE(estados);
        }

        //Verifica los estados que recorrio la palabra y verifica si es un estado final
        for(Estado estado: estados){
            if(estado.esEstadoFinal()){
                return 's';
            }
        }
        return 'n';
    }

    public void reset(){
        this.automataFN = null;
    }

    public void detalleEstados(){
        System.out.println(" Mis estados:");
        for (Estado estado: automataFN.getEstados()){
            if(estado.esEstadoInicial() && estado.esEstadoFinal()){
                System.out.println("*>" + estado.getNombre());
            }
            else if(estado.esEstadoInicial()){
                System.out.println("> " + estado.getNombre());
            }
            else if(estado.esEstadoFinal()){
                System.out.println("* " + estado.getNombre());
            }
            else {
                System.out.println("  " +estado.getNombre());
            }
        }
    }

    private Set<Estado> transicionesE(Set<Estado> estados){

        //Posibles estados alcanzados por 'ε'
        Set<Estado> estadosAlcanzados = new HashSet<>(estados);

        //pila para recorrer los estados
        Stack<Estado> pila = new Stack<>();
        pila.addAll(estados);

        while (!pila.isEmpty()){
            Estado estado = pila.pop();
            for(Transicion t: estado.getTransiciones()){
                if(t.getSimbolo() == null ){
                    if(!estadosAlcanzados.contains(t.getEstadoDestino())){
                        estadosAlcanzados.add(t.getEstadoDestino());
                        pila.push(t.getEstadoDestino());
                    }
                }
            }
        }
        return estadosAlcanzados;
    }

    private Set<Estado> move(Set<Estado> estados, char simbolo){
        //Almacena los nodos a los que realizo la transicion con el caracter
        Set<Estado> result = new HashSet<>();

        for (Estado estado: estados){
            for(Transicion t: estado.getTransiciones()){
                if(t.getSimbolo() != null){
                    if(t.getSimbolo() == simbolo) {
                        result.add(t.getEstadoDestino());
                    }
                }
            }
        }
        return result;
    }

}
