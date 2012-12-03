package me.apanasenko.ejson4s

/**
 * @author apanasenko
 */

trait Deserializer {
  def asObject(str: String): Any
}
