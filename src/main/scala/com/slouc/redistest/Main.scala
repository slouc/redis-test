package com.slouc.redistest

import com.redis._

object Main {
  def main(args: Array[String]) = {
    val r = new RedisClient("localhost", 6379)
    r.set("key", "some value")
    println(r.get("key"))
  }

}
