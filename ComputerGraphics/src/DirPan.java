
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

public class DirPan extends JFrame {

    PaintGraph pg; // класс вывода графика функции
    ButPan bp; // класс управляющих масштабом кнопок

    public DirPan() {
        super("Лабораторная 1 Выполнил Черников М.В. 213.2 График - кардиоида p=2a(1 + cosf)");
        Container globalContainer = getContentPane();
        globalContainer.setLayout(new BorderLayout()); // установка менеджера размещения
        pg = new PaintGraph(); // инициализация класса построения графика функции
        bp = new ButPan(pg); // инициализация класса кнопок масштаба

//        bp.setLayout(new BorderLayout());
        bp.setPreferredSize(new Dimension(150 /*200*/, 700));
        globalContainer.add(bp, BorderLayout.EAST);
        globalContainer.add(pg, BorderLayout.CENTER); // задание размещения
        pg.setBorder(new BasicBorders.FieldBorder(Color.darkGray, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
        setSize(1400, 700); // задание размеров
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // задание параметров
        // главного окна при закрытии
        setVisible(true);
    }

    public static void main(String[] args) {
        new DirPan();
    }
}
