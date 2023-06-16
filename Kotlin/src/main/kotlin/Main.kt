@file:OptIn(ExperimentalTime::class)

import de.m3y.kformat.Table
import de.m3y.kformat.table
import java.io.File
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val trieGPT = TrieGPT()
val trieRay = TrieRay<Char>()
val dataStructures = listOf<MutableCollection<String>>(ArrayList(), LinkedList(), Vector(), HashSet())

val testData = ArrayList<String>()

fun main() {
    populateData()

    searchForTestInput()
}

fun searchForTestInput() {
    println(
        table {
            val types = mutableListOf<String>()
            types.add("Term")
            types.addAll(dataStructures.map { it.getName() })
            types.add(trieRay::class.java.simpleName)
            types.add(trieGPT::class.java.simpleName)

            header(types)

            testData.forEach { line ->
                row(
                    // Input
                    inputRow(line),
                    // Spreads all other results
                    *(dataStructures.map { measureSearch { search(it, line) } }).toTypedArray(),
                    // Trie results
                    measureTime { trieRay.allMatches(line) }.toString(),
                    // Trie results
                    measureTime { trieGPT.searchPrefix(line) }.toString(),
                )
            }

            hints {
                types.forEach {
                    alignment(it, Table.Hints.Alignment.LEFT)
                }
                borderStyle = Table.BorderStyle.SINGLE_LINE
            }
        }.render(StringBuilder())
    )
}

inline fun measureSearch(search: () -> Unit) = measureTime { search() }.toString()

fun search(collection: MutableCollection<String>, input: String) = collection.filter { it.startsWith(input) }

fun inputRow(input: String) = "$input - ${
    if (dataStructures.any { input in it || trieRay.contains(input.toList()) || trieGPT.contains(input) })
        "Found ${dataStructures.map { search(it, input).size }} "
    else "0"
}"

fun populateData() {
    // Populate search queries
    File(Thread.currentThread().contextClassLoader.getResource("testinput")?.file ?: "").readLines()
        .forEach { line ->
            testData.add(line)
        }
    // Populate words lists
    File(
        Thread.currentThread().contextClassLoader.getResource("fullwords")?.file ?: ""
    ).readLines()
        .forEach { line ->
            dataStructures.forEach {
                it.add(line)
            }
            trieRay.insert(line)
            trieGPT.insert(line)
        }
}

fun MutableCollection<String>.getName(): String = this::class.java.simpleName

fun printResults(
    otherTime: Duration,
    trieTime: Duration,
    otherDsType: String
) = println("$otherDsType $otherTime - Trie $trieTime")
