package com.americancolors.endreverse.ar.models

import java.lang.StringBuilder

class ArColor internal constructor() : ColorData {

    var a: Int? = null
    var b: Int? = null

    var cieA: Float? = null

    var cieB: Float? = null

    var cieL: Float? = null

    var collectionId: String? = null

    var collectionName: String? = null
    var colorDistance = 0.0

    var colorId: String? = null
    var columnNumber: Int? = null

    var displayColumn: Int? = null

    var displayPage: Int? = null

    var displayRow: Int? = null

    var family: String? = null

    var id: Int? = null
    var isFavourite = false

    var luminosity: Int? = null
    var primaryKeyColorId = 0

    var primaryLabel: String? = null

    var productAvailable: Boolean? = null
    var rowNumber: Int? = null

    var schemesDesignerCombinations: List<String>? = null

    var schemesNeutralCombinations: List<String>? = null

    var schemesTonalCombinations: List<String>? = null

    var secondaryLabel: String? = null

    var sensation: String? = null

    var swappable: Swappable? = null

    var testerAvailable: Boolean? = null

    var token: String? = null
    var type: Int? = null

    var uid: String? = null

    var useInColourPicker: Boolean? = null

    init {
        schemesDesignerCombinations = null
        schemesNeutralCombinations = null
        schemesTonalCombinations = null
    }

    fun getRgb(): String? {
        return token
    }

    class Swappable {
        var forProduct: String? = null
        var forTester: String? = null
    }

    override fun type(): Int = 1

    fun parseRGB() {
        var localObject: Any? = token
        if (localObject != null && (localObject as String).length > 0 && token!!.length < 7) {
            localObject = StringBuilder("000000")
            localObject.append(token)
            localObject = localObject.substring(token!!.length)
            val i = localObject!!.substring(0, 2).toInt(16)
            val j = localObject.substring(2, 4).toInt(16)
            val k = localObject.substring(4, 6).toInt(16)
            setR(Integer.valueOf(i))
            setG(Integer.valueOf(j))
            setB(Integer.valueOf(k))
            setLuminosity(Integer.valueOf(((i * 0.2126 + j * 0.7152 + k * 0.0722) * 1000.0).toInt()))
        }
    }

    private fun setR(paramInteger: Int) {
        type = paramInteger
    }

    private fun setG(paramInteger: Int) {
        a = paramInteger
    }

    private fun setB(paramInteger: Int) {
        b = paramInteger
    }

    private fun setLuminosity(paramInteger: Int) {
        luminosity = paramInteger
    }
}