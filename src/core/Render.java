package core;
import asset.Database;
import asset.ResourceLoader;
import asset.SpriteSheet;
import asset.Texture;
import asset.control.Key;
import asset.render.*;
import asset.map.IdentityMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.geom.Area;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ConcurrentModificationException;
import java.util.Vector;

public class Render extends JFrame{
	private static final long serialVersionUID = 8674352425862518163L;
	private JLayeredPane display;
	private JPanel bgLayer;
	private volatile int[] offset = {0,0,0,0};
	private volatile Integer l;
	private volatile Timer sttx;
	private volatile IdentityMap<JLabel>
		contentMap = new IdentityMap<>(),
		uiMap = new IdentityMap<>();
	public static final Integer CANVAS = new Integer(0),
								BACKGROUND = new Integer(1),
								TILE_LAYER = new Integer(2),
								ENTITY_LAYER = new Integer(3),
								OBSTRUCTION_LAYER = new Integer(4),
								UI_LAYER = new Integer(5);
	public static final Dimension WINDOWSIZE = Database.window_size;

	public Render(){
		super(Database.hasProperty("title") ? Database.<String>getGameProperty("title").replace("_"," ") : "platformer");
		l = 0;
		bgLayer = new JPanel();
		display = new JLayeredPane();
		display.setSize(WINDOWSIZE);
		display.setOpaque(false);
		display.setLayout(null);
		bgLayer.setSize(display.getSize());
		display.add(bgLayer,CANVAS);
		this.add(display);
		this.setSize(WINDOWSIZE);
		this.setResizable(false);
		Dimension tmp = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((tmp.width-getWidth())/2,(tmp.height-getHeight())/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public int width(){return display.getWidth();}
	public int height(){return display.getHeight();}

	public void setZ(int arg){l = new Integer(arg);}
	public void setZ(Integer arg){l = arg;}
	public Integer getZ(){return l;}
	public void clearZ(){clearZ(l);}
	public void clearZ(int arg){
		for(Component x : display.getComponentsInLayer(arg)){
			if(x instanceof TextureLabel)
				contentMap.remove(((TextureLabel)x).getIdentity());
			display.remove(x);
		}
		display.removeAll();
	}

	public void remove(long arg){
		if(!contentMap.containsKey(arg))
			return;
		display.remove(contentMap.get(arg));
		contentMap.remove(arg);
		display.repaint();
	}
	public void remove(Component arg){
		if(!contentMap.containsValue(arg))
			return;
		if(arg instanceof TextureLabel)
			remove(((TextureLabel)arg).getIdentity());
		else if(arg instanceof JLabel)
			remove(contentMap.getKey((JLabel)arg));
	}

	public void setVisible(long arg0,boolean arg1){
		if(!contentMap.containsKey(arg0))
			return;
		contentMap.get(arg0).setVisible(arg1);
	}

	public TextureLabel addTexture(Texture arg,int x,int y,Integer z){return addTexture(arg,x,y,x,true);}
	public TextureLabel addTexture(Texture arg,int x,int y,Integer z,boolean c){return addTexture(arg,x,y,z,c,true);}
	public TextureLabel addTexture(Texture arg,int x,int y,Integer z,boolean c,boolean v){
		TextureLabel tmp = new TextureLabel(arg,c);
		tmp.setSize(arg.width(),arg.height());
		tmp.setLocation(x,y);
		tmp.setIdentity(contentMap.add(tmp));
		tmp.setVisible(v);
		display.add(tmp,z);
		return tmp;
	}
	public TextureLabel addLevelTexture(Texture arg,Integer z){return addLevelTexture(arg,z,true);}
	public TextureLabel addLevelTexture(Texture arg,Integer z,boolean v){return addTexture(arg,0,arg.height(),z,false,v);}
	public SpriteLabel addSprite(SpriteSheet arg,int x,int y,Integer z){
		SpriteLabel tmp = new SpriteLabel(arg);
		tmp.setSize(arg.width(),arg.height());
		tmp.setLocation(x,y);
		tmp.setIdentity(contentMap.add(tmp));
		display.add(tmp,z);
		return tmp;
	}
	public WeaponLabel addWeapon(Texture arg,int x,int y,Integer z){
		WeaponLabel tmp = new WeaponLabel(arg);
		tmp.setLocation(x,y);
		tmp.setIdentity(contentMap.add(tmp));
		tmp.setCollidable(false);
		display.add(tmp,z);
		return tmp;
	}

	public HealthBar addHPBar(Texture arg0,Texture arg1,int arg2){
		HealthBar output = new HealthBar(arg0,arg1,arg2);
		output.setIdentity(uiMap.add(output));
		display.add(output,UI_LAYER);
		return output;
	}

	public void drawSplash(String arg){drawSplash(arg,Color.WHITE);}
	public void drawSplash(String arg0,Color arg1){drawSplash(arg0,ResourceLoader.Typeface.FIXEDSYS,32f,8,arg1);}
	public void drawSplash(String arg0,ResourceLoader.Typeface arg1,float arg2,int arg3,Color arg4){
		JLabel textLayer = new JLabel();
		textLayer.setSize(WINDOWSIZE);
		textLayer.setOpaque(false);
		textLayer.setBackground(new Color(0,0,0,0));
		display.add(textLayer,UI_LAYER);
		Graphics tmp = textLayer.getGraphics();
		tmp.setFont(ResourceLoader.loadFont(arg1).deriveFont(arg2));
		Color pc = tmp.getColor();
		tmp.setColor(arg4);
		int x = (384-(arg3*arg0.length()));
		tmp.drawString(arg0,((x>0) ? x : 0),224);
		tmp.setColor(pc);
		try{Thread.sleep(2500);}
		catch(Exception e){}
		display.remove(textLayer);
		display.repaint();
	}
	public void fill(Color arg){bgLayer.setBackground(arg);}

	public void setOffsets(int x,int y){
		if(sttx!=null && sttx.isRunning())
			sttx.stop();
		offset[0] = (x>0) ? (x-offset[2]) : (-1*offset[2]);
		offset[1] = (y<=0) ? (y+offset[3]) : (-1*offset[3]);
		refresh();
	}
	public void offsetXBy(int arg){
		if(offset[2]>0 || arg>0)
			offset[0] += arg;
	}
	public void offsetYBy(int arg){
		if(offset[3]>0 || arg<0)
			offset[1] += arg;
	}
	public int getOffsetX(){return offset[0];}
	public int getOffsetY(){return offset[1];}

	public boolean isOnScreen(float[] position,float width,float height){
		int[] tmp = getPosOf(position);
		return ((tmp[0]>0) && ((tmp[0])<width()) && ((tmp[1]-height)>0) && (tmp[1]<height()));
	}
	public boolean isInMargin(float[] position,int width,int height){
		int[] tmp = getPosOf(position);
		return ((tmp[0]>(width()-(width()*Database.scroll_margin_x))) && ((tmp[0]+(width*16))<(width()*Database.scroll_margin_x)) && ((tmp[1]-height)>0) && (tmp[1]<height()));
	}
	public void setFollowing(final float[] pos,final float width,final float height){
		sttx = new Timer((int)Database.scroll_tick,null){
			private static final long serialVersionUID = 2507313511263855724L;
			@Override
			public void fireActionPerformed(ActionEvent e){
				int tlx = (int)((pos[0]*Database.tile_size)-offset[2]),
					tly = height()-(int)((pos[1]*Database.tile_size)-offset[3]),
					sax = 0,
					say = 0;
				if(tlx<(width()*Database.scroll_margin_x))
					sax = -1;
				else if(tlx+(Database.tile_size*width)>(width()*(1-Database.scroll_margin_x)))
					sax = 1;
				if(tly<(height()*Database.scroll_margin_y))
					say = -1;
				else if(tly+(Database.tile_size*height)>(height()*(1-Database.scroll_margin_y)))
					say = 1;
				if(sax==0 && say==0)
					return;
				offsetXBy(sax);
				offsetYBy(say);
				refresh();
			}
		};
		sttx.start();
	}

	public int[] getPosOf(float[] arg){
		int[] output = new int[2];
		output[0] = (int)((arg[0]*Database.tile_size)-offset[2]);
		output[1] = height()-(int)((arg[1]*Database.tile_size)-offset[3]);
		return output;
	}

	public void refresh(){
		offset[2] += offset[0];
		offset[3] -= offset[1];
		try{
			for(JLabel cl : contentMap.values())
				if(cl instanceof TextureLabel)
					((TextureLabel)cl).setPosition(cl.getX()-offset[0],cl.getY()-offset[1]);
				else
					cl.setLocation(cl.getX()-offset[0],cl.getY()-offset[1]);
		}catch(ConcurrentModificationException e){refresh();return;}
		offset[0] = 0;
		offset[1] = 0;
	}

	public Vector<Long> collisions(WeaponLabel arg){
		Vector<Long> tmp = new Vector<>();
		Area ap = new Area(arg.getBounds());
		for(JLabel cl : contentMap.values())
			if(cl!=arg && (cl instanceof TextureLabel) && ((TextureLabel)cl).getIdentity()!=arg.getWield()){
				TextureLabel tl = (TextureLabel)cl;
				Area at = new Area(cl.getBounds());
				if(tl.canCollide() && ap.intersects(at.getBounds2D()))
					tmp.add(tl.getIdentity());
			}
		return tmp;
	}

	public ItemSlot addItemSlot(Texture arg0,Texture arg1,Key arg2,int arg3) {
		ItemSlot output = new ItemSlot(arg2,arg1,arg3);
		if(arg0!=null)
			output.setItemTexture(arg0);
		output.setIdentity(uiMap.add(output));
		display.add(output,UI_LAYER);
		return output;
	}

	public TextLabel addCounter(int x,int y){
		TextLabel output = new TextLabel();
		output.setLocation(x,y);
		output.setIdentity(uiMap.add(output));
		display.add(output,UI_LAYER);
		return output;
	}

	public JLabel getContent(long arg){return contentMap.get(arg);}

}
