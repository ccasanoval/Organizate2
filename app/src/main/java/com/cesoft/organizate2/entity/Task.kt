package com.cesoft.organizate2.entity

object Task {
    val ID_NIL = 0//Int.None
    val NO_SUPER = 0//Int.None
    const val LEVEL1 = 0
    const val LEVEL2 = 1
    const val LEVEL3 = 2

    fun levelChildOf(level: Int): Int {
        return level + 1
    }
}