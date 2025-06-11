-- fixedWindowRateLimit.lua

local key = KEYS[1]
local limit = tonumber(ARGV[1])

local requests = tonumber(redis.call("HGET", key, "tokens") or 0) 

if requests + 1 > limit then
    return 0
end

redis.call("HSET", key, "tokens", requests + 1)

return 1
