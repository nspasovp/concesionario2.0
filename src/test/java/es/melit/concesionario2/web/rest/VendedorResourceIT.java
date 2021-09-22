package es.melit.concesionario2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.melit.concesionario2.IntegrationTest;
import es.melit.concesionario2.domain.Vendedor;
import es.melit.concesionario2.repository.VendedorRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendedorResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMERI_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMERI_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_CONTRATACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CONTRATACION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/vendedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .primeriApellido(DEFAULT_PRIMERI_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .fechaContratacion(DEFAULT_FECHA_CONTRATACION)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        return vendedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primeriApellido(UPDATED_PRIMERI_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaContratacion(UPDATED_FECHA_CONTRATACION)
            .fechaBaja(UPDATED_FECHA_BAJA);
        return vendedor;
    }

    @BeforeEach
    public void initTest() {
        vendedor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendedor() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();
        // Create the Vendedor
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isCreated());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getPrimeriApellido()).isEqualTo(DEFAULT_PRIMERI_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testVendedor.getFechaContratacion()).isEqualTo(DEFAULT_FECHA_CONTRATACION);
        assertThat(testVendedor.getFechaBaja()).isEqualTo(DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void createVendedorWithExistingId() throws Exception {
        // Create the Vendedor with an existing ID
        vendedor.setId(1L);

        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setDni(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setNombre(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrimeriApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setPrimeriApellido(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setFechaNacimiento(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaContratacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setFechaContratacion(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendedors() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].primeriApellido").value(hasItem(DEFAULT_PRIMERI_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaContratacion").value(hasItem(DEFAULT_FECHA_CONTRATACION.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.primeriApellido").value(DEFAULT_PRIMERI_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.fechaContratacion").value(DEFAULT_FECHA_CONTRATACION.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).get();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primeriApellido(UPDATED_PRIMERI_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaContratacion(UPDATED_FECHA_CONTRATACION)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getPrimeriApellido()).isEqualTo(UPDATED_PRIMERI_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testVendedor.getFechaContratacion()).isEqualTo(UPDATED_FECHA_CONTRATACION);
        assertThat(testVendedor.getFechaBaja()).isEqualTo(UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void putNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.dni(UPDATED_DNI).fechaContratacion(UPDATED_FECHA_CONTRATACION);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getPrimeriApellido()).isEqualTo(DEFAULT_PRIMERI_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testVendedor.getFechaContratacion()).isEqualTo(UPDATED_FECHA_CONTRATACION);
        assertThat(testVendedor.getFechaBaja()).isEqualTo(DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void fullUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primeriApellido(UPDATED_PRIMERI_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaContratacion(UPDATED_FECHA_CONTRATACION)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getPrimeriApellido()).isEqualTo(UPDATED_PRIMERI_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testVendedor.getFechaContratacion()).isEqualTo(UPDATED_FECHA_CONTRATACION);
        assertThat(testVendedor.getFechaBaja()).isEqualTo(UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void patchNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeDelete = vendedorRepository.findAll().size();

        // Delete the vendedor
        restVendedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
