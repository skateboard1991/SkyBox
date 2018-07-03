package com.skateboard.skybox

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import java.io.BufferedReader
import java.io.InputStreamReader
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

    private var width = 0

    private var height = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)
    {
        rotate(0f,0f)
        val vertexSource = readSlglContent("vertex.slgl")
        val fragmentSource = readSlglContent("fragment.slgl")
        program = genProgram(vertexSource, fragmentSource)
        vao = preparePos(pos)
        texture = prepareTexture()
        val bitmapList = preparePics()
        for (i in 0 until bitmapList.size)
        {
            val bitmap = bitmapList[i]
            GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GLES30.GL_RGBA, bitmap, GLES30.GL_UNSIGNED_BYTE, 0)
            bitmap.recycle()
        }
    }

    private fun readSlglContent(file: String): String
    {
        val stringBuilder = StringBuilder()
        try
        {
            val vertexInput = BufferedReader(InputStreamReader(context.assets.open(file)))
            var content = vertexInput.readLine()
            while (content.isNotEmpty())
            {
                stringBuilder.append(content).append("\n")
                content = vertexInput.readLine()
            }
            vertexInput.close()
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
        return stringBuilder.toString()
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
        this.width = width
        this.height = height
    }

    override fun onDrawFrame(gl: GL10?)
    {
        draw(program, vao, texture, width.toFloat(), height.toFloat())
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

    external fun draw(program: Int, vao: Int, texture: Int, width: Float, height: Float)

    external fun rotate(pitch: Float, yaw: Float)

}