package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import event.Event;
import event.KeyPressEvent;
import event.KeyReleaseEvent;
import listener.Listener;

public class KeyHandler implements KeyListener{

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
		char[] value() default {};
	}

	@Override
	public void keyPressed(KeyEvent e){
		callEvent(new KeyPressEvent(e.getKeyChar()),e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e){
		callEvent(new KeyReleaseEvent(e.getKeyChar()),e.getKeyChar());
	}

	@Override
	public void keyTyped(KeyEvent e){
	}

	public static Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	public void callEvent(Event e, char key){
		try{
			Method listen;
			for (Listener l : Listener.getListeners()){
				listen = l.getClass().getMethod("listen", Event.class);
				if (listen.isAnnotationPresent(Listen.class)){
					char[] lt = ((Listen) listen.getAnnotation(Listen.class)).value();
					if (lt.length!=0){
						for (char c : lt){
							if (key == c){
								l.listen(e);
								break;
							}
						}
					}else{
						l.listen(e);
					}
				}
			}
		}catch (NoSuchMethodException e1){
			DebugPrint.errln("error trying to call event + " + e.getName() + ":");
			DebugPrint.addLvl();
			DebugPrint.spaces();
			e1.printStackTrace();
		}catch (SecurityException e2){
			DebugPrint.errln("error trying to call event + " + e.getName() + ":");
			DebugPrint.addLvl();
			DebugPrint.spaces();
			e2.printStackTrace();
		}
	}
}
