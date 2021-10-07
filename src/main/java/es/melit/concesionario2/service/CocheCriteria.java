package es.melit.concesionario2.service;

import java.io.Serializable;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

public class CocheCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private StringFilter marca;

    private StringFilter modelo;

    public CocheCriteria() {}

    public StringFilter getMarca() {
        return marca;
    }

    public void setMarca(StringFilter marca) {
        this.marca = marca;
    }

    public StringFilter getModelo() {
        return modelo;
    }

    public void setModelo(StringFilter name) {
        this.modelo = name;
    }
}
