package com.hectormorales.colorescabinalacado2.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hectormorales.colorescabinalacado2.databinding.InputViewBinding

class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    val binding: InputViewBinding

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        binding = InputViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun bind(model: InputUiModel) {
        with(binding) {
            editTextColour.setText(model.colourCode)
            textViewChangeStartTimeLabel.text = model.changeStartTime
            textViewColourStartTimeLabel.text = model.colourStartTime
            textViewColourEndTimeLabel.text = model.colourEndTime
            editTextHangersAmount.setText(model.hangersAmount)
            editTextObservations.setText(model.observations)
        }
    }
}

// Clase "InputRecord" en Python
data class InputUiModel(
    val colourCode: String,
    val changeStartTime: String,
    val colourStartTime: String,
    val colourEndTime: String,
    val hangersAmount: String,
    val observations: String
)