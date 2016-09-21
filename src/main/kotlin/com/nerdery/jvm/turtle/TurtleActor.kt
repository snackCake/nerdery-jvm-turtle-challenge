package com.nerdery.jvm.turtle

import akka.actor.UntypedActor
import ch.aplu.turtle.Turtle

/**
 * TurtleActor is the abstract base class for all entries in the code challenge. To enter the challenge, create a valid subclass.
 *
 * @author Josh Klun (jklun@nerdery.com)
 */
abstract class TurtleActor : UntypedActor() {

    data class TurtleDrawMessage(val turtle: Turtle)

    override fun onReceive(message: Any?) = when (message) {
        is TurtleDrawMessage -> draw(message.turtle)
        else -> println("Invalid message received by $this")
    }

    /**
     * Override draw to perform turtle commands (e.g. drawing).
     */
    protected abstract fun draw(turtle: Turtle)
}