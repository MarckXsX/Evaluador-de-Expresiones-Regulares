import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutomataFN {

    private List<Estado> estados;
    private List<Transicion> transicions;
    private Estado initialEstado;
    private List<Estado> finalStates;
    private Set<Character> alfabeto;

    public AutomataFN() {
        estados = new ArrayList<>();
        transicions = new ArrayList<>();
        finalStates = new ArrayList<>();
        alfabeto = new HashSet<>();
    }

    public boolean agregarEstados(Estado estado){
        if(estados.contains(estado)) return false;
        estados.add(estado);
        if(estado.esEstadoInicial()){
            if(initialEstado != null) return false;
            initialEstado = estado;
        }
        if(estado.esEstadoFinal()){
            finalStates.add(estado);
        }
        return true;

    }

    public void agregarTransiciones(Transicion transicion){
        transicions.add(transicion);
    }

    public void agregarAlfabeto(Character caracter) {
       alfabeto.add(caracter);
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public List<Transicion> getTransicions() {
        return transicions;
    }

    public Estado getInitialEstado() {
        return initialEstado;
    }

    public List<Estado> getFinalStates() {
        return finalStates;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }
}
