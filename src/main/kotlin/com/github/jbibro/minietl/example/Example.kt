package com.github.jbibro.minietl.example

import com.github.jbibro.minietl.MiniEtl
import java.io.File

fun main() {
    MiniEtl
        .using(File("example/configuration.yaml"))
        .transform(File("example/orders.csv"))
        .forEach { println(it) }
}