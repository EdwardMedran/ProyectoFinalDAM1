package pe.edu.cibertec.proyectofinaldam1.historial

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.proyectofinaldam1.R
import java.text.SimpleDateFormat
import java.util.Locale

class AsistenciaAdapter(
    private val asistencias: List<Asistencia>
) : RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder>() {

    class AsistenciaViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
        val txtHora: TextView = itemView.findViewById(R.id.txtHora)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsistenciaViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asistencia, parent, false)

        return AsistenciaViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: AsistenciaViewHolder,
        position: Int
    ) {
        val asistencia = asistencias[position]

        holder.txtTipo.text = asistencia.tipo

        if (asistencia.tipo == "ENTRADA") {
            holder.txtTipo.setTextColor(Color.parseColor("#16A34A"))
        } else {
            holder.txtTipo.setTextColor(Color.parseColor("#DC2626"))
        }

        try {
            val formatoEntrada = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault()
            )

            val fecha = formatoEntrada.parse(
                asistencia.fechaHora.substringBefore(".")
            )

            if (fecha != null) {
                val formatoFecha = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

                val formatoHora = SimpleDateFormat(
                    "HH:mm:ss",
                    Locale.getDefault()
                )

                holder.txtFecha.text = formatoFecha.format(fecha)
                holder.txtHora.text = formatoHora.format(fecha)
            }
        } catch (e: Exception) {
            holder.txtFecha.text = asistencia.fechaHora
            holder.txtHora.text = "--:--:--"
        }

        holder.txtEstado.text =
            if (asistencia.estadoSincronizacion) {
                "Sincronizado"
            } else {
                "Pendiente de sincronización"
            }
    }

    override fun getItemCount(): Int = asistencias.size
}
