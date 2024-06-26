package org.example.models.objetos;

public class Pared extends Objeto {
    private int nroPared;
    private static int nroParedAuto = 1;

    public Pared(){
        super();
        this.nroPared = nroParedAuto;
        nroParedAuto++;
    }
    public static void decrementarNumeroAuto(){
        nroParedAuto--;
    }
}