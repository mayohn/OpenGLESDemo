package com.example.mayohn.openglesdemo.fieldhockey;

import android.content.Context;
import android.graphics.Color;
import android.media.tv.TvContract;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;
    private Context context;
    private int program;
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int uColorLocation;
    private int aPositionLocation;
    private ShaderHelper shaderHelper;
    private String backgroundColor = "#ffffff";
    private String deskColor = "#FFDAB9";
    private String middleLineColor = "#000000";
    private String pointColor = "#000000";
    private GLColor backgroundGlColor;
    private GLColor deskGlColor;
    private GLColor middleLineGlColor;
    private GLColor pointGlColor;

    public AirHockeyRender(Context context) {
        this.context = context;
        backgroundGlColor = new GLColor(backgroundColor);
        deskGlColor = new GLColor(deskColor);
        middleLineGlColor = new GLColor(middleLineColor);
        pointGlColor = new GLColor(pointColor);
        float[] tableVerticesWithTriangles = {
                //第一个三角形
                -0.5f, 0.5f,
                -0.5f, -0.5f,
                0.5f, 0.5f,
                //第二个三角形
                0.5f, -0.5f,
                -0.5f, -0.5f,
                0.5f, 0.5f,
                //中间线
                -0.5f, 0f,
                0.5f, 0f,
                //两个木槌
                0f, 0.25f,
                0f, -0.25f,
                //外边框top直线
                - 0.52f, 0.52f,
                0.52f, 0.52f,
                //外边框left直线
                -0.52f, 0.52f,
                -0.52f, -0.52f,
                //外边框bottom直线
                0.52f, -0.52f,
                -0.52f, -0.52f,
                //外边框right直线
                0.52f, 0.52f,
                0.52f, -0.52f,
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
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        //桌子
        glUniform4f(uColorLocation, deskGlColor.getRed(), deskGlColor.getGreen(), deskGlColor.getBlue(), deskGlColor.getAlpha());
        glDrawArrays(GL_TRIANGLES, 0, 6);
        //分割线
        glUniform4f(uColorLocation, middleLineGlColor.getRed(), middleLineGlColor.getGreen(), middleLineGlColor.getBlue(), middleLineGlColor.getAlpha());
        glDrawArrays(GL_LINES, 6, 2);
        //两个点
        glUniform4f(uColorLocation, pointGlColor.getRed(), pointGlColor.getGreen(), pointGlColor.getBlue(), pointGlColor.getAlpha());
        glDrawArrays(GL_POINTS, 8, 2);
        //top线
        glUniform4f(uColorLocation, middleLineGlColor.getRed(), middleLineGlColor.getGreen(), middleLineGlColor.getBlue(), middleLineGlColor.getAlpha());
        glLineWidth(7);
        glDrawArrays(GL_LINES, 10, 8);

    }
}
