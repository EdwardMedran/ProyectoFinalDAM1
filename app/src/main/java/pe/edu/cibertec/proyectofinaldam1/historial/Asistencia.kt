package pe.edu.cibertec.proyectofinaldam1.historial

data class Asistencia(
    val idAsistencia: Int,
    val fechaHora: String,
    val tipo: String,
    val estadoSincronizacion: Boolean,
    val idPersona: Int
)
