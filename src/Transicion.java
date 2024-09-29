public class Transicion {
    private Character simbolo;
    private Estado estadoDestino;
    private Estado estadoOrigen;

    public Transicion(Character simbolo, Estado estadoDestino, Estado estadoOrigen) {
        this.simbolo = simbolo;
        this.estadoDestino = estadoDestino;
        this.estadoOrigen = estadoOrigen;
    }

    public Character getSimbolo() {
        return simbolo;
    }

    public Estado getEstadoDestino() {
        return estadoDestino;
    }

    public Estado getEstadoOrigen() {
        return estadoOrigen;
    }
}
