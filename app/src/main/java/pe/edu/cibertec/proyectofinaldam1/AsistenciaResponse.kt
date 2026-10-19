package pe.edu.cibertec.proyectofinaldam1

import com.google.gson.annotations.SerializedName

data class AsistenciaResponse(
    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("idAsistencia")
    val idAsistencia: Int?
)
