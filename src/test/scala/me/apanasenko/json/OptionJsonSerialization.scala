package me.apanasenko.json

import hint.FullTypeHints
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import parser.JsonSerializer

/**
 * @author apanasenko
 */

class OptionJsonSerialization extends Spec with ShouldMatchers {
  describe("Option to Json Serialization") {
    val serializer = new JsonSerializer()

    it("Some serialization") {
      serializer.toString(Some(1)) should equal("1")
      serializer.toString(Some("1")) should equal("'1'")
      serializer.toString(List(1, Some(2))) should equal("[1,2]")
    }
    
    it("None ignore serialization") {
      val serializer = new JsonSerializer(typeHints = new FullTypeHints(){
        override def isIgnoreNone = true
      })
      serializer.toString(None) should equal("")
      serializer.toString(List(None, 1)) should equal("[1]")
      serializer.toString(List(1, None)) should equal("[1]")
      serializer.toString(Map(1 -> 1, 2 -> None)) should equal("{1:1}")
      serializer.toString(Map(1 -> 1, 2 -> 2, 3 -> None)) should equal("{1:1,2:2}")
      serializer.toString(Map(1 -> 1, 2 -> None, 3 -> 3)) should equal("{1:1,3:3}")
      serializer.toString(Map(None -> 1)) should equal("{}")
    }
    
    it("None to null serialization") {
      serializer.toString(None) should equal("null")
      serializer.toString(List(1, None)) should equal("[1,null]")
      serializer.toString(Map(1 -> 1, 2 -> None)) should equal("{1:1,2:null}")
    }
  }
}
