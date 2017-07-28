package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import service.UserService;

public class UserServiceImpl implements UserService{
	
	public static boolean hasLog = false;
	public static String username = "";

	@Override
	public boolean login(String username, String password) throws RemoteException {
		return true;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	//检测用户名和和密码
	public boolean loginJudge(String name, String password) throws RemoteException {
		boolean ret = false;
		boolean found = false;
		File file = new File("users");
		File[] filelist = file.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			found = found || (filelist[i].getName().equals(name));
		}
		if (!found) 
			JOptionPane.showMessageDialog(null, "NO such username!", "Error", JOptionPane.INFORMATION_MESSAGE);
		else {
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].getName().equals(name)) {
					try {
						BufferedReader reader = new BufferedReader(new FileReader("users/_info/" + name + "/password.txt"));
						String string = reader.readLine();
						reader.close();
						if (string.equals(password)) {
							ret = true;
							hasLog = true;
							username = name;
							return ret;
						} 
						else 
							JOptionPane.showMessageDialog(null, "Password is wrong!", "Error", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} 
			}
		} 
		return ret;
	}
	
	@Override
	//获取用户名
	public String getUsername() throws RemoteException {
		return username;
	}
	
	@Override
	//判断是否登录
	public boolean hasLogin() throws RemoteException {
		return hasLog;
	}

	@Override
	public boolean hasLogout() throws RemoteException {
		hasLog = false;
		return hasLog;
	}
}