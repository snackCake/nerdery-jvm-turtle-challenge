package com.nerdery.jvm.turtle.model

import com.nerdery.jvm.turtle.exception.ExceededBoundsOfSkinSample

/**
  * Case class for the dimensions and concentration matrices of a skin sample to be drawn by AlanTurtle.
  *
  * @author Mark Soule on 9/24/16.
  */
case class SkinSample(concentrationMatrixA: Seq[Seq[Double]], concentrationMatrixB: Seq[Seq[Double]], cellsTall: Int, cellsWide: Int) {

  /**
    * Update the skin sample with new concentrations.
    *
    * @param newConcentrationA New concentration of component A.
    * @param newConcentrationB new concentration of component B.
    * @param row The row of each concentration matrix to update.
    * @param column The column of each concentration matric to update.
    * @throws ExceededBoundsOfSkinSample if row or column exceeds bounds of concentration matrices.
    * @return The new SkinSample.
    */
  @throws[ExceededBoundsOfSkinSample]
  def update(newConcentrationA: Double, newConcentrationB: Double, row: Int, column: Int): SkinSample = {
    try {
      val newSampleARow = concentrationMatrixA(row).updated(column, newConcentrationA)
      val newSampleBRow = concentrationMatrixB(row).updated(column, newConcentrationB)
      SkinSample(
        concentrationMatrixA.updated(row, newSampleARow),
        concentrationMatrixB.updated(row, newSampleBRow),
        cellsTall,
        cellsWide
      )
    } catch {
      case outOfBound: IndexOutOfBoundsException =>
        throw new ExceededBoundsOfSkinSample(cellsTall, cellsWide, row, column)
    }
  }

}
