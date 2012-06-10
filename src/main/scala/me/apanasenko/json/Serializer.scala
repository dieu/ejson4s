package me.apanasenko.json


/**
 * @author apanasenko
 */

trait Serializer {
  def toString(value: Any): String
  def toString(value: Any, out: Appendable)
}
