package back_end;

public class Arista {
    private Vertice origen;
    private Vertice destino;
    private double peso;

    public Arista(Vertice origen, Vertice destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getOrigen() { return origen; }
    public Vertice getDestino() { return destino; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    @Override
    public String toString() {
        return origen.getId() + " -> " + destino.getId() + " (Peso: " + peso + ")";
    }
}
