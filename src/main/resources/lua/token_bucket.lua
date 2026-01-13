-- KEYS[1] = redis key
-- ARGV[1] = capacity
-- ARGV[2] = refill_rate (tokens per second)
-- ARGV[3] = now (epoch milliseconds)

local key = KEYS[1]

local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local data = redis.call("HMGET", key, "tokens", "last_refill_ts")

local tokens = tonumber(data[1])
local last_refill = tonumber(data[2])

if tokens == nil then
    tokens = capacity
end

if last_refill == nil then
    last_refill = now
end

local delta = math.max(0, now - last_refill)
local refill_tokens = (delta / 1000) * refill_rate

if refill_tokens > 0 then
    tokens = math.min(capacity, tokens + refill_tokens)
    last_refill = now
end

local allowed = 0
if tokens >= 1 then
    tokens = tokens - 1
    allowed = 1
end

redis.call("HMSET", key,
  "tokens", tokens,
  "last_refill_ts", last_refill
)

local ttl = math.ceil((capacity / refill_rate) * 2)
redis.call("EXPIRE", key, ttl)

return { allowed, math.floor(tokens) }
