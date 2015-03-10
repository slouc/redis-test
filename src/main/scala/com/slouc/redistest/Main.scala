package com.slouc.redistest

import com.redis._

/**
 * @author slouc
 *
 */
object Main {
  def main(args: Array[String]) = {

    val r = new RedisClient("localhost", 6379)
    r.flushall

    r.set("key", "some value")
    println(r.get("key").get) // some value

    r.set("connections", 10)
    /*** incr is atomic! ***/
    r.incr("connections")
    println(r.get("connections").get) // 11

    r.del("connections")
    println(r.get("connections").isDefined) // false

    r.set("expires", "redis demo")
    r.expire("expires", 60)
    Thread.sleep(1000)
    println(r.ttl("expires").get) // 59
    
    r.set("expires", "redis demo 2")
    println(r.ttl("expires").get) // -1, no TTL is set
    println(r.ttl("expires2").get) // -2, doesn't exist
    
    r.rpush("numbers", 1)
    r.rpush("numbers", 2)
    r.rpush("numbers", 3)
    r.lpush("numbers", 4)
    println(r.llen("numbers").get) // 4
    println(r.lrange("numbers", 0, -1).get.flatten)// List(4, 1, 2, 3)
    println(r.lrange("numbers", 2, 3).get.flatten)// List(2, 3)
    r.lpop("numbers")
    println(r.lrange("numbers", 0, -1).get.flatten)// List(1, 2, 3)
    r.rpop("numbers")
    println(r.lrange("numbers", 0, -1).get.flatten)// List(1, 2)
    
    r.sadd("integers", 1)
    r.sadd("integers", 23)
    r.sadd("integers", 47)
    println(r.smembers("integers").get.flatten) // Set(1, 23, 47)
    r.srem("integers", 23)
    println(r.smembers("integers").get.flatten) // Set(1, 47)
    println(r.sismember("integers", 47)) // true
    println(r.sismember("integers", 48)) // false    
    r.sadd("primes", 2)
    r.sadd("primes", 3)
    r.sadd("primes", 47)
    println(r.sunion("integers", "primes").get.flatten) // Set(1, 2, 3, 47)
    
    r.zadd("sortedSet", 1, "matt")
    r.zadd("sortedSet", 3, "chris")
    r.zadd("sortedSet", 2, "dom")
    println(r.zrange("sortedSet", 0, -1).get) // List(matt, dom, chris)
    
    r.hset("hash", "user:007", "james")
    println(r.hget("hash", "user:007").get) // james
    r.hmset("hash", Map("user:42" -> "douglas", "user:300" -> "gerrard"))
    println(r.hgetall("hash").get) // Map(user:007 -> james, user:42 -> douglas, user:300 -> gerrard)
    r.hdel("hash", "user:007")
    println(r.hgetall("hash").get) //  Map(user:42 -> douglas, user:300 -> gerrard)
    r.hset("hashNumbers", "first", 1)
    r.hincrby("hashNumbers", "first", 2)
    println(r.hget("hashNumbers", "first").get) // 3
    
  }

}
