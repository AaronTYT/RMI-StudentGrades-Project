package twoTierHonours;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface tierTwoInterface extends Remote {
	public String getResult(double input, List<Integer> scoreArray, int fails) throws RemoteException;
	public double getAverage(List<Integer> unitScores) throws RemoteException;
}
