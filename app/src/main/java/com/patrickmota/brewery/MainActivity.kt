package com.patrickmota.brewery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.patrickmota.brewery.databinding.ActivityMainBinding
import com.patrickmota.brewery.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Brewery)
        binding = ActivityMainBinding.inflate(layoutInflater)

        navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentHomePageFragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        when (navHostFragment.childFragmentManager.fragments[0]) {
            is HomeFragment -> super.onBackPressed()
            else -> navController.navigateUp()
        }
    }
}
