package me.apanasenko.json.hint

import me.apanasenko.json.TypeHints

/**
 * @author apanasenko
 */

case class FullTypeHints() extends TypeHints {
  def hint = "_type"

  def hint(value: Class[_]) = value.getName

  def isHint(clazz: Class[_]) = true

  def insteadOfNone = null
}
