package com.example.mayohn.openglesdemo.point;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.mayohn.openglesdemo.BaseActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PointActivity extends BaseActivity {
    private float vertices[] = {
            -0.5f, 0.5f, 0.0f,//左上角
            -0.5f, -0.5f, 0.0f,//左下角
            0.5f, -0.5f, 0.0f,//右下角
            0.5f, 0.5f, 0.0f//右上角
    };
    private GLSurfaceView surfaceView;
    //顶点数组
//    private float[] mArrayVertex = {0f, 0f, 0f};
    private FloatBuffer vertexBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //浮点数是4个字节，因此我们将,如果顶点数为4。
        surfaceView = new GLSurfaceView(this);
        surfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                gl10.glClearColor(0.0f, 0, 0.0f, 0.5f);
//                gl10.glShadeModel(GL10.GL_SMOOTH);//两点之间颜色过度效果
//                gl10.glClearDepthf(1.0f);//深度缓冲设置
//                gl10.glEnable(GL10.GL_DEPTH_TEST);// Enables depth testing.
//                gl10.glDepthFunc(GL10.GL_LEQUAL);//要做的深度测试类型。
//                gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);//非常好的透视计算。
            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                gl10.glViewport(0, 0, width / 2, height);//将当前视图端口设置为新大小。
//                gl10.glMatrixMode(GL10.GL_PROJECTION);//选择投影矩阵
//                gl10.glLoadIdentity();//重置投影矩阵
//                GLU.gluPerspective(gl10, 45.0f, (float) width / (float) height, 0.1f, 100.0f);//计算窗口的高宽比
//                gl10.glMatrixMode(GL10.GL_MODELVIEW);//选择模型视图矩阵
//                gl10.glLoadIdentity();//重置投影矩阵

            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);//清除屏幕和深度缓冲区。
                gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);//启用顶点缓冲区，以便写入并在呈现过程中使用。
                gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, getFloatBuffer(vertices));//指定顶点数组的位置和数据格式。渲染时使用的坐标
                gl10.glColor4f(1f, 0f, 0f, 0f);
                gl10.glPointSize(100f);
                gl10.glDrawArrays(GL10.GL_POINTS, 0, 4);
                gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);//禁用顶点缓冲区。
            }
        });
        setContentView(surfaceView);

    }

    /**
     * @param vertexes float 数组
     * @return 获取浮点形缓冲数据
     */
    public static FloatBuffer getFloatBuffer(float[] vertexes) {
        FloatBuffer buffer;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexes.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        buffer = vbb.asFloatBuffer();
        //写入数组
        buffer.put(vertexes);
        //设置默认的读取位置
        buffer.position(0);
        return buffer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (surfaceView != null) {
            surfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (surfaceView != null) {
            surfaceView.onPause();

        }
    }
}
