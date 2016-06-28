package com.github.chengpohi.api.dsl

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder

/**
  * elasticshell
  * Created by chengpohi on 6/28/16.
  */
trait AnalyzeDSL extends DSLDefinition{
  case object analysis {
    def text(text: String) = {
      val analyzeRequestBuilder: AnalyzeRequestBuilder = indicesClient.prepareAnalyze(text)
      AnalyzeRequestDefinition(analyzeRequestBuilder)
    }
  }
}
