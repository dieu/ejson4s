package me.apanasenko.json

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import parser.JsonSerializer

/**
 * @author apanasenko
 */

class IterableJsonSerialization extends Spec with ShouldMatchers {
  describe("Iterable to Json Serialization") {
    val serializer = new JsonSerializer()

    it("simple string list serialization") {
      val list = List("1")
      serializer.toString(list) should equal("['1']")
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
}
