package com.hectormorales.colorescabinalacado2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hectormorales.colorescabinalacado2.view.InputUiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.exitProcess

class MainViewModel(private val validator: Validator) : ViewModel() {

    val inputUiModelObservable = MutableLiveData<InputUiModel>()
    val changeStartEnableObservable = MutableLiveData<Boolean>()
    val colourStartEnableObservable = MutableLiveData<Boolean>()
    val colourEndEnableObservable = MutableLiveData<Boolean>()
    val errorMessageObservable = MutableLiveData<String>()

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
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(hangersAmount = hangersAmount)
        }
    }

    fun onObservationsInput(observations: String) {
        val currentInputUiModel = inputUiModelObservable.value
        currentInputUiModel?.let {
            inputUiModelObservable.value = it.copy(observations = observations)
        }
    }

    fun onRegisterAndContinueButtonClick() {
        when (validator.validate(inputUiModelObservable.value!!)) {
            Validator.Validation.Success -> {
                writeRegisterInStorage(inputUiModelObservable.value!!)

                // Copiamos la hora del final_color en el inicio_cambio:
                val colourEndTime = inputUiModelObservable.value!!.colourEndTime
                inputUiModelObservable.value = InputUiModel("", colourEndTime, "", "", "", "")
                updateButtonActivation()
            }

            Validator.Validation.Failure.InvalidColour ->
                errorMessageObservable.value = "El color introducido no es correcto."

            Validator.Validation.Failure.InvalidHours ->
                errorMessageObservable.value = "Debes introducir las tres horas."

            Validator.Validation.Failure.InvalidHangers ->
                errorMessageObservable.value = "El campo de bastidores debe estar relleno."
        }
    }

    fun onRegisterAndBreakButtonClick() {
        when (validator.validate(inputUiModelObservable.value!!)) {
            Validator.Validation.Success -> {
                writeRegisterInStorage(inputUiModelObservable.value!!)

                inputUiModelObservable.value = InputUiModel("", "", "", "", "", "")
                updateButtonActivation()
            }

            Validator.Validation.Failure.InvalidColour ->
                errorMessageObservable.value = "El color introducido no es correcto."

            Validator.Validation.Failure.InvalidHours ->
                errorMessageObservable.value = "Debes introducir las tres horas."

            Validator.Validation.Failure.InvalidHangers ->
                errorMessageObservable.value = "El campo de bastidores debe estar relleno."
        }
    }

    fun onRegisterAndFinishButtonClick() {
        when (validator.validate(inputUiModelObservable.value!!)) {
            Validator.Validation.Success -> {
                writeRegisterInStorage(inputUiModelObservable.value!!)

                exitProcess(0)
            }

            Validator.Validation.Failure.InvalidColour ->
                errorMessageObservable.value = "El color introducido no es correcto."

            Validator.Validation.Failure.InvalidHours ->
                errorMessageObservable.value = "Debes introducir las tres horas."

            Validator.Validation.Failure.InvalidHangers ->
                errorMessageObservable.value = "El campo de bastidores debe estar relleno."
        }
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

    private fun writeRegisterInStorage(model: InputUiModel) {
        // Poner función para guardar esto en un archivo:
        // Internamente deberá escribir el último registro en el historial

        // Guardar en base de datos y volcar a fichero bajo demanda?
    }
}