package Persistencia;

import Modelo.Conexion;
import Modelo.DetalleTicket;
import Modelo.TicketCompra;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Grupo 15 Luis Ezequiel Sosa Lucas Saidman Luca Rodrigaño Ignacio
 * Rodriguez
*
 */
public class DetalleTicketData {

    private final Connection con;

    public DetalleTicketData() {
        try {
            con = Conexion.getConexion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

public void insertar(int idTicket, int idAsiento) throws SQLException {
    String sql = "INSERT INTO detalle_ticket (id_ticket, id_asiento) VALUES (?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idTicket);
        ps.setInt(2, idAsiento);
        ps.executeUpdate();
    }
}

public List<Integer> obtenerAsientosOcupadosPorFuncion(int idFuncion) throws SQLException {
    String sql =
        "\nSELECT dt.id_asiento" +
        "\nFROM detalle_ticket dt" +
        "\nJOIN ticket_compra tc ON dt.id_ticket = tc.id_ticket" +
        "\nJOIN asiento a ON dt.id_asiento = a.id_asiento" +
        "\nWHERE a.id_funcion = ?";
    List<Integer> asientosOcupados = new ArrayList<>();

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idFuncion);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asientosOcupados.add(rs.getInt("id_asiento"));
            }
        }
    }
    return asientosOcupados;
}

    public List<DetalleTicket> listarPorTicket(int idTicket) throws SQLException {
        String sql = "SELECT id_detalle, id_ticket, id_asiento FROM detalle_ticket WHERE id_ticket= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetalleTicket> out = new ArrayList<>();
                while (rs.next()) {
                    DetalleTicket d = new DetalleTicket();
                    d.setId_detalle(rs.getInt("id_detalle"));
                    d.setId_ticket(rs.getInt("id_ticket"));
                    d.setId_asiento(rs.getInt("id_asiento"));
                    out.add(d);
                }
                return out;
            }
        }
    }

    public static class DetalleTicketVista {

        public final String titulo;
        public final int nroSala;
        public final java.time.LocalDate fechaFuncion;
        public final java.time.LocalTime horaInicio;
        public final String fila;
        public final int numero;
        public final boolean es3d;
        public final String idioma;
        public final boolean subtitulada;
        public final java.math.BigDecimal precioUnitario;

        public DetalleTicketVista(String titulo,
                                  int nroSala,
                                  java.time.LocalDate fechaFuncion,
                                  java.time.LocalTime horaInicio,
                                  String fila,
                                  int numero,
                                  boolean es3d,
                                  String idioma,
                                  boolean subtitulada,
                                  java.math.BigDecimal precioUnitario) {
            this.titulo = titulo;
            this.nroSala = nroSala;
            this.fechaFuncion = fechaFuncion;
            this.horaInicio = horaInicio;
            this.fila = fila;
            this.numero = numero;
            this.es3d = es3d;
            this.idioma = idioma;
            this.subtitulada = subtitulada;
            this.precioUnitario = precioUnitario;
        }
    }

    public List<DetalleTicketVista> listarVistaPorTicket(int idTicket) throws SQLException {
        String sql = "SELECT p.titulo, " +
                    "\ns.nro_sala, " +
                    "\nDATE(f.hora_inicio) AS fecha_funcion, " +
                    "\nTIME(f.hora_inicio) AS hora_inicio, " +
                    "\na.fila, " +
                    "\na.numero, " +
                    "\nf.es_3d AS es3d, " +
                    "\nf.idioma, " +
                    "\nf.subtitulada, " +
                    "\ntc.precio_unitario " +
                    "\nFROM detalle_ticket dt " +
                    "\nJOIN asiento a ON a.id_asiento = dt.id_asiento " +
                    "\nJOIN funcion f ON f.id_funcion = a.id_funcion " +
                    "\nJOIN pelicula p ON p.id_pelicula = f.id_pelicula " +
                    "\nJOIN sala s ON s.nro_sala = f.nro_sala " +
                    "\nJOIN ticket_compra tc ON tc.id_ticket = dt.id_ticket " +
                    "\nWHERE dt.id_ticket = ? " +
                    "\nORDER BY s.nro_sala, a.fila, a.numero";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetalleTicketVista> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new DetalleTicketVista(
                            rs.getString("titulo"),
                            rs.getInt("nro_sala"),
                            rs.getDate("fecha_funcion").toLocalDate(),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getString("fila"),
                            rs.getInt("numero"),
                            rs.getBoolean("es3d"),
                            rs.getString("idioma"),
                            rs.getBoolean("subtitulada"),
                            rs.getBigDecimal("precio_unitario")
                    ));
                }
                return out;
            }
        }
    }
    
}