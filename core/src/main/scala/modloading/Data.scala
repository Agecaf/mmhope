package com.agecaf.mmhope.modloading

object Data {
  case class ModIndex(
      id: String,
      name: String,
      author: Option[String],
      description: Option[String],
      libs: Option[List[String]],
      levels: Option[List[String]]) {
    val libList: List[String] = libs getOrElse List()
    val levelList: List[String] = levels getOrElse List()
  }
}
