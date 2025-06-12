-- slidingWindowRateLimit.lua

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local currentTimeInMs = tonumber(ARGV[2])
local windowStartInMs = tonumber(ARGV[3])
local windowSizeInSeconds = tonumber(ARGV[4])

-- Remove reqs before start of new window
redis.call("ZREMRANGEBYSCORE", key, 0, windowStartInMs)

-- Calculate reqs in window
local requestsInWindow = tonumber(redis.call("ZCARD", key))

-- Check if limit exceeded
if requestsInWindow + 1 > limit then
    if requestsInWindow > 0 then
        -- Update window expiry
        redis.call("EXPIRE", key, windowSizeInSeconds)
    end
    return 0
end

-- Add req to window
redis.call("ZADD", key, currentTimeInMs, currentTimeInMs)

-- Update window expiry
redis.call("EXPIRE", key, windowSizeInSeconds)

return 1
