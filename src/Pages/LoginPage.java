package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Base {
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage login(String Username){
        driver.get("https://www.saucedemo.com");
        sendKeys(usernameField, Username);
        sendKeys(passwordField, "secret_sauce");
        click(loginButton);
        return new InventoryPage(driver);
    }
}