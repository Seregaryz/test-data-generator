package com.github.seregaryz.testdatagenerator.action.data_analyze

object Constants {

    val STOP_WORDS: Array<String> = arrayOf("package", "\n", "import", "os", "io", "parcelize", "Parcelize")

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
        "List",
        "MutableList",
        "ArrayList",
        "Array",
        "LinkedList",
        "HashMap",
        "MutableMap",
        "Map",
        "TreeMap",
        "Set",
        "HashSet",
        "SortedSet",
        "TreeSet",
        "Queue",
        "Stack",
        "Char",
        "CharArray",
        "null",
        "?"
    )
}