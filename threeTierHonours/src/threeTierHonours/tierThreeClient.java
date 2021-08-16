package threeTierHonours;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.net.MalformedURLException;

public class tierThreeClient {
	public static Scanner scan = null;
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		try {
			tierThreeInterface threeAPI = (tierThreeInterface) Naming.lookup("rmi://localhost:7777/tierThree");    
			scan = new Scanner(System.in);		
			
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
			
			int optionChoice = 0;
			for(;;) {
				
				System.out.println("You have 3 options: ");
				System.out.println("");
				System.out.println("1 - Retrieve a past student results for Honours Evaulation.");
				System.out.println("2 - Add a new student to the database system (includes an Honours Evaulation)");
				System.out.println("3 - Quit this program.");
				
				try {
					int inputID = scan.nextInt();
					optionChoice = inputID;
					
					//Retrieve a past student results for Honours Evaulation.
					if(optionChoice == 1) {
						//If a studentID does not exist in the database:
						Connection con = tierThreeDBServer.getConnection();
						PreparedStatement statement = con.prepareStatement("SELECT COUNT(1) FROM StudentDetails");
						
						ResultSet result = statement.executeQuery();
						int rowExist = 0;
						while(result.next()) {
							rowExist = result.getInt(1);
						}
						
						//If there is no data in the database.
						if(rowExist == 0) {
							System.out.println("There is no student data in the Database.");
						}else {
							Scanner scan2 = new Scanner(System.in);
							
							System.out.println("Please enter a past studentID: ");
							int id = scan2.nextInt();
							
							List<String>studentData = new ArrayList<String>();
							studentData = threeAPI.getStudentResults(id);
							
							if(studentData.size() == 0) {
								System.out.println("This student does not exist in the school's database.");
							}else {
								
								PreparedStatement statement2 = con.prepareStatement("SELECT unitdetails.UnitName, resultunitsdetails.Score "
								+ "FROM unitdetails "
								+ "INNER JOIN resultunitsdetails ON unitdetails.UnitID = resultunitsdetails.UnitID "
								+ "WHERE resultunitsdetails.StudentID = '" + id + "';");
								
								ResultSet result2 = statement2.executeQuery();
								
								List<String>studentUnitData = new ArrayList<String>();
								List<String>studentScoreData = new ArrayList<String>();
								while(result2.next()) {	
									studentUnitData.add(result2.getString("UnitName"));
									studentScoreData.add(result2.getString("Score"));
								}
								
								int studentID = 0;
								double avg = 0.0;
								double top12 = 0.0;
								int fails = 0;
								
								for(int i = 0; i < studentData.size(); i++) {
									if(i == 0) {
										String studentString = studentData.get(i);
										studentID = Integer.parseInt(studentString);
									}else if(i == 1) {
										String avgString = studentData.get(i);
										avg = Double.parseDouble(avgString);
									}else if(i == 2) {
										String top12String = studentData.get(i);
										top12 = Double.parseDouble(top12String);
									}else if(i == 3) {
										String failString = studentData.get(i);
										fails = Integer.parseInt(failString);
									}
								}
								
								System.out.println("StudentID " + studentID + " results:"); 
								System.out.println(""); 
								System.out.println("Average Course Marks: " + avg); 
								System.out.println("Best 12 marks average: " + top12); 
								System.out.println("Fails: " + fails); 
								System.out.println(""); 
								
								for(int i = 0; i < studentScoreData.size(); i++) {
									System.out.println("Unit #" 
								    + (i+1) + ": " + studentUnitData.get(i) + " - " + studentScoreData.get(i));
								}
								System.out.println(""); 
								
								String ans = "";
								if(fails >= 6) {
									ans = "DOES NOT QUALIFY FOR HONORS STUDY!";
								}else {
									// TODO Auto-generated method stub
									if(avg >= 70) {
										ans = "QUALIFIED FOR HONORS STUDY!";
										
									//if score is less then 70 or if the top 12 unit scores average marks is 80 or higher 
									}else if(avg < 70 && top12 >= 80) {
										ans = "MAY HAVE A GOOD CHANCE! Need further assessment!";
										
									//if score is less then 70 or if the top 12 unit scores average marks is 70 - 79
									}else if(avg < 70 && avg >= 70  || top12 <= 79) {
										ans = "MAY HAVE A CHANCE! Must be carefully reassessed and get the coordinator's special permission.";
									
									//if score is less then 70 and the average of the top 12 unit scores average marks is lower then 70.
									}else if (avg < 70 && top12 < 70) {
										ans = "DOES NOT QUALIFY FOR HONORS STUDY! Try Masters by course work.";
									}
								}
								System.out.println("StudentID " + studentID + " is " + ans);
							}					
						}	
					
					// Add a new student to the database system (not included the Honours Evaulation)
					}else if(optionChoice == 2) {
						Scanner scan2 = new Scanner(System.in);
						
						int studentID = 0;
						try {
							System.out.println("Please enter your studentID: ");
							studentID = scan.nextInt();
							
						}catch(Exception e) {
							System.out.println("Invalid input. Please enter only digits.");
						}
						
						int numUnits = 0;
						
						Connection con = tierThreeDBServer.getConnection();
						PreparedStatement statement = con.prepareStatement("SELECT StudentID FROM StudentDetails");
						
						ResultSet result = statement.executeQuery();
						
						int sameID = 0;
						while(result.next()) {
							sameID = result.getInt("StudentID");
						}
						
						if(studentID == sameID) {
							System.out.println("This studentID already exist in the database.");
						}else {
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
							
							
							//unitNames and unitScores Arrays
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
										
								unitScores.add(score);
								
								if(score < 50) {
									fails++;
								}
							}
							
							double averageScore = threeAPI.getAverage(unitScores);
							//Results whether he or she is admitted into Honors. 
							String result1 = threeAPI.getResult(averageScore, unitScores, fails);
							Collections.sort(unitScores, Collections.reverseOrder());
							
							double top12 = 0;
							
							for(int i = 0; i < 12; i++) {
								top12 += unitScores.get(i);
							}
							double topAverage = top12 / 12;
							
							
							//Set student's id, avg, top12, fails and figure out later how to obtain units.
							threeAPI.setStudentResults(studentID, averageScore, topAverage, fails);
							threeAPI.setUnitResults(unitNames, unitScores, studentID);
							
							System.out.println("Inserted student to the Database.");
							
							//Results
							System.out.println("---------------------------");
							System.out.println("Results: ");
							System.out.println("---------------------------");
							System.out.println("");
							
							System.out.println("Student ID: " + studentID);
							System.out.println("Average score is: " + averageScore);
							System.out.println("Top 12 average score is: " + topAverage);
							System.out.println("Fails: " + fails);
							System.out.println("");
							System.out.println("---------------------------");
							System.out.println("");
							
							
							for(int i = 0; i < numUnits; i++) {
								System.out.println("Unit #" 
							    + (i+1) + ": " + unitNames.get(i) + " - " + unitScores.get(i));
							}
							
							System.out.println("This student is " + result1);
						}
						
					//Quit program
					}else if(optionChoice == 3) {
						System.out.println("Ok, have a good day!");
						System.exit(0);
					}
				}catch (Exception e) {
					System.out.println("Please enter either 1 to 3 shown the options below: ");
					System.out.println("");
					System.out.println(e);
				}
			}
						
		}catch(Exception e) {
			System.out.println("ERROR: The Honours Pre-Assessment Program is not running....");
			e.printStackTrace();
		}
	}
}
