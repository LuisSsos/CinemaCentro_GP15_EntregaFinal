package Persistencia;

import Modelo.Conexion;
import Modelo.Pelicula;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class PeliculaData {

    private final Connection con;   

    public PeliculaData() {
        try {
            con = Conexion.getConexion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarPelicula(Pelicula p) {
        String sql = "INSERT INTO pelicula (titulo,director,actores,origen,genero,estreno,en_cartelera) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDirector());
            ps.setString(3, p.getActores());
            ps.setString(4, p.getOrigen());
            ps.setString(5, p.getGenero());
            ps.setDate(6, new java.sql.Date(p.getEstreno().getTime()));
            ps.setInt(7, p.isEnCartelera() ? 1 : 0);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarPelicula(Pelicula p) {
        String sql = "UPDATE pelicula SET titulo=?, director=?, actores=?, origen=?, genero=?, estreno=?, en_cartelera=? WHERE id_pelicula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDirector());
            ps.setString(3, p.getActores());
            ps.setString(4, p.getOrigen());
            ps.setString(5, p.getGenero());
            ps.setDate(6, new java.sql.Date(p.getEstreno().getTime()));
            ps.setInt(7, p.isEnCartelera() ? 1 : 0);
            ps.setInt(8, p.getIdPelicula());
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarPelicula(int idPelicula) {
        String sql = "DELETE FROM pelicula WHERE id_pelicula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pelicula buscarPelicula(int id) {
        Pelicula pelicula = null;
        String sql = "SELECT * FROM pelicula WHERE id_pelicula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pelicula = new Pelicula();
                    pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                    pelicula.setTitulo(rs.getString("titulo"));
                    pelicula.setDirector(rs.getString("director"));
                    pelicula.setActores(rs.getString("actores"));
                    pelicula.setOrigen(rs.getString("origen"));
                    pelicula.setGenero(rs.getString("genero"));
                    pelicula.setEstreno(rs.getDate("estreno"));
                    pelicula.setEnCartelera(rs.getInt("en_cartelera") == 1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
            e.printStackTrace();
        }
        return pelicula;
    }

    public List<Pelicula> listarPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT * FROM pelicula";
        try (PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                p.setTitulo(rs.getString("titulo"));
                p.setDirector(rs.getString("director"));
                p.setActores(rs.getString("actores"));
                p.setOrigen(rs.getString("origen"));
                p.setGenero(rs.getString("genero"));
                p.setEstreno(rs.getDate("estreno"));
                p.setEnCartelera(rs.getInt("en_cartelera") == 1);
                peliculas.add(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar: " + e.getMessage());
            e.printStackTrace();
        }
        return peliculas;
    }
}