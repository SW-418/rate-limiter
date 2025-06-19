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
