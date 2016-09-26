package com.nerdery.jvm.turtle.art

import java.awt.Color

import ch.aplu.turtle.Turtle

/**
  * @author Mark Soule on 9/25/16.
  */
class TestTurtleActions(turtle: Turtle) extends TurtleActions {

  def testMoveToBottomLeftCorner(width: Int, height: Int) = {
    turtle.moveToBottomLeftCorner(width, height)
  }

  def testMoveUpOneRow(size: Int, width: Int) = {
    turtle.moveUpOneRow(size, width)
  }

  def testDrawCell(size: Int, color: Color) = {
    turtle.drawCell(size, color)
  }

  def testFillUnlessColorMatchesBackground(color: Color) = {
    turtle.fillUnlessColorMatchesBackground(color)
  }

  def testDrawSkinCells(colorMatrix: Seq[Seq[Color]], size: Int, width: Int) = {
    turtle.drawSkinCells(colorMatrix, size, width)
  }
}
