package com.fongarodev.sudokuapp

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class CellAdapter(var context: Context, var sudo: Sudoku) : RecyclerView.Adapter<CellHolder>() {
    lateinit var mRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellHolder {
        mRecyclerView = parent as RecyclerView

        val cellItem = LayoutInflater.from(context).inflate(R.layout.cell, parent, false)
        return CellHolder(cellItem)
    }

    override fun getItemCount(): Int {
        return 9*9
    }

    override fun onBindViewHolder(holder: CellHolder, position: Int) {

        val col = position % 9
        val row = position / 9

        if (col % 3 == 0) {
            print("Increasing startLine at ($col, $row)")
            holder.startLine.setBackgroundColor(Color.BLACK)
            holder.startLine.visibility = View.VISIBLE
        } else if (col % 3 == 2) {
            print("Increasing endLine at ($col, $row)")
            holder.endLine.setBackgroundColor(Color.BLACK)
        }

        if(row % 3 == 0) {
            print("Increasing topLine at ($col, $row)")
            holder.topLine.setBackgroundColor(Color.BLACK)
            holder.topLine.visibility = View.VISIBLE
        } else if(row % 3 == 2) {
            print("Increasing bottomLine at ($col, $row)")
            holder.bottomLine.setBackgroundColor(Color.BLACK)
        }

        holder.etCell.addTextChangedListener(object : TextWatcher {

            lateinit var before: CharSequence

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                before = s
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val userInput: Int
                if (count == 0) {
                    sudo.addValue(0, position)
                    colorCheck(position)
                    return
                }

                try {
                    userInput = s.toString().toInt()
                } catch (NumberFormatException: Exception) {
                    Toast.makeText(context, "Please insert a numeric value", Toast.LENGTH_LONG).show()
                    holder.etCell.setText(before)
                    return
                }

                if (!inBounds(userInput)) {
                    Toast.makeText(context, "Please insert an integer between 1 and 9", Toast.LENGTH_LONG).show()
                    holder.etCell.setText("")
                    return
                }

                if ((userInput == sudo.getValue(position)) and (userInput != 0)) {
                    holder.etCell.setTextColor(Color.BLUE)
                    return
                }

                val valid = sudo.isValid(userInput, position)
                sudo.addValue(userInput, position)
                if (valid) {//We don't have this value in col, block or row
                    //Check if a sudoku with this value has a solution
                    if (sudo.hasSolution()) {
                        holder.etCell.setTextColor(Color.BLACK)
                    } else {
                        holder.etCell.setTextColor(Color.MAGENTA)
                    }
                } else { //We already have this value in col, row, or block
                    holder.etCell.setTextColor(Color.RED)
                }
                colorCheck(position)
            }
        })
    }

    private fun colorCheck(pos : Int)  {

        val col = pos % 9
        val row = pos / 9

        val blockRow = row / 3
        val blockCol = col / 3

        for (i in 0 until 9) {
            val sameRowIndices = i + row * 9
            val sameColIndices = i * 9 + col

            val sameBlockCols = (blockCol * 3) + (i % 3)
            val sameBlockRows = (blockRow * 3) + (i / 3)
            val sameBlockIndices = sameBlockCols + (sameBlockRows * 9)

            val etCellRow: EditText = mRecyclerView.getChildAt(sameRowIndices).findViewById(R.id.etCell)
            val etCellCol: EditText = mRecyclerView.getChildAt(sameColIndices).findViewById(R.id.etCell)
            val etCellBlock: EditText = mRecyclerView.getChildAt(sameBlockIndices).findViewById(R.id.etCell)

            setCorrectColor(etCellRow, sameRowIndices)
            setCorrectColor(etCellCol, sameColIndices)
            setCorrectColor(etCellBlock, sameBlockIndices)
        }
    }

    private fun setCorrectColor(etCell : EditText, pos : Int) {
        if (etCell.text.isNullOrEmpty()) {
            etCell.setTextColor(Color.BLACK)
            return
        }

        val cellValue: Int
        try {
            cellValue = etCell.text.toString().toInt()
        } catch (NumberFormatException: Exception) {
            return
        }

        if (sudo.isValid(cellValue, pos)) {
            etCell.setTextColor(Color.BLACK)
        } else {
            etCell.setTextColor(Color.RED)
        }
    }

}


fun inBounds(v: Int) : Boolean {
    return (v > 0) and (v < 10)
}