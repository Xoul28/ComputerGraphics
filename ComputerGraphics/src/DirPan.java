
import javax.swing.*;
import java.awt.*;

public class DirPan extends JFrame {

    PaintGraph pg; // класс вывода графика функции
    ButPan bp; // класс управляющих масштабом кнопок

    public DirPan() {
        super("Лабораторная 1 Выполнил Черников М.В. 213.2 График - кардиоида p=2a(1 + cosf)");
        Container c = getContentPane();
        c.setLayout(new BorderLayout()); // установка менеджера размещения
        pg = new PaintGraph(); // инициализация класса построения графика функции
        pg.setSize(600, 1200); // задание размеров
        c.add(pg, BorderLayout.CENTER); // задание размещения
        bp = new ButPan(pg); // инициализация класса кнопок масштаба
        c.add(bp, BorderLayout.NORTH);
        setSize(1200, 700); // задание размеров
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // задание параметров
        // главного окна при закрытии
        setVisible(true);
    }

    public static void main(String[] args) {
        new DirPan();
    }
}
