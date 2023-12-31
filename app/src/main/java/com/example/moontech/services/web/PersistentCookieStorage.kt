package com.example.moontech.services.web

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import decode
import editDecodedValues
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.hostIsIp
import io.ktor.http.isSecure
import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PersistentCookieStorage(private val dataStore: DataStore<Preferences>) : CookiesStorage {
    private val key = stringSetPreferencesKey("PersistentCookieStorage")

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val wrappedCookie = cookie.wrap().fillDefaults(requestUrl)
        dataStore.edit { preferences ->
            val newCookies = preferences[key]?.editDecodedValues<CookieWrapper> {
                removeIf { persistedCookie ->
                    persistedCookie.name == cookie.name && persistedCookie.matches(wrappedCookie)
                }
                add(wrappedCookie)
            } ?: setOf(Json.encodeToString(wrappedCookie))
            preferences[key] = newCookies
        }
    }

    override fun close() {
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        dataStore.data.first().let { preferences ->
            val wrappedCookies = preferences[key]?.decode<CookieWrapper>()
                ?.filter { cookie -> cookie.matches(requestUrl) }?.toList()
                ?: listOf()
            return wrappedCookies.map(CookieWrapper::unwrap)
        }
    }

    @Serializable
    data class CookieWrapper(
        val domain: String?,
        val name: String,
        val path: String?,
        val secure: Boolean = true,
        val value: String
    )
}

internal fun Cookie.wrap(): PersistentCookieStorage.CookieWrapper {
    return PersistentCookieStorage.CookieWrapper(
        domain = domain,
        name = name,
        path = path,
        secure = secure,
        value = value
    )
}

internal fun PersistentCookieStorage.CookieWrapper.fillDefaults(requestUrl: Url): PersistentCookieStorage.CookieWrapper {
    var result = this

    if (result.path?.startsWith("/") != true) {
        result = result.copy(path = requestUrl.encodedPath)
    }

    if (result.domain.isNullOrBlank()) {
        result = result.copy(domain = requestUrl.host)
    }

    return result
}

internal fun PersistentCookieStorage.CookieWrapper.unwrap(): Cookie {
    return Cookie(
        name = name,
        domain = domain,
        path = path,
        secure = secure,
        value = value
    )
}

internal fun PersistentCookieStorage.CookieWrapper.matches(requestUrl: Url): Boolean {
    val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
        ?: error("Domain field should have the default value")

    val path = with(path) {
        val current = path ?: error("Path field should have the default value")
        if (current.endsWith('/')) current else "$path/"
    }

    val host = requestUrl.host.toLowerCasePreservingASCIIRules()
    val requestPath = let {
        val pathInRequest = requestUrl.encodedPath
        if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
    }

    if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
        return false
    }

    if (path != "/" &&
        requestPath != path &&
        !requestPath.startsWith(path)
    ) {
        return false
    }

    return !(secure && !requestUrl.protocol.isSecure())
}

internal fun PersistentCookieStorage.CookieWrapper.matches(cookie: PersistentCookieStorage.CookieWrapper): Boolean {
    val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
        ?: error("Domain field should have the default value")

    val path = with(path) {
        val current = path ?: error("Path field should have the default value")
        if (current.endsWith('/')) current else "$path/"
    }

    val host = cookie.domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
        ?: error("Domain field should have the default value")
    val requestPath = with(cookie.path) {
        val current = this ?: error("Path field should have the default value")
        if (current.endsWith('/')) current else "$this/"
    }
    if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
        return false
    }

    if (path != "/" &&
        requestPath != path &&
        !requestPath.startsWith(path)
    ) {
        return false
    }
    return true
}
