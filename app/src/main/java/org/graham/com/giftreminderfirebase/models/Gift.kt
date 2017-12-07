package org.graham.com.giftreminderfirebase.models

data class Gift(val name: String, val cost: String, val dateOfPurchase: String) {
    constructor() : this("", "", "")
}