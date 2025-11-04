package Persistencia;

import Modelo.Conexion;
import Modelo.Sala;
import java.sql.*;
import java.util.*;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class SalaData {
    
    private final Connection con;
    
    public SalaData() {
        try { con = Conexion.getConexion(); } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public int crear(Sala s) throws SQLException {
        String sql = "INSERT INTO sala(nro_sala, apta_3d, capacidad, estado) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getNroSala());
            ps.setBoolean(2, s.isApta3D());
            ps.setInt(3, s.getCapacidad());
            ps.setBoolean(4, s.isEstado());
            return ps.executeUpdate();
        }
    }

    public void actualizar(Sala s) throws SQLException {
        String sql = "UPDATE sala SET apta_3d=?, capacidad=?, estado=? WHERE nro_sala=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, s.isApta3D());
            ps.setInt(2, s.getCapacidad());
            ps.setBoolean(3, s.isEstado());
            ps.setInt(4, s.getNroSala());
            ps.executeUpdate();
        }
    }

    public void eliminar(int nroSala) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM sala WHERE nro_sala=?")) {
            ps.setInt(1, nroSala);
            ps.executeUpdate();
        }
    }

    public Sala buscarPorId(int nroSala) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM sala WHERE nro_sala=?")) {
            ps.setInt(1, nroSala);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Sala s = new Sala();
                s.setNroSala(rs.getInt("nro_sala"));
                s.setApta3D(rs.getBoolean("apta_3d"));
                s.setCapacidad(rs.getInt("capacidad"));
                s.setEstado(rs.getBoolean("estado"));
                return s;
            }
        }
    }

    public List<Sala> listarActivas() throws SQLException {
        String sql = "SELECT * FROM sala WHERE estado=1 ORDER BY nro_sala";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Sala> out = new ArrayList<>();
            while (rs.next()) {
                Sala s = new Sala();
                s.setNroSala(rs.getInt("nro_sala"));
                s.setApta3D(rs.getBoolean("apta_3d"));
                s.setCapacidad(rs.getInt("capacidad"));
                s.setEstado(rs.getBoolean("estado"));
                out.add(s);
            }
            return out;
        }
    }
    
}
