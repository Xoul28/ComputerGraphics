import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.*;

public class PaintGraph extends JPanel {

    private int scale, nx, paddingY, paddingX, lengthY, lengthX, center, biasForSecondSystem;
    private ArrayList<Point> polygonPoints, transferedPoints, spinPoints, scaledPoints, reflectedPoints;
    private float halfOfX, halfOfY, stepX;
    private boolean drawGrid, drawCoords, drawSystem, drawSteps, doTransfer, spining, scaling;

    Image img;

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
        transferedPoints = new ArrayList<>();
        spinPoints = new ArrayList<>();
        scaledPoints = new ArrayList<>();
        reflectedPoints = new ArrayList<>();
        drawCoords = true;
        doTransfer = false;
        spining = false;

        try {
            img = ImageIO.read(new File("kabam.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        super.paint(g);
//        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
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
        drawPolygon(g, 0, polygonPoints);
        if (doTransfer) {
            drawPolygon(g, biasForSecondSystem, transferedPoints);
        }
        if (spining) {
            drawPolygon(g, biasForSecondSystem, spinPoints);
        }
        if (scaling) {
            drawPolygon(g, biasForSecondSystem, scaledPoints);
        }
        clearBools();
//        funcCar(g);
    }

    private void drawCoords(Graphics2D g, int biasForSecondSystem, ArrayList<Point> polygonPoints) {
        for (Point p : polygonPoints) {
            Font currentFont = g.getFont();
            g.setFont(new Font("Calibri", 0, 17));
            double x = (p.x - lengthX * halfOfX - paddingX) / scale;
            double y = -(p.y - lengthY * halfOfY - paddingY) / scale;
            String formatX = String.format( "% .2f", x);
            String formatY = String.format( "% .2f", y);
            g.drawString(formatX + " : " + formatY, p.x + biasForSecondSystem, p.y);
            g.setFont(currentFont);
        }
    }

    void drawPolygon(Graphics2D g, int biasForSecondSystem, ArrayList<Point> polygonPoints) {
        if (drawCoords) {
            drawCoords(g, biasForSecondSystem, polygonPoints);
        }
        int[] xPoints = new int[polygonPoints.size()];
        int[] yPoints = new int[polygonPoints.size()];
        int i = 0;
        for (Point p : polygonPoints) {
            xPoints[i] = (int) (p.x + biasForSecondSystem);
            yPoints[i] = (int) p.y;
            i++;
        }
        Polygon polygon = new Polygon(xPoints, yPoints, polygonPoints.size());
        Stroke currentStroke = g.getStroke();
        g.setStroke(new BasicStroke());
        g.drawPolygon(polygon);
        g.setStroke(currentStroke);
        if (spin) {
//            g.setColor(Color.red);
//            g.fillPolygon(polygon);
//            g.setColor(Color.black);
        }
    }

    public void tryToTransfer(Point point) {
        if (isPartOfPolygon(point)) {
            doTransfer = true;
        }
    }

    private boolean isPartOfPolygon(Point point) {
        Point previousPoint, currentPoint;
        for (int i = 0; i < polygonPoints.size(); i++) {
            if (i == 0) {
                previousPoint = polygonPoints.get(polygonPoints.size() - 1);
                currentPoint = polygonPoints.get(i);
            } else {
                previousPoint = polygonPoints.get(i - 1);
                currentPoint = polygonPoints.get(i);
            }
            int counts = (point.x - previousPoint.x) * (currentPoint.y - previousPoint.y) -
                    (currentPoint.x - previousPoint.x) * (point.y - previousPoint.y);
            boolean sideContainsPoint = Math.abs(counts) < 600;
            System.out.println(sideContainsPoint);
            if (sideContainsPoint) {
                transfer(new Point(currentPoint.x - previousPoint.x, currentPoint.y - previousPoint.y));
                return true;
            }
        }
        return false;
    }

    private void transfer(Point point) {
        transferedPoints.clear();
        for (Point p : polygonPoints) {
            transferedPoints.add(new Point(p.x + point.x, p.y + point.y));
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

            g.drawString(number + "", (int) (paddingX + halfOfX * lengthX + 4) + bias, (int) (lengthY * halfOfY + paddingY - scale * i) + 10);
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
        transferedPoints.clear();
        spinPoints.clear();
    }


    // группа getXXX(), setXXX() - методов

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public float getStepX() {
        return stepX;
    }

    public void setStepX(float stepX) {
        this.stepX = stepX;
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

    public boolean isDoTransfer() {
        return doTransfer;
    }

    public void setDoTransfer(boolean doTransfer) {
        this.doTransfer = doTransfer;
    }

    public boolean isScaling() {
        return scaling;
    }

    public void setScaling(boolean scaling) {
        this.scaling = scaling;
    }

    public void clearBools() {
        doTransfer = spining = scaling = false;
    }

    public void spin(Point point, int angle) {
        spinPolygon(polygonPoints, point, angle);
        spining = true;
    }


    public void reflect(Point point) {
        double angle = (300 - point.y) / ((double) (point.x) - 300);
        angle = Math.toDegrees(Math.atan(angle));
        Point center = new Point((int) (lengthX * halfOfX + paddingX), (int) (lengthY * halfOfY + paddingY));
        spinPolygon(polygonPoints, center, (int) -angle);
        ArrayList<Point> reflectedPoints = new ArrayList<>();
        for (int i = 0; i < spinPoints.size(); i++) {
            int y = 300 - spinPoints.get(i).y;
            y += 300;
            reflectedPoints.add(new Point(spinPoints.get(i).x, y));
        }
        spinPolygon(reflectedPoints, center, (int) (angle));
        spining = true;
    }

    public void reflectSpin(Point point){
        double angle = (300 - point.y) / ((double) (point.x) - 300);
        angle = Math.toDegrees(Math.atan(angle));
        Point center = new Point((int) (lengthX * halfOfX + paddingX), (int) (lengthY * halfOfY + paddingY));
        spinPolygon(polygonPoints, center, (int) -angle);
        ArrayList<Point> reflectedPoints = new ArrayList<>();
        for (int i = 0; i < spinPoints.size(); i++) {
            int y = 300 - spinPoints.get(i).y;
            y += 300;
            reflectedPoints.add(new Point(spinPoints.get(i).x, y));
        }
        spinPolygon(reflectedPoints, center, (int) (angle));
        spining = true;
    }

    public void scale(double scale) {
        scaledPoints.clear();
        for (Point p : polygonPoints) {
            int x = p.x;
            x = (int) ((x - 300) * scale + 300);
            Point newP = new Point(x, p.y);
            scaledPoints.add(newP);
        }
    }

    public void scaleTimer(double scale){
        if (scaledPoints.isEmpty()) {
            for (Point p : polygonPoints) {
                Point newP = new Point(p.x, p.y);
                scaledPoints.add(newP);
            }
        }
        for (Point p : scaledPoints) {
            int x = p.x;
            x = (int)Math.round(((x - 300) * scale + 300));
            p.x = x;
        }
    }

    void spinPolygon(ArrayList<Point> polygonPoints, Point point, int angle) {
        spinPoints.clear();
        angle = -angle;
        for (Point p : polygonPoints) {
            double cos = Math.cos(Math.toRadians(angle));
            double sin = Math.sin(Math.toRadians(angle));

            int vectorToMoveX = (int) (point.x - lengthX * halfOfX - paddingX);
            int vectorToMoveY = (int) (point.y - lengthY * halfOfY - paddingY);

            int pointToRollX = (int) (p.x - lengthX * halfOfX - paddingX);
            int pointToRollY = (int) (p.y - lengthY * halfOfY - paddingY);
            int length = (int) (lengthX * halfOfX);
            //x * cos(0)
            int x1 = (int) ((pointToRollX - vectorToMoveX) * cos);
            //y * sin(0)
            int y1 = (int) ((pointToRollY - vectorToMoveY) * sin);

            //x * cos(0)
            int x2 = (int) ((pointToRollX - vectorToMoveX) * sin);
            //y * sin(0)
            int y2 = (int) ((pointToRollY - vectorToMoveY) * cos);

            int newX2 = vectorToMoveX + x1 - y1;
            int newY2 = vectorToMoveY + y2 + x2;

            spinPoints.add(new Point((int) (newX2 + lengthX * halfOfX + paddingX), (int) (newY2 + lengthY * halfOfY + paddingY)));
        }
    }
}
