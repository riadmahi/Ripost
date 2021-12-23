package com.app.ripost.UI.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbarText.text = "Settings"
    }
}