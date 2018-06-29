package com.coxautodata.waimak.dataflow.spark

import java.nio.file.Files

import org.apache.commons.io.FileUtils
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FunSpec, Matchers}

/**
  * Created by Vicky Avison on 24/10/17.
  */
trait SparkSpec extends FunSpec with Matchers with BeforeAndAfterEach {

  var sparkSession: SparkSession = _
  val master = "local[2]"
  val appName: String

  def sparkOptions: Map[String, String] = Map("spark.executor.memory" -> "2g", "spark.ui.enabled" -> "false")

  override def beforeEach(): Unit = {

    val sparkBuilder = SparkSession
      .builder()
      .appName(appName)
      .master(master)

    sparkSession = sparkOptions.foldLeft(sparkBuilder) { case (z, (k, v)) => z.config(k, v) }.getOrCreate()

  }

  override def afterEach(): Unit = {
    sparkSession.stop()
  }
}


trait SparkAndTmpDirSpec extends SparkSpec {

  var testingBaseDir: java.nio.file.Path = _
  var testingBaseDirName: String = _
  var tmpDir: Path = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    testingBaseDir = Files.createTempDirectory("test_output")
    testingBaseDirName = testingBaseDir.toString
    tmpDir = new Path(testingBaseDir.toAbsolutePath.toString + "/tmp")
  }

  override def afterEach(): Unit = {
    super.afterEach()
    FileUtils.deleteDirectory(testingBaseDir.toFile)
  }
}

