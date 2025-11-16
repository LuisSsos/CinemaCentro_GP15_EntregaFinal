package Persistencia;

import Modelo.Conexion;
import Modelo.TicketCompra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Grupo 15 Luis Ezequiel Sosa Lucas Saidman Luca Rodrigaño Ignacio
 * Rodriguez
 *
 */
public class TicketCompraData {

    private final Connection con;

    public TicketCompraData() {
        try {
            con = Conexion.getConexion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int crear(TicketCompra t) {
        String sql = "INSERT INTO ticket_compra (id_comprador, fecha_compra, precio_unitario, cantidad, monto_total, canal, medio_pago) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getIdcomprador());
            ps.setTimestamp(2, new Timestamp(t.getFechacompra().getTime()));
            ps.setBigDecimal(3, t.getPreciounitario());
            ps.setInt(4, t.getCantidad());
            ps.setBigDecimal(5, t.getMontototal());
            ps.setString(6, t.getCanal());
            ps.setString(7, t.getMediopago());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear ticket: " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public int actualizar(TicketCompra t) throws SQLException {

        String sql = "UPDATE ticket_compra SET id_comprador=?, fecha_compra=?, cantidad=?, monto_total=?, canal=?, medio_pago=? WHERE id_ticket=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getIdcomprador());
            ps.setTimestamp(2, new Timestamp(t.getFechacompra().getTime()));
            ps.setInt(3, t.getCantidad());
            ps.setBigDecimal(4, t.getMontototal());
            ps.setString(5, t.getCanal());
            ps.setString(6, t.getMediopago());
            ps.setInt(7, t.getIdticket());
            return ps.executeUpdate();

        }
    }

    public TicketCompra buscarPorId(int idTicket) throws SQLException {
        String sql = "SELECT * FROM ticket_compra WHERE id_ticket=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public void anular(int idTicket) throws SQLException {
        String sql = "DELETE FROM ticket_compra WHERE id_ticket=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            ps.executeUpdate();
        }
    }

    public List<TicketCompra> listarTodos() throws SQLException {
        String sql = "SELECT * FROM ticket_compra ORDER BY fecha_compra DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<TicketCompra> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(map(rs));
                }
                return out;
            }
        }
    }

    public List<TicketCompra> buscarPorComprador(int idComprador) throws SQLException {
        List<TicketCompra> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket_compra WHERE id_comprador=? ORDER BY fecha_compra DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idComprador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(map(rs));
                }
            }
        }
        return tickets;
    }

    public List<TicketCompra> listarPorFecha(java.time.LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM ticket_compra WHERE DATE(fecha_compra)=? ORDER BY id_ticket";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                List<TicketCompra> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(map(rs));
                }
                return out;
            }
        }
    }

    private TicketCompra map(ResultSet rs) throws SQLException {
        TicketCompra t = new TicketCompra();
        t.setIdticket(rs.getInt("id_ticket"));
        t.setIdcomprador(rs.getInt("id_comprador"));
        t.setFechacompra(rs.getTimestamp("fecha_compra"));
        t.setCantidad(rs.getInt("cantidad"));
        t.setMontototal(rs.getBigDecimal("monto_total"));
        t.setCanal(rs.getString("canal"));
        t.setMediopago(rs.getString("medio_pago"));
        t.setPreciounitario(rs.getBigDecimal("precio_unitario"));
        return t;
    }
}
