package back_end;

public class Vertice {
    private String id;
    private String nombre;
    private String tipo;
    private String estado;
    private double capacidad;

    public Vertice(String id, String nombre, String tipo, String estado, double capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.capacidad = capacidad;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getCapacidad() { return capacidad; }
    public void setCapacidad(double capacidad) { this.capacidad = capacidad; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %s) - Cap: %.2f", id, nombre, tipo, estado, capacidad);
    }
}
