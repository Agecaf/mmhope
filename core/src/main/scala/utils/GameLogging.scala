package com.agecaf.mmhope.utils

import java.text.SimpleDateFormat
import java.util.Calendar

/**
  * Used for logging throughout the game. Produces logs which can be read in-game and in mmhope.log.
  */
trait GameLogging {
  import GameLogger._

  // Logging methods.
  def info(msg: String) = {
    GameLogger.info.add(Message(msg))
    GameLogger.debug.add(Message(msg))
  }
  def infoStart(key: String) = {
    GameLogger.info.add(Start(key))
    GameLogger.debug.add(Start(key))
  }
  def infoEnd(key: String) = {
    GameLogger.info.add(End(key))
    GameLogger.debug.add(End(key))
  }
  def error(msg: String) = {
    GameLogger.info.add(Error(msg))
    GameLogger.debug.add(Error(msg))
  }
  def error(t: Throwable) = {
    GameLogger.info.add(Error(t))
    GameLogger.debug.add(Error(t))
  }
  def debug(msg: String) = GameLogger.debug.add(Message(msg))
  def debugStart(key: String) = GameLogger.debug.add(Start(key))
  def debugEnd(key: String) = GameLogger.debug.add(End(key))
}

class GameLogger {
  import GameLogger._
  var log: List[Loggable] = List()

  /**
    * Adds a loggable to the log.
    *
    * Note: The current implementation has Ends closing all open Starts with their key.
    * @return
    */
  def add: Loggable => Unit = {
    case end: End => log = log map {
      case Start(k2, t2, None) if k2 == end.key => Start(k2, t2, Some(end))
      case l: Loggable => l
    }
    case l: Loggable => log = l :: log
  }

  override def toString: String = log mkString "\n"
}

object GameLogger {
  // Global loggers.
  val info: GameLogger = new GameLogger()
  val debug: GameLogger = new GameLogger()

  // Loggable cases.
  sealed abstract class Loggable()
  case class Message(msg: String, at: String) extends Loggable {
    override val toString: String = s"[$at] $msg"
  }
  case class Error(t: Throwable, at: String) extends Loggable {
    override val toString: String = s"[$at] [ERROR] $t"
  }
  case class Start(key: String, at: String, end: Option[End]) extends Loggable {
    override val toString: String =
      if (end.isEmpty) s"[$at] Started $key..." else s"[$at] [${end.get.at}] Done $key."
  }
  case class End(key: String, at: String) extends Loggable

  // Additional constructors.
  object Message {
    def apply(msg: String): Message = Message(msg, now)
  }
  object Error {
    def apply(t: Throwable): Error = Error(t, now)
    def apply(msg: String): Error = Error(new Throwable(msg), now)
  }
  object Start {
    def apply(key: String): Start = Start(key, now, None)
  }
  object End {
    def apply(key: String): End = End(key, now)
  }

  // Helper functions.
  val timeFormat = new SimpleDateFormat("hh:mm:ss:SSSS")
  def now: String =
    timeFormat format Calendar.getInstance.getTime

}