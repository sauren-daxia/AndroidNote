/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.jasonchen.mysdk.net.cookie.cache

import okhttp3.Cookie
import java.util.*

/**
 * This class decorates a Cookie to re-implements equals() and hashcode() methods in order to identify
 * the cookie by the following attributes: name, domain, path, secure & hostOnly.
 *
 *
 *
 * This new behaviour will be useful in determining when an already existing cookie in session must be overwritten.
 */
internal class IdentifiableCookie(val cookie: Cookie) {

    override fun equals(other: Any?): Boolean {
        if (other !is IdentifiableCookie) return false
        val that = other as IdentifiableCookie?
        return (that!!.cookie.name() == this.cookie.name()
                && that.cookie.domain() == this.cookie.domain()
                && that.cookie.path() == this.cookie.path()
                && that.cookie.secure() == this.cookie.secure()
                && that.cookie.hostOnly() == this.cookie.hostOnly())
    }

    override fun hashCode(): Int {
        var hash = 17
        hash = 31 * hash + cookie.name().hashCode()
        hash = 31 * hash + cookie.domain().hashCode()
        hash = 31 * hash + cookie.path().hashCode()
        hash = 31 * hash + if (cookie.secure()) 0 else 1
        hash = 31 * hash + if (cookie.hostOnly()) 0 else 1
        return hash
    }

    companion object {

        fun decorateAll(cookies: Collection<Cookie>): List<IdentifiableCookie> {
            val identifiableCookies = ArrayList<IdentifiableCookie>(cookies.size)
            for (cookie in cookies) {
                identifiableCookies.add(IdentifiableCookie(cookie))
            }
            return identifiableCookies
        }
    }
}
