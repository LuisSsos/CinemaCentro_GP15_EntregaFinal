package Vistas;

import Persistencia.DetalleTicketData;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Lucas
 */
public class VistaEntradas extends javax.swing.JInternalFrame {
    
    private final Persistencia.CompradorData compradorDao = new Persistencia.CompradorData();
    private final Persistencia.TicketCompraData ticketDao = new Persistencia.TicketCompraData();
    private final Persistencia.DetalleTicketData detalleDao = new Persistencia.DetalleTicketData();

    private javax.swing.table.DefaultTableModel modelo;
    private Modelo.Comprador compradorActual = null;

    private final java.util.List<Persistencia.DetalleTicketData.DetalleTicketVista> listaEntradas = new java.util.ArrayList<>();
    private final java.util.List<Integer> listaTicketPorFila = new java.util.ArrayList<>();

    /**
     * Creates new form VistaEntradas
     */
    public VistaEntradas() {
        initComponents();
        
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tablaClick();
            }
        });

        txt_nombre_cliente.setEditable(false);
        txt_dni_cliente.setEditable(false);
        txt_nombre_pelicula.setEditable(false);
        txt_funcion.setEditable(false);
        txt_idioma.setEditable(false);
        txt_formato.setEditable(false);
        txt_sala.setEditable(false);
        txt_fila.setEditable(false);
        txt_asiento.setEditable(false);

        btn_imprimir.setEnabled(false);

        limpiarTodo();
    }
    
    private void limpiarTabla() {
        modelo.setRowCount(0);
        listaEntradas.clear();
        listaTicketPorFila.clear();
        tabla.clearSelection();
    }

    private void limpiarDetalle() {
        txt_nombre_cliente.setText("");
        txt_dni_cliente.setText("");
        txt_nombre_pelicula.setText("");
        txt_funcion.setText("");
        txt_idioma.setText("");
        txt_formato.setText("");
        txt_sala.setText("");
        txt_fila.setText("");
        txt_asiento.setText("");
    }

    private void limpiarTodo() {
        txt_buscar_dni.setText("");
        compradorActual = null;
        limpiarTabla();
        limpiarDetalle();
        btn_imprimir.setEnabled(false);
    }

    private String formatearIdioma(String idioma, boolean subtitulada) {
        if (idioma == null) return "";
        if (idioma.equalsIgnoreCase("Ingles") && subtitulada) {
            return "Ingles - Subtitulada";
        }
        return idioma;
    }

    private void cargarEntradasDeComprador(Modelo.Comprador comprador) {
        limpiarTabla();
        limpiarDetalle();

        try {
            List<Modelo.TicketCompra> tickets = ticketDao.buscarPorComprador(comprador.getIdcomprador());
            if (tickets.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ese cliente no tiene tickets registrados");
                return;
            }

            for (Modelo.TicketCompra t : tickets) {
                List<DetalleTicketData.DetalleTicketVista> vistas = detalleDao.listarVistaPorTicket(t.getIdticket());
                for (DetalleTicketData.DetalleTicketVista v : vistas) {
                    listaEntradas.add(v);
                    listaTicketPorFila.add(t.getIdticket());

                    String tipo = v.es3d ? "3D" : "2D";
                    String idiomaFmt = formatearIdioma(v.idioma, v.subtitulada);
                    String funcionStr = v.fechaFuncion.toString() + " - " + v.horaInicio.toString();

                    modelo.addRow(new Object[]{
                            v.titulo,
                            v.nroSala,
                            v.fila,
                            v.numero,
                            tipo,
                            idiomaFmt,
                            funcionStr
                    });
                }
            }

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Ese cliente no tiene entradas asociadas");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las entradas: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablaClick() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= listaEntradas.size()) {
            btn_imprimir.setEnabled(false);
            return;
        }

        DetalleTicketData.DetalleTicketVista v = listaEntradas.get(fila);

        if (compradorActual != null) {
            txt_nombre_cliente.setText(compradorActual.getNombre());
            txt_dni_cliente.setText(String.valueOf(compradorActual.getDni()));
        }

        txt_nombre_pelicula.setText(v.titulo);
        txt_sala.setText(String.valueOf(v.nroSala));
        txt_fila.setText(v.fila);
        txt_asiento.setText(String.valueOf(v.numero));
        txt_formato.setText(v.es3d ? "3D" : "2D");
        txt_idioma.setText(formatearIdioma(v.idioma, v.subtitulada));
        txt_funcion.setText(v.fechaFuncion.toString() + " - " + v.horaInicio.toString());

        btn_imprimir.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_entradas = new javax.swing.JPanel();
        lb_titulo = new javax.swing.JLabel();
        lb_buscar_dni = new javax.swing.JLabel();
        txt_buscar_dni = new javax.swing.JTextField();
        btn_buscar = new javax.swing.JButton();
        sp_tabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        lb_titulo_cliente = new javax.swing.JLabel();
        lb_nombre_cliente = new javax.swing.JLabel();
        txt_nombre_cliente = new javax.swing.JTextField();
        lb_dni_cliente = new javax.swing.JLabel();
        txt_dni_cliente = new javax.swing.JTextField();
        lb_titulo_pelicula = new javax.swing.JLabel();
        lb_nombre_pelicula = new javax.swing.JLabel();
        txt_nombre_pelicula = new javax.swing.JTextField();
        lb_funcion = new javax.swing.JLabel();
        txt_funcion = new javax.swing.JTextField();
        lb_idioma = new javax.swing.JLabel();
        txt_idioma = new javax.swing.JTextField();
        lb_sala = new javax.swing.JLabel();
        txt_sala = new javax.swing.JTextField();
        lb_fila = new javax.swing.JLabel();
        txt_fila = new javax.swing.JTextField();
        lb_asiento = new javax.swing.JLabel();
        txt_asiento = new javax.swing.JTextField();
        btn_imprimir = new javax.swing.JButton();
        btn_limpiar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        lb_formato = new javax.swing.JLabel();
        txt_formato = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        lb_titulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_titulo.setText("Comprobante de Entrada");

        lb_buscar_dni.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_buscar_dni.setText("Buscar por DNI:");

        btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-magnifying-glass-tilted-right-48.png"))); // NOI18N
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Pelicula", "Sala", "Fila", "Asiento", "Tipo", "Idioma", "Funcion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sp_tabla.setViewportView(tabla);

        lb_titulo_cliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_titulo_cliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_titulo_cliente.setText("Cliente");

        lb_nombre_cliente.setText("Nombre:");

        lb_dni_cliente.setText("DNI:");

        lb_titulo_pelicula.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_titulo_pelicula.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_titulo_pelicula.setText("Pelicula");

        lb_nombre_pelicula.setText("Nombre:");

        lb_funcion.setText("Funcion:");

        lb_idioma.setText("Idioma:");

        lb_sala.setText("Sala:");

        lb_fila.setText("Fila:");

        lb_asiento.setText("Asiento:");

        btn_imprimir.setText("Imprimir");
        btn_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimirActionPerformed(evt);
            }
        });

        btn_limpiar.setText("Limpiar");
        btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiarActionPerformed(evt);
            }
        });

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        lb_formato.setText("Formato:");

        javax.swing.GroupLayout pnl_entradasLayout = new javax.swing.GroupLayout(pnl_entradas);
        pnl_entradas.setLayout(pnl_entradasLayout);
        pnl_entradasLayout.setHorizontalGroup(
            pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_titulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_entradasLayout.createSequentialGroup()
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_entradasLayout.createSequentialGroup()
                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_entradasLayout.createSequentialGroup()
                                .addGap(322, 322, 322)
                                .addComponent(lb_buscar_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_buscar_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_entradasLayout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(lb_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(100, 100, 100)
                                .addComponent(lb_dni_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_dni_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_entradasLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(lb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnl_entradasLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lb_nombre_pelicula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_idioma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_nombre_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(100, 100, 100)
                                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lb_funcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_formato, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_funcion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_formato, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(99, 99, 99))
                                    .addGroup(pnl_entradasLayout.createSequentialGroup()
                                        .addComponent(txt_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lb_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lb_asiento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_asiento, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 59, Short.MAX_VALUE))
                    .addGroup(pnl_entradasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_titulo_cliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_titulo_pelicula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sp_tabla, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_entradasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btn_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(219, 219, 219))
        );
        pnl_entradasLayout.setVerticalGroup(
            pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_entradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_titulo)
                .addGap(18, 18, 18)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_buscar_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_buscar_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lb_titulo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_dni_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dni_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lb_titulo_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nombre_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nombre_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_funcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_funcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_formato, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_formato, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_asiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_asiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46)
                .addGroup(pnl_entradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_entradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_entradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        String dniTexto = txt_buscar_dni.getText().trim();

        if (dniTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar");
            txt_buscar_dni.requestFocus();
            return;
        }

        if (dniTexto.length() != 8) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 digitos");
            txt_buscar_dni.requestFocus();
            return;
        }

        int dni;
        try {
            dni = Integer.parseInt(dniTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI solo debe contener numeros");
            txt_buscar_dni.requestFocus();
            return;
        }

        try {
            Modelo.Comprador c = compradorDao.buscarPorDni(dni);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No existe un cliente con ese DNI");
                limpiarTodo();
                txt_buscar_dni.setText(dniTexto);
                txt_buscar_dni.requestFocus();
                return;
            }

            compradorActual = c;
            cargarEntradasDeComprador(c);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= listaEntradas.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrada de la tabla");
            return;
        }

        DetalleTicketData.DetalleTicketVista v = listaEntradas.get(fila);
        String idiomaFmt = formatearIdioma(v.idioma, v.subtitulada);
        String funcionStr = v.fechaFuncion.toString() + " - " + v.horaInicio.toString();

        String mensaje = "Entrada impresa correctamente\n\n" +
                "Pelicula: " + v.titulo + "\n" +
                "Sala: " + v.nroSala + "\n" +
                "Funcion: " + funcionStr + "\n" +
                "Formato: " + (v.es3d ? "3D" : "2D") + "\n" +
                "Idioma: " + idiomaFmt + "\n" +
                "Fila: " + v.fila + "  Asiento: " + v.numero;

        JOptionPane.showMessageDialog(this, mensaje, "Imprimir entrada", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        limpiarTodo();
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        dispose();
    }//GEN-LAST:event_btn_salirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JButton btn_salir;
    private javax.swing.JLabel lb_asiento;
    private javax.swing.JLabel lb_buscar_dni;
    private javax.swing.JLabel lb_dni_cliente;
    private javax.swing.JLabel lb_fila;
    private javax.swing.JLabel lb_formato;
    private javax.swing.JLabel lb_funcion;
    private javax.swing.JLabel lb_idioma;
    private javax.swing.JLabel lb_nombre_cliente;
    private javax.swing.JLabel lb_nombre_pelicula;
    private javax.swing.JLabel lb_sala;
    private javax.swing.JLabel lb_titulo;
    private javax.swing.JLabel lb_titulo_cliente;
    private javax.swing.JLabel lb_titulo_pelicula;
    private javax.swing.JPanel pnl_entradas;
    private javax.swing.JScrollPane sp_tabla;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_asiento;
    private javax.swing.JTextField txt_buscar_dni;
    private javax.swing.JTextField txt_dni_cliente;
    private javax.swing.JTextField txt_fila;
    private javax.swing.JTextField txt_formato;
    private javax.swing.JTextField txt_funcion;
    private javax.swing.JTextField txt_idioma;
    private javax.swing.JTextField txt_nombre_cliente;
    private javax.swing.JTextField txt_nombre_pelicula;
    private javax.swing.JTextField txt_sala;
    // End of variables declaration//GEN-END:variables
}
