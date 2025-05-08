package com.yun.seoul.moduta

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yun.seoul.moduta.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.run {
            lifecycleOwner = this@MainActivity
            main = mainViewModel
        }

        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

//        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navi)
//        val topLevelDestinations = bottomNavView.menu.iterator().asSequence()
//            .map { it.itemId }
//            .toSet()
//        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        findViewById<BottomNavigationView>(R.id.bottom_navi).run {
//            setupWithNavController(navController)
//
//            navController.addOnDestinationChangedListener { _, destination, _ ->
//                val isTopLevelDestination = topLevelDestinations.contains(destination.id)
//                visibility = if(isTopLevelDestination) View.VISIBLE else View.GONE
//            }
//        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.HomeFragment){
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

        // 네비게이션 바 색상을 투명하게 설정
        window.navigationBarColor = Color.WHITE

        // 전체화면 관련(상태바)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
//            v.updatePadding(insets.left, 0, insets.right, 0)
//            WindowInsetsCompat.CONSUMED
//        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}