package me.apanasenko.ejson4s

import deserializer.JsonDeserializer
import org.scalatest.matchers.ShouldMatchers
import serializer.JsonSerializer
import org.scalatest.Spec

/**
 * @author apanasenko
 */

case class Simple(test: String = "value")

case class ComplexClass(name: String,  
                        url: Option[String] = None, 
                        likes: List[Simple] = List(), 
                        parameters: Map[String, Any] = Map())

case class StrongClass(name: String, complex: ComplexClass = ComplexClass("test"))

class CaseClassJsonSerialization extends Spec with ShouldMatchers {
  val simpleName = classOf[Simple].getName
  val complexName = classOf[ComplexClass].getName
  val strongName = classOf[StrongClass].getName

  describe("CaseClass to Json serialization") {
    val serializer = new JsonSerializer()

    case class InnerClass(test: String = "value")

    it("simple case class serialization") {
      serializer.toString(Simple()) should equal("{'_type':'%s','test':'value'}".format(simpleName))
      serializer.toString(new Simple()) should equal("{'_type':'%s','test':'value'}".format(simpleName))
      serializer.toString(Simple("value2")) should equal("{'_type':'%s','test':'value2'}".format(simpleName))
      serializer.toString(new Simple("value2")) should equal("{'_type':'%s','test':'value2'}".format(simpleName))
    }

    it("inner case class serialization") {
      serializer.toString(InnerClass()) should equal("{'_type':'%s','test':'value'}".format(classOf[InnerClass].getName))
    }

    it("complex case class serialization") {
      serializer.toString(ComplexClass("test")) should equal("{'_type':'%s','name':'test','url':null,'likes':[],'parameters':{}}".format(complexName))
    }
    
    it("strong case class serialization") {
      serializer.toString(StrongClass("test")) should equal("{'_type':'%s','name':'test','complex':{'_type':'%s','name':'test','url':null,'likes':[],'parameters':{}}}".format(strongName, complexName))
    }
  }

  describe("Json to CaseClass serialization") {
    val deserializer = new JsonDeserializer()
    val serializer = new JsonSerializer()

    val fullyComplex = ComplexClass("test", Some("1"), List(Simple(), Simple()), Map("1" -> Simple(), "2" -> List("1", "2")))
    val fullyComplexWithNone = ComplexClass("test", None, List(Simple(), Simple()), Map("1" -> Simple(), "2" -> List("1", "2")))

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

    it("fully complex case class deserialization") {
      deserializer.asObject(serializer.toString(fullyComplex)) should equal(fullyComplex)
    }

    it("strong case class deserialization") {
      deserializer.asObject(serializer.toString(StrongClass("test"))) should equal(StrongClass("test"))
    }
  }
}
