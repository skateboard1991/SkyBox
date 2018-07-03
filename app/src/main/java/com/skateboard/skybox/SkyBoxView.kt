package com.skateboard.skybox

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent


class SkyBoxView(context: Context, attributeSet: AttributeSet?) : GLSurfaceView(context, attributeSet), SurfaceTexture.OnFrameAvailableListener
{


    private lateinit var sensorManager: SensorManager

    private lateinit var accSensor: Sensor

    private lateinit var magneticSensor:Sensor

    private val R=FloatArray(9)

    private val degrees=FloatArray(3)

    private var acc=FloatArray(3)

    private var magnetic=FloatArray(3)

    private lateinit var skyBoxRender: SkyBoxRender

    private var lastX=0F

    private var lastY=0F

    private var yaw=0f

    private var pitch=0f

    private var screenWidth=0

    private var screenHeight=0

    private var horSensity=0.03f

    private var verSensity=0.03f

    constructor(context: Context) : this(context, null)

    init
    {
//        initSensor()
        initSensity()
        initConfig()
    }

    private fun initSensity()
    {
        screenWidth=resources.displayMetrics.widthPixels
        screenHeight=resources.displayMetrics.heightPixels
        horSensity= 360.0f/screenWidth
        verSensity=180.0f/screenHeight
    }

//    private val sensorEventListener = object : SensorEventListener
//    {
//        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
//        {
//
//        }
//
//        override fun onSensorChanged(event: SensorEvent?)
//        {
//            if(event!=null)
//            {
//
//                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
//                    magnetic = event.values
//                }
//                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
//                    acc = event.values
//                    calValue()
//                    rotate()
//                }
//
//            }
//
//        }
//    }
//
//    private fun calValue()
//    {
//        SensorManager.getRotationMatrix(R,null,acc,magnetic)
//        SensorManager.getOrientation(R,degrees)
//        for(i in degrees)
//        {
//            println("degree is ${Math.toDegrees(i.toDouble())}")
//        }
//    }

    private fun rotate(pitch:Float,yaw:Float)
    {
        queueEvent {

            skyBoxRender.rotate(pitch,yaw)
        }
    }

//    private fun initSensor()
//    {
//
//        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//
//    }

    private fun initConfig()
    {
        setEGLContextClientVersion(3)
        skyBoxRender=SkyBoxRender(context)
        setRenderer(skyBoxRender)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?)
    {
        requestRender()
    }

    override fun onResume()
    {
        super.onResume()
//        sensorManager.registerListener(sensorEventListener, accSensor, SENSOR_DELAY_NORMAL)
//        sensorManager.registerListener(sensorEventListener,magneticSensor,SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
//        sensorManager.unregisterListener(sensorEventListener, accSensor)
//        sensorManager.unregisterListener(sensorEventListener,magneticSensor)
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        when(event?.action)
        {
            MotionEvent.ACTION_DOWN->
            {
                lastX=event.x
                lastY=event.y
                return true
            }

            MotionEvent.ACTION_MOVE->
            {
                val offsetX=event.x-lastX
                val offsetY=lastY-event.y
                yaw+=offsetX*horSensity
                pitch+=offsetY*verSensity
                lastX=event.x
                lastY=event.y
                skyBoxRender.rotate(pitch,yaw)
            }
        }

        return true
    }

}