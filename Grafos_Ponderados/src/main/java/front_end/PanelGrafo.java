package front_end;

import back_end.Arista;
import back_end.Vertice;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class PanelGrafo extends JPanel {
    private List<Vertice> vertices;
    private List<Arista> aristas;

    public PanelGrafo() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Visualización Topológica"));
        this.vertices = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    public void actualizarDatos(List<Vertice> vertices, List<Arista> aristas) {
        this.vertices = vertices;
        this.aristas = aristas;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (vertices.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = Math.min(cx, cy) - 50;

        Map<String, Point> posiciones = new HashMap<>();
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            posiciones.put(vertices.get(i).getId(), new Point(x, y));
        }

       
        g2d.setColor(Color.BLUE);
        for (Arista a : aristas) {
            Point p1 = posiciones.get(a.getOrigen().getId());
            Point p2 = posiciones.get(a.getDestino().getId());

            if (p1 != null && p2 != null) {
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;
                g2d.setColor(Color.RED);
                g2d.drawString(String.valueOf(a.getPeso()), mx, my);
                g2d.setColor(Color.BLUE);
            }
        }

        
        for (Vertice v : vertices) {
            Point p = posiciones.get(v.getId());
            g2d.setColor(new Color(100, 180, 255));
            g2d.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(p.x - 20, p.y - 20, 40, 40);

            FontMetrics fm = g2d.getFontMetrics();
            int tx = p.x - fm.stringWidth(v.getId()) / 2;
            int ty = p.y + fm.getAscent() / 2 - 2;
            g2d.drawString(v.getId(), tx, ty);
        }
    }
}