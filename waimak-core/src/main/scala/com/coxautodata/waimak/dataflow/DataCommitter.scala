package com.coxautodata.waimak.dataflow

import scala.util.Try

/**
  * Defines phases of each data committers in the data flow, which are:
  * 1) validate that committer is properly configured
  * 2) cache the labels in temp area
  * 3) when flow is successful, move all cached data into its permanent storage
  * 4) finalise or cleanup after the committer had committed all of the data into permanent storage
  *
  * Created by Alexei Perelighin
  */
abstract class DataCommitter {

  /**
    * Adds cache actions to the flow.
    *
    * @param commitName logical name of the commit
    * @param labels     labels to cache
    * @param flow       data flow to which the caching actions are added to
    * @return           data flow with caching actions
    */
  protected[dataflow] def cacheToTempFlow(commitName: String, labels: Seq[CommitEntry], flow: DataFlow): DataFlow

  /**
    * Adds actions to the flow that move data to the permanent storage, simulating a wave commit
    *
    * @param commitName logical name of the commit
    * @param labels     labels to move
    * @param flow       data flow to which move actions are added to
    * @return           data flow with move actions
    */
  protected[dataflow] def moveToPermanentStorageFlow(commitName: String, labels: Seq[CommitEntry], flow: DataFlow): DataFlow

  /**
    * Adds actions that are preformed when all data is fully committed/moved into permanent storage. Can be used
    * to do cleanup operations.
    *
    * @param commitName logical name of the commit
    * @param labels     labels that were committed
    * @param flow       data flow to which to add finalise actions
    * @return           data flow with finalise actions
    */
  protected[dataflow] def finish(commitName: String, labels: Seq[CommitEntry], flow: DataFlow): DataFlow

  /**
    * Checks if data flow that will be used to cache, commit and finalise steps contains all necessary configurations.
    *
    * @param flow data flow to validate
    * @return
    */
  protected[dataflow] def validate(flow: DataFlow, commitName: String, entries: Seq[CommitEntry]): Try[Unit]

}
