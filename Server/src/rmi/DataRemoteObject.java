package rmi;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String username, String fileName) throws RemoteException{
		return iOService.writeFile(file, username, fileName);
	}

	@Override
	public ArrayList<String> readFile(String username, String fileName, String version) throws RemoteException{
		return iOService.readFile(username, fileName, version);
	}

	@Override
	public ArrayList<File> readFileList(String username) throws RemoteException{
		return iOService.readFileList(username);
	}
	
	public String register(String username, String password, boolean nameEmpty, boolean passwordEmpty) throws RemoteException{
		return iOService.register(username, password, nameEmpty, passwordEmpty);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return userService.logout(username);
	}

	public boolean loginJudge(String name, String password) throws RemoteException {
		return userService.loginJudge(name, password);
	}

	@Override
	public String newFile(String username, String projectName) throws RemoteException {
		return iOService.newFile(username, projectName);
	}

	@Override
	public boolean hasLogin() throws RemoteException {
		return userService.hasLogin();
	}

	@Override
	public String getUsername() throws RemoteException {
		return userService.getUsername();
	}

	@Override
	public String getProject() throws RemoteException {
		return iOService.getProject();
	}

	@Override
	public boolean isProjectExisted(String username, String projectname) throws RemoteException {
		return iOService.isProjectExisted(username, projectname);
	}

	@Override
	public File[] getVersionList(String username, String project) throws RemoteException {
		return iOService.getVersionList(username, project);
	}

	@Override
	public String execute(String code, String param) throws RemoteException {
		return executeService.execute(code, param);
	}

	@Override
	public boolean hasLogout() throws RemoteException {
		return userService.hasLogout();
	}
}