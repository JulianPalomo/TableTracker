package org.example.view.controllers;
import org.example.models.Categoria;
import org.example.models.Producto;
import org.example.service.ProductoService;
import org.example.view.MenuLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductoController {

    private final ProductoService productoService;

    public ProductoController() {
        this.productoService = new ProductoService();
    }



}