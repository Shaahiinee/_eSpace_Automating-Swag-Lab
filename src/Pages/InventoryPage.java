package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class InventoryPage extends Base {
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutButton = By.id("logout_sidebar_link");
    private final By cartButton = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By addToCartButtons = By.className("btn_primary");
    private final By removeFromCartButtons = By.className("btn_secondary");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String addToCart() {
        List<WebElement> addButtons = findElements(addToCartButtons);

        if (addButtons.isEmpty()) {
            return null;
        }

        int addbuttonIndex = random.nextInt(addButtons.size());
        String ItemAdded = Objects.requireNonNull(addButtons.get(addbuttonIndex)
                        .getAttribute("id"))
                .replace("add-to-cart-", "");
        addButtons.get(addbuttonIndex).click();
        return ItemAdded;
    }

    public String removeFromCart() {
        List<WebElement> removeButtons = findElements(removeFromCartButtons);

        if (removeButtons.isEmpty()) {
            return null;
        }

        int RemoveButtonIndex = random.nextInt(removeButtons.size());
        String ItemRemoved = Objects.requireNonNull(removeButtons.get(RemoveButtonIndex)
                        .getAttribute("id"))
                .replace("remove-", "");
        removeButtons.get(RemoveButtonIndex).click();
        return ItemRemoved;
    }

    public CartPage clickCartButton() {
        click(cartButton);
        return new CartPage(driver);
    }

    public int cartBadgeNumber() {
        if (isElementPresent(cartBadge)) {
            return Integer.parseInt(getText(cartBadge));
        }
        return 0;
    }

    public boolean isItemRemovable(String itemId) {
        By removeButtonLocator = By.id("remove-" + itemId);
        return isElementPresent(removeButtonLocator);
    }

    public boolean isItemAddable(String itemId) {
        By addButtonLocator = By.id("add-to-cart-" + itemId);
        return isElementPresent(addButtonLocator);
    }

    public List<String> getAllItemsAddedToCarIds() {
        return findElements(removeFromCartButtons).stream()
                .map(element -> element.getAttribute("id"))
                .toList();
    }

    public LoginPage logout() {
        click(burgerMenuButton);
        click(logoutButton);
        return new LoginPage(driver);
    }

}