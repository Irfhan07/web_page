package com.example.calculator;

import java.util.ArrayList;
import java.util.List;

public class FibonacciCalculator {

    // Class to store Arc details
    public static class QuadrantArc {
        public long radius; 
        public List<Double> X = new ArrayList<>();
        public List<Double> Y = new ArrayList<>();

        public QuadrantArc(long radius) {
            this.radius = radius;
        }
    }

    public static List<QuadrantArc> calculateFibonacci(int n) {
        // ==========================================
        // UPDATE: No strict limit here (Handled in Generator)
        // But we ensure n is at least 0
        // ==========================================
        if (n < 0) n = 0;

        List<QuadrantArc> allArcs = new ArrayList<>();
        
        if (n == 0) return allArcs;

        // Fibonacci Sequence (Using Long to support N=35 huge numbers)
        List<Long> fib = new ArrayList<>();
        fib.add(1L);
        if (n > 1) fib.add(1L);
        
        for (int i = 2; i < n; i++) {
            long nextFib = fib.get(i - 1) + fib.get(i - 2);
            fib.add(nextFib);
        }

        double currentX = 0.0;
        double currentY = 0.0;

        // Arcs Calculation
        for (int i = 0; i < n; i++) {
            long r = fib.get(i);
            QuadrantArc arc = new QuadrantArc(r);

            double startAngleDeg = (i % 4) * 90.0;
            double endAngleDeg = startAngleDeg + 90.0;
            double startRad = Math.toRadians(startAngleDeg);

            // Center calculation (Continuity Logic)
            double cx = currentX - (r * Math.cos(startRad));
            double cy = currentY - (r * Math.sin(startRad));

            // Generate points (Step size 1.0 for smoothness)
            for (double a = startAngleDeg; a <= endAngleDeg; a += 1.0) {
                double rad = Math.toRadians(a);
                double px = cx + r * Math.cos(rad);
                double py = cy + r * Math.sin(rad);
                
                arc.X.add(px);
                arc.Y.add(py);

                currentX = px;
                currentY = py;
            }
            allArcs.add(arc);
        }

        return allArcs;
    }
}