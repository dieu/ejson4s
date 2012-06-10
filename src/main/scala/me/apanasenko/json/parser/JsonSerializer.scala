package me.apanasenko.json.parser

import java.lang.StringBuilder
import scala.collection.Map
import scala.collection.Iterable
import me.apanasenko.json.{TypeHints, Serializer}
import me.apanasenko.json.hint.FullTypeHints
import me.apanasenko.json.utils.ClazzUtils

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
      case map: Map[Any, Any] => map2String(map, out)
      case iter: Iterable[Any] => iterable2String(iter, out)
      case str: String => string2String(str, out)
      case number: Number => primitive2String(number, out)
      case any: AnyRef if (isPrimitive(any)) => primitive2String(any, out)
      case Some(any) => toString(any, out)
      case None => if (!typeHints.isIgnoreNone) primitive2String(typeHints.insteadOfNone, out)
      case other: AnyRef => clazz2String(other, out)
    }
  }

  def iterable2String(value: Iterable[Any], out: StringBuilder) {
    val size = value.size
    out.append("[")
    value.foldLeft(0) {
      case (i, None) if (typeHints.isIgnoreNone && i == size-1) => out.deleteCharAt(out.length()-1); i + 1
      case (i, None) if (typeHints.isIgnoreNone) => i + 1
      case (i, v) => {
        write(v, out, size, i)
      }
    }
    out.append("]")
  }

  def map2String(value: Map[Any, Any], out: StringBuilder) {
    val size = value.size
    out.append("{")
    value.foldLeft(0) {
      case (i, (None, _)) => i + 1
      case (i, (_, None)) if (typeHints.isIgnoreNone && i == size-1) => out.deleteCharAt(out.length()-1); i + 1 
      case (i, (_, None)) if (typeHints.isIgnoreNone) => i + 1
      case (i, (k, v)) => {
        write(k, v, out, size, i)
      }
    }
    out.append("}")
  }

  def string2String(value: String, out: StringBuilder) {
    out.append("'%s'".format(value))
  }

  def primitive2String(value: AnyRef, out: StringBuilder) {
    out.append("%s".format(value))
  }

  def clazz2String(value: AnyRef, out: StringBuilder) {
    val list = (if (typeHints.isHint(value.getClass))
      Seq((typeHints.hint, typeHints.hint(value.getClass)))
    else
      Seq()) ++ ClazzUtils.namesValues(value)
    map2String(list.toMap, out)
  }

  private def write(k: Any, v: Any, out: StringBuilder, size: Int, i: Int): Int = {
    k match {
      case n: Number => {
        primitive2String(n, out)
        out.append(':')
      }
      case s: String => {
        string2String(s, out)
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
