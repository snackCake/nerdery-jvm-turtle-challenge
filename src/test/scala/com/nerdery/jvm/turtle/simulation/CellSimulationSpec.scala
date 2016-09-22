package com.nerdery.jvm.turtle.simulation

import com.nerdery.jvm.turtle.UnitSpec

/**
  * @author Mark Soule on 9/25/16.
  */
class CellSimulationSpec extends UnitSpec {

  val testCellSimulation = new TestCellSimulation

  "CellSimulation" should "generate square initial skin sample" in {
    val generatedSample = testCellSimulation.generateInitialSkinSample(5, 5, None)
    generatedSample.cellsWide should equal (5)
    generatedSample.cellsTall should equal (5)
    generatedSample.concentrationMatrixA.size should equal (5)
    generatedSample.concentrationMatrixA foreach { row =>
      row.size should equal (5)
    }
    generatedSample.concentrationMatrixB.size should equal (5)
    generatedSample.concentrationMatrixB foreach { row =>
      row.size should equal (5)
    }
  }

  it should "generate rectangular initial skin sample" in {
    val generatedSample = testCellSimulation.generateInitialSkinSample(5, 7, None)
    generatedSample.cellsWide should equal (7)
    generatedSample.cellsTall should equal (5)
    generatedSample.concentrationMatrixA.size should equal (5)
    generatedSample.concentrationMatrixA foreach { row =>
      row.size should equal (7)
    }
    generatedSample.concentrationMatrixB.size should equal (5)
    generatedSample.concentrationMatrixB foreach { row =>
      row.size should equal (7)
    }
  }

  it should "generate square initial skin sample with set bound" in {
    val generatedSample = testCellSimulation.generateInitialSkinSample(5, 5, Some(100))
    generatedSample.cellsWide should equal (5)
    generatedSample.cellsTall should equal (5)
    generatedSample.concentrationMatrixA.size should equal (5)
    generatedSample.concentrationMatrixA foreach { row =>
      row.size should equal (5)
      row foreach { value =>
        value should be <= 100D
      }
    }
    generatedSample.concentrationMatrixB.size should equal (5)
    generatedSample.concentrationMatrixB foreach { row =>
      row.size should equal (5)
      row foreach { value =>
        value should be <= 100D
      }
    }
  }

  it should "generate random concentration of components" in {
    val concentrationMatrix = testCellSimulation.generateRandomConcentrationOfComponents(5, 5, None)
    concentrationMatrix.size should equal (5)
    concentrationMatrix foreach { row =>
      row.size should equal (5)
    }
  }

  it should "generate random concentration of components with bound" in {
    val concentrationMatrix = testCellSimulation.generateRandomConcentrationOfComponents(5, 5, Some(100))
    concentrationMatrix.size should equal (5)
    concentrationMatrix foreach { row =>
      row.size should equal (5)
      row foreach { value =>
        value should be <= 100D
      }
    }
  }

  it should "perform the morphogenesis" in {
    val skinSample = testCellSimulation.generateInitialSkinSample(5, 5, None)
    val morphedSkinSample = testCellSimulation.morphogenesisByEulersMethod(skinSample, 10, 20D, 20D)
    skinSample should not equal (morphedSkinSample)
  }
}
