package com.nerdery.jvm.turtle.actors

import java.awt.Color

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.TurtleActor
import com.nerdery.jvm.turtle.art.{CellColorGeneration, TurtleActions}
import com.nerdery.jvm.turtle.simulation.CellSimulation
import com.nerdery.jvm.turtle.model.{AlanTurtlePresets, CellSize, SkinSample}

/**
  * This Turtle uses randomly generated data to draw realistic looking animal textures using Alan Turing's
  * Reaction-Diffusion equation first proposed in his 1952 publication "The Chemical Basis of Morphogenesis" to explain
  * how animals like Cheetahs and Zebras get their fur patterns.
  *
  * AlanTurtle will randomly draw spots, splotches, and stripes. Also with default settings the textures won't look very
  * realistic because random colors are chosen. Sometimes AlanTurtle will choose very similar colors so just rerun if
  * you can't see any discernible pattern.
  *
  * README
	*
  * This turtle takes 30-60 seconds to start drawing (depending on randomly generated data). But the really long part is
  * the turtle itself. I haven't found a way to make the turtle draw any faster. So it may take several minutes for the
  * turtle to finish drawing on default settings. If you have spare CPU cycles you might consider setting ChosenCellSize
  * to CellSize.Tiny for some really interesting patterns.
  *
	* The AlanTurtle object contains a Presets which points to an instance of AlanTurtleSettings where settings can be
  * altered. Everything is randomly generated but the bounds of the random generation and some hardcoded values can be
  * provided through the Presets. Feel free to play with the settings and see what kinds of things you can generate.
  *
	* AlanTurtle doesn't share turtles very well. This might be a problem for other turtles as well. AlanTurtle will
  * happily walk the turtle off the edge of the playground and cause an exception. You may want to consider disabling
  * AlanTurtle for the shared turtle run.
  *
  * BACKGROUND
	*
  * Turing's equation looks something of the form:
  *
  *     ∂C/∂t = F(C) + D∇2C
  *
  * Note: It's not clear from what I've read how the equation actually looks.
  *
  * I do not understand the mathematics behind this equation. However, I did find an old Dr. Dobbs article that explains
  * how to solve it using Euler's method for differential equations. I also don't understand Euler's method but I can
  * explain the code here.
  *
  * The cells of the animal's skin will be representing by two matrices representing the concentrations of two
  * chemicals, A and B. For some number of iterations (see Presets) we calculate the diffusion and reaction part of both
  * components A and B for each cell and update the matrices accordingly. After all iterations the matrix representing
  * component A will contain the values we need to color the skin. We interpolate the values into colors and use the
  * turtle to color the cells. The entire playground is treated as the skin sample that AlanTurtle will color.
  *
	* SOURCES
  *
  * http://www.drdobbs.com/cpp/algorithm-alley/184410024?pgno=1
  * http://www.sjsu.edu/faculty/watkins/murray.htm
  * http://www.dna.caltech.edu/courses/cs191/paperscs191/turing.pdf
  *
  * @author Mark Soule on 9/22/16.
  */
class AlanTurtle extends TurtleActor
  with TurtleActions
  with CellSimulation
  with CellColorGeneration {
  import AlanTurtle._

  @scala.throws[Throwable](classOf[Throwable])
  override def onReceive(message: Any): Unit = {
    super.onReceive(message)
  }

  /**
    * Start drawing with the Turtle.
    *
    * @param turtle The Turtle instance that will do the drawing.
    */
  override def draw(turtle: Turtle) = {
    val pixelsTall = turtle.getPlayground.getHeight
    val pixelsWide = turtle.getPlayground.getWidth
    val cellsTall = pixelsTall / ChosenCellSize
    val cellsWide = pixelsWide / ChosenCellSize

    turtle
      .speed(Ludicrous)
      .moveToBottomLeftCorner(pixelsWide, pixelsTall)
      .drawSkinCells(
        processPresetsAndExecuteMorphogenesis(cellsTall, cellsWide, Presets),
        ChosenCellSize,
        pixelsWide
      )
  }

  /**
    * Process Presets, generate random data, and start Turing's morphogenesis equations.
    *
    * @param cellsTall How tall the sample will be in cells.
    * @param cellsWide How wide the sample will be in cells.
    * @param presets AlanTurtlePresets instance containing instructions for random generation.
    * @return A matrix of colors representing the generated sample.
    */
  private def processPresetsAndExecuteMorphogenesis(cellsTall: Int, cellsWide: Int, presets: AlanTurtlePresets): Seq[Seq[Color]] = {
    // Process presents and generate random data as needed.
    val iterations =
      presets
        .simulationIterations
        .getOrElse(
          randomIntWithinBounds(presets.simulationIterationMin, presets.simulationIterationMax)
        )
    val constantA =
      presets
        .constantComponentA
        .getOrElse(
          randomDoubleWithinBounds(presets.constantMin.toInt, presets.constantMax.toInt)
        )
    val constantB =
      presets
        .constantComponentB
        .getOrElse(
          randomDoubleWithinBounds(presets.constantMin.toInt, presets.constantMax.toInt)
        )
    val concentrationBound = presets.initialConcentrationMax
    val minColor = presets.minColor.getOrElse(randomizeColor)
    val maxColor = presets.maxColor.getOrElse(randomizeColor)
    val initialRandomSkinSample = generateInitialSkinSample(cellsTall, cellsWide, concentrationBound)

    // Finished processing so we can start calculating how to color the sample.
    colorCellsByTuringsReactionDiffusion(
      initialRandomSkinSample,
      iterations,
      constantA,
      constantB,
      minColor,
      maxColor
    )
  }

  /**
    * Decide how to color our sample by performing Turing's Reaction-Diffusion equation to our skinSample. The results
    * of performing the equation over the given iterations will tell us how to color the cells.
    *
    * @param skinSample The original SkinSample containing two matrices filled with random data for component A and B.
    * @param iterations The number of iterations to perform on the skinSample.
    * @param constantA Constant for component A to use in the equations.
    * @param constantB Constant for component B to use in the equations.
    * @param minColor One end of the color range for our final sample.
    * @param maxColor The other end of the color range for our final sample.
    * @return A matrix of colors to draw the final product.
    */
  private def colorCellsByTuringsReactionDiffusion(skinSample: SkinSample,
                                                   iterations: Int,
                                                   constantA: Double,
                                                   constantB: Double,
                                                   minColor: Color,
                                                   maxColor: Color
                                                  ): Seq[Seq[Color]] = {
    val availableColors = generateColorGradient(minColor, maxColor)
    val morphedSample = morphogenesisByEulersMethod(skinSample, iterations, constantA, constantB)
    // We use concentrationMatrixA for our colors because concentrationMatrixB tends to converge on one solid color. I
    // don't know the mathematics behind this.
    chooseCellColors(morphedSample.concentrationMatrixA, availableColors)
  }
}

object AlanTurtle {
  val Ludicrous = Double.MaxValue
  val ChosenCellSize = CellSize.Normal

  val Default = AlanTurtlePresets(
    simulationIterationMin = 1000,
    simulationIterationMax = 3000,
    constantMin = 0.01,
    constantMax = 15,
    simulationIterations = None,
    constantComponentA = None,
    constantComponentB = None,
    initialConcentrationMax = None,
    minColor = None,
    maxColor = None
  )

  val Cheetah = AlanTurtlePresets(
    simulationIterationMin = 500,
    simulationIterationMax = 3000,
    constantMin = 0,
    constantMax = 50,
    simulationIterations = Some(2000),
    constantComponentA = Some(3.5),
    constantComponentB = Some(16),
    initialConcentrationMax = Some(1000000),
    minColor = Some(Color.YELLOW),
    maxColor = Some(Color.BLACK)
  )

  val Ferret = AlanTurtlePresets(
    simulationIterationMin = 500,
    simulationIterationMax = 3000,
    constantMin = 0,
    constantMax = 50,
    simulationIterations = Some(2000),
    constantComponentA = Some(4.241),
    constantComponentB = Some(3.2),
    initialConcentrationMax = Some(1000000),
    minColor = Some(Color.WHITE),
    maxColor = Some(new Color(46, 31, 0))
  )

  val InterestingSplotchyPattern = AlanTurtlePresets(
    simulationIterationMin = 500,
    simulationIterationMax = 3000,
    constantMin = 0,
    constantMax = 50,
    simulationIterations = Some(2000),
    constantComponentA = Some(0.241),
    constantComponentB = Some(3.2),
    initialConcentrationMax = Some(1000000),
    minColor = None,
    maxColor = None
  )

  val Presets = InterestingSplotchyPattern
}
