package app.knowledge4retail

enum class Profile(
    val oauthTokenEndpoint: String,
    val oauthUsername: String,
    val oauthPassword: String,
    val oauthClientId: String,
    val knowrobOauthEndpoint: String,
    val knowrobX509Endpoint: String,
    val dtApiOauthEndpoint: String,
    val dtApiX509Endpoint: String,
    val distanceMatrixX509Endpoint: String
) {
    SANDBOX(
        oauthTokenEndpoint = System.getenv("E2E_TOKEN_ENDPOINT") ?: "https://auth.sandbox.knowledge4retail.org/auth/realms/k4r/protocol/openid-connect/token",
        oauthUsername = System.getenv("E2E_TOKEN_USERNAME") ?:"",
        oauthPassword = System.getenv("E2E_TOKEN_PASSWORD") ?:"",
        oauthClientId = System.getenv("E2E_TOKEN_CLIENTID") ?:"k4r-test",
        knowrobOauthEndpoint = "https://knowrob-oidc.sandbox.knowledge4retail.org/knowrob/api/v1.0/query",
        knowrobX509Endpoint = "https://knowrob.sandbox.knowledge4retail.org/knowrob/api/v1.0/query",
        dtApiOauthEndpoint = "https://dt-api-oidc.sandbox.knowledge4retail.org/k4r/api/v0/stores",
        dtApiX509Endpoint = "https://dt-api.sandbox.knowledge4retail.org/k4r/api/v0/stores",
        distanceMatrixX509Endpoint = "https://distance-matrix-api.sandbox.knowledge4retail.org/distancematrixapi/retrieve/",
    ),
    DEV(
        oauthTokenEndpoint = System.getenv("E2E_TOKEN_ENDPOINT") ?: "https://auth.dev.knowledge4retail.org/realms/k4r/protocol/openid-connect/token",
        oauthUsername = System.getenv("E2E_TOKEN_USERNAME") ?:"",
        oauthPassword = System.getenv("E2E_TOKEN_PASSWORD") ?:"",
        oauthClientId = System.getenv("E2E_TOKEN_CLIENTID") ?:"k4r-test",
        knowrobOauthEndpoint = "https://knowrob-oidc.dev.knowledge4retail.org/knowrob/api/v1.0/query",
        knowrobX509Endpoint = "https://knowrob.sandbox.knowledge4retail.org/knowrob/api/v1.0/query",
        dtApiOauthEndpoint = "https://dt-api-oidc.dev.knowledge4retail.org/k4r/api/v0/stores",
        dtApiX509Endpoint = "https://dt-api.dev.knowledge4retail.org/k4r/api/v0/stores",
        distanceMatrixX509Endpoint = "https://distance-matrix-api.dev.knowledge4retail.org/distancematrixapi/retrieve/",
    );

    companion object {
        const val PROFILE_ENV_VAR = "E2E_PROFILE"
        fun String.toProfile() = Profile.values().single { it.name == this.uppercase() }
    }
}