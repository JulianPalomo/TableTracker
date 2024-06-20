package org.example.view.buttons;

import org.example.models.Mesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
///ES EL COMPONENTE "MESA", EL CUADRADO QUE SE GENERA AL AGREGAR UNA MESA
public class MesaButton extends MouseAdapter {
    private boolean editable;
    private final Mesa mesa;
    private boolean dragging = false;

    public MesaButton(Mesa mesa) {
        this.mesa = mesa;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!editable) {
            return;
        }
        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!editable) {
            return;
        }
        JButton button = (JButton) e.getSource();
        button.setLocation(button.getX() + e.getX(), button.getY() + e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!editable) {
            return;
        }
        dragging = false;
    }
}
*/


public class MesaButton extends MouseAdapter {
    private static final int DRAG = 1;
    private static final int RESIZE = 2;
    private Point clickInicial;
    private Component componente;
    private int mode;
    private Rectangle tamañoInicial;
    private Mesa mesa;

    private boolean editable;

    public MesaButton(Mesa mesa) {
        this.mesa = mesa;
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
        mesa.setX(x);
        mesa.setY(y);
    }

    private void redimensionarMesa(MouseEvent e) {
        int newWidth = tamañoInicial.width + e.getX() - clickInicial.x;
        int newHeight = tamañoInicial.height + e.getY() - clickInicial.y;

        componente.setSize(new Dimension(newWidth, newHeight));
        componente.revalidate();
        componente.repaint();

        // Actualizar el tamaño en el objeto Mesa
        mesa.setAncho(newWidth);
        mesa.setAlto(newHeight);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}


