package com.app.ripost

import android.Manifest
import android.R.drawable
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ripost.Utils.DoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.snippet_camera_widgets.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: Camera
    private var isRecording = false
    private var cameraFacing = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var captureBtnOn = false
        //Camera facing selector
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        setupCameraWidget()

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }





    private fun takePhoto() {}

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector =
                if (cameraFacing == 1) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

                val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        val scale =
                            camera.cameraInfo.zoomState.value!!.zoomRatio * detector.scaleFactor
                        camera.cameraControl.setZoomRatio(scale)
                        return true
                    }
                }


                val scaleDetector = ScaleGestureDetector(this, listener)

                viewFinder.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        //Set focus when click action

                        val factory = viewFinder.meteringPointFactory
                        val point = factory.createPoint(event.x, event.y)
                        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                            .setAutoCancelDuration(5, TimeUnit.SECONDS)
                            .build()
                        camera.cameraControl.startFocusAndMetering(action)
                        showFocusCircle(event.x, event.y)

                    }
                    scaleDetector.onTouchEvent(event)
                    return@setOnTouchListener false
                }


            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))


    }



    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    /**
     * Camera widgets
     */

    private fun setupCameraWidget(){
        viewFinder.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View) {
                cameraFacing = if (cameraFacing == 0) 1 else 0
                startCamera()
            }
        })

        camera_capture_button.setOnClickListener {
            Log.d(TAG, "onCreate: capture btn clicked")
            takePhoto()
            animCaptureButtonOn()
            isRecording = true
            animRecordingIndicator()
        }

        avd_record_indicator.setOnClickListener {
            Log.d(TAG, "onCreate: avd record indicator visibility = $isRecording")
            isRecording = false
            animRecordingIndicator()
            animCaptureButtonOff()
        }

        btnCameraSelected.setOnClickListener {
            cameraFacing = if (cameraFacing == 0) 1 else 0
            startCamera()
        }
        var torchEnable = false
        flashlight.setOnClickListener {
            torchEnable = !torchEnable
            camera.cameraControl.enableTorch(torchEnable)
            if(torchEnable)
                flashlight.setImageResource(R.drawable.ic_flashlight_off)
            else
                flashlight.setImageResource(R.drawable.ic_flashlight)
        }

        // 0 default | 1 5s | 2 10s
        var timerPosition = 0
        timer.setOnClickListener {
            timerPosition += 1
            timerPosition %= 3
            Log.d(TAG, "setupCameraWidget: timer position = $timerPosition")
            when(timerPosition){
                0 -> {timer.setImageResource(R.drawable.ic_timer)}
                1 -> {timer.setImageResource(R.drawable.ic_timer_5s)}
                2 -> {timer.setImageResource(R.drawable.ic_timer_10s)}
            }
        }
    }

    /**
     *                           Record Animation
     */


    private fun animCaptureButtonOn(){
        camera_capture_button.setImageResource(R.drawable.avd_btn_capture_on)
        val shapeTransformation = camera_capture_button.drawable as AnimatedVectorDrawable
        shapeTransformation.start()
    }

    private fun animCaptureButtonOff(){
        camera_capture_button.setImageResource(R.drawable.avd_btn_capture_off)
        val shapeTransformation = camera_capture_button.drawable as AnimatedVectorDrawable
        shapeTransformation.start()
    }

    private fun showFocusCircle(x: Float, y: Float){
        //Show the focus circle
        focusCircle.x = x
        focusCircle.y = y
        focusCircle.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            focusCircle.visibility = View.GONE
        }, 500)

    }

    private fun animRecordingIndicator(){
        if(!isRecording){
            Log.d(TAG, "animRecordingIndicator: hide record indicator")
            avd_record_indicator.setImageResource(R.drawable.avd_record_off)
            val shapeTransformation = avd_record_indicator.drawable as AnimatedVectorDrawable
            shapeTransformation.start()
            avd_record_indicator.visibility = View.GONE
            camera_capture_button.visibility = View.VISIBLE
        }else{
            Log.d(TAG, "animRecordingIndicator: show record indicator")
            camera_capture_button.visibility = View.GONE
            avd_record_indicator.setImageResource(R.drawable.avd_record_on)
            avd_record_indicator.visibility = View.VISIBLE

            val shapeTransformation = avd_record_indicator.drawable as AnimatedVectorDrawable
            shapeTransformation.start()
        }
    }



    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

