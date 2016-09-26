package com.nerdery.jvm.turtle.model

/**
  * Case class containing a cell's position and indices of neighboring cells.
  * @author Mark Soule on 9/25/16.
  */
case class CellNeighborhood(rowIndex: Int, columnIndex: Int, topNeighbor: Int, bottomNeighbor: Int, rightNeighbor: Int, leftNeighbor: Int)
