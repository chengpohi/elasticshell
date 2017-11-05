package com.github.chengpohi.api

import com.github.chengpohi.api.dsl._
import org.elasticsearch.client.{Client, ClusterAdminClient, IndicesAdminClient}

/**
  * elasticdsl
  * Created by chengpohi on 1/6/16.
  */
trait DSLs
    extends AggsDSL
    with AnalyzeDSL
    with DeleterDSL
    with IndexDSL
    with ManageDSL
    with QueryDSL

class ElasticDSL(cl: Client) extends DSLs {
  val client: Client = cl
  val clusterClient: ClusterAdminClient = client.admin.cluster()
  val indicesClient: IndicesAdminClient = client.admin.indices()
  val ALL_INDEX: String = "*"
  val ALL_TYPE: String = "_all"
}
