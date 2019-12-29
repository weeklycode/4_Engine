package event;

import java.lang.annotation.Annotation;

public interface Event {
	public Class<? extends Annotation> getAnnotation();
	public String getName();
}
