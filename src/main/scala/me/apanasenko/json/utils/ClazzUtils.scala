package me.apanasenko.json.utils

import java.lang.reflect.Field

/**
 * @author apanasenko
 */

object ClazzUtils {
  def namesValues(any: AnyRef) = if (any.getClass.getInterfaces.contains(classOf[Product]))
    caseNameValues(any.asInstanceOf[Product])
  else
    classNameValues(any)
  
  def classNameValues(any: AnyRef) = classFields(any, numConstructorParams(any)).
    map{field => (field.getName, field.get(any))}
  
  def caseNameValues(product: Product) = {
    classFields(product, product.productArity).map{field => (field.getName, field.get(product))}
  }
  
  def classFields(any: AnyRef, take: Int) = any.getClass.getDeclaredFields.toSeq.
    filterNot(_.isSynthetic).
    take(take).
    map { field =>
      field.setAccessible(true)
      field
    }

  private def numConstructorParams(a: AnyRef) = a.getClass.getConstructors()(0).getParameterTypes.size
}
