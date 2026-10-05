package br.com.cmrgsPedro.selenium;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void abrirNavegador() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,900");
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.get("https://automationexercise.com/");

        // Espero a página inicial carregar antes de abrir o formulário.
        visivel(By.id("slider"));
        clicar(By.cssSelector("a[href='/login']"));
    }

    protected By campo(String nome) {
        return By.cssSelector("[data-qa='" + nome + "']");
    }

    protected WebElement visivel(By seletor) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(seletor));
    }

    protected void preencher(By seletor, String texto) {
        WebElement elemento = visivel(seletor);
        elemento.clear();
        if (!texto.isEmpty()) {
            elemento.sendKeys(texto);
        }
    }

    protected void clicar(By seletor) {
        WebElement elemento = visivel(seletor);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", elemento);
        wait.until(ExpectedConditions.elementToBeClickable(seletor)).click();
    }

    protected String novoEmail() {
        // Cada execução usa um e-mail diferente para não repetir o cadastro.
        return "teste." + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

    protected boolean validacao(By seletor, String erro) {
        return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity[arguments[1]];", visivel(seletor), erro));
    }

    @AfterEach
    public void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }
}
