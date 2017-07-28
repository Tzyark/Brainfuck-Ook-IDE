//需要客户端的Stub
package service;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IOService extends Remote{
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException;
	
	public ArrayList<String> readFile(String userId, String fileName, String version) throws RemoteException;
	
	public ArrayList<File> readFileList(String userId) throws RemoteException;
	
	public String register(String name, String password, boolean nameEmpty, boolean passwordEmpty) throws RemoteException;
	
	public String newFile(String userId, String projectName) throws RemoteException;
	
	public String getProject() throws RemoteException;
	
	public boolean isProjectExisted(String userId, String projectname) throws RemoteException;
	
	public File[] getVersionList(String username, String project) throws RemoteException;
}
