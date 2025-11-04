package Modelo;

import java.math.BigDecimal;
import java.util.Date;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class Funcion {
    private int id_funcion;
    private int id_pelicula;
    private int nro_sala;
    private String idioma;
    private boolean es_3d;
    private boolean subtitulada;
    private Date hora_inicio;
    private Date hora_fin;
    private int lugares_disponibles;
    private java.math.BigDecimal precio_tipo;

    public Funcion() {
    }

    public Funcion(int id_funcion, int id_pelicula, int nro_sala, String idioma, boolean es_3d, boolean subtitulada, Date hora_inicio, Date hora_fin, int lugares_disponibles, BigDecimal precio_tipo) {
        this.id_funcion = id_funcion;
        this.id_pelicula = id_pelicula;
        this.nro_sala = nro_sala;
        this.idioma = idioma;
        this.es_3d = es_3d;
        this.subtitulada = subtitulada;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.lugares_disponibles = lugares_disponibles;
        this.precio_tipo = precio_tipo;
    }

    public int getIdfuncion() {
        return id_funcion;
    }

    public void setIdfuncion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public int getIdpelicula() {
        return id_pelicula;
    }

    public void setIdpelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public int getNrosala() {
        return nro_sala;
    }

    public void setNrosala(int nro_sala) {
        this.nro_sala = nro_sala;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public boolean isEs3d() {
        return es_3d;
    }

    public void setEs3d(boolean es_3d) {
        this.es_3d = es_3d;
    }

    public boolean isSubtitulada() {
        return subtitulada;
    }

    public void setSubtitulada(boolean subtitulada) {
        this.subtitulada = subtitulada;
    }

    public Date getHorainicio() {
        return hora_inicio;
    }

    public void setHorainicio(Date hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public Date getHorafin() {
        return hora_fin;
    }

    public void setHorafin(Date hora_fin) {
        this.hora_fin = hora_fin;
    }

    public int getLugaresdisponibles() {
        return lugares_disponibles;
    }

    public void setLugaresdisponibles(int lugares_disponibles) {
        this.lugares_disponibles = lugares_disponibles;
    }

    public BigDecimal getPreciotipo() {
        return precio_tipo;
    }

    public void setPreciotipo(BigDecimal precio_tipo) {
        this.precio_tipo = precio_tipo;
    }

    @Override
    public String toString() {
        return "Funcion{" + "id_funcion=" + id_funcion + ", id_pelicula=" + id_pelicula + ", nro_sala=" + nro_sala + ", idioma=" + idioma + ", es_3d=" + es_3d + ", subtitulada=" + subtitulada + ", hora_inicio=" + hora_inicio + ", hora_fin=" + hora_fin + ", lugares_disponibles=" + lugares_disponibles + ", precio_tipo=" + precio_tipo + '}';
    }
   
   
}
