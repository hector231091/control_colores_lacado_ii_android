package com.hectormorales.colorescabinalacado2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hectormorales.colorescabinalacado2.view.HistoricalItem
import com.hectormorales.colorescabinalacado2.view.HistoricalUiModel

class HistoricalViewModel : ViewModel() {

    val historicalListObservable = MutableLiveData<HistoricalUiModel>()

    fun onStart() {
        // Cargar el histórico. El histórico es "observable".
        // Mapearlo a UI model
        // Mostrarlo a través de un LiveData

        // Esto es temporal, la idea es mostrar algo para ver que todo funciona bien, pero
        // hay que cargar el registro y convertirlo a HistoricalItem
        val historicalItems = listOf(
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, "")
        )
        historicalListObservable.value = HistoricalUiModel(historicalItems)
    }
}