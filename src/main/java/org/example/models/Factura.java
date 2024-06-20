package org.example.models;

//import com.itextpdf.text.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private final int id;
    private int nroMesa;

    private MetodosDePago metodoDePago;

    private List<Producto> pedido;

    private LocalDateTime fecha;

    public Factura(MetodosDePago metodoDePago, List<Producto> pedido) {

        synchronized (Factura.class) {
            this.id = contador++;  // Incrementa y asigna el ID
        }
        this.metodoDePago = metodoDePago;
        pedido= new ArrayList<>();

        fecha= LocalDateTime.now();
    }
    public List<Producto> getPedido(){
        return this.pedido;
    }

    public MetodosDePago getMetodoDePago() {
        return metodoDePago;
    }

    public LocalDateTime getFecha() {
        return fecha;
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

    public String fileNameFecha(){
        // Obtener la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();
        // Formatear la fecha y hora para que sea adecuada como parte del nombre del archivo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = now.format(formatter);

        // Crear el nombre del archivo con la fecha y hora
        String fileName = "F" +formattedDateTime + ".pdf";
        return  fileName;

    }
}