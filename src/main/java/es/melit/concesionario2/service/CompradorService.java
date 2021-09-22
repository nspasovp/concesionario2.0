package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.Comprador;
import es.melit.concesionario2.repository.CompradorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comprador}.
 */
@Service
@Transactional
public class CompradorService {

    private final Logger log = LoggerFactory.getLogger(CompradorService.class);

    private final CompradorRepository compradorRepository;

    public CompradorService(CompradorRepository compradorRepository) {
        this.compradorRepository = compradorRepository;
    }

    /**
     * Save a comprador.
     *
     * @param comprador the entity to save.
     * @return the persisted entity.
     */
    public Comprador save(Comprador comprador) {
        log.debug("Request to save Comprador : {}", comprador);
        return compradorRepository.save(comprador);
    }

    /**
     * Partially update a comprador.
     *
     * @param comprador the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Comprador> partialUpdate(Comprador comprador) {
        log.debug("Request to partially update Comprador : {}", comprador);

        return compradorRepository
            .findById(comprador.getId())
            .map(
                existingComprador -> {
                    if (comprador.getDni() != null) {
                        existingComprador.setDni(comprador.getDni());
                    }
                    if (comprador.getNombre() != null) {
                        existingComprador.setNombre(comprador.getNombre());
                    }
                    if (comprador.getPrimerApellido() != null) {
                        existingComprador.setPrimerApellido(comprador.getPrimerApellido());
                    }
                    if (comprador.getSegundoApellido() != null) {
                        existingComprador.setSegundoApellido(comprador.getSegundoApellido());
                    }
                    if (comprador.getFechaNacimiento() != null) {
                        existingComprador.setFechaNacimiento(comprador.getFechaNacimiento());
                    }
                    if (comprador.getDireccion() != null) {
                        existingComprador.setDireccion(comprador.getDireccion());
                    }

                    return existingComprador;
                }
            )
            .map(compradorRepository::save);
    }

    /**
     * Get all the compradors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Comprador> findAll(Pageable pageable) {
        log.debug("Request to get all Compradors");
        return compradorRepository.findAll(pageable);
    }

    /**
     * Get one comprador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Comprador> findOne(Long id) {
        log.debug("Request to get Comprador : {}", id);
        return compradorRepository.findById(id);
    }

    /**
     * Delete the comprador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comprador : {}", id);
        compradorRepository.deleteById(id);
    }
}
