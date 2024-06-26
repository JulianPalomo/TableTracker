package org.example.models.pagos;

import org.example.models.mesas.Pedido;

public class Factura {
    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private final int id;
    private MetodosDePago metodoDePago;
    private Pedido pedido;
    private double total;

    public Factura(Pedido pedido, MetodosDePago metodoDePago) {
        this.id = contador++;  // Incrementa y asigna el ID
        this.metodoDePago = metodoDePago;
        this.pedido = pedido;
        this.total = pedido.getTotal();
    }

    public Pedido getPedido() {
        return pedido;
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

    public String getNombreArchivo() {
        return "F" + id + ".pdf";
    }

    @Override
    public String toString() {
        return "*************************************************" +
                "GestorFactura.Factura " +
                "N°==>" + id +
                "\n Metodo de Pago=" + metodoDePago +
                "\n Total: $" + total +
                "\n*************************************************";
    }
}