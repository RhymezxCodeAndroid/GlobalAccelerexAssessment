package com.e.globalaccelerexassessment.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson


class CardInfoPage {
    var type: String? = null
    var scheme: String? = null
    var prepaid: Boolean? = null
    var number: CardNumberInfo? = null
    var bank: CardBankInfo? = null
    var country: CardCountryInfo? = null

    class Deserializer : ResponseDeserializable<CardInfoPage> {
        override fun deserialize(content: String) =
            Gson().fromJson(content, CardInfoPage::class.java)
    }

}

