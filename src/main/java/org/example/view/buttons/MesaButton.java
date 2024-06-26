package org.example.view.buttons;

import org.example.models.objetos.Objeto;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

///ES EL COMPONENTE "MESA", EL CUADRADO QUE SE GENERA AL AGREGAR UNA MESA
public class MesaButton <T extends Objeto> extends MouseAdapter {
    private static final int DRAG = 1;
    private static final int RESIZE = 2;
    private Point clickInicial;
    private Component componente;
    private int mode;
    private Rectangle tamañoInicial;
    private T objeto;

    private boolean editable;

    public MesaButton(T objeto) {
        this.objeto = objeto;
    }

    public void mousePressed(MouseEvent e) {
        if (!editable) {
            return;
        }
        componente = e.getComponent();
        clickInicial = e.getPoint();
        tamañoInicial = componente.getBounds();

        if (isInResizeZone(clickInicial, tamañoInicial)) {
            mode = RESIZE;
        } else {
            mode = DRAG;
        }
        componente.getParent().setComponentZOrder(componente, 0); // Bring the componente to the front
    }

    public void mouseDragged(MouseEvent e) {
        if (!editable) {
            return;
        }
        if (mode == DRAG) {
            arrastrarMesa(e);
        } else if (mode == RESIZE) {
            redimensionarMesa(e);
        }
    }

    private boolean isInResizeZone(Point p, Rectangle bounds) {
        int resizeMargin = 20;
        return p.x >= bounds.width - resizeMargin && p.y >= bounds.height - resizeMargin;
    }

    private void arrastrarMesa(MouseEvent e) {
        Point parentLocation = componente.getParent().getLocationOnScreen();
        Point mouseLocation = e.getLocationOnScreen();

        int x = mouseLocation.x - parentLocation.x - clickInicial.x;
        int y = mouseLocation.y - parentLocation.y - clickInicial.y;

        componente.setLocation(x, y);

        // Actualizar la posición en el objeto Mesa
        objeto.setX(x);
        objeto.setY(y);
    }

    private void redimensionarMesa(MouseEvent e) {
        int newWidth = tamañoInicial.width + e.getX() - clickInicial.x;
        int newHeight = tamañoInicial.height + e.getY() - clickInicial.y;

        componente.setSize(new Dimension(newWidth, newHeight));
        componente.revalidate();
        componente.repaint();

        // Actualizar el tamaño en el objeto Mesa
        objeto.setAncho(newWidth);
        objeto.setAlto(newHeight);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}