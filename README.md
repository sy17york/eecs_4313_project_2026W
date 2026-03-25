# EECS 4313 Project – Selenium Test Suite

This project demonstrates automated web testing using Selenium WebDriver. The test suite validates the functionality of a web application (running on Tomcat).

## Test Files

All Selenium tests are located in `src/main/java/SeleniumTest/`:

| `BaseTest.java` | Helper class that sets up the WebDriver and provides common utilities (e.g., waiting, pausing) for all tests. |
| `DemoTest.java` | Manual test cases written to demonstrate basic Selenium usage and verification of core functionality. |
| `DemoTest_ai.java` | AI‑generated test cases that explore additional scenarios. This version may contain some issues (e.g., improper state handling). |
| `DemoTest_ai_fix.java` | Final version of the AI‑generated tests, with issues resolved.

## Prerequisites

- **Java** – version 8 or higher
- **Apache Tomcat 9.0.89** – the web application must be deployed and running
- **Selenium Java 4.41.0** – included in the project’s dependencies
- **EdgeDriver** – download the appropriate version for your Edge browser from [Microsoft Edge WebDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/) and place it in a known location (or ensure it is in your system PATH).

## Running the Tests

1. **Start the web application**  
   Deploy the project on Tomcat 9.0.89 and start the server. The application should be accessible at `http://localhost:8080/` (or your configured

 base URL).

2. **Update the base URL** (if needed)  
   In `BaseTest.java`, adjust the `baseUrl` variable to match your Tomcat deployment.

3. **Run the tests**  
   You can execute the test classes individually or as a suite using your IDE (Eclipse, IntelliJ) or a build tool like Maven/Gradle.

   - **Eclipse** – right‑click on a test class → **Run As → JUnit Test**
   - **Maven** – `mvn test`

## Dependencies

The project uses the following libraries (provided in `WEB-INF/lib`):
- Selenium Java 4.41.0
- SQLite JDBC (for the demo application’s database)
- Commons FileUpload (for handling file uploads in the admin interface)

## Notes

- The EdgeDriver version must match the installed Edge browser version.
- If you encounter issues with the WebDriver location, set the system property `webdriver.edge.driver` in `BaseTest.java`:
  ```java
  System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");


## Vedio demos

demo 1 manual: 

https://github.com/user-attachments/assets/b3f99722-24cb-4a16-815c-5bd31ee06d9a

demo 2 ai:

https://github.com/user-attachments/assets/72133b4c-d725-4166-8f57-2d767fe55bf5

demo 3 ai_fixed:

https://github.com/user-attachments/assets/8f6cde1f-7fc1-46ff-a1d7-59fcbfe66c11

