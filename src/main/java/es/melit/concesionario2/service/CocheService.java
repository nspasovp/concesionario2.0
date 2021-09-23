package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.*;
import es.melit.concesionario2.domain.Coche;
import es.melit.concesionario2.repository.*;
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
 * Service Implementation for managing {@link Coche}.
 */
@Service
@Transactional
public class CocheService {

    private final Logger log = LoggerFactory.getLogger(CocheService.class);

    private final CocheRepository cocheRepository;
    private final VentaRepository ventaRepository;

    public CocheService(CocheRepository cocheRepository, VentaRepository ventaRepository) {
        this.cocheRepository = cocheRepository;
        this.ventaRepository = ventaRepository;
    }

    /**
     * Save a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    public Coche save(Coche coche) {
        log.debug("Request to save Coche : {}", coche);
        return cocheRepository.save(coche);
    }

    /**
     * Partially update a coche.
     *
     * @param coche the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Coche> partialUpdate(Coche coche) {
        log.debug("Request to partially update Coche : {}", coche);

        return cocheRepository
            .findById(coche.getId())
            .map(
                existingCoche -> {
                    if (coche.getMarca() != null) {
                        existingCoche.setMarca(coche.getMarca());
                    }
                    if (coche.getModelo() != null) {
                        existingCoche.setModelo(coche.getModelo());
                    }
                    if (coche.getPrecio() != null) {
                        existingCoche.setPrecio(coche.getPrecio());
                    }

                    return existingCoche;
                }
            )
            .map(cocheRepository::save);
    }

    public void cocheVendido(Venta venta, Long cocheId) {
        Optional<Coche> c = findOne(cocheId);
        c.get().setVenta(venta);
        save(c.get());
    }

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Coche> findAll(Pageable pageable) {
        log.debug("Request to get all Coches");
        return cocheRepository.findAll(pageable);
    }

    /**
     * Get the not selled coches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Coche> findByVentaIsNull() {
        log.debug("Request to get all Coches");
        return cocheRepository.findByVentaIsNull();
    }

    /**
     * Get one coche by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Coche> findOne(Long id) {
        log.debug("Request to get Coche : {}", id);
        return cocheRepository.findById(id);
    }

    /**
     * Delete the coche by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
    }
}
