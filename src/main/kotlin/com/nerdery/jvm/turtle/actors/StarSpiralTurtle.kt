package com.nerdery.jvm.turtle.actors

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor
import java.awt.Color

/**
 * An actor that draws a star spiral with the turtle.
 *
 * @author Josh Klun (jklun@nerdery.com)
 */

class StarSpiralTurtle : TurtleActor() {

    var runs = 0
    override fun draw(turtle: Turtle) {
        val iterations = 20
        val sizeInc = 10.0
        turtle.penUp().forward(runs * iterations * sizeInc).penDown()
        turtle.setPenColor(Color.YELLOW)
        (1 .. iterations).forEach {
            turtle.forward(it * sizeInc).right(144.0)
        }
        runs++
    }
}