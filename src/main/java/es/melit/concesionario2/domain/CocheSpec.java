package es.melit.concesionario2.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class CocheSpec {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

    private static void findAllCoches() {
        System.out.println("-- All coches --");
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Coche> query = cb.createQuery(Coche.class); //create query object
        Root<Coche> cocheRoot = query.from(Coche.class); //get object representing 'from' part
        query.select(cocheRoot); //linking 'select' and 'from' parts, equivalent to 'select t from Coche t;'
        TypedQuery<Coche> typedQuery = em.createQuery(query);
        typedQuery.getResultList().forEach(System.out::println);
        em.close();
    }

    private static void findAstras() {
        System.out.println("-- All employees with 'Astra' modelo --");
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Coche> query = cb.createQuery(Coche.class);
        Root<Coche> Coche = query.from(Coche.class);
        query.select(Coche).where(cb.equal(Coche.get("modelo"), "Astra"));
        TypedQuery<Coche> typedQuery = em.createQuery(query);
        typedQuery.getResultList().forEach(System.out::println);
        em.close();
    }

    public static Specification<Coche> equalModeloOfCoche(String modelo) {
        if (modelo == null) {
            return null;
        }
        return (root, query, cb) -> {
            return cb.equal(root.get(Coche_.MODELO), modelo);
        };
    }

    /*public static Specification<Coche> getCochesByNameSpec(String modelo) {
        return new Specification<Coche>() {
            @Override
            public Predicate toPredicate(Root<Coche> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.select(Coche).where();
                Predicate equalPredicate = criteriaBuilder.equal(root.get(Coche_.modelo), modelo);
                return equalPredicate;
            }
        };
    }*/

    public static Specification<Coche> getCochesByPrecioSpec(Double precio) {
        return new Specification<Coche>() {
            @Override
            public Predicate toPredicate(Root<Coche> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate equalPredicate = criteriaBuilder.equal(root.get(Coche_.precio), precio);
                return equalPredicate;
            }
        };
    }
    /*
     * public static Specification<Coche> getCochesByModeloTypeSpec(Long precio) {
     * return new Specification<Coche>() {
     *
     * @Override public Predicate toPredicate(Root<Coche> root, CriteriaQuery<?>
     * query, CriteriaBuilder criteriaBuilder) { ListJoin<Coche, marca> phoneJoin =
     * root.join(Coche.marca); Predicate equalPredicate =
     * criteriaBuilder.equal(phoneJoin.get(Phone_.type), phoneType); return
     * equalPredicate; } }; }
     */
}
