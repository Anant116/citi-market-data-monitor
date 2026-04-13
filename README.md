# 📈 Citi Market Data Monitor

A real-time stock market monitoring system built using Java that fetches live market data, handles API failures, and visualizes trends dynamically.

---

## 🚀 Features
- Real-time stock price tracking (DJIA)
- Handles API rate limiting (HTTP 429)
- Fallback mechanism for reliability
- Live time-series data visualization
- Queue-based data handling

---

## 🧠 System Design
- Scheduled polling using multithreading
- Fault-tolerant API handling
- Real-time UI updates using charts

---

## ⚙️ Tech Stack
- Java (Core + Multithreading)
- Gradle
- REST APIs
- JFreeChart

---

## ⚠️ Challenges
- Yahoo Finance API rate limiting
- Implemented fallback strategy to ensure continuous data flow

---

## 📸 Screenshots
<img width="1366" height="768" alt="Screenshot_20260412_223249" src="https://github.com/user-attachments/assets/86e829c1-28e3-4b8f-9ffe-b73c57b18a58" />


---

## 📌 Future Improvements
- Use paid financial API
- Store data in database
- Build web dashboard using Spring Boot
