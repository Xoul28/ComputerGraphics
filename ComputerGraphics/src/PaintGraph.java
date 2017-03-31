import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PaintGraph extends JPanel {

    private int scale, nx, paddingY, paddingX, lengthY, lengthX, center, biasForSecondSystem;
    private ArrayList<Point> polygonPoints;
    private float halfOfX, halfOfY, stepX;
    private boolean drawGrid, drawCoords;
    private boolean drawSteps;
    private boolean drawSystem;

    public PaintGraph() {
        scale = 20;// цена деления  по шкалам
        halfOfY = (float) 0.5; // коэф шкалы по у
        halfOfX = (float) 0.5; // коэф шкалы по x
        paddingY = 50; // начальный отступ по y
        paddingX = 50; //начальный отступ по х
        lengthY = 500; // длина оси у
        lengthX = 500; // длина оси х
        // по умолчанию в начале на экран выводится график y=x
        stepX = (float) 0.011;//шаг табуляции
        drawGrid = true;
        drawSteps = true;
        drawSystem = true;
        center = (int) (lengthX * halfOfX + paddingX);
        biasForSecondSystem = 600;
        polygonPoints = new ArrayList<>();
        drawCoords = true;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        super.paint(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        if (drawSystem) {
            drawSystem(g, 0);
            drawSystem(g, biasForSecondSystem);
//            drawRoundSystem(g);
        }
        if (drawSteps) {
            drawSteps(g, 0);
            drawSteps(g, biasForSecondSystem);
        }
        if (drawGrid) {
            drawGrid(g, 0);
            drawGrid(g, biasForSecondSystem);
//            drawRoundGrid(g);
        }
        if (drawCoords) {
            drawCoords(g, 0);
            drawCoords(g, biasForSecondSystem);
        }
        drawPolygon(g);
//        funcCar(g);
    }

    private void drawCoords(Graphics2D g, int biasForSecondSystem) {
        for (Point p : polygonPoints) {
            Font currentFont = g.getFont();
            g.setFont(new Font("Calibri", 1, 15));
            g.drawString(p.x + " " + p.y, p.x + biasForSecondSystem, p.y + biasForSecondSystem);
            g.setFont(currentFont);
        }
    }

    void drawPolygon(Graphics2D g) {
        int[] xPoints = new int[polygonPoints.size()];
        int[] yPoints = new int[polygonPoints.size()];
        int i = 0;
        for (Point p : polygonPoints) {
            xPoints[i] = p.x;
            yPoints[i] = p.y;
            i++;
        }
        Polygon polygon = new Polygon(xPoints, yPoints, polygonPoints.size());
        Stroke currentStroke = g.getStroke();
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(polygon);
        g.setStroke(currentStroke);
        if (spin) {
            g.fillPolygon(polygon);
        }
    }

    //p=2a(1 + cosf);
    // Метод рисующий кардиоиду

    void funcCar(Graphics2D g) {
        double previousX, previousY, currentX, currentY, t;
        currentX = 0;
        currentY = 0;
        previousX = 0;
        previousY = 0;
        t = 0;
//        while ((t + stepX) * scale < 0 && Math.pow(t + stepX, 2) * scale < lengthY * halfOfY) {
        while (t < 360) {
            double cos = Math.cos(Math.toRadians(t));
            double cos2 = Math.cos(Math.toRadians(2 * t));
            double sin = Math.sin(Math.toRadians(t));
            double sin2 = Math.sin(Math.toRadians(2 * t));

            int a = 50;
            currentX = 2 * scale * cos - scale * cos2;
            currentY = 2 * scale * sin - scale * sin2;
            int x1 = (int) (center + currentX);
            int y1 = (int) (center + currentY);
            int x2 = (int) (center + previousX);
            int y2 = (int) (center + previousY);
            g.drawLine(x1, y1, x2, y2);
            previousX = currentX;
            previousY = currentY;
            t = t + stepX;
        }

    }

    public void drawSteps(Graphics2D g, int bias) {
        //y
        int k1 = lengthY / scale / 2;
        int number = -k1;
        for (int i = number; i < k1; i++, number++) {
            //vertical
            g.drawLine((int) (paddingX + halfOfX * lengthX - 2) + bias, (int) (lengthY * halfOfY + paddingY - scale * i),
                    (int) (paddingX + halfOfX * lengthX + 2) + bias, (int) (lengthY * halfOfY + paddingY - scale * i)
            );

            g.drawString(number + "", (int) (paddingX + halfOfX * lengthX + 4), (int) (lengthY * halfOfY + paddingY - scale * i) + 10);
        }

        //x
        k1 = lengthX / scale / 2;
        number = -k1;
        for (int i = number; i < k1; i++, number++) {
            //right
            g.drawLine((int) (center + scale * i) + bias, (int) (paddingY + halfOfY * lengthY - 2),
                    (int) (center + scale * i) + bias, (int) (paddingY + halfOfY * lengthY + 2)
            );
            g.drawString(number + "", (int) (center + scale * i + 4) + bias, (int) (paddingY + halfOfY * lengthY) + 10);

        }
    }

    public void drawGrid(Graphics2D g, int bias) {
        int k1 = lengthX / scale / 2;
        int number = -k1;
        Stroke stroke = g.getStroke();
        float[] dashl = {2, 2};
        BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
        g.setStroke(pen);
        for (int i = number; i <= k1; i++) {
            //vertical
            // right
            g.drawLine((int) (center + scale * i) + bias, paddingY,
                    (int) (center + scale * i) + bias, lengthY + paddingY
            );
            g.drawLine(paddingX + bias, (int) (lengthY * halfOfY + paddingY + scale * i),
                    lengthX + paddingX + bias, (int) (lengthY * halfOfY + paddingY + scale * i)
            );
        }
        g.setStroke(stroke);
    }

    public void drawSystem(Graphics g, int bias) {
        // Ось Y
        g.drawLine((int) (center) + bias, paddingY, (int) (center) + bias, lengthY + paddingY);
        // Стрелки
        g.drawLine((int) (center) + bias, paddingY, (int) (center) - 3 + bias, paddingY + 10);
        g.drawLine((int) (center) + bias, paddingY, (int) (center) + 3 + bias, paddingY + 10);
        // Надпись
        g.drawString("Y", (int) (center) - 10, paddingY + 10);

        // Ось Х
        g.drawLine(paddingX + bias, (int) (lengthY * halfOfY + paddingY), lengthX + paddingX + bias, (int) (lengthY * halfOfY + paddingY));
        // Стрелки
        g.drawLine(lengthX + paddingX + bias, (int) (lengthY * halfOfY + paddingY), lengthX + paddingX - 10 + bias, (int) (lengthY * halfOfY + paddingY) - 3);
        g.drawLine(lengthX + paddingX + bias, (int) (lengthY * halfOfY + paddingY), lengthX + paddingX - 10 + bias, (int) (lengthY * halfOfY + paddingY) + 3);
        // Надпись
        g.drawString("Х", lengthX + paddingY - 10 + bias, (int) (lengthY * halfOfY + paddingY) - 10);
    }

    void drawRoundGrid(Graphics2D g) {
        Stroke stroke = g.getStroke();
        float[] dashl = {2, 2};
        BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
        g.setStroke(pen);

        int k1 = lengthY / scale / 2;
        for (int i = 0; i < k1; i++) {
            int width = scale * i;
            g.drawOval((int) (center) - width, (int) (lengthY * halfOfX + paddingY) - width, width * 2, width * 2);
        }
        g.setStroke(stroke);

    }

    void drawRoundSystem(Graphics2D g) {
        Stroke stroke = g.getStroke();
        float[] dashl = {2, 2};
        BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
        g.setStroke(pen);
        for (int i = 0; i < 360; i += 30) {
            double cos = Math.cos(Math.toRadians(i));
            double sin = Math.sin(Math.toRadians(i));

            int pointToRollX = lengthX + paddingX;
            int pointToRollY = (int) (lengthY * halfOfY + paddingY);
            int length = (int) (lengthX * halfOfX);
            //x * cos(0)
            int x1 = (int) (length * cos);
            //y * sin(0)
            int y1 = (int) (0 * sin);

            //x * cos(0)
            int x2 = (int) (length * sin);
            //y * sin(0)
            int y2 = (int) (0 * cos);

            int newX2 = x1 - y1;
            int newY2 = y2 + x2;

            newX2 = newX2 + center;
            newY2 = newY2 + center;
            g.drawLine(center, center, newX2, newY2);
            g.drawLine(center, center, pointToRollX, pointToRollY);

        }
        g.setStroke(stroke);
    }

    void addPointToList(Point point) {
        boolean isInLeftBorders = point.x > paddingX && point.x < paddingX + lengthX && point.y > paddingY && point.y < paddingY + lengthY;
        if (isInLeftBorders) {
            polygonPoints.add(point);
        }
    }

    void clear() {
        polygonPoints.clear();
    }


    // группа getXXX(), setXXX() - методов
    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public float getHalfOfY() {
        return halfOfY;
    }

    public void setHalfOfY(float halfOfY) {
        this.halfOfY = halfOfY;
    }

    public float getHalfOfX() {
        return halfOfX;
    }

    public void setHalfOfX(float halfOfX) {
        this.halfOfX = halfOfX;
    }

    public float getStepX() {
        return stepX;
    }

    public void setStepX(float stepX) {
        this.stepX = stepX;
    }

    public int getLengthX() {
        return lengthX;
    }

    public void setLengthX(int lengthX) {
        this.lengthX = lengthX;
    }

    public int getLengthY() {
        return lengthY;
    }

    public void setLengthY(int lengthY) {
        this.lengthY = lengthY;
    }

    public int getPaddingY() {
        return paddingY;
    }

    public void setPaddingY(int paddingY) {
        this.paddingY = paddingY;
    }

    public int getPaddingX() {
        return paddingX;
    }

    public void setPaddingX(int paddingX) {
        this.paddingX = paddingX;
    }

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public boolean isSpin() {
        return spin;
    }

    public void setSpin(boolean spin) {
        this.spin = spin;
    }

    private boolean spin;

    public boolean isDrawSteps() {
        return drawSteps;
    }

    public void setDrawSteps(boolean drawSteps) {
        this.drawSteps = drawSteps;
    }

    public boolean isDrawSystem() {
        return drawSystem;
    }

    public void setDrawSystem(boolean drawSystem) {
        this.drawSystem = drawSystem;
    }

    public boolean isDrawCoords() {
        return drawCoords;
    }

    public void setDrawCoords(boolean drawCoords) {
        this.drawCoords = drawCoords;
    }
}
