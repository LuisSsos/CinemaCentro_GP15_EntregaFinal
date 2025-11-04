package Persistencia;

import Modelo.Conexion;
import Modelo.TicketCompra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class TicketCompraData {

    private final Connection con;

    public TicketCompraData() {
        try {
            con = Conexion.getConexion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int crear(TicketCompra t) throws SQLException {
        String sql = "INSERT INTO ticket_compra (id_comprador, fecha_compra, precio_unitario, monto_total, canal, medio_pago, cantidad, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getIdcomprador());
            ps.setBigDecimal(2, t.getPreciounitario());
            ps.setBigDecimal(3, t.getMontototal());
            ps.setString(4, t.getCanal());
            ps.setString(5, t.getMediopago());
            ps.setInt(6, t.getCantidad());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Error. No se genero el ticket");
    }

    public void anular(int idTicket) throws SQLException {
        String sql = "UPDATE ticket_compra SET estado='ANULADO' WHERE id_ticket=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);
            ps.executeUpdate();
        }
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
        try {
            t.setIdfuncion(rs.getInt("id_funcion"));
        } catch (SQLException ignore) {
        }
        t.setFechacompra(rs.getTimestamp("fecha_compra"));
        t.setPreciounitario(rs.getBigDecimal("precio_unitario"));
        t.setMontototal(rs.getBigDecimal("monto_total"));
        try {
            t.setCanal(rs.getString("canal"));
        } catch (SQLException ignore) {
        }
        try {
            t.setMediopago(rs.getString("medio_pago"));
        } catch (SQLException ignore) {
        }
        try {
            t.setCantidad(rs.getInt("cantidad"));
        } catch (SQLException ignore) {
        }
        return t;
    }
}   
        
        
        

    
    
    
    
    
    

