package event;

import java.lang.annotation.Annotation;

import util.KeyHandler;

public class KeyPressEvent implements Event{
	
	private char key;
	
	public KeyPressEvent(char key){
		this.key=key;
	}
	
	public char getKey(){
		return key;
	}
	
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return KeyHandler.getAnnotation();
	}

	@Override
	public String getName() {
		return "EVENT_KEY_RELEASE";
	}
}
