# rate-limiter
Implementing rate limiter algorithms using Java/Spring and Redis

# Running Locally
1. Ensure you have docker installed
2. Run `docker compose up` to start the rate limiter and Redis containers
3. The rate limiter will be available at `http://localhost:8080`
4. Redis Insight will be available at `http://localhost:5540`

# Algorithms
- Fixed Window ✅
- Sliding Window ✅
- Token Bucket ✅
- Leaky Bucket ✅
    - The job for background processing is hacky and not suitable for a distributed production environment

# API Endpoints

All endpoints are available under the base path `/api/v1/rate-limit`.

## Fixed Window

- **Method:** `GET`
- **Path:** `/fixed-window`
- **Headers:**
    - `X-User-Id` (string, required): Identifier for the user.

## Sliding Window

- **Method:** `GET`
- **Path:** `/sliding-window`
- **Headers:**
    - `X-User-Id` (string, required): Identifier for the user.

## Token Bucket

- **Method:** `GET`
- **Path:** `/token-bucket`
- **Headers:**
    - `X-User-Id` (string, required): Identifier for the user.

## Leaky Bucket

- **Method:** `POST`
- **Path:** `/leaky-bucket`
- **Headers:**
    - `X-User-Id` (string, required): Identifier for the user.
- **Request Body:**
    ```json
    {
        "callbackUrl": "string",
        "httpMethod": "string",
        "body": {}
    }
    ```
    - `callbackUrl` (string, required): The URL to call back when the job is processed.
    - `httpMethod` (string, required): The HTTP method to use for the callback (e.g., "GET", "POST").
    - `body` (object, optional): The body to send with the callback request.
