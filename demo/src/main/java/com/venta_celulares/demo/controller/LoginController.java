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

    private static final String LOGIN = "login";
    private static final String ERROR = "error";
    private static final String USUARIO = "usuario";

    @GetMapping("/")
    public String inicio() {
        return LOGIN;
    }

    @GetMapping("/login")
    public String login() {
        return LOGIN;
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            Model model) {

        Optional<Usuario> resultado =
                usuarioService.iniciarSesion(correo, contrasena);

        if (resultado.isEmpty()) {
            model.addAttribute(
                    ERROR,
                    "Correo o contraseña incorrectos"
            );
            return LOGIN;
        }

        Usuario usuario = resultado.get();

        List<String> roles =
                usuarioService.obtenerRoles(usuario);

        if (roles.isEmpty()) {
            model.addAttribute(
                    ERROR,
                    "El usuario no tiene un rol activo"
            );
            return LOGIN;
        }

        session.setAttribute(USUARIO, usuario);
        session.setAttribute("roles", roles);

        String rolPrincipal = roles.get(0);

        if (rolPrincipal.equalsIgnoreCase("Administrador")) {
            return "redirect:/administrador";
        }

        if (rolPrincipal.equalsIgnoreCase("Empleado")
                || rolPrincipal.equalsIgnoreCase("Vendedor")) {
            return "redirect:/empleado";
        }

        if (rolPrincipal.equalsIgnoreCase("Proveedor")) {
            return "redirect:/proveedor";
        }

        if (rolPrincipal.equalsIgnoreCase("Cliente")) {
            return "redirect:/cliente";
        }

        model.addAttribute(
                ERROR,
                "El rol del usuario no está configurado"
        );

        return LOGIN;
    }

    @GetMapping("/administrador")
    public String administrador(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(USUARIO);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(USUARIO, usuario);

        return "administrador";
    }

    @GetMapping("/empleado")
    public String empleado(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(USUARIO);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(USUARIO, usuario);

        model.addAttribute(
                "usuarios",
                usuarioService.listarUsuarios()
        );

        return "empleado";
    }

    @GetMapping("/proveedor")
    public String proveedor(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(USUARIO);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(USUARIO, usuario);

        return "proveedor";
    }

    @GetMapping("/cliente")
    public String cliente(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(USUARIO);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(USUARIO, usuario);

        return "cliente";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}
