package com.example.analyzer_manager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.analyzer_manager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("stas.060232@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Bug Report")
                putExtra(Intent.EXTRA_TEXT, "Здравствуйте,\n\nЯ обнаружил ошибку в вашем приложении и хотел бы сообщить о ней:\n\n")
            }
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            }
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_changeip, R.id.nav_brightness, R.id.nav_size, R.id.nav_animation, R.id.nav_color, R.id.nav_signal
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        onPause()
        @Suppress("DEPRECATION")
        super.onBackPressed()
        Log.d("MainActivity", "Back button pressed")
    }
    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "Activity stopped")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "Activity destroyed")
    }
}