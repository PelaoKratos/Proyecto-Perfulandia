package microservice.cliente.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private Long idUsuario;

    @Column(length = 20, nullable = false, unique = true)
    private String rut;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 100, nullable = false)
    private String correo;

    @Column(length = 20, nullable = false)
    private String telefono;

    @Column(length = 30)
    private String estado;

    private boolean activo = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("cliente")
    private List<DireccionCliente> direcciones = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(Long idCliente, Long idUsuario, String rut, String nombre, String apellido, String correo, String telefono,
            String estado, boolean activo) {
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
        this.activo = activo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<DireccionCliente> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<DireccionCliente> direcciones) {
        this.direcciones.clear();
        if (direcciones != null) {
            direcciones.forEach(this::agregarDireccion);
        }
    }

    public void activar() {
        this.activo = true;
        this.estado = "ACTIVO";
    }

    public void desactivar() {
        this.activo = false;
        this.estado = "INACTIVO";
    }

    public void actualizarDatosPersonales(String rut, String nombre, String apellido, String correo,
            String telefono, String estado) {
        if (rut != null) {
            this.rut = rut;
        }
        if (nombre != null) {
            this.nombre = nombre;
        }
        if (apellido != null) {
            this.apellido = apellido;
        }
        if (correo != null) {
            this.correo = correo;
        }
        if (telefono != null) {
            this.telefono = telefono;
        }
        if (estado != null) {
            this.estado = estado;
        }
    }

    public void agregarDireccion(DireccionCliente direccion) {
        if (direccion != null) {
            direccion.setCliente(this);
            direccion.setIdCliente(this.idCliente);
            this.direcciones.add(direccion);
        }
    }
}
