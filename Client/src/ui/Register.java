package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;

public class Register extends JDialog{
	private static final long serialVersionUID = 1L;
	JLabel nameLabel, passwordLabel;
	JTextField name;
	JPasswordField password;
	JButton register, cancel;
	Font font;
	JPanel jp;
	
	public Register(MainFrame frame) {
		
		super(frame, "Register", true);
		
		font = new Font("微软雅黑", Font.PLAIN, 17);
		
		nameLabel = new JLabel("Name");
		nameLabel.setFont(font);
		name = new JTextField(16);
		name.setFont(font);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(font);
		password = new JPasswordField(16);
		password.setFont(font);
		
		register = new JButton("Register");
		register.setFont(font);
		cancel = new JButton("Cancel");
		cancel.setFont(font);

		jp = new JPanel();
		jp.setLayout(null);
		
		jp.add(nameLabel);
		jp.add(name);
		jp.add(passwordLabel);
		jp.add(password);
		jp.add(register);
		jp.add(cancel);
		
		//设置边界
		nameLabel.setBounds(80, 40, 50, 20);
		name.setBounds(170, 37, 200, 30);
		passwordLabel.setBounds(65, 100, 90, 20);
		password.setBounds(170, 97, 200, 30);
		register.setBounds(100, 160, 110, 30);
		cancel.setBounds(280, 160, 110, 30);
		
		//添加事件监听
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = name.getText();
				String userPassword = String.valueOf(password.getPassword());
				boolean nameEmpty = userName.equals("");
				boolean passwordEmpty = userPassword.equals("");
				if (nameEmpty || passwordEmpty) 
					JOptionPane.showMessageDialog(null, "Name or password cannot be empty!", "Error", JOptionPane.WARNING_MESSAGE);
				try {
					RemoteHelper.getInstance().getIOService().register(userName, userPassword, nameEmpty, passwordEmpty);
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		setContentPane(jp);
		int width = getToolkit().getScreenSize().width;
		int height = getToolkit().getScreenSize().height;
		setBounds((width - 500) / 2, (height - 300) / 2, 500, 300);
		setVisible(true);
	}
}