package me.apanasenko.ejson4s

/**
 * @author apanasenko
 */

trait TypeHints {
  def insteadOfNone: AnyRef
  def isHint(clazz: Class[_]): Boolean
  def hint: String
  def hint(value: Class[_]): String
}
