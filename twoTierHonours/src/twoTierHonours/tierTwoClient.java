package twoTierHonours;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Collections;

import java.net.MalformedURLException;

public class tierTwoClient {
	private static Scanner scan = null;
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		try {
			tierTwoInterface twoAPI = (tierTwoInterface) Naming.lookup("rmi://localhost:7777/tierTwo");    
			scan = new Scanner(System.in);
			//scan = new Scanner(System.in);
			
			int count = 3;
			System.out.println("Welcome to the honours pre-assessment system.");
			
			//Authenticate a user.
			for(;;) {
				System.out.println("Please enter your username: ");
				String username = scan.nextLine();
				
				System.out.println("And please enter your password: ");
				String password = scan.nextLine();
				
				if(username.equals("a") && password.equals("a")
						|| username.equals("b") && password.equals("b")) {
					System.out.println("");
					System.out.println("Welcome " + username);
					break;
				}else {
					if(count == 1) {
						System.out.println("Last chance before this program is locked.");
					}
					if(count == 0) {
						System.out.println("You have tried 3 times. Security fraud alert, End program.");
						System.exit(0);
					}
					count--;
					System.out.println("");
					System.out.println("Invalid credentials, please try again.");
					System.out.println("You have " + count + " tries remaining.");
				}
			}
			
			Scanner scan2 = new Scanner(System.in);
			
			int id = 0;
			try {
				System.out.println("Please enter your studentID: ");
				int inputID = scan.nextInt();
				id = inputID;
			}catch(Exception e) {
				System.out.println("Invalid input. Please enter only digits.");
			}
			
			
			int numUnits = 0;
			for(;;) {
				try {
					System.out.println("Please enter between 12-30 units: ");
					int numInputUnits = scan.nextInt();
					
					if(numInputUnits < 12 || numInputUnits > 30) {
						System.out.println("Invalid number of units. Please enter between 12-30 units: ");
						continue;
					}else {
						numUnits = numInputUnits;
						break;
					}
				}catch(Exception e) {
					System.out.println("Invalid input. Please enter only digits.");
				}
			}
			
			//unitNames and unitScores
			List<String>unitNames = new ArrayList<String>();
			List<Integer>unitScores = new ArrayList<>();
			
			int fails = 0;
			
			for(int i = 0; i < numUnits; i++) {
				System.out.println("Enter your unit name number " + (i+1) + ": ");
				String unitName = scan2.nextLine();
				
				unitNames.add(unitName); 
				
				System.out.println("Enter your unit score between 0 to 100 for "
					+ unitNames.get(i) + ": ");
				int score = scan.nextInt();
						
				System.out.println(""); 
				unitScores.add(score);
				
				if(score < 50) {
					fails++;
				}
			}
			
			double averageScore = twoAPI.getAverage(unitScores);
			
			//Results whether he or she is admitted into Honors. 
			String result = twoAPI.getResult(averageScore, unitScores, fails);
			
			Collections.sort(unitScores, Collections.reverseOrder());
			
			double top12 = 0;
			for(int i = 0; i < 12; i++) {
				top12 += unitScores.get(i);
			}
			double topAverage = top12 / 12;
			
			//Results
			System.out.println("---------------------------");
			System.out.println("Results: ");
			System.out.println("---------------------------");
			System.out.println("");
			
			System.out.println("Student ID: " + id);
			System.out.println("Average score is: " + averageScore);
			System.out.println("Top 12 average score is: " + topAverage);
			System.out.println("Fails: " + fails);
			System.out.println("");
			System.out.println("---------------------------");
			
			for(int i = 0; i < numUnits; i++) {
				System.out.println("Unit #" 
			    + (i+1) + ": " + unitNames.get(i) + " - " + unitScores.get(i));
			}
			
			System.out.println("This student is " + result);
						
		}catch(Exception e) {
			System.out.println("ERROR: The Honours Pre-Assessment Program is not running....");
			e.printStackTrace();
		}
	}
}
