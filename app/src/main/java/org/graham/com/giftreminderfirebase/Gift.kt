package org.graham.com.giftreminderfirebase

data class Gift(val name: String, val cost: String, val dateOfPurchase: String) {
    constructor() : this("", "", "")
}