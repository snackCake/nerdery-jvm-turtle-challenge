package com.nerdery.jvm.turtle.actors

import java.awt.Color

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor

/**
  * Not a submission, just a Scala template. Draws a pretty pink line.
  *
  * @author Mark Soule on 9/21/16.
  */
class DummyTurtle extends TurtleActor {

  override def draw(turtle: Turtle) = {
    turtle.setPenColor(Color.PINK).fd(42)
  }

  @scala.throws[Throwable](classOf[Throwable])
  override def onReceive(message: Any): Unit = {
    super.onReceive(message)
  }
}
