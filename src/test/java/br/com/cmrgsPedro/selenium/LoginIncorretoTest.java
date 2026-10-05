package br.com.cmrgsPedro.selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

public class LoginIncorretoTest extends BaseTest {
    static Stream<Arguments> senhasIncorretas() {
        return Stream.of(
                Arguments.of("L01", "senhaIncorreta123"),
                Arguments.of("L02", "x"),
                Arguments.of("L03", "xy"));
    }

    @ParameterizedTest(name = "{0}: login com senha incorreta")
    @MethodSource("senhasIncorretas")
    void loginComEmailESenhaIncorretos(String id, String senha) {
        assertEquals("Login to your account", visivel(By.cssSelector(".login-form h2")).getText());
        preencher(campo("login-email"), novoEmail());
        preencher(campo("login-password"), senha);
        clicar(campo("login-button"));

        assertEquals("Your email or password is incorrect!",
                visivel(By.cssSelector(".login-form p")).getText());
        assertTrue(driver.findElements(By.cssSelector("a[href='/logout']")).isEmpty());
    }

    static Stream<Arguments> camposInvalidos() {
        return Stream.of(
                Arguments.of("L04", "EMAIL_NOVO", "", "login-password", "valueMissing"),
                Arguments.of("L05", "emailsemarroba.com", "senhaIncorreta123", "login-email", "typeMismatch"));
    }

    @ParameterizedTest(name = "{0}: login com campo inválido")
    @MethodSource("camposInvalidos")
    void loginComCampoInvalido(String id, String email, String senha, String nomeCampo, String erro) {
        assertEquals("Login to your account", visivel(By.cssSelector(".login-form h2")).getText());
        preencher(campo("login-email"), email.equals("EMAIL_NOVO") ? novoEmail() : email);
        preencher(campo("login-password"), senha);
        clicar(campo("login-button"));

        // Nesses casos, o próprio navegador bloqueia o envio.
        assertTrue(validacao(campo(nomeCampo), erro));
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
        assertTrue(driver.findElements(By.cssSelector(".login-form p")).isEmpty());
        assertTrue(driver.findElements(By.cssSelector("a[href='/logout']")).isEmpty());
    }
}
