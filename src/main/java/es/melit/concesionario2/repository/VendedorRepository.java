package es.melit.concesionario2.repository;

import es.melit.concesionario2.domain.Vendedor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vendedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {}
