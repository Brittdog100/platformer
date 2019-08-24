package asset.thread;

public class BackgroundProcess implements Runnable{

	@Override
	public void run(){}

	public final void initiate(){new Thread(this).start();}

}
