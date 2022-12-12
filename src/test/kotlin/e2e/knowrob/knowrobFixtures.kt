package e2e.knowrob
val minimalKnowRobQueryKeyFixture = "A"
val minimalKnowRobQueryValueFixture: Int = 1
val minimalKnowRobQueryFixture = "$minimalKnowRobQueryKeyFixture=$minimalKnowRobQueryValueFixture"

val minimalKnowrobRequestBody: String = """
    {
        "query": "$minimalKnowRobQueryFixture",
        "maxSolutionCount": 1000000
    }
""".trimIndent()