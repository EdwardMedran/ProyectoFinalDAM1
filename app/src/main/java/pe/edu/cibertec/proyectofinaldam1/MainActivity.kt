package pe.edu.cibertec.proyectofinaldam1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pe.edu.cibertec.proyectofinaldam1.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var idPersonaLogueada: Int = 0

    private val mainHandler = Handler(Looper.getMainLooper())
    private val relojRunnable = object : Runnable {
        override fun run() {
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            binding.tvRelojDigital.text = formatter.format(Date())
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnCerrarSesion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idPersonaLogueada = intent.getIntExtra("idPersona", 0)
        val nombreColaborador = intent.getStringExtra("nombre") ?: "Colaborador"
        val apellidoColaborador = intent.getStringExtra("apellido") ?: ""

        binding.tvBienvenida.text = "¡Bienvenido, $nombreColaborador $apellidoColaborador!"

        mainHandler.post(relojRunnable)
        inicializarFechaEstatica()

        binding.btnRegistrarEntrada.setOnClickListener { registrarAsistenciaCloud("ENTRADA") }
        binding.btnRegistrarSalida.setOnClickListener { registrarAsistenciaCloud("SALIDA") }
    }

    private fun inicializarFechaEstatica() {
        val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "PE"))
        binding.tvFechaActual.text = formatter.format(Date()).replaceFirstChar { it.uppercase() }
    }

    private fun registrarAsistenciaCloud(tipoMarcacion: String) {
        binding.btnRegistrarEntrada.isEnabled = false
        binding.btnRegistrarSalida.isEnabled = false

        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val fechaHoraIso = isoFormatter.format(Date())

        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.asistenciaApi.registrarAsistencia(
                    AsistenciaRequest(
                        idPersona = idPersonaLogueada,
                        tipo = tipoMarcacion,
                        fechaHora = fechaHoraIso
                    )
                )

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    mostrarAlerta("¡Registro Correcto!", respuesta.body()!!.mensaje)
                } else {
                    val msgError = try {
                        val errorBodyStr = respuesta.errorBody()?.string()
                        if (!errorBodyStr.isNullOrBlank()) {
                            Gson().fromJson(errorBodyStr, ErrorResponse::class.java)?.mensaje
                        } else { null }
                    } catch (_: Exception) { null }

                    mostrarAlerta("Error de Validación", msgError ?: "Error de servidor: HTTP ${respuesta.code()}")
                }

            } catch (e: IOException) {
                mostrarAlerta("Error de Red", "No se pudo establecer conexión con el backend en localhost. Verifica tu servidor C#.")
            } catch (e: Exception) {
                mostrarAlerta("Error Interno", "Ocurrió un fallo inesperado: ${e.message}")
            } finally {
                binding.btnRegistrarEntrada.isEnabled = true
                binding.btnRegistrarSalida.isEnabled = true
            }
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(relojRunnable)
    }
}
