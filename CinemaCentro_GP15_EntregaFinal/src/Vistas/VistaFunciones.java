package Vistas;

import Modelo.Funcion;
import Modelo.Pelicula;
import Modelo.Sala;
import Persistencia.FuncionData;
import Persistencia.PeliculaData;
import Persistencia.SalaData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author Grupo 15
 *  Luis Ezequiel Sosa
 *  Lucas Saidman
 *  Luca Rodrigaño
 *  Ignacio Rodriguez
 */

public class VistaFunciones extends javax.swing.JInternalFrame {
    
    private final FuncionData funcionDao = new FuncionData();
    private final PeliculaData peliculaDao = new PeliculaData();
    private final SalaData salaDao = new SalaData();

    private DefaultTableModel modelo;

    private final List<Pelicula> listaPeliculas = new ArrayList<>();
    private final List<Sala> listaSalas = new ArrayList<>();
    private final List<Funcion> listaFuncionesTabla = new ArrayList<>();
    private Funcion seleccionadaOriginal = null;

    private static final LocalTime[] HORAS_FIJAS = {
        LocalTime.of(16, 0),
        LocalTime.of(19, 0),
        LocalTime.of(22, 0)
    };
    
    private static final java.math.BigDecimal PRECIO_2D = new java.math.BigDecimal("6000.00");
    private static final java.math.BigDecimal PRECIO_3D = new java.math.BigDecimal("9000.00");

    /**
     * Creates new form VistaFunciones
     */
    public VistaFunciones() {
        initComponents();
        
        modelo = (DefaultTableModel) tb_tabla.getModel();
        tb_tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        inicializarCombos();
        cargarPeliculasFiltro();
        cargarSalas();
        cargarTablaSegunFiltro();

        escucharCambios();
        reglasHabilitacion();
    }
    
    private void inicializarCombos() {
        cb_idioma.removeAllItems();
        cb_idioma.addItem("Español");
        cb_idioma.addItem("Ingles");
        cb_idioma.setSelectedIndex(-1);

        cb_hora_inicio.removeAllItems();
        for (LocalTime h : HORAS_FIJAS) {
            cb_hora_inicio.addItem(h.toString());
        }
        cb_hora_inicio.setSelectedIndex(-1);
    }

    private void escucharCambios() {
        cb_pelicula.addItemListener(e -> {
            cargarTablaSegunFiltro();
            limpiarFormulario();
            tb_tabla.clearSelection();
            seleccionadaOriginal = null;
            reglasHabilitacion();
        });

        dc_fecha_inicio.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                actualizarFin();
                reglasHabilitacion();
            }
        });

        cb_hora_inicio.addItemListener(e -> {
            actualizarFin();
            reglasHabilitacion();
        });

        cb_sala.addItemListener(e -> reglasHabilitacion());
        cb_idioma.addItemListener(e -> reglasHabilitacion());

        tb_tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                tablaClick();
            }
        });
    }

    private void reglasHabilitacion() {
        boolean haySeleccion = tb_tabla.getSelectedRow() >= 0 && seleccionadaOriginal != null;

        boolean peliculaElegida = cb_pelicula.getSelectedIndex() > 0;
        boolean salaElegida = cb_sala.getSelectedIndex() >= 0;
        boolean fechaElegida = dc_fecha_inicio.getDate() != null;
        boolean horaElegida = cb_hora_inicio.getSelectedIndex() >= 0;
        boolean idiomaElegido = cb_idioma.getSelectedIndex() >= 0;

        btn_nuevo.setEnabled(true);
        boolean datosCompletos = peliculaElegida && salaElegida && fechaElegida && horaElegida && idiomaElegido;
        btn_guardar.setEnabled(datosCompletos && !haySeleccion);
        btn_actualizar.setEnabled(datosCompletos && haySeleccion);
        btn_eliminar.setEnabled(haySeleccion);
    }

    private void cargarPeliculasFiltro() {
        listaPeliculas.clear();
        try {
            listaPeliculas.addAll(peliculaDao.listarTodas());
        } catch (SQLException e) {
            error(e);
        }

        cb_pelicula.removeAllItems();
        cb_pelicula.addItem("Todas");
        for (Pelicula p : listaPeliculas) {
            cb_pelicula.addItem(p.getTitulo());
        }
        cb_pelicula.setSelectedIndex(0);
    }

    private void cargarSalas() {
        listaSalas.clear();
        try {
            listaSalas.addAll(salaDao.listarTodas());
        } catch (SQLException e) {
            error(e);
        }

        cb_sala.removeAllItems();
        for (Sala s : listaSalas) {
            cb_sala.addItem(String.valueOf(s.getNroSala()));
        }
        cb_sala.setSelectedIndex(-1);
    }

    private void cargarTablaSegunFiltro() {
        limpiarTabla();
        listaFuncionesTabla.clear();

        try {
            String sel = (String) cb_pelicula.getSelectedItem();
            if (sel == null || "Todas".equals(sel)) {
                for (Pelicula p : listaPeliculas) {
                    List<Funcion> fs = funcionDao.listarPorPelicula(p.getIdPelicula());
                    agregarFuncionesATabla(fs, p.getTitulo());
                }
            } else {
                Pelicula peli = buscarPeliculaPorTitulo(sel);
                if (peli != null) {
                    List<Funcion> fs = funcionDao.listarPorPelicula(peli.getIdPelicula());
                    agregarFuncionesATabla(fs, peli.getTitulo());
                }
            }
        } catch (SQLException e) {
            error(e);
        }
    }

    private void agregarFuncionesATabla(List<Funcion> funciones, String tituloPelicula) {
        for (Funcion f : funciones) {
            listaFuncionesTabla.add(f);

            LocalDateTime ini = convertirALocalDateTime(f.getHorainicio());
            LocalDateTime fin = convertirALocalDateTime(f.getHorafin());

            modelo.addRow(new Object[]{
                tituloPelicula,
                f.getNrosala(),
                ini.toString().replace('T', ' '),
                fin.toString().replace('T', ' ')
            });
        }
    }

    private void limpiarTabla() {
        modelo.setRowCount(0);
    }

    private void limpiarFormulario() {
        cb_sala.setSelectedIndex(-1);
        dc_fecha_inicio.setDate(null);
        cb_hora_inicio.setSelectedIndex(-1);
        cb_idioma.setSelectedIndex(-1);
        txt_fecha_fin.setText("");
        txt_hora_fin.setText("");
        seleccionadaOriginal = null;
        reglasHabilitacion();
    }

    private Pelicula buscarPeliculaPorTitulo(String titulo) {
        for (Pelicula p : listaPeliculas) {
            if (p.getTitulo().equals(titulo)) {
                return p;
            }
        }
        return null;
    }

    private LocalDateTime getInicioDesdeUI() {
        Date fecha = dc_fecha_inicio.getDate();
        Object horaSel = cb_hora_inicio.getSelectedItem();

        if (fecha == null || horaSel == null) {
            return null;
        }

        LocalDate ld = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime lt = LocalTime.parse(horaSel.toString());
        return LocalDateTime.of(ld, lt);
    }
    
    private Sala getSalaSeleccionada() {
        int idx = cb_sala.getSelectedIndex();
        if (idx < 0 || idx >= listaSalas.size()) {
            return null;
        }
        return listaSalas.get(idx);
    }
    
    private boolean esSala3DSeleccionada() {
        Sala s = getSalaSeleccionada();
        return s != null && s.isApta3D();
    }

    private LocalDateTime convertirALocalDateTime(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Date convertirADate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void actualizarFin() {
        LocalDateTime ini = getInicioDesdeUI();
        if (ini == null) {
            txt_fecha_fin.setText("");
            txt_hora_fin.setText("");
            return;
        }

        LocalDateTime fin = ini.plusHours(2).plusMinutes(50);
        txt_fecha_fin.setText(fin.toLocalDate().toString());
        txt_hora_fin.setText(fin.toLocalTime().withSecond(0).withNano(0).toString());
    }

    private void setFormulario(Funcion f) {
        Pelicula p = null;
        for (Pelicula x : listaPeliculas) {
            if (x.getIdPelicula() == f.getIdpelicula()) {
                p = x;
                break;
            }
        }
        if (p != null) {
            cb_pelicula.setSelectedItem(p.getTitulo());
        }

        cb_sala.setSelectedItem(String.valueOf(f.getNrosala()));

        LocalDateTime ini = convertirALocalDateTime(f.getHorainicio());
        dc_fecha_inicio.setDate(convertirADate(ini.toLocalDate().atStartOfDay()));
        cb_hora_inicio.setSelectedItem(ini.toLocalTime().withSecond(0).withNano(0).toString());

        cb_idioma.setSelectedItem(f.isSubtitulada() ? "Ingles" : "Español");

        LocalDateTime fin = convertirALocalDateTime(f.getHorafin());
        txt_fecha_fin.setText(fin.toLocalDate().toString());
        txt_hora_fin.setText(fin.toLocalTime().withSecond(0).withNano(0).toString());
    }

    private Funcion armarFuncionDesdeFormulario() {
        Funcion f = new Funcion();

        String tituloPeli = (String) cb_pelicula.getSelectedItem();
        Pelicula peli = buscarPeliculaPorTitulo(tituloPeli);
        if (peli == null) {
            msg("No se pudo encontrar la película seleccionada");
            return null;
        }
        
        Sala sala = getSalaSeleccionada();
        if (sala == null) {
            msg("Seleccione una sala");
            return null;
        }
        
        
        String idioma = cb_idioma.getSelectedItem().toString();
        boolean subtitulada = idioma.equalsIgnoreCase("Ingles");

        LocalDateTime ini = getInicioDesdeUI();
        if (ini == null) {
            msg("Fecha u hora de inicio inválidas");
            return null;
        }
        LocalDateTime fin = ini.plusHours(2).plusMinutes(50);
        
        boolean es3D = esSala3DSeleccionada();
        java.math.BigDecimal precio = es3D ? PRECIO_3D : PRECIO_2D;

        f.setIdpelicula(peli.getIdPelicula());
        f.setNrosala(sala.getNroSala());
        f.setIdioma(idioma);
        f.setEs3d(sala.isApta3D());
        f.setSubtitulada(subtitulada);
        f.setHorainicio(convertirADate(ini));
        f.setHorafin(convertirADate(fin));
        f.setLugaresdisponibles(sala.getCapacidad());
        f.setPreciotipo(precio);

        if (seleccionadaOriginal != null) {
            f.setIdfuncion(seleccionadaOriginal.getIdfuncion());
        }

        return f;
    }

    private boolean validarFormulario() {
        if (cb_pelicula.getSelectedIndex() <= 0) {
            msg("Seleccione una pelicula");
            cb_pelicula.requestFocus();
            return false;
        }
        if (cb_sala.getSelectedIndex() < 0) {
            msg("Seleccione una sala");
            cb_sala.requestFocus();
            return false;
        }
        if (dc_fecha_inicio.getDate() == null) {
            msg("Seleccione una fecha de inicio");
            dc_fecha_inicio.requestFocus();
            return false;
        }
        if (cb_hora_inicio.getSelectedIndex() < 0) {
            msg("Seleccione una hora de inicio");
            cb_hora_inicio.requestFocus();
            return false;
        }
        if (cb_idioma.getSelectedIndex() < 0) {
            msg("Seleccione un idioma");
            cb_idioma.requestFocus();
            return false;
        }
        return true;
    }

    private void tablaClick() {
        int fila = tb_tabla.getSelectedRow();
        if (fila < 0 || fila >= listaFuncionesTabla.size()) {
            return;
        }

        seleccionadaOriginal = listaFuncionesTabla.get(fila);
        setFormulario(seleccionadaOriginal);
        reglasHabilitacion();
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

        pnl_gestion_funciones = new javax.swing.JPanel();
        lb_titulo = new javax.swing.JLabel();
        sp_tabla = new javax.swing.JScrollPane();
        tb_tabla = new javax.swing.JTable();
        lb_pelicula = new javax.swing.JLabel();
        cb_pelicula = new javax.swing.JComboBox<>();
        lb_sala = new javax.swing.JLabel();
        lb_inicio = new javax.swing.JLabel();
        lb_idioma = new javax.swing.JLabel();
        cb_sala = new javax.swing.JComboBox<>();
        cb_idioma = new javax.swing.JComboBox<>();
        lb_fecha_inicio = new javax.swing.JLabel();
        lb_hora_inicio = new javax.swing.JLabel();
        btn_guardar = new javax.swing.JButton();
        btn_nuevo = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lb_fin = new javax.swing.JLabel();
        lb_fecha_fin = new javax.swing.JLabel();
        lb_hora_fin = new javax.swing.JLabel();
        txt_fecha_fin = new javax.swing.JTextField();
        txt_hora_fin = new javax.swing.JTextField();
        cb_hora_inicio = new javax.swing.JComboBox<>();
        dc_fecha_inicio = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        lb_titulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_titulo.setText("Gestión Funciones");

        tb_tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Pelicula", "Sala", "Inicio", "Fin"
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

        lb_pelicula.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_pelicula.setText("Pelicula:");

        lb_sala.setText("Sala:");

        lb_inicio.setText("Inicio:");

        lb_idioma.setText("Idioma:");

        lb_fecha_inicio.setText("Fecha:");

        lb_hora_inicio.setText("Hora:");

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/guardar.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        btn_nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/escoba.png"))); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
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

        lb_fin.setText("Fin:");

        lb_fecha_fin.setText("Fecha:");

        lb_hora_fin.setText("Hora:");

        txt_fecha_fin.setEditable(false);

        txt_hora_fin.setEditable(false);

        javax.swing.GroupLayout pnl_gestion_funcionesLayout = new javax.swing.GroupLayout(pnl_gestion_funciones);
        pnl_gestion_funciones.setLayout(pnl_gestion_funcionesLayout);
        pnl_gestion_funcionesLayout.setHorizontalGroup(
            pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                        .addGap(305, 305, 305)
                        .addComponent(lb_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cb_pelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(sp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_gestion_funcionesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                        .addComponent(lb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_gestion_funcionesLayout.createSequentialGroup()
                            .addComponent(lb_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                                    .addComponent(lb_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                                    .addComponent(lb_hora_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cb_hora_inicio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                            .addComponent(lb_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cb_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(50, 50, 50)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_hora_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                        .addComponent(lb_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lb_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_hora_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
        );
        pnl_gestion_funcionesLayout.setVerticalGroup(
            pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_gestion_funcionesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lb_titulo)
                .addGap(34, 34, 34)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_pelicula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cb_pelicula, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addComponent(sp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_hora_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_hora_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_hora_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_hora_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cb_idioma)
                    .addComponent(lb_idioma, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(pnl_gestion_funcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_gestion_funciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_gestion_funciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        limpiarFormulario();
        tb_tabla.clearSelection();
        seleccionadaOriginal = null;
        reglasHabilitacion();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        if (!validarFormulario()) {
            return;
        }

        try {
            Funcion f = armarFuncionDesdeFormulario();
            
            if (f == null) {
                return;
            }
            
            LocalDateTime ini = getInicioDesdeUI();
            LocalDateTime fin = ini.plusHours(2).plusMinutes(50);
            if (funcionDao.existeSolapado(f.getNrosala(), ini, fin)) {
                msg("Ya existe una funcion en ese horario para la sala seleccionada");
                return;
            }

            int idGenerado = funcionDao.crear(f);
            f.setIdfuncion(idGenerado);

            java.util.List<String> filas = java.util.Arrays.asList("A","B","C","D","E");
            int porFila = 6;

            funcionDao.generarAsientos(idGenerado, filas, porFila);
        
            msg("Funcion guardada");
            cargarTablaSegunFiltro();
            limpiarFormulario();
        } catch (Exception e) {
            error(e);
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        if (seleccionadaOriginal == null) {
            msg("Seleccione una funcion para actualizar");
            return;
        }
        if (!validarFormulario()) {
            return;
        }

        try {
            Funcion f = armarFuncionDesdeFormulario();
            
            if(f == null) {
                return;
            }

            LocalDateTime ini = getInicioDesdeUI();
            LocalDateTime fin = ini.plusHours(2).plusMinutes(50);

            if (funcionDao.existeSolapado(f.getNrosala(), ini, fin)) {
                msg("Ya existe una funcion en ese horario para la sala seleccionada");
                return;
            }

            funcionDao.actualizar(f);
            msg("Funcion actualizada");
            cargarTablaSegunFiltro();
            limpiarFormulario();
        } catch (Exception e) {
            error(e);
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        if (seleccionadaOriginal == null) {
            msg("Seleccione una funcion para eliminar");
            return;
        }

        int op = JOptionPane.showConfirmDialog(this,
                "Eliminar definitivamente la funcion seleccionada?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            funcionDao.eliminar(seleccionadaOriginal.getIdfuncion());
            msg("Funcion eliminada");
            cargarTablaSegunFiltro();
            limpiarFormulario();
        } catch (Exception e) {
            error(e);
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<String> cb_hora_inicio;
    private javax.swing.JComboBox<String> cb_idioma;
    private javax.swing.JComboBox<String> cb_pelicula;
    private javax.swing.JComboBox<String> cb_sala;
    private com.toedter.calendar.JDateChooser dc_fecha_inicio;
    private javax.swing.JLabel lb_fecha_fin;
    private javax.swing.JLabel lb_fecha_inicio;
    private javax.swing.JLabel lb_fin;
    private javax.swing.JLabel lb_hora_fin;
    private javax.swing.JLabel lb_hora_inicio;
    private javax.swing.JLabel lb_idioma;
    private javax.swing.JLabel lb_inicio;
    private javax.swing.JLabel lb_pelicula;
    private javax.swing.JLabel lb_sala;
    private javax.swing.JLabel lb_titulo;
    private javax.swing.JPanel pnl_gestion_funciones;
    private javax.swing.JScrollPane sp_tabla;
    private javax.swing.JTable tb_tabla;
    private javax.swing.JTextField txt_fecha_fin;
    private javax.swing.JTextField txt_hora_fin;
    // End of variables declaration//GEN-END:variables
}
