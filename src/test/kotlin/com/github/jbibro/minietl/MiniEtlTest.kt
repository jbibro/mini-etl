package com.github.jbibro.minietl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

internal class MiniEtlTest {
    @Test
    fun `transforms csv file`() {
        // when
        val result = MiniEtl
            .using(File(javaClass.classLoader.getResource("configuration.yaml").file))
            .transform(File(javaClass.classLoader.getResource("orders.csv").file))

        // then
        assertThat(result).containsExactlyInAnyOrder(
            mutableMapOf(
                "OrderID" to 1000,
                "OrderDate" to LocalDate.of(2018, 1, 1),
                "ProductId" to "Arugola",
                "ProductName" to "Arugola",
                "Quantity" to 5250.5.toBigDecimal(),
                "Unit" to "kg"
            ),
            mutableMapOf(
                "OrderID" to 1001,
                "OrderDate" to LocalDate.of(2017, 12, 12),
                "ProductId" to "Iceberg lettuce",
                "ProductName" to "Iceberg Lettuce",
                "Quantity" to 500.00.toBigDecimal(),
                "Unit" to "kg"
            )
        )
    }
}