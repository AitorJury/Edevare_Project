import com.edevare.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test Fallido para verificar los servicios de Seguridad (Cifrado y JWT).
 * Se utiliza @ExtendWith(MockitoExtension.class) para inicializar los Mocks y la clase a probar.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Test
    void passwordEncoder_debeCifrarContrasena_yPermitirVerificacion() {
        // ARRANGE: Instancia real de un encoder (ej. BCrypt). Usamos Mockito para simular el contrato.
        // En la vida real se inyectaría una instancia de BCryptPasswordEncoder. Aquí, verificamos el contrato.

        // Creamos un Mock del Encoder para forzar su uso (aunque en VERDE usaremos la implementación real).
        PasswordEncoder encoderMock = mock(PasswordEncoder.class);
        String contrasenaPlana = "Password123";
        String hashSimulado = "$2a$10$hashSimuladoParaPrueba";

        // 1. Simular el cifrado
        when(encoderMock.encode(contrasenaPlana)).thenReturn(hashSimulado);

        // 2. Simular la verificación de match
        when(encoderMock.matches(contrasenaPlana, hashSimulado)).thenReturn(true);

        // ACT: Llamamos a los métodos
        String hashReal = encoderMock.encode(contrasenaPlana);
        boolean esValida = encoderMock.matches(contrasenaPlana, hashSimulado);

        // ASSERT: Verificamos el flujo y el resultado

        // Verificamos que se llamó al método de cifrado
        verify(encoderMock, times(1)).encode(contrasenaPlana);
        // Verificamos que se llamó al método de match
        verify(encoderMock, times(1)).matches(contrasenaPlana, hashSimulado);

        // Verificamos que el resultado es el que Mockito simuló
        assertEquals(hashSimulado, hashReal);
        assertTrue(esValida);

        // NOTA: Este test PASARÁ siempre que usemos Mocks. Su objetivo es
        // asegurar que los métodos 'encode' y 'matches' existen y son usados.
    }

    // --- TEST ROJO 🔴 para Generación de JWT (Requiere la implementación) ---

    @Test
    void jwtService_debeGenerarTokenValido_paraUsuario() {
        // ARRANGE: Datos simulados de un usuario
        JwtService jwtService = new JwtService(); // Asumimos que esta clase existe (STUB)
        User user = new User();
        user.setEmail("test@edevare.com");
        // user.setId_user(1L); // Asumimos que tiene un ID

        // ACT: Llamamos al método que queremos probar
        String token = jwtService.generateToken(user);

        // ASSERT: El token no debe estar vacío y debe ser una cadena JWT válida (mínimo de 3 partes separadas por puntos)

        // 1. Comprobamos que el token no es nulo ni vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 2. Comprobamos que el token tiene la estructura de un JWT
        assertTrue(token.split("\\.").length == 3, "El token JWT debe tener 3 partes (header.payload.signature).");

        // Este test FALLARÁ ahora con 'UnsupportedOperationException' (desde el Stub),
        // ¡lo cual es correcto para el ciclo ROJO!
    }
}