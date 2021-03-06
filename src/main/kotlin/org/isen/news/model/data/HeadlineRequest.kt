package org.isen.news.model.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

/**
 * Response object for top-headline request
 *
 * @property status
 * @property totalResults
 * @property articles
 */
data class HeadlineRequest(
        val status: String,
        val totalResults: Int,
        val articles: List<Article>?
) {
    constructor(): this(
            "",
            0,
            null
    )

    class Deserializer : ResponseDeserializable<HeadlineRequest> {
        override fun deserialize(content: String): HeadlineRequest =
                Gson().fromJson(content, HeadlineRequest::class.java)
    }
}