package com.hectormorales.colores_cabina_lacado_ii_android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pongo todos los botones en "false" menos el de hora inicio cambio.
        change_start_time_button.setEnabled(true)
        colour_start_time_button.setEnabled(false)
        colour_end_time_button.setEnabled(false)

        change_start_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            change_start_time_label.text = dateTime
            change_start_time_button.setEnabled(false)
            colour_start_time_button.setEnabled(true)
        }

        colour_start_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            colour_start_time_label.text = dateTime
            colour_start_time_button.setEnabled(false)
            colour_end_time_button.setEnabled(true)
        }

        colour_end_time_button.setOnClickListener{
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            colour_end_time_label.text = dateTime
            colour_end_time_button.setEnabled(false)
        }

        register_continue_button.setOnClickListener {
            register_and_continue()
        }
    }
    fun print_last_record(){
        ultimo_color_label.text = colour_entry.text
        ultimo_hora_inicio_cambio_label.text = change_start_time_label.text
        ultimo_hora_inicio_color_label.text = colour_start_time_label.text
        ultimo_hora_final_color_label.text = colour_end_time_label.text
        ultimo_bastidores_label.text = hangers_entry.text
        ultimo_observaciones_label.text = observations_entry.text
    }

    fun register_and_continue(){

        // Si color_ok, comprobar_bastidores y comprobar_campos_horas == true entonces podemos registar,
        //si no es así, mostramos un mensaje diciendo que hay errores que corregir.

        if (check_colour() == true && check_hours() == true && check_hangers() == true ){
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
            colour_start_time_button.setEnabled(true)

            // Quitamos el error:
            errores.setText("")
        }
        else{
            errores.setText("No furula")
        }
    }

    fun check_hangers():Boolean {
        return hangers_entry.getText().toString().isNotEmpty()
    }

    fun check_hours():Boolean{
        return (change_start_time_label.getText().toString().isNotEmpty() ||
                colour_start_time_label.getText().toString().isNotEmpty() ||
                colour_end_time_label.getText().toString().isNotEmpty())
    }

    fun check_colour():Boolean{
        return colour_entry.getText().toString().isNotEmpty()
    }

    fun check_colour_in_list(){

    }
}