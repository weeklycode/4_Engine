package event;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("serial")
public class MouseWheelEvent extends java.awt.event.MouseWheelEvent implements Event{

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
	}

	public MouseWheelEvent(java.awt.event.MouseWheelEvent e){
		super(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getXOnScreen(),e.getYOnScreen(),e.getClickCount(),e.isPopupTrigger(),e.getScrollType(),e.getScrollAmount(),e.getWheelRotation(),e.getPreciseWheelRotation());
	}
	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation){
		super(source,id,when,modifiers,x,y,clickCount,popupTrigger,scrollType,scrollAmount,wheelRotation);
	}
	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y, int xAbs, int yAbs, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation){
		super(source,id,when,modifiers,x,y,xAbs,yAbs,clickCount,popupTrigger,scrollType,scrollAmount,wheelRotation);
	}
	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y, int xAbs, int yAbs, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation, double preciseWheelRotation){
		super(source,id,when,modifiers,x,y,xAbs,yAbs,clickCount,popupTrigger,scrollType,scrollAmount,wheelRotation,preciseWheelRotation);
	}

	@Override
	public Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	@Override
	public String getName(){
		return "EVENT_MOUSE_WHEEL";
	}
}
