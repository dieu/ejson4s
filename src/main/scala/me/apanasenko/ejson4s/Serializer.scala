package me.apanasenko.ejson4s


/**
 * @author apanasenko
 */

trait Serializer {
  def toString(value: Any): String
  def toString(value: Any, out: Appendable)
}
