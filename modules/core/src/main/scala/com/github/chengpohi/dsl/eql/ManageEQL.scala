package com.github.chengpohi.dsl.eql

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequestBuilder
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequestBuilder
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder
import org.elasticsearch.action.admin.cluster.tasks.PendingClusterTasksRequestBuilder
import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder
import org.elasticsearch.action.support.master.AcknowledgedResponse

import scala.concurrent.{Future, Promise}
import scala.util.Success

/**
  * eql
  * Created by chengpohi on 6/26/16.
  */
trait ManageEQL extends DeleterEQL with QueryEQL {

  case object node {
    def stats(nodeIds: List[String]): NodeStatsRequestDefinition = {
      val prepareNodesStats: NodesStatsRequestBuilder =
        clusterClient.prepareNodesStats(nodeIds: _*)
      NodeStatsRequestDefinition(prepareNodesStats)
    }

    def stats(nodeType: NodeType): NodeStatsRequestDefinition = {
      val prepareNodesStats: NodesStatsRequestBuilder =
        clusterClient.prepareNodesStats()
      NodeStatsRequestDefinition(prepareNodesStats)
    }

    def info: NodeInfoRequestDefinition = {
      val prepareNodesStats: NodesInfoRequestBuilder =
        clusterClient.prepareNodesInfo()
      NodeInfoRequestDefinition(prepareNodesStats)
    }
  }

  case object indice {
    def stats(nodeIds: List[String]): IndicesStatsRequestDefinition = {
      val prepareNodesStats: IndicesStatsRequestBuilder =
        indicesClient.prepareStats(nodeIds: _*)
      IndicesStatsRequestDefinition(prepareNodesStats)
    }

    def stats(indiceType: NodeType): IndicesStatsRequestDefinition = {
      val prepareNodesStats: IndicesStatsRequestBuilder =
        indicesClient.prepareStats(indiceType.value: _*)
      IndicesStatsRequestDefinition(prepareNodesStats)
    }

    def update(indexName: String): UpdateSettingsRequestDefinition = {
      val clusterUpdateSettingsRequestBuilder: UpdateSettingsRequestBuilder =
        indicesClient.prepareUpdateSettings(indexName)
      UpdateSettingsRequestDefinition(clusterUpdateSettingsRequestBuilder)
    }
  }

  case object cluster {
    def stats: ClusterStatsRequestDefinition = {
      val prepareNodesStats: ClusterStatsRequestBuilder =
        clusterClient.prepareClusterStats()
      ClusterStatsRequestDefinition(prepareNodesStats)
    }

    def state: ClusterStateRequestDefinition = {
      val stateRequestBuilder: ClusterStateRequestBuilder =
        clusterClient.prepareState()
      ClusterStateRequestDefinition(stateRequestBuilder)
    }

    def health: ClusterHealthRequestDefinition = {
      val clusterHealthRequestBuilder: ClusterHealthRequestBuilder =
        clusterClient.prepareHealth()
      ClusterHealthRequestDefinition(clusterHealthRequestBuilder)
    }

    def settings: ClusterStateRequestDefinition = {
      val stateRequestBuilder: ClusterStateRequestBuilder =
        clusterClient
          .prepareState()
          .setNodes(false)
          .setRoutingTable(false)
          .setLocal(true)
      ClusterStateRequestDefinition(stateRequestBuilder)
    }
  }

  case object create {
    def tokenizer(tokenizer: String): NgramTokenizerDefinition = {
      NgramTokenizerDefinition(tokenizer)
    }

    def repository(repositoryName: String): PutRepositoryDefinition = {
      val putRepository: PutRepositoryRequestBuilder =
        clusterClient.preparePutRepository(repositoryName)
      PutRepositoryDefinition(putRepository)
    }

    def snapshot(snapshotName: String): CreateSnapshotDefinition = {
      CreateSnapshotDefinition(snapshotName)
    }

    def index(indexName: String): CreateIndexDefinition =
      CreateIndexDefinition(indicesClient.prepareCreate(indexName))

    def analyzer(analyzer: String): CreateAnalyzerRequestDefinition =
      CreateAnalyzerRequestDefinition(analyzer)
    def filter(filter: String): CreateFilterRequestDefinition =
      CreateFilterRequestDefinition(filter)
    def analyze(analyzer: String): AnalyzerDefinition =
      AnalyzerDefinition(analyzer)
    def field(field: String): FieldDefinition = FieldDefinition(field)
  }

  case object add {
    def alias(targetIndex: String): AddAliasRequestDefinition = {
      AddAliasRequestDefinition(targetIndex)
    }
  }

  case object restore {
    def snapshot(snapshotName: String): RestoreSnapshotRequestDefinition =
      RestoreSnapshotRequestDefinition(snapshotName)
  }

  case object close {
    def index(indexName: String): CloseIndexRequestDefinition = {
      val closeIndexRequestBuilder: CloseIndexRequestBuilder =
        indicesClient.prepareClose(indexName)
      CloseIndexRequestDefinition(closeIndexRequestBuilder)
    }
  }

  case object open {
    def index(indexName: String): OpenIndexRequestDefinition = {
      val openIndexRequestBuilder: OpenIndexRequestBuilder =
        indicesClient.prepareOpen(indexName)
      OpenIndexRequestDefinition(openIndexRequestBuilder)
    }
  }

  case object pending {
    def tasks: PendingClusterTasksDefinition = {
      val pendingClusterTasksRequestBuilder: PendingClusterTasksRequestBuilder =
        clusterClient.preparePendingClusterTasks()
      PendingClusterTasksDefinition(pendingClusterTasksRequestBuilder)
    }
  }

  case object waiting {
    def index(indexName: String): ClusterHealthRequestDefinition = {
      val prepareHealth: ClusterHealthRequestBuilder =
        clusterClient.prepareHealth(indexName)
      ClusterHealthRequestDefinition(prepareHealth)
    }
  }

  case object refresh {
    def index(indices: String): RefreshRequestDefinition = {
      RefreshRequestDefinition(indices)
    }
  }

  case class CreateFilterRequestDefinition(analyzerSetting: String)
      extends Definition[AcknowledgedResponse] {
    var keepwords: String = _
    var tpe: String = _

    def keepwords(k: String): CreateFilterRequestDefinition = {
      this.keepwords = k
      this
    }

    def tpe(p: String): CreateFilterRequestDefinition = {
      this.tpe = p
      this
    }

    override def execute: Future[AcknowledgedResponse] = ???
    override def json: String = ???
  }

  case class CreateAnalyzerRequestDefinition(analyzerSetting: String)
      extends Definition[AcknowledgedResponse] {
    var tpe: String = _

    var tokenizer: String = _

    var filter: String = _

    var stopwords: String = _

    def tpe(p: String): CreateAnalyzerRequestDefinition = {
      this.tpe = p
      this
    }
    def tokenizer(t: String): CreateAnalyzerRequestDefinition = {
      this.tokenizer = t
      this
    }

    def filter(f: String): CreateAnalyzerRequestDefinition = {
      this.filter = f
      this
    }
    def stopwords(s: String): CreateAnalyzerRequestDefinition = {
      this.stopwords = s
      this
    }

    override def execute: Future[AcknowledgedResponse] = {
      val p = Promise[AcknowledgedResponse]()
      EQL {
        close index ELASTIC_SHELL_INDEX_NAME
      } andThen {
        case _ =>
          EQL {
            indice update ELASTIC_SHELL_INDEX_NAME settings analyzerSetting
          } andThen {
            case Success(r) => p success r
          }
      }
      p.future onComplete { _ =>
        EQL {
          open index ELASTIC_SHELL_INDEX_NAME
        }
      }
      p.future
    }

    override def json: String = execute.await.json
  }

  case object cat {
    def nodes: CatNodesDefinition = {
      CatNodesDefinition()
    }
    def allocation: CatAllocationDefinition = {
      CatAllocationDefinition()
    }

    def master: CatMasterDefinition = {
      CatMasterDefinition()
    }
    def indices: CatIndicesDefinition = {
      CatIndicesDefinition()
    }
    def shards: CatShardsDefinition = {
      CatShardsDefinition()
    }

    def count: CatCountDefinition = {
      CatCountDefinition()
    }

    def pending_tasks: CatPendingTaskDefinition = {
      CatPendingTaskDefinition()
    }
    def recovery: CatRecoveryDefinition = {
      CatRecoveryDefinition()
    }
  }
}
