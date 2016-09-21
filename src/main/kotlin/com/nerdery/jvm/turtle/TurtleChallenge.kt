package com.nerdery.jvm.turtle

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import ch.aplu.turtle.Turtle
import ch.aplu.turtle.TurtleFrame
import org.reflections.Reflections
import java.awt.Color
import java.lang.reflect.Modifier
import java.security.SecureRandom

/**
 * TurtleChallenge is the main entry into the Turtle Drawing challenge.
 *
 * @author Josh Klun (jklun@nerdery.com)
 */
object TurtleChallenge {

    fun runTurtles() {
        val system = ActorSystem.create("TurtleSystem")

        runSharedTurtle(buildTurtleActors(system, "shared"))
        runIndependentTurtles(buildTurtleActors(system))
    }

    private fun buildTurtleActors(system: ActorSystem, namePrefix: String = ""): List<ActorRef> {
        return findTurtleActorClasses("com.nerdery.jvm.turtle").map { clazz: Class<out TurtleActor> ->
            system.actorOf(Props.create(clazz), namePrefix + clazz.simpleName)
        }
    }

    private fun runSharedTurtle(turtleActors: List<ActorRef>) {
        val frame = TurtleFrame("Turtle Challenge", 1024, 768)
        val mainTurtle = Turtle(frame)
        mainTurtle.color = Color.RED
        turtleActors.forEach {
            it.tell(TurtleActor.TurtleDrawMessage(mainTurtle), ActorRef.noSender())
        }
    }

    private fun runIndependentTurtles(turtleActors: List<ActorRef>) {
        val sharedFrame = TurtleFrame("Shared Turtle Challenge", 1024, 768)
        val random = SecureRandom()
        turtleActors.forEach {
            val turtle = Turtle(sharedFrame)
            turtle.color = Color(random.nextFloat(), random.nextFloat(), random.nextFloat())
            it.tell(TurtleActor.TurtleDrawMessage(turtle), ActorRef.noSender())
        }
    }

    private fun findTurtleActorClasses(turtlePackage: String): List<Class<out TurtleActor>> {
        val entrantFinder = Reflections(turtlePackage)
        val subTypes: MutableSet<Class<out TurtleActor>> = entrantFinder.getSubTypesOf(TurtleActor::class.java)
        return subTypes.filter { !(it.isInterface || Modifier.isAbstract(it.modifiers)) }
    }
}

fun main(args: Array<String>) {
    TurtleChallenge.runTurtles()
}
