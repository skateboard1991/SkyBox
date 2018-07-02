package com.skateboard.skybox

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import java.io.File
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SkyBoxRender(private val context: Context) : GLSurfaceView.Renderer
{
    private var program: Int = 0

    private var vao: Int = 0

    private var pos = floatArrayOf(

            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    )

    private var texture: Int = 0

    private val SLGL = "slgl"

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)
    {
        val vertexPath = context.applicationContext.packageCodePath + File.separator + SLGL + File.separator + "vertex.slgl"
        val fragmentPath = context.applicationContext.packageCodePath + File.separator + SLGL + File.separator + "fragment.slgl"
        program = genProgram(vertexPath, fragmentPath)
        vao = preparePos(pos)
        texture = prepareTexture()
        val bitmapList = preparePics()
        for (i in 0 until bitmapList.size)
        {
            val bitmap = bitmapList[i]
            GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GLES30.GL_RGB, bitmap, GLES30.GL_UNSIGNED_BYTE, 0)
        }
    }

    private fun preparePics(): List<Bitmap>
    {
        val bitmapList = mutableListOf<Bitmap>()

        val picList = mutableListOf(

                R.drawable.right,
                R.drawable.left,
                R.drawable.top,
                R.drawable.bottom,
                R.drawable.back,
                R.drawable.front
        )
        for (i in picList)
        {
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeResource(context.resources, i, options)
            bitmapList.add(bitmap)
        }

        return bitmapList
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)
    {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?)
    {
        draw(program, vao)
    }

    companion object
    {

        // Used to load the 'native-lib' library on application startup.
        init
        {
            System.loadLibrary("skybox")
        }
    }


    external fun genProgram(vertexPath: String, fragmentPath: String): Int

    external fun prepareTexture(): Int

    external fun preparePos(pos: FloatArray): Int

    external fun draw(program: Int, vao: Int)

    external fun stringFromJNI():String

}