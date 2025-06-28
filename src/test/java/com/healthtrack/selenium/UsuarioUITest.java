package com.healthtrack.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioUITest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final int TIMEOUT = 10;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        driver.get(BASE_URL);
    }

    @Test
    void testRegistroUsuario() {
        try {
            WebElement nombreInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nombre")));
            WebElement pesoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("peso")));
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("registrar")));

            nombreInput.sendKeys("Juan Pérez");
            pesoInput.sendKeys("70.5");
            submitButton.click();

            WebElement mensaje = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mensaje-exito")));
            assertTrue(mensaje.getText().contains("Usuario registrado exitosamente"), "El mensaje de éxito no se mostró correctamente");
        } catch (Exception e) {
            throw new AssertionError("Error durante el registro de usuario: " + e.getMessage());
        }
    }

    @Test
    void testActualizacionPeso() {
        try {
            testRegistroUsuario();

            WebElement pesoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nuevo-peso")));
            WebElement actualizarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("actualizar")));

            pesoInput.sendKeys("72.5");
            actualizarButton.click();

            WebElement pesoActual = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("peso-actual")));
            assertEquals("72.5", pesoActual.getText(), "El peso no se actualizó correctamente");
        } catch (Exception e) {
            throw new AssertionError("Error durante la actualización de peso: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 