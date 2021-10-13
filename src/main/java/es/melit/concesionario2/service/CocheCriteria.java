package es.melit.concesionario2.service;

import java.io.Serializable;
import java.util.ArrayList;
import liquibase.pro.packaged.x;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

public class CocheCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String marca;

    private String modelo;

    public CocheCriteria() {}

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String name) {
        this.modelo = name;
    }

    public String getArray(int i) {
        //arreglarArray();
        String[] x = marca.split(" ");
        return x[i];
    }

    public int getLong() {
        String[] x = marca.split(" ");
        return x.length;
    }

    public void arreglarArray() {
        String z = null;
        String[] x = this.marca.split(" ");
        ArrayList<String> y = new ArrayList<String>();
        for (int i = 0; i < x.length; i++) {
            if (x[i] != " ") {
                y.add(x[i]);
            } else {
                break;
            }
        }
        for (int i = 0; i < y.size(); i++) {
            z += y.get(i);
        }
        this.marca = z;
    }
}
