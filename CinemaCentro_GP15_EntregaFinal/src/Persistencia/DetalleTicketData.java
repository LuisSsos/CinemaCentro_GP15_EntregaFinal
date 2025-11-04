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

    public void insertar(int idTicket, int idAsiento, BigDecimal precioUnitario) throws SQLException {
        String sql = "INSERT INTO detalle_ticket (id_ticket, id_asiento, precio_unitario) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            ps.setInt(2, idAsiento);
            ps.setBigDecimal(3, precioUnitario);
            ps.executeUpdate();
        }
    }

    public List<DetalleTicket> listarPorTicket(int idTicket) throws SQLException {
        String sql = "SELECT id_detalle, id_ticket, id_asiento FROM detalle_ticket WHERE id_ticket= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetalleTicket> out = new ArrayList<>();
                while (rs.next()) {
                    DetalleTicket d = new DetalleTicket();
                    d.setIddetalle(rs.getInt("id_detalle"));
                    d.setIdticket(rs.getInt("id_ticket"));
                    d.setIdasiento(rs.getInt("id_asiento"));
                    out.add(d);
                }
                return out;
            }
        }
    }

    //vista completa del ticket/entrada para imprimir
    public static class DetalleTicketVista {

        public final String titulo;
        public final int nroSala;
        public final LocalDate fechaFuncion;
        public final LocalTime horaInicio;
        public final String fila;
        public final int numero;
        public final BigDecimal precioUnitario;

        public DetalleTicketVista(String titulo, int nroSala, LocalDate fechaFuncion, LocalTime horaInicio,
                String fila, int numero, BigDecimal precioUnitario) {
            this.titulo = titulo;
            this.nroSala = nroSala;
            this.fechaFuncion = fechaFuncion;
            this.horaInicio = horaInicio;
            this.fila = fila;
            this.numero = numero;
            this.precioUnitario = precioUnitario;
        }
    }

    public List<DetalleTicketVista> listarVistaPorTicket(int idTicket) throws SQLException {
        String sql = "SELECT p.titulo, s.nro_sala, DATE(f.hora_inicio) AS fecha_funcion, TIME(f.hora_inicio) AS hora_inicio, "
                + "a.fila, a.numero, dt.precio_unitario "
                + "FROM detalle_ticket dt "
                + "JOIN asiento a ON a.id_asiento = dt.id_asiento "
                + "JOIN funcion f ON f.id_funcion = a.id_funcion "
                + "JOIN pelicula p ON p.id_pelicula = f.id_pelicula "
                + "JOIN sala s ON s.nro_sala = f.nro_sala "
                + "WHERE dt.id_ticket = ? "
                + "ORDER BY s.nro_sala, a.fila, a.numero";

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
                            rs.getBigDecimal("precio_unitario")
                    ));
                }
                return out;
            }
        }
    }

}








