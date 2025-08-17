package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class CartPage extends  Base {
    private final By cartItems = By.className("inventory_item_name");
    private final By removeFromCartButtons = By.cssSelector("[id^='remove-sauce-labs']");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutButton = By.id("logout_sidebar_link");


    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getCartItemNames() {
        return findElements(cartItems).stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getCartItemCount() {
        return findElements(cartItems).size();
    }

    public boolean isItemInCart(String itemName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.toLowerCase().replace(" ", "-").contains(itemName));
    }

    public String removefromCart() {
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

    public InventoryPage clickContinueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    public int cartBadgeNumber() {
        if (isElementPresent(cartBadge)) {
            return Integer.parseInt(getText(cartBadge));
        }
        return 0;
    }

    public LoginPage logout() {
        click(burgerMenuButton);
        click(logoutButton);
        return new LoginPage(driver);
    }
}
