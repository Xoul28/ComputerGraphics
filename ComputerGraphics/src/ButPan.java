
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;

public class ButPan extends JPanel implements ActionListener, MouseListener {

    private JButton jbt1, jbt2, jbt7, jbt8, clearPolygonButton;
    private JCheckBox gridBox, stepsBox, coordinateSystemBox, spin, showCoords, transfer;
    PaintGraph pg;
    boolean spiner;
    java.util.Timer timer;
    TimerTask task;

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
        if (e.getSource().equals(clearPolygonButton)) {
            pg.clear();
            pg.repaint();
        }
        if (e.getSource().equals(spin)) {

            if (spin.isSelected()) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        pg.setSpin(!pg.isSpin());
                        pg.repaint();
                    }
                };
                timer.schedule(task, 1, 1);
            } else
                task.cancel();
        }

    }

    public ButPan(PaintGraph p) {
        pg = p;
        jbt1 = new JButton("Scale +");
        jbt2 = new JButton("Scale -");
        jbt7 = new JButton("hx+");
        jbt8 = new JButton("hx-");
        clearPolygonButton = new JButton("Очистить полигон");

        gridBox = new JCheckBox("Координатная сетка");
        gridBox.doClick();
        stepsBox = new JCheckBox("Единичный отрезок");
        stepsBox.doClick();
        coordinateSystemBox = new JCheckBox("Система координат");
        coordinateSystemBox.doClick();
        showCoords = new JCheckBox("Показывать координаты");
        showCoords.doClick();
        spin = new JCheckBox("ВРАЩАТЬ!");
        transfer = new JCheckBox("Переносить");


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
        showCoords.addActionListener(this);
        transfer.addActionListener(this);

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
        add(transfer);

        timer = new java.util.Timer();


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Произошёл клик по координатам мыши " + e.getXOnScreen() + "  " + e.getYOnScreen());
        Point point = new Point(e.getX(), e.getY());
        if (!transfer.isSelected()) {
            pg.addPointToList(point);
            pg.repaint();
        } else {
            pg.tryToTransfer(point);
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
}