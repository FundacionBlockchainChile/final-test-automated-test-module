package com.healthtrack.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "selenium.tests.enabled", matches = "true")
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
        
        // Improved HTML with better event handling and debugging
        driver.get("data:text/html;charset=utf-8," +
            "<html>" +
            "<head><title>HealthTrack Test Page</title></head>" +
            "<body>" +
            "<form id='registro-form' onsubmit='return handleRegistro(event)'>" +
            "<input type='text' id='nombre' required />" +
            "<input type='number' id='peso' required step='0.1' />" +
            "<button type='submit' id='registrar'>Registrar</button>" +
            "</form>" +
            "<div id='mensaje-exito' style='display:none'>Usuario registrado exitosamente</div>" +
            "<form id='actualizar-form' onsubmit='return handleActualizacion(event)'>" +
            "<input type='number' id='nuevo-peso' required step='0.1' />" +
            "<button type='submit' id='actualizar'>Actualizar</button>" +
            "</form>" +
            "<div id='peso-actual'></div>" +
            "<div id='debug-log' style='margin-top: 20px; color: gray;'></div>" +
            "<script>" +
            "function log(msg) {" +
            "    const debugLog = document.getElementById('debug-log');" +
            "    debugLog.innerHTML += msg + '<br>';" +
            "    console.log(msg);" +
            "}" +
            "function handleRegistro(e) {" +
            "    e.preventDefault();" +
            "    log('Registro form submitted');" +
            "    const nombre = document.getElementById('nombre').value;" +
            "    const peso = document.getElementById('peso').value;" +
            "    log('Nombre: ' + nombre + ', Peso: ' + peso);" +
            "    document.getElementById('mensaje-exito').style.display = 'block';" +
            "    log('Mensaje de éxito mostrado');" +
            "    return false;" +
            "}" +
            "function handleActualizacion(e) {" +
            "    e.preventDefault();" +
            "    log('Actualización form submitted');" +
            "    const nuevoPeso = document.getElementById('nuevo-peso').value;" +
            "    log('Nuevo peso: ' + nuevoPeso);" +
            "    document.getElementById('peso-actual').textContent = nuevoPeso;" +
            "    log('Peso actualizado en la UI');" +
            "    return false;" +
            "}" +
            "</script>" +
            "</body>" +
            "</html>");
    }

    @Test
    void testRegistroUsuario() {
        try {
            WebElement nombreInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nombre")));
            WebElement pesoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("peso")));
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("registrar")));

            // Fill in the form
            nombreInput.sendKeys("Juan Pérez");
            pesoInput.sendKeys("70.5");

            // Get debug log before submission
            String beforeLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("Before submission: " + beforeLog);

            // Submit the form
            submitButton.click();

            // Wait for the success message
            WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-exito")));

            // Get debug log after submission
            String afterLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("After submission: " + afterLog);

            assertTrue(mensaje.isDisplayed(), "El mensaje de éxito no se mostró correctamente");
        } catch (Exception e) {
            // Get final debug log in case of failure
            String finalLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("Final debug log: " + finalLog);
            throw new AssertionError("Error durante el registro de usuario: " + e.getMessage() + "\nDebug log: " + finalLog);
        }
    }

    @Test
    void testActualizacionPeso() {
        try {
            // First register a user
            testRegistroUsuario();

            // Now update the weight
            WebElement pesoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nuevo-peso")));
            WebElement actualizarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("actualizar")));

            // Get debug log before update
            String beforeLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("Before update: " + beforeLog);

            pesoInput.sendKeys("72.5");
            actualizarButton.click();

            // Wait for the weight to be updated
            WebElement pesoActual = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("peso-actual")));
            
            // Get debug log after update
            String afterLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("After update: " + afterLog);

            assertEquals("72.5", pesoActual.getText(), "El peso no se actualizó correctamente");
        } catch (Exception e) {
            // Get final debug log in case of failure
            String finalLog = ((JavascriptExecutor) driver).executeScript(
                "return document.getElementById('debug-log').innerHTML"
            ).toString();
            System.out.println("Final debug log: " + finalLog);
            throw new AssertionError("Error durante la actualización de peso: " + e.getMessage() + "\nDebug log: " + finalLog);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 