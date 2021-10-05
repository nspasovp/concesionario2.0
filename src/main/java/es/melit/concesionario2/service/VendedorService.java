package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.Vendedor;
import es.melit.concesionario2.repository.UserRepository;
import es.melit.concesionario2.repository.VendedorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendedor}.
 */
@Service
@Transactional
public class VendedorService {

    private final Logger log = LoggerFactory.getLogger(VendedorService.class);

    private final VendedorRepository vendedorRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public VendedorService(VendedorRepository vendedorRepository, UserRepository userRepository, UserService userService) {
        this.vendedorRepository = vendedorRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Save a vendedor.
     *
     * @param vendedor the entity to save.
     * @return the persisted entity.
     */
    public Vendedor save(Vendedor vendedor) {
        userService.activateRegistration(null);

        log.debug("Request to save Vendedor : {}", vendedor);
        return vendedorRepository.save(vendedor);
    }

    /**
     * Partially update a vendedor.
     *
     * @param vendedor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vendedor> partialUpdate(Vendedor vendedor) {
        log.debug("Request to partially update Vendedor : {}", vendedor);

        return vendedorRepository
            .findById(vendedor.getId())
            .map(
                existingVendedor -> {
                    if (vendedor.getDni() != null) {
                        existingVendedor.setDni(vendedor.getDni());
                    }
                    if (vendedor.getNombre() != null) {
                        existingVendedor.setNombre(vendedor.getNombre());
                    }
                    if (vendedor.getPrimeriApellido() != null) {
                        existingVendedor.setPrimeriApellido(vendedor.getPrimeriApellido());
                    }
                    if (vendedor.getSegundoApellido() != null) {
                        existingVendedor.setSegundoApellido(vendedor.getSegundoApellido());
                    }
                    if (vendedor.getFechaNacimiento() != null) {
                        existingVendedor.setFechaNacimiento(vendedor.getFechaNacimiento());
                    }
                    if (vendedor.getFechaContratacion() != null) {
                        existingVendedor.setFechaContratacion(vendedor.getFechaContratacion());
                    }
                    if (vendedor.getFechaBaja() != null) {
                        existingVendedor.setFechaBaja(vendedor.getFechaBaja());
                    }

                    return existingVendedor;
                }
            )
            .map(vendedorRepository::save);
    }

    /**
     * Get all the vendedors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Vendedor> findAll(Pageable pageable) {
        log.debug("Request to get all Vendedors");
        return vendedorRepository.findAll(pageable);
    }

    /**
     * Get one vendedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vendedor> findOne(Long id) {
        log.debug("Request to get Vendedor : {}", id);
        return vendedorRepository.findById(id);
    }

    /**
     * Delete the vendedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendedor : {}", id);
        vendedorRepository.deleteById(id);
    }
}
