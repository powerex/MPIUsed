/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;

/**Данный класс  используется для создания основного фрейма,добавления на него панелей меню,
 * обработка событий меню.
 * Методы описанные в классе:
 * конструктор  public ToolBar()
 * public void AddSubToolBar()-добавление допольнительной панели меню
 * public void AddToolBar()-добавление основной панели меню
 * public void AddMenu()-добавление выпадающего меню
 * public void actionSaveIMG_Performed()-обработка события выпадающего меню "Экспорт"
 * public void streamToString(InputStream is) -чтение данных из файла
 * public void actionLoad_Performed()-обработка события выпадающего меню "Открыть проект"
 * public void actionSave_Performed() -обработка события выпадающего меню "Сохранить проект"
 *
 * Подкласс  class CommandAction extends AbstractAction
 * отвечает за добавление кнопок с иконками на панели меню.
 *
 */
public class ToolBar extends JFrame {

    public static int DEFAULT_WIDTH = 1366;//1024;
    public static int DEFAULT_HEIGHT = 700;//740;
    public static JPanel panel;
    public static Action circleAction, ellipseAction, polygonAction, intervalAction, pointAction, lineAction;
    public static Action circleMouseAction, ellipseMouseAction, intervalMouseAction,
            polygonMouseAction, pointMouseAction, lineMouseAction, cornerMouseAction;
    public static Action MoveAction, ClearAllAction,
            ZoomAction, ReduceAction, RotateAction, SectorAction, TangentAction, SignatureAction,
            DivisionAction;
    public static JToolBar bar;
    public static boolean AddCirc, AddEll, numPolPoint, AddInter, AddPol, AddPoint, AddLine,
            CircleMouseAdd, EllipseMouseAdd, IntervalMouseAdd,
            polygonMouseAdd, pointMouseAdd, LineMouseAdd, AddCorner, CornerMouseAdd;
    public static boolean moveObject, ClearAll, Zoom, Reduce, Rotate, setName, Sector, Tangent, Signature, Division;
    public static String label, STR;
    public static JMenuBar menubar;
    public static JToolBar SubBar;
    private File file;
    public static JMenu menuFile, menuHelp;
    public static JFileChooser chooser = new JFileChooser();
    public static ArrayList<String> S;

    /**Создание основного фрейма программы,
     * добавление экземпляра класса DrawElement
     */
    public ToolBar() {
        label = "Geometry2D";
        setTitle(label);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout(2, 2));

        DrawElement component = new DrawElement();
        
        component.setEnabled(false);
        add(component);


   //     AddToolBar();
        AddMenu();
    //    AddSubToolBar();

        Zoom = false;
        moveObject = false;
        AddCirc = false;
        AddEll = false;
        numPolPoint = false;
        AddInter = false;
        AddPol = false;
        Rotate = false;
        ClearAll = false;
        Zoom = false;
        Reduce = false;
        CircleMouseAdd = false;
        EllipseMouseAdd = false;
        IntervalMouseAdd = false;
        polygonMouseAdd = false;
        pointMouseAdd = false;
        setName = false;
        AddLine = false;
        LineMouseAdd = false;
        AddCorner = false;
        CornerMouseAdd = false;
    }



        public ImageIcon getIcon(String name){
            this.getClass().getClassLoader().getResource(name);
      URL url=this.getClass().getResource(System.getProperty("java"));
            System.out.println("PATH!!!!!! = " + url);
      if (url==null){
          throw new RuntimeException("No icon^ "+name);
      }
      return new ImageIcon(url);
  }


    /**Добалвение дополнительного меню
     * создаются экземпляры класса  CommandAction,которые представляют собой кнопки на панели
     */

    public void AddSubToolBar() {
        SubBar = new JToolBar(SwingConstants.VERTICAL);
        SubBar.setFloatable(false);
        add(SubBar, BorderLayout.WEST);
        SubBar.setVisible(true);

        MoveAction = new CommandAction("Перемещение объекта / Move Object", getIcon("pic/pic7.gif"), "0.1");
        ClearAllAction = new CommandAction("Очистить все / Clear All", getIcon("pic/picClear.gif"), "0.2");
        ZoomAction = new CommandAction("Увеличить / Zoom", getIcon("pic/picZoom.gif"), "0.3");
        ReduceAction = new CommandAction("Уменьшить / Reduce", getIcon("pic/picUnZoom.gif"), "0.4");
        RotateAction = new CommandAction("Повернуть / Rotate", getIcon("pic/rotate.gif"), "0.5");
        SectorAction = new CommandAction("Сектор / Sector", getIcon("pic/sector.gif"), "0.6");
        TangentAction = new CommandAction("Касательная / Tangent", getIcon("pic/tangent.gif"), "0.7");
        DivisionAction = new CommandAction("Деление открезка в заданной пропорции / Interval division", getIcon("pic/del.gif"), "0.8");
        SignatureAction = new CommandAction("Добавление подписи / Add signature", getIcon("pic/sign.gif"), "0.9");
        SubBar.add(MoveAction);
        SubBar.add(RotateAction);
        SubBar.add(ClearAllAction);
        SubBar.add(ZoomAction);
        SubBar.add(ReduceAction);
        SubBar.addSeparator();
        SubBar.add(DivisionAction);
        SubBar.add(SectorAction);
        SubBar.add(TangentAction);
        SubBar.add(SignatureAction);
        SubBar.setBorderPainted(true);
    }

    /**Создание и добавление основого меню
     * создаются экземпляры класса  CommandAction,которые представляют собой кнопки на панели
     */
    public void AddToolBar() {
        // Image img1 = Toolkit.getDefaultToolkit().getImage("pic/pic1.gif");
        circleAction = new CommandAction("Окружность / Circle",getIcon("/pic/pic1.gif"), "1");
        circleMouseAction = new CommandAction("Окружность / Circle", getIcon("graphic2d.pic/pic8.gif"), "1.1");
        ellipseAction = new CommandAction("Эллипс / Ellipse", getIcon("pic/pic2.gif"), "2");
        ellipseMouseAction = new CommandAction("Эллипс / Ellipse", getIcon("pic/pic9.gif"), "2.1");
        polygonAction = new CommandAction("Многоугольник / Polygon", getIcon("pic/pic6.gif"), "3");
        polygonMouseAction = new CommandAction("Многоугольник / Polygon", getIcon("pic/pic10.gif"), "3.1");
        intervalAction = new CommandAction("Отрезок / Interval ", getIcon("pic/pic4.gif"), "4");
        intervalMouseAction = new CommandAction("Отрезок / Interval", getIcon("pic/pic3.gif"), "4.1");
        pointAction = new CommandAction("Точка / Point", getIcon("pic/pic11.gif"), "5");
        pointMouseAction = new CommandAction("Точка / Point", getIcon("pic/pic14.gif"), "5.1");
        lineAction = new CommandAction("Прямая / Line", getIcon("pic/pic12.gif"), "6");
        lineMouseAction = new CommandAction("Прямая / Line", getIcon("pic/pic13.gif"), "6.1");
        cornerMouseAction = new CommandAction("Угол / Corner", getIcon("pic/corner.gif"), "7");
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(pointAction);
        bar.add(circleAction);
        bar.add(ellipseAction);
        bar.add(polygonAction);
        bar.add(lineAction);
        bar.add(intervalAction);
        bar.addSeparator();
        bar.add(pointMouseAction);
        bar.add(circleMouseAction);
        bar.add(ellipseMouseAction);
        bar.add(polygonMouseAction);
        bar.add(lineMouseAction);
        bar.add(intervalMouseAction);
        bar.add(cornerMouseAction);
        add(bar, BorderLayout.NORTH);
    }

    /**Добавление выпадающего меню,обработка событий,связанных с выбором того или
     * иного пункта
     */
    public void AddMenu() {
        menuFile = new JMenu("Файл");
        menuHelp = new JMenu("Справка");
        menubar = new JMenuBar();
        menubar.add(menuFile);
        menubar.add(menuHelp);
        setJMenuBar(menubar);

        JMenuItem itemLoad = new JMenuItem("Oткрыть проект");
        menuFile.add(itemLoad);
        itemLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionLoad_Performed();
            }
        });

        JMenuItem itemSave = new JMenuItem("Сохранить проект");
        menuFile.add(itemSave);
        itemSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionSave_Performed();
            }
        });

        JMenuItem itemSaveIMG = new JMenuItem("Экспорт");
        menuFile.add(itemSaveIMG);
        itemSaveIMG.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionSaveIMG_Performed();
            }
        });

        JMenuItem itemHelp = new JMenuItem("Справка");
        menuHelp.add(itemHelp);
        itemHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionHelp_Performed();
            }
        });

    }
/** Вывод справки по пользованию приложением
 *
 */
    public void actionHelp_Performed() {
        JFrame f = new JFrame();
        f.setLocation(200, 200);
        f.setSize(900, 600);
        f.setTitle("Справка");
        f.setVisible(true);
        JTextArea text = new JTextArea(60, 80);
        JScrollPane scroll = new JScrollPane(text);

        f.add(scroll, BorderLayout.EAST);
        text.setLineWrap(true);
        String S0="Для построения элемента необходимо нажать соответсвующую кнопку основной панели,расположенной горизонтально." +"\n"+"\n"+
                " Если вы хотите построить элемент по точным данным, то выберите одну из кнопок меню,расположенных до разделителя." +"\n"+"\n"+
                "После нажатия появится диалоговое окно с текстовыми полями для ввода необходимых параметров построения" +"\n"+"\n"+
                "Если же хотите построить элемент с помощью мыши,то справа от разделителя. После этого щелчком мыши на плоскости ставятся опорные точки элемента," +
                "а движение мыши выбирается его размер или размер составной его части." +"\n"+"\n"+
                "Для построения дополнительного, зависимого от основного, элемента,  выберите соответствующую кнопку на боковой панели меню." +"\n"+"\n"+
                "После того как вы нажали кнопку,щелкните по основному элементу,к которому может относится дополнительный-появится диалоговое окно для ввода данных для построения" +"\n"+"\n"+
                "Дополнительные элементы:"+ "\n" +"\n"+
                "касательная к окружности/эллипсу" + "\n" +
                "сектор окружности/эллипса"+ "\n" +
                "точка деления отрезка в заданном соотношении" + "\n" + "\n" +
                "Для того чтобы удалить элемент необходимо нажать правой кнопкой мыши по нему или по его определенной области-появится выспылающее меню,где нужно выбрать пункт 'Удалить'" + "\n" +
                "Области захвата элемента:" + "\n" +"\n"+
                "       эллипс/окружность-небольшая область вокруг центра" + "\n" +
                "       многоугольник-вся фигура,включая вершины" + "\n" +
                "       отрезок-точка конца или начала " + "\n" +
                "       прямая-базовая точка" + "\n" +
                "       угол- любая из 3 базовых точек" + "\n" +
                "       точка- сама точка" + "\n" +
                "       касательная- точка касания" + "\n" +
                "       сектор-область ограниченная сектором" +"\n"+"\n"+
                "Для того чтобы дать точке имя нужно нажать на нее правой кнопкой мыши и выбрать пункт 'Назвать точку'" + "\n" +"\n"+
                "Для того чтобы удалить имя точки-'Удалить имя'" + "\n" +"\n"+
                "Для поворота эллипса нужно выбрать пункт дополнительного меню далее нажав левую кнопку мыши поворачивать элемент" + "\n" +"\n"+
                "Для перемещения объекта -выбрать пункт дополнительного меню и зажав левую кнопку мыши на объекте перетащить его в нужное место"+ "\n" + "\n"  ;
        String S1 = "Шаблоны записи данных элементов в файл:" + "\n" + "\n";
        String S2 = "Окружность =( центр( x , y ); радиус (r) ;[Сектор=(start, Arc)]; [Касательная=(A , L)])" + "\n" + "\n";
        String S3 = "Эллипс = центр( x , y ); полуосьХ  R1, полуосьY R2 ; угол( R );[Сектор=(start, Arc)]; [Касательная=(A , L)]" + "\n" + "\n";
        String S4 = "Угол =  значение a, точки( (x1, y1) , (x2, y2) , (x3, y3) )" + "\n" + "\n";
        String S5 = "Прямая = (k , b)  ,    где k и b коэффициенты в уравнении y=kx+b" + "\n" + "\n";
        String S6 = "Отрезок = (начало(x1 , y1), конец (x2, y2 ) ; [деление( a : b)]) " + "\n" + "\n";
        String S7 = "Точка = (x,y) " + "\n" + "\n";
        String S8 = "Многоугольник = вершины (x1,y1),(x2,y2),(x3,y3) ... " + "\n" + "\n";
        String S9 = "Обозначения =( А(x1 , y1), В(x2, y2 ), ...) " + "\n" + "\n" + "\n" + "\n" + "\n";
        String S10 = "                                                   " +
                "                                                      " +
                "                     Pазработчик: Гришина Ю.В. 43 группа. 2010г.";

        text.append(S0+S1 + S2 + S3 + S4 + S5 + S6 + S7 + S8 + S9 + S10);
        text.setEditable(false);
    }
    /**Обработка события выпадающего меню "Экспорт"
     * Генерируется изображение экрана в виде картинки,сохраняется только та часть,где
     * производится построение.
     * Изображение сохраняется в файл,имя которого задает сам пользователь через диалоговое окно
     */

    public void actionSaveIMG_Performed() {
        chooser.setCurrentDirectory(new File(".jpeg"));
        if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                Robot robot = new Robot();
                BufferedImage img = robot.createScreenCapture(getBounds());
                BufferedImage I = img.getSubimage(60, 120, getWidth() - 70, getHeight() - 130);
                ImageIO.write(I, "jpeg", new File(chooser.getSelectedFile().toString()));
            } catch (Exception a) {
            }
        }
    }

    /**Создание списка строк,считанных из файла через поток
     */
    public void streamToString(InputStream is) {
        S = new ArrayList<String>();
        try {
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(is));
            while ((line = input.readLine()) != null) {
                line = line.toUpperCase();
                S.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**Обработка события выпадающего меню "Открыть проект"
     * Через диалоговое окно выбирается текстовый файл.содержащий проект.
     * Переменная,отвечающая за очистку экрана, получает значение true.
     * Через созданный экземпляр класса DrawElement вызывается метод очистки экрана
     * Создается экземпляр класса REadFromFile для обработки данных из файла
     */

    public void actionLoad_Performed() {
        chooser.setCurrentDirectory(new File(".txt"));

        if (chooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                FileInputStream in = new FileInputStream(file);
                streamToString(in);
                in.close();
                String l = label + " - " + file.getName();
                setTitle(l);
                ToolBar.ClearAll = true;
                DrawElement d = new DrawElement();
                d.removeAllObjects();
                ReadFromFile R = new ReadFromFile();

            } catch (Exception ioe) {
            }
        }
    }

    /**Обработка события выпадающего меню "Сохранить проект"
     * через диалоговое окно пользователь создает новый или перезаписывает уже имеющийся файл
     * Создается поток,куда записываются элементы списков,содержащих текстовое представление данных элемента
     */
    public void actionSave_Performed() {
        chooser.setCurrentDirectory(new File(".txt"));
        if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new FileWriter(chooser.getSelectedFile().toString())), true);

                int Size = DrawElement.ParamPoint.size();
                for (int i = 0; i < Size; i++) {
                    out.println(DrawElement.ParamPoint.get(i));
                }

                Size = DrawElement.ParamEllipse.size();
                for (int j = 0; j < Size; j++) {
                    STR = DrawElement.ParamEllipse.get(j);
                    for (int i = 0; i < DrawElement.ParamSector.size(); i++) {
                        if (DrawElement.rArc.get(i).getX() == DrawElement.circles.get(j).getWidth() && DrawElement.rArc.get(i).getY() == DrawElement.circles.get(j).getHeight() && DrawElement.cArc.get(i).getX() == DrawElement.circles.get(j).getMinX() && DrawElement.cArc.get(i).getY() == DrawElement.circles.get(j).getMinY()) {
                            STR = STR + " " + DrawElement.ParamSector.get(i);
                        }
                    }

                    for (int i = 0; i < DrawElement.ParamTangent.size(); i++) {
                        if (DrawElement.rTan.get(i).getX() == DrawElement.circles.get(j).getWidth() * 0.5 && DrawElement.rTan.get(i).getY() == DrawElement.circles.get(j).getHeight() * 0.5 && DrawElement.cTan.get(i).getX() == DrawElement.circles.get(j).getCenterX() && DrawElement.cTan.get(i).getY() == DrawElement.circles.get(j).getCenterY()) {
                            STR = STR + " " + DrawElement.ParamTangent.get(i);
                        }
                    }
                    out.println(STR);
                }

                Size = DrawElement.ParamPoly.size();
                for (int i = 0; i < Size; i++) {
                    out.println(DrawElement.ParamPoly.get(i));
                }

                Size = DrawElement.ParamLine.size();
                for (int i = 0; i < Size; i++) {
                    out.println(DrawElement.ParamLine.get(i));
                }

                Size = DrawElement.ParamInter.size();
                for (int i = 0; i < Size; i++) {
                    STR = DrawElement.ParamInter.get(i);
                    for (int j = 0; j < DrawElement.ParamDiv.size(); j++) {
                        if (DrawElement.hashDiv.get(j) == DrawElement.intervals.get(i).hashCode()) {
                            STR = STR + DrawElement.ParamDiv.get(j);
                        }
                    }
                    out.println(STR);
                }

                Size = DrawElement.ParamCorner.size();
                for (int i = 0; i < Size; i++) {
                    out.println(DrawElement.ParamCorner.get(i));
                }

                Size = DrawElement.PointsNames.size();
                if (Size != 0) {
                    STR = "Обозначения= ";
                } else {
                    STR = "";
                }
                for (int i = 0; i < Size; i++) {
                    double x = (double) (DrawElement.nameXY.get(i).getX() - Axes.l) / Axes.measure;
                    double y = (double) (-DrawElement.nameXY.get(i).getY() + Axes.h) / Axes.measure;
                    STR = STR + DrawElement.PointsNames.get(i) + "( " + x + ", " + y + " ); ";
                }

                out.println(STR);
                out.flush();
                out.close();
                String l = label + " - " + file.getName();
                setTitle(l);
            } catch (Exception ioe) {
            }
        }
    }

    /**Класс для определения кнопок с иконками на панели меню
     */
    class CommandAction extends AbstractAction {

        public CommandAction(String name, Icon icon, String f) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, name);
            //putValue(Action.LARGE_ICON_KEY, icon);   //for jdk 1.6
            putValue(Action.DEFAULT, icon);  //for jdk 1.5
            putValue("ButtonNumber", f);
        }

        /**Обработка события,связанного с нажатием кнопки меню,при этом переменной,отвечающей за
         * построение объекта или действие присваивается значение true
         */
        public void actionPerformed(ActionEvent event) {
            String f = (String) getValue("ButtonNumber");
            AddInter = false;
            AddCirc = false;
            AddEll = false;
            numPolPoint = false;
            moveObject = false;
            AddPol = false;
            EllipseMouseAdd = false;
            polygonMouseAdd = false;
            IntervalMouseAdd = false;
            CircleMouseAdd = false;
            Rotate = false;
            Sector = false;
            Zoom = false;
            Reduce = false;
            LineMouseAdd = false;
            AddLine = false;
            Tangent = false;
            setName = false;
            CornerMouseAdd = false;
            AddCorner = false;
            Signature = false;
            Division = false;
            if (f == "1") {
                AddCirc = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "2") {
                AddEll = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "3") {
                AddPol = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "4") {
                AddInter = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "5") {
                AddPoint = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "6") {
                AddLine = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "7") {
                CornerMouseAdd = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }
            if (f == "1.1") {
                CircleMouseAdd = true;
            }
            if (f == "2.1") {
                EllipseMouseAdd = true;
            }
            if (f == "3.1") {
                polygonMouseAdd = true;
            }
            if (f == "4.1") {
                IntervalMouseAdd = true;
            }
            if (f == "5.1") {
                pointMouseAdd = true;
            }
            if (f == "6.1") {
                LineMouseAdd = true;
            }


            if (f == "0.1") {
                moveObject = true;
            }

            if (f == "0.2") {
                ClearAll = true;
                com.mathpar.Graphic2D.ParamPanel.ClickImitation();
            }
            if (f == "0.3") {
                Zoom = true;
                com.mathpar.Graphic2D.ParamPanel.ClickImitation();
            }
            if (f == "0.4") {
                Reduce = true;
                com.mathpar.Graphic2D.ParamPanel.ClickImitation();
            }
            if (f == "0.5") {
                Rotate = true;
            }
            if (f == "0.6") {
                Sector = true;
            }
            if (f == "0.7") {
                Tangent = true;
            }
            if (f == "0.8") {
                Division = true;

            }
            if (f == "0.9") {
                Signature = true;
                ParamPanel panel = new ParamPanel();
                panel.setVisible(true);
            }

        }
    }
}
