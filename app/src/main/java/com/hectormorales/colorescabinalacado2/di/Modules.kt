package com.hectormorales.colorescabinalacado2.di

import com.hectormorales.colorescabinalacado2.HistoricalViewModel
import com.hectormorales.colorescabinalacado2.MainViewModel
import com.hectormorales.colorescabinalacado2.Validator
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Ya se irá llenando este módulo de dependencias, por ahora se queda vacío
    single { Validator() }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HistoricalViewModel() }
}