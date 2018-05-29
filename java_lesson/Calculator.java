/*
 * @author
 * name:chen jinyu
 * number:15130120196
 * email:jinyucn@aliyun.com
 */
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class Calculator{
    public static void main(String[] args){
        new  Interface();
    }
}
class Interface extends JFrame {
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> lists = new ArrayList<String>();
    int cout = 0;                                     
    JPanel inputpanel;                                
    JTextField inputField;                           
    JButton button;                                 
    JButton btn1,btn2,btn3,btn4;
    JButton btn5,btn6,btn7,btn8;
    JButton btn9,btn10,btn11,btn12;
    JButton btn13,btn14,btn15,btn16;
    public Interface(){
        inputpanel = new JPanel();
        inputField = new JTextField(9);              
        button = new JButton("ÇåÁã");
        inputpanel.add(inputField);
        inputpanel.add(button);
        this.add(inputpanel);
        JPanel panel = new JPanel(new GridLayout(4,4));
        btn1 = new JButton("7");
        btn2 = new JButton("8");
        btn3 = new JButton("9");
        btn4 = new JButton("+");
        btn5 = new JButton("4");
        btn6 = new JButton("5");
        btn7 = new JButton("6");
        btn8 = new JButton("-");
        btn9 = new JButton("1");
        btn10 = new JButton("2");
        btn11 = new JButton("3");
        btn12 = new JButton("¡Á");
        btn13 = new JButton("0");
        btn14 = new JButton(".");
        btn15 = new JButton("=");
        btn16 = new JButton("¡Â");  
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        panel.add(btn5);
        panel.add(btn6);
        panel.add(btn7);
        panel.add(btn8);
        panel.add(btn9);
        panel.add(btn10);
        panel.add(btn11);
        panel.add(btn12);
        panel.add(btn13);
        panel.add(btn14);
        panel.add(btn15);
        panel.add(btn16);            
        btn1.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"7");
            }
        });
        btn2.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"8");
            }
        });
        btn3.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"9");
            }
        });
        btn5.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"4");
            }
        });
        btn6.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"5" );
            }
        });
        btn7.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"6");
            }
        });
        btn9.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"1");
            }
        });
        btn10.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"2");
            }
        });
        btn11.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"3");
            }
        });

        btn13.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+"0");
            }
        });
        btn14.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                inputField.setText(inputField.getText()+".");
            }
        });                   
        btn4.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                list.add(inputField.getText()); 
                lists.add("+");     
                cout++;            
                inputField.setText("");
            }
        });
        btn8.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                list.add(inputField.getText());
                lists.add("-");
                cout++;
                inputField.setText("");
            }
        });
        btn12.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                list.add(inputField.getText());
                lists.add("¡Á");
                cout++;
                inputField.setText("");
            }
        });
        btn16.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                list.add(inputField.getText());
                lists.add("¡Â");
                cout++;
                inputField.setText("");
            }
        });
        btn15.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                list.add(inputField.getText());           
                inputField.setText(Operator(lists.get(cout-1)));
            }
        });
        button.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){

                inputField.setText("");
                list.clear();
                lists.clear();
                cout = 0;          
            }
        });
        this.add(panel);
        this.setTitle("¼ÆËãÆ÷");
        this.setSize(300, 350);
        this.setLayout(new FlowLayout());
        this.setVisible(true);                 
    }
    public String  Operator(String str){
        Double result = 0.0 ;

        Double a = Double.parseDouble(list.get(2*cout-2));
        Double b = Double.parseDouble(list.get(2*cout-1));
        switch(str){
            case "+" : result = a + b;
                break;
            case "-" : result = a - b;
                break;
            case "¡Á" : result = a * b;
                break;
            case "¡Â" : result = a / b;
                break;
        }
        String res = String.valueOf(result);
        return res; 
    }
}
