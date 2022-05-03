package com.github.seregaryz.testdatagenerator.action.data_analyze

object Constants {

    val STOP_WORDS: Array<String> = arrayOf("package", "\n", "import", "os", "io", "parcelize", "Parcelize")

    val LISTS_TYPES: Array<String> = arrayOf(
        "MutableList",
        "ArrayList",
        "LinkedList",
        "HashSet",
        "SortedSet",
        "TreeSet",
        "Queue",
        "Stack",
        "List",
        "Set",
        "Array"
    )

    val MAP_TYPES: Array<String> = arrayOf("HashMap", "MutableMap", "TreeMap", "Map")

    val PRIMITIVES: Array<String> = arrayOf(
        "String",
        "Int",
        "Double",
        "Float",
        "Number",
        "Byte",
        "Boolean",
        "Short",
        "Long",
        "Char",
        "CharArray",
        "null",
        "?"
    )
}