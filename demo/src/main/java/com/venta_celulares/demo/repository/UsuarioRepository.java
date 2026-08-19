package com.venta_celulares.demo.repository;

import com.venta_celulares.demo.model.Usuario;
import com.venta_celulares.demo.model.PersonaId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, PersonaId> {

    // =====================================================
    // LOGIN
    // =====================================================

    Optional<Usuario> findByCorreoAndContrasena(
            String correo,
            String contrasena
    );


    // =====================================================
    // OBTENER ROLES
    // =====================================================

    @Query(value = """
            SELECT r.DESC_ROL
            FROM ROL_has_PERSONA rp
            INNER JOIN ROL r
                ON rp.ROL_COD_ROL = r.COD_ROL
            WHERE rp.PERSONA_TIPO_DOCUMENTO_COD_TDOC = :tipoDocumento
              AND rp.PERSONA_ID_PERSONA = :idPersona
              AND rp.ESTADO_RP = 1
            """,
            nativeQuery = true)
    List<String> obtenerRoles(

            @Param("tipoDocumento")
            String tipoDocumento,

            @Param("idPersona")
            Integer idPersona
    );
}