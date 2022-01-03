package com.app.ripost.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.utils.database.FirebaseCallbackGroup
import com.app.ripost.utils.models.Group
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private var mGroup: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        Log.d(TAG, "onCreate: started.")

        mGroup = intent.getParcelableExtra("EXTRA_GROUP")
        groupName.text = mGroup?.name
        val config = SlidrConfig.Builder().position(SlidrPosition.RIGHT)
            .build()
        Slidr.attach(this, config)

    }

    companion object{
        private const val TAG = "MessageActivity"
    }




}