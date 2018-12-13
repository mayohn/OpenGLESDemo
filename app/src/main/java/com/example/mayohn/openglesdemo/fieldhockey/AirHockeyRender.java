package com.example.mayohn.openglesdemo.fieldhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.mayohn.openglesdemo.R;
import com.example.mayohn.openglesdemo.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;
    private Context context;

    public AirHockeyRender(Context context) {
        this.context = context;
        float[] tableVertices = {
                0f, 0f,
                0f, 14f,
                9f, 14f,
                9f, 0f
        };
        float[] tableVerticesWithTriangles = {
                //第一个三角形
                0f, 0f,
                9f, 14f,
                0f, 14f,
                //第二个三角形
                0f, 0f,
                9f, 0f,
                9f, 14f,
                //中间线
                0f, 7f,
                9f, 7f,
                //两个木槌
                4.5f, 2f,
                4.5f, 12f
        };
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(1f, 0f, 0f, 0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

    }
}
