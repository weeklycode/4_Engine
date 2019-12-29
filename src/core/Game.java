package core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entity.Entity;
import event.EventCaller;
import event.WindowCloseEvent;
import util.DebugPrint;
import util.GameVisualManager;
import util.KeyHandler;
import util.MouseHandler;

public class Game {

	private static Game instance;

	public final static int ASPECT_RATIO_X = 14;//def. 14
	public final static int ASPECT_RATIO_Y = 9;//def. 9
	public final static int ENTITY_MOVEMENT_MULTIPLIER = 1000;//def. 1k

	public static final int HITBOX_RGB = 0;//-16777216 is black
	public static final boolean USE_HITBOX_RGB_AS_ANTI = true;//make everything which isnt HITBOX_RGB a hitbox

	public final static byte MS_FRAME_WAIT = 30;//30ms between each frame
	public final static byte TICK_WAIT = 35;//35ms between each tick
	public final static boolean USE_TICK_DEBT = false;//if the game lags behind, dont wait until game is back at the same spot as if it never lagged
	public final static boolean RANDOM_ENTITY_UPDATE_ORDER = true;//def false

	public final static String NAME = "TEST_GAME";
	public final static String DISPLAY_NAME = "Test Game!";

	public final static String ASSET_PATH = "src/assets/";

	private GameVisualManager vm;

	protected static boolean running = false;
	protected static byte retries = 0;

	private static List<Entity> entities = new ArrayList<Entity>();
	protected static List<Entity> toAddEnts = new ArrayList<Entity>();
	protected static List<Entity>  toRemoveEnts = new ArrayList<Entity>();

	/***
	 * NOTES:
	 * 		-Entities are drawn with their coords being the top-left (meaning the picture is always to the right and bottom of the ent's coords)
	 ***/

	public synchronized static boolean init(){
		if (!running){
			boolean initSuccess = false;
			try{
				try{
					running = true;
					DebugPrint.printlnAdd("initializing...");
					DebugPrint.println("creating game instance");
					instance = new Game();
					DebugPrint.println("creating the game visual manager");
					instance.vm = new GameVisualManager(new JFrame(DISPLAY_NAME));

					DebugPrint.println("setting default close operation");
					instance.vm.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

					DebugPrint.println("registering the key handler");
					instance.getJFrame().addKeyListener(new KeyHandler());

					DebugPrint.println("registering mouse handler");
					instance.getJFrame().addMouseListener(new MouseHandler());
					instance.getJFrame().addMouseMotionListener(new MouseHandler());
					instance.getJFrame().addMouseWheelListener(new MouseHandler());

					DebugPrint.println("registering the window closing event handler");
					instance.getJFrame().addWindowListener(new WindowListener(){
						@Override
						public void windowClosing(WindowEvent e){
							EventCaller.callEvent(new WindowCloseEvent());
							instance.destroy();
						}

						@Override public void windowOpened(WindowEvent e){}
						@Override public void windowClosed(WindowEvent e){}
						@Override public void windowIconified(WindowEvent e){}
						@Override public void windowDeiconified(WindowEvent e){}
						@Override public void windowActivated(WindowEvent e){}
						@Override public void windowDeactivated(WindowEvent e){}
					});

					DebugPrint.println("setting game to visible");
					instance.vm.getFrame().setVisible(true);

					DebugPrint.println("setting up the frame");
					if (!instance.vm.setUpFrame(ASPECT_RATIO_X, ASPECT_RATIO_Y)){
						DebugPrint.errln("failed to set up frame, switching to fullscreen aspect ratio");
						if (!instance.vm.setUpFrame()){
							DebugPrint.errln("failed to set up frame with fullscreen aspect ratio. killing game");
							DebugPrint.addLvl();
							instance.destroy();
						}
					}

					DebugPrint.println("creating the BufferStrategy");
					instance.vm.createBufferStrategy(2);

					DebugPrint.subLvl();


					////////////////////////////////////////////////////////////////////////////////////////////////////////////
					new Thread(new Runnable(){
						@Override
						public void run(){
							long time;
							try{
								while (running){
									time = System.currentTimeMillis();
									instance.updateFrame();
									Thread.sleep(Math.max(MS_FRAME_WAIT-(System.currentTimeMillis()-time),0));//subtracts delta and makes sure it is above or equal to 0
								}
							}catch(InterruptedException e){
								DebugPrint.errln("Sleep exception in frame thread:");
								e.printStackTrace();
							}
						}
					},NAME+"_frameUpdater").start();

					////////////////////////////////////////////////////////
					new Thread(new Runnable(){
						@Override
						public void run(){
							long time;
							long debt = 0;
							try{
								while (running){
									time = System.currentTimeMillis();
									instance.tick();
									debt+=Math.min(0, TICK_WAIT-(System.currentTimeMillis()-time));
									Thread.sleep(Math.max(TICK_WAIT-(System.currentTimeMillis()-time-debt),0));//subtracts delta time and makes it equal or above 0
								}
							}catch(InterruptedException e){
								DebugPrint.errln("Sleep exception in tick thread:");
								e.printStackTrace();
							}
						}
					},NAME+"_tickUpdater").start();


					initSuccess = true;
					return true;

				}catch(Exception e){
					initSuccess=true;//error was caught
					e.printStackTrace();
					DebugPrint.subLvl();
					DebugPrint.println("initialization failed!");
					if (retries<5){
						retries++;
						running = false;
						DebugPrint.println("retrying (retry #" + retries + ")...");
						DebugPrint.setLevel(0);
						return init();
					}else{
						DebugPrint.println("too many retries! quitting...");
						DebugPrint.setSilent(true);
						instance.destroy();
					}
					return false;
				}
			}finally{
				if (!initSuccess){
					DebugPrint.println("Uncaught unkown error! Init unseccessful");
					DebugPrint.setSilent(true);
					instance.destroy();
				}
			}
		}else{
			DebugPrint.errln("init called, but already running");
		}
		return false;
	}

	public synchronized void updateFrame(){
		vm.draw(true);
	}
	public synchronized void tick(){
		entities.addAll(toAddEnts);
		entities.removeAll(toRemoveEnts);

		toAddEnts.clear();
		toRemoveEnts.clear();

		if (RANDOM_ENTITY_UPDATE_ORDER)
			entities.sort(new Comparator<Entity>(){
				Random r = new Random();
				@Override
				public int compare(Entity o1, Entity o2) {
					return (r.nextBoolean())?1:-1;
				}
			});

		for (Entity e : entities){
			if (e == null)
				continue;
			e.update();
		}
	}

	public synchronized void addEntity(Entity e){
		if (toRemoveEnts.contains(e)){
			toRemoveEnts.remove(e);
		}else{
			toAddEnts.add(e);
		}
	}
	public synchronized void removeEntity(Entity e){
		if (toAddEnts.contains(e)){
			toAddEnts.remove(e);
		}else{
			toRemoveEnts.add(e);
		}
	}
	public synchronized void setEntities(List<Entity> e){
		toAddEnts=e;
		toRemoveEnts.clear();
		toRemoveEnts.addAll(entities);
	}

	@SuppressWarnings("serial")
	public List<Entity> getEntities(){
		return new ArrayList<Entity>(){{//this anon class gives serial warn
			this.addAll(entities);
			this.addAll(toAddEnts);
			this.removeAll(toRemoveEnts);
		}};
	}

	public GameVisualManager getVisualManager(){
		return vm;
	}

	public JFrame getJFrame(){
		return vm.getFrame();
	}

	public static Game getInstance(){
		return instance;
	}

	public static BufferedImage getImageAsset(String assetName){
		try{
			return ImageIO.read(new File(ASSET_PATH + assetName));
		}catch (IOException e){
			DebugPrint.errln("returned null on a getAsset():");
			DebugPrint.addLvl();
			DebugPrint.errln("Asset name: \"" + assetName + "\"");
			DebugPrint.subLvl();
			return null;
		}
	}

	public void destroy(){
		DebugPrint.printlnAdd("destroying game...");
		running = false;
		vm.destroy();
		DebugPrint.println("done!");
		DebugPrint.subLvl();
		System.exit(0);
	}
}
