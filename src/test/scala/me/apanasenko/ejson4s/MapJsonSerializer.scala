package me.apanasenko.ejson4s

import deserializer.JsonDeserializer
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import serializer.JsonSerializer

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

  describe("Json to Map deserialization") {
    val deserializer = new JsonDeserializer()

    it("simple json deserialization") {
      deserializer.asObject("{'1':1}") should equal(Map("1" -> 1))
      deserializer.asObject("{'1':1,'2':2}") should equal(Map("1" -> 1, "2" -> 2))
    }

    it("complex json deserialization") {
      deserializer.asObject("{'1':1,'2':2,'3':[1]}") should equal(Map("1" -> 1, "2" -> 2, "3" -> List(1)))
      deserializer.asObject("{'1':1,'2':2,'3':[1,2]}") should equal(Map("1" -> 1, "2" -> 2, "3" -> List(1,2)))
      deserializer.asObject("{'1':1,'2':2,'3':[1,2,'3']}") should equal(Map("1" -> 1, "2" -> 2, "3" -> List(1,2,"3")))
    }
  }
}
