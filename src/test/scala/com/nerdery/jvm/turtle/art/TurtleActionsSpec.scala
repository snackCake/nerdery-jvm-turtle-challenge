package com.nerdery.jvm.turtle.art

import java.awt.Color

import ch.aplu.turtle.{Playground, Turtle}
import com.nerdery.jvm.turtle.UnitSpec
import com.nerdery.jvm.turtle.model.Direction
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach

/**
  * @author Mark Soule on 9/25/16.
  */
class TurtleActionsSpec extends UnitSpec with BeforeAndAfterEach {
  import TurtleActionsSpec._

  var mockTurtle: Turtle = _
  var mockPlayground: Playground = _
  var actionTester: TestTurtleActions = _

  override def beforeEach = {
    mockTurtle = mock(classOf[Turtle])
    mockPlayground = mock(classOf[Playground])
    actionTester = new TestTurtleActions(mockTurtle)
  }

  "TurtleActions" should "move to bottom left corner" in {
    when(mockTurtle.setX(-5)).thenReturn(mockTurtle)
    when(mockTurtle.setY(-5)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Up)).thenReturn(mockTurtle)

    actionTester.testMoveToBottomLeftCorner(10, 10) should equal (mockTurtle)

    verify(mockTurtle).setX(-5)
    verify(mockTurtle).setY(-5)
    verify(mockTurtle).setHeading(Direction.Up)
  }

  it should "move up one row" in {
    when(mockTurtle.setX(-50)).thenReturn(mockTurtle)
    when(mockTurtle.curY).thenReturn(testSize)
    when(mockTurtle.setY(20)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Up)).thenReturn(mockTurtle)

    actionTester.testMoveUpOneRow(testSize, 100) should equal (mockTurtle)

    verify(mockTurtle).setX(-50)
    verify(mockTurtle).curY
    verify(mockTurtle).setY(20)
    verify(mockTurtle).setHeading(Direction.Up)
  }

  it should "draw a cell" in {
    when(mockTurtle.setPenColor(testColor)).thenReturn(mockTurtle)
    when(mockTurtle.forward(testSize))
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Right)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Down)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Left)).thenReturn(mockTurtle)
    when(mockTurtle.getPlayground).thenReturn(mockPlayground)
    when(mockPlayground.getBackground).thenReturn(testColor)
    when(mockTurtle.curX).thenReturn(testSize)
    when(mockTurtle.setX(20)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Up)).thenReturn(mockTurtle)

    actionTester.testDrawCell(testSize, testColor) should equal (mockTurtle)

    verify(mockTurtle).setPenColor(testColor)
    verify(mockTurtle, times(4)).forward(testSize)
    verify(mockTurtle).setHeading(Direction.Right)
    verify(mockTurtle).setHeading(Direction.Down)
    verify(mockTurtle).setHeading(Direction.Left)
    verify(mockTurtle).getPlayground
    verify(mockPlayground).getBackground
    verify(mockTurtle).curX
    verify(mockTurtle).setX(20)
    verify(mockTurtle).setHeading(Direction.Up)
  }

  it should "fill since background is a different color" in {
    when(mockTurtle.getPlayground).thenReturn(mockPlayground)
    when(mockPlayground.getBackground).thenReturn(Color.WHITE)
    when(mockTurtle.curX).thenReturn(testSize)
    when(mockTurtle.curY).thenReturn(testSize)
    when(mockTurtle.setFillColor(testColor)).thenReturn(mockTurtle)
    when(mockTurtle.fill(testSize + 1, testSize + 1)).thenReturn(mockTurtle)
    
    actionTester.testFillUnlessColorMatchesBackground(testColor) should equal (mockTurtle)

    verify(mockTurtle).getPlayground
    verify(mockPlayground).getBackground
    verify(mockTurtle).curX
    verify(mockTurtle).curY
    verify(mockTurtle).setFillColor(testColor)
    verify(mockTurtle).fill(testSize + 1, testSize + 1)
  }

  it should "don't fill because of bad color matchup" in {
    when(mockTurtle.getPlayground).thenReturn(mockPlayground)
    when(mockPlayground.getBackground).thenReturn(testColor)

    actionTester.testFillUnlessColorMatchesBackground(testColor) should equal (mockTurtle)

    verify(mockTurtle).getPlayground
    verify(mockPlayground).getBackground
  }

  it should "draw skin cells" in {
    when(mockTurtle.setPenColor(testColor)).thenReturn(mockTurtle)
    when(mockTurtle.forward(testSize))
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Right)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Down)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Left)).thenReturn(mockTurtle)
    when(mockTurtle.getPlayground).thenReturn(mockPlayground)
    when(mockPlayground.getBackground).thenReturn(testColor)
    when(mockTurtle.curX).thenReturn(0)
    when(mockTurtle.setX(10)).thenReturn(mockTurtle)
    when(mockTurtle.setHeading(Direction.Up))
      .thenReturn(mockTurtle)
      .thenReturn(mockTurtle)
    when(mockTurtle.setX(-5)).thenReturn(mockTurtle)
    when(mockTurtle.curY).thenReturn(0)
    when(mockTurtle.setY(10)).thenReturn(mockTurtle)

    actionTester.testDrawSkinCells(Seq(Seq(testColor)), testSize, testSize)

    verify(mockTurtle).setPenColor(testColor)
    verify(mockTurtle, times(4)).forward(testSize)
    verify(mockTurtle).setHeading(Direction.Right)
    verify(mockTurtle).setHeading(Direction.Down)
    verify(mockTurtle).setHeading(Direction.Left)
    verify(mockTurtle).getPlayground
    verify(mockPlayground).getBackground
    verify(mockTurtle).curX
    verify(mockTurtle).setX(10)
    verify(mockTurtle, times(2)).setHeading(Direction.Up)
    verify(mockTurtle).setX(-5)
    verify(mockTurtle).curY
    verify(mockTurtle).setY(10)
  }
}

object TurtleActionsSpec {
  val testColor = Color.BLACK
  val testSize = 10
}
