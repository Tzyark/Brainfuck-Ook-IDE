package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rmi.RemoteHelper;

public class NewFile extends JDialog{
	private static final long serialVersionUID = 1L;
	Font font;
	JLabel nameLabel;
	JTextField projectName;
	JButton confirm;
	JButton cancel;
	JPanel panel;
	
	public NewFile(MainFrame frame, JTextArea textArea, JTextArea inputArea, JTextArea outputArea, JMenuItem version) {
		super(frame, "New Project", true);
		font = new Font("微软雅黑", Font.PLAIN, 17);
		panel = new JPanel();
		nameLabel = new JLabel("Project Name :");
		nameLabel.setFont(font);
		projectName = new JTextField(20);
		projectName.setFont(font);
		confirm = new JButton("Confirm");
		confirm.setFont(font);
		cancel = new JButton("Cancel");
		cancel.setFont(font);
		
		panel.add(nameLabel);
		panel.add(projectName);
		panel.add(confirm);
		panel.add(cancel);
		panel.setLayout(null);
		
		//设置边界
		nameLabel.setBounds(70, 30, 120, 20);
		projectName.setBounds(210, 30, 280, 30);
		confirm.setBounds(150, 80, 100, 30);
		cancel.setBounds(370, 80, 100, 30);
		
		//添加时间监听
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				version.removeAll();
				String username;
				try {
					username = RemoteHelper.getInstance().getUserService().getUsername();
					boolean isProjectExisted = RemoteHelper.getInstance().getIOService().isProjectExisted(username, projectName.getText());
					if (!isProjectExisted) {
						RemoteHelper.getInstance().getIOService().newFile(username, projectName.getText());
						MainFrame.setProject(projectName.getText());
						textArea.setText("");
						inputArea.setText("");
						outputArea.setText("");
						dispose();
					} 
					else 
						JOptionPane.showMessageDialog(null, "Project has existed!", "Error", JOptionPane.WARNING_MESSAGE);
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
		
		add(panel);
		int width = getToolkit().getScreenSize().width;
		int height = getToolkit().getScreenSize().height;
		setBounds((width - 600) / 2, (height - 200) / 2, 600, 200);
		setVisible(true);
	}
}