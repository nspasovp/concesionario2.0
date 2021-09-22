package es.melit.concesionario2.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Min(value = 1)
    @Column(name = "numero_coches", nullable = false)
    private Integer numeroCoches;

    @ManyToOne(optional = false)
    @NotNull
    private Vendedor vendedor;

    @ManyToOne(optional = false)
    @NotNull
    private Comprador comprador;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Venta fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroCoches() {
        return this.numeroCoches;
    }

    public Venta numeroCoches(Integer numeroCoches) {
        this.numeroCoches = numeroCoches;
        return this;
    }

    public void setNumeroCoches(Integer numeroCoches) {
        this.numeroCoches = numeroCoches;
    }

    public Vendedor getVendedor() {
        return this.vendedor;
    }

    public Venta vendedor(Vendedor vendedor) {
        this.setVendedor(vendedor);
        return this;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Comprador getComprador() {
        return this.comprador;
    }

    public Venta comprador(Comprador comprador) {
        this.setComprador(comprador);
        return this;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", numeroCoches=" + getNumeroCoches() +
            "}";
    }
}
