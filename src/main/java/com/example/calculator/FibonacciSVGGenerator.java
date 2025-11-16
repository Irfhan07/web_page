package com.example.calculator;

import java.util.List;

public class FibonacciSVGGenerator {

    /* Rainbow palette (10 distinct colours, cycles) */
    private static final String[] COLORS = {
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA502", "#2ED573",
        "#A8D8EA", "#FF9FF3", "#5F27CD", "#FFDA79", "#54A0FF"
    };

    /** create SVG for N (1-35) Fibonacci quadrant spiral */
    public static String generateSVG(int n, boolean unitsMode, boolean generatorMode) {

        final int SIZE = 700;           // canvas 700×700
        final double M  = 60;           // margin
        final double US = SIZE - 2*M;   // usable square

        /* ───────────────── SVG header ───────────────── */
        StringBuilder svg = new StringBuilder();
        svg.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        svg.append("<svg xmlns='http://www.w3.org/2000/svg' ")
           .append("width='").append(SIZE).append("' height='").append(SIZE)
           .append("' viewBox='0 0 ").append(SIZE).append(" ").append(SIZE).append("'>\n");

        /* ①  Light GRID background (10 px spacing) */
        svg.append("  <defs>\n");
        svg.append("    <pattern id='grid' width='10' height='10' patternUnits='userSpaceOnUse'>\n");
        svg.append("      <path d='M 10 0 L 0 0 0 10' fill='none' stroke='#e8e8e8' stroke-width='0.7'/>\n");
        svg.append("    </pattern>\n");
        svg.append("  </defs>\n");
        svg.append("  <rect width='100%' height='100%' fill='url(#grid)'/>\n");
        svg.append("  <rect x='0.5' y='0.5' width='").append(SIZE-1)
           .append("' height='").append(SIZE-1)
           .append("' fill='none' stroke='#ddd' stroke-width='1'/>\n");

        /* ②  Validation 1-35 */
        if (n < 1 || n > 35) {
            svg.append("<text x='").append(SIZE/2).append("' y='").append(SIZE/2)
               .append("' font-family='Arial' font-size='20' font-weight='bold' text-anchor='middle' fill='red'>")
               .append("Enter N between 1 and 30</text>\n</svg>");
            return svg.toString();
        }

        /* ③  Compute quarter-circle arcs */
        List<FibonacciCalculator.QuadrantArc> arcs = FibonacciCalculator.calculateFibonacci(n);

        /* bounds & scale */
        double minX=1e9,minY=1e9,maxX=-1e9,maxY=-1e9;
        for (var a : arcs) {
            for (double v : a.X) { if (v < minX) minX = v; if (v > maxX) maxX = v; }
            for (double v : a.Y) { if (v < minY) minY = v; if (v > maxY) maxY = v; }
        }
        double scale = US / Math.max(maxX - minX, maxY - minY);
        double ox = M + (0 - minX) * scale;
        double oy = M + (maxY - 0) * scale;

        /* ④  Full-length dashed axes */
        svg.append("  <g stroke='#888' stroke-width='1.2' stroke-dasharray='6,6'>\n");
        svg.append("    <line x1='0' y1='").append(fmt(oy)).append("' x2='").append(SIZE)
           .append("' y2='").append(fmt(oy)).append("'/>\n");
        svg.append("    <line x1='").append(fmt(ox)).append("' y1='0' x2='")
           .append(fmt(ox)).append("' y2='").append(SIZE).append("'/>\n");
        svg.append("  </g>\n")
           .append("  <circle cx='").append(fmt(ox)).append("' cy='").append(fmt(oy))
           .append("' r='3' fill='#555'/>\n");

        /* ⑤  Rainbow spiral + number bubbles */
        int colIdx = 0;
        for (var a : arcs) {
            String col = COLORS[colIdx++ % COLORS.length];

            svg.append("  <path d='");
            boolean first = true;
            for (int i = 0; i < a.X.size(); i++) {
                double px = M + (a.X.get(i) - minX) * scale;
                double py = M + (maxY - a.Y.get(i)) * scale;
                svg.append(first ? "M " : " L ").append(fmt(px)).append(" ").append(fmt(py));
                first = false;
            }
            svg.append("' stroke='").append(col)
               .append("' stroke-width='3' fill='none' stroke-linecap='round'/>\n");

            int mid = a.X.size() / 2;
            double lx = M + (a.X.get(mid) - minX) * scale;
            double ly = M + (maxY - a.Y.get(mid)) * scale;
            svg.append("  <circle cx='").append(fmt(lx)).append("' cy='").append(fmt(ly))
               .append("' r='10' fill='white' stroke='").append(col).append("' stroke-width='1.3'/>\n");
            svg.append("  <text x='").append(fmt(lx)).append("' y='").append(fmt(ly+3))
               .append("' font-family='Arial' font-size='9' font-weight='bold' text-anchor='middle' fill='#333'>")
               .append(a.radius).append("</text>\n");
        }

        /* ⑥  Title */
        svg.append("  <text x='").append(M).append("' y='").append(M-25)
           .append("' font-family='Arial' font-size='15' font-weight='bold' fill='#444'>")
           .append("Fibonacci Spiral (N=").append(n).append(")</text>\n");

        svg.append("</svg>");
        return svg.toString();
    }

    private static String fmt(double v) { return String.format("%.3f", v); }
}
