package me.apanasenko.json

/**
 * @author apanasenko
 */

trait Deserializer {
  def asObject(str: String): Any
}
