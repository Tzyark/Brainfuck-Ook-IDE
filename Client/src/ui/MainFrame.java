package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


import rmi.RemoteHelper;

public class MainFrame extends JFrame {
	private static Font font;
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private JPanel jp1;
	private JPanel jp2;
	private JTextArea inputArea;
	private JTextArea outputArea;
	private JScrollPane scoll;
	public static String project = "";
	public static ArrayList<String> codestr = new ArrayList<>();
	private int curr = 0;
	private int last = 0;

	public MainFrame() {
		font = new Font("微软雅黑", Font.PLAIN, 17);
		
		jp1 = new JPanel();
		jp1.setLayout(new GridLayout());
		jp2 = new JPanel();
		jp2.setLayout(new GridLayout());
		
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints constraint = new GridBagConstraints();
		
		// 创建窗体
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(gridBag);

		//菜单栏
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.LIGHT_GRAY);
		
		//文件-新建、打开、保存
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(font);
		menuBar.add(fileMenu);
			JMenuItem newMenuItem = new JMenuItem("New");
			newMenuItem.setFont(font);
			fileMenu.add(newMenuItem);
			
			JMenu openMenuItem = new JMenu("Open");
			openMenuItem.setFont(font);
			fileMenu.add(openMenuItem);
			
			JMenuItem saveMenuItem = new JMenuItem("Save");
			saveMenuItem.setFont(font);
			fileMenu.add(saveMenuItem);
			frame.setJMenuBar(menuBar);
		
		//工具-撤销、重做
		JMenu editMenu = new JMenu("Edit");
		editMenu.setFont(font);
		menuBar.add(editMenu);
			JMenuItem undoMenuItem = new JMenuItem("Undo");
			undoMenuItem.setFont(font);
			editMenu.add(undoMenuItem);
			undoMenuItem.addActionListener(new UndoListener());
			
			JMenuItem redoMenuItem = new JMenuItem("Redo");
			redoMenuItem.setFont(font);
			editMenu.add(redoMenuItem);
			redoMenuItem.addActionListener(new RedoListener());
		
		//运行
		JMenu runMenu = new JMenu("Run");
		runMenu.setFont(font);
		menuBar.add(runMenu);
			JMenuItem excuteMenuItem = new JMenuItem("Execute");
			excuteMenuItem.setFont(font);
			runMenu.add(excuteMenuItem);
		
		//版本
		JMenu versionMenu = new JMenu("Version");
		versionMenu.setFont(font);
		menuBar.add(versionMenu);
		
		//用户-登入、登出
		JMenu accurrMenu = new JMenu();
		ImageIcon image1 = new ImageIcon("picture/account1.png");
		accurrMenu.setIcon(image1);
			JMenuItem loginMenuItem = new JMenuItem("Login");
			loginMenuItem.setFont(font);
			accurrMenu.add(loginMenuItem);
		
			JMenuItem logoutMenuItem = new JMenuItem("Logout");
			logoutMenuItem.setFont(font);
			accurrMenu.add(logoutMenuItem);
		menuBar.add(javax.swing.Box.createHorizontalGlue());
		menuBar.add(accurrMenu);

		//文本输入域
		textArea = new JTextArea();
		textArea.setFont(font);
		
		//设置滚动条
		scoll = new JScrollPane(textArea);
		
		//设置边界
		textArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		textArea.setMargin(new Insets(20, 20, 20, 20));
		textArea.setBackground(Color.WHITE);
		jp1.add(scoll);
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.weightx = 1;
		constraint.weighty = 0.7;
		gridBag.setConstraints(jp1, constraint);
		frame.add(jp1);
		
		textArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
				codestr.add(textArea.getText());
				curr = codestr.size();
				last = curr-1;
			}
		});
		
		// 输入
		inputArea = new JTextArea();
		scoll = new JScrollPane(inputArea);
		jp2.add(scoll);
		inputArea.setFont(font);
		inputArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		
		//设置边框文字
		inputArea.setBorder(BorderFactory.createTitledBorder("Input Area"));
		
		// 显示结果
		outputArea = new JTextArea();
		scoll = new JScrollPane(outputArea);
		outputArea.setFont(font);
		outputArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		outputArea.setBorder(BorderFactory.createTitledBorder("Output Area"));
		jp2.add(scoll);
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.weightx = 1;
		constraint.weighty = 0.28;
		gridBag.setConstraints(jp2, constraint);
		frame.add(jp2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int w = frame.getToolkit().getScreenSize().width;
		int h = frame.getToolkit().getScreenSize().height;
		frame.setBounds((w - 1000) / 2, (h - 750) / 2, 1000, 750);
		frame.setVisible(true);
		
		//菜单事件监听
		excuteMenuItem.addActionListener(new MenuItemActionListener());
		SaveEvent(saveMenuItem, openMenuItem, versionMenu, inputArea, outputArea);
		
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean hasLogin = RemoteHelper.getInstance().getUserService().hasLogin();
					if (hasLogin) 
						new NewFile(MainFrame.this, textArea, inputArea, outputArea, versionMenu);
					else 
						JOptionPane.showMessageDialog(null, "Please login!", "Warning", JOptionPane.WARNING_MESSAGE);
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		loginMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Login(MainFrame.this, openMenuItem, textArea, inputArea, outputArea, versionMenu, newMenuItem, versionMenu);
			}
		});
		
		logoutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					RemoteHelper.getInstance().getUserService().hasLogout();
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
				new MainFrame();
			}
		});
		
		//鼠标移入移出效果
		fileMenu.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				fileMenu.setForeground(Color.BLUE);
			}
			public void mouseExited(MouseEvent e){
				fileMenu.setForeground(Color.BLACK);
			}
		});
		
		editMenu.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				editMenu.setForeground(Color.BLUE);
			}
			public void mouseExited(MouseEvent e){
				editMenu.setForeground(Color.BLACK);
			}
		});
		
		runMenu.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				runMenu.setForeground(Color.BLUE);
			}
			public void mouseExited(MouseEvent e){
				runMenu.setForeground(Color.BLACK);
			}
		});
		
		versionMenu.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				versionMenu.setForeground(Color.BLUE);
			}
			public void mouseExited(MouseEvent e){
				versionMenu.setForeground(Color.BLACK);
			}
		});
		
		accurrMenu.addMouseListener(new MouseAdapter() {
			ImageIcon image2 = new ImageIcon("picture/account2.png");
			public void mouseEntered(MouseEvent e){
				accurrMenu.setIcon(image2);
			}
			public void mouseExited(MouseEvent e){
				accurrMenu.setIcon(image1);
			}
		});
	}
	
	//撤销监听
	class UndoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (last > 0 && last < curr) {
				last --;
				textArea.setText("");
				textArea.setText(codestr.get(last));
			}
		}
		
	}
	
	//重做监听
	class RedoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (last >= 0 && last < curr - 1) {
				last ++;
				textArea.setText("");
				textArea.setText(codestr.get(last));
			}
		}
	}
	
	//执行监听
	class MenuItemActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("Execute")) {
				try {
					String output = RemoteHelper.getInstance().getExecuteService().execute(textArea.getText(), inputArea.getText());
					outputArea.setText(output);
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			} 
		}
	}
	
	//在server端保存事件
	public static void SaveEvent(JMenuItem saveMenuItem, JMenuItem openMenuItem, JMenu version, JTextArea inputArea, JTextArea outputArea) {
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean hasLogin;
				String username = null;
				String project = null;
				try {
					hasLogin = RemoteHelper.getInstance().getUserService().hasLogin();
					username = RemoteHelper.getInstance().getUserService().getUsername();
					project = RemoteHelper.getInstance().getIOService().getProject();
					if (hasLogin) {
						if (project.equals(getProject())) {
							String code = textArea.getText();
							RemoteHelper.getInstance().getIOService().writeFile(code, username, project);
							latestFile(username, project, openMenuItem, version, textArea, inputArea, outputArea);
						} 
						else {
							String code = textArea.getText();
							RemoteHelper.getInstance().getIOService().writeFile(code, username, getProject());
							latestFile(username, project, openMenuItem, version, textArea, inputArea, outputArea);
						}
					} 
					else 
						JOptionPane.showMessageDialog(null, "Please login!", "Warning", JOptionPane.WARNING_MESSAGE);
					
				} catch (RemoteException e2) {
					e2.printStackTrace();
				}
				
			}
		});
	}
	
	//更新open列表和version列表
	public static void latestFile(String userId, String project, JMenuItem openMenuItem, JMenu version, JTextArea textArea, JTextArea inputArea, JTextArea outputArea) {
		try {
			File[] files = RemoteHelper.getInstance().getIOService().getVersionList(userId, project);
			openMenuItem.removeAll();
			saveOpenList(userId, openMenuItem, textArea, version, project, inputArea, outputArea);
			saveVersion(files, version, userId, project, textArea);
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
	}
	
	//设置当前工程
	public static void setProject(String projectName) {
		project = projectName;
	}
	
	//获得当前所在工程
	public static String getProject() {
		return project;
	}
	
	//保存后更新工程列表
	public static void saveOpenList(String username, JMenuItem openitem, JTextArea textArea, JMenu version, String project, JTextArea inputArea, JTextArea outputArea) {
		ArrayList<File> fileList;
		try {
			fileList = RemoteHelper.getInstance().getIOService().readFileList(username);
			for (int a = 0; a < fileList.size(); a++) {
				String fileContent = fileList.get(a).getName();
				JMenuItem fileItem = new JMenuItem(fileContent);
				fileItem.setFont(font);
				openitem.add(fileItem);
				
				File[] versionList = RemoteHelper.getInstance().getIOService().getVersionList(username, fileContent);
				if (versionList != null) {
					fileItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							codestr.clear();
							inputArea.setText("");
							outputArea.setText("");
							MainFrame.setProject(fileContent);
							saveVersion(versionList, version, username, fileContent, textArea);
							try {
								textArea.setText("");
								String latest = Login.fileSort(versionList);
								ArrayList<String> content = RemoteHelper.getInstance().getIOService().readFile(username, fileContent, latest);
								for (int k = 0; k < content.size(); k++) {
									textArea.append(content.get(k));
									textArea.append("\n");
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				} 
				else 
					MainFrame.setProject(fileContent);
			}
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
	}
	
	//保存后更新版本列表
	public static void saveVersion(File[] versionlist, JMenu version, String username, String project, JTextArea textArea) {
		version.removeAll();
		if (versionlist.length != 0) {
			for (int i = 0; i < versionlist.length; i++) {
				String ver = versionlist[i].getName();
				String[] str = ver.split("\\.");
				JMenuItem file = new JMenuItem(str[0]);
				file.setFont(font);
				version.add(file);
				
				file.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						codestr.clear();
						try {
							textArea.setText("");
							ArrayList<String> content = RemoteHelper.getInstance().getIOService().readFile(username, project, ver);
							for (int k = 0; k < content.size(); k++) {
								textArea.append(content.get(k));
								textArea.append("\n");
							}
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		}
	}
}