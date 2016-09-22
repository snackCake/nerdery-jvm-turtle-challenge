package com.nerdery.jvm.turtle.art

import java.awt.Color

import ch.aplu.turtle.Turtle
import com.nerdery.jvm.turtle.model.Direction

/**
  * Trait containing an implicit class for Turtle which contains useful Turtle functions.
  * @author Mark Soule on 9/23/16.
  */
trait TurtleActions {
  implicit class TurtleActions(turtle: Turtle) {

    /**
      * Move turtle to bottom left corner pointing up.
      *
      * @param playgroundWidth Width of playground in pixels.
      * @param playgroundHeight Height of playground in pixels.
      * @return The Turtle.
      */
    def moveToBottomLeftCorner(playgroundWidth: Int, playgroundHeight: Int): Turtle = {
      turtle
        .setX(-playgroundWidth / 2)
        .setY(-playgroundHeight / 2)
        .setHeading(Direction.Up)
    }

    /**
      * Move one row up based on a cell size.
      * @param cellSize The height of a row in pixels.
      * @param playgroundWidth Width of playground in pixels.
      * @return The Turtle.
      */
    def moveUpOneRow(cellSize: Int, playgroundWidth: Int): Turtle = {
      turtle
        .setX(-playgroundWidth / 2)
        .setY(turtle.curY + cellSize)
        .setHeading(Direction.Up)
    }

    /**
      * Draw a cell based on a cellSize and color that cell.
      * @param cellSize The size of the square cell to draw.
      * @param cellColor The color of that cell.
      * @return The Turtle.
      */
    def drawCell(cellSize: Int, cellColor: Color): Turtle = {
      turtle
        .setPenColor(cellColor)
        .forward(cellSize).setHeading(Direction.Right)
        .forward(cellSize).setHeading(Direction.Down)
        .forward(cellSize).setHeading(Direction.Left)
        .forward(cellSize)
        .fillUnlessColorMatchesBackground(cellColor)
        .setX(turtle.curX + cellSize)
        .setHeading(Direction.Up)
    }

    /**
      * Fill a color unless that color matches the background of the playground.
      * @param fillColor Color to fill.
      * @return The Turtle.
      */
    def fillUnlessColorMatchesBackground(fillColor: Color): Turtle = {
      if (turtle.getPlayground.getBackground == fillColor)
        turtle
      else
        turtle
          .setFillColor(fillColor)
          .fill(turtle.curX + 1, turtle.curY + 1)
    }

    /**
      * Draw the full sample based on its matrix of colors.
      * @param cellColors Matrix of colors for the cells.
      * @param cellSize Size of a cell.
      * @param playgroundWidth Width of the playground in pixels.
      * @return The Turtle.
      */
    def drawSkinCells(cellColors: Seq[Seq[Color]],
                      cellSize: Int,
                      playgroundWidth: Int): Turtle = {
      cellColors.foldLeft(turtle) { (drawingTurtle, cellColorRow) =>
        cellColorRow.foldLeft(drawingTurtle) { (dTurtle, cellColor) =>
          dTurtle.drawCell(cellSize, cellColor)
        } moveUpOneRow(cellSize, playgroundWidth)
      }
    }
  }
}
