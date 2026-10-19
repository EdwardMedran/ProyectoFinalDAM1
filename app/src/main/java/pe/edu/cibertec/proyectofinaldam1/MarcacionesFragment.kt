package pe.edu.cibertec.proyectofinaldam1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pe.edu.cibertec.proyectofinaldam1.databinding.FragmentMarcacionesBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarcacionesFragment : Fragment() {

    private var _binding: FragmentMarcacionesBinding? = null
    private val binding get() = _binding!!

    private val mainHandler = Handler(Looper.getMainLooper())

    private val relojRunnable = object : Runnable {
        override fun run() {
            val vista = _binding ?: return

            val formatter = SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            )

            vista.tvRelojDigital.text = formatter.format(Date())
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarcacionesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        val sessionManager = SessionManager(requireContext())

        val idPersona = sessionManager.obtenerIdPersona()
        val nombreCompleto = sessionManager.obtenerNombre()
            .ifBlank { "Colaborador" }


        binding.tvBienvenida.text =
            "¡Bienvenido, $nombreCompleto!"

        inicializarFechaEstatica()

        binding.btnRegistrarEntrada.setOnClickListener {
            registrarAsistenciaCloud(idPersona, "ENTRADA")
        }

        binding.btnRegistrarSalida.setOnClickListener {
            registrarAsistenciaCloud(idPersona, "SALIDA")
        }
    }

    override fun onStart() {
        super.onStart()
        mainHandler.removeCallbacks(relojRunnable)
        mainHandler.post(relojRunnable)
    }

    override fun onStop() {
        mainHandler.removeCallbacks(relojRunnable)
        super.onStop()
    }

    private fun inicializarFechaEstatica() {
        val formatter = SimpleDateFormat(
            "EEEE, dd 'de' MMMM 'de' yyyy",
            Locale.forLanguageTag("es-PE")
        )

        binding.tvFechaActual.text = formatter.format(Date())
            .replaceFirstChar { it.uppercase() }
    }

    private fun registrarAsistenciaCloud(
        idPersona: Int,
        tipoMarcacion: String
    ) {
        if (idPersona <= 0) {
            mostrarAlerta(
                "Sesión no válida",
                "No se encontró el identificador del colaborador."
            )
            return
        }

        binding.btnRegistrarEntrada.isEnabled = false
        binding.btnRegistrarSalida.isEnabled = false

        val isoFormatter = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.getDefault()
        )
        val fechaHoraIso = isoFormatter.format(Date())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val respuesta =
                    RetrofitClient.asistenciaApi.registrarAsistencia(
                        AsistenciaRequest(
                            idPersona = idPersona,
                            tipo = tipoMarcacion,
                            fechaHora = fechaHoraIso
                        )
                    )

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    mostrarAlerta(
                        "¡Registro Correcto!",
                        respuesta.body()!!.mensaje
                    )
                } else {
                    val msgError = try {
                        val errorBodyStr = respuesta.errorBody()?.string()

                        if (!errorBodyStr.isNullOrBlank()) {
                            Gson().fromJson(
                                errorBodyStr,
                                ErrorResponse::class.java
                            )?.mensaje
                        } else {
                            null
                        }
                    } catch (_: Exception) {
                        null
                    }

                    mostrarAlerta(
                        "Error de Validación",
                        msgError ?: "Error de servidor: HTTP ${respuesta.code()}"
                    )
                }

            } catch (e: IOException) {
                mostrarAlerta(
                    "Error de Red",
                    "No se pudo establecer conexión con el servidor. Verifica la red y la API."
                )
            } catch (e: Exception) {
                mostrarAlerta(
                    "Error Interno",
                    "Ocurrió un fallo inesperado: ${e.message}"
                )
            } finally {
                _binding?.let { vista ->
                    vista.btnRegistrarEntrada.isEnabled = true
                    vista.btnRegistrarSalida.isEnabled = true
                }
            }
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        if (!isAdded || _binding == null) return

        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    override fun onDestroyView() {
        mainHandler.removeCallbacks(relojRunnable)
        _binding = null
        super.onDestroyView()
    }
}
