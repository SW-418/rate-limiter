-- fixedWindowRateLimit.lua

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local windowSizeInSeconds = tonumber(ARGV[2])

local requests = tonumber(redis.call("GET", key) or "0") 

if requests + 1 > limit then
    return 0
end


if requests == 0 then
    redis.call("SET", key, 1, "EX", windowSizeInSeconds)
else
    redis.call("INCR", key)
end

return 1
