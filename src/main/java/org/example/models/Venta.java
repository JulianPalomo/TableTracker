package org.example.models;

import org.example.models.pagos.MetodosDePago;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {
    private LocalDateTime fecha;
    private String mesero;
    private List<Producto> productosVendidos;
    private double total;
    private MetodosDePago metodoPago;

    public Venta(LocalDateTime fecha, String mesero, List<Producto> productosVendidos, double total, MetodosDePago metodoPago) {
        this.fecha = fecha;
        this.mesero = mesero;
        this.productosVendidos = productosVendidos;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getMesero() {
        return mesero;
    }

    public List<Producto> getProductosVendidos() {
        return productosVendidos;
    }

    public double getTotal() {
        return total;
    }

    public MetodosDePago getMetodoDePago() {
        return metodoPago;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "fecha=" + fecha +
                ", mesero='" + mesero + '\'' +
                ", productosVendidos=" + productosVendidos +
                ", total=" + total +
                ", metodoPago=" + metodoPago +
                '}';
    }
}
