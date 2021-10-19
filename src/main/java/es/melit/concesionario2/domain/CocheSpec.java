package es.melit.concesionario2.domain;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import es.melit.concesionario2.service.CocheCriteria;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public class CocheSpec {

    public static Specification<Coche> marcaLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Coche_.MARCA), name + "%");
    }

    public static Specification<Coche> modeloLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Coche_.MODELO), name + "%");
    }

    public static Specification<Coche> buscarCoches(CocheCriteria coche) {
        Specification<Coche> x = Specification.where(null);
        //coche.arreglarArray(coche.getMarca());

        if (coche.getLong() >= 1) {
            if (coche.getArray(0) != "") {
                x = x.and(marcaLike(coche.getArray(0)).or(modeloLike(coche.getArray(0))));
                //.or(precioLike(coche.getArray(0))));
            }
            if (coche.getLong() > 1) {
                x = x.and(modeloLike(coche.getArray(1)));
            }
        }

        return x;
    }
    /*public static Specification<Coche> getCochesByNameSpec(CocheCriteria coche) {
        return new Specification<Coche>() {
            public Predicate toPredicate(Root<Coche> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> ors = new ArrayList<Predicate>();
                Expression<String> marca = root.get("marca").as(String.class);
                Expression<String> modelo = root.get("modelo").as(String.class);

                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.like(marca, "%" + coche.getMarca() + "%"));
                ors.add(criteriaBuilder.or(predicates.toArray(new Predicate[] {})));
                Predicate result = criteriaBuilder.or(ors.toArray(new Predicate[] {}));
                return result;
            }
        };
    }*/

}
