package com.nerdery.jvm.turtle.simulation

import com.nerdery.jvm.turtle.model.{CellNeighborhood, SkinSample}
import com.nerdery.jvm.turtle.random.RandomGeneration

/**
  * This contains the functionality to perform Turing's Reaction-Diffusion morphogenesis equation.
  * @author Mark Soule on 9/23/16.
  */
trait CellSimulation extends RandomGeneration {

  /**
    * Generate the inital skin sample from random data.
    * @param cellsTall The sample's height in cells.
    * @param cellsWide The sample's width in cells.
    * @param randomBound The maximum value for random values. Optional, otherwise Int.MaxValue is used.
    * @return The SkinSample seeded with random data.
    */
  def generateInitialSkinSample(cellsTall: Int, cellsWide: Int, randomBound: Option[Int]): SkinSample = {
    val concentrationOfComponentA = generateRandomConcentrationOfComponents(cellsTall, cellsWide, randomBound)
    val concentrationOfComponentB = generateRandomConcentrationOfComponents(cellsTall, cellsWide, randomBound)
    SkinSample(concentrationOfComponentA, concentrationOfComponentB, cellsTall, cellsWide)
  }

  /**
    * Generates the matrix of random component concentrations.
    * @param cellsTall The sample's height in cells.
    * @param cellsWide The sample's width in cells.
    * @param randomBound The maximum value for random values. Optional, otherwise Int.MaxValue is used.
    * @return The randomly generated matrix.
    */
  def generateRandomConcentrationOfComponents(cellsTall: Int, cellsWide: Int, randomBound: Option[Int]): Seq[Seq[Double]] = {
    Seq.tabulate[Double](cellsTall, cellsWide) { (i, j) =>
      randomDouble(randomBound)
    }
  }

  /**
    * Solve Turing's morphogenesis equation by Euler's method.
    *
    * @param skinSample Object containing component concentrations.
    * @param iterations Number of iterations of Euler's method to perform.
    * @param constantA Constant for diffusion of component A.
    * @param constantB Constant for diffusion of component B.
    * @return The final SkinSample to draw with AlanTurtle.
    */
  def morphogenesisByEulersMethod(skinSample: SkinSample,
                                  iterations: Int,
                                  constantA: Double,
                                  constantB: Double): SkinSample = {
    (0 until iterations).foldLeft(skinSample) { (sampleGeneration, _) =>
      (0 until skinSample.cellsTall).foldLeft(sampleGeneration) { (sampleGeneration, row) =>
        (0 until skinSample.cellsWide).foldLeft(sampleGeneration) { (sampleGeneration, column) =>
          reactAndDiffuseCell(sampleGeneration, row, column, constantA, constantB)
        }
      }
    }
  }

  /**
    * React and diffuse a particular cell for this iteration.
    * @param skinSample SkinSample containing all concentration information.
    * @param cellRowIndex Row index of the cell being calculated.
    * @param cellColumnIndex Column index of the cell being calculated.
    * @param constantA Constant for diffusion of component A.
    * @param constantB Constant for diffusion of component B.
    * @return SkinSample with updated component concentrations for the given cell.
    */
  private def reactAndDiffuseCell(skinSample: SkinSample,
                                  cellRowIndex: Int,
                                  cellColumnIndex: Int,
                                  constantA: Double,
                                  constantB: Double): SkinSample = {
    val (concentrationA, concentrationB) = (skinSample.concentrationMatrixA, skinSample.concentrationMatrixB)
    val neighborhood = mapNeighborhood(skinSample, cellRowIndex, cellColumnIndex)

    val newConcentrationAForCell = calculateReactionAndDiffusionForComponent(
      constantA,
      concentrationA,
      concentrationB,
      neighborhood,
      None,
      calculateReactionA
    )

    val newConcentrationBForCell = calculateReactionAndDiffusionForComponent(
      constantB,
      concentrationB,
      concentrationA,
      neighborhood,
      Some(newConcentrationAForCell),
      calculateReactionB
    )

    skinSample.update(newConcentrationAForCell, newConcentrationBForCell, cellRowIndex, cellColumnIndex)
  }

  /**
    * Calculate the reaction and diffusion part of Euler's algorithm for this iteration and for one component.
    * This will be called for component A and B separately.
    *
    * @param diffusionConstant Constant used for calculating diffusion.
    * @param primaryConcentrationMatrix The matrix of the component to calculate.
    * @param secondaryConcentrationMatrix The maxtrix of the other component.
    * @param cellNeighborhood Object containing spatial information about the cell being processed.
    * @param newCellConcentrationForSecondary The reaction-diffusion result for the secondary component if available.
    * @param calculateReaction Function for calculating reaction since reaction is calculated differently for both
    *                          components.
    * @return The new concentration for this component as a Double.
    */
  private def calculateReactionAndDiffusionForComponent(diffusionConstant: Double,
                                                        primaryConcentrationMatrix: Seq[Seq[Double]],
                                                        secondaryConcentrationMatrix: Seq[Seq[Double]],
                                                        cellNeighborhood: CellNeighborhood,
                                                        newCellConcentrationForSecondary: Option[Double] = None,
                                                        calculateReaction: (Seq[Seq[Double]], Seq[Seq[Double]], Int, Int, Double) => Double
                                                       ): Double = {
    val diffusion = calculateDiffusion(
      diffusionConstant,
      primaryConcentrationMatrix,
      cellNeighborhood
    )
    val reaction = calculateReaction(
      primaryConcentrationMatrix,
      secondaryConcentrationMatrix,
      cellNeighborhood.rowIndex,
      cellNeighborhood.columnIndex,
      newCellConcentrationForSecondary.getOrElse(0)
    )

    calculateNewConcentration(
      primaryConcentrationMatrix(cellNeighborhood.rowIndex)(cellNeighborhood.columnIndex),
      diffusion,
      reaction
    )
  }

  /**
    * Calculate the reaction part of Euler's method for component A. Shares the same signature as calculateReactionB.
    * @param concentrationMatrixA Concentration matrix for component A.
    * @param concentrationMatrixB Concentration matrix for component B.
    * @param cellRowIndex Row index of the cell being calculated.
    * @param cellColumnIndex Column index of the cell being calculated.
    * @param calculatedNewConcentrationForB N/A
    * @return The reaction result as a Double.
    */
  private def calculateReactionA(concentrationMatrixA: Seq[Seq[Double]],
                                 concentrationMatrixB: Seq[Seq[Double]],
                                 cellRowIndex: Int,
                                 cellColumnIndex: Int,
                                 calculatedNewConcentrationForB: Double): Double = {
    concentrationMatrixA(cellRowIndex)(cellColumnIndex) * concentrationMatrixB(cellRowIndex)(cellColumnIndex) - concentrationMatrixA(cellRowIndex)(cellColumnIndex) - 12
  }

  /**
    * Calculate the reaction part of Euler's method for component B. Shares the same signature as calculateReactionA.
    * @param concentrationMatrixB Concentration matrix for component B.
    * @param concentrationMatrixA N/A
    * @param cellRowIndex Row index of the cell being calculated.
    * @param cellColumnIndex Column index of the cell being calculated.
    * @param calculatedNewConcentrationForA The calculated reaction-diffusion result for component A this generation.
    * @return The reaction result as a Double.
    */
  private def calculateReactionB(concentrationMatrixB: Seq[Seq[Double]],
                                 concentrationMatrixA: Seq[Seq[Double]],
                                 cellRowIndex: Int,
                                 cellColumnIndex: Int,
                                 calculatedNewConcentrationForA: Double): Double = {
    16 - (calculatedNewConcentrationForA * concentrationMatrixB(cellRowIndex)(cellColumnIndex))
  }

  /**
    * Calculate the diffusion part of Euler's method.
    * @param constant A constant value.
    * @param concentrationMatrix The current concentration matrix.
    * @param cellNeighborhood Object containing the indices of the cell being calculated and the indices of its
    *                         neighbors.
    * @return The diffusion result as a Double.
    */
  private def calculateDiffusion(constant: Double,
                                 concentrationMatrix: Seq[Seq[Double]],
                                 cellNeighborhood: CellNeighborhood
                                ): Double = {
    constant *
      (concentrationMatrix(cellNeighborhood.topNeighbor)(cellNeighborhood.columnIndex) -
        (2 * concentrationMatrix(cellNeighborhood.rowIndex)(cellNeighborhood.columnIndex)) +
        concentrationMatrix(cellNeighborhood.bottomNeighbor)(cellNeighborhood.columnIndex) +
        concentrationMatrix(cellNeighborhood.rowIndex)(cellNeighborhood.rightNeighbor) -
        (2 * concentrationMatrix(cellNeighborhood.rowIndex)(cellNeighborhood.columnIndex)) +
        concentrationMatrix(cellNeighborhood.rowIndex)(cellNeighborhood.leftNeighbor))
  }

  /**
    * Calculate the new concentration based on Euler's method.
    * @param oldConcentration The old value for the component's concentration.
    * @param diffusionResult Final result of diffusion.
    * @param reactionResult Final result of reaction.
    * @return The new concentration as a Double.
    */
  private def calculateNewConcentration(oldConcentration: Double, diffusionResult: Double, reactionResult: Double): Double = {
    correctForNegative(oldConcentration + (0.01 * (diffusionResult + reactionResult)))
  }

  /**
    * Map the indices of the surrounding cells into an object.
    * @param skinSample The SkinSample object containing data about this sample.
    * @param cellRowIndex Row index of the cell being calculated.
    * @param cellColumnIndex Column index of the cell being calculated.
    * @return A CellNeighborhood object containing relevant spatial information.
    */
  private def mapNeighborhood(skinSample: SkinSample,
                              cellRowIndex: Int,
                              cellColumnIndex: Int): CellNeighborhood = {
    val (bottomNeighbor, topNeighbor) = fitToTorus(cellRowIndex, skinSample.cellsTall)
    val (leftNeighbor, rightNeighbor) = fitToTorus(cellColumnIndex, skinSample.cellsWide)
    CellNeighborhood(cellRowIndex, cellColumnIndex, topNeighbor, bottomNeighbor, rightNeighbor, leftNeighbor)
  }

  /**
    * Make any number < 0 to be 0.
    * @param number The number to correct.
    * @return The number or 0 if it was < 0.
    */
  private def correctForNegative(number: Double): Double = {
    if (number < 0) 0 else number
  }

  /**
    * Fit indices to a torus. That means that elements at the bottom will touch the top, elements on the left will touch
    * the right. Like the Asteroids video game.
    * @param dimensionIndex The index to fit.
    * @param dimensionSize The size of that dimension.
    * @return The tuple (leftOrBottomNeighbor, rightOrTopNeighbor)
    */
  private def fitToTorus(dimensionIndex: Int, dimensionSize: Int): (Int, Int) = {
    val leftOrBottomNeighbor = if (dimensionIndex == 0)
      dimensionSize - 1
    else
      dimensionIndex - 1

    val rightOrTopNeighbor = if (dimensionIndex == (dimensionSize - 1))
      0
    else
      dimensionIndex + 1

    (leftOrBottomNeighbor, rightOrTopNeighbor)
  }

}
