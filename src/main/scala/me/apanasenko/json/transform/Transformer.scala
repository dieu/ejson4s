package me.apanasenko.json.transform

import scala.collection.mutable.{Map => MMap, Set => MSet, Seq => MSeq, IndexedSeq => MIndexedSeq}
import scala.collection.mutable.{Buffer, ArrayBuffer, LinkedList, DoubleLinkedList}
import scala.collection.immutable.{List => IList, Map => IMap, Set => ISet, Seq => ISeq, IndexedSeq => IIndexedSeq}
import java.util
import collection.JavaConverters

/**
 * @author Anton Panasenko
 */

object Transformer {
  def as(name: String, real: collection.Iterable[_]): AnyRef = name match {

    case ScalaCollectionClasses.BufferClass => Buffer.empty ++ real
    case ScalaCollectionClasses.ArrayBufferClass => ArrayBuffer.empty ++ real

    case ScalaCollectionClasses.SeqClass => Seq.empty ++ real
    case ScalaCollectionClasses.ISeqClass => ISeq.empty ++ real
    case ScalaCollectionClasses.MSeqClass => MSeq.empty ++ real

    case ScalaCollectionClasses.IListClass => IList.empty ++ real

    case ScalaCollectionClasses.SetClass => Set.empty ++ real
    case ScalaCollectionClasses.ISetClass => ISet.empty ++ real
    case ScalaCollectionClasses.MSetClass => MSet.empty ++ real

    case ScalaCollectionClasses.ISetClass => ISet.empty ++ real
    case ScalaCollectionClasses.MSetClass => MSet.empty ++ real

    case ScalaCollectionClasses.VectorClass => Vector.empty ++ real

    case ScalaCollectionClasses.IndexedSeq => IndexedSeq.empty ++ real
    case ScalaCollectionClasses.IIndexedSeq => IIndexedSeq.empty ++ real
    case ScalaCollectionClasses.MIndexedSeq => MIndexedSeq.empty ++ real

    case ScalaCollectionClasses.LinkedList => LinkedList.empty ++ real
    case ScalaCollectionClasses.DoubleLinkedList => DoubleLinkedList.empty ++ real

    case JavaCollectionsClasses.ListClass => new util.ArrayList(JavaConverters.asJavaCollectionConverter(real).asJavaCollection)
    case JavaCollectionsClasses.ArrayList => new util.ArrayList(JavaConverters.asJavaCollectionConverter(real).asJavaCollection)

    case x => throw new IllegalArgumentException("failed as find proper Traversable[_] impl for %s".format(x))
  }

  def as(t: Class[_], real: scala.collection.Iterable[_]): AnyRef =
    t.getName match {
      case "scala.package.Seq" => as(ScalaCollectionClasses.SeqClass, real)
      case "scala.package.List" => as(ScalaCollectionClasses.IListClass, real)
      case "scala.package.Vector" => as(ScalaCollectionClasses.VectorClass, real)
      case "scala.package.IndexedSeq" => as(ScalaCollectionClasses.IndexedSeq, real)
      case "scala.Predef.Set" => as(ScalaCollectionClasses.SetClass, real)
      case x => as(x, real)
    }

  def as[K, V](name: String, real: scala.collection.Map[K, V]): AnyRef = name match {
    case ScalaCollectionClasses.MapClass => Map.empty ++ real
    case ScalaCollectionClasses.IMapClass => IMap.empty ++ real
    case ScalaCollectionClasses.MMapClass => MMap.empty ++ real

    case JavaCollectionsClasses.MapClass => new util.HashMap[K, V](JavaConverters.mapAsJavaMapConverter(real).asJava)
    case JavaCollectionsClasses.HashMap => new util.HashMap[K, V](JavaConverters.mapAsJavaMapConverter(real).asJava)

    case x => throw new IllegalArgumentException("failed as find proper Map[_,_] impl for %s".format(x))
  }

  def as(t: Class[_], real: collection.Map[_, _]): AnyRef =
    t.getName match {
      case "scala.Predef.Map" => as(ScalaCollectionClasses.IMapClass, real)
      case x => as(x, real)
    }

  def as(t: Class[_], real: String): String = real

  def as(t: Class[_], real: Number): Number = real

  def as(t: Class[_], real: Any): AnyRef = real match {
    case map: collection.Map[_, _] => as(t, map)
    case list: collection.Iterable[_] => as(t, list)
    case str: String => as(t, str)
    case number: Number => as(t, number)
    case _ => real.asInstanceOf[AnyRef]
  }
}

object ScalaCollectionClasses {

  val IListClass = classOf[IList[_]].getName

  val SeqClass = classOf[scala.collection.Seq[_]].getName
  val ISeqClass = classOf[ISeq[_]].getName
  val MSeqClass = classOf[MSeq[_]].getName

  val BufferClass = classOf[Buffer[_]].getName
  val ArrayBufferClass = classOf[ArrayBuffer[_]].getName

  val MapClass = classOf[scala.collection.Map[_, _]].getName
  val IMapClass = classOf[IMap[_, _]].getName
  val MMapClass = classOf[MMap[_, _]].getName

  val SetClass = classOf[scala.collection.Set[_]].getName
  val ISetClass = classOf[ISet[_]].getName
  val MSetClass = classOf[MSet[_]].getName

  val VectorClass = classOf[scala.collection.immutable.Vector[_]].getName

  val IndexedSeq = classOf[scala.collection.IndexedSeq[_]].getName
  val IIndexedSeq = classOf[IIndexedSeq[_]].getName
  val MIndexedSeq = classOf[MIndexedSeq[_]].getName

  val LinkedList = classOf[LinkedList[_]].getName
  val DoubleLinkedList = classOf[DoubleLinkedList[_]].getName
}

object JavaCollectionsClasses {
  val ListClass = classOf[util.List[_]].getName
  val ArrayList = classOf[util.ArrayList[_]].getName

  val MapClass = classOf[util.Map[_, _]].getName
  val HashMap = classOf[util.HashMap[_, _]].getName
}
