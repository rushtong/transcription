package org.rushton.transcription

import java.io.File

fun main(args: Array<String>) {
    val sequences = arrayOf(
            "ATGAGGAGACGGCGATAA",
            "AAAAAAAA",
            "ATGAGGAGACGGCGA",
            "AAAAAAATGGCTGCCGCAGCGTAGAAAAAAA",
            "ATGAGGAGACGGCGXTAA")

    sequences.forEach {
        println("Processing Sequence: ${it}")
        println("\t${Transcriber.transcribe(it)}")
    }
}

object Transcriber {

    private val codonTable: List<AminoAcid> = parseCodonFile()
    private val codonMap: Map<String, String> = codonTable.flatMap { it.codons.map { codon -> codon to it.triName } }.toMap()
    private val stopCodons: List<String> = codonMap.filterValues { it.equals("Stop") }.keys.toList()
    private val startCodon: String = codonMap.filterValues{ it.equals("Start") }.keys.toList().get(0)

    /**
     * Takes a string of ACTG characters as input
     *
     * 1. read the DNA string until the START codon is read.
     * 2. for each codon, translate the codon into its triName amino acid.
     * 3. continue reading until the DNA string ends or the STOP codon is read.
     *
     * Error Conditions:
     * 1. No sequence
     * 2. No start codon
     * 3. No stop codon
     * 4. No reading codons between start and stop
     *
     */
    fun transcribe(sequence: String): String {

        val readingSequence: String = findReadSequence(sequence)
        if (readingSequence.isEmpty()) {
            return "Never Starts: ${sequence}"
        }

        // Batch the remainder into groups of 3 letter strings so we can iterate
        val codonBatches: List<String> = readingSequence.
                split("").
                filterNot { it.isEmpty() }. // Split on "" leaves an empty element at the beginning and end of list
                chunked(3).
                map { l -> l.joinToString("")}. // Re-combine them into 3 letter strings
                toList()

        // Does it stop immediately
        val stopIndex = codonBatches.indexOfFirst { stopCodons.contains(it) }
        if (stopIndex == 0) {
            return "Never Starts: ${sequence}"
        }
        // Are there any stop codons in this batch?
        if (stopIndex == -1) {
            return "Never Stops: ${sequence}"
        }

        val readCodons = codonBatches.subList(0, stopIndex)

        val triStrings = readCodons.map { codonMap.get(it) }.filterNot { it == null }
        if (triStrings.size == readCodons.size) {
            return triStrings.joinToString(":")
        } else {
            return "Bad codon in string: ${sequence}"
        }
    }

    private fun findReadSequence(sequence: String): String {
        val index = sequence.indexOf(startCodon, 0, true)
        if (index > -1) {
            return sequence.substring(index + startCodon.length).trim()
        } else {
            return ""
        }
    }

    private fun parseCodonFile(): List<AminoAcid> {
        val url = this.javaClass.classLoader.getResource("codonTable.txt")
        val file = File(url.toURI())
        return file.readLines().map { line -> makeAminoAcid(line) }
    }

    private fun makeAminoAcid(line: String): AminoAcid {
        val elements: List<String> = line.trim().split("\\s+".toRegex())
        val proteinName: String = elements.get(0)
        val triName: String = elements.get(1)
        val oneLetterName: String = elements.get(2)
        val codons: List<String> = elements.takeLast(elements.size - 3)
        return AminoAcid(proteinName, triName, oneLetterName, codons)
    }

}
