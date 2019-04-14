package com.github.jbibro.minietl

data class Configuration(
    val sourceColumns: List<SourceColumn>,
    val targetColumns: List<TargetColumn>
)

data class SourceColumn(
    val name: String,
    val pattern: String
)

data class TargetColumn(
    val name: String,
    val type: Type,
    val value: String
)

enum class Type {
    INT, STRING, STRING_PROPER_CASED, DATE, BIG_DECIMAL
}