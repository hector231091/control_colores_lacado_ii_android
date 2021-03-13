package com.hectormorales.colores_cabina_lacado_ii_android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hectormorales.colores_cabina_lacado_ii_android.databinding.ActivityMainBinding
import com.hectormorales.colores_cabina_lacado_ii_android.view.HistoricalItem
import com.hectormorales.colores_cabina_lacado_ii_android.view.HistoricalUiModel
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Quitar la ActionBar de la aplicación.
        supportActionBar?.hide()

        // Pongo todos los botones en "false" menos el de hora inicio cambio.
        binding.changeStartTimeButton.isEnabled = true
        binding.colourStartTimeButton.isEnabled = false
        binding.colourEndTimeButton.isEnabled = false

        binding.changeStartTimeButton.setOnClickListener {
            val dateTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            binding.changeStartTimeLabel.text = dateTime
            binding.changeStartTimeButton.isEnabled = false
            binding.colourStartTimeButton.isEnabled = true
        }

        binding.colourStartTimeButton.setOnClickListener {
            val dateTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            binding.colourStartTimeLabel.text = dateTime
            binding.colourStartTimeButton.isEnabled = false
            binding.colourEndTimeButton.isEnabled = true
        }

        binding.colourEndTimeButton.setOnClickListener {
            val dateTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/mm/yy"))
            binding.colourEndTimeLabel.text = dateTime
            binding.colourEndTimeButton.isEnabled = false
        }

        binding.registerContinueButton.setOnClickListener {
            register_and_continue()
        }

        binding.registerStopButton.setOnClickListener() {
            register_and_stop()
        }

        binding.registerEndButton.setOnClickListener() {
            register_and_end()
        }

        // Esto es temporal, la idea es mostrar algo para ver que todo funciona bien, pero
        // hay que cargar el registro y convertirlo a HistoricalItem
        val historicalItems = listOf(
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, "")
        )
        show_data(historicalItems)
    }

    private fun show_data(historicalItems: List<HistoricalItem>) {
        val historicalUiModel = HistoricalUiModel(historicalItems)
        binding.historicalView.bind(historicalUiModel)
    }

    private fun check_all_necessary_input_data(): Boolean {
        // Variable de ayuda.
        var variable = ""

        // Comprobamos que todos los campos están rellenos.
        if (check_colour() && check_hours() && check_hangers()) {
            variable = ""
        } else {
            //errores.text = "No furula"
            if (check_colour() == false) {
                //Snackbar
                //val message = "El color no es correcto."
                //Snackbar.make(findViewById(R.id.binding.colourEntry), message, Snackbar.LENGTH_LONG).show()

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

    private fun register_and_continue() {
        if (check_all_necessary_input_data()) {
            // Ponemos lo último que hemos introducido en la pantalla:
            show_data(emptyList())

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Copiamos la hora del final_color en el inicio_cambio:
            binding.colourStartTimeLabel.text = binding.colourEndTimeLabel.text

            // Borramos todos los labels que hay que borrar:
            binding.colourEntry.setText("")
            binding.colourStartTimeLabel.text = ""
            binding.colourEndTimeLabel.text = ""
            binding.hangersEntry.setText("")
            binding.observationsEntry.setText("")

            // Activamos el botón del inicio del color:
            binding.colourStartTimeButton.isEnabled = true
        }
    }

    private fun register_and_stop() {
        // Poner un mensaje de que se va a registrar e irse al descanso y si se quiere continuar o no.
        if (check_all_necessary_input_data()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            show_data(emptyList())

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Borramos todos los labels que hay que borrar:
            binding.colourEntry.setText("")
            binding.changeStartTimeLabel.text = ""
            binding.colourStartTimeLabel.text = ""
            binding.colourEndTimeLabel.text = ""
            binding.hangersEntry.setText("")
            binding.observationsEntry.setText("")

            // Activamos el botón del inicio del cambio:
            binding.changeStartTimeButton.isEnabled = true
        }
    }

    private fun register_and_end() {

        // Poner un mensaje que avise de que se va a cerrar la aplicación y decidir si continuar o no.

        if (check_all_necessary_input_data()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            show_data(emptyList())

            // Poner función para guardar esto en un archivo:
            write_data_in_storage()

            // Borramos todos los labels que hay que borrar:
            binding.colourEntry.setText("")
            binding.changeStartTimeLabel.text = ""
            binding.colourStartTimeLabel.text = ""
            binding.colourEndTimeLabel.text = ""
            binding.hangersEntry.setText("")
            binding.observationsEntry.setText("")

            // Activamos el botón del inicio del color:
            binding.colourStartTimeButton.isEnabled = true

            finish()
            exitProcess(0)
        }
    }

    private fun write_data_in_storage() {

        // Creamos una variable String con todos los datos.
        val data = binding.colourEntry.getText().toString() + ";" +
                binding.changeStartTimeLabel.text.toString() + ";" +
                binding.colourStartTimeLabel.text.toString() + ";" +
                binding.colourEndTimeLabel.text.toString() + ";" +
                binding.hangersEntry.getText().toString() + ";" +
                binding.observationsEntry.getText().toString() + "\n"

        // Dirección en la que lo vamos a guardar
        val file_direccion = getExternalFilesDir(null)

        // Creamos una carpeta dentro de la dirección anterior.
        val folder = File(file_direccion, "Datos Lacado")
        //Log.i("Dirección: ", folder.toString())
        // Comprobamos que existe la carpeta. En caso contrario la creamos.
        if (!folder.exists()) {
            folder.mkdir()
        }
        // Creamos el fichero y le pasamos los datos que debe almacenar.
        val ficheroFisico = File(folder, "datos.csv")
        ficheroFisico.appendText(data)

        val list_of_historic_data = arrayListOf<String>()
        list_of_historic_data.add(data)

        //read_colours(folder)
        //read_data_in_storage(folder)
    }

    private fun read_colours(folder: File) {
        // Creamos la dirección en la que se ubica el archivo
        val fileName = folder.toString() + "/colores.csv"
        Log.i("Dirección: ", fileName)
        CSVReader(FileReader(fileName)).use { reader ->
            val r = reader.readAll()
            r.forEach(Consumer { x: Array<String?>? ->
                System.out.println(Arrays.toString(x))
            })
        }
    }

    private fun read_data_in_storage() {
        val storageDir = getExternalFilesDir("Registros")?.also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        // Se necesitan permisos para ejecutar esta operación!!!
        File(storageDir.toString(), "registro.csv").forEachLine { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }

        val csvReader = CSVReaderBuilder(resources.openRawResource(R.raw.registro).bufferedReader())
            .withCSVParser(CSVParserBuilder().withSeparator(';').build())
            .build()

        // Maybe do something with the header if there is one
        val header = csvReader.readNext()

        //
        var myMap: Map<String, String> = mutableMapOf()
        // Read the rest
        var line: Array<String>? = csvReader.readNext()

        val list: List<Array<String>> = ArrayList()

        //var line_1: androidx.collection.ArrayMap<String,String> = csvReader.readNext()

        while (line != null) {
            // Do something with the data
            println(line[0])
            println(line[1])

            line = csvReader.readNext()
        }
    }

    private fun check_hangers(): Boolean {
        return binding.hangersEntry.text.toString().isNotEmpty()
    }

    private fun check_hours(): Boolean {
        return (binding.changeStartTimeLabel.text.toString().isNotEmpty() &&
                binding.colourStartTimeLabel.text.toString().isNotEmpty() &&
                binding.colourEndTimeLabel.text.toString().isNotEmpty())
    }

    private fun check_colour(): Boolean {
        var colour_list: MutableList<String> = mutableListOf()

        for (colour in 40100000..40100400) {
            colour_list.add(colour.toString())
        }
        colour_list.add("FIN")

        return binding.colourEntry.text.toString().isNotEmpty() &&
                colour_list.contains(binding.colourEntry.text.toString())
    }

    private fun dialog_alert(message: String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        // Título.
        builder.setTitle("ERROR DE REGISTRO")

        // Mensaje a mostrar.
        builder.setMessage(message)

        // Mostrar un botón neutral para cerrar la alerta.
        builder.setNeutralButton("ENTENDIDO") { _, _ ->
        }

        // Creamos la alerta
        val dialog: AlertDialog = builder.create()

        // Mostramos la alerta.
        dialog.show()
    }
}
