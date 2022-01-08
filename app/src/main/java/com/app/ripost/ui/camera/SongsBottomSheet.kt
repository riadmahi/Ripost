package com.app.ripost.ui.camera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ripost.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote


class SongsBottomSheet : BottomSheetDialogFragment() {


    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_spotify, container, false)
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(requireContext(), connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote?) {
                    mSpotifyAppRemote = spotifyAppRemote;
                    Log.d(TAG, "onConnected: Connected! Yay!")

                    // Now you can start interacting with App Remote
                    connected();
                }

                override fun onFailure(error: Throwable?) {
                    Log.e(TAG, "onFailure: ", error)
                }
            })
        return view
    }

    private fun connected(){
    }

    companion object{
        private const val TAG = "SongsBottomSheet"
        private const val CLIENT_ID = "00fc36d8159443068d5c72dab8a5b5d8"
        private const val REDIRECT_URI = "http://com.yourdomain.yourapp/callback"
    }
}