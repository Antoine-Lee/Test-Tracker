package testTracker.gui;

import java.util.ArrayList;
import java.io.Serializable;

public class Test implements Serializable {
	public static ArrayList<String> globalSubjects = new ArrayList<String>(); // all subjects

	public static ArrayList<String> getGlobalSubjects() {
		return globalSubjects;
	}

	public static void setGlobalSubjects(ArrayList<String> globalSubjects) {
		Test.globalSubjects = globalSubjects;
	}

	private String testName;
	private int score;
	private String reflection;
	private int total;
	private String subject;

	private static ArrayList<String> changeLog = new ArrayList<String>();

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		changeLog.add("Changed test name <" + this.testName + "> to <" + testName + ">");
		this.testName = testName;

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		changeLog.add("Changed score <" + this.score + "> to <" + score + ">");
		this.score = score;
	}

	public String getReflection() {
		return reflection;
	}

	public void setReflection(String reflection) {
		changeLog.add("Changed reflection <" + this.reflection + "> to <" + reflection + ">");
		this.reflection = reflection;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		changeLog.add("Changed total <" + this.total + "> to <" + total + ">");
		this.total = total;
	}

	public void setSubject(String subject) {
		changeLog.add("Changed subject <" + this.subject + "> to <" + subject + ">");
		this.subject = subject;

		amendGlobalSubjects();
	}

	public static ArrayList<String> getChangeLog() {
		return changeLog;
	}

	public static void setChangeLog(ArrayList<String> changeLog) 
	{
		Test.changeLog = changeLog;
	}

	// public static void main(String[] args)
//	{
//		Test myTest = new Test (); 
//		myTest.score = 7; 
//		myTest.total = 101; 
//		System.out.println(myTest.testPercentage()); 
//	}
//	
	public Test(String testName, int score, String reflection, int total, String subject) {
//		super();
//		this.testName = testName;
//		this.score = score;
//		this.reflection = reflection;
//		this.total = total;
//		this.subject = subject;

		changeLog.add("Created a new test");
		setTestName(testName);
		setScore(score);
		setTotal(total);
		setSubject(subject);
		setReflection(reflection);

//		amendGlobalSubjects(); 
	}

	public String toString() {
		return subject + ", " + testName + ": " + testPercentage() + "%";
	}

	public String testPercentage() {
		double percentage = ((double) score / total) * 100;
		percentage = (double) Math.round(percentage * 10) / 10;
		return "" + percentage;
	}
	
	public double testPercentage (boolean rawPercentage)
	{
		double percentage = (double)score/(double)total;
		
		if (!rawPercentage)
			return percentage * 100; 
		else
			return percentage; 
	}

	private void amendGlobalSubjects() // if subject doesn't already exist in globalSubjects, add it to globalSubjects
	{
		if (!globalSubjects.contains(subject))
			globalSubjects.add(subject);
	}

	public String getSubject() {
		return subject;
	}
}
