package me.apanasenko.json

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import parser.JsonSerializer

/**
 * @author apanasenko
 */

case class Simple(test: String = "value")

class CaseClassJsonSerialization extends Spec with ShouldMatchers {
  describe("CaseClass to Json serialization") {
    val serializer = new JsonSerializer()

    case class InnerClass(test: String = "value")

    it("simple case class serialization") {
      serializer.toString(Simple()) should equal("{'_type':'me.apanasenko.json.Simple','test':'value'}")
      serializer.toString(new Simple()) should equal("{'_type':'me.apanasenko.json.Simple','test':'value'}")
      serializer.toString(Simple("value2")) should equal("{'_type':'me.apanasenko.json.Simple','test':'value2'}")
      serializer.toString(new Simple("value2")) should equal("{'_type':'me.apanasenko.json.Simple','test':'value2'}")
    }

    it("inner case class serialization") {
      serializer.toString(InnerClass()) should equal("{'_type':'%s','test':'value'}".format(classOf[InnerClass].getName))
    }
  }
}
