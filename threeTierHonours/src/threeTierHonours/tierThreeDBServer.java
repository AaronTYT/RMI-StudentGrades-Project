package threeTierHonours;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class tierThreeDBServer {

	public static void main(String[] args) throws Exception {	
		//Start function called createTable() of creating the DB Tables from the students schema.
		createTable();
	}
	
	public static void createTable() throws Exception{
		try {
			Connection con = getConnection();
			/* 3NF Database schema structure and Add order::
			 * R11: studentID(PK), avg, top12, fails
			 * R121: unitID(PK), unitName
			 * R122: untiID(FK), studentID(FK), Score
			 */
			
			//Create ResultStudents Table
			//R112 = StudentID(PK), Avg, Top12 and Fails
			PreparedStatement createStudentDetails = con.prepareStatement
			("CREATE TABLE IF NOT EXISTS StudentDetails"
					+ "(StudentID INT NOT NULL PRIMARY KEY, Avg FLOAT NOT NULL, Top12 FLOAT NOT NULL, Fails INT NOT NULL)");
			createStudentDetails.executeUpdate();
			System.out.println("Created StudentDetails table.");
			
			//Create UnitDetails Table
			//R121: unitID(PK), unitName
			PreparedStatement createUnitDetails = con.prepareStatement
			("CREATE TABLE IF NOT EXISTS UnitDetails(UnitID INT AUTO_INCREMENT PRIMARY KEY, UnitName VARCHAR(50))");
			createUnitDetails.executeUpdate();
			System.out.println("Created UnitDetails table.");
			
			
			//Create UnitResults Table
			//R122: unitID(FK), studentID(FK), Score
			PreparedStatement createUnitResults = con.prepareStatement
			("CREATE TABLE IF NOT EXISTS ResultUnitsDetails(UnitID INT, StudentID INT, Score INT, "
					+ "FOREIGN KEY(UnitID) REFERENCES UnitDetails(UnitID), "
					+ "FOREIGN KEY(StudentID) REFERENCES StudentDetails(StudentID))");
			createUnitResults.executeUpdate();
			System.out.println("Created ResultUnitsDetails table.");
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			System.out.println("Created all 3 DB tables.");
		}
	}
	
	public static Connection getConnection() throws Exception{
		try {
			//Create a schema in MYSQL first name "students" and then it able to connect to my database.
			String url = "jdbc:mysql://localhost:3306/students";
			String username = "root";
			String password = "root";
			
			Connection conn = DriverManager.getConnection(url, username, password);
			//System.out.println("Connected to the students DB.");
			return conn;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
