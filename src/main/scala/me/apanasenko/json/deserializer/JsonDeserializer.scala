package me.apanasenko.json.deserializer

import util.parsing.json._
import collection.{JavaConverters, Map, Iterable}
import me.apanasenko.json.hint.FullTypeHints
import me.apanasenko.json.{TypeHints, Deserializer}
import me.apanasenko.json.transform.Transformer.as

/**
 * @author apanasenko
 */

class JsonDeserializer(typeHints: TypeHints = FullTypeHints(), classLoader: ClassLoader = getClass.getClassLoader)
  extends Deserializer {

  val myConversionFunc = {
    input: String =>
      input.toInt
  }

  def asObject(str: String) = {
    val json = normalize(str)

    parse(json) match {
      case Some(x) => asObject(x)
      case None => throw new IllegalArgumentException("Json is invalid")
    }
  }

  private def normalize(json: String) = json.replaceAll("'", "\"")

  private def parse(json: String): Option[Any] = {
    JSON.perThreadNumberParser = myConversionFunc
    JSON.parseFull(json)
  }

  private def asObject(any: Any): Any = any match {
    case map: Map[_, _] => asObject(map.asInstanceOf[Map[String, _]])
    case list: Iterable[_] => list.map(asObject(_))
    case str: String => str
    case number: Number => number
  }

  private def asObject(map: Map[String, _]): Any = {
    if (map.contains(typeHints.hint)) {
      val clazz = getClass(map)

      val typeArgs = (map - typeHints.hint).values

      val candidates = bestConstructors(clazz, typeArgs)

      if (candidates.length == 0) throw new IllegalArgumentException("Not found constructor for %s with %s type arguments".format(clazz, typeArgs))

      val (someArgs, nonArgs) = candidates.partition(_._1.getParameterTypes.nonEmpty)

      if (nonArgs.length != 0) {
        val self = clazz.newInstance()
        for ((k, v) <- map if (k != typeHints.hint)) {
          val field = clazz.getDeclaredField(k)
          val value = transform(v, field.getType).getOrElse(throw new IllegalArgumentException("Don't cast %s as %s".format(v, field.getType)))
          field.setAccessible(true)
          field.set(self, value)
        }
        self
      } else {
        val args = someArgs.head._2.flatMap(_._2).toSeq
        val cons = someArgs.head._1

        cons.newInstance(args: _*)
      }
    } else {
      map.mapValues(asObject(_))
    }
  }

  private def transform(x: Any, to: Class[_]): Option[AnyRef] = {
    try {
      if (to.isAssignableFrom(classOf[Option[_]])) {
        x match {
          case null if (typeHints.insteadOfNone == null) => Option(None)
          case null => Option(Some(null))
          case _ => Option(Option(asObject(x)))
        }
      } else {
        x match {
          case null => Some(null)
          case _ => Option(as(to, asObject(x)))
        }
      }
    } catch {
      case _: Exception => None
    }
  }

  private def getClass(map: Map[String, _]) = {
    map.get(typeHints.hint).
      filter(_.isInstanceOf[String]).
      map(_.asInstanceOf[String]).
      orElse(throw new IllegalArgumentException("_type %s is not string".format(map(typeHints.hint)))).
      map(classLoader.loadClass(_)).
      get
  }

  private def bestConstructors(clazz: Class[_], typeArgs: Iterable[_]) = {
    clazz.getDeclaredConstructors.map(cons => {
      val args = scala.collection.mutable.ArrayBuffer.empty[Any] ++ typeArgs
      (cons, cons.getParameterTypes.map(parameter => {
        val isTransform = (x: (Any, Int)) => transform(x._1, parameter).isDefined
        (parameter, args.
          zipWithIndex.
          find(isTransform).
          flatMap(x => { args.remove(x._2); transform(x._1, parameter) }))
      }))
    }).filter(x => x._2.filterNot(_._2.isDefined).isEmpty)
  }
}
