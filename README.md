# ✅ Novillex Automated Tests

A comprehensive **test automation framework** built using **Java, Maven, TestNG, and Allure**, designed for testing **Web, API, and Mobile applications** across multiple platforms.

---

## 📁 Project Structure

novillex_automated_tests/
├── api/           # API testing module
├── cbs/           # CBS testing module
├── cts/           # CTS testing module
├── common/        # Common utilities and shared code
├── mobile/        # Mobile application testing (Appium)
└── pom.xml        # Parent Maven configuration

---

## ✅ Prerequisites

### 🖥 System Requirements

- **Operating System:** Windows / macOS / Linux
- **RAM:** Minimum 8 GB (16 GB recommended)
- **Disk Space:** At least 2 GB free

---

### 🧰 Software Requirements

#### 1️⃣ Java Development Kit (JDK)
- **Version:** Java 21 or higher

```bash
java -version
```

---

#### 2️⃣ Apache Maven
- **Version:** 3.9.0 or higher

```bash
mvn -version
```

---

#### 3️⃣ Allure Report CLI *(Optional but Recommended)*
- **Version:** 2.25.0 or higher

```bash
# Windows (Scoop)
scoop install allure

# macOS (Homebrew)
brew install allure
```

```bash
allure --version
```

---

#### 4️⃣ Git
- **Version:** 2.x or higher

---

### 📱 Mobile Testing Requirements

- Appium v2.0+
- Android SDK (Android testing)
- iOS Simulator (macOS only)

---

## ⚙️ Framework Installation & Setup

### Clone Repository

```bash
git clone <https://github.com/qastudio-putitrightdone/novillex_automated_tests.git>
cd novillex_automated_tests
```

### Install Dependencies

```bash
mvn clean install
```

---

## ▶️ Running Tests

```bash
mvn clean test
```

### Module Specific Execution

```bash
mvn clean test -pl cbs
mvn clean test -pl cts
mvn clean test -pl mobile
```

---

## 📊 Allure Reporting

```bash
mvn clean test
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

---

## 🛠 Useful Commands

```bash
mvn -X clean test
mvn clean install -DskipTests
mvn clean test -T 1C
```

---

**Last Updated:** April 2026
