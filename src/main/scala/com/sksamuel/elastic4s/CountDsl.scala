package com.sksamuel.elastic4s

import org.elasticsearch.action.count.CountRequestBuilder
import org.elasticsearch.index.query.QueryBuilders

/** @author Stephen Samuel */
trait CountDsl {

    def count = new CountExpectsIndex
    class CountExpectsIndex {
        def from(indexes: Iterable[String]): CountDefinition = new CountDefinition(indexes.toSeq)
        def from(indexes: String*): CountDefinition = new CountDefinition(indexes)
    }

    class CountDefinition(indexes: Seq[String]) {

        val _builder = new CountRequestBuilder(null).setIndices(indexes: _*).setQuery(QueryBuilders.matchAllQuery())
        def build = _builder.request()

        def query(string: String): CountDefinition = {
            val q = new StringQueryDefinition(string)
            _builder.setQuery(q.builder.buildAsBytes)
            this
        }

        def query(block: => QueryDefinition): CountDefinition = {
            _builder.setQuery(block.builder)
            this
        }

        def types(iterable: Iterable[String]): CountDefinition = types(iterable.toSeq: _*)
        def types(types: String*): CountDefinition = {
            _builder.setTypes(types: _*)
            this
        }
    }

}