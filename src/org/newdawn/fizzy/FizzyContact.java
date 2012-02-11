package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.contacts.Contact;

public class FizzyContact {
	private final Contact jboxContact;
	private List<Vector> contacts;
	private Vector referencePoint;

	public FizzyContact(Contact contact){
		jboxContact = contact;
	}
	
	public final int getContactCount(){
		return jboxContact.getManifold().pointCount;
	}
	
	public final Vector getReferencePoint(){
		if(referencePoint == null){
			referencePoint = Vector.fromVec2(jboxContact.getManifold().localPoint);
		}
		return referencePoint;
	}
	
	public final List<Vector> getContacts(){
		if(contacts == null){
			int totalCount = getContactCount();
			contacts = new ArrayList<Vector>(totalCount);
			for(int i = 0; i < totalCount; i++){
				contacts.add(Vector.fromVec2(jboxContact.getManifold().points[i].localPoint.mul(World.PIXELS_PER_METER)));
			}
		}
		return contacts;
	}
	
	public boolean isTouching(){
		return jboxContact.isTouching();
	}
	
	@Override
	public boolean equals(Object obj) {
		return jboxContact.equals(obj);
	}

	@Override
	public int hashCode() {
		return jboxContact.hashCode();
	}
}
