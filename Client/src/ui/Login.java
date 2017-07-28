package ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rmi.RemoteHelper;

public class Login extends Dialog {
	private static final long serialVersionUID = 1L;
	static Font font;
	JPanel panel;
	JLabel nameLabel;
	JLabel passwordLabel;
	JTextField name;
	JPasswordField password;
	JButton register;
	JButton login;
	JButton cancel;

	public Login(MainFrame frame, JMenuItem openitem, JTextArea textArea, JTextArea inputArea, JTextArea outputArea, JMenuItem versionMenu, JMenuItem newMenuItem, JMenu version){
		
		super(frame, "Login", true);
		
		font = new Font("微软雅黑", Font.PLAIN, 17);
		
		nameLabel = new JLabel("Name");
		nameLabel.setFont(font);
		name = new JTextField(16);
		name.setFont(font);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(font);
		password = new JPasswordField(16);
		password.setFont(font);
		
		register = new JButton("+New Account");
		register.setFont(font);
		register.setContentAreaFilled(false);
		register.setBorderPainted(false);
		register.setForeground(Color.BLUE);
		
		login = new JButton("Login");
		login.setFont(font);
		cancel = new JButton("Cancel");
		cancel.setFont(font);
		
		panel = new JPanel();
		panel.setLayout(null);
		
		panel.add(nameLabel);
		panel.add(name);
		panel.add(passwordLabel);
		panel.add(password);
		panel.add(register);
		panel.add(login);
		panel.add(cancel);
		
		nameLabel.setBounds(80, 35, 50, 20);
		name.setBounds(170, 32, 200, 30);
		passwordLabel.setBounds(65, 95, 90, 20);
		password.setBounds(170, 92, 200, 30);
		login.setBounds(120, 155, 90, 30);
		cancel.setBounds(290, 155, 90, 30);
		register.setBounds(315, 205, 160, 30);
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = name.getText();
				String getPassword = String.valueOf(password.getPassword());
				try {
					boolean hasLogin = RemoteHelper.getInstance().getUserService().hasLogin();
					boolean judge = RemoteHelper.getInstance().getUserService().loginJudge(username, getPassword);
					if (judge) {
						if (hasLogin) 
							JOptionPane.showMessageDialog(null, "You have logined!", "Warning", JOptionPane.INFORMATION_MESSAGE);
						else {
							JOptionPane.showMessageDialog(null, "Login successfully!", "Tip", JOptionPane.INFORMATION_MESSAGE);
							openListAdd(username, openitem, textArea, inputArea, outputArea, version);
						}
						dispose();
					} 
				} catch (RemoteException ex) {
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
		
		register.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				register.setForeground(Color.WHITE);
			}
			public void mouseExited(MouseEvent e){
				register.setForeground(Color.BLUE);
			}
			public void mouseClicked(MouseEvent e){
				dispose();
				new Register(frame);
				
			}
		});
		
		add(panel);
		int width = getToolkit().getScreenSize().width;
		int height = getToolkit().getScreenSize().height;
		
		setBounds((width - 500) / 2, (height - 300) / 2, 500, 300);
		setVisible(true);
	}
	
	public static void openListAdd(String username, JMenuItem openitem, JTextArea textArea, JTextArea inputArea, JTextArea outputArea, JMenu version) {
		ArrayList<File> filearr;
		try {
			filearr = RemoteHelper.getInstance().getIOService().readFileList(username);
			for (int a = 0; a < filearr.size(); a++) {
				String fileContent = filearr.get(a).getName();
				JMenuItem fileitem = new JMenuItem(fileContent);
				fileitem.setFont(font);
				openitem.add(fileitem);
				
				File[] versionlist = RemoteHelper.getInstance().getIOService().getVersionList(username, fileContent);
				if (versionlist.length!=0) {
					String latest = fileSort(versionlist);
					fileitem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							MainFrame.codestr.clear();
							inputArea.setText("");
							outputArea.setText("");
							MainFrame.setProject(fileContent);
							textArea.setText("");
							try {
								version.removeAll();
								versionAdd(versionlist, version, username, fileContent, textArea);
								ArrayList<String> content = RemoteHelper.getInstance().getIOService().readFile(username, fileContent, latest);
								for (int k = 0; k<content.size(); k++) {
									textArea.append(content.get(k));
									textArea.append("\n");
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
				} 
				else 
					MainFrame.setProject(fileContent);
			}
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}
	
	//版本功能
	public static void versionAdd(File[] versionList, JMenu version, String username, String project, JTextArea textArea) {
		for (int m = 0; m < versionList.length; m++) {
			String v = versionList[m].getName();
			String[] vername = v.split("\\.");
			JMenuItem file = new JMenuItem(vername[0]);
			file.setFont(font);
			version.add(file);
			
			file.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.codestr.clear();
					try {
						textArea.setText("");
						ArrayList<String> content = RemoteHelper.getInstance().getIOService().readFile(username, project, v);
						for (int k = 0; k < content.size(); k++) {
							textArea.append(content.get(k));
							textArea.append("\n");
						}
					} catch (RemoteException ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}
	
	//版本排序
	public static String fileSort(File[] fileList) {
		ArrayList<String> filename = new ArrayList<>();
		for (int i = 0; i<fileList.length; i++) {
			filename.add(fileList[i].getName());
		}
		Collections.sort(filename);
		return filename.get(filename.size() - 1);
	}
}
