package com.hectormorales.colores_cabina_lacado_ii_android

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Quitar la ActionBar de la aplicación.
        supportActionBar?.hide()

        // Para mantener la pantalla siempre en modo LANDSCAPE.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

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
            register_and_stop()
        }

        register_end_button.setOnClickListener(){
            register_and_end()
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

    private fun check_all_necessary_input_data():Boolean{

        // Variable de ayuda.
        var variable = ""

        // Comprobamos que todos los campos están rellenos.
        if (check_colour() && check_hours() && check_hangers()) {
            variable = ""
        }
        else {
            //errores.text = "No furula"
            if (check_colour() == false) {
                //Snackbar
                //val message = "El color no es correcto."
                //Snackbar.make(findViewById(R.id.colour_entry), message, Snackbar.LENGTH_LONG).show()

                // Hacemos un "AlertDialog".
                val message = "El color introducido no es correcto."
                dialog_alert(message)

            } else if (check_hours() == false) {
                // Hacemos un "AlertDialog".
                val message = "Debes introducir las tres horas."
                dialog_alert(message)

            } else if (check_hangers() == false) {
                // Hacemos un "AlertDialog".
                val message = "El campo de bastidores debe estar relleno."
                dialog_alert(message)
            }
            variable = "1"
        }
        return variable.isEmpty()
    }

    private fun register_and_stop(){

        // Poner un mensaje de que se va a registrar e irse al descanso y si se quiere continuar o no.

        if(check_all_necessary_input_data()){
            // Ponemos el lo último que hemos introducido en la pantalla:
            print_last_record()

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Borramos todos los labels que hay que borrar:
            colour_entry.setText("")
            change_start_time_label.text = ""
            colour_start_time_label.text = ""
            colour_end_time_label.text = ""
            hangers_entry.setText("")
            observations_entry.setText("")

            // Activamos el botón del inicio del cambio:
            change_start_time_button.isEnabled = true
        }
    }

    private fun register_and_end(){

        // Poner un mensaje que avise de que se va a cerrar la aplicación y decidir si continuar o no.

        if(check_all_necessary_input_data()){
            // Ponemos el lo último que hemos introducido en la pantalla:
            print_last_record()

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Borramos todos los labels que hay que borrar:
            colour_entry.setText("")
            change_start_time_label.text = ""
            colour_start_time_label.text = ""
            colour_end_time_label.text = ""
            hangers_entry.setText("")
            observations_entry.setText("")

            // Activamos el botón del inicio del color:
            colour_start_time_button.isEnabled = true

            finish()
            System.exit(0)
        }

    }

    private fun register_and_continue() {

        if(check_all_necessary_input_data()){
            // Ponemos el lo último que hemos introducido en la pantalla:
            print_last_record()

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Copiamos la hora del final_color en el inicio_cambio:
            colour_start_time_label.text = colour_end_time_label.text

            // Borramos todos los labels que hay que borrar:
            colour_entry.setText("")
            colour_start_time_label.text = ""
            colour_end_time_label.text = ""
            hangers_entry.setText("")
            observations_entry.setText("")

            // Activamos el botón del inicio del color:
            colour_start_time_button.isEnabled = true
        }
    }

    private fun write_data_in_storage() {

        // Creamos una variable String con todos los datos.
        val data = colour_entry.getText().toString() + ";" +
                change_start_time_label.text.toString() +  ";" +
                colour_start_time_label.text.toString() +  ";" +
                colour_end_time_label.text.toString()+  ";" +
                hangers_entry.getText().toString()+  ";" +
                observations_entry.getText().toString() + "\n"

        // Dirección en la que lo vamos a guardar
        val file_direccion = getExternalFilesDir(null)

        // Creamos una carpeta dentro de la dirección anterior.
        val folder = File(file_direccion, "Datos Lacado")
        // Comprobamos que existe la carpeta. En caso contrario la creamos.
        if(!folder.exists()){
            folder.mkdir()
        }
        // Creamos el fichero y le pasamos los datos que debe almacenar.
        val ficheroFisico = File(folder, "datos.csv")
        ficheroFisico.appendText(data)

        val list_of_historic_data = arrayListOf<String>(colour_entry.getText().toString())
    }

    private fun read_data_in_storage(){

    }

    private fun check_hangers():Boolean {
        return hangers_entry.text.toString().isNotEmpty()
    }

    private fun check_hours():Boolean{
        return (change_start_time_label.text.toString().isNotEmpty() &&
                colour_start_time_label.text.toString().isNotEmpty() &&
                colour_end_time_label.text.toString().isNotEmpty())
    }

    private fun check_colour():Boolean{
        return colour_entry.text.toString().isNotEmpty()
    }

    fun check_colour_in_list(){

    }

    private fun saveFile() {
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

    private fun dialog_alert(message: String){
        val builder = AlertDialog.Builder(this@MainActivity)
        // Título.
        builder.setTitle("ERROR DE REGISTRO")

        // Mensaje a mostrar.
        builder.setMessage(message)

        // Mostrar un botón neutral para cerrar la alerta.
        builder.setNeutralButton("ENTENDIDO"){ _, _ ->
        }

        // Creamos la alerta
        val dialog: AlertDialog = builder.create()

        // Mostramos la alerta.
        dialog.show()
    }
}
