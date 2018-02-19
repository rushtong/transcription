# Kotlin Transcription
A sample kotlin project for fun

## See:
Adapted from:
[http://www.cse.msu.edu/~cse231/PracticeOfComputingUsingPython/08_ClassDesign/Transcription/](http://www.cse.msu.edu/~cse231/PracticeOfComputingUsingPython/08_ClassDesign/Transcription/) 


## Requirements

Create a class, called Transcriber, which will do the transcription process. It is coded as follows:

1. The constructor of the class takes a single argument, a filename from which can be read the 
    amino acid to codon information. The table provided, called `codonTable.txt`, has the 
    following format:
    - each line represents an amino acid or transcription command
    - the line format has space separated fields, which are in order:
        ```
        proteinName triName oneLetterName listOfCodons
        ```
2. You will provide a method called transcribe.
    - It takes a single argument, a string of DNA bases (a string of ATGC characters)
    - It yields a string of triName amino acids, separated by ‘:’
    - Transcribe works as follows:
        - read the DNA string until the START codon is read.
        - for each codon, translate the codon into its triName amino acid.
        For example, if you read the codon CAG, you can see from the table entry:
        ```
        Glutamine Glu Q CAA CAG
        ```
        That the codon CAG translates to the `triName` Glu.
        - continue reading until the DNA string ends or the STOP codon is read.
3. One odd thing. In the common biological translation (that is, using the provided table), 
    the START codon is the same as the Methionine (met) codon.. When reading begins, that codon 
    codes for START. After transcription begins, that codon codes for Methionine (met).
4. Your transcriber should print an error if:
    - it does not read a codon from the string (that is, there are less than three DNA bases at 
        the end of the string if transcription is still in process)
    - if the DNA string ends and transcription never began
    - if the DNA string ends and transcription never ended