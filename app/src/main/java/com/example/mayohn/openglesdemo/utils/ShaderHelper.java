package com.example.mayohn.openglesdemo.utils;

import android.opengl.GLES20;
import android.util.Log;

import java.util.logging.Logger;

import static android.opengl.GLES20.*;

public class ShaderHelper {
    public static final String TAG = "ShaderHelper";


    public int compileVertexShaderd(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public int compileFragmentShaderd(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.i(TAG, "compileShader: could not create new shader" + glGetError());
            }
        } else {
            glShaderSource(shaderObjectId, shaderCode);
            glCompileShader(shaderObjectId);
            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
            if (LoggerConfig.ON) {
                Log.i(TAG, "compileShader: Results of compile source :\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));
            }
            if (compileStatus[0] == 0) {
                glDeleteShader(shaderObjectId);
                if (LoggerConfig.ON) {
                    Log.i(TAG, "compileShader: compile of shader failed.");
                }
                return 0;
            }
        }
        return shaderObjectId;
    }

    public int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.i(TAG, "linkProgram: could not create program.");
            }
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (LoggerConfig.ON) {
            Log.i(TAG, "linkProgram: results of link program :\n" + glGetProgramInfoLog(programObjectId));
        }
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.i(TAG, "linkProgram: linking of program failed.");
            }
            return 0;
        }
        return programObjectId;
    }

    public boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.i(TAG, "validateProgramf: results of validate program:" + validateStatus[0] + "\nlog:" + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
