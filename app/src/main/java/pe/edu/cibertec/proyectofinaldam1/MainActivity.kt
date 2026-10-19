package pe.edu.cibertec.proyectofinaldam1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pe.edu.cibertec.proyectofinaldam1.databinding.ActivityMainBinding
import pe.edu.cibertec.proyectofinaldam1.historial.HistorialFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { vista, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            vista.setPadding(
                barras.left,
                barras.top,
                barras.right,
                barras.bottom
            )

            insets
        }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_marcaciones -> {
                    mostrarFragment(MarcacionesFragment())
                    true
                }

                R.id.nav_historial -> {
                    mostrarFragment(HistorialFragment())
                    true
                }

                R.id.nav_perfil -> {
                    mostrarFragment(PerfilFragment())
                    true
                }

                else -> false
            }
        }


        binding.bottomNavigation.setOnItemReselectedListener {
            // Mantener la pestaña actual
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_marcaciones
            mostrarFragment(MarcacionesFragment())
        }
    }

    private fun mostrarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
