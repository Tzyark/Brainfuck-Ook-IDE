package serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	public static String projectName = "";
	
	//写入文件
	public boolean writeFile(String code, String username, String project) {
		boolean ret = false;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String version = dateFormat.format(date);
		String suffix = (code.charAt(0) == 'O') ? ".ook" : ".bf";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("users/"+username+"/"+project+"/"+version+suffix));
			writer.write(code);
			writer.close();
			ret = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	//读取文件
	public ArrayList<String> readFile(String username, String filename, String version) {
		projectName = filename;
		ArrayList<String> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("users/"+username+"/"+filename+"/"+version));
			String string = null;
			while ((string = reader.readLine()) != null) {
				list.add(string);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//读取文件目录
	public ArrayList<File> readFileList(String username) {
		File file = new File("users/"+username);
		File[] list = file.listFiles();
		ArrayList<File> fileList = new ArrayList<>();
		for (int i = 0; i < list.length; i++) {
			fileList.add(list[i]);
			System.out.println(list[i].getName());
		}
		return fileList;
	}
	
	//保存注册信息
	public String register(String username, String password, boolean nameEmpty, boolean passwordEmpty) {
		File file = new File("users/"+username);
		if (!file.exists())
			file.mkdirs();
		else {
			if (!(nameEmpty || passwordEmpty)) {
				JOptionPane.showMessageDialog(null, "This account has existed!", "error", JOptionPane.WARNING_MESSAGE);
				return null;
			}
		}
		
		File path = new File("users/_info/"+username);
		if (!path.exists())
			path.mkdirs();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("users/_info/"+username+"/password.txt"));
			writer.write( password);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "You have registered successfully!", "Tip", JOptionPane.INFORMATION_MESSAGE);
		return null;
	}
	
	//新建工程目录
	public String newFile(String username, String projectname) {
		projectName = projectname;
		File project = new File("users/"+username+"/"+projectName);
		project.mkdirs();
		return null;
	}

	//获得当前project名称
	public String getProject() throws RemoteException {
		return projectName;
	}

	//判断新建project是否重名
	public boolean isProjectExisted(String username, String projectName) throws RemoteException {
		File file = new File("users/" + username);
		File[] list = file.listFiles();
		for (int i = 0; i < list.length; i++) 
			if (list[i].getName().equals(projectName)) return true; 
		return false;
	}

	//获取所有版本文件
	public File[] getVersionList(String username, String project) throws RemoteException {
		File file = new File("users/"+username+"/"+project);
		File[] fileList = file.listFiles();
		return fileList;
	}
}