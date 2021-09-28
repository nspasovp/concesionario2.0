package es.melit.concesionario2.repository;

import es.melit.concesionario2.domain.Coche;
import es.melit.concesionario2.domain.Venta;
import es.melit.concesionario2.repository.VentaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CocheRepository extends JpaRepository<Coche, Long> {
    List<Coche> findByVentaIsNull();
    Optional<Coche> findCocheByVenta(Venta venta);
}
