import java.io.*;

@SuppressWarnings("unused")
public class Runner 
{
	public static void main(String [] args) throws IOException
	{
		Board test = new Board ();
		test.phase = 2;
		test.step = 1;
		GraphicsLobby g = new GraphicsLobby(test);
	}
}
