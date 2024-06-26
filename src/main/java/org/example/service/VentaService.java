package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Utils.LocalDateTimeAdapter;
import org.example.models.Producto;
import org.example.models.Venta;
import org.example.models.pagos.MetodosDePago;
import org.example.models.personas.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class VentaService {

    private List<Venta> ventas;
    private final String VENTAS_PATH = "src/main/java/org/example/resource/ventas.json";
    private Gson gson;

    public VentaService() {
        gson = createGson();  // Inicializa Gson en el constructor
        ventas = cargarVentas();
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void registrarVenta(Venta venta) {
        ventas.add(venta);
        guardarVenta(venta);
    }

    public double obtenerTotalPorMetodoPago(MetodosDePago metodoPago) {
        return ventas.stream()
                .filter(venta -> venta.getMetodoDePago() == metodoPago)
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    public double obtenerTotalVentasPorMes(Month mes) {
        return ventas.stream()
                .filter(venta -> venta.getFecha().getMonth() == mes)
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    public String obtenerEstadisticas() {
        StringBuilder estadisticas = new StringBuilder();

        Month mesActual = LocalDate.now().getMonth();
        String mesActualEnEspanol = mesActual.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

        estadisticas.append("Ventas del mes: ").append(mesActualEnEspanol).append("\n");

        estadisticas.append("Total en débito: ").append(obtenerTotalPorMetodoPago(MetodosDePago.TARJETA_DEBITO)).append("\n");
        estadisticas.append("Total en crédito: ").append(obtenerTotalPorMetodoPago(MetodosDePago.TARJETA_CREDITO)).append("\n");
        estadisticas.append("Total en transferencia bancaria: ").append(obtenerTotalPorMetodoPago(MetodosDePago.TRANSFERENCIA_BANCARIA)).append("\n");
        estadisticas.append("Total en otro: ").append(obtenerTotalPorMetodoPago(MetodosDePago.OTRO)).append("\n");
        estadisticas.append("Total en efectivo: ").append(obtenerTotalPorMetodoPago(MetodosDePago.EFECTIVO)).append("\n");

        estadisticas.append("Total ventas en ").append(mesActualEnEspanol).append(": ").append(obtenerTotalVentasPorMes(mesActual)).append("\n");

        return estadisticas.toString();
    }

    private void guardarVenta(Venta venta) {
        try (Writer writer = new FileWriter(VENTAS_PATH)) {
            gson.toJson(ventas, writer);  // Guarda toda la lista de ventas
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Venta> cargarVentas() {
        try (Reader reader = new FileReader(VENTAS_PATH)) {
            Type ventaListType = new TypeToken<ArrayList<Venta>>() {}.getType();
            ventas = gson.fromJson(reader, ventaListType);
            if (ventas == null) {
                ventas = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, simplemente iniciamos con una lista vacía
            ventas = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ventas;
    }

    public List<Venta> getVentas() {
        return cargarVentas();
    }
}
