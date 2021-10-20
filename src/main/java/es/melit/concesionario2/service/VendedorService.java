package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.*;
import es.melit.concesionario2.domain.Vendedor;
import es.melit.concesionario2.repository.UserRepository;
import es.melit.concesionario2.repository.VendedorRepository;
import es.melit.concesionario2.repository.VentaRepository;
import es.melit.concesionario2.security.AuthoritiesConstants;
import es.melit.concesionario2.service.dto.AdminUserDTO;
import es.melit.concesionario2.service.dto.VendedorDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final VentaRepository ventaRepository;

    public VendedorService(
        VentaRepository ventaRepository,
        VendedorRepository vendedorRepository,
        UserRepository userRepository,
        UserService userService
    ) {
        this.vendedorRepository = vendedorRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.ventaRepository = ventaRepository;
    }

    /**
     * Save a vendedor.
     *
     * @param vendedor the entity to save.
     * @return the persisted entity.
     */
    public Vendedor save(Vendedor vendedor) {
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
     * Get all the vendedors by login.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Vendedor> findAllByLogin(Pageable pageable) {
        log.debug("Request to get all Vendedors");
        Optional<User> u = userService.getUserWithAuthorities();
        if (u.get().getLogin().equals("admin")) {
            return this.findAll(pageable);
        }
        List<Vendedor> pageL = vendedorRepository.obtenerVendedorIdUser(u.get().getId());
        Page<Vendedor> page = new PageImpl<>(pageL);
        return page;
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
        Optional<Vendedor> v = vendedorRepository.findById(id);
        Optional<User> u = userRepository.findById(v.get().getIdUser().getId());
        User user = u.get();
        userRepository.delete(user);
        vendedorRepository.deleteById(id);
    }

    public Vendedor crearVendedorUsuario(Vendedor vendedor, PasswordEncoder passwordEncoder) {
        User u = new User();
        u.setLogin(vendedor.getNombre());
        AdminUserDTO usuario = new AdminUserDTO(u);

        //Añadir el rol al usuario creado
        Set<String> authorities = new TreeSet();
        String rol = AuthoritiesConstants.VENDEDOR;
        authorities.add(rol);
        usuario.setAuthorities(authorities);
        User user = userService.createUser(usuario);

        //Asigna como contraseña el nombre del login
        String encryptedPassword = passwordEncoder.encode(usuario.getLogin());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        Optional<User> x = userRepository.findById(user.getId());
        vendedor.setIdUser(x.get());

        return vendedor;
    }
}
