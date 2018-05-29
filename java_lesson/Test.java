/*
 * @author
 * name:chen jinyu
 * number:15130120196
 * email:jinyucn@aliyun.com
 */
import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;  
import java.awt.GridLayout;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;  
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.JButton;  
import javax.swing.JComboBox;  
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;  
import java.sql.*;
  
public class Test extends JFrame implements ActionListener {  
      
    //年份  
    private JLabel YearLabel = new JLabel("年份:");  
    private JComboBox YearBox = new JComboBox();  
      
    //月份  
    private JLabel MonthLabel = new JLabel("月份:");  
    private JComboBox MonthBox = new JComboBox();  
    //创建
    private JLabel creLabel = new JLabel("Create:"); 
    String[] cre={"Todo","Note","Appointment","Contact"};
    private JComboBox creBox = new JComboBox(cre);  
    private JButton crebut=new JButton("创建");
    //删除
    private JLabel delLabel = new JLabel("Delete:"); 
    private JComboBox delBox = new JComboBox(cre);  
    private JButton delbut=new JButton("删除");
    //修改
    private JLabel upLabel = new JLabel("Update:"); 
    private JComboBox upBox = new JComboBox(cre);  
    private JButton upbut=new JButton("修改");
    //查看  
    JButton button_ok = new JButton("查看"); 
    JButton button_note = new JButton("See_Note");    
    JButton button_cont = new JButton("See_Contact"); 
    JTable Tnote=null,Tcontact=null;
    //今天  
    JButton button_today = new JButton("今天");  
     
    //今天的日期,年份,月份  
    private Date now_date = new Date();  
    private int now_year = now_date.getYear() + 1900;  
    private int now_month = now_date.getMonth();  
    private boolean todayFlag = false;   //是否显示今天的日期  
      
    //用一行来显示日期  
    private JTextArea[] button_day = new JTextArea[42]; //TextField 
    private final String[] week = {"日", "一", "二", "三", "四", "五", "六"};  
    private JButton[] button_week = new JButton[7];  
      
    //用户选择年份和月份  
    private String year_int = null;  
    private int month_int; 
       
    public Test(){  
        super();  
        setTitle("日历");  
        init();  
        setLocation(500, 300);  
        setResizable(false);  
        pack();  
    }  
      
    //初始化日历  
    private void init() {  
        Font font = new Font("Dialog", Font.BOLD, 14);  
        YearLabel.setFont(font);  
        MonthLabel.setFont(font);  
        button_ok.setFont(font);  
        button_today.setFont(font);  
          
        //当前年份前10年和未来20年时间区间  
        for(int i = now_year - 10; i <= now_year + 20; i++){  
            YearBox.addItem(i + "");  
        }  
        YearBox.setSelectedIndex(10);  
          
        //12个月的月份区间  
        for(int i = 1; i < 13; i++){  
            MonthBox.addItem(i + "");  
        }  
        MonthBox.setSelectedIndex(now_month);  
          
        //年份,月份,查看,今天  
        JPanel panel_ym = new JPanel();  
        panel_ym.add(YearLabel);  
        panel_ym.add(YearBox);  
        panel_ym.add(MonthLabel);  
        panel_ym.add(MonthBox);  
        panel_ym.add(button_ok);  
        panel_ym.add(button_today);
        panel_ym.add(button_note);
        panel_ym.add(button_cont);
        panel_ym.add(creLabel);
        panel_ym.add(creBox);
        panel_ym.add(crebut);
        panel_ym.add(delLabel);
        panel_ym.add(delBox);
        panel_ym.add(delbut);
        panel_ym.add(upLabel);
        panel_ym.add(upBox);
        panel_ym.add(upbut);
        button_ok.addActionListener(this);  
        button_today.addActionListener(this);  
        button_note.addActionListener(this);
        button_cont.addActionListener(this);
        crebut.addActionListener(this);
        delbut.addActionListener(this);
        upbut.addActionListener(this);
        //星期 "日", "一", "二", "三", "四", "五", "六"  
        JPanel panel_day = new JPanel();  
        panel_day.setLayout(new GridLayout(7, 7, 3, 3));  
        for(int i = 0; i < 7; i++){  
            button_week[i] = new JButton(week[i]);  
            button_week[i].setForeground(Color.black);  
            panel_day.add(button_week[i]);  
        }  
        button_week[0].setForeground(Color.red);  
        button_week[6].setForeground(Color.red);  
          
        //1,2,3..30等日  
        for(int i = 0; i < 42; i++){  
            button_day[i] = new JTextArea(" ");
            panel_day.add(button_day[i]);  
        }  
        printDay();  
        //  
        JPanel panel_main = new JPanel();  
        panel_main.setLayout(new BorderLayout());  
        panel_main.add(panel_ym, BorderLayout.NORTH);  
        panel_main.add(panel_day, BorderLayout.SOUTH);  
        getContentPane().add(panel_main);  
    }  
  
    //显示当前年月的日期  
    private void printDay(){  
        clearBtns();  
          
        if(todayFlag){  
            year_int = now_year + "";  
            month_int = now_month;  
        }else{  
            year_int = YearBox.getSelectedItem().toString();  
            month_int = MonthBox.getSelectedIndex();  
        }  
          
        int year_sel = Integer.parseInt(year_int) - 1900;  
        Date firstDay = new Date(year_sel, month_int, 1);  
        GregorianCalendar cal = new GregorianCalendar();  
        cal.setTime(firstDay);  
          
        //  
        int days = 0; //本月有多少天  
        int day_week = firstDay.getDay();  //星期几  
        if(month_int == 0 || month_int == 2 || month_int == 4 || month_int == 6 || month_int == 7 || month_int == 9 || month_int == 11 ){  
            days = 31;  
        }else if(month_int == 3 || month_int == 5|| month_int == 8 || month_int == 10 ){  
            days = 30;  
        }else{  
            if(cal.isLeapYear(year_sel)){  
                days = 29;  
            }else{  
                days = 28;  
            }  
        }  
          
        //根据选定月份第一天是星期几来确定按钮的绘制位置 day_week为绘制的起始位置。  
        int count = 1;  
          
        for(int i = day_week; i < day_week + days; count++, i++){  
        	String s = Integer.toString(count);
        	String para=year_int+"/"+Integer.toString(month_int+1)+"/"+Integer.toString(count);
            Collection<String> x=getTodos(para);
            if(i % 7 == 0 || i == 6 || i == 13 || i == 20 || i == 27 || i == 34 || i == 41){  //6+31 = 37  
                if(i == day_week + now_date.getDate() - 1){  
                    button_day[i].setForeground(Color.black); 
                    for(String s1:x)
                    	if(s1!=null)
                    	s=s+"\n"+s1;
                    button_day[i].setText(s+ ""); 
                    button_day[i].setFont(new Font("宋体", Font.PLAIN, 15));
                    button_day[i].setPreferredSize(new Dimension(200, 90));
                }else{  
                    button_day[i].setForeground(Color.black); 
                    for(String s1:x)
                    	s=s+"\n"+s1;
                    button_day[i].setText(s+ "");  
                    button_day[i].setFont(new Font("宋体", Font.PLAIN, 15));
                    button_day[i].setPreferredSize(new Dimension(200, 90));
                }  
            }else{  
                if(i == day_week + now_date.getDate() - 1){  
                    button_day[i].setForeground(Color.blue);  
                    for(String s1:x)
                    	s=s+"\n"+s1;
                    button_day[i].setText(s+ "");  
                    button_day[i].setFont(new Font("宋体", Font.PLAIN, 15));
                    button_day[i].setPreferredSize(new Dimension(200, 90));
                }else{  
                    button_day[i].setForeground(Color.black);  
                    for(String s1:x)
                    	s=s+"\n"+s1;
                    button_day[i].setText(s+ "");  
                    button_day[i].setFont(new Font("宋体", Font.PLAIN, 15));
                    button_day[i].setPreferredSize(new Dimension(200, 90));
                }  
            }  
        }  
          
        //  
        if(day_week == 0){  
            for(int i = days; i < 42; i++){  
                button_day[i].setText("");  
            }  
        }else{  
            //第一天不是周日,则将第一天前面的按钮置空  
            for(int i = 0; i < day_week; i++){  
                button_day[i].setText("");  
            }  
            //最后一天后面的按钮置空  
            for(int i = day_week + days; i < 42; i++){  
                button_day[i].setText("");  
            }  
        }  
    }  
  
    private void clearBtns(){  
        for(int i = 0; i < 42; i++){  
            button_day[i].setText("");  
        }  
    }  
      
    //----->按钮监听函数  
    public void actionPerformed(ActionEvent e) {  
        if(e.getSource() == button_ok){  
            todayFlag = false;  
            printDay();  
        }else if(e.getSource() == button_today){  
            todayFlag = true;  
            YearBox.setSelectedIndex(10);  
            MonthBox.setSelectedIndex(now_month);  
            printDay();  
        }else if(e.getSource() ==button_note){
        	String[] ColumnName={"Text","Priority"};
        	String[][] rowdata=getnote("pimnote");
        	Tnote=new JTable(rowdata,ColumnName);
        	Tnote.setRowHeight(40);
        	Tnote.setFont(new Font("Menu.font", Font.PLAIN, 20));
        	Tnote.getTableHeader().setFont(new Font("Dialog", 0, 20));
        	JFrame jf=new JFrame("Note");
        	jf.setSize(800, 600);
        	jf.setLayout(new BorderLayout());
        	JScrollPane scroll = new JScrollPane(Tnote);
        	scroll.setSize(500, 300);
        	jf.add(scroll,BorderLayout.NORTH);
        	jf.setVisible(true);
        }else if(e.getSource() ==button_cont){
        	String[] ColumnName={"First_name","Last_name","Email_address","Priority"};
        	String[][] rowdata=getcontact("pimcontact");
        	Tcontact=new JTable(rowdata,ColumnName);
        	Tcontact.setRowHeight(40);
        	Tcontact.setFont(new Font("Menu.font", Font.PLAIN, 20));
        	Tcontact.getTableHeader().setFont(new Font("Dialog", 0, 20));
        	JFrame jf=new JFrame("Contact");
        	jf.setSize(800, 600);
        	jf.setLayout(new BorderLayout());
        	JScrollPane scroll = new JScrollPane(Tcontact);
        	scroll.setSize(500, 300);
        	jf.add(scroll,BorderLayout.NORTH);
        	jf.setVisible(true);
        }else if(e.getSource()==crebut){
        	Create_Object();
        }else if(e.getSource()==delbut){
        	Delete_Object();
        }else if(e.getSource()==upbut){
        	Update_Object();
        }
    }  
    public static Collection<String> getTodos(String date2){
    	Collection<String> x = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery("select * from "+"pimtodo"+" where date1='"+date2+"'");
			ResultSetMetaData metadata=resultset.getMetaData();
			int column=metadata.getColumnCount();
			x=new ArrayList<String>();
			while(resultset.next()){
				String res = "Todo:";
				for(int i=2;i<=column;i++)
					res=res+resultset.getString(i).trim()+",";
				x.add(res);
					
			}
			resultset = statement.executeQuery("select * from "+"pimappointment"+" where date1='"+date2+"'");
			metadata=resultset.getMetaData();
			column=metadata.getColumnCount();
			while(resultset.next()){
				String res = "App:";
				for(int i=2;i<=column;i++){
					res=res+resultset.getString(i).trim()+",";
				}
				x.add(res);
			}
			statement.close();
			resultset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
	}
    public static String[][] getnote(String table){
    	String[][] x =new String[10][2];
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery("select * from "+table);
			ResultSetMetaData metadata=resultset.getMetaData();
			int column=metadata.getColumnCount();
			int sum=0;
			while(resultset.next()){
				for(int i=1;i<=column;i++){
					String res=resultset.getString(i);
					x[sum][i-1]=res;
				}	
				sum++;
			}
			statement.close();
			resultset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
	}
    public static String[][] getcontact(String table){
    	String[][] x =new String[10][4];
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery("select * from "+table);
			ResultSetMetaData metadata=resultset.getMetaData();
			int column=metadata.getColumnCount();
			int sum=0;
			while(resultset.next()){
				for(int i=1;i<=column;i++){
					String res=resultset.getString(i);
					x[sum][i-1]=res;
				}	
				sum++;
			}
			statement.close();
			resultset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
	}
    void Create_Object(){
    	String table=(String) creBox.getSelectedItem();
    	switch(table){
    	case "Todo":String date1=JOptionPane.showInputDialog("Enter the date");
    	String text=JOptionPane.showInputDialog("Enter the text");
    	String priority=JOptionPane.showInputDialog("Enter the priority");
    	String stl="insert into pimtodo values (?,?,?)";
		PreparedStatement psrm=null;
		try {
			psrm=connection.prepareStatement(stl);
			psrm.setString(1,date1);
			psrm.setString(2, text);
			psrm.setString(3, priority);
			psrm.executeQuery();
			psrm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}break;
    	case "Note":
    	text=JOptionPane.showInputDialog("Enter the text");
    	priority=JOptionPane.showInputDialog("Enter the priority");
    	stl="insert into pimnote values (?,?)";
		psrm=null;
		try {
			psrm=connection.prepareStatement(stl);
			psrm.setString(1, text);
			psrm.setString(2, priority);
			psrm.executeQuery();
			psrm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}break;
    	case "Appointment":
    		date1=JOptionPane.showInputDialog("Enter the date");
        	text=JOptionPane.showInputDialog("Enter the description");
        	priority=JOptionPane.showInputDialog("Enter the priority");
        	stl="insert into pimappointment values (?,?,?)";
    		psrm=null;
    		try {
    			psrm=connection.prepareStatement(stl);
    			psrm.setString(1, date1);
    			psrm.setString(2, text);
    			psrm.setString(3, priority);
    			psrm.executeQuery();
    			psrm.close();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}break;
    	case "Contact":
    		String first_name=JOptionPane.showInputDialog("Enter the first_name");
    		String last_name=JOptionPane.showInputDialog("Enter the last_name");
        	String email_address=JOptionPane.showInputDialog("Enter the email_address");
        	priority=JOptionPane.showInputDialog("Enter the priority");
        	stl="insert into pimcontact values (?,?,?,?)";
    		psrm=null;
    		try {
    			psrm=connection.prepareStatement(stl);
    			psrm.setString(1, first_name);
    			psrm.setString(2, last_name);
    			psrm.setString(3, email_address);
    			psrm.setString(4, priority);
    			psrm.executeQuery();
    			psrm.close();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}break;
    	}
    }
    void Delete_Object(){
    	String item=(String) delBox.getSelectedItem();
    	String table="pim"+item;
    	String yindao = new String();
    	switch(item){
    	case "Todo":yindao="Enter the attribute,including date,text or priority";break;
    	case "Note":yindao="Enter the attribute,including text or priority";break;
    	case "Appointment":yindao="Enter the attribute,including date,description or priority";break;
    	case "Contact":yindao="Enter the attribute,including first_name,last_name,email_address or priority";break;
    	}
    	String key1=JOptionPane.showInputDialog(yindao);
    	if(key1.equals("date"))
    		key1+="1";
    	String value1=JOptionPane.showInputDialog("Enter the deleted value");
		String stl="delete from "+table+" where "+key1+"="+"'"+value1+"'";
		PreparedStatement psrm=null;
		try {
			psrm=connection.prepareStatement(stl);
			psrm.executeQuery();
			psrm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    void Update_Object(){
    	String item=(String) upBox.getSelectedItem();
    	String table="pim"+item;
    	String yindao = new String();
    	switch(item){
    	case "Todo":yindao="Enter the attribute,including date,text or priority";break;
    	case "Note":yindao="Enter the attribute,including text or priority";break;
    	case "Appointment":yindao="Enter the attribute,including date,description or priority";break;
    	case "Contact":yindao="Enter the attribute,including first_name,last_name,email_address or priority";break;
    	}
    	String key1=JOptionPane.showInputDialog(yindao);
    	if(key1.equals("date"))
    		key1+="1";
    	String value1=JOptionPane.showInputDialog("Enter the old value");
    	String key2=JOptionPane.showInputDialog(yindao);
    	if(key2.equals("date"))
    		key2+="1";
    	String value2=JOptionPane.showInputDialog("Enter the new value");
    	String stl="update "+table+" set "+key2+"="+"'"+value2+"' where "+key1+"='"+value1+"'";
		PreparedStatement psrm=null;
		try {
			psrm=connection.prepareStatement(stl);
			psrm.executeQuery();
			psrm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static final String Driver="oracle.jdbc.driver.OracleDriver";
	static final String DATABASE_URL="jdbc:oracle:thin:@localhost";
	static Connection connection=null;
    //----->主函数  
    public static void main(String[] args) throws SQLException{  
    	try {
			Class.forName(Driver);//driver
			connection=DriverManager.getConnection(DATABASE_URL,"15130120196","123456");	
			 Test ct = new Test();  
		        ct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		        ct.setVisible(true);  
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//连接
    }     
} 
