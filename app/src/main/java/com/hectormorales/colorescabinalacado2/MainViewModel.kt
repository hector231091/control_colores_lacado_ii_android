package com.hectormorales.colorescabinalacado2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hectormorales.colorescabinalacado2.view.InputUiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    val inputUiModelObservable = MutableLiveData<InputUiModel>()
    val changeStartEnableObservable = MutableLiveData<Boolean>()
    val colourStartEnableObservable = MutableLiveData<Boolean>()
    val colourEndEnableObservable = MutableLiveData<Boolean>()

    fun onStart() {
        // Pongo todos los botones en "false" menos el de hora inicio cambio.
        changeStartEnableObservable.value = true
        colourStartEnableObservable.value = false
        colourEndEnableObservable.value = false
        inputUiModelObservable.value = InputUiModel("", "", "", "", "", "")
    }

    fun onChangeStartButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(changeStartTime = dateTime)
        }
        changeStartEnableObservable.value = false
        colourStartEnableObservable.value = true
        colourEndEnableObservable.value = false
    }

    fun onColourStartButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(colourStartTime = dateTime)
        }
        changeStartEnableObservable.value = false
        colourStartEnableObservable.value = false
        colourEndEnableObservable.value = true
    }

    fun onColourEndButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(colourEndTime = dateTime)
        }
        changeStartEnableObservable.value = false
        colourStartEnableObservable.value = false
        colourEndEnableObservable.value = false
    }

    private fun getFormattedDateTime() =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy"))
}