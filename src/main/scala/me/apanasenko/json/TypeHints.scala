package me.apanasenko.json

/**
 * @author apanasenko
 */

trait TypeHints {
  def insteadOfNone: AnyRef
  def isIgnoreNone: Boolean
  def isHint(clazz: Class[_]): Boolean
  def hint: String
  def hint(value: Class[_]): String
}
