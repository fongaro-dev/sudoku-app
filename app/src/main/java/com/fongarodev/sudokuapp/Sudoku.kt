package com.fongarodev.sudokuapp

import kotlin.String as String

class Sudoku() {
    var values = Array<Int>(9 * 9) {0}

    // These are hot vectors first with logic [row]value
    var valuesInRows = Array<Array<Int>>(9) {Array<Int>(9) { 0 } }
    var valuesInCols = Array<Array<Int>>(9) {Array<Int>(9) { 0 } }
    var valuesInBlock = Array<Array<Array<Int>>> (3) {Array<Array<Int>>(3) {Array<Int>(9) { 0 } } }

    fun addValue(value: Int, index: Int) {
        val col = index % 9
        val row = index / 9

        val inBounds = {input : Int -> (input > 0) and (input <10)}

        //This check is essentially because all values are 0 at startup
        if(inBounds(values[index])){//Reset values if user enters 0
            val prevVal = values[index]
            valuesInRows[row][prevVal - 1]--
            valuesInCols[col][prevVal - 1]--
            valuesInBlock[row / 3][col / 3][prevVal - 1]--
        }

        if ( inBounds (value) ) {
            values[index] = value
            valuesInRows[row][value - 1]++
            valuesInCols[col][value - 1]++
            valuesInBlock[row / 3][col / 3][value - 1]++
        } else {
            values[index] = 0
        }

    }

    fun isValid(value: Int, index: Int): Boolean {
        if((value <= 0) or (value > 10)) {return false}
        if((index < 0) or (index >= 9 * 9)){return false}

        val col = index % 9
        val row = index / 9

        val numInRow = this.valuesInRows[row][value - 1]
        val numInCol = this.valuesInCols[col][value - 1]
        val numInBlock = this.valuesInBlock[row / 3][col / 3][value - 1]

        if((numInRow < 0) or (numInCol < 0) or (numInBlock < 0) ){
            error("numInRow is $numInRow, numInCol is $numInCol and numInBlock is $numInBlock")
        }

        val numInstances : Int
        //If this is the value that is already in this cell
        if(values[index] == value) {
            numInstances = 1
        } else {
            numInstances = 0
        }
        return ( (numInRow == numInstances) and (numInCol == numInstances) and (numInBlock == numInstances) )
    }

    override fun toString() : String
    {
        var ret = ""
        for(index in 0 until 9*9) {
            if ( (index > 0) and (index % 9 == 0) ) ret += "\n"
            val v = values[index]
            ret = "$ret{$v}"
        }
        return ret
    }

    fun isFilled() : Boolean
    {
        var filled = true
        for (index in 0 until 9*9) {
            if ( (values[index] < 1) or (values[index] > 9) ) {
                filled = false
                break
            }
        }
        return  filled
    }

    fun clone() : Sudoku
    {
        val cloned = Sudoku()

        for (index in 0 until 9 * 9) {
            cloned.values[index] = this.values[index]
        }

        for (row in 0 until 9){
            for(col in 0 until 9){
                for(value in 0 until 9){
                    cloned.valuesInRows[row][value] = this.valuesInRows[row][value]
                    cloned.valuesInCols[col][value] = this.valuesInCols[col][value]
                    cloned.valuesInBlock[row / 3][col / 3][value] = this.valuesInBlock[row / 3][col / 3][value]
                }
            }
        }
        return cloned
    }

    fun hasSolution() : Boolean {
        val solution = fillTable(this, 0)
        return solution.isFilled()
    }

    fun solution() : Sudoku {
        println("About to solve \n${this.toString()}")
        val solution = fillTable(this, 0)
        return solution

    }

    fun getValue(index: Int) : Int {
        return values[index]
    }
}

fun fillTable(input : Sudoku, index : Int) : Sudoku {
    var output = input.clone()
    if (index < 9*9) {
//        println("Processing index: $index of value: ${input.values[index]}")
        if (output.values[index] == 0) {
            var valid = false
            for (value in 1..9) {
                output = input.clone()
                valid = output.isValid(value, index)
//                println("    value $value, valid is $valid ")
                if (valid) {
                    output.addValue(value, index)
                    output = fillTable(output, index + 1)

                    if (output.isFilled()) {
                        return output
                    } else {
                        valid = false
                    }
                }
            }
            if ( !valid ) {
                return Sudoku()
            }
        }
        return fillTable(output, index + 1)
    } else {
        return output
    }
}