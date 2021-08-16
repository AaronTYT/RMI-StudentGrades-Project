package threeTierHonours;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import java.util.ArrayList;
import java.util.Collections;

import threeTierHonours.tierThreeServer;

public class tierThreeServer extends UnicastRemoteObject implements tierThreeInterface{

	public tierThreeServer() throws RemoteException {
		super();
	}
	
	private static final long serialVersionUID = 1L;

	//Run here to start the server.
	public static void main(String[] args) throws RemoteException, AlreadyBoundException{
		try {
			Registry registry = LocateRegistry.createRegistry(7777);
			registry.bind("tierThree", new tierThreeServer());
			System.out.println("The Honours Application is running.....");
		}catch(Exception e){
			System.out.println("ERROR: The Honours Application is not running....");
		}
	}


	public String getResult(double courseAverge, List<Integer> scoreArray, int fails) throws RemoteException {
		double topAverage = 0;
		double top12 = 0;
		Collections.sort(scoreArray, Collections.reverseOrder());
		
		//Replace 1 to 12
		for(int i = 0; i < 1; i++) {
			top12 += scoreArray.get(i);
		}
		
		//Replace 1 to 12
		topAverage = top12 / 1;
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
	
	public List<String> getStudentResults(int studentID) throws RemoteException{
		try {
			Connection con = tierThreeDBServer.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM StudentDetails WHERE studentID = " + studentID);
			
			ResultSet result = statement.executeQuery();
			
			List<String>studentData = new ArrayList<String>();
			while(result.next()) {	
				studentData.add(result.getString("StudentID"));
				studentData.add(result.getString("Avg"));
				studentData.add(result.getString("Top12"));
				studentData.add(result.getString("Fails"));
			}
			
			return studentData;
			
		}catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	
	public void setStudentResults(int id, double averageScore, double top12, int fails) throws RemoteException {
	
		//Set student's id, avg, top12, fails 
		//AND figure out later how to set specific units and names.
		try {
			Connection con = tierThreeDBServer.getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO StudentDetails "
					+ "(StudentID, Avg, Top12, Fails) VALUES ('" + id + "', '" 
					+ averageScore + "', '" + top12 + "', '" + fails + "')");
			statement.executeUpdate();
			
			System.out.println("Student's insertion complete.");
			System.out.println("");
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void setUnitResults(List<String> unitNames, List<Integer> unitScores, int studentID) throws RemoteException{
		try {
			
			Connection con2 = tierThreeDBServer.getConnection();
			
			//Append unitIDs from unitdetails.
			List<String> unitIDs = new ArrayList<String>();
			for(int i = 0; i < unitNames.size(); i++) {
					
				PreparedStatement statement2 = con2.prepareStatement("INSERT INTO unitdetails(UnitName)"
						+ " VALUES('" + unitNames.get(i)+ "')");
				statement2.executeUpdate();
			}
			
			for(int i = 0; i < unitNames.size(); i++) {
				PreparedStatement statement3 = con2.prepareStatement("SELECT UnitID "
						+ "FROM unitdetails WHERE UnitName = '" + unitNames.get(i) + "'");
				ResultSet result2 = statement3.executeQuery();
				while(result2.next()) {	
					unitIDs.add(result2.getString("UnitID"));
				}
			}
			
			for(int i = 0; i < unitScores.size(); i++) {
				
				PreparedStatement statement4 = con2.prepareStatement("INSERT INTO resultunitsdetails(UnitID, StudentID, Score)"
						+ " VALUES('" + unitIDs.get(i) + "', '" + studentID 
						+ "', '" + unitScores.get(i) + "')");
				statement4.executeUpdate();
			}

			System.out.println("Student's units insertion complete.");
			System.out.println("");
			
		}catch (Exception e) {
			System.out.println("setUnitResults error: " + e);
		}
		
	}

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
