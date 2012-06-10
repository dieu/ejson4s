package me.apanasenko.json

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import parser.JsonSerializer

/**
 * @author apanasenko
 */

class MapJsonSerializer extends Spec with ShouldMatchers {
  describe("Map to Json serialization") {
    val serializer = new JsonSerializer()
    
    it("string, string map with one record serialization") {
      val map = Map("test" -> "value")
      val builder = new java.lang.StringBuilder()
      serializer.toString(map, builder)
      builder.toString should equal("{'test':'value'}")
    }

    it("string, string map serialization") {
      val map = Map("test1" -> "value1", "test2" -> "value2")
      val builder = new java.lang.StringBuilder()
      serializer.toString(map, builder)
      builder.toString should equal("{'test1':'value1','test2':'value2'}")
    }

    it("string, list[string] map serialization") {
      val map = Map("test" -> List("test"))
      serializer.toString(map) should equal("{'test':['test']}")
    }
  }
}
