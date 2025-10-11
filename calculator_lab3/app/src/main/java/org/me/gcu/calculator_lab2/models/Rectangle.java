package org.me.gcu.calculator_lab2.models;

import androidx.annotation.NonNull;

public class Rectangle {
    private float length;
    private float width;

    public float getLength() { return length; }
    public void setLength(float length) { this.length = length; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getArea() { return length * width; }

    @NonNull
    @Override
    public String toString() {
        return "Rectangle: " + length + " x " + width + " (Area: " + getArea() + ")";
    }
}
