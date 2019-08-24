package asset.script;

import asset.map.ResourceToken;
import asset.ResourceLoader;
import asset.thread.BackgroundProcess;
import java.io.File;
import java.util.Scanner;

public class Script{
	private String[] lines;
	private ResourceToken token;

	public Script(ResourceToken arg){
		Scanner tmp = null;
		token = arg;
		switch(arg.getType()){
		case INPUTSTREAM:
			tmp = new Scanner(ResourceLoader.getStream(arg.getPath()));
			break;
		case FILE:
			try{tmp = new Scanner(new File(arg.getPath()));}
			catch(Exception e){return;}
			break;
		case URL:
			return;
		}
		if(tmp==null)
			return;
		java.util.ArrayList<String> tll = new java.util.ArrayList<>();
		while(tmp.hasNextLine())
			tll.add(tmp.nextLine());
		lines = new String[tll.size()];
		for(int x=0; x<tll.size(); x++)
			lines[x] = tll.get(x);
	}

	public ResourceToken getToken(){return token;}

	public void runOnThread(){
		for(String x : lines)
			System.out.println(x);
	}
	public void run(){new Thread(new BackgroundProcess(){public void run(){Script.this.runOnThread();}}).run();}

}
