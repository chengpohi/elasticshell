package com.github.chengpohi.plugin.dsl

import org.elasticsearch.action.ActionResponse
import org.elasticsearch.common.xcontent.{ToXContent, ToXContentFragment, XContentBuilder}

class DSLResponse extends ActionResponse with ToXContentFragment {
  override def toXContent(builder: XContentBuilder, params: ToXContent.Params): XContentBuilder = {
    builder
  }
}
