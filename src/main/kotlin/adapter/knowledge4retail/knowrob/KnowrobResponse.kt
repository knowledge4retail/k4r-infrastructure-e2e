package adapter.knowledge4retail.knowrob

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

//{"query": "A=1", "maxSolutionCount": 1000000, "response": [{"A": 1}]}

@Serializable
data class KnowrobResponse (
    val query: String,
    val maxSolutionCount: Int,
    val response: List<Map<String, Int>>
)