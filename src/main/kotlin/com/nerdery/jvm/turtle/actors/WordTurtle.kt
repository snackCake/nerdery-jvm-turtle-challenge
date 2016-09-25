package com.nerdery.jvm.turtle.actors

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor
import java.awt.Color

val displayUnitMultiplier = 20.0

// Josh if you give me the victory I'll get you tacos
// I'll also throw in this amazing chile powder candy that a QA guy showed me from the general store right next to the taco place

class WordTurtle : TurtleActor() {
    val words = listOf(something, awesome)

    override fun draw(turtle: Turtle) {
        turtle.setX(-500.0)
        turtle.setY(200.0)
        turtle.color = Color.RED


        words.forEach {

            // Store the offset for new lines
            var totalOffset: Int = 1

            // Draw a word
            it.forEach {
                // Draw the letter
                turtle.drawLetter(it)

                // Set the position of the turtle to get ready for the next letter
                turtle.penUp()
                turtle.setHeading(Direction.RIGHT.heading)
                val positionOffset = it.width + 1
                totalOffset += positionOffset
                turtle.forward(positionOffset * displayUnitMultiplier)
                turtle.penDown()
            }

            // Go to new line
            // We could teleport there but that would look lame af
            turtle.penUp()
            turtle.setHeading(Direction.DOWN.heading)
            turtle.forward(6 * displayUnitMultiplier)

            turtle.setHeading(Direction.LEFT.heading)
            turtle.back(totalOffset * displayUnitMultiplier)
            turtle.penDown()
        }
    }
}

fun Turtle.drawLetter(letter: Letter) {
    letter.path.forEach {
        this.setHeading(it.first.heading)
        when (it.first) {
            Direction.LEFT -> this.back(it.second * displayUnitMultiplier) // Couldn't figure out how to get the turtle to face left ;_;
            else -> this.forward(it.second * displayUnitMultiplier)
        }
    }
}

enum class Direction(val heading: Double) { UP(0.0), DOWN(180.0), LEFT(90.0), RIGHT(90.0) }

/**
 * Letters should always end being drawn at the same point that they started.
 * The width could probably be calculated instead of stated. They are always 5 units tall.
 */
enum class Letter(val width: Int, val path: Collection<Pair<Direction, Double>>) {

    A(3, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 1.0,
            Direction.UP to 2.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 2.0,
            Direction.LEFT to 1.0)),

    E(3, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 1.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 1.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 3.0)),

    G(4, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 4.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 3.0,
            Direction.DOWN to 3.0,
            Direction.RIGHT to 2.0,
            Direction.UP to 1.0,
            Direction.LEFT to 1.0,
            Direction.UP to 1.0,
            Direction.RIGHT to 2.0,
            Direction.DOWN to 3.0,
            Direction.LEFT to 4.0)),

    H(3, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 2.0,
            Direction.RIGHT to 1.0,
            Direction.UP to 2.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 1.0,
            Direction.UP to 2.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 2.0,
            Direction.LEFT to 1.0)),

    I(3, listOf(
            Direction.UP to 1.0,
            Direction.RIGHT to 1.0,
            Direction.UP to 3.0,
            Direction.LEFT to 1.0,
            Direction.UP to 1.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 3.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 3.0)),

    M(5, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 5.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 1.0,
            Direction.UP to 2.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.UP to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 2.0,
            Direction.LEFT to 1.0)),

    // Wow these are getting pretty boring to type out
    // Watch some nerd draw a beautiful fractal pattern in like 5 lines and wreck me

    N(3, listOf( // ah shit this is just like A
            Direction.UP to 5.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 1.0,
            Direction.UP to 2.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 2.0,
            Direction.LEFT to 1.0)),

    O(3, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 3.0)),

    S(3, listOf(
            Direction.UP to 1.0,
            Direction.RIGHT to 2.0,
            Direction.UP to 1.0,
            Direction.LEFT to 2.0,
            Direction.UP to 3.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 1.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 3.0,
            Direction.LEFT to 3.0)),

    T(2, listOf( // This one is a little gross because there is no content in the lower left
            Direction.RIGHT to 1.0,
            Direction.UP to 4.0,
            Direction.LEFT to 1.0,
            Direction.UP to 1.0,
            Direction.RIGHT to 3.0,
            Direction.DOWN to 1.0,
            Direction.LEFT to 1.0,
            Direction.DOWN to 4.0,
            Direction.LEFT to 1.0)),

    W(5, listOf(
            Direction.UP to 5.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 2.0,
            Direction.RIGHT to 1.0,
            Direction.UP to 1.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 1.0,
            Direction.RIGHT to 1.0,
            Direction.UP to 2.0,
            Direction.RIGHT to 1.0,
            Direction.DOWN to 5.0,
            Direction.LEFT to 5.0));
    //jeez
}

val something = listOf(
        Letter.S,
        Letter.O,
        Letter.M,
        Letter.E,
        Letter.T,
        Letter.H,
        Letter.I,
        Letter.N,
        Letter.G)

val awesome = listOf(
        Letter.A,
        Letter.W,
        Letter.E,
        Letter.S,
        Letter.O,
        Letter.M,
        Letter.E)



