package com.venta_celulares.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "PERSONA")
@IdClass(PersonaId.class)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TIPO_DOCUMENTO_COD_TDOC")
    private String tipoDocumento;

    @Id
    @Column(name = "ID_PERSONA")
    private Integer idPersona;

    @Column(name = "P_NOMBRE", nullable = false)
    private String primerNombre;

    @Column(name = "S_NOMBRE")
    private String segundoNombre;

    @Column(name = "P_APELLIDO", nullable = false)
    private String primerApellido;

    @Column(name = "S_APELLIDO")
    private String segundoApellido;

    @Column(name = "DIRECCION_PERSONA", nullable = false)
    private String direccion;

    @Column(name = "TELEFONO_PERSONA")
    private Long telefono;

    @Column(name = "CELL_PERSONA")
    private Long celular;

    @Column(name = "CORREO", nullable = false, unique = true)
    private String correo;

    @Column(name = "CONTRASENA", nullable = false)
    private String contrasena;

    public Usuario() {
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Long getCelular() {
        return celular;
    }

    public void setCelular(Long celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreCompleto() {

        String nombre = primerNombre;

        if (segundoNombre != null && !segundoNombre.isBlank()) {
            nombre += " " + segundoNombre;
        }

        nombre += " " + primerApellido;

        if (segundoApellido != null && !segundoApellido.isBlank()) {
            nombre += " " + segundoApellido;
        }

        return nombre;
    }
}
