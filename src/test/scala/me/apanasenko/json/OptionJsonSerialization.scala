package me.apanasenko.json

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import serializer.JsonSerializer

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

    it("None to null serialization") {
      serializer.toString(None) should equal("null")
      serializer.toString(List(1, None)) should equal("[1,null]")
      serializer.toString(Map(1 -> 1, 2 -> None)) should equal("{1:1,2:null}")
    }
  }
}
