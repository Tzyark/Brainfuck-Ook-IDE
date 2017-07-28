//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote{
	
	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;
	
	public boolean loginJudge(String name, String password) throws RemoteException;
	
	public boolean hasLogin() throws RemoteException;
	
	public String getUsername() throws RemoteException;
	
	public boolean hasLogout() throws RemoteException;
}
