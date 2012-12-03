package me.apanasenko.ejson4s

import deserializer.JsonDeserializer
import serializer.JsonSerializer
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.util

/**
 * @author Anton Panasenko
 */

class JavaClassJsonSerialization extends Spec with ShouldMatchers {
  val serializer = new JsonSerializer()
  val deserializer = new JsonDeserializer()

  val javaName = classOf[JavaComplex].getName
  val simpleName = classOf[Simple].getName

  val fullyJavaClass = new JavaComplex("name", new util.HashMap[String, AnyRef]() {{
    put("1", "2")
    put("2", Simple())
  }}, new util.ArrayList[Simple]() {{
    add(Simple())
    add(Simple("1"))
  }})

  describe("JavaClass as Json serialization") {
    it("simple serialization") {
      serializer.toString(new JavaComplex()) should equal("{'_type':'%s','name':null,'likes':null,'parameters':null}".format(javaName))
    }

    it("fully serializaiton") {
      serializer.toString(fullyJavaClass) should equal(
        "{'_type':'%s','name':'name','likes':[{'_type':'%s','test':'value'},{'_type':'%s','test':'1'}],'parameters':{'2':{'_type':'%s','test':'value'},'1':'2'}}".format(javaName, simpleName, simpleName, simpleName)
      )
    }
  }

  describe("Json as JavaClass deserialization") {
    it("simple deserializaiton") {
      deserializer.asObject(serializer.toString(new JavaComplex())) should equal(new JavaComplex())
    }

    it("fully deserialization") {
      deserializer.asObject(serializer.toString(fullyJavaClass)) should equal(fullyJavaClass)
    }
  }
}
