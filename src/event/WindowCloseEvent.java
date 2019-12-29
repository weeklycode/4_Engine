package event;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class WindowCloseEvent implements Event{

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
	}
	
	@Override
	public Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	@Override
	public String getName(){
		return "EVENT_WINDOW_CLOSE";
	}

}
