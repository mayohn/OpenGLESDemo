package com.example.mayohn.openglesdemo.model;

import android.graphics.Color;

public class GLColor {
    private int glColor;

    public GLColor(String color) {
        glColor = Color.parseColor(color);
    }

    public float getRed() {
        return ((glColor >> 16) & 0xFF) / 255f;
    }


    public float getGreen() {
        return ((glColor >> 8) & 0xFF) / 255f;
    }


    public float getBlue() {
        return ((glColor >> 0) & 0xFF) / 255f;
    }


    public float getAlpha() {
        return ((glColor >> 24) & 0xFF) / 255f;
    }
}
