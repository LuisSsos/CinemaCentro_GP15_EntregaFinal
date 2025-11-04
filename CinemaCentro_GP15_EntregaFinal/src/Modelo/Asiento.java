package Modelo;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class Asiento {
    private int id_asiento;
    private int nro_sala;
    private char fila;
    private int numero;
    private boolean estado;

    public Asiento() {
    }

    public Asiento(int id_asiento, int nro_sala, char fila, int numero, boolean estado) {
        this.id_asiento = id_asiento;
        this.nro_sala = nro_sala;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public int getIdasiento() {
        return id_asiento;
    }

    public void setIdasiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getNrosala() {
        return nro_sala;
    }

    public void setNrosala(int nro_sala) {
        this.nro_sala = nro_sala;
    }

    public char getFila() {
        return fila;
    }

    public void setFila(char fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Asiento{" + "id_asiento=" + id_asiento + ", nro_sala=" + nro_sala + ", fila=" + fila + ", numero=" + numero + ", estado=" + estado + '}';
    }
    
    
    
}
