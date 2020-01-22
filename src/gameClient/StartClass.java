package gameClient;

import javax.swing.JOptionPane;

public class StartClass 
{
	public static void main(String[] args)
	{
		while(true)
		{
		String asrc = JOptionPane.showInputDialog("For authomatic game press a, else any key");
		boolean authomatic = (asrc.equals("a"));
		try {
		int level = Integer.valueOf(JOptionPane.showInputDialog("select level [0,23]"));
		Manual_client client =new Manual_client(level,authomatic);
		break;
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
	}
}}
