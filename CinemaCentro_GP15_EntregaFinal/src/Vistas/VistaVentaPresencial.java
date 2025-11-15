package Vistas;

import Modelo.Asiento;
import Modelo.Comprador;
import Modelo.DetalleTicket;
import Modelo.Funcion;
import Modelo.Pelicula;
import Modelo.Sala;
import Modelo.TicketCompra;

import Persistencia.AsientoData;
import Persistencia.CompradorData;
import Persistencia.DetalleTicketData;
import Persistencia.FuncionData;
import Persistencia.PeliculaData;
import Persistencia.SalaData;
import Persistencia.TicketCompraData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class VistaVentaPresencial extends javax.swing.JInternalFrame {

    private final CompradorData compradorDao = new CompradorData();
    private Comprador compradorActual = null;
    private final PeliculaData peliculaDao = new PeliculaData();
    private List<Pelicula> listaPeliculas = new ArrayList<>();
    private final FuncionData funcionDao = new FuncionData();
    private List<Funcion> listaFunciones = new ArrayList<>();

    public VistaVentaPresencial() {
        initComponents();
        initComponents();
        cargarPeliculas();
        txtNombreComprador.setEnabled(false);
        jDateComprador.setEnabled(false);
        btnAgregarComprador.setEnabled(false);
        cbPeliculas.addActionListener(e -> cargarFormatosPorPelicula());
        cbFormatoIdioma.addActionListener(e -> cargarFechasYHoras());
        cbHoras.addActionListener(e -> cargarAsientosPorFuncion());
        listaAsientos.add(A1);
        listaAsientos.add(A2);
        listaAsientos.add(A3);
        listaAsientos.add(A4);
        listaAsientos.add(A5);
        listaAsientos.add(A6);
        listaAsientos.add(B1);
        listaAsientos.add(B2);
        listaAsientos.add(B3);
        listaAsientos.add(B4);
        listaAsientos.add(B5);
        listaAsientos.add(B6);
        listaAsientos.add(C1);
        listaAsientos.add(C2);
        listaAsientos.add(C3);
        listaAsientos.add(C4);
        listaAsientos.add(C5);
        listaAsientos.add(C6);
        listaAsientos.add(D1);
        listaAsientos.add(D2);
        listaAsientos.add(D3);
        listaAsientos.add(D4);
        listaAsientos.add(D5);
        listaAsientos.add(D6);
        listaAsientos.add(E1);
        listaAsientos.add(E2);
        listaAsientos.add(E3);
        listaAsientos.add(E4);
        listaAsientos.add(E5);
        listaAsientos.add(E6);

    }

    //Cliente
    private Integer parseEntero(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean esSoloTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        for (char c : texto.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }

    //Peliculas
    private void cargarPeliculas() {
        try {
            listaPeliculas = peliculaDao.listarPeliculas();
            cbPeliculas.removeAllItems();

            boolean hayPeliculas = false;
            for (Pelicula p : listaPeliculas) {
                if (p.isEnCartelera()) {
                    cbPeliculas.addItem(p.getTitulo());
                    hayPeliculas = true;
                }
            }

            if (!hayPeliculas) {
                cbPeliculas.addItem("No hay peliculas en cartelera");
                cbPeliculas.setEnabled(false);
            } else {
                cbPeliculas.setEnabled(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las peliculas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFormatosPorPelicula() {
        cbFormatoIdioma.removeAllItems();
        listaFunciones.clear();

        String tituloSeleccionado = (String) cbPeliculas.getSelectedItem();
        if (tituloSeleccionado == null || tituloSeleccionado.equals("No hay peliculas en cartelera")) {
            return;
        }

        Pelicula peliculaSeleccionada = null;
        for (Pelicula p : listaPeliculas) {
            if (p.getTitulo().equalsIgnoreCase(tituloSeleccionado)) {
                peliculaSeleccionada = p;
                break;
            }
        }

        if (peliculaSeleccionada == null) {
            return;
        }

        try {
            listaFunciones = funcionDao.listarPorPelicula(peliculaSeleccionada.getIdPelicula());

            java.util.Set<String> formatosUnicos = new java.util.HashSet<>();

            for (Funcion f : listaFunciones) {
                String formato = (f.isEs3d() ? "3D" : "2D");
                String idioma = f.getIdioma();
                String combinado = formato + " - " + idioma;

                formatosUnicos.add(combinado);
            }

            if (formatosUnicos.isEmpty()) {
                cbFormatoIdioma.addItem("Sin funciones disponibles");
                cbFormatoIdioma.setEnabled(false);
            } else {
                for (String f : formatosUnicos) {
                    cbFormatoIdioma.addItem(f);
                }
                cbFormatoIdioma.setEnabled(true);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los formatos: " + e.getMessage());
        }
    }

    private void cargarFechasYHoras() {
        cbFechasDisponibles.removeAllItems();
        cbHoras.removeAllItems();

        String formatoSeleccionado = (String) cbFormatoIdioma.getSelectedItem();
        if (formatoSeleccionado == null || formatoSeleccionado.equals("Sin funciones disponibles")) {
            return;
        }

        String[] partes = formatoSeleccionado.split(" - ");
        if (partes.length != 2) {
            return;
        }

        String formato = partes[0].trim();
        String idioma = partes[1].trim();

        for (Funcion f : listaFunciones) {
            boolean coincideFormato = (formato.equals("3D") && f.isEs3d()) || (formato.equals("2D") && !f.isEs3d());
            boolean coincideIdioma = idioma.equalsIgnoreCase(f.getIdioma());

            if (coincideFormato && coincideIdioma) {
                java.util.Date inicio = f.getHorainicio();
                if (inicio != null) {
                    java.time.LocalDateTime ldt = inicio.toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime();

                    String fecha = ldt.toLocalDate().toString();
                    String hora = ldt.toLocalTime().withSecond(0).withNano(0).toString();

                    if (((DefaultComboBoxModel<String>) cbFechasDisponibles.getModel()).getIndexOf(fecha) == -1) {
                        cbFechasDisponibles.addItem(fecha);
                    }
                    if (((DefaultComboBoxModel<String>) cbHoras.getModel()).getIndexOf(hora) == -1) {
                        cbHoras.addItem(hora);
                    }
                }
            }
        }

        if (cbFechasDisponibles.getItemCount() == 0) {
            cbFechasDisponibles.addItem("Sin fechas disponibles");
            cbHoras.addItem("-");
            cbFechasDisponibles.setEnabled(false);
            cbHoras.setEnabled(false);
        } else {
            cbFechasDisponibles.setEnabled(true);
            cbHoras.setEnabled(true);
        }
    }

    //ASIENTOS
    private final List<javax.swing.JToggleButton> listaAsientos = new ArrayList<>();

    private void cargarAsientosPorFuncion() {

        for (javax.swing.JToggleButton b : listaAsientos) {
            b.setEnabled(false);
            b.setSelected(false);
        }

        String fechaSel = (String) cbFechasDisponibles.getSelectedItem();
        String horaSel = (String) cbHoras.getSelectedItem();

        if (fechaSel == null || horaSel == null || fechaSel.equals("Sin fechas disponibles")) {
            return;
        }

        try {
            Funcion funcionSeleccionada = null;
            for (Funcion f : listaFunciones) {
                java.time.LocalDateTime inicio = f.getHorainicio().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();

                String fechaFuncion = inicio.toLocalDate().toString();
                String horaFuncion = inicio.toLocalTime().withSecond(0).withNano(0).toString();

                if (fechaFuncion.equals(fechaSel) && horaFuncion.equals(horaSel)) {
                    funcionSeleccionada = f;
                    break;
                }
            }

            if (funcionSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "No se encontro la funcion seleccionada.");
                return;
            }

            AsientoData asientoDao = new AsientoData();
            List<Asiento> asientos = asientoDao.listarTodosPorFuncion(funcionSeleccionada.getIdfuncion());

            if (asientos == null || asientos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay asientos cargados para esta funcion" + funcionSeleccionada.getIdfuncion());
                return;
            }

            int limite = listaAsientos.size();
            for (int i = 0; i < limite; i++) {
                javax.swing.JToggleButton boton = listaAsientos.get(i);
                Asiento asiento = asientos.get(i);

                if (asiento.isEstado()) {
                    boton.setEnabled(true);
                } else {
                    boton.setEnabled(false);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los asientos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblTituloCliente = new javax.swing.JLabel();
        lblSelectAsiento = new javax.swing.JLabel();
        lblDNI = new javax.swing.JLabel();
        lblNombreComprador = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        txtNombreComprador = new javax.swing.JTextField();
        btnBuscarComprador = new javax.swing.JButton();
        lblFechaNac = new javax.swing.JLabel();
        jDateComprador = new com.toedter.calendar.JDateChooser();
        btnAgregarComprador = new javax.swing.JButton();
        lblPeliculaTitulo = new javax.swing.JLabel();
        lblPago = new javax.swing.JLabel();
        lblPelicula = new javax.swing.JLabel();
        lblFormato = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        cbPeliculas = new javax.swing.JComboBox<>();
        cbFormatoIdioma = new javax.swing.JComboBox<>();
        cbFechasDisponibles = new javax.swing.JComboBox<>();
        cbHoras = new javax.swing.JComboBox<>();
        lblMetodoPago = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        cbMedioPago = new javax.swing.JComboBox<>();
        txtPrecioUnit = new javax.swing.JTextField();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnCompra = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblPantalla = new javax.swing.JLabel();
        A1 = new javax.swing.JToggleButton();
        A2 = new javax.swing.JToggleButton();
        A3 = new javax.swing.JToggleButton();
        A4 = new javax.swing.JToggleButton();
        A5 = new javax.swing.JToggleButton();
        A6 = new javax.swing.JToggleButton();
        B1 = new javax.swing.JToggleButton();
        B2 = new javax.swing.JToggleButton();
        B3 = new javax.swing.JToggleButton();
        B4 = new javax.swing.JToggleButton();
        B5 = new javax.swing.JToggleButton();
        B6 = new javax.swing.JToggleButton();
        C1 = new javax.swing.JToggleButton();
        C2 = new javax.swing.JToggleButton();
        C3 = new javax.swing.JToggleButton();
        C4 = new javax.swing.JToggleButton();
        C5 = new javax.swing.JToggleButton();
        C6 = new javax.swing.JToggleButton();
        D1 = new javax.swing.JToggleButton();
        D2 = new javax.swing.JToggleButton();
        D3 = new javax.swing.JToggleButton();
        D4 = new javax.swing.JToggleButton();
        D5 = new javax.swing.JToggleButton();
        D6 = new javax.swing.JToggleButton();
        E1 = new javax.swing.JToggleButton();
        E2 = new javax.swing.JToggleButton();
        E3 = new javax.swing.JToggleButton();
        E4 = new javax.swing.JToggleButton();
        E5 = new javax.swing.JToggleButton();
        E6 = new javax.swing.JToggleButton();

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Venta de Entradas Mostrador");

        lblTituloCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTituloCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloCliente.setText("Cliente");

        lblSelectAsiento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectAsiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectAsiento.setText("Asientos");

        lblDNI.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDNI.setText("DNI:");

        lblNombreComprador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombreComprador.setText("Nombre y Apellido:");

        btnBuscarComprador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-magnifying-glass-tilted-right-48.png"))); // NOI18N
        btnBuscarComprador.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnBuscarComprador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCompradorActionPerformed(evt);
            }
        });

        lblFechaNac.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFechaNac.setText("Fecha de Nacimiento:");

        btnAgregarComprador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/guardar.png"))); // NOI18N
        btnAgregarComprador.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnAgregarComprador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCompradorActionPerformed(evt);
            }
        });

        lblPeliculaTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPeliculaTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPeliculaTitulo.setText("Pelicula");

        lblPago.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPago.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPago.setText("Pago y Confirmacion");

        lblPelicula.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPelicula.setText("Cartelera:");

        lblFormato.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFormato.setText("Formato e Idioma:");

        lblFecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFecha.setText("Funciones Disponibles:");

        lblMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMetodoPago.setText("Metodo de Pago:");

        lblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrecio.setText("Precio Unitario:");

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCantidad.setText("Cantidad:");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotal.setText("Total:");

        btnCompra.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCompra.setText("Confirmar Compra");

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar y Limpiar");

        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSalir.setText("Salir");

        lblPantalla.setBackground(new java.awt.Color(0, 0, 255));
        lblPantalla.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblPantalla.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPantalla.setText("PANTALLA");
        lblPantalla.setOpaque(true);

        A1.setText("A1");

        A2.setText("A2");

        A3.setText("A3");

        A4.setText("A4");

        A5.setText("A5");

        A6.setText("A6");

        B1.setText("B1");

        B2.setText("B2");

        B3.setText("B3");

        B4.setText("B4");

        B5.setText("B5");

        B6.setText("B6");

        C1.setText("C1");

        C2.setText("C2");

        C3.setText("C3");

        C4.setText("C4");

        C5.setText("C5");

        C6.setText("C6");

        D1.setText("D1");

        D2.setText("D2");

        D3.setText("D3");

        D4.setText("D4");

        D5.setText("D5");

        D6.setText("D6");

        E1.setText("E1");

        E2.setText("E2");

        E3.setText("E3");

        E4.setText("E4");

        E5.setText("E5");

        E6.setText("E6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFormato, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbPeliculas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbFechasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbFormatoIdioma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lblTituloCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblDNI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNombreComprador, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAgregarComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombreComprador)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnBuscarComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(lblPeliculaTitulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSelectAsiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPantalla, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(E1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(E2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(E3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(E4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(E5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(E6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(B1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(B2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(B3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(B4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(B5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(B6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(A1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(A2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(A3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(A4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(A5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(A6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(C1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(C2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(C3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(C4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(C5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(C6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(D1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(D2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(D3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(D4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(D5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(D6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(104, 104, 104))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(lblPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecioUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addComponent(lblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
            .addGroup(layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(lblMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMedioPago, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(btnCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTituloCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSelectAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(lblNombreComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(lblFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnBuscarComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jDateComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAgregarComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addComponent(lblPeliculaTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbPeliculas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFormato, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbFormatoIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(E1)
                            .addComponent(E2)
                            .addComponent(E3)
                            .addComponent(E4)
                            .addComponent(E5)
                            .addComponent(E6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(D1)
                            .addComponent(D2)
                            .addComponent(D3)
                            .addComponent(D4)
                            .addComponent(D5)
                            .addComponent(D6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(C1)
                            .addComponent(C2)
                            .addComponent(C3)
                            .addComponent(C4)
                            .addComponent(C5)
                            .addComponent(C6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(B1)
                            .addComponent(B2)
                            .addComponent(B3)
                            .addComponent(B4)
                            .addComponent(B5)
                            .addComponent(B6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(A1)
                            .addComponent(A2)
                            .addComponent(A3)
                            .addComponent(A4)
                            .addComponent(A5)
                            .addComponent(A6))
                        .addGap(18, 18, 18)
                        .addComponent(lblPantalla, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFechasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblPago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbMedioPago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarCompradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCompradorActionPerformed
        String dniTexto = txtDNI.getText().trim();
        if (dniTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI");
            txtDNI.requestFocus();
            return;
        }

        if (dniTexto.length() != 8) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 digitos");
            txtDNI.requestFocus();
            return;
        }

        Integer dni = null;
        try {
            dni = Integer.parseInt(dniTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI solo debe contener numeros");
            txtDNI.requestFocus();
            return;
        }

        try {
            Comprador c = compradorDao.buscarPorDni(dni);
            if (c == null) {
                JOptionPane.showMessageDialog(this,
                        "No existe un cliente con ese DNI.\nComplete los datos para registrarlo.");

                compradorActual = null;
                txtNombreComprador.setText("");
                jDateComprador.setDate(null);

                txtNombreComprador.setEnabled(true);
                jDateComprador.setEnabled(true);
                btnAgregarComprador.setEnabled(true);
                return;
            }

            compradorActual = c;
            txtNombreComprador.setText(c.getNombre());
            if (c.getFecha_nac() != null) {
                jDateComprador.setDate(c.getFecha_nac());
            } else {
                jDateComprador.setDate(null);
            }

            txtNombreComprador.setEnabled(false);
            jDateComprador.setEnabled(false);
            btnAgregarComprador.setEnabled(false);

            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + c.getNombre());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar Cliente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarCompradorActionPerformed

    private void btnAgregarCompradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCompradorActionPerformed

        if (compradorActual != null) {
            JOptionPane.showMessageDialog(this, "El cliente ya existe en la base de datos");
            return;
        }

        String dniTexto = txtDNI.getText().trim();
        if (dniTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI");
            txtDNI.requestFocus();
            return;
        }

        if (dniTexto.length() != 8) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos");
            txtDNI.requestFocus();
            return;
        }

        Integer dni = null;
        try {
            dni = Integer.parseInt(dniTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI solo debe contener numeros");
            txtDNI.requestFocus();
            return;
        }

        String nombre = txtNombreComprador.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre y apellido del Cliente");
            txtNombreComprador.requestFocus();
            return;
        }

        if (!esSoloTexto(nombre)) {
            JOptionPane.showMessageDialog(this, "El nombre solo puede contener letras y espacios");
            txtNombreComprador.requestFocus();
            txtNombreComprador.selectAll();
            return;
        }

        java.util.Date fechaNac = jDateComprador.getDate();
        if (fechaNac == null) {
            JOptionPane.showMessageDialog(this, "Seleccione la fecha de nacimiento");
            jDateComprador.requestFocus();
            return;
        }

        if (fechaNac.after(new java.util.Date())) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser mayor a la actual");
            jDateComprador.requestFocus();
            return;
        }

        try {
            Comprador nuevo = new Comprador();
            nuevo.setDni(dni);
            nuevo.setNombre(nombre);
            nuevo.setFecha_nac(fechaNac);

            compradorDao.crear(nuevo);

            JOptionPane.showMessageDialog(this, "Cliente agregado correctamente");

            txtNombreComprador.setEnabled(false);
            jDateComprador.setEnabled(false);
            btnAgregarComprador.setEnabled(false);
            compradorActual = nuevo;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el cliente: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnAgregarCompradorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton A1;
    private javax.swing.JToggleButton A2;
    private javax.swing.JToggleButton A3;
    private javax.swing.JToggleButton A4;
    private javax.swing.JToggleButton A5;
    private javax.swing.JToggleButton A6;
    private javax.swing.JToggleButton B1;
    private javax.swing.JToggleButton B2;
    private javax.swing.JToggleButton B3;
    private javax.swing.JToggleButton B4;
    private javax.swing.JToggleButton B5;
    private javax.swing.JToggleButton B6;
    private javax.swing.JToggleButton C1;
    private javax.swing.JToggleButton C2;
    private javax.swing.JToggleButton C3;
    private javax.swing.JToggleButton C4;
    private javax.swing.JToggleButton C5;
    private javax.swing.JToggleButton C6;
    private javax.swing.JToggleButton D1;
    private javax.swing.JToggleButton D2;
    private javax.swing.JToggleButton D3;
    private javax.swing.JToggleButton D4;
    private javax.swing.JToggleButton D5;
    private javax.swing.JToggleButton D6;
    private javax.swing.JToggleButton E1;
    private javax.swing.JToggleButton E2;
    private javax.swing.JToggleButton E3;
    private javax.swing.JToggleButton E4;
    private javax.swing.JToggleButton E5;
    private javax.swing.JToggleButton E6;
    private javax.swing.JButton btnAgregarComprador;
    private javax.swing.JButton btnBuscarComprador;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCompra;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbFechasDisponibles;
    private javax.swing.JComboBox<String> cbFormatoIdioma;
    private javax.swing.JComboBox<String> cbHoras;
    private javax.swing.JComboBox<String> cbMedioPago;
    private javax.swing.JComboBox<String> cbPeliculas;
    private com.toedter.calendar.JDateChooser jDateComprador;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblDNI;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaNac;
    private javax.swing.JLabel lblFormato;
    private javax.swing.JLabel lblMetodoPago;
    private javax.swing.JLabel lblNombreComprador;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblPantalla;
    private javax.swing.JLabel lblPelicula;
    private javax.swing.JLabel lblPeliculaTitulo;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblSelectAsiento;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloCliente;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtNombreComprador;
    private javax.swing.JTextField txtPrecioUnit;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
