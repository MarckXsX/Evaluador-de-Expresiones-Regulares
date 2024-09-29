import java.util.ArrayList;
import java.util.List;

public class Estado {
    private String nombre;
    private boolean esEstadoFinal;
    private boolean esEstadoInicial;
    private List<Transicion> transiciones;

    public Estado(String nombre, boolean esEstadoFinal, boolean esEstadoInicial) {
        this.nombre = nombre;
        this.esEstadoFinal = esEstadoFinal;
        this.esEstadoInicial = esEstadoInicial;
        this.transiciones = new ArrayList<>();
    }

    public void agregarTransicion(Transicion transicion) {
        transiciones.add(transicion);
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public boolean esEstadoFinal() {
        return esEstadoFinal;
    }

    public boolean esEstadoInicial() {
        return esEstadoInicial;
    }

    public String getNombre() {
        return nombre;
    }
}

