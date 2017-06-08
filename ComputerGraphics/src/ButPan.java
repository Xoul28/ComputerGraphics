
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;
import java.util.TimerTask;

public class ButPan extends JPanel implements ActionListener, MouseListener {

    private JButton jbt1, jbt2, jbt7, jbt8, clearPolygonButton, spinButton, reflectButton, scaleButton, apply;
    private JButton speedUp, speedDown;
    private JCheckBox gridBox, stepsBox, coordinateSystemBox, spin, showCoords, transfer, scaleCyc, transferCyc, reflectCyc;
    private JTextField angle, scale;
    private JTextField x11, x12, x13, x21, x22, x23, x31, x32, x33;
    private JLabel speedLabel, emptyLabel;
    PaintGraph pg;
    boolean spiner, areWeGoingToSpin, areWeGoingToReflect, scaleB, transferB, reflectB;
    java.util.Timer timer;
    TimerTask task;

    private Point lastPoint;
    private int lastAngle, speed = 5;
    private double lastScale = 1;

    private double[][] matrix;

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbt1)) {
            jbt2.setEnabled(true);
            pg.setScale(pg.getScale() + 10);
            pg.repaint();
            if (pg.getScale() == 120) {
                jbt1.setEnabled(false);
            }
        }
        if (e.getSource().equals(jbt2)) {
            jbt1.setEnabled(true);
            pg.setScale(pg.getScale() - 10);
            pg.repaint();
            if (pg.getScale() == 10) {
                jbt2.setEnabled(false);
            }
        }
        if (e.getSource().equals(jbt7)) {
            if (pg.getStepX() >= 0.01) {
                jbt8.setEnabled(true);
            }
            pg.setStepX(pg.getStepX() + (float) 0.3);
            pg.repaint();
            if (pg.getStepX() >= 20) {
                jbt7.setEnabled(false);
            }
        }
        if (e.getSource().equals(jbt8)) {
            if (pg.getStepX() <= 1) {
                jbt7.setEnabled(true);
            }
            pg.setStepX(pg.getStepX() - (float) 0.01);
            pg.repaint();
            if (pg.getStepX() <= 0.01) {
                jbt8.setEnabled(false);
            }
        }
        if (e.getSource().equals(gridBox)) {
            pg.setDrawGrid(!pg.isDrawGrid());
            pg.repaint();
        }
        if (e.getSource().equals(showCoords)) {
            pg.setDrawCoords(!pg.isDrawCoords());
            pg.repaint();
        }
        if (e.getSource().equals(stepsBox)) {
            pg.setDrawSteps(!pg.isDrawSteps());
            pg.repaint();
        }
        if (e.getSource().equals(coordinateSystemBox)) {
            pg.setDrawSystem(!pg.isDrawSystem());
            pg.repaint();
        }
        if (e.getSource().equals(scaleButton)) {
            double sc;
            try {
                sc = Double.parseDouble(scale.getText());
            } catch (Exception ex) {
                sc = 1;
            }
            pg.scale(sc);
            pg.setScaling(true);
            pg.repaint();
        }

        if (e.getSource().equals(transferCyc)) {
            if (transferCyc.isSelected()) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        pg.tryToTransfer(lastPoint);
                        pg.repaint();
                    }
                };
                timer.schedule(task, 1, 2000);
            } else {
                task.cancel();
                pg.clearBools();
            }
        }

        if (e.getSource().equals(spin)) {
            if (spin.isSelected()) {
                task = new TimerTask() {
                    @Override
                    public void run() {
//                        pg.setSpin(!pg.isSpin());
                        try {
                            lastAngle += Integer.parseInt(angle.getText());
                        } catch (Exception e) {
                            lastAngle = 1;
                        }
                        pg.spin(lastPoint, lastAngle);
                        pg.repaint();
                    }
                };
                timer.schedule(task, 1, speed);
            } else {
                task.cancel();
                pg.clearBools();
            }
        }
        if (e.getSource().equals(spinButton)) {
            areWeGoingToSpin = true;
        }
        if (e.getSource().equals(reflectButton)) {
            areWeGoingToReflect = true;
        }
        if (e.getSource().equals(clearPolygonButton)) {
            pg.clear();
            pg.repaint();
        }

        if (e.getSource().equals(scaleCyc)) {
            if (scaleCyc.isSelected()) {

                task = new TimerTask() {
                    @Override
                    public void run() {
                        pg.setScaling(true);
                        try {
                            lastScale = Double.parseDouble(scale.getText());
                        } catch (Exception e) {
                            lastScale = 1;
                        }
                        pg.scaleTimer(lastScale);
                        pg.repaint();
                    }
                };
                timer.schedule(task, 100, 100);
            } else {
                task.cancel();

                pg.clearBools();
            }
//            pg.setScaling(false);
        }

        if (e.getSource().equals(reflectCyc)) {
            if (reflectCyc.isSelected()) {

                task = new TimerTask() {
                    @Override
                    public void run() {
                        pg.setSpin(true);
                        pg.reflectSpin(lastPoint);
                        pg.setReflecting(true);
                        pg.repaint();
                    }
                };
                timer.schedule(task, 100, 1000);
            } else {
                task.cancel();
                pg.setReflecting(false);
                pg.clearBools();
            }
//            pg.setScaling(false);
        }

        if (e.getSource().equals(apply)) {
            readMatrix();
            pg.multiplyMatrix(matrix);
            pg.repaint();
        }

    }

    private void readMatrix() {
        try {
            matrix[0][0] = Double.parseDouble(x11.getText());
            matrix[0][1] = Double.parseDouble(x12.getText());
            matrix[0][2] = Double.parseDouble(x13.getText());
            matrix[1][0] = Double.parseDouble(x21.getText());
            matrix[1][1] = Double.parseDouble(x22.getText());
            matrix[1][2] = Double.parseDouble(x23.getText());
            matrix[2][0] = Double.parseDouble(x31.getText());
            matrix[2][1] = Double.parseDouble(x32.getText());
            matrix[2][2] = Double.parseDouble(x33.getText());
        } catch (Exception e) {
            matrix[0][0] = 1;
            matrix[0][1] = 0;
            matrix[0][2] = 0;
            matrix[1][0] = 0;
            matrix[1][1] = 1;
            matrix[1][2] = 0;
            matrix[2][0] = 0;
            matrix[2][1] = 0;
            matrix[2][2] = 1;
        }
    }

    public void setMatrix(double matrix[][]) {
        x11.setText(String.format(Locale.ROOT, "% .2f", matrix[0][0]));
        x12.setText(String.format(Locale.ROOT, "% .2f", matrix[0][1]));
        x13.setText(String.format(Locale.ROOT, "% .2f", matrix[0][2]));
        x21.setText(String.format(Locale.ROOT, "% .2f", matrix[1][0]));
        x22.setText(String.format(Locale.ROOT, "% .2f", matrix[1][1]));
        x23.setText(String.format(Locale.ROOT, "% .2f", matrix[1][2]));
        x31.setText(String.format(Locale.ROOT, "% .2f", matrix[2][0]));
        x32.setText(String.format(Locale.ROOT, "% .2f", matrix[2][1]));
        x33.setText(String.format(Locale.ROOT, "% .2f", matrix[2][2]));
    }

    public ButPan(PaintGraph p) {
        pg = p;
        pg.setBp(this);
        jbt1 = new JButton("Scale +");
        jbt2 = new JButton("Scale -");
        jbt7 = new JButton("hx+");
        jbt8 = new JButton("hx-");
        clearPolygonButton = new JButton("Очистить полигон");
        spinButton = new JButton("Вращать");
        reflectButton = new JButton("Отражать");
        scaleButton = new JButton("  Scaling  ");
        apply = new JButton("  apply  ");
        matrix = new double[3][3];
        gridBox = new JCheckBox("Координатная сетка");
        gridBox.doClick();
        stepsBox = new JCheckBox("Единичный отрезок");
        stepsBox.doClick();
        coordinateSystemBox = new JCheckBox("Система координат");
        coordinateSystemBox.doClick();
        showCoords = new JCheckBox("Показывать координаты");
        showCoords.doClick();
        spin = new JCheckBox("ВРАЩАТЬ!");
        reflectCyc = new JCheckBox("ОТРАЖАТЬ!");
        transferCyc = new JCheckBox("ПЕРЕНОСИТЬ!");
        scaleCyc = new JCheckBox("СКЕЙЛИТЬ!");
        transfer = new JCheckBox("Переносить");
        angle = new JTextField("");
        angle.setColumns(3);
        scale = new JTextField("");
        scale.setColumns(4);

        x11 = new JTextField("");
        x11.setColumns(3);
//        x11.setBackground(Color.GRAY);
        x11.setHorizontalAlignment(JTextField.CENTER);

        x12 = new JTextField("");
        x12.setColumns(3);
//        x12.setBackground(Color.GRAY);
        x12.setHorizontalAlignment(JTextField.CENTER);

        x13 = new JTextField("");
        x13.setColumns(3);
//        x13.setBackground(Color.GRAY);
        x13.setHorizontalAlignment(JTextField.CENTER);

        x21 = new JTextField("");
        x21.setColumns(3);
//        x21.setBackground(Color.GRAY);
        x21.setHorizontalAlignment(JTextField.CENTER);

        x22 = new JTextField("");
        x22.setColumns(3);
//        x22.setBackground(Color.GRAY);
        x22.setHorizontalAlignment(JTextField.CENTER);

        x23 = new JTextField("");
        x23.setColumns(3);
//        x23.setBackground(Color.GRAY);
        x23.setHorizontalAlignment(JTextField.CENTER);

        x31 = new JTextField("");
        x31.setColumns(3);
//        x31.setBackground(Color.GRAY);
        x31.setHorizontalAlignment(JTextField.CENTER);

        x32 = new JTextField("");
        x32.setColumns(3);
//        x32.setBackground(Color.GRAY);
        x32.setHorizontalAlignment(JTextField.CENTER);

        x33 = new JTextField("");
        x33.setColumns(3);
//        x33.setBackground(Color.GRAY);
        x33.setHorizontalAlignment(JTextField.CENTER);

        speedLabel = new JLabel();
        speedLabel.setText(speed + "");

        emptyLabel = new JLabel();
        emptyLabel.setText("    ");

        jbt1.addActionListener(this);
        jbt2.addActionListener(this);
        jbt7.addActionListener(this);
        jbt8.addActionListener(this);
        gridBox.addActionListener(this);
        stepsBox.addActionListener(this);
        coordinateSystemBox.addActionListener(this);
        clearPolygonButton.addActionListener(this);
        pg.addMouseListener(this);
        spin.addActionListener(this);
        transferCyc.addActionListener(this);
        reflectCyc.addActionListener(this);
        scaleCyc.addActionListener(this);
        showCoords.addActionListener(this);
        transfer.addActionListener(this);
        spinButton.addActionListener(this);
        reflectButton.addActionListener(this);
        scaleButton.addActionListener(this);
        apply.addActionListener(this);

        add(jbt1);
        add(jbt2);
        add(jbt7);
        add(jbt8);
        add(gridBox);
        add(showCoords);
        add(stepsBox);
        add(coordinateSystemBox);
        add(clearPolygonButton);
        add(spin);
        add(scaleCyc);
        add(reflectCyc);
        add(transferCyc);
        add(transfer);
        add(spinButton);
        add(angle);
        add(reflectButton);
        add(emptyLabel);
        add(scaleButton);
        add(scale);

        add(x11);
        add(x12);
        add(x13);
        add(x21);
        add(x22);
        add(x23);
        add(x31);
        add(x32);
        add(x33);
        add(apply);

        timer = new java.util.Timer();

        lastPoint = new Point(600, 600);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Произошёл клик по координатам мыши " + e.getXOnScreen() + "  " + e.getYOnScreen());
        Point point = new Point(e.getX(), e.getY());
        if (transfer.isSelected()) {
            pg.tryToTransfer(point);
            lastPoint = point;
            pg.repaint();
        } else if (areWeGoingToSpin) {
            pg.spin(new Point(e.getX(), e.getY()), Integer.parseInt(angle.getText()));
            lastPoint = new Point(e.getX(), e.getY());
            areWeGoingToSpin = false;
            pg.repaint();
        } else if (areWeGoingToReflect) {
            lastPoint = new Point(e.getX(), e.getY());
            pg.reflect(new Point(e.getX(), e.getY()));
            areWeGoingToReflect = false;
            pg.repaint();
        } else {
            pg.addPointToList(point);
            pg.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void uncheck() {
        spin.setSelected(false);
        transferCyc.setSelected(false);
        scaleCyc.setSelected(false);
        reflectCyc.setSelected(false);
    }
}