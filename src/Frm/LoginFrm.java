package Frm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import Toolkit.BeautyUI;

public class LoginFrm extends JFrame implements ActionListener {

    JLabel l1 = new JLabel("全国交通调度系统");
    JLabel l2 = new JLabel("");
    JLabel l3 = new JLabel("请选择身份");
    JLabel l4 = new JLabel("账号");
    JLabel l5 = new JLabel("密码");


    JComboBox<String> cb = new JComboBox<>();

    JTextField tf1 = new JTextField();
    JPasswordField tf2 = new JPasswordField();

    JButton jb1 = new JButton("马上登录");

    LoginFrm() {


        cb.addItem("用户");
        cb.addItem("管理员");

        this.setLayout(null);

        add(l1);
        l1.setBounds(100, 50, 400, 50);
        l1.setFont(new Font("仿宋", Font.BOLD, 36));
        add(l2);
        l2.setBounds(150, 125, 300, 50);
        l2.setFont(new Font("宋体", Font.PLAIN, 28));
        add(l3);
        l3.setBounds(100, 200, 100, 25);
        add(cb);
        cb.setBounds(100, 225, 300, 50);
        add(l4);
        l4.setBounds(100, 300, 100, 25);
        add(tf1);
        tf1.setBounds(100, 325, 300, 50);
        tf1.setFont(new Font("宋体", Font.BOLD, 18));
        add(l5);
        l5.setBounds(100, 400, 50, 25);
        add(tf2);
        tf2.setBounds(100, 425, 300, 50);
        tf2.setFont(new Font("宋体", Font.BOLD, 15));
        add(jb1);
        jb1.setBounds(80, 525, 340, 60);

        jb1.addActionListener(this);
        tf2.addActionListener(this);

        setVisible(true);
        setBounds(100, 100, 500, 680);
        this.setResizable(false);
        setTitle("系统登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb1 || e.getSource() == tf2) {
            if (tf1.getText().isEmpty() || new String(tf2.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "信息不完整", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                if (Objects.requireNonNull(cb.getSelectedItem()).toString().equals("管理员")) {
                    if (tf1.getText().equals("2022001") &&  new String(tf2.getPassword()).equals("123456")) {
                        //new HMenuFrm(tf1.getText());
                        new ManageFrm();
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误", "提示", JOptionPane.PLAIN_MESSAGE);
                        tf1.setText(null);
                        tf2.setText(null);
                    }
                } else if (cb.getSelectedItem().toString().equals("用户")) {
                    if (tf1.getText().equals("20221536") && new String(tf2.getPassword()).equals("123456")) {
                        new UserFrm();
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误", "提示", JOptionPane.PLAIN_MESSAGE);
                        tf1.setText(null);
                        tf2.setText(null);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        BeautyUI.beautyUI();
        new LoginFrm();
    }
}
