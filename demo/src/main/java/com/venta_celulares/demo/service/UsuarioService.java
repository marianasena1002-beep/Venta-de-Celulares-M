package com.venta_celulares.demo.service;

import com.venta_celulares.demo.model.Usuario;
import com.venta_celulares.demo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    // =====================================================
    // LOGIN
    // =====================================================

    public Optional<Usuario> iniciarSesion(
            String correo,
            String contrasena) {

        return usuarioRepository.findByCorreoAndContrasena(
                correo,
                contrasena
        );
    }


    // =====================================================
    // OBTENER ROLES
    // =====================================================

    public List<String> obtenerRoles(Usuario usuario) {

        return usuarioRepository.obtenerRoles(
                usuario.getTipoDocumento(),
                usuario.getIdPersona()
        );
    }


    // =====================================================
    // LISTAR USUARIOS
    // =====================================================

    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll();
    }
}