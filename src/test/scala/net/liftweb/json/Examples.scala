/*
 * Copyright 2009-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftweb
package json

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import me.apanasenko.ejson4s.serializer.JsonSerializer
import me.apanasenko.ejson4s.deserializer.JsonDeserializer

class Examples extends Spec with ShouldMatchers {
  val serializer = new JsonSerializer()
  val deserializer = new JsonDeserializer()

  it("Lotto example") {
    val json = deserializer.asObject(lotto)
    json should equal(lottoMap)
  }

  it("Person example") {
    val json = deserializer.asObject(person)
    json should equal(personMap)
    serializer.toString(personMap("person")("spouse")) should equal("""{'person':{'name':'Marilyn','age':33}}""")
  }

  it("Transformation example") {
//    val uppercased = parse(person).transform { case JField(n, v) => JField(n.toUpperCase, v) }
//    val rendered = compact(render(uppercased))
//    rendered mustEqual
//      """{"PERSON":{"NAME":"Joe","AGE":35,"SPOUSE":{"PERSON":{"NAME":"Marilyn","AGE":33}}}}"""
  }

  it("Remove example") {
//    val json = parse(person) remove { _ == JField("name", "Marilyn") }
//    compact(render(json \\ "name")) mustEqual """{"name":"Joe"}"""
  }

  it("Queries on person example") {
//    val json = parse(person)
//    val filtered = json filter {
//      case JField("name", _) => true
//      case _ => false
//    }
//    filtered mustEqual List(JField("name", JString("Joe")), JField("name", JString("Marilyn")))
//
//    val found = json find {
//      case JField("name", _) => true
//      case _ => false
//    }
//    found mustEqual Some(JField("name", JString("Joe")))
  }

//  "Object array example" in {
//    val json = parse(objArray)
//    compact(render(json \ "children" \ "name")) mustEqual """["name":"Mary","name":"Mazy"]"""
//    compact(render((json \ "children")(0) \ "name")) mustEqual "\"Mary\""
//    compact(render((json \ "children")(1) \ "name")) mustEqual "\"Mazy\""
//    (for { JField("name", JString(y)) <- json } yield y) mustEqual List("joe", "Mary", "Mazy")
//  }
//
//  "Unbox values using XPath-like type expression" in {
//    parse(objArray) \ "children" \\ classOf[JInt] mustEqual List(5, 3)
//    parse(lotto) \ "lotto" \ "winning-numbers" \ classOf[JInt] mustEqual List(2, 45, 34, 23, 7, 5, 3)
//    parse(lotto) \\ "winning-numbers" \ classOf[JInt] mustEqual List(2, 45, 34, 23, 7, 5, 3)
//  }
//
//  "Quoted example" in {
//    val json = parse(quoted)
//    List("foo \" \n \t \r bar") mustEqual json.values
//  }
//
//  "Null example" in {
//    compact(render(parse(""" {"name": null} """))) mustEqual """{"name":null}"""
//  }
//
//  "Null rendering example" in {
//    compact(render(nulls)) mustEqual """{"f1":null,"f2":[null,"s"]}"""
//  }
//
//  "Symbol example" in {
//    compact(render(symbols)) mustEqual """{"f1":"foo","f2":"bar"}"""
//  }
//
//  "Unicode example" in {
//    parse("[\" \\u00e4\\u00e4li\\u00f6t\"]") mustEqual JArray(List(JString(" \u00e4\u00e4li\u00f6t")))
//  }
//
//  "Exponent example" in {
//    parse("""{"num": 2e5 }""") mustEqual JObject(List(JField("num", JDouble(200000.0))))
//    parse("""{"num": -2E5 }""") mustEqual JObject(List(JField("num", JDouble(-200000.0))))
//    parse("""{"num": 2.5e5 }""") mustEqual JObject(List(JField("num", JDouble(250000.0))))
//    parse("""{"num": 2.5e-5 }""") mustEqual JObject(List(JField("num", JDouble(2.5e-5))))
//  }
//
//  "JSON building example" in {
//    val json = concat(JField("name", JString("joe")), JField("age", JInt(34))) ++ concat(JField("name", JString("mazy")), JField("age", JInt(31)))
//    compact(render(json)) mustEqual """[{"name":"joe","age":34},{"name":"mazy","age":31}]"""
//  }
//
//  "JSON building with implicit primitive conversions example" in {
//    import Implicits._
//    val json = concat(JField("name", "joe"), JField("age", 34)) ++ concat(JField("name", "mazy"), JField("age", 31))
//    compact(render(json)) mustEqual """[{"name":"joe","age":34},{"name":"mazy","age":31}]"""
//  }
//
//  "Example which collects all integers and forms a new JSON" in {
//    val json = parse(person)
//    val ints = json.fold(JNothing: JValue) { (a, v) => v match {
//      case x: JInt => a ++ x
//      case _ => a
//    }}
//    compact(render(ints)) mustEqual """[35,33]"""
//  }

  val lottoMap = Map("lotto" ->
    Map("lotto-id" -> 5,
      "winning-numbers" -> List(2, 45, 34, 23, 7, 5, 3),
      "winners" -> List(Map("winner-id" -> 23, "numbers" -> List(2, 45, 34, 23, 3, 5)),
                        Map("winner-id" -> 54, "numbers" -> List(52, 3, 12, 11, 18, 22)))))


  val lotto = """
{
  "lotto":{
    "lotto-id":5,
    "winning-numbers":[2,45,34,23,7,5,3],
    "winners":[ {
      "winner-id":23,
      "numbers":[2,45,34,23,3, 5]
    },{
      "winner-id" : 54 ,
      "numbers":[ 52,3, 12,11,18,22 ]
    }]
  }
}
"""

  val personMap =
    Map("person" ->
      Map("name" -> "Joe",
        "age" -> 35,
        "spouse" ->
          Map("person" ->
            Map("name" -> "Marilyn",
              "age" -> 33)
            )
          )
      )

  val person = """
{ 
  "person": {
    "name": "Joe",
    "age": 35,
    "spouse": {
      "person": {
        "name": "Marilyn",
        "age": 33
      }
    }
  }
}
"""

  val objArray =
    """
    { "name": "joe",
      "address": {
        "street": "Bulevard",
        "city": "Helsinki"
      },
      "children": [
        {
          "name": "Mary",
          "age": 5
        },
        {
          "name": "Mazy",
          "age": 3
        }
      ]
    }
    """

//  val nulls = ("f1" -> null) ~ ("f2" -> List(null, "s"))
//  val quoted = """["foo \" \n \t \r bar"]"""
//  val symbols = ("f1" -> 'foo) ~ ("f2" -> 'bar)
}