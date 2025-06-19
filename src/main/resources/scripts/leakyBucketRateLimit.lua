-- leakyBucketRateLimit.lua

local key = KEYS[1]
local bucketSize = tonumber(ARGV[1])
local dripTimeInMs = tonumber(ARGV[2])
local job = ARGV[3]

local currentCount = redis.call("ZCARD", key)

if currentCount + 1 > bucketSize then
    return 0
end

redis.call("ZADD", key, dripTimeInMs, job)

-- TODO: We may want to set an expiration on the key to prevent memory usage from growing indefinitely
-- but this will have to be generous to allow for any issues with the background job processing these requests

return 1
