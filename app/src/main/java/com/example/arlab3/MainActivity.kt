package com.example.arlab3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), LocationListener, SensorEventListener {

    private lateinit var fragment: ArFragment
    private lateinit var sm: SensorManager
    private var testRenderable: ModelRenderable? = null
    private var nearestStation: Station? = null
    private var formattedDistanceInMeters: Double? = null
    private var formattedDistanceInKilometers: Double? = null
    private var headingToNorth: Float? = null
    private var headingtoDestination: Float? = null
    private var headingSet = false
    private var bearing: Float? = null
    private var magnetometer: Sensor? = null
    private var accelerometer: Sensor? = null
    private var mGravity: FloatArray? = null
    private var mGeomagnetic: FloatArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headingToNorth = 0f
        bearing = 0f

        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment

        var model =
            Uri.parse("https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Duck/glTF/Duck.gltf")

        val renderableFuture = ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this, model, RenderableSource.SourceType.GLTF2
                )
                    .setScale(0.1f)
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
            .setRegistryId("Duck")
            .build()
        renderableFuture.thenAccept { it -> testRenderable = it }

        fragment.arSceneView.scene.addOnUpdateListener {
            frameUpdate()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                0
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                0
            )
        }
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)

        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            magnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            Log.i("IMGLAB", "Magnetometer set")
        } else {
            Log.i("IMGLAB", "No magnetometer in device")
        }
        if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            Log.i("IMGLAB", "Accelerometer set")
        } else {
            Log.i("IMGLAB", "No accelerometer in device")
        }

        sm.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        button.setOnClickListener {
            exitProcess(0)
        }
    }

    private fun frameUpdate() {
        val arFrame = fragment.arSceneView.arFrame

        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        if (headingSet) {
            updatedAugmentedImages.forEach {
                when (it.trackingState) {
                    TrackingState.PAUSED -> {
                        val text = "Detected image: " + it.name + " - need more info"
                        Log.i("IMGLAB", text)
                    }
                    TrackingState.STOPPED -> {
                        val text = "Tracking stopped: " + it.name
                        Log.i("IMGLAB", text)
                    }
                    TrackingState.TRACKING -> {
                        val anchors = it.anchors
                        if (anchors.isEmpty()) {
                            fit_to_scan_img.visibility = View.GONE

                            stationText.text = "${nearestStation?.name}"

                            if (formattedDistanceInKilometers == null) {
                                distanceText.text = "$formattedDistanceInMeters M"
                            } else {

                                distanceText.text = "$formattedDistanceInKilometers KM"
                            }

                            stationText.visibility = View.VISIBLE
                            distanceText.visibility = View.VISIBLE

                            val pose = it.centerPose
                            val anchor = it.createAnchor(pose)
                            val anchorNode = AnchorNode(anchor)
                            anchorNode.setParent(fragment.arSceneView.scene)
                            val modelNode = TransformableNode(fragment.transformationSystem)
                            Log.i("IMGLAB", "Heading in model: $headingtoDestination")
                            modelNode.localRotation =
                                Quaternion.multiply(
                                    modelNode.localRotation,
                                    Quaternion.axisAngle(Vector3(1f, 0f, 0f), -90f)
                                )
                            modelNode.localRotation =
                                Quaternion.multiply(
                                    modelNode.localRotation,
                                    Quaternion.axisAngle(Vector3(0f, 1f, 0f), 90f)
                                )
                            modelNode.localRotation =
                                Quaternion.multiply(
                                    modelNode.localRotation,
                                    Quaternion.axisAngle(
                                        Vector3(0f, 1f, 0f),
                                        -headingtoDestination!!
                                    )
                                )
                            modelNode.rotationController.isEnabled = false
                            modelNode.localScale = (Vector3(0.1f, 0.1f, 0.1f))
                            modelNode.setParent(anchorNode)
                            modelNode.renderable = testRenderable
                            modelNode.setOnTapListener { hitTestRes: HitTestResult?, motionEv: MotionEvent? ->
                                Log.i(
                                    "IMGLAB",
                                    "TAP. Your nearest station is: " + nearestStation?.name
                                )
                                Toast.makeText(
                                    this,
                                    "Duck points the way!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onLocationChanged(p: Location) {
        val stations = Stations.stationList
        var previousDistanceToStation = p.distanceTo(stations[0].pos)

        for (s in stations) {
            val distanceToStation = p.distanceTo(s.pos)

            //val formattedDistanceToStation = String.format("%.1f", distanceToStation).toDouble()
            //Log.i("IMGLAB", "Checking station ${s.name} $formattedDistanceToStation away")

            if (distanceToStation < previousDistanceToStation) {
                nearestStation = s
                previousDistanceToStation = distanceToStation
            }
        }

        val distanceToNearestStation = p.distanceTo(nearestStation?.pos)
        val formattedDistance = String.format("%.1f", distanceToNearestStation).toDouble()

        formattedDistanceInMeters = formattedDistance
        if (formattedDistanceInMeters!! > 1000) {
            formattedDistanceInKilometers = String.format(
                "%.2f",
                (formattedDistanceInMeters!! / 1000)
            ).toDouble()
        }

        if (nearestStation != null) {
            Log.i(
                "IMGLAB",
                "Nearest metro station is: " + nearestStation!!.name + ". It is " + formattedDistance + " meters away"
            )
        } else {
            Log.i("IMGLAB", "Could not find metro station")
        }


        var geofield = GeomagneticField(
            p.latitude.toFloat(),
            p.longitude.toFloat(),
            p.altitude.toFloat(),
            System.currentTimeMillis()
        )

        headingToNorth = headingToNorth?.plus(geofield.declination)
        bearing = p.bearingTo(nearestStation?.pos)
        headingtoDestination = (bearing!! - headingToNorth!!) * -1
        headingSet = true
        Log.i("IMGLAB", "Heading to destination is now: $headingtoDestination")

    }

    override fun onStatusChanged(p: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p: String?) {
    }

    override fun onProviderDisabled(p: String?) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Log.i("IMGLAB", "Sensor event in: ${event?.sensor?.name}")

        val r = FloatArray(9)
        val i = FloatArray(9)

        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic = event.values
            }
        }

        if (mGeomagnetic != null && mGravity != null) {
            if (SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation);

                val azimuth = orientation[0]
                SensorManager.getOrientation(r, orientation)

                headingToNorth = -azimuth * 360 / (2 * 3.14159f)

                //Log.i("IMGLAB", "Heading set: $headingToNorth")
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}