package es.melit.concesionario2.web.rest;

import es.melit.concesionario2.domain.*;
import es.melit.concesionario2.domain.Venta;
import es.melit.concesionario2.repository.CocheRepository;
import es.melit.concesionario2.repository.VentaRepository;
import es.melit.concesionario2.service.CocheService;
import es.melit.concesionario2.service.UserService;
import es.melit.concesionario2.service.VendedorService;
import es.melit.concesionario2.service.VentaService;
import es.melit.concesionario2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.melit.concesionario2.domain.Venta}.
 */
@RestController
@RequestMapping("/api")
public class VentaResource {

    private final Logger log = LoggerFactory.getLogger(VentaResource.class);

    private static final String ENTITY_NAME = "venta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentaService ventaService;

    private final VentaRepository ventaRepository;

    private final CocheService cocheService;

    private final VendedorService vendedorService;

    private final UserService userService;

    private final CocheRepository cocheRepository;

    //private final VentaRepository ventaRepository;

    public VentaResource(
        UserService userService,
        VendedorService vendedorService,
        VentaService ventaService,
        VentaRepository ventaRepository,
        CocheService cocheService,
        CocheRepository cocheRepository
    ) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
        this.cocheService = cocheService;
        this.vendedorService = vendedorService;
        this.userService = userService;
        this.cocheRepository = cocheRepository;
    }

    /**
     * {@code POST  /ventas} : Create a new venta.
     *
     * @param venta the venta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venta, or with status {@code 400 (Bad Request)} if the venta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws JSONException
     */
    @PostMapping("/ventas/{cocheId}")
    public ResponseEntity<Venta> createVenta(@Valid @RequestBody Venta venta, @PathVariable(value = "cocheId") final String cocheId)
        throws URISyntaxException, JSONException {
        log.debug("REST request to save Venta : {} cocheId {}", venta, cocheId);

        if (venta.getId() != null) {
            throw new BadRequestAlertException("A new venta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Venta result = ventaService.save(venta);
        cocheService.cocheVendido(result, cocheId);
        ventaService.calculateComision(result);
        result.setNumeroCoches(getNumCochesPorVenta(result.getId()));
        result = ventaService.save(result);

        return ResponseEntity
            .created(new URI("/api/ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventas/:id} : Updates an existing venta.
     *
     * @param id the id of the venta to save.
     * @param venta the venta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venta,
     * or with status {@code 400 (Bad Request)} if the venta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventas/update/{cocheId}")
    public ResponseEntity<Venta> updateVenta(@Valid @RequestBody Venta venta, @PathVariable(value = "cocheId") final String cocheId)
        throws URISyntaxException, JSONException {
        log.debug("REST request to update Venta : {}, {}", cocheId, venta);
        if (venta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        /*if (!Objects.equals(id, venta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }*/

        /*if (!ventaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }*/

        Venta result = ventaService.editarVenta(venta, cocheId);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ventas/:id} : Partial updates given fields of an existing venta, field will ignore if it is null
     *
     * @param id the id of the venta to save.
     * @param venta the venta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venta,
     * or with status {@code 400 (Bad Request)} if the venta is not valid,
     * or with status {@code 404 (Not Found)} if the venta is not found,
     * or with status {@code 500 (Internal Server Error)} if the venta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ventas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Venta> partialUpdateVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Venta venta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Venta partially : {}, {}", id, venta);
        if (venta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Venta> result = ventaService.partialUpdate(venta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venta.getId().toString())
        );
    }

    /**
     * {@code GET  /ventas} : get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventas in body.
     */
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> getAllVentas(Pageable pageable) {
        log.debug("REST request to get a page of Ventas");
        Page<Venta> page = ventaService.findAllByIdVendedor(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ventas/:id} : get the "id" venta.
     *
     * @param id the id of the venta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> getVenta(@PathVariable Long id) {
        log.debug("REST request to get Venta : {}", id);
        Optional<Venta> venta = ventaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venta);
    }

    @GetMapping("/ventas/numCoches/{id}")
    public int getNumCochesPorVenta(@PathVariable Long id) {
        log.debug("REST request to get Venta : {}", id);
        int numCoches = cocheService.numeroCochesPorVenta(id);
        return numCoches;
    }

    /**
     * {@code DELETE  /ventas/:id} : delete the "id" venta.
     *
     * @param id the id of the venta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventas/{cocheId}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long cocheId) {
        log.debug("REST request to delete Venta : {}", cocheId);
        //Obtenemos la venta a traves del id
        Optional<Venta> venta = ventaService.findOne(cocheId);
        List<Coche> c = cocheRepository.obtenerCochesIdVenta(venta.get().getId());
        Double precio = cocheService.TotalPrecioCochesPorVenta(venta.get());
        if (c.size() == 1) {
            //Se borra la venta que tenga un coche asignado
            cocheService.deleteVentaFromCoche(venta.get());
            Vendedor vendedor = venta.get().getVendedor();
            vendedor.setComision(vendedor.getComision() - (precio * 0.01));
        }
        //Borra la venta con varios coches asignados
        ventaService.delete(cocheId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, cocheId.toString()))
            .build();
    }
}
