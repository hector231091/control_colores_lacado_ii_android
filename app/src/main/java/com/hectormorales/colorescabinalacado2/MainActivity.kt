package com.hectormorales.colorescabinalacado2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hectormorales.colorescabinalacado2.databinding.ActivityMainBinding
import com.hectormorales.colorescabinalacado2.view.HistoricalUiModel
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()
    private val historicalViewModel: HistoricalViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.inputView.binding.buttonChangeStartTimeHeader.setOnClickListener {
            mainViewModel.onChangeStartButtonClick()
        }

        binding.inputView.binding.buttonColourStartTimeHeader.setOnClickListener {
            mainViewModel.onColourStartButtonClick()
        }

        binding.inputView.binding.buttonColourEndTimeHeader.setOnClickListener {
            mainViewModel.onColourEndButtonClick()
        }

        binding.buttonRegisterAndContinue.setOnClickListener {
            onRegisterAndContinueButtonClick()
        }

        binding.buttonRegisterAndBreak.setOnClickListener {
            onRegisterAndBreakButtonClick()
        }

        binding.buttonRegisterAndFinish.setOnClickListener {
            onRegisterAndFinishButtonClick()
        }

        with(mainViewModel) {
            onStart()
            inputUiModelObservable.observe(this@MainActivity, {
                binding.inputView.bind(it)
            })
            changeStartEnableObservable.observe(this@MainActivity, { enabled ->
                binding.inputView.binding.buttonChangeStartTimeHeader.isEnabled = enabled
            })
            colourStartEnableObservable.observe(this@MainActivity, { enabled ->
                binding.inputView.binding.buttonColourStartTimeHeader.isEnabled = enabled
            })
            colourEndEnableObservable.observe(this@MainActivity, { enabled ->
                binding.inputView.binding.buttonColourEndTimeHeader.isEnabled = enabled
            })
        }

        with(historicalViewModel) {
            onStart()
            historicalListObservable.observe(this@MainActivity, { populateHistorical(it) })
        }
    }

    private fun populateHistorical(model: HistoricalUiModel) {
        binding.historicalView.bind(model)
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
                    //Snackbar.make(findViewById(R.id.binding.editTextColour), message, Snackbar.LENGTH_LONG).show()

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
            populateHistorical(HistoricalUiModel(emptyList()))

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

            // Copiamos la hora del final_color en el inicio_cambio:
            binding.inputView.binding.textViewColourStartTimeLabel.text =
                binding.inputView.binding.textViewColourEndTimeLabel.text

            // Borramos todos los labels que hay que borrar:
            binding.inputView.binding.editTextColour.setText("")
            binding.inputView.binding.textViewColourStartTimeLabel.text = ""
            binding.inputView.binding.textViewColourEndTimeLabel.text = ""
            binding.inputView.binding.editTextHangersAmount.setText("")
            binding.inputView.binding.editTextObservations.setText("")

            // Activamos el botón del inicio del color:
            binding.inputView.binding.buttonColourStartTimeHeader.isEnabled = true
        }
    }

    private fun onRegisterAndBreakButtonClick() {
        // Poner un mensaje de que se va a registrar e irse al descanso y si se quiere continuar o no.
        if (checkAllNecessaryInputData()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            populateHistorical(HistoricalUiModel(emptyList()))

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

            // Borramos todos los labels que hay que borrar:
            binding.inputView.binding.editTextColour.setText("")
            binding.inputView.binding.textViewChangeStartTimeLabel.text = ""
            binding.inputView.binding.textViewColourStartTimeLabel.text = ""
            binding.inputView.binding.textViewColourEndTimeLabel.text = ""
            binding.inputView.binding.editTextHangersAmount.setText("")
            binding.inputView.binding.editTextObservations.setText("")

            // Activamos el botón del inicio del cambio:
            binding.inputView.binding.buttonChangeStartTimeHeader.isEnabled = true
        }
    }

    private fun onRegisterAndFinishButtonClick() {
        // Poner un mensaje que avise de que se va a cerrar la aplicación y decidir si continuar o no.

        if (checkAllNecessaryInputData()) {
            // Ponemos el lo último que hemos introducido en la pantalla:
            populateHistorical(HistoricalUiModel(emptyList()))

            // Poner función para guardar esto en un archivo:
            writeDataInStorage()

            // Borramos todos los labels que hay que borrar:
            binding.inputView.binding.editTextColour.setText("")
            binding.inputView.binding.textViewChangeStartTimeLabel.text = ""
            binding.inputView.binding.textViewColourStartTimeLabel.text = ""
            binding.inputView.binding.textViewColourEndTimeLabel.text = ""
            binding.inputView.binding.editTextHangersAmount.setText("")
            binding.inputView.binding.editTextObservations.setText("")

            // Activamos el botón del inicio del color:
            binding.inputView.binding.buttonColourStartTimeHeader.isEnabled = true

            finish()
            exitProcess(0)
        }
    }

    private fun writeDataInStorage() {
        // Creamos una variable String con todos los datos.
        val data = binding.inputView.binding.editTextColour.text.toString() + ";" +
                binding.inputView.binding.textViewChangeStartTimeLabel.text.toString() + ";" +
                binding.inputView.binding.textViewColourStartTimeLabel.text.toString() + ";" +
                binding.inputView.binding.textViewColourEndTimeLabel.text.toString() + ";" +
                binding.inputView.binding.editTextHangersAmount.text.toString() + ";" +
                binding.inputView.binding.editTextObservations.text.toString() + "\n"

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

    // TODO mover a HistoricalViewModel
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
        return binding.inputView.binding.editTextHangersAmount.text.toString().isNotEmpty()
    }

    private fun checkHours(): Boolean {
        return (binding.inputView.binding.textViewChangeStartTimeLabel.text.toString()
            .isNotEmpty() &&
                binding.inputView.binding.textViewColourStartTimeLabel.text.toString()
                    .isNotEmpty() &&
                binding.inputView.binding.textViewColourEndTimeLabel.text.toString().isNotEmpty())
    }

    private fun checkColour(): Boolean {
        // TODO solución temporal, cargar colores de fichero
        val colourList = (40100000..40100400).map { it.toString() }.toMutableList()
        colourList.add("FIN")

        return binding.inputView.binding.editTextColour.text.toString().isNotEmpty() &&
                colourList.contains(binding.inputView.binding.editTextColour.text.toString())
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
