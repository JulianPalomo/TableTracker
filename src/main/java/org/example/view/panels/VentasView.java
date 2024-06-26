//package org.example.view.panels;
//
//import org.example.models.Producto;
//import org.example.models.Venta;
//import org.example.models.pagos.MetodosDePago;
//import org.example.service.Usuario.UsuarioService;
//import org.example.service.VentaService;
//import org.jdatepicker.impl.JDatePanelImpl;
//import org.jdatepicker.impl.JDatePickerImpl;
//import org.jdatepicker.impl.UtilDateModel;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.text.SimpleDateFormat;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class VentasView extends JPanel {
//    private JTable ventasTable;
//    private DefaultTableModel tableModel;
//    private JComboBox<String> meseroComboBox;
//    private JDatePickerImpl datePicker;
//    private JButton aplicarFiltroButton;
//    private JButton cierreCajaButton;
//    private VentaService ventaService;
//    private UsuarioService usuarioService;
//
//    public VentasView(VentaService ventaService) {
//        this.ventaService = ventaService;
//        usuarioService = new UsuarioService();
//        setLayout(new BorderLayout());
//
//        // Panel superior con filtros
//        JPanel filtrosPanel = new JPanel();
//        filtrosPanel.setLayout(new FlowLayout());
//
//        meseroComboBox = new JComboBox<>();
//        meseroComboBox.addItem("Todos los meseros");
//        meseroComboBox =
//        usuarioService.getListaMeseros().forEach(meseroComboBox::addItem);
//        filtrosPanel.add(meseroComboBox);
//
//        Properties p = new Properties();
//        UtilDateModel model = new UtilDateModel();
//        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
//        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
//        filtrosPanel.add(datePicker);
//
//        aplicarFiltroButton = new JButton("Aplicar Filtro");
//        aplicarFiltroButton.addActionListener(e -> aplicarFiltros());
//        filtrosPanel.add(aplicarFiltroButton);
//
//        cierreCajaButton = new JButton("Cierre de Caja");
//        cierreCajaButton.addActionListener(e -> realizarCierreCaja());
//        filtrosPanel.add(cierreCajaButton);
//
//        add(filtrosPanel, BorderLayout.NORTH);
//
//        // Tabla de ventas
//        tableModel = new DefaultTableModel();
//        tableModel.addColumn("Fecha");
//        tableModel.addColumn("Mesero");
//        tableModel.addColumn("Productos Vendidos");
//        tableModel.addColumn("Total");
//        tableModel.addColumn("Método de Pago");
//
//        ventasTable = new JTable(tableModel);
//        add(new JScrollPane(ventasTable), BorderLayout.CENTER);
//
//        // Cargar todas las ventas inicialmente
//        cargarVentas(ventaService.getVentas());
//    }
//
//    private void cargarVentas(List<Venta> ventas) {
//        tableModel.setRowCount(0); // Limpiar la tabla
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        for (Venta venta : ventas) {
//            String productos = venta.getProductosVendidos().stream()
//                    .map(Producto::getNombre)
//                    .collect(Collectors.joining(", "));
//            tableModel.addRow(new Object[]{
//                    dateFormat.format(Date.from(venta.getFecha().atZone(ZoneId.systemDefault()).toInstant())),
//                    venta.getMesero(),
//                    productos,
//                    venta.getTotal(),
//                    venta.getMetodoDePago()
//            });
//        }
//    }
//
//    private void aplicarFiltros() {
//        String meseroSeleccionado = (String) meseroComboBox.getSelectedItem();
//        Date fechaSeleccionada = (Date) datePicker.getModel().getValue();
//
//        List<Venta> ventasFiltradas = ventaService.getVentas().stream()
//                .filter(venta -> {
//                    boolean coincideMesero = meseroSeleccionado.equals("Todos los meseros") ||
//                            venta.getMesero().equals(meseroSeleccionado);
//                    boolean coincideFecha = fechaSeleccionada == null ||
//                            venta.getFecha().toLocalDate().equals(fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//                    return coincideMesero && coincideFecha;
//                })
//                .collect(Collectors.toList());
//
//        cargarVentas(ventasFiltradas);
//    }
//
//    private void realizarCierreCaja() {
//        double totalDebito = ventaService.obtenerTotalPorMetodoPago(MetodosDePago.TARJETA_DEBITO);
//        double totalCredito = ventaService.obtenerTotalPorMetodoPago(MetodosDePago.TARJETA_CREDITO);
//        double totalEfectivo = ventaService.obtenerTotalPorMetodoPago(MetodosDePago.EFECTIVO);
//        double totalGeneral = totalDebito + totalCredito + totalEfectivo;
//
//        String mensaje = String.format("Cierre de Caja:\n\n" +
//                "Total en Débito: %.2f\n" +
//                "Total en Crédito: %.2f\n" +
//                "Total en Efectivo: %.2f\n\n" +
//                "Total General: %.2f", totalDebito, totalCredito, totalEfectivo, totalGeneral);
//
//        JOptionPane.showMessageDialog(this, mensaje, "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);
//    }
//}
