package Frm;

import Dao.AdjacentList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class TrainManage extends JPanel implements ActionListener {
    AdjacentList graph;
    JLabel l1 = new JLabel("出发站");
    JLabel l2 = new JLabel("到达站");
    JLabel l3 = new JLabel("出发时刻");
    JLabel l4 = new JLabel("到达时刻");
    JLabel l5 = new JLabel("车次");
    JLabel l6 = new JLabel("花费");

    JTextField tf1 = new JTextField(11);
    JTextField tf2 = new JTextField(11);
    JTextField tf3 = new JTextField(11);

    JTextField tf4 = new JTextField(11);
    JTextField tf5 = new JTextField(11);
    JTextField tf6 = new JTextField(11);

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JButton jb1 = new JButton("删除");
    JButton jb2 = new JButton("清空");
    JButton jb3 = new JButton("查询");
    JButton jb4 = new JButton("更新");
    JButton jb5 = new JButton("返回");
    JButton jb6 = new JButton("添加");
    JTable t;
    Object[][] row;
    Object[] colum = { "出发站", "到达站", "出发时间", "到站时间", "车次","花费" };

   TrainManage(AdjacentList graph) {

       this.graph=graph;
       p1.setBorder(new TitledBorder("火车信息"));
       p1.add(l1);
       p1.add(tf1);
       p1.add(l2);
       p1.add(tf2);
       p1.add(l3);
       p1.add(tf3);
       p1.add(l4);
       p1.add(tf4);
       p1.add(l5);
       p1.add(tf5);
       p1.add(l6);
       p1.add(tf6);

       p2.add(jb1);
       p2.add(jb2);
       p2.add(jb3);
       p2.add(jb4);
       p2.add(jb5);
       p2.add(jb6);

       jb1.addActionListener(this);
       jb2.addActionListener(this);
       jb3.addActionListener(this);
       jb4.addActionListener(this);
       jb5.addActionListener(this);
       jb6.addActionListener(this);

       this.setLayout(new BorderLayout());
       updateTable("");

       setVisible(true);
       setBounds(300, 300, 1500, 500);

    }

    // 选中变色
    public void tableRowSelected() {

        t.setSelectionBackground(Color.BLUE);
        t.setSelectionForeground(Color.white);
        int row = t.getSelectedRow();
        int colCount = t.getColumnCount();

        String[] data = new String[colCount];
        System.out.println(row + "," + colCount);
        for (int i = 0; i < colCount; i++)
            data[i] = t.getModel().getValueAt(row, i).toString();

        tf1.setText(data[0]);
        tf2.setText(data[1]);
        tf3.setText(data[2]);
        tf4.setText(data[3]);
        tf5.setText(data[4]);
        tf6.setText(data[5]);

    }

    // 更新面板
    public void updateTable(String text) {
        if(text.isEmpty())  row= graph.getNewInformation(0);
        if(row==null)   JOptionPane.showMessageDialog(null, "不存在", "提示", JOptionPane.PLAIN_MESSAGE);
        else {
            t = new JTable(row, colum);
            this.removeAll();
            add(new JScrollPane(t), BorderLayout.CENTER);
           // t.setEnabled(false);
            t.addMouseListener(new mouse());
            delete();
            add(p1, BorderLayout.NORTH);
            add(p2, BorderLayout.SOUTH);
            validate();
        }
    }

    // 点击事件触发
    class mouse extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == t)
                tableRowSelected();
        }
    }

    // 按钮ActionEvent事件触发处理
    public void actionPerformed(ActionEvent e) {

        // 触发删除
        if (e.getSource() == jb1) {
            if(tf1.getText().isEmpty()&&tf2.getText().isEmpty()&&tf5.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
             else  if (graph.delete(tf1.getText(),tf2.getText(),tf5.getText(),0)) {
                JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.PLAIN_MESSAGE);
                updateTable("");
            } else {
                JOptionPane.showMessageDialog(null, "删除失败", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        }

        // 触发清除文本框
        else if (e.getSource() == jb2) {
            delete();
        }

        // 触发查询
        else if (e.getSource() == jb3) {
            if(tf5.getText().isEmpty()&&tf1.getText().isEmpty()&&tf2.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else {
                row = graph.getInformation(tf5.getText(),tf1.getText(),tf2.getText(),0);
                updateTable("survey");
            }
        }
        // 触发更新
        else if (e.getSource() == jb4) {

            //修改到达时间 //停留时间//花费
            if (tf1.getText().isEmpty()||tf2.getText().isEmpty()||tf3.getText().isEmpty()||tf4.getText().isEmpty()||tf5.getText().isEmpty()||tf6.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
             else {
                graph.updateInformation(tf1.getText(), tf2.getText(), tf5.getText(), tf6.getText(), tf3.getText(), tf4.getText(), 0);
                updateTable("");
            }
        }
        else if(e.getSource()==jb5){
            updateTable("");
        }
        else{
            if(tf1.getText().isEmpty()&&tf2.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
          else {
                graph.AddFrm(tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), tf5.getText(), tf6.getText(),0);
                updateTable("");
            }
        }
    }

    public void delete() {
        tf1.setText(null);
        tf2.setText(null);
        tf3.setText(null);
        tf4.setText(null);
        tf5.setText(null);
        tf6.setText(null);
    }

    //实现城市的增删、以及信息的更新、修改
    public static void main(String[] args) {

    }

}

