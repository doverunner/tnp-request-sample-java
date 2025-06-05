# Doverunner Tnp Job API samples (Java) 

## Prerequisites
- JDK 21 (amazon-corretto recommended)
- Gradle 8.x or higher
- Doverunner TnP service account (Site ID, Email ID,  Access Key required)

## Overview
This repository provides Java-based server-side sample code for integrating with the Doverunner TnP (Transcoding & Packaging) service Job API.

It demonstrates:
- How to obtain an authentication token using HTTP Basic Authentication
- How to create and configure a Job with:
    - Input files (video/audio/subtitles)
    - Output definitions
    - Transcoding options (Audio/Video)
    - DRM and Forensic Watermarking settings
    - Packaging formats (HLS, DASH, CMAF)

## Project Structure
- `src/main/java`: Core domain models and API constants used for Job request
- `src/test/java`: JUnit 5-based test classes for API integration tests (e.g., `TnpApiTest.java`)
- `README.md`: Project description and usage guide

## How to Run
1. Clone this repository:
   ```
   git clone https://github.com/doverunner/tnp-request-sample-java.git
   cd tnp-request-sample-java
   ```

2. Set your credentials in `TnpApiTest.java`:
   ```java
   private static final String SITE_ID = "<Your Site ID>";
   private static final String EMAIL_ID = "<Your Email ID>";
   private static final String ACCESS_KEY = "<Your Access Key>";
   private static final String AUTH_TOKEN = "<Bearer valid-token>";
   ```

3. Run tests to verify:
   ```
   ./gradlew test
   ```

If successful, the console should display logs showing the authentication token retrieval and job creation result.


## Documentation
- [Doverunner TnP AuthToken API](https://doverunner.com/docs/ko/tnp/tnp-api-guide/#api-%EA%B3%B5%ED%86%B5-%EA%B7%9C%EA%B2%A9): How to get an authentication token
- [Doverunner TnP Guide](https://doverunner.com/docs/ko/tnp/tnp-api-guide): API reference including Job, Input, Output, DRM, Packaging, etc.
- [How to Use Doverunner’s TnP Service using API?](https://www.youtube.com/watch?v=JNpU0vkMZKc&t=609s): YouTube guide demonstrating API usage with Postman