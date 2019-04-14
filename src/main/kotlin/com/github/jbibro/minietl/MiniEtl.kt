package com.github.jbibro.minietl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.jbibro.minietl.Type.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MiniEtl(private val configuration: Configuration) {

    companion object {
        fun using(configurationFile: File) = MiniEtl(readConfiguration(configurationFile))

        private fun readConfiguration(file: File) =
            ObjectMapper(YAMLFactory())
                .registerKotlinModule()
                .readValue<Configuration>(file)
    }

    fun transform(file: File) = extract(file).map { transform(it) }

    private fun extract(file: File) =
        CSVFormat.DEFAULT.withHeader().parse(file.bufferedReader())
            .filter { record ->
                configuration.sourceColumns.all { column ->
                    record.isMapped(column.name) && record[column.name].matches(column.pattern.toRegex())
                }
            }

    private fun transform(record: CSVRecord): MutableMap<String, Any> {
        val newRecord = mutableMapOf<String, Any>()
        configuration.targetColumns.map { targetColumn ->
            newRecord[targetColumn.name] = convert(
                type = targetColumn.type,
                value = targetColumn.value.replace("#\\{[^{}]*}".toRegex()) {
                    record[it.value.replace("[{}#]".toRegex(), "")]
                }
            )
        }
        return newRecord
    }

    private fun convert(type: Type, value: String): Any =
        when (type) {
            INT -> value.toInt()
            STRING -> value
            STRING_PROPER_CASED -> value.split(" ").joinToString(" ") { it.capitalize() }
            DATE -> LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-M-d"))
            BIG_DECIMAL -> value.replace(",", "").toBigDecimal()
        }
}
