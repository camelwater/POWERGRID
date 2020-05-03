import java.io.*;

@SuppressWarnings("unused")
public class Runner 
{
	public static void main(String [] args) throws IOException
	{
		Board test = new Board ();
		//test.phase = 1;
		//test.step = 0;
		GraphicsLobby g = new GraphicsLobby(test);
	}
}
