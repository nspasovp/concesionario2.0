package es.melit.concesionario2.service;

import es.melit.concesionario2.domain.*;
import es.melit.concesionario2.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
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

    /**
     * Set field venta when coche is selled.
     *
     * @param Venta Venta object
     * @param Long  coche Id.
     * @throws JSONException
     *
     */
    public void cocheVendido(Venta venta, String cochesId) throws JSONException {
        ArrayList<Long> idCoches = jsonStringToArray(cochesId);
        for (int i = 0; i < idCoches.size(); i++) {
            Optional<Coche> c = findOne(idCoches.get(i));
            c.get().setVenta(venta);
            save(c.get());
        }
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

    /**
     * Delete the field Venta from Coche.
     *
     * @param Venta the venta to delete.
     */
    public void deleteVentaFromCoche(Venta venta) {
        // Esta condición comprueba que haya coche asignado a la venta, en caso
        // contrario no realiza nada
        if (cocheRepository.findCocheByVenta(venta).isPresent()) {
            Optional<Coche> coche = cocheRepository.findCocheByVenta(venta);
            coche.get().setVentaToNull();
        }
    }

    public void deleteVentaFromCoches(Venta venta, String cochesId) throws JSONException {
        ArrayList<Long> miarray = jsonStringToArray(cochesId);
        for (int i = 0; i < miarray.size(); i++) {
            Optional<Venta> v = ventaRepository.findById(miarray.get(i));
            if (cocheRepository.findCocheByVenta(v.get()).isPresent()) {
                Optional<Coche> c = findOne(miarray.get(i));
                c.get().setVentaToNull();
                save(c.get());
            }
        }
    }

    /**
     * Convert JSON String to ArrayList<Long>.
     *
     * @param String the JSON to convert.
     *
     * @return the StringArray<Long>.
     */
    public ArrayList<Long> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<Long> stringArray = new ArrayList<Long>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getLong(i));
        }

        return stringArray;
    }

    /**
     * Get JSON String to ArrayList<Long>.
     *
     * @param String the JSON to convert.
     *
     * @return the StringArray<Long>.
     */
    public int numeroCochesPorVenta(Long id) {
        List<Coche> num = cocheRepository.obtenerCochesIdVenta(id);
        return num.size();
    }

    public Double TotalPrecioCochesPorVenta(Venta venta) {
        Double total = 0.0;
        List<Coche> coches = cocheRepository.findCochesByVenta(venta);
        for (int i = 0; i < coches.size(); i++) {
            total += coches.get(i).getPrecio();
        }
        return total;
    }
}
