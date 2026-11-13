package com.isetr.smarttravel

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import com.isetr.smarttravel.data.local.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnGlobalAuth: Button
    private lateinit var appBarLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ربط الـ Views
        btnGlobalAuth = findViewById(R.id.btnGlobalAuth)
        appBarLayout = findViewById(R.id.appBarLayout)

        // Insets: status bar fil AppBar, clavier / nav bar fil bottom
        val baseTop = appBarLayout.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

            appBarLayout.updatePadding(top = baseTop + bars.top)
            v.setPadding(bars.left, 0, bars.right, maxOf(bars.bottom, ime.bottom))
            WindowInsetsCompat.CONSUMED
        }

        updateAuthButtonState()

        val navHost = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        val navController = navHost.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = graph

        // مراقبة تغيير الشاشات (تحديث الزر + إخفاء/إظهار الـ AppBar في صفحة الـ Login)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateAuthButtonState()

            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment
                || destination.id == R.id.forgotPasswordFragment || destination.id == R.id.resetPasswordFragment) {
                appBarLayout.visibility = View.GONE
            } else {
                appBarLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun updateAuthButtonState() {
        val session = SessionManager(this)
        val navHost = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        val navController = navHost.navController

        if (session.isLoggedIn()) {
            btnGlobalAuth.text = "Déconnexion"
            btnGlobalAuth.setOnClickListener {
                session.clear()
                Toast.makeText(this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show()
                updateAuthButtonState()
                navController.navigate(R.id.offersFragment)
            }
        } else {
            btnGlobalAuth.text = "Connexion"
            btnGlobalAuth.setOnClickListener {
                navController.navigate(R.id.loginFragment)
            }
        }
    }
}