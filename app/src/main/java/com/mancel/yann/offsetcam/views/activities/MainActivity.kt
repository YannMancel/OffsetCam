package com.mancel.yann.offsetcam.views.activities

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mancel.yann.offsetcam.R

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.activities
 *
 * An [BaseActivity] subclass.
 */
class MainActivity : BaseActivity() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    override fun getActivityLayout(): Int = R.layout.activity_main

    override fun configureDesign() {
        // Navigation
        this.configureActionBarForNavigation()
    }

    // -- Activity --

    override fun onSupportNavigateUp(): Boolean {
        return this.mNavController.navigateUp(this.mAppBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // -- Action bar --

    /**
     * Configures the Action bar for the navigation
     */
    private fun configureActionBarForNavigation() {
        // NavController
        this.mNavController = this.findNavController(R.id.activity_main_NavHostFragment)

        // AppBarConfiguration
        this.mAppBarConfiguration = AppBarConfiguration(this.mNavController.graph)

        // Action bar
        this.setupActionBarWithNavController(this.mNavController, this.mAppBarConfiguration)
    }
}