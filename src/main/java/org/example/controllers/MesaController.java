package org.example.controllers;

import org.example.models.Mesa;
import org.example.service.MesaService;

import java.util.Map;

public class MesaController {

    private Map<Integer, Mesa> mesas;
    private MesaService mesaService;

    public MesaController(){
        this.mesas = mesaService.cargarDatosDesdeJson();
    }

}
