package me.apanasenko.json

import deserializer.JsonDeserializer
import org.scalatest.matchers.ShouldMatchers
import parser.JsonSerializer
import org.scalatest.{Ignore, Spec}

/**
 * @author apanasenko
 */

case class Simple(test: String = "value")

case class ComplexClass(name: String,  
                        url: Option[String] = None, 
                        likes: List[Simple] = List(), 
                        parameters: Map[String, Any] = Map())

class CaseClassJsonSerialization extends Spec with ShouldMatchers {
  val simpleName = classOf[Simple].getName

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

  describe("Json to CaseClass serialization") {
    val deserializer = new JsonDeserializer()
    val serializer = new JsonSerializer()

    case class InnerClass(test: String = "value")

    it("simple case class deserialization") {
      deserializer.asObject("{'_type':'%s','test':'value'}".format(simpleName)) should equal(Simple())
      deserializer.asObject("{'_type':'%s','test':'v'}".format(simpleName)) should equal(Simple(test="v"))
      deserializer.asObject(serializer.toString(Simple())) should equal(Simple())
    }


    ignore("inner case class deserialization") {
      deserializer.asObject(serializer.toString(InnerClass())) should equal(InnerClass())
    }
    
    it("complex case class deserialization") {
      deserializer.asObject(serializer.toString(ComplexClass("test"))) should equal(ComplexClass("test"))
    }
  }
}
