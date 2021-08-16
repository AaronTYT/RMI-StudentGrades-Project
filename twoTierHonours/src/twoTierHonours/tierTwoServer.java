package twoTierHonours;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.Collections;

import twoTierHonours.tierTwoServer;

public class tierTwoServer extends UnicastRemoteObject implements tierTwoInterface{

	public tierTwoServer() throws RemoteException {
		super();
	}
	
	private static final long serialVersionUID = 1L;

	//Run here to start the server.
	public static void main(String[] args) throws RemoteException, AlreadyBoundException{
		try {
			Registry registry = LocateRegistry.createRegistry(7777);
			registry.bind("tierTwo", new tierTwoServer());
			System.out.println("The Honours Application is running.....");
		}catch(Exception e){
			System.out.println("ERROR: The Honours Application is not running....");
		}
	}

	@Override
	public String getResult(double courseAverge, List<Integer> scoreArray, int fails) throws RemoteException {
		
		double topAverage = 0;
		double top12 = 0;
		
		Collections.sort(scoreArray, Collections.reverseOrder());
		
		for(int i = 0; i < 12; i++) {
			top12 += scoreArray.get(i);
		}
		topAverage = top12 / 12;
		
		if(fails >= 6) {
			return "DOES NOT QUALIFY FOR HONORS STUDY!";
		}else {
			// TODO Auto-generated method stub
			if(courseAverge >= 70) {
				return "QUALIFIED FOR HONORS STUDY!";
				
			//if score is less then 70 or if the top 12 unit scores average marks is 80 or higher 
			}else if(courseAverge < 70 && topAverage >= 80) {
				return "MAY HAVE A GOOD CHANCE! Need further assessment!";
				
			//if score is less then 70 or if the top 12 unit scores average marks is 70 - 79
			}else if(courseAverge < 70 && topAverage >= 70  || topAverage <= 79) {
				return "MAY HAVE A CHANCE! Must be carefully reassessed and get the coordinator's special permission.";
			
			//if score is less then 70 and the average of the top 12 unit scores average marks is lower then 70.
			}else if (courseAverge < 70 && topAverage < 70) {
				return "DOES NOT QUALIFY FOR HONORS STUDY! Try Masters by course work.";
			}
		}
		
		return null;
	}


	public int getID(double d) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getAverage(List<Integer> scoreArray) throws RemoteException {
		// TODO Auto-generated method stub
		
		int total = 0;
		for(int value : scoreArray) {
			total += value;
		}
		double average = total / scoreArray.size();
		return average;
	}


}
