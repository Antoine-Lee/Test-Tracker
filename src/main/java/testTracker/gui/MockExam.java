package testTracker.gui;

public class MockExam extends Test 
{
	
	public MockExam(String testName, int score, String reflection, int total, String subject) 
	{
		super(testName, score, reflection, total, subject);
	}
	
	public String toString()
	{
		return "MOCK EXAM: " + super.toString(); 
	}
}
