package com.hectormorales.colores_cabina_lacado_ii_android

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.nio.file.Files.write
import java.nio.file.StandardOpenOption

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pongo todos los botones en "false" menos el de hora inicio cambio.
        change_start_time_button.isEnabled = true
        colour_start_time_button.isEnabled = false
        colour_end_time_button.isEnabled = false

        change_start_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            change_start_time_label.text = dateTime
            change_start_time_button.isEnabled = false
            colour_start_time_button.isEnabled = true
        }

        colour_start_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            colour_start_time_label.text = dateTime
            colour_start_time_button.isEnabled = false
            colour_end_time_button.isEnabled = true
        }

        colour_end_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            colour_end_time_label.text = dateTime
            colour_end_time_button.isEnabled = false
        }

        register_continue_button.setOnClickListener {
            register_and_continue()
        }

        register_stop_button.setOnClickListener(){
            saveFile()
        }
    }
    private fun print_last_record(){
        ultimo_color_fila_1.text = colour_entry.text
        ultimo_hora_inicio_cambio_fila_1.text = change_start_time_label.text
        ultimo_hora_inicio_color_fila_1.text = colour_start_time_label.text
        ultimo_hora_final_color_fila_1.text = colour_end_time_label.text
        ultimo_bastidores_fila_1.text = hangers_entry.text
        ultimo_observaciones_fila_1.text = observations_entry.text
    }

    private fun register_and_continue(){

        // Si color_ok, comprobar_bastidores y comprobar_campos_horas == true entonces podemos registar,
        //si no es así, mostramos un mensaje diciendo que hay errores que corregir.

        if (check_colour() && check_hours() && check_hangers()){
            // Ponemos el lo último que hemos introducido en la pantalla:
            print_last_record()

            // Poner función para guardar esto en un archivo:

            // Copiamos la hora del final_color en el inicio_cambio:
            colour_start_time_label.text = colour_end_time_label.text

            // Borramos todos los labels:
            colour_entry.setText("")
            colour_start_time_label.text = ""
            colour_end_time_label.text = ""
            hangers_entry.setText("")
            observations_entry.setText("")

            // Activamos el botón del inicio del color:
            colour_start_time_button.isEnabled = true

            // Quitamos el error:
            errores.text = ""
        }
        else{
            errores.text = "No furula"

        }
    }

    private fun check_hangers():Boolean {
        return hangers_entry.text.toString().isNotEmpty()
    }

    private fun check_hours():Boolean{
        return (change_start_time_label.text.toString().isNotEmpty() ||
                colour_start_time_label.text.toString().isNotEmpty() ||
                colour_end_time_label.text.toString().isNotEmpty())
    }

    private fun check_colour():Boolean{
        return colour_entry.text.toString().isNotEmpty()
    }

    fun check_colour_in_list(){

    }

    fun saveFile() {
        val hola = Environment.getStorageDirectory()
        val nombreArchivo = Environment.DIRECTORY_DOWNLOADS.toString() + "/" + "test.csv"
        Toast.makeText(this, "Guardando en $nombreArchivo", Toast.LENGTH_SHORT).show()
        val file = File(nombreArchivo)
        if (!file.exists()) {
            file.createNewFile()
        }
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write("Hola")// <-- Aquí el contenido
        bufferedWriter.close()
    }
}