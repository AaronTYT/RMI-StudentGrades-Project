# RMI-StudentGrades-Project
<b>Remote Method Invocation</b> (RMI) is an API service that produces a distributed system (DS) for allow invoke requests from client to server (two-tiered).<br> In this project, this DS determines whether he or she has the capacity of undertaking an honours program based on their WAM (Weighted Average Marks) results (assuming that its their only undergraduate degree).<br>

The following software tools to develop this project:
<ul>
  <li>Eclipse IDE (develop java scripts)</li>
  <li>MYSQL Workbench 8.0</li>
</ul>

The following requirements are shown below (only for the top 12 unit marks will be calculated as the final WAM score):
<ul>
  <li>If the student’s WAM is <b>more or equal to 70</b>, the DS <b>shall process an invitation to an honours program.</b></li>
  <li>If the student’s WAM is <b>between 65 and 70</b>, the DS is <b>not guaranteed that a student will be admitted but it requires the coordinator approval and the probability that a student will be admitted into honours is good but not certain</b></li>
  <li>If the student’s WAM is between <b>60 to 65</b>, the DS <b>will not produce an invitation, but it will place a special note for a coordinator to extensively review the applicant’s performance.</b></li>
  <li>If the student’s WAM is <b>lower than 60</b>, the DS <b>guarantees that the applicant is not qualified to admit into the honours program.</b></li>
</ul>

There is also one additional requirement not just based on WAM that automatically disqualifies an applicant’s entry into the honours program:
<ul>
  <li> If the student fails 6 or more units (unit score < 50) during the entire Bachelor course, then the DS automatically not invite the applicant into honours based on fail units. Even if the other units are exceptional (strong High Distinctions), unfortunately, based on the record and the requirements that were set by the University, he or she cannot appeal to this matter.</li>
</ul>                                                    

This RMI consists of 2 different types of tiers and the number of tiers represent the number of systems are used into this Distributed System Application.

## Two-Tiered
To setup as a two-tiered system, it uses a client machine and a server machine to simulate a DS application to allow remote requests communication based on the server application (does the honours evaulation execution).<br>
As you can see the code below, in the <b>tierTwoServer.java</b> java file (line 25-34), it first goes through a try and catch block statement to create a registry port (7777) to determine whether this port has been executed or not, otherwise it would have gone to the catch block statement (the registry is either execute or failed to use the port). Once the port has been allocated, then it requires to bind the existing port (7777) to this program to allow an RMI system to be generated and allowing the client to distribute requests to the server.

```
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
```
Go to <b>tierTwoClient.java</b> and execute the file to follow the prompts.

```
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
```

## Three-Tiered










