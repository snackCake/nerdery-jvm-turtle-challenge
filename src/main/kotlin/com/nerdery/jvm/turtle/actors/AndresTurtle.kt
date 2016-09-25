package com.nerdery.jvm.turtle.actors

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor

class AndresTurtle : TurtleActor() {

    override fun draw(turtle: Turtle) {
        //Fractal Snowflake...
        val length  = 250.0
        val depth = 4.0
        turtle.forward(100.0)

        (1 .. 3).forEach {
            side(length,depth,turtle)
            turtle.right(120.0)
        }

    }

    fun side( length : Double,  depth:Double,turtle: Turtle) {
        if (depth <= 0) {
            turtle.forward(length);
        } else {
            side(length/3,depth-1,turtle);
            turtle.left(60.0);

            side(length/3,depth-1,turtle);
            turtle.right(120.0);

            side(length/3,depth-1,turtle);
            turtle.left(60.0);

            side(length/3,depth-1,turtle);

        }
    }
}