package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PracticeFormPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---
    private By firstNameInput = By.id("firstName");
    private By lastNameInput = By.id("lastName");
    private By emailInput = By.id("userEmail");
    private By genderMaleRadio = By.cssSelector("label[for='gender-radio-1']");
    private By mobileInput = By.id("userNumber");
    
    // Date Picker Locators
    private By dateOfBirthInput = By.id("dateOfBirthInput");
    private By monthSelect = By.className("react-datepicker__month-select");
    private By yearSelect = By.className("react-datepicker__year-select");
    
    // File Upload Locator
    private By uploadPictureInput = By.id("uploadPicture");
    
    // Submit & Modal Locators
    private By submitButton = By.id("submit");
    private By modalTitle = By.id("example-modal-sizes-title-lg");
    private By modalTableBody = By.xpath("//div[@class='modal-body']//tbody");

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openPage(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void fillPersonalDetails(String firstName, String lastName, String email, String mobile) {
        // Explicit Wait tại vị trí 1
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        
        driver.findElement(firstNameInput).clear();
        driver.findElement(firstNameInput).sendKeys(firstName);
        
        driver.findElement(lastNameInput).clear();
        driver.findElement(lastNameInput).sendKeys(lastName);
        
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        
        // Click chọn Giới tính Nam
        driver.findElement(genderMaleRadio).click();
        
        driver.findElement(mobileInput).clear();
        driver.findElement(mobileInput).sendKeys(mobile);

        System.out.println("-> Đã nhập thông tin: " + firstName + " " + lastName + " | " + email + " | " + mobile);
    }

    // Yêu cầu 3: Chọn ngày sinh từ Date Picker
    public void selectDateOfBirth(String year, String month, String day) {
        driver.findElement(dateOfBirthInput).click();

        Select selectYear = new Select(driver.findElement(yearSelect));
        selectYear.selectByValue(year);

        Select selectMonth = new Select(driver.findElement(monthSelect));
        selectMonth.selectByVisibleText(month);

        // Chọn ngày cụ thể (tránh ngày của tháng khác hiển thị mờ)
        String dayXpath = String.format("//div[contains(@class,'react-datepicker__day--0%s') and not(contains(@class,'react-datepicker__day--outside-month'))]", day);
        driver.findElement(By.xpath(dayXpath)).click();
    }

    // Yêu cầu 4: Upload File
    public void uploadFile(String filePath) {
        WebElement uploadElement = driver.findElement(uploadPictureInput);
        uploadElement.sendKeys(filePath);
    }

    public void submitForm() {
        WebElement btn = driver.findElement(submitButton);
        // Cuộn trang tới nút submit để tránh bị che bởi quảng cáo
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    // Yêu cầu 5 & 8: Kiểm tra Modal Xác nhận (Sử dụng Explicit Wait)
    public boolean isConfirmationModalDisplayed() {
        try {
            // Explicit Wait tại vị trí 2
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
            return driver.findElement(modalTitle).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getModalText() {
        return driver.findElement(modalTableBody).getText();
    }

    // Hàm Chụp Màn Hình (Sử dụng đoạn code mẫu từ đề bài)
    public void captureScreen(String prefix) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File dest = new File("screenshots/" + prefix + "_" + time + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("-> Đã lưu ảnh màn hình: " + dest.getPath());
        } catch (IOException e) {
            System.err.println("Lỗi khi chụp ảnh màn hình: " + e.getMessage());
        }
    }
}