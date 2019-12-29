package util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import event.EventCaller;
import event.MouseMoveEvent;
import event.MousePressEvent;
import event.MouseReleaseEvent;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener{

	@Override
	public void mouseDragged(MouseEvent e){//dont know what this is
	}

	@Override
	public void mouseMoved(MouseEvent e){
		EventCaller.callEvent(new MouseMoveEvent(e));
	}

	@Override
	public void mouseClicked(MouseEvent e){//mouse press and release
	}

	@Override
	public void mousePressed(MouseEvent e){
		EventCaller.callEvent(new MousePressEvent(e));
	}

	@Override
	public void mouseReleased(MouseEvent e){
		EventCaller.callEvent(new MouseReleaseEvent(e));
	}

	@Override
	public void mouseEntered(MouseEvent e){//dont know what this is
	}

	@Override
	public void mouseExited(MouseEvent e){//or this
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		EventCaller.callEvent(new event.MouseWheelEvent(e));
	}

}
