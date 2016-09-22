package com.nerdery.jvm.turtle.art

import java.awt.Color

import com.nerdery.jvm.turtle.UnitSpec

/**
  * @author Mark Soule on 9/25/16.
  */
class CellColorGenerationSpec extends UnitSpec {

  val testCellColorGeneration = new TestCellColorGeneration

  "CellColorGeneration" should "scale concentration accurately" in {
    testCellColorGeneration.scaleConcentration(-1, 0, 10, testCellColorGeneration.MaxRGB - 1) should equal (0)
    testCellColorGeneration.scaleConcentration(0, 0, 10, testCellColorGeneration.MaxRGB - 1) should equal (0)
    testCellColorGeneration.scaleConcentration(5, 0, 10, testCellColorGeneration.MaxRGB - 1) should equal (127.5)
    testCellColorGeneration.scaleConcentration(10, 0, 10, testCellColorGeneration.MaxRGB - 1) should equal (255)
    testCellColorGeneration.scaleConcentration(11, 0, 10, testCellColorGeneration.MaxRGB - 1) should equal (255)
  }

  it should "generate a random color" in {
    val color = testCellColorGeneration.randomizeColor
    color should not be null
  }

  it should "generate a solid gradient" in {
    testCellColorGeneration.generateColorGradient(Color.BLACK, Color.BLACK) foreach { colorShade =>
      colorShade should equal (Color.BLACK)
    }
  }

  it should "generate a smooth gradient" in {
    val colorGradient = testCellColorGeneration.generateColorGradient(Color.WHITE, Color.BLACK)
    colorGradient.size should equal (testCellColorGeneration.MaxRGB)
    (testCellColorGeneration.MaxRGB to 0) foreach { (colorIndex: Int) =>
      val colorShade = colorGradient(colorIndex)
      colorShade.getRed should equal (colorIndex)
      colorShade.getGreen should equal (colorIndex)
      colorShade.getBlue should equal (colorIndex)
    }
  }

  it should "choose cell colors for a multi-element (single row) one color matrix" in {
    val availableColors = Seq.fill(testCellColorGeneration.MaxRGB)(Color.BLACK)
    val colorMatrix = testCellColorGeneration.chooseCellColors(Seq(Seq(0, 1)), availableColors)
    colorMatrix should equal (Seq(Seq[Color](Color.BLACK, Color.BLACK)))
  }

  it should "choose cell colors for a multi-element (multiple rows) one color matrix" in {
    val availableColors = Seq.fill(testCellColorGeneration.MaxRGB)(Color.BLACK)
    val colorMatrix = testCellColorGeneration.chooseCellColors(Seq(Seq(0, 1), Seq(10, 11)), availableColors)
    colorMatrix should equal (Seq(Seq(Color.BLACK, Color.BLACK), Seq(Color.BLACK, Color.BLACK)))
  }

  it should "lower values should get the lower color while larger values should get the higher color" in {
    val availableColors = testCellColorGeneration.generateColorGradient(Color.WHITE, Color.BLACK)
    val colorMatrix = testCellColorGeneration.chooseCellColors(Seq(Seq(0, 1)), availableColors)
    colorMatrix should equal (Seq(Seq[Color](Color.WHITE, Color.BLACK)))
  }
}
