package es.melit.concesionario2.repository;

import es.melit.concesionario2.domain.Venta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
    @Query("SELECT v FROM Venta v WHERE v.vendedor.nombre = :nameVendedor")
    List<Venta> obtenerVentasByNameVendedor(@Param(value = "nameVendedor") String nameVendedor);
}
