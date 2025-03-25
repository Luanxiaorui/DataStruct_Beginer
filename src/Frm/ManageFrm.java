package Frm;

import Dao.AdjacentList;

import  javax.swing.*;
import javax.swing.border.TitledBorder;
import  java.awt.*;
import java.awt.event.*;

public class ManageFrm extends  JFrame {

    JTabbedPane tabbedPane;
    AdjacentList graph=new AdjacentList();
    ManageFrm() {

        graph.create();

        tabbedPane = new JTabbedPane();

        // 实例化JPanel
        TrainManage i = new TrainManage(graph);
        PlaneManage j = new PlaneManage(graph);


        tabbedPane.addTab("火车信息管理", i);
        tabbedPane.addTab("飞机信息管理", j);

        tabbedPane.setPreferredSize(new Dimension(430, 340));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.TOP);


        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
        setBounds(100, 100, 1500, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public static void main(String[] args) {
        new ManageFrm();
    }
}
