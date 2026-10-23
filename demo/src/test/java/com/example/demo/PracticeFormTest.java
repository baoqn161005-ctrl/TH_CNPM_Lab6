package com.example.demo;

import pages.PracticeFormPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class PracticeFormTest {
    private WebDriver driver;
    private PracticeFormPage formPage;
    private static final String TARGET_URL = "https://demoqa.com/automation-practice-form";
    private String sampleFilePath;

    @BeforeClass
    public void setupClass() throws IOException {
        // Tạo một file tạm để phục vụ cho test upload file (Yêu cầu 4)
        File tempFile = new File("test_sample.txt");
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        sampleFilePath = tempFile.getAbsolutePath();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        formPage = new PracticeFormPage(driver);
    }

    // Yêu cầu 7: Khai báo 3 bộ dữ liệu kiểm thử khác nhau
    @DataProvider(name = "formDataProvider")
    public Object[][] getFormData() {
        return new Object[][]{
            {"Nguyen", "An", "an.nguyen@example.com", "0912345678", "2000", "May", "15"},
            {"Tran", "Binh", "binh.tran@example.com", "0987654321", "1998", "August", "20"},
            {"Le", "Cuong", "cuong.le@example.com", "0905112233", "2002", "December", "05"}
        };
    }

    @Test(dataProvider = "formDataProvider")
    public void testAutomationPracticeForm(String firstName, String lastName, String email, String mobile, String year, String month, String day) {
        System.out.println("\n==========================================");
        System.out.println("BẮT ĐẦU CHẠY TEST CASE CHO: " + firstName + " " + lastName);

        // 1. Mở trang và kiểm tra Tiêu đề / URL (Yêu cầu 1)
        formPage.openPage(TARGET_URL);
        Assert.assertEquals(formPage.getCurrentUrl(), TARGET_URL, "URL không đúng!");
        Assert.assertFalse(formPage.getPageTitle().isEmpty(), "Tiêu đề trang bị rỗng!");

        // 2. Điền thông tin cá nhân (Yêu cầu 2)
        formPage.fillPersonalDetails(firstName, lastName, email, mobile);

        // 3. Chọn ngày sinh (Yêu cầu 3)
        formPage.selectDateOfBirth(year, month, day);

        // 4. Upload file (Yêu cầu 4)
        formPage.uploadFile(sampleFilePath);

        // 8. Chụp ảnh màn hình TRƯỚC khi Submit (Yêu cầu 8)
        formPage.captureScreen("Before_Submit_" + firstName);

        // 5 & 8. Submit form và kiểm tra kết quả
        formPage.submitForm();

        // Kiểm tra xuất hiện Modal (Yêu cầu 5 & 8)
        boolean isSuccess = formPage.isConfirmationModalDisplayed();

        if (isSuccess) {
            System.out.println("[PASS] Modal xác nhận xuất hiện thành công!");
            String modalContent = formPage.getModalText();
            System.out.println("Nội dung xác nhận:\n" + modalContent);
            
            // Chụp ảnh màn hình SAU khi Submit thành công (Yêu cầu 8)
            formPage.captureScreen("After_Submit_Success_" + firstName);
        } else {
            System.out.println("[FAIL] Không xuất hiện Modal xác nhận!");
            // Chụp ảnh màn hình khi LỖI (Yêu cầu ở phần tổng quan)
            formPage.captureScreen("Error_Submit_" + firstName);
            Assert.fail("Gửi Form không thành công hoặc Modal không xuất hiện!");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}