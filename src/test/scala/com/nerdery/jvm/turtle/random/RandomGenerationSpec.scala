package com.nerdery.jvm.turtle.random

import com.nerdery.jvm.turtle.UnitSpec

/**
  * @author Mark Soule on 9/25/16.
  */
class RandomGenerationSpec extends UnitSpec {

  val testRandomGeneration = new TestRandomGeneration

  "RandomGeneration" should "generate a random integer" in {
    testRandomGeneration.randomInt()
  }

  it should "generate a random integer with a limit" in {
    (0 until 100) foreach { _ =>
      testRandomGeneration.randomInt(Some(2)) should be < 2
    }
  }

  it should "generate a random double" in {
    testRandomGeneration.randomDouble()
  }

  it should "generate a random double with a limit" in {
    (0 until 100) foreach { _ =>
      testRandomGeneration.randomDouble(Some(2)) should be < 2D
    }
  }

  it should "generate a random integer with a lower and upper bound" in {
    (0 until 100) foreach { _ =>
      val generated = testRandomGeneration.randomIntWithinBounds(1, 2)
      generated should be >= 1
      generated should be < 2
    }
  }



}
