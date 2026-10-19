package pe.edu.cibertec.proyectofinaldam1

import pe.edu.cibertec.proyectofinaldam1.historial.Asistencia
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AsistenciaApi {

    @POST("api/asistencias")
    suspend fun registrarAsistencia(
        @Body request: AsistenciaRequest
    ): Response<AsistenciaResponse>

    @GET("api/asistencias/colaborador/{idPersona}")
    fun obtenerHistorial(
        @Path("idPersona") idPersona: Int
    ): Call<List<Asistencia>>
}
