package Persistencia;

import Modelo.Asiento;
import Modelo.Conexion;
import java.sql.*;
import java.util.*;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class AsientoData {
    private final Connection con;
    public AsientoData() {
        try { con = Conexion.getConexion(); } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Asiento> listarTodosPorFuncion(int idFuncion) throws SQLException {
        String sql = "SELECT * FROM asiento WHERE id_funcion=? ORDER BY fila, numero";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) {
                List<Asiento> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Asiento> listarLibres(int idFuncion) throws SQLException {
        String sql = "SELECT * FROM asiento WHERE id_funcion=? AND estado='LIBRE' ORDER BY fila, numero";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) {
                List<Asiento> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public boolean ocuparSiLibre(int idAsiento) throws SQLException {
        String sql = "UPDATE asiento SET estado='OCUPADO' WHERE id_asiento=? AND estado='LIBRE'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsiento);
            return ps.executeUpdate() == 1;
        }
    }

    public void inhabilitar(int idAsiento) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE asiento SET estado='INHABILITADO' WHERE id_asiento=?")) {
            ps.setInt(1, idAsiento); ps.executeUpdate();
        }
    }

    public void habilitar(int idAsiento) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE asiento SET estado='LIBRE' WHERE id_asiento=? AND estado='INHABILITADO'")) {
            ps.setInt(1, idAsiento); ps.executeUpdate();
        }
    }

    public void liberarPorTicket(int idTicket) throws SQLException {
        String sql = "UPDATE asiento a JOIN detalle_ticket dt ON dt.id_asiento = a.id_asiento SET a.estado='LIBRE' WHERE dt.id_ticket=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket); ps.executeUpdate();
        }
    }

    public int contarLibresFuncion(int idFuncion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asiento WHERE id_funcion=? AND estado='LIBRE'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    private Asiento map(ResultSet rs) throws SQLException {
        Asiento a = new Asiento();
        a.setIdasiento(rs.getInt("id_asiento"));
        a.setNrosala(rs.getInt("nro_sala"));
        a.setFila(rs.getString("fila").charAt(0));
        a.setNumero(rs.getInt("numero"));
        a.setEstado("LIBRE".equals(rs.getString("estado")));
        return a;
    }
}
