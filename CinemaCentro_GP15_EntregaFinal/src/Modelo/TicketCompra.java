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

public class TicketCompra {
    
    private int id_ticket;
    private int id_comprador;
    private int id_funcion;
    private Date fecha_compra;
    private java.math.BigDecimal precio_unitario;
    private java.math.BigDecimal monto_total;
    private String Canal;
    private String medio_pago;
    private int cantidad;
    public TicketCompra() {
    }

    public TicketCompra(int id_ticket, int id_comprador, int id_funcion, Date fecha_compra, BigDecimal precio_unitario, BigDecimal monto_total, String Canal,int cantidad, String medio_pago) {
        this.id_ticket = id_ticket;
        this.id_comprador = id_comprador;
        this.id_funcion = id_funcion;
        this.fecha_compra = fecha_compra;
        this.precio_unitario = precio_unitario;
        this.monto_total = monto_total;
        this.Canal = Canal;
        this.medio_pago = medio_pago;
        this.cantidad = cantidad;
    }

    public int getIdticket() {
        return id_ticket;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public void setIdticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public int getIdcomprador() {
        return id_comprador;
    }

    public void setIdcomprador(int id_comprador) {
        this.id_comprador = id_comprador;
    }

    public int getIdfuncion() {
        return id_funcion;
    }

    public void setIdfuncion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public Date getFechacompra() {
        return fecha_compra;
    }

    public void setFechacompra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public BigDecimal getPreciounitario() {
        return precio_unitario;
    }

    public void setPreciounitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getMontototal() {
        return monto_total;
    }

    public void setMontototal(BigDecimal monto_total) {
        this.monto_total = monto_total;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String Canal) {
        this.Canal = Canal;
    }

    public String getMediopago() {
        return medio_pago;
    }

    public void setMediopago(String medio_pago) {
        this.medio_pago = medio_pago;
    }

    @Override
    public String toString() {
        return "TicketCompra{" + "id_ticket=" + id_ticket + ", id_comprador=" + id_comprador + ", id_funcion=" + id_funcion + ", fecha_compra=" + fecha_compra + ", precio_unitario=" + precio_unitario + ", monto_total=" + monto_total + ", Canal=" + Canal + ", medio_pago=" + medio_pago +", cantidad=" + cantidad + '}';
    }

    
    
}
