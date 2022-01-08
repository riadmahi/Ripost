package com.app.ripost.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ripost.R
import com.app.ripost.utils.BottomNavigationHelper
import com.app.ripost.utils.DoubleClickListener
import com.app.ripost.utils.Timer
import com.app.ripost.utils.VideoUtils
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.layout_bottom_navigation.*
import kotlinx.android.synthetic.main.snippet_camera_widgets.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class CameraActivity : AppCompatActivity(){
    //WIDGETS
    private lateinit var progressTimer: Timer
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: Camera


    //VAR
    private var isRecording = false
    private var cameraFacing = 0
    private var currentProgress = 0
    private var timerCountDown = 0

    private val videoCapture = VideoCapture.Builder().build()

    //List of video file (already record)
    private var mVideos : MutableList<File> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //Camera facing selector
        // Request camera permissions

        myMoments.visibility = View.VISIBLE
        setupBottomNavigation()
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

        PushDownAnim.setPushDownAnimTo(btnFinishRecord).setOnClickListener {
            if(mVideos.size > 0) {
                if (mVideos.size != 1) {
                    VideoUtils().concatenateVideos(this, mVideos)

                }else {
                    Log.d(TAG, "onCreate: absolute path: ${mVideos[0].absolutePath}")
                    btnFinishRecord.visibility = View.GONE
                    progressBar.progress = 0
                    currentProgress = 0
                    val intent = Intent(this, PreviewActivity::class.java).putExtra(
                        "EXTRA_VIDEO",
                        mVideos[0].absolutePath
                    )
                    mVideos.clear()
                    startActivity(intent)
                }
            }else{
                Toast.makeText(this, "Error, please reattempt.", Toast.LENGTH_SHORT).show()
            }
        }

        myMoments.setOnClickListener {
            val intent = Intent(this, MyMomentsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation: started.")
        BottomNavigationHelper().navigate(this, bottom_navigation)
        val menu: Menu = bottom_navigation.menu
        val menuItem: MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }

    fun startPreviewActivity(){
        btnFinishRecord.visibility = View.GONE
        progressBar.progress = 0
        currentProgress = 0
        val intent = Intent(this, PreviewActivity::class.java).putExtra("EXTRA_VIDEO", mVideos[0].absolutePath)
        mVideos.clear()
        startActivity(intent)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
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
                        this, cameraSelector, preview, videoCapture
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

    fun showMergeProgression(){
        cardView.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    fun hideMergeProgression(){
        progress_bar.visibility = View.GONE
        cardView.visibility = View.VISIBLE
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



    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                allPermissionsGranted()
                Toast.makeText(
                        this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                ).show()
                //finish()
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
            myMoments.visibility = View.GONE
            setCountDownTimer()
            if(timerCountDown == 0){
                countDownTimer.visibility = View.GONE
                startRecording()
                animCaptureButtonOn()
                isRecording = true
                animRecordingIndicator()
                if (currentProgress == 0)
                    initProgressTimer()
                else
                    progressTimer.start()
            }

        }

        avd_record_indicator.setOnClickListener {
            isRecording = false
            animRecordingIndicator()
            animCaptureButtonOff()
            progressTimer.pause()
            stopRecording()
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
        timerCountDown = 0
        timer.setOnClickListener {
            timerCountDown += 1
            timerCountDown %= 3
            Log.d(TAG, "setupCameraWidget: timer position = $timerCountDown")
            when(timerCountDown){
                0 -> {
                    timer.setImageResource(R.drawable.ic_timer)
                }
                1 -> {
                    timer.setImageResource(R.drawable.ic_timer_5s)
                }
                2 -> {
                    timer.setImageResource(R.drawable.ic_timer_10s)
                }
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun setCountDownTimer() {
        if (timerCountDown != 0) {
            val millisInFuture = when (timerCountDown) {
                1 -> 5000L
                2 -> 10000L
                else -> 0L
            }
            countDownTimer.visibility = View.VISIBLE
            val timer = object : Timer(millisInFuture, 1000) {
                override fun onTimerTick(millisUntilFinished: Long) {
                    Log.d(TAG, "onTimerTick: decompte: ${millisUntilFinished / 1000}s")
                    val sec = (millisUntilFinished / 1000).toInt()
                    countDownTimer.text = "${sec}s"
                    val ring: MediaPlayer = MediaPlayer.create(this@CameraActivity, R.raw.timer_count_down)
                    ring.start()
                }

                override fun onTimerFinish() {
                    countDownTimer.visibility = View.GONE
                    startRecording()
                    animCaptureButtonOn()
                    isRecording = true
                    animRecordingIndicator()
                    if (currentProgress == 0)
                        initProgressTimer()
                    else
                        progressTimer.start()

                }

            }
            timer.start()
        }
    }

    /**
     * Record a video
     *
     */

    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        val videoFile = File(
                outputDirectory,
                SimpleDateFormat(FILENAME_FORMAT, Locale.US
                ).format(System.currentTimeMillis()) + ".mp4")
        val outputOptions = VideoCapture.OutputFileOptions.Builder(videoFile).build()

        videoCapture.startRecording(outputOptions, ContextCompat.getMainExecutor(this), object: VideoCapture.OnVideoSavedCallback {
            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                Log.e(TAG, "Video capture failed: $message")
            }

            override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(videoFile)
                val msg = "Video capture succeeded: $savedUri"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
                mVideos.add(videoFile)
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        videoCapture.stopRecording()
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
            recordAnim.visibility = View.GONE
            avd_record_indicator.visibility = View.GONE

            camera_capture_button.visibility = View.VISIBLE
            btnFinishRecord.visibility = View.VISIBLE
        }else{
            Log.d(TAG, "animRecordingIndicator: show record indicator")

            btnFinishRecord.visibility = View.GONE
            camera_capture_button.visibility = View.GONE
            avd_record_indicator.setImageResource(R.drawable.avd_record_on)
            avd_record_indicator.visibility = View.VISIBLE
            recordAnim.visibility = View.VISIBLE
            val shapeTransformation2 = recordAnim.drawable as AnimatedVectorDrawable

            val shapeTransformation = avd_record_indicator.drawable as AnimatedVectorDrawable
            shapeTransformation.start()

            shapeTransformation2.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    shapeTransformation2.start()
                }
            })
            shapeTransformation2.start()

        }
    }


    /**
     * Progress Indicator
     */

    private fun initProgressTimer(){
        Log.d(TAG, "initProgressTimer: started.")

        progressTimer = object : Timer(TIMER_DURATION, TIMER_INTERVAL) {
            override fun onTimerTick(millisUntilFinished: Long) {
                currentProgress = ((TIMER_DURATION - millisUntilFinished)).toInt()

                Log.d("MainActivity", "onTimerTick $currentProgress")
                updateProgressUI()

            }

            override fun onTimerFinish() {
                Log.d("MainActivity", "onTimerFinish")
                isRecording = false
                animRecordingIndicator()
                stopRecording()
                if(mVideos.size > 0) {
                    if (mVideos.size != 1) {
                        VideoUtils().concatenateVideos(this@CameraActivity, mVideos)

                    }else {
                        Log.d(TAG, "onCreate: absolute path: ${mVideos[0].absolutePath}")
                        btnFinishRecord.visibility = View.GONE
                        progressBar.progress = 0
                        currentProgress = 0
                        val intent = Intent(this@CameraActivity, PreviewActivity::class.java).putExtra(
                                "EXTRA_VIDEO",
                                mVideos[0].absolutePath
                        )
                        mVideos.clear()
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this@CameraActivity, "Error, please reattempt.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        progressTimer.start()
    }

    private fun updateProgressUI(){
        progressBar.setProgress(currentProgress, true)
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }



    override fun onStart() {
        overridePendingTransition(0, 0)
        super.onStart()
    }


    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 100
        private const val TIMER_DURATION = 20000L
        private const val TIMER_INTERVAL = 100L
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val ACTIVITY_NUM = 2
    }
}