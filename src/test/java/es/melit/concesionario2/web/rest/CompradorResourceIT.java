package es.melit.concesionario2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.melit.concesionario2.IntegrationTest;
import es.melit.concesionario2.domain.Comprador;
import es.melit.concesionario2.repository.CompradorRepository;
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
 * Integration tests for the {@link CompradorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompradorResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompradorMockMvc;

    private Comprador comprador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprador createEntity(EntityManager em) {
        Comprador comprador = new Comprador()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .direccion(DEFAULT_DIRECCION);
        return comprador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprador createUpdatedEntity(EntityManager em) {
        Comprador comprador = new Comprador()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);
        return comprador;
    }

    @BeforeEach
    public void initTest() {
        comprador = createEntity(em);
    }

    @Test
    @Transactional
    void createComprador() throws Exception {
        int databaseSizeBeforeCreate = compradorRepository.findAll().size();
        // Create the Comprador
        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isCreated());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeCreate + 1);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testComprador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComprador.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testComprador.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testComprador.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void createCompradorWithExistingId() throws Exception {
        // Create the Comprador with an existing ID
        comprador.setId(1L);

        int databaseSizeBeforeCreate = compradorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = compradorRepository.findAll().size();
        // set the field null
        comprador.setDni(null);

        // Create the Comprador, which fails.

        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isBadRequest());

        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = compradorRepository.findAll().size();
        // set the field null
        comprador.setNombre(null);

        // Create the Comprador, which fails.

        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isBadRequest());

        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrimerApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = compradorRepository.findAll().size();
        // set the field null
        comprador.setPrimerApellido(null);

        // Create the Comprador, which fails.

        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isBadRequest());

        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = compradorRepository.findAll().size();
        // set the field null
        comprador.setDireccion(null);

        // Create the Comprador, which fails.

        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isBadRequest());

        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompradors() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        // Get all the compradorList
        restCompradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprador.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }

    @Test
    @Transactional
    void getComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        // Get the comprador
        restCompradorMockMvc
            .perform(get(ENTITY_API_URL_ID, comprador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comprador.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }

    @Test
    @Transactional
    void getNonExistingComprador() throws Exception {
        // Get the comprador
        restCompradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador
        Comprador updatedComprador = compradorRepository.findById(comprador.getId()).get();
        // Disconnect from session so that the updates on updatedComprador are not directly saved in db
        em.detach(updatedComprador);
        updatedComprador
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);

        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComprador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComprador))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testComprador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComprador.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testComprador.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void putNonExistingComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompradorWithPatch() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador using partial update
        Comprador partialUpdatedComprador = new Comprador();
        partialUpdatedComprador.setId(comprador.getId());

        partialUpdatedComprador.nombre(UPDATED_NOMBRE).segundoApellido(UPDATED_SEGUNDO_APELLIDO).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprador))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testComprador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComprador.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testComprador.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void fullUpdateCompradorWithPatch() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador using partial update
        Comprador partialUpdatedComprador = new Comprador();
        partialUpdatedComprador.setId(comprador.getId());

        partialUpdatedComprador
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);

        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprador))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testComprador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComprador.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testComprador.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void patchNonExistingComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comprador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comprador))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeDelete = compradorRepository.findAll().size();

        // Delete the comprador
        restCompradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, comprador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
