
package front_end;
import back_end.GrafoPonderado;
import back_end.Vertice;
import javax.swing.JOptionPane;
import java.util.List;


public class InterfazGrafo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InterfazGrafo.class.getName());
    
    private GrafoPonderado grafo;

   
    public InterfazGrafo() {
        initComponents();
        this.grafo = new GrafoPonderado(true);
        this.setLocationRelativeTo(null);

    
        panelVisual.setLayout(new java.awt.BorderLayout());
        panelVisual.setPreferredSize(new java.awt.Dimension(500, 500));
        panelVisual.setMinimumSize(new java.awt.Dimension(400, 400));
    
    
        front_end.PanelGrafo miLienzo = new front_end.PanelGrafo();
        panelVisual.add(miLienzo, java.awt.BorderLayout.CENTER);
    
    
        panelVisual.revalidate();
        panelVisual.repaint();
        this.pack(); 
    }
    
    private void agregarVertice() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tipo = txtTipo.getText().trim();
            String estado = txtEstado.getText().trim();
            String capStr = txtCapacidad.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty() || estado.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos del vértice son obligatorios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double capacidad = Double.parseDouble(capStr);
            if (capacidad < 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número positivo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Vertice v = new Vertice(id, nombre, tipo, estado, capacidad);
            if (grafo.agregarVertice(v)) {
                imprimir("Vértice registrado con éxito: " + v);
                limpiarCamposVertice();
                actualizarVisualizacion();
            } else {
                JOptionPane.showMessageDialog(this, "El ID de vértice '" + id + "' ya existe en el grafo.", "ID Duplicado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un valor numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarVertice() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del vértice a buscar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Vertice v = grafo.buscarVertice(id);
        if (v != null) {
            imprimir("--- VÉRTICE ENCONTRADO ---");
            imprimir("ID: " + v.getId() + " | Nombre: " + v.getNombre());
            imprimir("Tipo: " + v.getTipo() + " | Estado: " + v.getEstado());
            imprimir("Capacidad Operacional: " + v.getCapacidad());
            imprimir("--------------------------");
        } else {
            imprimir("El vértice con ID '" + id + "' no existe en la red.");
        }
    }

    private void eliminarVertice() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del vértice a eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (grafo.eliminarVertice(id)) {
            imprimir("Vértice [" + id + "] y sus conexiones asociadas han sido eliminados.");
            limpiarCamposVertice();
            actualizarVisualizacion();
        } else {
            imprimir("No se encontró ningún vértice con el ID: " + id);
        }
    }

    private void agregarArista() {
        try {
            String orig = txtOrigen.getText().trim();
            String dest = txtDestino.getText().trim();
            String pesoStr = txtPeso.getText().trim();

            if (orig.isEmpty() || dest.isEmpty() || pesoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los campos Origen, Destino y Peso.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double peso = Double.parseDouble(pesoStr);
            if (peso <= 0) {
                JOptionPane.showMessageDialog(this, "El peso debe ser un valor numérico estrictamente positivo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (grafo.agregarArista(orig, dest, peso)) {
                imprimir("Ruta/Arista creada: " + orig + " -> " + dest + " (Peso: " + peso + ")");
                txtOrigen.setText(""); txtDestino.setText(""); txtPeso.setText("");
                actualizarVisualizacion();
            } else {
                JOptionPane.showMessageDialog(this, "Uno o ambos vértices especificados no existen en el grafo.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El peso debe ser un número numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarArista() {
        String orig = txtOrigen.getText().trim();
        String dest = txtDestino.getText().trim();

        if (orig.isEmpty() || dest.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique el ID de origen y de destino.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (grafo.eliminarArista(orig, dest)) {
            imprimir("Ruta/Arista entre " + orig + " y " + dest + " eliminada.");
            txtOrigen.setText(""); txtDestino.setText("");
            actualizarVisualizacion();
        } else {
            imprimir("No existe ninguna arista entre " + orig + " y " + dest + ".");
        }
    }

    private void ejecutarBFS() {
        String inicio = txtId.getText().trim();
        if (inicio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID inicial en el campo 'ID Nodo' para iniciar el recorrido BFS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<String> res = grafo.recorridoBFS(inicio);
        if (res.isEmpty()) {
            imprimir("No fue posible ejecutar BFS. Verifique que el vértice origen exista.");
        } else {
            imprimir("Recorrido en Anchura (BFS) desde [" + inicio + "]: " + String.join(" -> ", res));
        }
    }

    private void ejecutarDFS() {
        String inicio = txtId.getText().trim();
        if (inicio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID inicial en el campo 'ID Nodo' para iniciar el recorrido DFS.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<String> res = grafo.recorridoDFS(inicio);
        if (res.isEmpty()) {
            imprimir("No fue posible ejecutar DFS. Verifique que el vértice origen exista.");
        } else {
            imprimir("Recorrido en Profundidad (DFS) desde [" + inicio + "]: " + String.join(" -> ", res));
        }
    }

    private void mostrarMatriz() {
        imprimir("=== MATRIZ DE ADYACENCIA ===");
        imprimir(grafo.obtenerMatrizAdyacencia());
    }

    private void evaluarConectividad() {
        imprimir("=== EVALUACIÓN DE CONECTIVIDAD ===");
        imprimir(grafo.evaluarConectividad());
    }

    private void destruirGrafo() {
        int r = JOptionPane.showConfirmDialog(this, "¿Está seguro de destruir todo el grafo?", "Confirmación de Eliminación", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            grafo.destruirGrafo();
            txtConsola.setText("");
            imprimir("El grafo ha sido destruido exitosamente. Estructura vacía.");
            actualizarVisualizacion();
        }
    }

    private void actualizarVisualizacion() {
        
        if (panelVisual.getComponentCount() > 0 && panelVisual.getComponent(0) instanceof front_end.PanelGrafo) {
        front_end.PanelGrafo miPanel = (front_end.PanelGrafo) panelVisual.getComponent(0);
        miPanel.actualizarDatos(grafo.getVertices(), grafo.getAristas());
    }
    }

    private void imprimir(String msj) {
        txtConsola.append(msj + "\n");
    }

    private void limpiarCamposVertice() {
        txtId.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txtEstado.setText("");
        txtCapacidad.setText("");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTipo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCapacidad = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnBuscarVertice = new javax.swing.JButton();
        btnAgregarVertice = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPeso = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        btnAgregarArista = new javax.swing.JButton();
        btnEliminarArista = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        btnEliminarVertice = new javax.swing.JButton();
        btnDFS = new javax.swing.JButton();
        btnConectividad = new javax.swing.JButton();
        btnBFS = new javax.swing.JButton();
        btnMatriz = new javax.swing.JButton();
        btnDestruir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConsola = new javax.swing.JTextArea();
        panelVisual = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Vertices");

        jLabel2.setText("ID nodo");

        txtId.addActionListener(this::txtIdActionPerformed);

        jLabel3.setText("Nombre");

        jLabel4.setText("Tipo");

        jLabel5.setText("Estado");

        jLabel6.setText("Capacidad");

        btnBuscarVertice.setText("Buscar Vertice");
        btnBuscarVertice.addActionListener(this::btnBuscarVerticeActionPerformed);

        btnAgregarVertice.setText("Agregar Vertice");
        btnAgregarVertice.addActionListener(this::btnAgregarVerticeActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBuscarVertice)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarVertice)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnBuscarVertice)
                .addGap(42, 42, 42)
                .addComponent(btnAgregarVertice)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jLabel7.setText("Aristas");

        jLabel8.setText("ID Origen");

        jLabel9.setText("ID Destino");

        jLabel10.setText("Peso");

        btnAgregarArista.setText("Agregar Arista");
        btnAgregarArista.addActionListener(this::btnAgregarAristaActionPerformed);

        btnEliminarArista.setText("Eliminar Arista");
        btnEliminarArista.addActionListener(this::btnEliminarAristaActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarArista)
                .addGap(16, 16, 16))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEliminarArista)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnAgregarArista)
                .addGap(29, 29, 29)
                .addComponent(btnEliminarArista)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel11.setText("Acciones");

        btnEliminarVertice.setText("Eliminar Vertice");
        btnEliminarVertice.addActionListener(this::btnEliminarVerticeActionPerformed);

        btnDFS.setText("Recorrido DFS");
        btnDFS.addActionListener(this::btnDFSActionPerformed);

        btnConectividad.setText("Conectividad");
        btnConectividad.addActionListener(this::btnConectividadActionPerformed);

        btnBFS.setText("Recorrido BFS");
        btnBFS.addActionListener(this::btnBFSActionPerformed);

        btnMatriz.setText("Matriz Ady");
        btnMatriz.addActionListener(this::btnMatrizActionPerformed);

        btnDestruir.setText("Destruir Grafo");
        btnDestruir.addActionListener(this::btnDestruirActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jLabel11))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnConectividad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDestruir))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEliminarVertice)
                                    .addComponent(btnDFS))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnMatriz)
                                    .addComponent(btnBFS))))))
                .addGap(46, 46, 46))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarVertice)
                    .addComponent(btnBFS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDFS)
                    .addComponent(btnMatriz))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConectividad)
                    .addComponent(btnDestruir))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                            .addComponent(txtNombre)
                                            .addComponent(txtTipo)
                                            .addComponent(txtEstado)
                                            .addComponent(txtCapacidad)))
                                    .addComponent(txtOrigen, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                    .addComponent(txtDestino)
                                    .addComponent(txtPeso))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(6, 6, 6))
                    .addComponent(txtCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtConsola.setEditable(false);
        txtConsola.setColumns(20);
        txtConsola.setRows(5);
        jScrollPane1.setViewportView(txtConsola);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelVisualLayout = new javax.swing.GroupLayout(panelVisual);
        panelVisual.setLayout(panelVisualLayout);
        panelVisualLayout.setHorizontalGroup(
            panelVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );
        panelVisualLayout.setVerticalGroup(
            panelVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panelVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnBuscarVerticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVerticeActionPerformed
    buscarVertice();
    }//GEN-LAST:event_btnBuscarVerticeActionPerformed

    private void btnAgregarVerticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarVerticeActionPerformed
    agregarVertice();
    }//GEN-LAST:event_btnAgregarVerticeActionPerformed

    private void btnAgregarAristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAristaActionPerformed
    agregarArista();
    }//GEN-LAST:event_btnAgregarAristaActionPerformed

    private void btnEliminarAristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAristaActionPerformed
    eliminarArista();
    }//GEN-LAST:event_btnEliminarAristaActionPerformed

    private void btnEliminarVerticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVerticeActionPerformed
     eliminarVertice();
    }//GEN-LAST:event_btnEliminarVerticeActionPerformed

    private void btnDFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDFSActionPerformed
     ejecutarDFS();
    }//GEN-LAST:event_btnDFSActionPerformed

    private void btnConectividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConectividadActionPerformed
     evaluarConectividad();
    }//GEN-LAST:event_btnConectividadActionPerformed

    private void btnBFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBFSActionPerformed
     ejecutarBFS();
    }//GEN-LAST:event_btnBFSActionPerformed

    private void btnMatrizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatrizActionPerformed
     mostrarMatriz();
    }//GEN-LAST:event_btnMatrizActionPerformed

    private void btnDestruirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDestruirActionPerformed
     destruirGrafo();
    }//GEN-LAST:event_btnDestruirActionPerformed

    
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new InterfazGrafo().setVisible(true);
        });
       
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(() -> new InterfazGrafo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarArista;
    private javax.swing.JButton btnAgregarVertice;
    private javax.swing.JButton btnBFS;
    private javax.swing.JButton btnBuscarVertice;
    private javax.swing.JButton btnConectividad;
    private javax.swing.JButton btnDFS;
    private javax.swing.JButton btnDestruir;
    private javax.swing.JButton btnEliminarArista;
    private javax.swing.JButton btnEliminarVertice;
    private javax.swing.JButton btnMatriz;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelVisual;
    private javax.swing.JTextField txtCapacidad;
    private javax.swing.JTextArea txtConsola;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtOrigen;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables
}
