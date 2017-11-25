package com.grosalex.alphabetlauncher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.content.Intent
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Created by grosalex on 24/11/2017.
 */

const val SETTINGS: String = "SETTINGS"
const val ALLOW_ROTATION: String = "ALLOW_ROTATION"


class SettingsActivity : AppCompatActivity() {

    private var switchAllowRotation: Switch? = null
    private var tvAllowRotation: TextView? = null

    private var tvWallPaper: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbar.title = getString(R.string.settings)
        toolbar.setTitleTextColor(getColor(R.color.primaryTextColor))

        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
        toolbar?.setNavigationOnClickListener { finish() }
        switchAllowRotation = findViewById(R.id.switch_allow_rotation)
        tvAllowRotation = findViewById(R.id.tv_allow_rotation)

        val pref = getSharedPreferences(SETTINGS, 0)
        var allowRotation = pref.getBoolean(ALLOW_ROTATION, false)

        switchAllowRotation?.isChecked = allowRotation

        switchAllowRotation?.setOnCheckedChangeListener { buttonView, isChecked -> pref.edit().putBoolean("ALLOW_ROTATION", isChecked).apply() }
        tvAllowRotation?.setOnClickListener { switchAllowRotation?.toggle() }

        tvWallPaper = findViewById(R.id.tv_wall_paper)

        tvWallPaper?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SET_WALLPAPER)
            startActivity(Intent.createChooser(intent, "Select Wallpaper"))
        })

        fab.setOnClickListener { finish() }
    }
}