package back_end;

import java.util.*;

public class GrafoPonderado {
    private Map<String, Vertice> vertices;
    private List<Arista> aristas;
    private boolean esDirigido;

    public GrafoPonderado(boolean esDirigido) {
        this.vertices = new LinkedHashMap<>();
        this.aristas = new ArrayList<>();
        this.esDirigido = esDirigido;
    }

    public boolean agregarVertice(Vertice v) {
        if (v == null || v.getId() == null || v.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del vértice no puede estar vacío.");
        }
        if (vertices.containsKey(v.getId())) {
            return false;
        }
        vertices.put(v.getId(), v);
        return true;
    }

    public boolean agregarArista(String idOrigen, String idDestino, double peso) {
        if (peso <= 0 || Double.isNaN(peso) || Double.isInfinite(peso)) {
            throw new IllegalArgumentException("El peso debe ser un número finito y estrictamente positivo.");
        }
        Vertice origen = vertices.get(idOrigen);
        Vertice destino = vertices.get(idDestino);

        if (origen == null || destino == null) {
            return false;
        }

        aristas.removeIf(a -> a.getOrigen().getId().equals(idOrigen) && a.getDestino().getId().equals(idDestino));
        aristas.add(new Arista(origen, destino, peso));

        if (!esDirigido) {
            aristas.removeIf(a -> a.getOrigen().getId().equals(idDestino) && a.getDestino().getId().equals(idOrigen));
            aristas.add(new Arista(destino, origen, peso));
        }
        return true;
    }

    public boolean eliminarVertice(String id) {
        if (!vertices.containsKey(id)) return false;

        aristas.removeIf(a -> a.getOrigen().getId().equals(id) || a.getDestino().getId().equals(id));
        vertices.remove(id);
        return true;
    }

    public boolean eliminarArista(String idOrigen, String idDestino) {
        boolean eliminada = aristas.removeIf(a -> a.getOrigen().getId().equals(idOrigen) && a.getDestino().getId().equals(idDestino));
        if (!esDirigido) {
            aristas.removeIf(a -> a.getOrigen().getId().equals(idDestino) && a.getDestino().getId().equals(idOrigen));
        }
        return eliminada;
    }

    public Vertice buscarVertice(String id) {
        return vertices.get(id);
    }

    public List<Vertice> getVertices() {
        return new ArrayList<>(vertices.values());
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    public void destruirGrafo() {
        vertices.clear();
        aristas.clear();
    }

    public List<String> recorridoBFS(String idInicio) {
        if (!vertices.containsKey(idInicio)) return Collections.emptyList();

        List<String> resultado = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();

        cola.add(idInicio);
        visitados.add(idInicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            resultado.add(actual);

            for (Arista a : aristas) {
                if (a.getOrigen().getId().equals(actual)) {
                    String vecino = a.getDestino().getId();
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                    }
                }
            }
        }
        return resultado;
    }

    public List<String> recorridoDFS(String idInicio) {
        if (!vertices.containsKey(idInicio)) return Collections.emptyList();

        List<String> resultado = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        dfsAux(idInicio, visitados, resultado);
        return resultado;
    }

    private void dfsAux(String idActual, Set<String> visitados, List<String> resultado) {
        visitados.add(idActual);
        resultado.add(idActual);

        for (Arista a : aristas) {
            if (a.getOrigen().getId().equals(idActual)) {
                String vecino = a.getDestino().getId();
                if (!visitados.contains(vecino)) {
                    dfsAux(vecino, visitados, resultado);
                }
            }
        }
    }

    public String obtenerMatrizAdyacencia() {
        if (vertices.isEmpty()) return "El grafo está vacío.";

        List<String> keys = new ArrayList<>(vertices.keySet());
        int n = keys.size();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-10s", ""));
        for (String k : keys) {
            sb.append(String.format("%-10s", k));
        }
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            String orig = keys.get(i);
            sb.append(String.format("%-10s", orig));
            for (int j = 0; j < n; j++) {
                String dest = keys.get(j);
                if (orig.equals(dest)) {
                    sb.append(String.format("%-10s", "0.0"));
                } else {
                    double peso = getPesoEntre(orig, dest);
                    if (peso > 0) {
                        sb.append(String.format("%-10.1f", peso));
                    } else {
                        sb.append(String.format("%-10s", "∞"));
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private double getPesoEntre(String orig, String dest) {
        for (Arista a : aristas) {
            if (a.getOrigen().getId().equals(orig) && a.getDestino().getId().equals(dest)) {
                return a.getPeso();
            }
        }
        return -1;
    }

    public String evaluarConectividad() {
        if (vertices.isEmpty()) return "El grafo está vacío.";

        int n = vertices.size();
        boolean fuertementeConectado = true;

        for (String v : vertices.keySet()) {
            if (recorridoBFS(v).size() < n) {
                fuertementeConectado = false;
                break;
            }
        }

        if (fuertementeConectado) {
            return "El grafo está FUERTEMENTE CONECTADO (Existe camino entre todos los pares de vértices).";
        }

        Set<String> visitadosDebil = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        String inicio = vertices.keySet().iterator().next();
        cola.add(inicio);
        visitadosDebil.add(inicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            for (Arista a : aristas) {
                String u = a.getOrigen().getId();
                String v = a.getDestino().getId();
                String vecino = null;
                if (u.equals(actual)) vecino = v;
                else if (v.equals(actual)) vecino = u;

                if (vecino != null && !visitadosDebil.contains(vecino)) {
                    visitadosDebil.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        if (visitadosDebil.size() == n) {
            return "El grafo está DÉBILMENTE CONECTADO (Conectado si se omiten las direcciones de las aristas).";
        } else {
            return "El grafo está DESCONECTADO (Existen componentes o vértices aislados).";
        }
    }
}
