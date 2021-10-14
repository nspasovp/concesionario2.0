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

public class VendedorSpec {

    public static Specification<Venta> ventaLike(User usuario) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Venta_.vendedor), usuario + "%");
    }

    public static Specification<Venta> buscarVentas(User usuario) {
        Specification<Venta> x = Specification.where(null);
        x = x.and(ventaLike(usuario));

        return x;
    }
}
