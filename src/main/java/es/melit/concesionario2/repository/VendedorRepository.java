package es.melit.concesionario2.repository;

import es.melit.concesionario2.domain.Vendedor;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vendedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    /*@Query("SELECT c FROM Coche c WHERE c.venta.id = :idVenta")
    List<Coche> obtenerNumCochesIdVenta(@Param(value = "idVenta") Long venta);*/

    @Query("SELECT v FROM Vendedor v WHERE v.user.id = :idUser")
    List<Vendedor> obtenerVendedorIdUser(@Param(value = "idUser") Long idUser);
}
