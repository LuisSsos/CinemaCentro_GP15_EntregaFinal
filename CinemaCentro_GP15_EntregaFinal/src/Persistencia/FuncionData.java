package Persistencia;

import Modelo.Conexion;
import Modelo.Funcion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class FuncionData {
    private final Connection con;
    public FuncionData() {
        try { con = Conexion.getConexion(); } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public int crear(Funcion f) throws SQLException {
        String sql = "INSERT INTO funcion(id_pelicula, nro_sala, idioma, es_3d, subtitulada,hora_inicio, hora_fin, lugares_disponibles, precio_tipo) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, f.getIdpelicula());
            ps.setInt(2, f.getNrosala());
            ps.setString(3, f.getIdioma());
            ps.setBoolean(4, f.isEs3d());
            ps.setBoolean(5, f.isSubtitulada());
            ps.setTimestamp(6, new Timestamp(f.getHorainicio().getTime()));
            ps.setTimestamp(7, new Timestamp(f.getHorafin().getTime()));
            ps.setInt(8, f.getLugaresdisponibles());
            ps.setBigDecimal(9, f.getPreciotipo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        throw new SQLException("No se genero id_funcion");
    }

    public void actualizar(Funcion f) throws SQLException {
        String sql = "UPDATE funcion SET id_pelicula=?, nro_sala=?, idioma=?, es_3d=?, subtitulada=?, hora_inicio=?, hora_fin=?, lugares_disponibles=?, precio_tipo=?" +
            "\nWHERE id_funcion=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getIdpelicula());
            ps.setInt(2, f.getNrosala());
            ps.setString(3, f.getIdioma());
            ps.setBoolean(4, f.isEs3d());
            ps.setBoolean(5, f.isSubtitulada());
            ps.setTimestamp(6, new Timestamp(f.getHorainicio().getTime()));
            ps.setTimestamp(7, new Timestamp(f.getHorafin().getTime()));
            ps.setInt(8, f.getLugaresdisponibles());
            ps.setBigDecimal(9, f.getPreciotipo());
            ps.setInt(10, f.getIdfuncion());
            ps.executeUpdate();
        }
    }

    public void eliminar(int idFuncion) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM funcion WHERE id_funcion=?")) {
            ps.setInt(1, idFuncion); ps.executeUpdate();
        }
    }

    public Funcion buscarPorId(int idFuncion) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM funcion WHERE id_funcion=?")) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Funcion f = new Funcion();
                f.setIdfuncion(rs.getInt("id_funcion"));
                f.setIdpelicula(rs.getInt("id_pelicula"));
                f.setNrosala(rs.getInt("nro_sala"));
                f.setIdioma(rs.getString("idioma"));
                f.setEs3d(rs.getBoolean("es_3d"));
                f.setSubtitulada(rs.getBoolean("subtitulada"));
                f.setHorainicio(rs.getTimestamp("hora_inicio"));
                f.setHorafin(rs.getTimestamp("hora_fin"));
                f.setLugaresdisponibles(rs.getInt("lugares_disponibles"));
                f.setPreciotipo(rs.getBigDecimal("precio_tipo"));
                return f;
            }
        }
    }

    public List<Funcion> listarPorPelicula(int idPelicula) throws SQLException {
        String sql = "SELECT * FROM funcion WHERE id_pelicula=? ORDER BY hora_inicio";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            try (ResultSet rs = ps.executeQuery()) {
                List<Funcion> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Funcion> listarPorSalaYFecha(int nroSala, LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM funcion WHERE nro_sala=? AND DATE(hora_inicio)=? ORDER BY hora_inicio";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nroSala);
            ps.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                List<Funcion> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public boolean existeSolapado(int nroSala, LocalDateTime ini, LocalDateTime fin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM funcion" +
            "\nWHERE nro_sala=? AND ( (? < hora_fin) AND (? > hora_inicio) )";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nroSala);
            ps.setTimestamp(2, Timestamp.valueOf(ini));
            ps.setTimestamp(3, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1) > 0; }
        }
    }

    public void generarAsientos(int idFuncion, List<String> filas, int porFila) throws SQLException {
        String sql = "INSERT INTO asiento(id_funcion, fila, numero, estado) VALUES(?,?,?,'LIBRE')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (String f : filas) {
                for (int n = 1; n <= porFila; n++) {
                    ps.setInt(1, idFuncion);
                    ps.setString(2, f);
                    ps.setInt(3, n);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private Funcion map(ResultSet rs) throws SQLException {
        Funcion f = new Funcion();
        f.setIdfuncion(rs.getInt("id_funcion"));
        f.setIdpelicula(rs.getInt("id_pelicula"));
        f.setNrosala(rs.getInt("nro_sala"));
        f.setIdioma(rs.getString("idioma"));
        f.setEs3d(rs.getBoolean("es_3d"));
        f.setSubtitulada(rs.getBoolean("subtitulada"));
        f.setHorainicio(rs.getTimestamp("hora_inicio"));
        f.setHorafin(rs.getTimestamp("hora_fin"));
        f.setLugaresdisponibles(rs.getInt("lugares_disponibles"));
        f.setPreciotipo(rs.getBigDecimal("precio_tipo"));
        return f;
    }
}
