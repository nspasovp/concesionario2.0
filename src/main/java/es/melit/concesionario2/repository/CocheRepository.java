package es.melit.concesionario2.repository;

import es.melit.concesionario2.domain.Coche;
import es.melit.concesionario2.domain.Venta;
import es.melit.concesionario2.repository.VentaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coche entity.
 */
@Repository
public interface CocheRepository extends JpaRepository<Coche, Long>, JpaSpecificationExecutor<Coche> {
    List<Coche> findByVentaIsNull();

    Optional<Coche> findCocheByVenta(Venta venta);

    List<Coche> findCochesByVenta(Venta venta);

    @Query("SELECT c FROM Coche c WHERE c.venta.id = :idVenta")
    List<Coche> obtenerNumCochesIdVenta(@Param(value = "idVenta") Long venta);
}
