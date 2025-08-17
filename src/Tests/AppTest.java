package Tests;

import Pages.CartPage;
import Pages.InventoryPage;
import Pages.LoginPage;
import Utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class AppTest {
    WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setup() {
        driver = DriverManager.getDriver("edge");
        loginPage = new LoginPage(driver);
        inventoryPage = loginPage.login("standard_user");
        inventoryPage.addToCart();
        inventoryPage.addToCart();
    }

    @Test
    public void testAddToCartFunctionality() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Add to Cart Functionality");

        int initialCartCount = inventoryPage.cartBadgeNumber();
        String addedItemId = inventoryPage.addToCart();
        int finalCartCount = inventoryPage.cartBadgeNumber();
        softAssert.assertEquals(finalCartCount, initialCartCount + 1,
                "Cart count should increase by 1");

        softAssert.assertTrue(inventoryPage.isItemRemovable(addedItemId),
                "Remove button should be present for added item");

        softAssert.assertFalse(inventoryPage.isItemAddable(addedItemId),
                "Add button should not be present for added item");

        cartPage = inventoryPage.clickCartButton();
        softAssert.assertTrue(cartPage.isItemInCart(addedItemId),
                "Item should appear in cart");

        softAssert.assertAll();
    }

    @Test
    public void testRemoveFromInventoryFunctionality() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Remove from Inventory Functionality");

        int initialCartCount = inventoryPage.cartBadgeNumber();
        String removedItemId = inventoryPage.removeFromCart();
        int finalCartCount = inventoryPage.cartBadgeNumber();
        softAssert.assertEquals(finalCartCount, initialCartCount - 1,
                "Cart count should decrease by 1");

        softAssert.assertFalse(inventoryPage.isItemRemovable(removedItemId),
                "Remove button should not be present for removed item");

        softAssert.assertTrue(inventoryPage.isItemAddable(removedItemId),
                "Add button should be present for removed item");

        cartPage = inventoryPage.clickCartButton();
        softAssert.assertFalse(cartPage.isItemInCart(removedItemId),
                "Item should not appear in cart");
        softAssert.assertAll();
    }

    @Test
    public void testCartButtonNavigation() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Cart Button Navigation");

        cartPage = inventoryPage.clickCartButton();
        String currentUrl = cartPage.getCurrentUrl();

        softAssert.assertEquals(currentUrl, "https://www.saucedemo.com/cart.html",
                "Should navigate to cart page");

        softAssert.assertAll();
    }

    @Test
    public void testCartBadgeAccuracy() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Cart Badge Accuracy");

        cartPage = inventoryPage.clickCartButton();
        int itemsInCart = cartPage.getCartItemCount();
        int badgeNumber = cartPage.cartBadgeNumber();

        softAssert.assertEquals(badgeNumber, itemsInCart,
                "Cart badge should match number of items in cart");

        softAssert.assertAll();
    }

    @Test
    public void testCartPersistenceAcrossSessions() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Cart Persistence Across Sessions");

        List<String> initialButtonIds = inventoryPage.getAllItemsAddedToCarIds();

        loginPage = inventoryPage.logout();
        inventoryPage = loginPage.login("standard_user");

        List<String> finalButtonIds = inventoryPage.getAllItemsAddedToCarIds();

        softAssert.assertEquals(finalButtonIds, initialButtonIds,
                "Cart items should persist across sessions");

        softAssert.assertAll();
    }

    @Test
    public void testRemoveFromCartPage() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Remove from Cart Page");

        cartPage = inventoryPage.clickCartButton();
        String removedItemId = cartPage.removefromCart();

        if (removedItemId != null) {

            softAssert.assertFalse(cartPage.isItemInCart(removedItemId),
                    "Item should not appear in cart after removal");

            inventoryPage = cartPage.clickContinueShopping();
            softAssert.assertFalse(inventoryPage.isItemRemovable(removedItemId),
                    "Remove button should not be present in inventory");
        }

        softAssert.assertAll();
    }

    @Test
    public void testContinueShoppingButton() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Continue Shopping Button");

        cartPage = inventoryPage.clickCartButton();
        List<String> cartItemsBefore = cartPage.getCartItemNames();

        inventoryPage = cartPage.clickContinueShopping();
        String currentUrl = inventoryPage.getCurrentUrl();

        softAssert.assertEquals(currentUrl, "https://www.saucedemo.com/inventory.html",
                "Should navigate back to inventory page");

        // Verify cart items are still there
        cartPage = inventoryPage.clickCartButton();
        List<String> cartItemsAfter = cartPage.getCartItemNames();

        softAssert.assertEquals(cartItemsAfter, cartItemsBefore,
                "Cart items should remain the same");

        softAssert.assertAll();
    }

    @Test
    public void testLogoutFunctionality() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Testing Logout Functionality");

        loginPage = inventoryPage.logout();
        String currentUrl = loginPage.getCurrentUrl();

        softAssert.assertEquals(currentUrl, "https://www.saucedemo.com/",
                "Should navigate to login page after logout");

        softAssert.assertAll();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}