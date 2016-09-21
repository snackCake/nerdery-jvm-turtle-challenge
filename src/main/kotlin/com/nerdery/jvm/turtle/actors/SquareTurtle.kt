package com.nerdery.jvm.turtle.actors

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor

/**
 * An actor that draws squares with the turtle.
 *
 * @author Josh Klun (jklun@nerdery.com)
 */

class SquareTurtle : TurtleActor() {

    private var length = 40.0

    override fun draw(turtle: Turtle) {
        (1 .. 4).forEach { turtle.fd(length).right(90.0) }
        length *= 2
    }

}