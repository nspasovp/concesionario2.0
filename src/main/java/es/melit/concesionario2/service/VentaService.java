package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.Coche;
import es.melit.concesionario2.domain.Vendedor;
import es.melit.concesionario2.domain.Venta;
import es.melit.concesionario2.repository.CocheRepository;
import es.melit.concesionario2.repository.VendedorRepository;
import es.melit.concesionario2.repository.VentaRepository;
import es.melit.concesionario2.service.*;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;
    private final VendedorRepository vendedorRepository;
    private final CocheService cocheService;
    private final CocheRepository cocheRepository;

    public VentaService(
        VentaRepository ventaRepository,
        CocheService cocheService,
        VendedorRepository vendedorRepository,
        CocheRepository cocheRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.cocheService = cocheService;
        this.vendedorRepository = vendedorRepository;
        this.cocheRepository = cocheRepository;
    }

    /**
     * Save a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    /**
     * Partially update a venta.
     *
     * @param venta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Venta> partialUpdate(Venta venta) {
        log.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(
                existingVenta -> {
                    if (venta.getFecha() != null) {
                        existingVenta.setFecha(venta.getFecha());
                    }
                    if (venta.getNumeroCoches() != null) {
                        existingVenta.setNumeroCoches(venta.getNumeroCoches());
                    }
                    if (venta.getNumFactura() != null) {
                        existingVenta.setNumFactura(venta.getNumFactura());
                    }

                    return existingVenta;
                }
            )
            .map(ventaRepository::save);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Venta> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable);
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }

    /**
     * Calculate comision for vendedor  by venta.
     *
     * @param venta the Venta entity.
     */
    public void calculateComision(Venta venta) {
        log.debug("Calculate comision", venta);
        Vendedor vendedor = venta.getVendedor();
        Double totalPrecio = cocheService.TotalPrecioCochesPorVenta(venta);
        vendedor.setComision(vendedor.getComision() + (0.10 * totalPrecio));
        vendedorRepository.save(vendedor);
    }

    public void actualizarVentaNullCoches(Venta venta) {
        List<Coche> coches = cocheRepository.findCochesByVenta(venta);
        if (!coches.isEmpty()) {
            for (int i = 0; i < coches.size(); i++) {
                coches.get(i).setVentaToNull();
            }
        }
    }
}
