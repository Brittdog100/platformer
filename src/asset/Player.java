package asset;

import asset.control.*;
import asset.thread.BackgroundProcess;
import java.io.File;
import java.awt.event.KeyEvent;
import asset.render.SpriteLabel;
import asset.render.TextLabel;
import asset.render.HealthBar;
import asset.event.collision.*;

public class Player extends Entity implements ControlListener, Faced, CollisionListener{
	private volatile KeyActivityMap ksm = new KeyActivityMap();
	private volatile int motionState = 0;
	private volatile Direction faceDir = Direction.RIGHT;
	private volatile SpriteLabel tl;
	private volatile HealthBar hb;
	private volatile int deaths = 0;
	private volatile TextLabel dc;
	private BackgroundProcess bch = new BackgroundProcess(){
		@Override
		public void run(){
			while(getHealth()>0){
				long st = System.currentTimeMillis();
				if(pos[1]<0)
					damage(1);
				acc[1] = 0;
				float
					pxv = vel[0],
					pyv = vel[1];
				boolean groundColl = false,
						onGround = isGrounded();
				if(onGround){
					groundColl = (Math.abs(vel[1])>0);
					pyv = vel[1];
					vel[1] = 0;
					if(getState(Key.SHIFT))
						motionState = 1;
					else if(getState(Key.LCTRL))
						motionState = 2;
					else
						motionState = 0;
					if(getState(Key.LEFT)){
						acc[0] = (-1f)*getProperty(("accel" + motionState),Float.class);
						setDirection(Direction.LEFT);
						if(motionState==0)
							tl.setState(1);
						else if(motionState==1)
							tl.setState(4);
						else if(motionState==2)
							tl.setState(5);
					}else if(getState(Key.RIGHT)){
						acc[0] = getProperty("accel" + motionState,Float.class);
						setDirection(Direction.RIGHT);
						if(motionState==0)
							tl.setState(1);
						else if(motionState==1)
							tl.setState(4);
						else if(motionState==2)
							tl.setState(5);
					}else{
						if(Math.abs(vel[0])<Database.min_velocity)
							vel[0] = 0;
						acc[0] = vel[0]*Database.friction;
						tl.setState(0);
					}
					if(getState(Key.UP))
						compoundDirection(Direction.UP);
					else if(getState(Key.DOWN))
						compoundDirection(Direction.DOWN);
					else
						negateY();
					if(getState(Key.JUMP)){
						acc[0] = 0;
						int jumpState = 0;
						if(facingUp())
							jumpState = 1;
						else if(facingDown())
							jumpState = 2;
						vel[1] = getProperty(("jump" + jumpState),Float.class);
						tl.setState(3);
					}
				}else{
					acc[0] = 0;
					acc[1] = Database.gravity;
					if(!(tl.getState()==3 || tl.getState()==6))
						tl.setState(2);
				}
				if(facingLeft())
					tl.setInverted(true);
				if(facingRight())
					tl.setInverted(false);
				boolean lc = Database.currentLevel.leftCollision(pos[0],pos[1],width(),height()),
						rc = Database.currentLevel.rightCollision(pos[0],pos[1],width(),height()),
						fc = Database.currentLevel.fullCollision(pos[0],pos[1],width(),height()),
						tc = Database.currentLevel.topCollision(pos[0],pos[1],width(),height()),
						ig = Database.currentLevel.grounded(pos[0],pos[1]+0.01f,width(),height()),
						wj = false;
				if(!onGround && fc){
					if((lc && getState(Key.GRAB_LEFT)) || (rc && getState(Key.GRAB_RIGHT))){
						wj = true;
						if(lc && getState(Key.GRAB_LEFT)){
							setDirection(Direction.RIGHT);
							tl.setInverted(false);
						}
						else if(rc && getState(Key.GRAB_RIGHT)){
							setDirection(Direction.LEFT);
							tl.setInverted(true);
						}
						tl.setState(6);
						if(getState(Key.GRAB_SLIDE))
							acc[1] = (Database.gravity/Math.abs(Database.friction));
						else{
							if(Math.abs(vel[1])<Database.min_velocity)
								vel[1] = 0;
							acc[1] = vel[1]*Database.friction;
						}
						vel[0] = 0;
						if(getState(Key.JUMP)){
							acc[0] = 0;
							int jumpStateV = 0;
							if(getState(Key.UP))
								jumpStateV = 1;
							else if(getState(Key.DOWN))
								jumpStateV = 2;
							int jumpStateH = 0;
							if((facingRight() && getState(Key.RIGHT)) || (facingLeft() && getState(Key.LEFT)))
								jumpStateH = 1;
							else if((facingRight() && getState(Key.LEFT)) || (facingLeft() && getState(Key.RIGHT)))
								jumpStateH = 2;
							vel[0] = (((float)Math.sqrt(2)*getProperty(("jump" + jumpStateH),Float.class)*((facingRight()) ? 1 : -1)))/2f;
							if(getState(Key.GRAB_PUSHOFF))
								vel[1] = getProperty(("jump" + jumpStateV),Float.class);
							else
								vel[1] = (((float)Math.sqrt(2)*getProperty(("jump" + jumpStateV),Float.class))/1.75f);
							tl.setState(3);
						}
					}else{
						wj = false;
						if(tl.getState()==6)
							tl.setState(2);
					}
				}else{
					wj = false;
					if(tl.getState()==6)
						tl.setState(2);
				}
				if(getState(Key.USE_ONE)){
					System.out.println(pos[0] + "," + pos[1]);
				}
				long tt = 0;
				do
					tt = (System.currentTimeMillis()-st);
				while(tt<Database.tick_time);
				float	lpx = pos[0],
						lpy = pos[1],
						dt = (((float)tt)/1000f);
				if(lc && rc)
					if(facingLeft())
						rc = false;
					else
						lc = false;
				float
					collForceX = (lc || rc) ? Math.abs((getProperty("mass",Float.class)*pxv)/(2*.015f)) : 0,
					collForceY = (groundColl || tc) ? Math.abs((getProperty("mass",Float.class)*pyv)/(2*.015f)) : 0,
					collForceV = (float)((Math.sqrt((collForceX*collForceX)+(collForceY*collForceY))));
				if((lc || rc || groundColl || tc) && (collForceX>=0 || collForceY>=0)){
					damage((int)(collForceV/getProperty("mfinteg",Float.class)));
				}
				if(lc && !wj){
					if(fc || !tc)
						pos[0] += 0.02;
					if(vel[0]<0){
						vel[0] = 0;
						acc[0] = 0;
					}
				}
				else if(rc && !wj){
					if(fc || !tc)
						pos[0] -= 0.02;
					if(vel[0]>0){
						vel[0] = 0;
						acc[0] = 0;
					}
				}
				if(tc){
					vel[1] = (vel[1]>0) ? 0 : vel[1];
					pos[1] -= 0.01;
				}
				if(ig){
					vel[1] = vel[1]<0 ? 0 : vel[1];
					pos[1] += 0.01;
				}
				if(onGround && vel[0]!=0 && acc[0]!=0){
					float tavq = acc[0]/vel[0];
					if(tavq<0)
						acc[0] += vel[0]*Database.friction;
				}
				pos[0] += ((vel[0]*dt) + (.5*(dt*dt)*acc[0]));
				pos[1] += ((vel[1]*dt) + (.5*(dt*dt)*acc[1]));
				vel[0] += (dt*acc[0]);
				vel[1] += (dt*acc[1]);
				if(vel[0]>getProperty("mvel" + motionState,Float.class))
					vel[0] = getProperty("mvel" + motionState,Float.class);
				if((lpx!=pos[0]) || (lpy!=pos[1])){
					tl.setLocation(pos);
				}
				if(Database.currentLevel.isCompleted(pos[0],pos[1])){
					core.Platformer.window.remove(tl.getIdentity());
					Database.advanceLevel();
					pos[0] = 0;
					pos[1] = 0;
					try{return;}
					catch(Exception e){}
					finally{respawn();}
				}
			}
			deaths++;
			dc.write("" + deaths);
			core.Platformer.window.remove(tl.getIdentity());
			Database.pauseBGM();
			try{Thread.sleep(Database.respawn_time);}
			catch(Exception e){}
			Database.resumeBGM();
			respawn();
		}
	};

	public Player(DataMap arg0,SpriteSheet arg1){super(arg0,arg1);}
	public Player(File arg0,SpriteSheet arg1){super(arg0,arg1);}
	public Player(DataMap arg0,File arg1){super(arg0,arg1);}
	public Player(File arg0,File arg1){super(arg0,arg1);}
	public Player(DataMap arg){super(arg);}
	public Player(File arg){super(arg);}

	public void makeCameraFollow(){core.Platformer.window.setFollowing(pos,width(),height());}
	public void respawn(){
		vel[0] = 0;
		vel[1] = 0;
		acc[0] = 0;
		acc[1] = 0;
		float[] temp = Database.currentLevel.getSpawn(pos[0]);
		core.Platformer.window.setOffsets(0,0);
		int[] tspa = core.Platformer.window.getPosOf(temp);
		core.Platformer.window.setOffsets(tspa[0]-(int)(core.Render.WINDOWSIZE.getWidth()/2),tspa[1]-(int)(core.Render.WINDOWSIZE.getHeight()/2));
		heal();
		setPos(temp[0],temp[1]);
	}

	@Override
	public void damage(int arg){
		super.damage(arg);
		if(arg<=0)
			return;
		if(this.hasProperty("hurtsound"))
			playSound("hurtsound");
		if(this.getHealth()<0){}
		else
			hb.setFrame(getHealth());
	}

	@Override
	public void setPos(float x,float y){
		super.setPos(x,y);
		SpriteSheet tmp = Database.getSpriteSheet("player");
		int tw = (int)(getProperty("width",Float.class)*Database.tile_size),
			th = (int)(getProperty("height",Float.class)*Database.tile_size);
		double
			ssfx = ((double)tw)/((double)tmp.width()),
			ssfy = ((double)th)/((double)tmp.height());
		if(ssfx!=1 || ssfy!=1)
			tmp = tmp.getScaled(ssfx,ssfy);
		if(tl!=null)
			tl.forceHalt();
		tl = core.Platformer.window.addSprite(tmp,0,0,core.Render.ENTITY_LAYER);
		tl.addCollisionListener(this);
		tl.setState(tmp.getProperty("default",Integer.class));
		double hbsf = 4;
		if(hb==null)
			hb = core.Platformer.window.addHPBar(Database.getTexture("health_i").scaleBy(hbsf),Database.getTexture("health_o").scaleBy(hbsf),getMaxHealth());
		hb.setFrame(getHealth());
		if(dc==null)
			dc = core.Platformer.window.addCounter(0,hb.getHeight());
		dc.write(deaths + "");
		makeCameraFollow();
		Database.currentLevel.startBGM();
		new Thread(bch).start();
	}

	@Override
	public Direction getDirection(){return faceDir;}
	@Override
	public void setDirection(Direction arg){faceDir = arg;}
	@Override
	public void compoundDirection(Direction arg){faceDir = faceDir.modify(arg);}
	@Override
	public void negateX(){
		if(facingUp())
			faceDir = Direction.UP;
		else if(facingDown())
			faceDir = Direction.DOWN;
		return;
	}
	@Override
	public void negateY(){
		if(facingLeft())
			faceDir = Direction.LEFT;
		else if(facingRight())
			faceDir = Direction.RIGHT;
		return;
	}
	@Override
	public boolean facingLeft(){return faceDir.goesLeft();}
	@Override
	public boolean facingRight(){return faceDir.goesRight();}
	@Override
	public boolean facingUp(){return faceDir.goesUp();}
	@Override
	public boolean facingDown(){return faceDir.goesDown();}
	@Override
	public boolean isFacing(Direction arg){return faceDir==arg;}

	@Override
	public boolean getState(int arg){return ksm.getState(arg);}
	@Override
	public boolean getState(Key arg){return ksm.getState(arg);}

	@Override
	public void keyTyped(KeyEvent e){}
	@Override
	public void keyPressed(KeyEvent e){ksm.setState(e.getKeyCode(),true);}
	@Override
	public void keyReleased(KeyEvent e){ksm.setState(e.getKeyCode(),false);}
	@Override

	public void collisionDetected(CollisionEvent arg){
		System.out.println(arg.getSourceIdentity() + ": " + arg.getDamage());
	}

}
