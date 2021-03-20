package com.hectormorales.colorescabinalacado2.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.hectormorales.colorescabinalacado2.databinding.InputViewBinding

class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    val binding: InputViewBinding

    private var colourCodeTextWatcher: InternalTextWatcher? = null
    private var hangersAmountTextWatcher: InternalTextWatcher? = null
    private var observationsTextWatcher: InternalTextWatcher? = null

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        binding = InputViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun bind(model: InputUiModel) {
        with(binding) {
            colourCodeTextWatcher?.let { editTextColour.removeTextChangedListener(it) }
            editTextColour.setText(model.colourCode)
            editTextColour.placeCursorToEnd()
            colourCodeTextWatcher?.let { editTextColour.addTextChangedListener(it) }

            textViewChangeStartTimeLabel.text = model.changeStartTime
            textViewColourStartTimeLabel.text = model.colourStartTime
            textViewColourEndTimeLabel.text = model.colourEndTime

            hangersAmountTextWatcher?.let { editTextHangersAmount.removeTextChangedListener(it) }
            editTextHangersAmount.setText(model.hangersAmount)
            editTextHangersAmount.placeCursorToEnd()
            hangersAmountTextWatcher?.let { editTextHangersAmount.addTextChangedListener(it) }

            observationsTextWatcher?.let { editTextObservations.removeTextChangedListener(it) }
            editTextObservations.setText(model.observations)
            editTextObservations.placeCursorToEnd()
            observationsTextWatcher?.let { editTextObservations.addTextChangedListener(it) }
        }
    }

    fun setColourCodeTextWatcher(colourCodeTextWatcher: (String) -> Unit) {
        this.colourCodeTextWatcher = InternalTextWatcher(colourCodeTextWatcher)
    }

    fun setHangersAmountTextWatcher(hangersAmountTextWatcher: (String) -> Unit) {
        this.hangersAmountTextWatcher = InternalTextWatcher(hangersAmountTextWatcher)
    }

    fun setObservationsTextWatcher(observationsTextWatcher: (String) -> Unit) {
        this.observationsTextWatcher = InternalTextWatcher(observationsTextWatcher)
    }

    private fun EditText.placeCursorToEnd() {
        this.setSelection(this.text.length)
    }

    inner class InternalTextWatcher(private var listener: ((String) -> Unit)?) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing*/ }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { /* Nothing*/ }

        override fun afterTextChanged(s: Editable) { listener?.invoke(s.toString()) }
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