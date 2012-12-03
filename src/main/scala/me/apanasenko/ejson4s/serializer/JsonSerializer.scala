package me.apanasenko.ejson4s.serializer

import java.lang.StringBuilder
import collection.{JavaConverters, Map, Iterable}
import me.apanasenko.ejson4s.{TypeHints, Serializer}
import me.apanasenko.ejson4s.hint.FullTypeHints
import me.apanasenko.ejson4s.utils.ClazzUtils

/**
 * @author apanasenko
 */

class JsonSerializer(typeHints: TypeHints = FullTypeHints()) extends Serializer {
  def toString(value: Any) = {
    val builder = new StringBuilder()
    toString(value, builder)
    builder.toString
  }

  def toString(value: Any, out: Appendable) {
    val builder = new StringBuilder()
    toString(value, builder)
    out.append(builder.toString)
  }

  def toString(value: Any, out: StringBuilder) {
    value match {
      case map: java.util.Map[_, _] => map2String(JavaConverters.mapAsScalaMapConverter(map).asScala, out)
      case collection: java.util.Collection[_] => iterable2String(JavaConverters.collectionAsScalaIterableConverter(collection).asScala, out)
      case map: Map[_, _] => map2String(map, out)
      case iter: Iterable[_] => iterable2String(iter, out)
      case str: String => string2String(str, out)
      case number: Number => primitive2String(number, out)
      case any: AnyRef if (isPrimitive(any)) => primitive2String(any, out)
      case Some(any) => toString(any, out)
      case None => primitive2String(typeHints.insteadOfNone, out)
      case null => primitive2String(null, out)
      case other: AnyRef => clazz2String(other, out)
    }
  }

  def iterable2String(value: Iterable[Any], out: StringBuilder) {
    val size = value.size
    out.append("[")
    value.foldLeft(0) {
      write(out, size)
    }
    out.append("]")
  }

  def map2String(value: Map[_, _], out: StringBuilder) {
    val size = value.size
    out.append("{")
    value.foldLeft(0) {
      write(out, size)
    }
    out.append("}")
  }

  def string2String(value: String, out: StringBuilder) {
    out.append("'%s'".format(value))
  }

  def primitive2String(value: AnyRef, out: StringBuilder) {
    if (value != null && value.getClass.isAssignableFrom(classOf[String])) {
      string2String(value.asInstanceOf[String], out)
    } else {
      out.append("%s".format(value))
    }
  }

  def clazz2String(value: AnyRef, out: StringBuilder) {
    val list = (if (typeHints.isHint(value.getClass))
      Seq((typeHints.hint, typeHints.hint(value.getClass)))
    else
      Seq()) ++ ClazzUtils.namesValues(value)
    map2String(list.toMap, out)
  }

  private def write(out: StringBuilder, size: Int): (Int, Any) => Int = {
    case (i, (None, _)) => i + 1
    case (i, (k, v)) => {
      write(k, v, out, size, i)
    }
    case (i, v) => {
      write(v, out, size, i)
    }
  }

  private def write(k: Any, v: Any, out: StringBuilder, size: Int, i: Int): Int = {
    k match {
      case _: Number | _: String => {
        primitive2String(k.asInstanceOf[AnyRef], out)
        out.append(':')
      }
      case _ => throw new IllegalArgumentException("%s not allowed type(`%s) for key in map".format(k, k.getClass))
    }
    write(v, out, size, i)
  }

  private def write(v: Any, out: StringBuilder, size: Int, i: Int): Int = {
    toString(v, out)
    if (size > 1 && size - 1 != i) out.append(",")
    i + 1
  }

  private def isPrimitive(value: AnyRef) = value.getClass.isPrimitive
}
