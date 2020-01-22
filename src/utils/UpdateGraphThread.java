package utils;

import dataStructure.graph;

public class UpdateGraphThread implements Runnable
{
	graph g;
	boolean work= true;
	int premc;
	MyGameGUI mainWin;
	
	public UpdateGraphThread(graph g,MyGameGUI mainWin) {
		super();
		this.g = g;
		this.premc = g.getMC();
		this.mainWin = mainWin;
		
	}




	@Override
	public synchronized void run(){
		while(work)
		{ 		
			if(premc<g.getMC())
			{
				mainWin.draw();
			    premc = g.getMC();
			 }
			 try 
			 {
				 Thread.sleep(10);
			 } catch (InterruptedException e)
			 {
				 e.printStackTrace();
			 }
		}
		
	}
	
	public void kill()
	{
		work=false;
		System.out.println("Killed");
	}
}
