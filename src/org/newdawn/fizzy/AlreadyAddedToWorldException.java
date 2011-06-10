package org.newdawn.fizzy;

@SuppressWarnings("serial")
public class AlreadyAddedToWorldException extends RuntimeException {
	@Override
	public String getMessage() {
		return "This method requires that the body has NOT been added to the world already";
	}
}
