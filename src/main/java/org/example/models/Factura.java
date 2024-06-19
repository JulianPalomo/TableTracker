package org.example.models;

public class Factura {
    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private final int id;
    private MetodosDePago metodoDePago;
    private Pedido pedido;
    private double total;

    public Factura(MetodosDePago metodoDePago,Pedido pedido) {
        synchronized (Factura.class) {
            this.id = contador++;  // Incrementa y asigna el ID
        }
        this.metodoDePago = metodoDePago;
        this.pedido = pedido;
    }
    public Pedido getPedido(){
        return this.pedido;
    }

    public MetodosDePago getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(MetodosDePago metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "*************************************************" +
                "GestorFactura.Factura " +
                "N°==>" + id +
                "\n Metodo de Pago=" + metodoDePago +
                "\n*************************************************";
    }
}