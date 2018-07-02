package com.skateboard.skybox

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import java.lang.Math.cos
import java.lang.Math.sin

class SkyBoxView(context: Context, attributeSet: AttributeSet?) : GLSurfaceView(context, attributeSet), SurfaceTexture.OnFrameAvailableListener
{


    private lateinit var sensorManager: SensorManager

    private lateinit var sensor: Sensor

    private var picth=0.0f

    private var yaw=0.0f

    constructor(context: Context) : this(context, null)

    init
    {
        initSensor()
        initConfig()
    }

    private val sensorEventListener = object : SensorEventListener
    {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
        {

        }

        override fun onSensorChanged(event: SensorEvent?)
        {
            if(event!=null)
            {
                // This timestep's delta rotation to be multiplied by the current rotation
                // after computing it from the gyro sample data.
//                if (timestamp !== 0)
//                {
//                    val dT = (event.timestamp - timestamp) * NS2S
//                    // Axis of the rotation sample, not normalized yet.
//                    var axisX = event.values[0]
//                    var axisY = event.values[1]
//                    var axisZ = event.values[2]
//
//                    // Calculate the angular speed of the sample
//                    val omegaMagnitude = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)
//
//                    // Normalize the rotation vector if it's big enough to get the axis
//                    // (that is, EPSILON should represent your maximum allowable margin of error)
//                    if (omegaMagnitude > EPSILON)
//                    {
//                        axisX /= omegaMagnitude
//                        axisY /= omegaMagnitude
//                        axisZ /= omegaMagnitude
//                    }
//
//                    // Integrate around this axis with the angular speed by the timestep
//                    // in order to get a delta rotation from this sample over the timestep
//                    // We will convert this axis-angle representation of the delta rotation
//                    // into a quaternion before turning it into the rotation matrix.
//                    val thetaOverTwo = omegaMagnitude * dT / 2.0f
//                    val sinThetaOverTwo = sin(thetaOverTwo)
//                    val cosThetaOverTwo = cos(thetaOverTwo)
//                    deltaRotationVector[0] = sinThetaOverTwo * axisX
//                    deltaRotationVector[1] = sinThetaOverTwo * axisY
//                    deltaRotationVector[2] = sinThetaOverTwo * axisZ
//                    deltaRotationVector[3] = cosThetaOverTwo
//                }
//                timestamp = event.timestamp
//                val deltaRotationMatrix = FloatArray(9)
//                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector)
//                // User code should concatenate the delta rotation we computed with the current rotation
//                // in order to get the updated rotation.
//                // rotationCurrent = rotationCurrent * deltaRotationMatrix;


            }

        }
    }

    private fun initSensor()
    {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    }

    private fun initConfig()
    {
        setEGLContextClientVersion(3)
//        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        setRenderer(SkyBoxRender(context))
    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?)
    {
        requestRender()
    }

    override fun onResume()
    {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, sensor, SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener, sensor)
    }
}