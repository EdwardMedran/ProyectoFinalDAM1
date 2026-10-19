
package pe.edu.cibertec.proyectofinaldam1.historial

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.appgrupo1_t2.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path



class HistorialActivity : AppCompatActivity() {

    private lateinit var recyclerAsistencias: RecyclerView
    private lateinit var progressHistorial: ProgressBar
    private lateinit var txtMensajeHistorial: TextView

    private interface HistorialApi {
        @GET("api/asistencias/colaborador/{idPersona}")
        fun obtenerHistorial(
            @Path("idPersona") idPersona: Int
        ): Call<List<Asistencia>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val contenedor = findViewById<View>(R.id.contenedorHistorial)

        ViewCompat.setOnApplyWindowInsetsListener(contenedor) { vista, insets ->
            val barrasSistema = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            vista.setPadding(
                vista.paddingLeft,
                barrasSistema.top + (16 * resources.displayMetrics.density).toInt(),
                vista.paddingRight,
                barrasSistema.bottom
            )

            insets
        }


        recyclerAsistencias = findViewById(R.id.recyclerAsistencias)
        progressHistorial = findViewById(R.id.progressHistorial)
        txtMensajeHistorial = findViewById(R.id.txtMensajeHistorial)

        recyclerAsistencias.layoutManager = LinearLayoutManager(this)
        recyclerAsistencias.adapter = AsistenciaAdapter(emptyList())

        val idPersona = intent.getIntExtra("idPersona", -1)

        if (idPersona <= 0) {
            mostrarMensaje("No se encontró la sesión del trabajador.")
            return
        }

        cargarHistorial(idPersona)
    }

    private fun cargarHistorial(idPersona: Int) {
        progressHistorial.visibility = View.VISIBLE
        txtMensajeHistorial.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5019/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(HistorialApi::class.java)

        api.obtenerHistorial(idPersona)
            .enqueue(object : Callback<List<Asistencia>> {

                override fun onResponse(
                    call: Call<List<Asistencia>>,
                    response: Response<List<Asistencia>>
                ) {
                    progressHistorial.visibility = View.GONE

                    if (response.isSuccessful) {
                        val lista = response.body().orEmpty()

                        recyclerAsistencias.adapter =
                            AsistenciaAdapter(lista)

                        if (lista.isEmpty()) {
                            mostrarMensaje("No tienes asistencias registradas.")
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
                    progressHistorial.visibility = View.GONE
                    mostrarMensaje(
                        "No se pudo conectar con el servidor."
                    )
                }
            })
    }

    private fun mostrarMensaje(mensaje: String) {
        progressHistorial.visibility = View.GONE
        txtMensajeHistorial.text = mensaje
        txtMensajeHistorial.visibility = View.VISIBLE
    }
}
