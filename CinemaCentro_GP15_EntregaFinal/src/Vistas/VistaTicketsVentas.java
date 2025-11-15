package Vistas;

import Modelo.TicketCompra;
import Persistencia.TicketCompraData;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;
import Modelo.Comprador;
import Persistencia.CompradorData;
import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Lucas
 */
public class VistaTicketsVentas extends javax.swing.JInternalFrame {

    private TicketCompraData ticketData;
    private CompradorData compradorData;

    public VistaTicketsVentas() {
        initComponents();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Tickets");
        dateCompra.setMaxSelectableDate(new java.util.Date());
        dateCompra.setMinSelectableDate(new java.util.Date());
        habilitarModoConsulta();
        cbCanal.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"WEB", "PRESENCIAL", "TELEFONO"}
        ));

        cbMedioPago.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA", "MERCADOPAGO"}
        ));

        ticketData = new TicketCompraData();
        compradorData = new CompradorData();
        configurarTabla();

        jtTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jtTickets.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosDesdeTabla(fila);
                }
            }
        });
        actualizarTabla();
    }

    private void habilitarModoConsulta() {
        txtIdComprador.setEditable(true);
        txtIdTicket.setEditable(false);
        txtIdTicket.setText("");
        dateCompra.setEnabled(false);
        txtPrecioUnitario.setEditable(false);
        txtMontoTotal.setEditable(false);
        txtCantidad.setEditable(false);
        cbCanal.setEnabled(false);
        cbMedioPago.setEnabled(false);

        btnBuscar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnAnular.setEnabled(false);
    }

    private void habilitarModoEdicion() {
        txtIdTicket.setEditable(false);
        txtIdComprador.setEditable(false);
        dateCompra.setEnabled(true);
        txtPrecioUnitario.setEditable(true);
        txtMontoTotal.setEditable(false);
        txtCantidad.setEditable(true);
        cbCanal.setEnabled(true);
        cbMedioPago.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnAnular.setEnabled(true);
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Comprador", "Fecha", "Precio Unit.", "Cantidad", "Monto Total", "Canal", "Medio Pago"};

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jtTickets.setModel(modelo);
    }

    private void limpiarCampos() {
        txtIdTicket.setText("");
        txtIdComprador.setText("");
        dateCompra.setDate(null);
        txtPrecioUnitario.setText("");
        txtMontoTotal.setText("");
        txtCantidad.setText("");
        cbCanal.setSelectedIndex(0);
        cbMedioPago.setSelectedIndex(0);
    }

    private void actualizarTabla() {
        try {
            javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jtTickets.getModel();
            modelo.setRowCount(0);

            List<TicketCompra> tickets = ticketData.listarTodos();

            for (TicketCompra t : tickets) {
                Object[] fila = {
                    t.getIdticket(),
                    t.getIdcomprador(),
                    t.getFechacompra(),
                    t.getPreciounitario(),
                    t.getCantidad(),
                    t.getMontototal(),
                    t.getCanal(),
                    t.getMediopago()
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al listar: " + e.getMessage());
        }
    }

    private void cargarDatosDesdeTabla(int fila) {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jtTickets.getModel();

        txtIdTicket.setText(modelo.getValueAt(fila, 0).toString());
        txtIdComprador.setText(modelo.getValueAt(fila, 1).toString());
        dateCompra.setDate((java.util.Date) modelo.getValueAt(fila, 2));
        txtPrecioUnitario.setText(modelo.getValueAt(fila, 3).toString());
        txtCantidad.setText(modelo.getValueAt(fila, 4).toString());
        txtMontoTotal.setText(modelo.getValueAt(fila, 5).toString());
        cbCanal.setSelectedItem(modelo.getValueAt(fila, 6).toString());
        cbMedioPago.setSelectedItem(modelo.getValueAt(fila, 7).toString());
        habilitarModoEdicion();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtIdTicket = new javax.swing.JTextField();
        txtIdComprador = new javax.swing.JTextField();
        txtPrecioUnitario = new javax.swing.JTextField();
        txtMontoTotal = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        dateCompra = new com.toedter.calendar.JDateChooser();
        cbCanal = new javax.swing.JComboBox<>();
        cbMedioPago = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtTickets = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(1060, 476));
        setMinimumSize(new java.awt.Dimension(960, 476));
        setPreferredSize(new java.awt.Dimension(960, 476));

        jLabel1.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestión de Tickets de Compra");

        jLabel2.setText("ID TICKET");

        jLabel3.setText("DNI CLIENTE");

        jLabel6.setText("FECHA COMPRA");

        jLabel7.setText("PRECIO UNITARIO");

        jLabel8.setText("MONTO TOTAL");

        jLabel9.setText("CANAL");

        jLabel10.setText("MEDIO DE PAGO");

        jLabel11.setText("CANTIDAD");

        txtIdTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdTicketActionPerformed(evt);
            }
        });

        cbCanal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbMedioPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });

        btnListar.setText("Listar");
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });

        jtTickets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jtTickets);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dateCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtIdComprador)
                                    .addComponent(txtPrecioUnitario)
                                    .addComponent(txtMontoTotal)
                                    .addComponent(cbCanal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbMedioPago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIdTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCantidad)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(290, 290, 290)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbCanal, cbMedioPago, jLabel10, jLabel11, jLabel2, jLabel3, jLabel6, jLabel7, jLabel8, jLabel9, txtCantidad, txtIdComprador, txtIdTicket, txtMontoTotal, txtPrecioUnitario});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnActualizar, btnAnular, btnBuscar, btnListar});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtIdComprador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtPrecioUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtMontoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cbCanal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cbMedioPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbCanal, cbMedioPago, jLabel10, jLabel11, jLabel2, jLabel3, jLabel6, jLabel7, jLabel8, jLabel9, txtCantidad, txtIdComprador, txtIdTicket, txtMontoTotal, txtPrecioUnitario});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnActualizar, btnAnular, btnBuscar, btnListar});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdTicketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdTicketActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        try {
            if (txtIdTicket.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el ID del ticket a actualizar");
                return;
            }
            if (dateCompra.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de compra");
                return;
            }
            if (txtPrecioUnitario.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar precio unitario y cantidad");
                return;
            }

            int idTicket = Integer.parseInt(txtIdTicket.getText().trim());
            BigDecimal precioUnitario = new BigDecimal(txtPrecioUnitario.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            BigDecimal montoTotal = precioUnitario.multiply(new BigDecimal(cantidad));

            TicketCompra ticket = new TicketCompra();
            ticket.setIdticket(idTicket);
            ticket.setIdcomprador(Integer.parseInt(txtIdComprador.getText().trim()));
            ticket.setFechacompra(dateCompra.getDate());
            ticket.setPreciounitario(precioUnitario);
            ticket.setMontototal(montoTotal);
            ticket.setCanal(cbCanal.getSelectedItem().toString());
            ticket.setMediopago(cbMedioPago.getSelectedItem().toString());
            ticket.setCantidad(cantidad);

            int filas = ticketData.actualizar(ticket);

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Ticket actualizado correctamente");
                limpiarCampos();
                actualizarTabla();
                habilitarModoConsulta();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún ticket con ese ID");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: formato de número inválido");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error SQL: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        if (txtIdComprador.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Debe ingresar el DNI del comprador");
            return;
        }

        try {

            int dniComprador = Integer.parseInt(txtIdComprador.getText().trim());


            Comprador comprador = compradorData.buscarPorDni(dniComprador);

            
            if (comprador == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún comprador con ese DNI");
                limpiarCampos();
                ((javax.swing.table.DefaultTableModel) jtTickets.getModel()).setRowCount(0); // Limpia la tabla
                return;
            }


            int idCompradorReal = comprador.getIdcomprador();
            List<TicketCompra> tickets = ticketData.buscarPorComprador(idCompradorReal);

            if (tickets.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El comprador con DNI " + dniComprador + " no tiene tickets registrados");
                limpiarCampos();
                ((javax.swing.table.DefaultTableModel) jtTickets.getModel()).setRowCount(0); // Limpia la tabla
                return;
            }


            javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jtTickets.getModel();
            modelo.setRowCount(0);

            for (TicketCompra t : tickets) {
                Object[] fila = {
                    t.getIdticket(),
                    t.getIdcomprador(), 
                    t.getFechacompra(),
                    t.getPreciounitario(),
                    t.getCantidad(),
                    t.getMontototal(),
                    t.getCanal(),
                    t.getMediopago()
                };
                modelo.addRow(fila);
            }

            JOptionPane.showMessageDialog(this, "Se encontraron " + tickets.size() + " tickets del comprador con DNI " + dniComprador);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        if (txtIdTicket.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un ID para eliminar");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este ticket?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtIdTicket.getText().trim());
                ticketData.anular(id);
                JOptionPane.showMessageDialog(this, "Ticket eliminado correctamente");
                limpiarCampos();
                actualizarTabla();
                habilitarModoConsulta();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Eliminación cancelada");
        }
    }//GEN-LAST:event_btnAnularActionPerformed

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
        try {
            actualizarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar tickets: " + e.getMessage());
        }
    }//GEN-LAST:event_btnListarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnListar;
    private javax.swing.JComboBox<String> cbCanal;
    private javax.swing.JComboBox<String> cbMedioPago;
    private com.toedter.calendar.JDateChooser dateCompra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jtTickets;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtIdComprador;
    private javax.swing.JTextField txtIdTicket;
    private javax.swing.JTextField txtMontoTotal;
    private javax.swing.JTextField txtPrecioUnitario;
    // End of variables declaration//GEN-END:variables
}
