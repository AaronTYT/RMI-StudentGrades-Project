package threeTierHonours;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface tierThreeInterface extends Remote {
	public String getResult(double input, List<Integer> scoreArray, int fails) throws RemoteException;
	public double getAverage(List<Integer> unitScores) throws RemoteException;
	public List<String> getStudentResults(int studentID) throws RemoteException;
	public void setStudentResults(int id, double averageScore, double top12, int fails) throws RemoteException;
	public void setUnitResults(List<String> unitNames, List<Integer> unitScores, int studentID) throws RemoteException;
}
