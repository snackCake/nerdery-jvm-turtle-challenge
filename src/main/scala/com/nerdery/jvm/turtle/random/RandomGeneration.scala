package com.nerdery.jvm.turtle.random

import java.security.SecureRandom

/**
  * Home for all random number generation functions.
  *
  * @author Mark Soule on 9/24/16.
  */
trait RandomGeneration {
  private val Random = new SecureRandom()

  /**
    * Generate a random inter from lowerBound (inclusive) to upperBound (exclusive).
    * @param lowerBound Minimum value as an integer.
    * @param upperBound Upper limit as an integer.
    * @return A randomly generated integer.
    */
  def randomIntWithinBounds(lowerBound: Int, upperBound: Int): Int = {
    randomInt(Some(upperBound - lowerBound)) + lowerBound
  }

  /**
    * Generate a random double from lowerBound (inclusive) to upperBound (exclusive).
    * @param lowerBound Minimum value as an integer.
    * @param upperBound Upper limit as an integer.
    * @return A randomly generated double.
    */
  def randomDoubleWithinBounds(lowerBound: Int, upperBound: Int): Double = {
    randomDouble(Some(upperBound - lowerBound)) + lowerBound
  }

  /**
    * Generate a random double from 0 to limit.
    * @param limit The maximum value (exclusive) to generate. Int.MaxValue is used if not provided.
    * @return A randomly generated double.
    */
  def randomDouble(limit: Option[Int] = None): Double = {
    randomInt(limit).toDouble
  }

  /**
    * Generate a random integer from 0 to limit.
    * @param limit The maximum value (exclusive) to generate. Int.MaxValue is used if not provided.
    * @return A randomly generated integer.
    */
  def randomInt(limit: Option[Int] = None): Int = {
    limit.map(Random.nextInt).getOrElse(Random.nextInt)
  }



}
