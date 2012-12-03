package me.apanasenko.ejson4s

import deserializer.JsonDeserializer
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import serializer.JsonSerializer

/**
 * @author apanasenko
 */

class IterableJsonSerialization extends Spec with ShouldMatchers {
  describe("Iterable to Json Serialization") {
    val serializer = new JsonSerializer()

    it("simple string list serialization") {
      serializer.toString(List("1")) should equal("['1']")
      serializer.toString(List()) should equal("[]")
    }

    it("string list serialization") {
      val list = List("1", 2)
      serializer.toString(list) should equal("['1',2]")
    }
    
    it("string map[string, string] list serialization") {
      val list = List(Map("test1" -> "value", "test2" -> "value"), "value")
      serializer.toString(list) should equal("[{'test1':'value','test2':'value'},'value']")
    }
  }

  describe("Json to Iterable Deserialization") {
    val deserializer = new JsonDeserializer()

    it("simple json deserialization") {
      deserializer.asObject("[1]") should equal(List(1))
      deserializer.asObject("[1,2]") should equal(List(1,2))
      deserializer.asObject("[1,2,'3']") should equal(List(1,2,"3"))
      deserializer.asObject("[1,2,'3',\"4\"]") should equal(List(1,2,"3","4"))
    }
  }
}
