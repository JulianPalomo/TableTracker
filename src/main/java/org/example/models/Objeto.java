package org.example.models;

public abstract class Objeto {

    protected int x;
    protected int y;
    protected int ancho;
    protected int alto;

    public Objeto() {
        this.x = 50;
        this.y = 50;
        this.alto = 50;
        this.ancho = 200;
    }

    public Objeto(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}
