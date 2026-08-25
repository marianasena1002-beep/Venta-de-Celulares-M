package com.venta_celulares.demo;

import com.venta_celulares.demo.controller.LoginController;
import com.venta_celulares.demo.model.Usuario;
import com.venta_celulares.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setTipoDocumento("CC");
        usuario.setIdPersona(1);
        usuario.setPrimerNombre("Usuario");
        usuario.setPrimerApellido("Prueba");
        usuario.setCorreo("usuario@gmail.com");
        usuario.setContrasena("123456");
    }

    @Test
    void debeMostrarLogin() {
        assertEquals("login", loginController.login());
    }

    @Test
    void debeMostrarLoginDesdeInicio() {
        assertEquals("login", loginController.inicio());
    }

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

    @Test
    void debeRedirigirAdministrador() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("Administrador"));

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
                List.of("Administrador")
        );
    }

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

    @Test
    void debeRechazarRolNoConfigurado() {

        when(usuarioService.iniciarSesion(
                "usuario@gmail.com",
                "123456"
        )).thenReturn(Optional.of(usuario));

        when(usuarioService.obtenerRoles(usuario))
                .thenReturn(List.of("RolInexistente"));

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

    @Test
    void empleadoConSesionDebeMostrarPanel() {

        when(session.getAttribute("usuario"))
                .thenReturn(usuario);

        when(usuarioService.listarUsuarios())
                .thenReturn(List.of(usuario));

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
                List.of(usuario)
        );
    }

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
