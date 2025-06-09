-- tokenRateLimit.lua

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local currentTime = tonumber(ARGV[2])
local refreshIntervalInMs = tonumber(ARGV[3])
local tokensPerInterval = tonumber(ARGV[4])

local tokens = tonumber(redis.call("HGET", key, "tokens") or limit)
local resetTime = tonumber(redis.call("HGET", key, "resetTime") or currentTime)

local msSinceReset = currentTime - resetTime
local intervalsSinceReset = math.floor(msSinceReset / refreshIntervalInMs)
local newTokens = math.min(tokens + (intervalsSinceReset * tokensPerInterval), limit)

if newTokens == 0 then
    return 0
end

redis.call("HSET", key, "tokens", newTokens - 1, "resetTime", currentTime)

-- TODO: Add TTL to prevent memory usage from growing indefinitely

return 1
