package pe.edu.cibertec.proyectofinaldam1.historial
import pe.edu.cibertec.proyectofinaldam1.SessionManager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.proyectofinaldam1.R
import pe.edu.cibertec.proyectofinaldam1.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialFragment : Fragment() {

    private var recyclerAsistencias: RecyclerView? = null
    private var progressHistorial: ProgressBar? = null
    private var txtMensajeHistorial: TextView? = null

    private var llamadaHistorial: Call<List<Asistencia>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_historial,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAsistencias = view.findViewById(R.id.recyclerAsistencias)
        progressHistorial = view.findViewById(R.id.progressHistorial)
        txtMensajeHistorial = view.findViewById(R.id.txtMensajeHistorial)

        recyclerAsistencias?.layoutManager = LinearLayoutManager(requireContext())
        recyclerAsistencias?.adapter = AsistenciaAdapter(emptyList())


        val sessionManager = SessionManager(requireContext())
        val idPersona = sessionManager.obtenerIdPersona()



        if (idPersona <= 0) {
            mostrarMensaje("No se encontró la sesión del trabajador.")
            return
        }

        cargarHistorial(idPersona)
    }

    private fun cargarHistorial(idPersona: Int) {
        progressHistorial?.visibility = View.VISIBLE
        txtMensajeHistorial?.visibility = View.GONE

        val api = RetrofitClient.asistenciaApi

        llamadaHistorial = api.obtenerHistorial(idPersona)

        llamadaHistorial?.enqueue(
            object : Callback<List<Asistencia>> {

                override fun onResponse(
                    call: Call<List<Asistencia>>,
                    response: Response<List<Asistencia>>
                ) {
                    if (view == null) return

                    progressHistorial?.visibility = View.GONE

                    if (response.isSuccessful) {
                        val lista = response.body().orEmpty()

                        recyclerAsistencias?.adapter =
                            AsistenciaAdapter(lista)

                        if (lista.isEmpty()) {
                            mostrarMensaje(
                                "No tienes asistencias registradas."
                            )
                        }
                    } else {
                        mostrarMensaje(
                            "Error al consultar las asistencias: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(
                    call: Call<List<Asistencia>>,
                    t: Throwable
                ) {
                    if (call.isCanceled || view == null) return

                    progressHistorial?.visibility = View.GONE

                    mostrarMensaje(
                        "No se pudo conectar con el servidor."
                    )
                }
            }
        )
    }

    private fun mostrarMensaje(mensaje: String) {
        progressHistorial?.visibility = View.GONE
        txtMensajeHistorial?.text = mensaje
        txtMensajeHistorial?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        llamadaHistorial?.cancel()
        llamadaHistorial = null

        recyclerAsistencias?.adapter = null
        recyclerAsistencias = null
        progressHistorial = null
        txtMensajeHistorial = null

        super.onDestroyView()
    }
}
