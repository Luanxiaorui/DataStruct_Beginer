package Frm;

import Dao.AdjacentList;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class UserFrm extends JFrame implements ActionListener {

    AdjacentList graph=new AdjacentList();
    JLabel l4 = new JLabel("出发地");
    JLabel l5 = new JLabel("目的地");
    JLabel l6 = new JLabel("出发时间");
    JLabel l7 = new JLabel("出行方式");
    JLabel l8 = new JLabel("条件筛选");

    JComboBox<String> cb1 = new JComboBox<String>();
    JComboBox<String> cb2 = new JComboBox<String>();
    JComboBox<String> cb3 = new JComboBox<String>();
    JComboBox<String> cb4 = new JComboBox<String>();
    JComboBox<String> cb5 = new JComboBox<String>();

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JButton jb1 = new JButton("点击查询");


    JTable t;
    Object[][] row;
    Object[] colum = { "出发站", "到达站", "出发时间", "到站时间", "车次/航班号","花费" };

    UserFrm() {

        graph.create();

        cb1.addItem("请选择出发地");
        //这里如何处理，试试直接从文件读入

        cb2.addItem("请选择目的地");
        //这里如何处理，试试直接从文件读入

        for(int i=0;i<graph.v_num[0];i++) {
            cb1.addItem(graph.city[i]);
            cb2.addItem(graph.city[i]);
        }
        for (int i=0;i<graph.v_num[1];i++){
            cb1.addItem(graph.planeCity[i]);
            cb2.addItem(graph.planeCity[i]);
        }

        cb3.addItem("请选择出行方式");
        cb3.addItem("火车");
        cb3.addItem("飞机");

        cb4.addItem("请选择时间");
        for (int j = 1; j <= 12; j++)
            for (int i = 1; i <= 30; i++)
                cb4.addItem("2024-" + j + "-" + i);


        cb5.addItem("筛选条件");
        cb5.addItem("最快路径");
        cb5.addItem("最少中转");
        cb5.addItem("最少花费");


        p1.setBorder(new TitledBorder("出行信息"));
        p1.add(l4);
        p1.add(cb1);
        p1.add(l5);
        p1.add(cb2);
        p1.add(l6);
        p1.add(cb4);
        p1.add(l7);
        p1.add(cb3);
        p1.add(l8);
        p1.add(cb5);

        jb1.addActionListener(this);

        p2.add(jb1);

        setLayout(new BorderLayout());
        updateTable("", "", "", "", "");


        setVisible(true);
        setBounds(100, 100, 1500, 800);
        setTitle("系统登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updateTable(String start, String end, String way, String time, String condition) {
        //处理结果返回二维数组，以建立table
        if (start.isEmpty())
            row = new Object[3][6];
        else
            row=graph.getBestWay(start,end,way,condition); //调用方法，查询展示
        if(row==null)   JOptionPane.showMessageDialog(null, "不存在", "提示", JOptionPane.PLAIN_MESSAGE);
        else {
            t = new JTable(row, colum);
            t.setEnabled(false);
            add(new JScrollPane(t), BorderLayout.CENTER);
            t.setEnabled(false);
            add(p1, BorderLayout.NORTH);
            add(p2, BorderLayout.SOUTH);
            validate();
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jb1) {
            //检查是否输入完全
            if (Objects.requireNonNull(cb1.getSelectedItem()).toString().equals("请选择出发地")
                    || Objects.requireNonNull(cb2.getSelectedItem()).toString().equals("请选择目的地") || Objects.requireNonNull(cb3.getSelectedItem()).toString().equals("请选择出行方式")
                    || Objects.requireNonNull(cb4.getSelectedItem()).toString().equals("请选择时间") || Objects.requireNonNull(cb5.getSelectedItem()).toString().equals("筛选条件"))
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);

                //然后查询，得到数组，更新table或者直接text输入
            else {
                if(cb1.getSelectedItem().toString().equals(cb2.getSelectedItem().toString()))
                    JOptionPane.showMessageDialog(null, "出发地与目的地相同", "提示", JOptionPane.PLAIN_MESSAGE);
               else
                 updateTable(cb1.getSelectedItem().toString(), cb2.getSelectedItem().toString(),
                        cb3.getSelectedItem().toString(), cb4.getSelectedItem().toString(), cb5.getSelectedItem().toString());
            }
        }

    }

    public static void main(String[] args) {
        new UserFrm();
    }
}
