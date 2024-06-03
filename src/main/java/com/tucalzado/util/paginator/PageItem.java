package com.tucalzado.util.paginator;

public class PageItem {
    private int number;
    private boolean actual;

    public PageItem(int numero, boolean actual) {
        this.number = numero;
        this.actual = actual;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }
}
