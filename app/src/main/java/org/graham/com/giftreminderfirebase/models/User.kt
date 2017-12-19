package org.graham.com.giftreminderfirebase.models

data class User(val email: String, val userUid: String) {
    constructor() : this("", "")
}
