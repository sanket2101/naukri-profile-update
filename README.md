# Naukri Profile Auto Refresh (Selenium + GitHub Actions)

This project automates **daily Naukri profile refresh** by performing  
**Edit Profile → Save** using **Selenium (Java)** and **GitHub Actions CI scheduling**.

It helps keep the profile **active and fresh in recruiter searches** without manual effort.

---

## Features

- Selenium 4 with Java 11
- TestNG-based execution
- Works locally and on GitHub Actions (CI)
- Headless execution only in CI
- Secure credential handling via GitHub Secrets
- Daily scheduled execution using cron
- Lightweight and free-tier friendly

---

## How It Works

1. Logs into Naukri (in CI using secrets)
2. Navigates to the profile page
3. Clicks **Edit Profile**
4. Clicks **Save**
5. Triggers profile refresh event

---

## Tech Stack

- **Language:** Java 11  
- **Automation:** Selenium 4.21  
- **Test Framework:** TestNG  
- **CI/CD:** GitHub Actions  
- **Browser:** Chrome (Headless in CI)

---

## Project Structure
naukri-profile-update/
├── pom.xml
├── src/test/java/
│ └── NaukriProfileUpdate.java
└── .github/workflows/
└── schedule.yml


---

## Scheduling ##

on:
  schedule:
    - cron: '25 3 * * *'   # 8:55 AM IST

---

| Secret Name | Description |
|------------|------------|
| `NAUKRI_USER` | Naukri login email |
| `NAUKRI_PASS` | Naukri login password |

---


## Headless Execution Logic

The browser runs headless only in CI:

```java
if (System.getenv("CI") != null) {
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
}


