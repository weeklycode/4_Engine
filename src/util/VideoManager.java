package util;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class VideoManager {
	private JFrame frame;
	private GraphicsDevice vc;

	public VideoManager(JFrame jf){
		frame = jf;
		vc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}

	public boolean createBufferStrategy(int numBuffers){
		if (frame.isVisible()){
			try{
				frame.createBufferStrategy(numBuffers);
				if (frame.getBufferStrategy() == null){
					DebugPrint.errln("was unable to create a buffer strategy!");
				}
			}catch(Exception e){
				return false;
			}
			return true;
		}
		return false;
	}

	public BufferStrategy getBufferStrategy(){
		return frame.getBufferStrategy();
	}

	public DisplayMode[] getCompatibleDisplayModes(){
		return vc.getDisplayModes();
	}

	public DisplayMode getHighestPixelDisplayMode(DisplayMode[] in){
		DisplayMode best = null;
		if (in.length>0){
			best = in[0];
			for (DisplayMode dm : in){
				if ((best.getHeight()*best.getWidth())<(dm.getHeight()*dm.getWidth())){//if the pixel count is more
					best=dm;
				}
			}
		}
		return best;
	}

	public DisplayMode getHighestPixelDisplayMode(){
		return getHighestPixelDisplayMode(getCompatibleDisplayModes());
	}

	public void setDisplayMode(DisplayMode dm){
		vc.setDisplayMode(dm);
	}

	public void setToHighestPixelDisplayMode(){
		setDisplayMode(getHighestPixelDisplayMode());
	}

	public boolean setUpFrame(){
		try{
			if (frame != null){
				frame.setLocation(0, 0);
				frame.setResizable(false);
				frame.setSize(vc.getDisplayMode().getWidth(), vc.getDisplayMode().getHeight());
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}


	public boolean setUpFrame(int aspectX, int aspectY){
		try{
			if (frame != null){

				int x = (int) (aspectX*Math.floor(Math.min(vc.getDisplayMode().getWidth()/aspectX,vc.getDisplayMode().getHeight()/aspectY)));
				int y = (int) (aspectY*Math.floor(Math.min(vc.getDisplayMode().getWidth()/aspectX,vc.getDisplayMode().getHeight()/aspectY)));

				DebugPrint.addLvl();
				DebugPrint.println("using resolution " + String.valueOf(x) + "x" + String.valueOf(y));
				DebugPrint.subLvl();

				if (x==0 || y==0){
					DebugPrint.errln("incompatible aspect ratio attempted:");
					DebugPrint.addLvl();
					DebugPrint.errln(String.valueOf(aspectX) + ":" + String.valueOf(aspectY));
					DebugPrint.subLvl();
					return false;
				}

				frame.setSize(x, y);
				frame.setResizable(false);
				frame.setLocation((vc.getDisplayMode().getWidth()-frame.getWidth())/2, (vc.getDisplayMode().getHeight()-frame.getHeight())/2);
				return true;
			}else{
				DebugPrint.errln("frame was null");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/*
	public static boolean toFullScreen(JFrame jf){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (gd.isDisplayChangeSupported()){
			gd.setFullScreenWindow(jf);
			if (gd.getFullScreenWindow() == jf){
				return true;
			}
		}
		return false;
	}
	 */

	public JFrame getFrame(){
		return frame;
	}

	public GraphicsDevice getGraphicsDevice(){
		return vc;
	}

	public boolean emptyFullScreen(boolean onlyLocalWindow){//meaning only if its frame (the local var)
		DebugPrint.addLvl();
		try{
			if (vc.getFullScreenWindow() == frame || (!onlyLocalWindow &&  vc.getFullScreenWindow() != null)){
				DebugPrint.println("emptying full screen frame");
				vc.setFullScreenWindow(null);
				if (vc.getFullScreenWindow() == null){
					return true;
				}
			}else if (vc.getFullScreenWindow() == null){
				DebugPrint.println("no frame was in full screen");
			}else if (vc.getFullScreenWindow() != frame && onlyLocalWindow){
				DebugPrint.println("a frame was found in full screen, but was looking for only local frame. (F.S. Frame was not local window)");
			}else{//it is the frame and onlyLocalWindow is true
				DebugPrint.errln("ERROR: frame was not emptied for unkown reason. This is not an Exception-type Erorr. Please check VideoManager emptyFullScreen(boolean onlyLocalWindow) method");
			}
			return false;
		}finally{
			DebugPrint.subLvl();
		}
	}

	public DisplayMode getDisplayMode(){
		return vc.getDisplayMode();
	}

	public void destroyFrame(){
		DebugPrint.println("disposing frame");
		frame.dispose();
	}

	public void destroy(){
		DebugPrint.printlnAdd("destroying video manager");
		try{
			destroyFrame();
		}finally{
			DebugPrint.subLvl();
		}
	}
}
