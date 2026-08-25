package com.venta_celulares.demo.controller;

import com.venta_celulares.demo.model.Usuario;
import com.venta_celulares.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    // =====================================================
    // INICIO
    // =====================================================

    @GetMapping("/")
    public String inicio() {
        return "login";
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // =====================================================
    // PROCESAR LOGIN
    // =====================================================

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            Model model
    ) {

        Optional<Usuario> resultado =
                usuarioService.iniciarSesion(
                        correo,
                        contrasena
                );

        // =================================================
        // USUARIO NO ENCONTRADO
        // =================================================

        if (resultado.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Correo o contraseña incorrectos"
            );

            return "login";
        }

        Usuario usuario = resultado.get();

        // =================================================
        // OBTENER ROLES
        // =================================================

        List<String> roles =
                usuarioService.obtenerRoles(usuario);

        // =================================================
        // SIN ROL
        // =================================================

        if (roles.isEmpty()) {

            model.addAttribute(
                    "error",
                    "El usuario no tiene un rol activo"
            );

            return "login";
        }

        // =================================================
        // GUARDAR SESIÓN
        // =================================================

        session.setAttribute(
                "usuario",
                usuario
        );

        session.setAttribute(
                "roles",
                roles
        );

        String rolPrincipal = roles.get(0);

        // =================================================
        // ADMINISTRADOR
        // =================================================

        if (rolPrincipal.equalsIgnoreCase("Administrador")) {
            return "redirect:/administrador";
        }

        // =================================================
        // EMPLEADO / VENDEDOR
        // =================================================

        if (rolPrincipal.equalsIgnoreCase("Empleado")
                || rolPrincipal.equalsIgnoreCase("Vendedor")) {

            return "redirect:/empleado";
        }

        // =================================================
        // PROVEEDOR
        // =================================================

        if (rolPrincipal.equalsIgnoreCase("Proveedor")) {
            return "redirect:/proveedor";
        }

        // =================================================
        // CLIENTE
        // =================================================

        if (rolPrincipal.equalsIgnoreCase("Cliente")) {
            return "redirect:/cliente";
        }

        // =================================================
        // ROL NO CONFIGURADO
        // =================================================

        model.addAttribute(
                "error",
                "El rol del usuario no está configurado"
        );

        return "login";
    }

    // =====================================================
    // PANEL ADMINISTRADOR
    // =====================================================

    @GetMapping("/administrador")
    public String administrador(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        return "administrador";
    }

    // =====================================================
    // PANEL EMPLEADO
    // =====================================================

    @GetMapping("/empleado")
    public String empleado(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "usuarios",
                usuarioService.listarUsuarios()
        );

        return "empleado";
    }

    // =====================================================
    // PANEL PROVEEDOR
    // =====================================================

    @GetMapping("/proveedor")
    public String proveedor(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        return "proveedor";
    }

    // =====================================================
    // PANEL CLIENTE
    // =====================================================

    @GetMapping("/cliente")
    public String cliente(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        return "cliente";
    }

    // =====================================================
    // CERRAR SESIÓN
    // =====================================================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}
