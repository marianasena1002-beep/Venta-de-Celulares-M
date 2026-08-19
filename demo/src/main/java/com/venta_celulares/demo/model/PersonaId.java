package com.venta_celulares.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class PersonaId implements Serializable {

    private String tipoDocumento;
    private Integer idPersona;

    public PersonaId() {
    }

    public PersonaId(String tipoDocumento, Integer idPersona) {
        this.tipoDocumento = tipoDocumento;
        this.idPersona = idPersona;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof PersonaId)) {
            return false;
        }

        PersonaId that = (PersonaId) o;

        return Objects.equals(tipoDocumento, that.tipoDocumento)
                && Objects.equals(idPersona, that.idPersona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoDocumento, idPersona);
    }
}