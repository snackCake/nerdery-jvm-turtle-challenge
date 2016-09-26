package com.nerdery.jvm.turtle.model

import java.awt.Color

/**
  * Configuration object containing bounds for randomly generated components and optional set values.
  *
  * @param simulationIterationMin The lower bound on the randomly generated number of iterations.
  * @param simulationIterationMax The upper bound on the randomly generated number of iterations.
  * @param constantMin The lower bound on the randomly generated constants.
  * @param constantMax The upper bound on the randomly generated constants.
  * @param simulationIterations Hardcoded number of iterations. Iterations won't be randomly generated.
  * @param constantComponentA Hardcoded constant for component A. ConstantA won't be randomly generated.
  * @param constantComponentB Hardcoded constant for component B. ConstantB won't be randomly generated.
  * @param initialConcentrationMax Max value for all values in the component concentration matrices. If not defined the
  *                                max is Int.MaxValue.
  * @param minColor Select color for one end of the gradient. Randomly generated otherwise.
  * @param maxColor Select color for the other end of the gradient. Randomly generated otherwise.
  *
  * @author Mark Soule on 9/24/16.
  */
case class AlanTurtlePresets(simulationIterationMin: Int,
                             simulationIterationMax: Int,
                             constantMin: Double,
                             constantMax: Double,
                             simulationIterations: Option[Int] = None,
                             constantComponentA: Option[Double] = None,
                             constantComponentB: Option[Double] = None,
                             initialConcentrationMax: Option[Int] = None,
                             minColor: Option[Color] = None,
                             maxColor: Option[Color] = None
                            )
