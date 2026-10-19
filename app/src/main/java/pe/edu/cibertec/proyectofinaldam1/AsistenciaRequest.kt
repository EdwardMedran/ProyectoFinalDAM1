package pe.edu.cibertec.proyectofinaldam1

import com.google.gson.annotations.SerializedName

data class AsistenciaRequest(
    @SerializedName("idPersona")
    val idPersona: Int,

    @SerializedName("tipo")
    val tipo: String, // Enviará "ENTRADA" o "SALIDA"

    @SerializedName("fechaHora")
    val fechaHora: String // Formato ISO 8601 (ej: "2026-10-19T08:30:00")
)
