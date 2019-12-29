package event;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("serial")
public class MousePressEvent extends MouseEvent implements Event{

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
	}

	public MousePressEvent(MouseEvent e){
		super(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getXOnScreen(),e.getYOnScreen(),e.getClickCount(),e.isPopupTrigger(),e.getButton());
	}
	public MousePressEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger){
		super(source,id,when,modifiers,x,y,clickCount,popupTrigger);
	}
	public MousePressEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button){
		super(source,id,when,modifiers,x,y,clickCount,popupTrigger,button);
	}
	public MousePressEvent(Component source, int id, long when, int modifiers, int x, int y, int xAbs, int yAbs, int clickCount, boolean popupTrigger, int button){
		super(source,id,when,modifiers,x,y,xAbs,yAbs,clickCount,popupTrigger,button);
	}

	@Override
	public Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	@Override
	public String getName(){
		return "EVENT_MOUSE_RELEASE";
	}
}
