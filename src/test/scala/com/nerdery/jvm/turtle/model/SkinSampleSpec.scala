package com.nerdery.jvm.turtle.model

import com.nerdery.jvm.turtle.UnitSpec
import com.nerdery.jvm.turtle.exception.ExceededBoundsOfSkinSample
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
  * @author Mark Soule on 9/25/16.
  */
@RunWith(classOf[JUnitRunner])
class SkinSampleSpec extends UnitSpec {

  "SkinSample" should "create a new SkinSample instance" in {
      val skinSample = SkinSample(Nil, Nil, 0, 0)
      skinSample.concentrationMatrixA should equal (Nil)
      skinSample.concentrationMatrixB should equal (Nil)
      skinSample.cellsTall should equal (0)
      skinSample.cellsWide should equal (0)
  }

  it should "update the skin sample with a new value" in {
    val skinSample = SkinSample(Seq(Seq(1, 2, 3)), Seq(Seq(4, 5, 6)), 1, 3)
    val updatedSample = skinSample.update(10, 11, 0, 2)
    updatedSample.concentrationMatrixA should equal (Seq(Seq(1, 2, 10)))
    updatedSample.concentrationMatrixB should equal (Seq(Seq(4, 5, 11)))
    updatedSample.cellsTall should equal (1)
    updatedSample.cellsWide should equal (3)
  }

  it should "fail to update due to index out of range" in {
    val skinSample = SkinSample(Seq(Seq(1, 2, 3)), Seq(Seq(4, 5, 6)), 1, 3)
    an [ExceededBoundsOfSkinSample] should be thrownBy skinSample.update(10, 11, 0, 4)
  }
}
