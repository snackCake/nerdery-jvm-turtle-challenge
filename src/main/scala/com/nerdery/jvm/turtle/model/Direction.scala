package com.nerdery.jvm.turtle.model

/**
  * Directions for a Turtle. Turtles treat 0 degrees as Up and the other directions going clockwise.
  * @author Mark Soule on 9/25/16.
  */
case object Direction {
  val Right = 90
  val Left = 270
  val Up = 0
  val Down = 180
}
