package testTracker.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataManager 
{
	public static void saveData()
	{
		try
		{
			FileOutputStream FOS = new FileOutputStream("TestData.dat"); 
			ObjectOutputStream OOS = new ObjectOutputStream(FOS); 
			OOS.writeObject(TestExplorer.tests);
//			OOS.writeObject(Dashboard.tests);
			OOS.writeObject(Test.getGlobalSubjects());
			OOS.writeObject(Test.getGlobalTargets());
			OOS.writeObject(Test.getChangeLog()); 
			OOS.writeObject(Settings.boundaries);
			OOS.close(); 
			FOS.close(); 
			
//			System.out.println("SAVING");
//			System.out.println(TestExplorer.tests);
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public static void loadData()
	{
		try
		{
			FileInputStream FIS = new FileInputStream ("TestData.dat"); 
			ObjectInputStream OIS = new ObjectInputStream(FIS); 
			
			TestExplorer.tests = (ArrayList<Test>)OIS.readObject(); 
//			Dashboard.tests = (ArrayList<Test>)OIS.readObject(); 
			Test.setGlobalSubjects((ArrayList<String>)OIS.readObject());
			Test.setGlobalTargets((ArrayList<Integer>)OIS.readObject());
			Test.setChangeLog((ArrayList<String>)OIS.readObject());
			Settings.boundaries = (int[]) OIS.readObject(); 
			OIS.close();
			FIS.close();
			
//			System.out.println("LOADING");
//			System.out.println(TestExplorer.tests);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
