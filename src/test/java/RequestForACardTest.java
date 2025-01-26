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

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestForACardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void fullForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Сидоров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79035714531");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        var actualText = actualElement.getText().trim();
        assertEquals("Ваша заявка принята! В данный момент она находится на рассмотрении. Менеджер нашего банка свяжется с Вами в указанное Вами время.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void emptyForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Данное поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void invalidNameSurname() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Sergey Sidorov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79035714531");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("В поле имя и фамилия допускаются только кирилические буквы и пробелы", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void invalidNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Сидоров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+790357145311");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Номер телефона указан не корректно. В поле телефон для обратной связи допускается не более 11 цифровых символов", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void emptyNameSurname () {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79035714531");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Данное поле не может быть пустым", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void emptyNumber () {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Сидоров");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Данное поле не может быть пустым", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void WithoutCheckbox () {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Сидоров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79035714531");
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        assertTrue(actualElement.isDisplayed());
    }
}
