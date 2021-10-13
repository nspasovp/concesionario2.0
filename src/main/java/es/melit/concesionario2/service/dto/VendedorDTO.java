package es.melit.concesionario2.service.dto;

import es.melit.concesionario2.domain.Authority;
import es.melit.concesionario2.domain.User;
import es.melit.concesionario2.domain.Vendedor;
import es.melit.concesionario2.repository.UserRepository;
import es.melit.concesionario2.security.AuthoritiesConstants;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.jhipster.security.RandomUtil;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class VendedorDTO implements Serializable {

    private Long id;

    private String login;

    private String password;

    private String email;

    private boolean activated = false;

    private String langKey;

    private String imageUrl;

    private String activationKey;

    private String resetKey;

    private Set<Authority> authorities = new HashSet<>();

    private String dni;

    private String nombre;

    private String primeriApellido;

    private String segundoApellido;

    private LocalDate fechaNacimiento;

    private LocalDate fechaContratacion;

    private UserRepository userRepository;

    public VendedorDTO() {
        // Empty constructor needed for Jackson.
    }

    public VendedorDTO(Vendedor vendedor, User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.resetKey = user.getResetKey();
        this.authorities = user.getAuthorities();
        this.dni = vendedor.getDni();
        this.nombre = vendedor.getNombre();
        this.fechaNacimiento = vendedor.getFechaNacimiento();
        this.fechaContratacion = vendedor.getFechaContratacion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimeriApellido() {
        return primeriApellido;
    }

    public void setPrimeriApellido(String primeriApellido) {
        this.primeriApellido = primeriApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    @Override
    public String toString() {
        return (
            "VendedorDTO [activated=" +
            activated +
            ", activationKey=" +
            activationKey +
            ", authorities=" +
            authorities +
            ", dni=" +
            dni +
            ", email=" +
            email +
            ", fechaContratacion=" +
            fechaContratacion +
            ", fechaNacimiento=" +
            fechaNacimiento +
            ", id=" +
            id +
            ", imageUrl=" +
            imageUrl +
            ", langKey=" +
            langKey +
            ", login=" +
            login +
            ", nombre=" +
            nombre +
            ", password=" +
            password +
            ", primeriApellido=" +
            primeriApellido +
            ", resetKey=" +
            resetKey +
            ", segundoApellido=" +
            segundoApellido +
            "]"
        );
    }
}
