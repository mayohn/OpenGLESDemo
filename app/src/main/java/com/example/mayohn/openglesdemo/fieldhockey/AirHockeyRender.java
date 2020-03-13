package com.example.mayohn.openglesdemo.fieldhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.orthoM;


import com.example.mayohn.openglesdemo.R;
import com.example.mayohn.openglesdemo.model.GLColor;
import com.example.mayohn.openglesdemo.utils.LoggerConfig;
import com.example.mayohn.openglesdemo.utils.ShaderHelper;
import com.example.mayohn.openglesdemo.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final String A_COLOR = "a_Color";
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
    private int aColorLocation;
    private FloatBuffer vertexData;
    private Context context;
    private int program;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private ShaderHelper shaderHelper;
    private String backgroundColor = "#000000";
    private GLColor backgroundGlColor;


    public AirHockeyRender(Context context) {
        this.context = context;
        backgroundGlColor = new GLColor(backgroundColor);

        float[] tableVerticesWithTriangles = {
                //中心点
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                //中间线
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
                //两个木槌
                0f, 0.25f, 0f, 0f, 1f,
                0f, -0.25f, 1f, 0f, 0f
        };
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(backgroundGlColor.getRed(), backgroundGlColor.getGreen(), backgroundGlColor.getBlue(), backgroundGlColor.getAlpha());
        shaderHelper = new ShaderHelper();
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        int vertexShader = shaderHelper.compileVertexShaderd(vertexShaderSource);
        int fragmentShader = shaderHelper.compileFragmentShaderd(fragmentShaderSource);
        program = shaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.ON) {
            shaderHelper.validateProgram(program);
        }
        glUseProgram(program);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        //桌子
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        //分割线
        glDrawArrays(GL_LINES, 6, 2);
        //两个点
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);


    }
}
