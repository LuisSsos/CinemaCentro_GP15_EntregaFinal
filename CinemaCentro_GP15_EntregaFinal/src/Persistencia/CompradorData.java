package Persistencia;

import Modelo.Comprador;
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

public class CompradorData {
    private final Connection con;
    public CompradorData() {
        try { con = Conexion.getConexion(); } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public int crear(Comprador c) throws SQLException {
        String sql = "INSERT INTO comprador(dni, nombre, fecha_nac, contraseña, email) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setDate(3, c.getFecha_nac() != null ? new java.sql.Date(c.getFecha_nac().getTime()) : null);
            ps.setString(4, c.getContraseña());
            ps.setString(5, c.getEmail());
            ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int id = rs.getInt(1);
                c.setIdcomprador(id); // 🔹 Guardamos el ID generado en el objeto
                return id;
            }
        }
        throw new SQLException("No se genero id_comprador");
    }
    }
    /*public int crear(Comprador c) {
    String sql = "INSERT INTO comprador (dni, nombre, fecha_nac, contrasena, email) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, c.getDni());
        ps.setString(2, c.getNombre());
        ps.setDate(3, new java.sql.Date(c.getFecha_nac().getTime()));
        ps.setString(4, c.getContrasena());
        ps.setString(5, c.getEmail());
        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int id = rs.getInt(1);
                c.setIdcomprador(id); // 🔹 Guardamos el ID generado en el objeto
                return id;
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al crear comprador: " + ex.getMessage());
    }
    return -1;
}
 */  

    public void actualizar(Comprador c) throws SQLException {
        String sql = "UPDATE comprador SET dni=?, nombre=?, fecha_nac=?, contraseña=?, email=? WHERE id_comprador=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setDate(3, c.getFecha_nac() != null ? new java.sql.Date(c.getFecha_nac().getTime()) : null);
            ps.setString(4, c.getContraseña());
            ps.setString(5, c.getEmail());
            ps.setInt(6, c.getIdcomprador());
            ps.executeUpdate();
        }
    }

    public void eliminar(int idComprador) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM comprador WHERE id_comprador=?")) {
            ps.setInt(1, idComprador); ps.executeUpdate();
        }
    }

    public Comprador buscarPorId(int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM comprador WHERE id_comprador=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public Comprador buscarPorDni(int dni) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM comprador WHERE dni=?")) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    private Comprador map(ResultSet rs) throws SQLException {
        Comprador c = new Comprador();
        c.setIdcomprador(rs.getInt("id_comprador"));
        c.setDni(rs.getInt("dni"));
        c.setNombre(rs.getString("nombre"));
        java.sql.Date sqlDate = rs.getDate("fecha_nac");
if (sqlDate != null) {
    c.setFecha_nac(new java.util.Date(sqlDate.getTime()));
} else {
    c.setFecha_nac(null);
}
        c.setContraseña(rs.getString("contraseña"));
        c.setEmail(rs.getString("email"));
        return c;
    }
}
