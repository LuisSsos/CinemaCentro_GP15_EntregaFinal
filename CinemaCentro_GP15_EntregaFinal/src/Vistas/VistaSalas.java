package Vistas;

import Modelo.Sala;
import Persistencia.SalaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author Grupo 15
 * Luis Ezequiel Sosa
 * Lucas Saidman
 * Luca Rodrigaño
 * Ignacio Rodriguez
 */

public class VistaSalas extends javax.swing.JInternalFrame {
    
    private final SalaData dao = new SalaData();
    private DefaultTableModel modelo;
    private Sala seleccionada = null;

    /**
     * Creates new form VistaSalas
     */
    public VistaSalas() {
        initComponents();
        
        txt_capacidad.setText("30");
        txt_capacidad.setEditable(false);
        
        modelo = (DefaultTableModel) tb_tabla.getModel();
        tb_tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        cb_tipo_sala.setModel(new DefaultComboBoxModel<>(new String[]{"2D", "3D"}));
        cb_estado.setModel(new DefaultComboBoxModel<>(new String[]{"Activa", "Inactiva"}));

        escucharCambios();
        cargarTablaBD();
        reglasHabilitacion();
    }
    
    private void escucharCambios() {
        java.awt.event.KeyAdapter ka = new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                reglasHabilitacion();
            }
        };

        txt_numero_sala.addKeyListener(ka);
        txt_capacidad.addKeyListener(ka);

        cb_tipo_sala.addItemListener(e -> reglasHabilitacion());
        cb_estado.addItemListener(e -> reglasHabilitacion());

        tb_tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tablaClick();
            }
        });
    }

    private void reglasHabilitacion() {
        boolean numeroIngresado = !txt_numero_sala.getText().trim().isEmpty();
        boolean haySeleccion = tb_tabla.getSelectedRow() >= 0 && seleccionada != null;

        btn_nuevo.setEnabled(true);
        btn_buscar.setEnabled(numeroIngresado);

        boolean puedeGuardar = false;
        Integer nro = parseEntero(txt_numero_sala.getText());
        Integer capacidad = parseEntero(txt_capacidad.getText());
        Boolean tipo3D = getTipoSalaCombo();
        Boolean estado = getEstadoCombo();

        if (!haySeleccion && nro != null && capacidad != null
                && tipo3D != null && estado != null) {
            try {
                puedeGuardar = (dao.buscarPorId(nro) == null);
            } catch (Exception e) {
                puedeGuardar = false;
            }
        }
        
        btn_guardar.setEnabled(puedeGuardar);

        boolean puedeActualizar = false;
        if (haySeleccion && nro != null && capacidad != null
                && tipo3D != null && estado != null) {

            boolean mismoNumero = (nro == seleccionada.getNroSala());
            boolean mismoTipo = (tipo3D == seleccionada.isApta3D());
            boolean mismaCapacidad = (capacidad == seleccionada.getCapacidad());
            boolean mismoEstado = (estado == seleccionada.isEstado());

            puedeActualizar = mismoNumero && !(mismoTipo && mismaCapacidad && mismoEstado);
        }
        
        btn_actualizar.setEnabled(puedeActualizar);
        btn_eliminar.setEnabled(haySeleccion);
    }

    private void cargarTablaBD() {
        try {
            List<Sala> datos = dao.listarTodas();
            cargarTabla(datos);
        } catch (SQLException e) {
            error(e);
            System.out.println("ERROR: " + e);
        }
    }

    private void cargarTabla(List<Sala> datos) {
        limpiarTabla();
        for (Sala s : datos) {
            modelo.addRow(new Object[]{
                s.getNroSala(),
                s.isApta3D() ? "3D" : "2D",
                s.getCapacidad(),
                s.isEstado() ? "Activa" : "Inactiva"
            });
        }
        tb_tabla.clearSelection();
        seleccionada = null;
        reglasHabilitacion();
    }

    private void limpiarTabla() {
        modelo.setRowCount(0);
    }

    private void seleccionarFilaPorNumero(int nroSala) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object v = modelo.getValueAt(i, 0);
            if (v != null && String.valueOf(nroSala).equals(v.toString())) {
                tb_tabla.setRowSelectionInterval(i, i);
                tb_tabla.scrollRectToVisible(tb_tabla.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void tablaClick() {
        int fila = tb_tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        Integer nro = parseEntero(String.valueOf(modelo.getValueAt(fila, 0)));
        if (nro == null) {
            return;
        }

        try {
            Sala s = dao.buscarPorId(nro);
            if (s != null) {
                setFormulario(s);
                seleccionada = s;
                reglasHabilitacion();
            }
        } catch (SQLException e) {
            error(e);
            System.out.println("ERROR: " + e);
        }
    }

    private void setFormulario(Sala s) {
        txt_numero_sala.setText(String.valueOf(s.getNroSala()));
        txt_capacidad.setText(String.valueOf(s.getCapacidad()));
        cb_tipo_sala.setSelectedItem(s.isApta3D() ? "3D" : "2D");
        cb_estado.setSelectedItem(s.isEstado() ? "Activa" : "Inactiva");
    }

    private void limpiarFormulario() {
        txt_numero_sala.setText("");
        cb_tipo_sala.setSelectedIndex(0);
        cb_estado.setSelectedIndex(0);
        tb_tabla.clearSelection();
        seleccionada = null;
        reglasHabilitacion();
        txt_numero_sala.requestFocus();
    }

    private Integer parseEntero(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Boolean getTipoSalaCombo() {
        Object sel = cb_tipo_sala.getSelectedItem();
        if (sel == null) {
            return null;
        }
        String v = sel.toString().trim();
        if (v.equalsIgnoreCase("2D")) {
            return false;
        }
        if (v.equalsIgnoreCase("3D")) {
            return true;
        }
        return null;
    }

    private Boolean getEstadoCombo() {
        Object sel = cb_estado.getSelectedItem();
        if (sel == null) {
            return null;
        }
        String v = sel.toString().trim();
        if (v.equalsIgnoreCase("Activa")) {
            return true;
        }
        if (v.equalsIgnoreCase("Inactiva")) {
            return false;
        }
        return null;
    }

    private void msg(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void error(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_gestion_salas = new javax.swing.JPanel();
        lb_titulo = new javax.swing.JLabel();
        sp_tabla = new javax.swing.JScrollPane();
        tb_tabla = new javax.swing.JTable();
        lb_numero_sala = new javax.swing.JLabel();
        btn_nuevo = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        txt_numero_sala = new javax.swing.JTextField();
        lb_tipo_sala = new javax.swing.JLabel();
        cb_tipo_sala = new javax.swing.JComboBox<>();
        lb_capacidad = new javax.swing.JLabel();
        lb_estado = new javax.swing.JLabel();
        txt_capacidad = new javax.swing.JTextField();
        cb_estado = new javax.swing.JComboBox<>();
        btn_buscar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        lb_titulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_titulo.setText("Gestión Salas");

        tb_tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Numero Sala", "Tipo de Sala", "Capacidad", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sp_tabla.setViewportView(tb_tabla);

        lb_numero_sala.setText("Numero de Sala:");

        btn_nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/escoba.png"))); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/guardar.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-aprobar-y-actualizar-48.png"))); // NOI18N
        btn_actualizar.setText("Actualizar");
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });

        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eliminar.png"))); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        lb_tipo_sala.setText("Tipo de Sala:");

        lb_capacidad.setText("Capacidad:");

        lb_estado.setText("Estado:");

        txt_capacidad.setEditable(false);
        txt_capacidad.setText("30");

        btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-magnifying-glass-tilted-right-48.png"))); // NOI18N
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_gestion_salasLayout = new javax.swing.GroupLayout(pnl_gestion_salas);
        pnl_gestion_salas.setLayout(pnl_gestion_salasLayout);
        pnl_gestion_salasLayout.setHorizontalGroup(
            pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_gestion_salasLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                                .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                                .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lb_capacidad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_tipo_sala, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_estado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cb_tipo_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_capacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                                        .addComponent(lb_numero_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_numero_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(sp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        pnl_gestion_salasLayout.setVerticalGroup(
            pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lb_titulo)
                .addGap(50, 50, 50)
                .addComponent(sp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_gestion_salasLayout.createSequentialGroup()
                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_numero_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_numero_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_tipo_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_tipo_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_capacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_capacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(50, 50, 50)
                .addGroup(pnl_gestion_salasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_gestion_salas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_gestion_salas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        if (seleccionada == null) {
            msg("Seleccione una sala para eliminar");
            return;
        }

        int op = JOptionPane.showConfirmDialog(this,
                "Eliminar definitivamente la sala " + seleccionada.getNroSala() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (op != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.eliminar(seleccionada.getNroSala());
            msg("Sala eliminada");
            cargarTablaBD();
            limpiarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar esta sala por que existen funciones asociadas");
            System.out.println("ERROR: " + e);
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        if (seleccionada == null) {
            msg("Seleccione una sala para actualizar");
            return;
        }

        Integer nro = parseEntero(txt_numero_sala.getText());
        Integer capacidad = parseEntero(txt_capacidad.getText());
        Boolean tipo3D = getTipoSalaCombo();
        Boolean estado = getEstadoCombo();

        if (nro == null) {
            msg("Numero de sala invalido");
            txt_numero_sala.requestFocus();
            return;
        }
        if (!nro.equals(seleccionada.getNroSala())) {
            msg("No se puede cambiar el numero de sala");
            txt_numero_sala.setText(String.valueOf(seleccionada.getNroSala()));
            return;
        }
        if (capacidad == null || capacidad <= 0) {
            msg("Capacidad invalida");
            txt_capacidad.requestFocus();
            return;
        }
        if (tipo3D == null) {
            msg("Seleccione tipo de sala");
            cb_tipo_sala.requestFocus();
            return;
        }
        if (estado == null) {
            msg("Seleccione estado");
            cb_estado.requestFocus();
            return;
        }

        try {
            Sala s = new Sala();
            s.setNroSala(nro);
            s.setCapacidad(capacidad);
            s.setApta3D(tipo3D);
            s.setEstado(estado);

            dao.actualizar(s);

            msg("Sala actualizada");
            cargarTablaBD();
            seleccionarFilaPorNumero(nro);
            seleccionada = dao.buscarPorId(nro);
        } catch (Exception e) {
            error(e);
            System.out.println("ERROR: " + e);
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        Integer nro = parseEntero(txt_numero_sala.getText());
        if (nro == null) {
            msg("Numero de sala invalido");
            txt_numero_sala.requestFocus();
            return;
        }

        Integer capacidad = parseEntero(txt_capacidad.getText());
        if (capacidad == null || capacidad <= 0) {
            msg("Capacidad invalida");
            txt_capacidad.requestFocus();
            return;
        }

        Boolean tipo3D = getTipoSalaCombo();
        if (tipo3D == null) {
            msg("Seleccione tipo de sala");
            cb_tipo_sala.requestFocus();
            return;
        }

        Boolean estado = getEstadoCombo();
        if (estado == null) {
            msg("Seleccione estado");
            cb_estado.requestFocus();
            return;
        }

        try {
            if (dao.buscarPorId(nro) != null) {
                msg("Ya existe una sala con ese numero");
                return;
            }

            Sala s = new Sala(nro, tipo3D, capacidad, estado);
            dao.crear(s);

            msg("Sala guardada");
            cargarTablaBD();
            seleccionarFilaPorNumero(nro);
            seleccionada = dao.buscarPorId(nro);
            reglasHabilitacion();
        } catch (Exception e) {
            error(e);
            System.out.println("ERROR: " + e);
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        Integer nro = parseEntero(txt_numero_sala.getText());
        if (nro == null) {
            msg("Ingrese un numero de sala");
            txt_numero_sala.requestFocus();
            return;
        }

        try {
            Sala s = dao.buscarPorId(nro);
            if (s == null) {
                msg("No existe una sala con ese numero");
                return;
            }

            setFormulario(s);
            seleccionada = s;
            seleccionarFilaPorNumero(nro);
            reglasHabilitacion();
        } catch (Exception e) {
            error(e);
            System.out.println("ERROR: " + e);
        }
    }//GEN-LAST:event_btn_buscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<String> cb_estado;
    private javax.swing.JComboBox<String> cb_tipo_sala;
    private javax.swing.JLabel lb_capacidad;
    private javax.swing.JLabel lb_estado;
    private javax.swing.JLabel lb_numero_sala;
    private javax.swing.JLabel lb_tipo_sala;
    private javax.swing.JLabel lb_titulo;
    private javax.swing.JPanel pnl_gestion_salas;
    private javax.swing.JScrollPane sp_tabla;
    private javax.swing.JTable tb_tabla;
    private javax.swing.JTextField txt_capacidad;
    private javax.swing.JTextField txt_numero_sala;
    // End of variables declaration//GEN-END:variables
}
