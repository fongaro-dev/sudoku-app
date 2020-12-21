package com.fongarodev.sudokuapp

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView


class CellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val etCell : EditText = itemView.findViewById<EditText>(R.id.etCell)
    val topLine : View = itemView.findViewById(R.id.topLine)
    val bottomLine : View = itemView.findViewById(R.id.bottomLine)
    val startLine : View = itemView.findViewById(R.id.startLine)
    val endLine : View = itemView.findViewById(R.id.endLine)
}