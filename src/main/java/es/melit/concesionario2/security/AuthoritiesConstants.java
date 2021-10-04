package es.melit.concesionario2.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String VENDEDOR = "ROLE_VENDEDOR";

    public static final String COMPRADOR = "ROLE_COMPRADOR";

    private AuthoritiesConstants() {}
}
