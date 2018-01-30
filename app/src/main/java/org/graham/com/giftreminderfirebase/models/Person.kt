package org.graham.com.giftreminderfirebase.models

data class Person(var key: String, val name: String, val birthDate: String, val gift: Gift) {
    constructor() : this("", "", "", Gift("", "", ""))
}