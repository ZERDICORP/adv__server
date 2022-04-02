package core;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import constants.PacketType;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Route
{
	PacketType value();
}
