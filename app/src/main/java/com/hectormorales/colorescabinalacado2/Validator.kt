package com.hectormorales.colorescabinalacado2

import com.hectormorales.colorescabinalacado2.view.InputUiModel

class Validator {

    fun validate(model: InputUiModel): Validation {
        val colourValidation = validateColour(model.colourCode)
        if (colourValidation != Validation.Success) {
            return colourValidation
        }

        val hoursValidation =
            validateHours(model.changeStartTime, model.colourStartTime, model.colourEndTime)
        if (hoursValidation != Validation.Success) {
            return hoursValidation
        }

        val hangersValidation = validateHangers(model.hangersAmount)
        if (hangersValidation != Validation.Success) {
            return hangersValidation
        }

        return Validation.Success
    }

    private fun validateColour(colourCode: String): Validation {
        // TODO solución temporal, cargar colores de fichero
        val colourList = (40100000..40100400).map { it.toString() }.toMutableList()
        colourList.add("FIN")

        val isValid = colourCode.isNotEmpty() && colourCode in colourList
        return if (isValid) Validation.Success else Validation.Failure.InvalidColour
    }

    private fun validateHours(
        changeStartTime: String,
        colourStartTime: String,
        colourEndTime: String
    ): Validation {
        val isValid =
            changeStartTime.isNotEmpty() && colourStartTime.isNotEmpty() && colourEndTime.isNotEmpty()
        return if (isValid) Validation.Success else Validation.Failure.InvalidHours
    }

    private fun validateHangers(hangersAmount: String): Validation {
        val isValid = hangersAmount.isNotEmpty()
        return if (isValid) Validation.Success else Validation.Failure.InvalidHangers
    }

    sealed class Validation {

        object Success : Validation()

        sealed class Failure : Validation() {

            object InvalidColour : Failure()

            object InvalidHours : Failure()

            object InvalidHangers : Failure()
        }
    }
}