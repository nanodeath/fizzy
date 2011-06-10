package org.newdawn.fizzy;

@SuppressWarnings("serial")
public class NotAttachedToWorldException extends RuntimeException {

	@Override
	public String getMessage() {
		return "This method requires that the body has been added to the world first";
	}
}
