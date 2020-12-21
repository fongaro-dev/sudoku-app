package com.fongarodev.sudokuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var sudoku = Sudoku()
    var adapter: CellAdapter = CellAdapter(this, sudoku)
    lateinit var recyclerView: RecyclerView
    lateinit var buttonSolve : Button
    lateinit var buttonReset : Button
    lateinit var buttonSeed : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val col = 9

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, col)

        buttonSolve = findViewById(R.id.buttonSolve)
        buttonSolve.setOnClickListener {solveSudoku()}

        buttonReset = findViewById(R.id.buttonReset)
        buttonReset.setOnClickListener {resetSudoku()}

        buttonSeed = findViewById(R.id.buttonSeed)
        buttonSeed.setOnClickListener {seedSudoku()}

    }

    private fun seedSudoku() {
        resetSudoku()

        var newSudoku = Sudoku()

        val seedValuesNum = 15
        for (col in 0 until 9){
            //Selected like the index has the lowest chance of being invalid
            val row = (col / 3) + (3 * (col % 3) )
            val index = col + row * 9
            var randomInteger : Int
            do {
                randomInteger = (1..9).shuffled().first()
            } while (!newSudoku.isValid(randomInteger, index))
            newSudoku.addValue(randomInteger, index)
        }

        newSudoku = newSudoku.solution()

        println("Solved sudoku: \n${newSudoku.toString()}")

        val indicesList : MutableList<Int> = (0 until  9*9).toMutableList()
        for(i in 0 until seedValuesNum){
            indicesList.shuffle()
            val curIndex = indicesList.first()
            indicesList.removeAt(0)

            println("Seeding #$i at index $curIndex, list has ${indicesList.size} elements")

            val etCell : EditText = recyclerView.getChildAt(curIndex).findViewById(R.id.etCell)
            etCell.setText(newSudoku.values[curIndex].toString())
        }
    }

    private fun solveSudoku() {
        val solution = sudoku.solution()
        for(i in solution.values.indices) {
            val etCell : EditText = recyclerView.getChildAt(i).findViewById(R.id.etCell)
            etCell.setText(solution.values[i].toString())
        }
        println("Sudoku solved")
    }

    private fun resetSudoku() {
        for(i in sudoku.values.indices) {
            val etCell : EditText = recyclerView.getChildAt(i).findViewById(R.id.etCell)
            etCell.setText("")
        }
    }
}