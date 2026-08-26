package com.venta_celulares.demo.controller;

import com.venta_celulares.demo.model.Usuario;
import com.venta_celulares.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();

        usuario.setTipoDocumento("CC");
        usuario.setIdPersona(1);
        usuario.setPrimerNombre("Usuario");
        usuario.setPrimerApellido("Prueba");
        usuario.setCorreo("usuario@gmail.com");
        usuario.setContrasena("123456");
    }

    // =====================================================
    // INICIO Y LOGIN
    // =====================================================

    @Test
    void debeMostrarInicio() {
        String resultado = loginController.inicio();

        assertEquals("login", resultado);
    }

    @Test
    void debeMostrarLogin() {
        String resultado = loginController.login();

        assertEquals("login", resultado);
    }

    // =====================================================
    // LOGIN INCORRECTO
    // =====================================================

    @Test
    void debeRechazarUsuarioNoEncontrado() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.empty());

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals("login", resultado);

        verify(model).addAttribute(
                "error",
                "Correo o contraseña incorrectos"
        );
    }

    // =====================================================
    // USUARIO SIN ROL
    // =====================================================

    @Test
    void debeRechazarUsuarioSinRol() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of());

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals("login", resultado);

        verify(model).addAttribute(
                "error",
                "El usuario no tiene un rol activo"
        );
    }

    // =====================================================
    // ADMINISTRADOR
    // =====================================================

    @Test
    void debeRedirigirAdministrador() {

        List<String> roles = List.of("Administrador");

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(roles);

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals(
                "redirect:/administrador",
                resultado
        );

        verify(session).setAttribute(
                "usuario",
                usuario
        );

        verify(session).setAttribute(
                "roles",
                roles
        );
    }

    // =====================================================
    // EMPLEADO
    // =====================================================

    @Test
    void debeRedirigirEmpleado() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("Empleado"));

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals(
                "redirect:/empleado",
                resultado
        );
    }

    // =====================================================
    // VENDEDOR
    // =====================================================

    @Test
    void debeRedirigirVendedor() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("Vendedor"));

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals(
                "redirect:/empleado",
                resultado
        );
    }

    // =====================================================
    // PROVEEDOR
    // =====================================================

    @Test
    void debeRedirigirProveedor() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("Proveedor"));

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals(
                "redirect:/proveedor",
                resultado
        );
    }

    // =====================================================
    // CLIENTE
    // =====================================================

    @Test
    void debeRedirigirCliente() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("Cliente"));

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals(
                "redirect:/cliente",
                resultado
        );
    }

    // =====================================================
    // ROL NO CONFIGURADO
    // =====================================================

    @Test
    void debeRechazarRolNoConfigurado() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("OtroRol"));

        String resultado = loginController.procesarLogin(
                "usuario@gmail.com",
                "123456",
                session,
                model
        );

        assertEquals("login", resultado);

        verify(model).addAttribute(
                "error",
                "El rol del usuario no está configurado"
        );
    }

    // =====================================================
    // ADMINISTRADOR SIN SESIÓN
    // =====================================================

    @Test
    void administradorSinSesionDebeVolverAlLogin() {

        when(session.getAttribute("usuario"))
                .thenReturn(null);

        String resultado =
                loginController.administrador(
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                resultado
        );
    }

    // =====================================================
    // ADMINISTRADOR CON SESIÓN
    // =====================================================

    @Test
    void administradorConSesionDebeMostrarPanel() {

        when(session.getAttribute("usuario"))
                .thenReturn(usuario);

        String resultado =
                loginController.administrador(
                        session,
                        model
                );

        assertEquals(
                "administrador",
                resultado
        );

        verify(model).addAttribute(
                "usuario",
                usuario
        );
    }

    // =====================================================
    // EMPLEADO SIN SESIÓN
    // =====================================================

    @Test
    void empleadoSinSesionDebeVolverAlLogin() {

        when(session.getAttribute("usuario"))
                .thenReturn(null);

        String resultado =
                loginController.empleado(
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                resultado
        );
    }

    // =====================================================
    // EMPLEADO CON SESIÓN
    // =====================================================

    @Test
    void empleadoConSesionDebeMostrarPanel() {

        List<Usuario> usuarios = List.of(usuario);

        when(session.getAttribute("usuario"))
                .thenReturn(usuario);

        when(usuarioService.listarUsuarios())
                .thenReturn(usuarios);

        String resultado =
                loginController.empleado(
                        session,
                        model
                );

        assertEquals(
                "empleado",
                resultado
        );

        verify(model).addAttribute(
                "usuario",
                usuario
        );

        verify(model).addAttribute(
                "usuarios",
                usuarios
        );
    }

    // =====================================================
    // PROVEEDOR SIN SESIÓN
    // =====================================================

    @Test
    void proveedorSinSesionDebeVolverAlLogin() {

        when(session.getAttribute("usuario"))
                .thenReturn(null);

        String resultado =
                loginController.proveedor(
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                resultado
        );
    }

    // =====================================================
    // PROVEEDOR CON SESIÓN
    // =====================================================

    @Test
    void proveedorConSesionDebeMostrarPanel() {

        when(session.getAttribute("usuario"))
                .thenReturn(usuario);

        String resultado =
                loginController.proveedor(
                        session,
                        model
                );

        assertEquals(
                "proveedor",
                resultado
        );

        verify(model).addAttribute(
                "usuario",
                usuario
        );
    }

    // =====================================================
    // CLIENTE SIN SESIÓN
    // =====================================================

    @Test
    void clienteSinSesionDebeVolverAlLogin() {

        when(session.getAttribute("usuario"))
                .thenReturn(null);

        String resultado =
                loginController.cliente(
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                resultado
        );
    }

    // =====================================================
    // CLIENTE CON SESIÓN
    // =====================================================

    @Test
    void clienteConSesionDebeMostrarPanel() {

        when(session.getAttribute("usuario"))
                .thenReturn(usuario);

        String resultado =
                loginController.cliente(
                        session,
                        model
                );

        assertEquals(
                "cliente",
                resultado
        );

        verify(model).addAttribute(
                "usuario",
                usuario
        );
    }

    // =====================================================
    // CERRAR SESIÓN
    // =====================================================

    @Test
    void cerrarSesionDebeInvalidarSesion() {

        String resultado =
                loginController.logout(session);

        assertEquals(
                "redirect:/login",
                resultado
        );

        verify(session).invalidate();
    }
}
