package br.com.cmrgsPedro.selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class RegistrarUsuarioTest extends BaseTest {
    static Stream<Arguments> nomesValidos() {
        return Stream.of(
                Arguments.of("R01", "Pedro Teste"),
                Arguments.of("R02", "P"),
                Arguments.of("R03", "Pe"));
    }

    @ParameterizedTest(name = "{0}: cadastrar usuário com nome {1}")
    @MethodSource("nomesValidos")
    void registrarUsuario(String id, String nome) {
        String email = novoEmail();
        assertEquals("New User Signup!", visivel(By.cssSelector(".signup-form h2")).getText());
        preencher(campo("signup-name"), nome);
        preencher(campo("signup-email"), email);
        clicar(campo("signup-button"));

        By titulo = By.xpath("//h2[b[normalize-space()='Enter Account Information']]");
        assertEquals("ENTER ACCOUNT INFORMATION", visivel(titulo).getText().toUpperCase(Locale.ROOT));
        assertEquals(nome, visivel(campo("name")).getDomProperty("value"));
        assertEquals(email, visivel(campo("email")).getDomProperty("value"));

        clicar(By.id("id_gender1"));
        preencher(campo("password"), "SenhaTeste123");
        new Select(visivel(By.id("days"))).selectByValue("15");
        new Select(visivel(By.id("months"))).selectByValue("6");
        new Select(visivel(By.id("years"))).selectByValue("2000");
        clicar(By.id("newsletter"));
        clicar(By.id("optin"));
        assertTrue(visivel(By.id("newsletter")).isSelected());
        assertTrue(visivel(By.id("optin")).isSelected());

        preencher(campo("first_name"), "Pedro");
        preencher(campo("last_name"), "Teste");
        preencher(campo("company"), "Empresa Teste");
        preencher(campo("address"), "Rua Teste 123");
        preencher(campo("address2"), "Apartamento 1");
        new Select(visivel(campo("country"))).selectByVisibleText("Canada");
        preencher(campo("state"), "Ontario");
        preencher(campo("city"), "Toronto");
        preencher(campo("zipcode"), "M5V 1A1");
        preencher(campo("mobile_number"), "2025550100");
        clicar(campo("create-account"));

        assertEquals("ACCOUNT CREATED!",
                visivel(campo("account-created")).getText().toUpperCase(Locale.ROOT));
        continuar();
        assertEquals(nome, visivel(By.xpath("//a[contains(., 'Logged in as')]/b")).getText());

        // Excluo a conta criada no teste, como pede o cenário.
        clicar(By.cssSelector("a[href='/delete_account']"));
        assertEquals("ACCOUNT DELETED!",
                visivel(campo("account-deleted")).getText().toUpperCase(Locale.ROOT));
        continuar();
        visivel(By.id("slider"));
        assertTrue(driver.findElements(By.cssSelector("a[href='/logout']")).isEmpty());
    }

    private void continuar() {
        clicar(campo("continue-button"));
        // Às vezes aparece um anúncio no Continue. Abro a home para seguir o teste.
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/");
        }
    }

    static Stream<Arguments> dadosInvalidos() {
        return Stream.of(
                Arguments.of("R04", "", "EMAIL_NOVO", "signup-name", "valueMissing"),
                Arguments.of("R05", "Pedro Teste", "emailsemarroba.com", "signup-email", "typeMismatch"));
    }

    @ParameterizedTest(name = "{0}: cadastro com campo inválido")
    @MethodSource("dadosInvalidos")
    void registrarComCampoInvalido(String id, String nome, String email, String nomeCampo, String erro) {
        assertEquals("New User Signup!", visivel(By.cssSelector(".signup-form h2")).getText());
        preencher(campo("signup-name"), nome);
        preencher(campo("signup-email"), email.equals("EMAIL_NOVO") ? novoEmail() : email);
        clicar(campo("signup-button"));

        assertTrue(validacao(campo(nomeCampo), erro));
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
        assertTrue(driver.findElements(campo("account-created")).isEmpty());
        assertTrue(driver.findElements(By.cssSelector("a[href='/logout']")).isEmpty());
    }
}
