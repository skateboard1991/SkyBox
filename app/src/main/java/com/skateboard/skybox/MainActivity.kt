package com.skateboard.skybox

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume()
    {
        super.onResume()
        skyBox.onResume()
    }

    override fun onPause()
    {
        super.onPause()
        skyBox.onPause()
    }
}
