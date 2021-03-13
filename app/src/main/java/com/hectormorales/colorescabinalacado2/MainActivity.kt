package com.hectormorales.colorescabinalacado2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hectormorales.colorescabinalacado2.databinding.ActivityMainBinding
import com.hectormorales.colorescabinalacado2.view.HistoricalItem
import com.hectormorales.colorescabinalacado2.view.HistoricalUiModel
import com.opencsv.CSVReader
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
            onRegisterAndContinueButtonClick()
        }

        binding.registerStopButton.setOnClickListener {
            onRegisterAndStopButtonClick()
        }

        binding.registerEndButton.setOnClickListener {
            onRegisterAndEndButtonClick()
        }

        // Esto es temporal, la idea es mostrar algo para ver que todo funciona bien, pero
        // hay que cargar el registro y convertirlo a HistoricalItem
        val historicalItems = listOf(
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, ""),
            HistoricalItem("fakeID", "40100100", "20:06:33", "20:06:33", "20:06:33", 2, "")
        )
        showData(historicalItems)
    }

    private fun showData(historicalItems: List<HistoricalItem>) {
        val historicalUiModel = HistoricalUiModel(historicalItems)
        binding.historicalView.bind(historicalUiModel)
    }

    private fun checkAllNecessaryInputData(): Boolean {
        // Comprobamos que todos los campos están rellenos.
        if (checkColour() && checkHours() && checkHangers()) {
            return true
        } else {
            //errores.text = "No furula"
            when {
                !checkColour() -> {
                    //Snackbar
                    //val message = "El color no es correcto."
                    //Snackbar.make(findViewById(R.id.binding.colourEntry), message, Snackbar.LENGTH_LONG).show()

                    // Hacemos un "AlertDialog".
                    val message = "El color introducido no es correcto."
                    showErrorDialog(message)

                }
                !checkHours() -> {
                    // Hacemos un "AlertDialog".
                    val message = "Debes introducir las tres horas."
                    showErrorDialog(message)

                }
                !checkHangers() -> {
                    // Hacemos un "AlertDialog".
                    val message = "El campo de bastidores debe estar relleno."
                    showErrorDialog(message)
                }
            }
            return false
        }
    }

    private fun onRegisterAndContinueButtonClick() {
        if (checkAllNecessaryInputData()) {
            // Ponemos lo último que hemos introducido en la pantalla:
            showData(emptyList())

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

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

    private fun onRegisterAndStopButtonClick() {
        // Poner un mensaje de que se va a registrar e irse al descanso y si se quiere continuar o no.
        if (checkAllNecessaryInputData()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            showData(emptyList())

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

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

    private fun onRegisterAndEndButtonClick() {
        // Poner un mensaje que avise de que se va a cerrar la aplicación y decidir si continuar o no.

        if (checkAllNecessaryInputData()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            showData(emptyList())

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

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

    private fun writeDataInStorage() {
        // Creamos una variable String con todos los datos.
        val data = binding.colourEntry.text.toString() + ";" +
                binding.changeStartTimeLabel.text.toString() + ";" +
                binding.colourStartTimeLabel.text.toString() + ";" +
                binding.colourEndTimeLabel.text.toString() + ";" +
                binding.hangersEntry.text.toString() + ";" +
                binding.observationsEntry.text.toString() + "\n"

        // Dirección en la que lo vamos a guardar
        val externalFilesFolderPath = getExternalFilesDir(null)

        // Creamos una carpeta dentro de la dirección anterior.
        val dataFolder = File(externalFilesFolderPath, "Datos Lacado")
        //Log.i("Dirección: ", folder.toString())
        // Comprobamos que existe la carpeta. En caso contrario la creamos.
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        // Creamos el fichero y le pasamos los datos que debe almacenar.
        val dataFile = File(dataFolder, "datos.csv")
        dataFile.appendText(data)

        val historicalDataList = arrayListOf<String>()
        historicalDataList.add(data)

        //read_colours(folder)
        //read_data_in_storage(folder)
    }

    private fun readColours(folder: File) {
        // Creamos la dirección en la que se ubica el archivo
        val fileName = "$folder/colores.csv"
        Log.i("Dirección: ", fileName)
        CSVReader(FileReader(fileName)).use { reader ->
            val r = reader.readAll()
            r.forEach(Consumer { x: Array<String?>? ->
                println(Arrays.toString(x))
            })
        }
    }

    private fun readDataInStorage() {
        val storageDir = getExternalFilesDir("Registros")?.also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        // Se necesitan permisos para ejecutar esta operación!!!
        File(storageDir.toString(), "registro.csv")
            .forEachLine { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }

//        val csvReader = CSVReaderBuilder(resources.openRawResource(R.raw.registro).bufferedReader())
//            .withCSVParser(CSVParserBuilder().withSeparator(';').build())
//            .build()
//
//        // Maybe do something with the header if there is one
//        val header = csvReader.readNext()

        //
        var myMap: Map<String, String> = mutableMapOf()
        // Read the rest
//        var line: Array<String>? = csvReader.readNext()

        val list: List<Array<String>> = ArrayList()

        //var line_1: androidx.collection.ArrayMap<String,String> = csvReader.readNext()

//        while (line != null) {
//            // Do something with the data
//            println(line[0])
//            println(line[1])
//
//            line = csvReader.readNext()
//        }
    }

    //region Validación del input
    private fun checkHangers(): Boolean {
        return binding.hangersEntry.text.toString().isNotEmpty()
    }

    private fun checkHours(): Boolean {
        return (binding.changeStartTimeLabel.text.toString().isNotEmpty() &&
                binding.colourStartTimeLabel.text.toString().isNotEmpty() &&
                binding.colourEndTimeLabel.text.toString().isNotEmpty())
    }

    private fun checkColour(): Boolean {
        val colourList = (40100000..40100400).map { it.toString() }.toMutableList()
        colourList.add("FIN")

        return binding.colourEntry.text.toString().isNotEmpty() &&
                colourList.contains(binding.colourEntry.text.toString())
    }
    //endregion Validación del input

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR DE REGISTRO")
            .setMessage(message)
            .setNeutralButton("ENTENDIDO") { _, _ -> }
            .create()
            .show()
    }
}
