# Citi Market Data Monitor

A real-time stock monitoring system built in Java.

## Features
- Fetches live stock data (DJIA)
- Handles API rate limits with fallback mechanism
- Displays real-time data using chart visualization
- Implements queue-based data storage

## Tech Stack
- Java
- Gradle
- JFreeChart
- REST APIs

## Key Learnings
- API integration & error handling
- Real-time data processing
- System design basics
- Data visualization

## Note
Yahoo Finance API may return HTTP 429 due to rate limiting. A fallback API is implemented to ensure continuity.
