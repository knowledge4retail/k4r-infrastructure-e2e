package adapter.knowledge4retail

import app.knowledge4retail.Profile

data class Knowledge4RetailClientConfig(
    val userName: String,
    val userPassword: String,
    val clientId: String,
    val tokenEndpoint: String
) {
    init {
        require(userName.isNotBlank())
        require(userPassword.isNotBlank())
        require(clientId.isNotBlank())
        require(tokenEndpoint.isNotBlank())
    }

    companion object {
        fun getForProfile(profile: Profile, userName: String? = null, password: String? = null) = Knowledge4RetailClientConfig(
            userName = userName?: profile.oauthUsername,
            userPassword = password?: profile.oauthPassword,
            clientId = profile.oauthClientId,
            tokenEndpoint = profile.oauthTokenEndpoint
        )
    }
}