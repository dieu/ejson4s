package me.apanasenko.ejson4s.hint

import me.apanasenko.ejson4s.TypeHints

/**
 * @author apanasenko
 */

case class FullTypeHints() extends TypeHints {
  def hint = "_type"

  def hint(value: Class[_]) = value.getName

  def isHint(clazz: Class[_]) = true

  def insteadOfNone: AnyRef = null
}
