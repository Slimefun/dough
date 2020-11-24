package io.github.thebusybiscuit.cscorelib2.math.vectors;

import lombok.Data;
import lombok.NonNull;

@Data
public class Vector2 {

    private double x;
    private double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public double distance(@NonNull Vector2 v) {
        return Math.sqrt(distanceSquared(v));
    }

    public double distanceSquared(@NonNull Vector2 v) {
        return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
    }

    public double dotProduct(@NonNull Vector2 v) {
        return x * v.x + y * v.y;
    }

    public void normalize() {
        double length = magnitude();

        this.x /= length;
        this.y /= length;
    }

}
