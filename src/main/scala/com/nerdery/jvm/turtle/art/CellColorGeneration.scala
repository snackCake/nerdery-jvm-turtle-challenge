package com.nerdery.jvm.turtle.art

import java.awt.Color

import com.nerdery.jvm.turtle.random.RandomGeneration

/**
  * Functions to handle random generation of cell colors and patterns.
  *
  * @author Mark Soule on 9/23/16.
  */
trait CellColorGeneration extends RandomGeneration {
  val MaxRGB = 256

  /**
    * Create the matric of cell colors based on the concentration of the chosen component. The values in the
    * concentration matrix will be scaled and then used at the index into colorGradient to choose the appropriate color.
    *
    * @param concentrationMatrix Matrix of doubles containing concentrations of a component.
    * @param colorGradient Seq of colors in order from the chosen minColor to the chosen maxColor.
    * @throws AssertionError The colorGradient sequence must contain MaxRGB colors.
    * @return A matrix of colors for the skin sample.
    */
  @throws[AssertionError]
  def chooseCellColors(concentrationMatrix: Seq[Seq[Double]], colorGradient: Seq[Color]): Seq[Seq[Color]] = {
    assert(colorGradient.size == MaxRGB, s"Parameter 'colorGradient' must have $MaxRGB colors, only has ${colorGradient.size} color(s)")

    val concentrationMin = concentrationMatrix.map(_.min).min
    val concentrationMax = concentrationMatrix.map(_.max).max
    concentrationMatrix map { concentrationRow =>
      concentrationRow map { cellConcentration =>
        colorGradient(scaleConcentration(cellConcentration, concentrationMin, concentrationMax, MaxRGB - 1).toInt)
      }
    }
  }

  /**
    * Scale the concentration from 0 to scaleMax.
    *
    * @param concentration The concentration to scale.
    * @param concentrationMin The minimum concentration value available from the matrix.
    * @param concentrationMax The maximum concentration value available from the matrix.
    * @param scaleMax The maximum value to scale the value.
    * @return The scaled concentration as a Double.
    */
  def scaleConcentration(concentration: Double,
                         concentrationMin: Double,
                         concentrationMax: Double,
                         scaleMax: Int): Double = {
    val concentrationDifference = concentrationMax - concentrationMin
    if(concentrationDifference == 0)
      0
    else {
      val scaledValue = ((concentration - concentrationMin) * scaleMax) / concentrationDifference
      correctForBounds(scaledValue, scaleMax)
    }
  }

  /**
    * Correct a value to 0 or scaleMax if the value is outside that range.
    *
    * @param value The value to correct.
    * @param scaleMax The max to scale the value.
    * @return The corrected value as a Double.
    */
  private def correctForBounds(value: Double, scaleMax: Int): Double = {
    if (value < 0)
      0
    else if (value > scaleMax)
      scaleMax
    else
      value
  }

  /**
    * Generate a gradient that is MaxRGB values long from minColor to maxColor.
    *
    * @param minColor The 'minimum' end of the gradient.
    * @param maxColor The 'maximum' end of the gradient.
    * @return A sequence of Colors MaxRGB values in length.
    */
  def generateColorGradient(minColor: Color, maxColor: Color): Seq[Color] = {
    (0 until MaxRGB).foldLeft(Seq.empty[Color]) { (colorSeq: Seq[Color], index: Int) =>
      val newRed = interpolateColor(index, minColor.getRed, maxColor.getRed)
      val newGreen = interpolateColor(index, minColor.getGreen, maxColor.getGreen)
      val newBlue = interpolateColor(index, minColor.getBlue, maxColor.getBlue)
      colorSeq :+ new Color(newRed, newGreen, newBlue)
    }
  }

  /**
    * Interpolate two RGB values
    *
    * @param index The position between the two colors to find.
    * @param min One end of the RGB gradient.
    * @param max The other end of the RGB gradient.
    * @return The RGB value as an Int that is 'index' between min and max.
    */
  private def interpolateColor(index: Int, min: Int, max: Int): Int = {
    min + (max - min) * index / (MaxRGB - 1)
  }

  /**
    * Create a randomized color.
    *
    * @return Color instance.
    */
  def randomizeColor: Color = {
    new Color(randomInt(Some(MaxRGB)), randomInt(Some(MaxRGB)), randomInt(Some(MaxRGB)))
  }

}
