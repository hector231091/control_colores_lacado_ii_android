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
        inputUiModelObservable.value = InputUiModel("", "", "", "", "", "")
        updateButtonActivation()
    }

    fun onChangeStartButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(changeStartTime = dateTime)
        }
        updateButtonActivation()
    }

    fun onColourStartButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(colourStartTime = dateTime)
        }
        updateButtonActivation()
    }

    fun onColourEndButtonClick() {
        val dateTime = getFormattedDateTime()
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(colourEndTime = dateTime)
        }
        updateButtonActivation()
    }

    fun onColourInput(colour: String) {
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(colourCode = colour)
        }
    }

    fun onHangersAmountInput(hangersAmount: String) {
        val i=0
    }

    fun onObservationsInput(observations: String) {
        val i=0
    }

    fun onRegisterAndContinueButtonClick() {
//        if (checkAllNecessaryInputData()) {
//            // Ponemos lo último que hemos introducido en la pantalla:
//            populateHistorical(HistoricalUiModel(emptyList()))
//
//            // Poner función para guardar esto en un archivo:
//            writeDataInStorage()

            // Copiamos la hora del final_color en el inicio_cambio:
            val colourEndTime = inputUiModelObservable.value!!.colourEndTime
            inputUiModelObservable.value = InputUiModel("", colourEndTime, "", "", "", "")
            updateButtonActivation()
//        }
    }

    fun onRegisterAndBreakButtonClick() {
        TODO("Not yet implemented")
    }

    fun onRegisterAndFinishButtonClick() {
        TODO("Not yet implemented")
    }

    // Actualiza los botones Inicio Cambio, Inicio Color y Final Color dependiendo si están vacíos o no
    private fun updateButtonActivation() {
        inputUiModelObservable.value?.let {
            changeStartEnableObservable.value =
                it.changeStartTime.isEmpty()

            colourStartEnableObservable.value =
                it.changeStartTime.isNotEmpty() && it.colourStartTime.isEmpty()

            colourEndEnableObservable.value =
                it.changeStartTime.isNotEmpty() && it.colourStartTime.isNotEmpty() && it.colourEndTime.isEmpty()
        }
    }

    private fun getFormattedDateTime() =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy"))
}