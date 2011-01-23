package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractElement implements Element {

	private Element parent;
	private List<Element> children = new ArrayList<Element>();
	private List<Element> unmodifiableChildren = Collections.unmodifiableList(children);
	private int firstCharIndex;
	private int lastCharIndex;
	private int level;
	private int relativePosition;
	
	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element parent) {
		if (parent != null) {			
			parent.addChild(this);
		}
		
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		
		this.parent = parent;
		evaluateLevel();
	}

	@Override
	public List<Element> getChildren() {
		return unmodifiableChildren;
	}

	@Override
	public void addChild(Element child) {
		children.add(child);
	}

	@Override
	public void removeChild(Element child) {
		children.remove(child);
	}

	@Override
	public int getFirstCharIndex() {
		return firstCharIndex;
	}

	@Override
	public void setFirstCharIndex(int firstCharIndex) {
		this.firstCharIndex = firstCharIndex;
	}

	@Override
	public int getLastCharIndex() {
		return lastCharIndex;
	}

	@Override
	public void setLastCharIndex(int lastCharIndex) {
		this.lastCharIndex = lastCharIndex;
	}

	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public int getRelativePosition() {
		return relativePosition;
	}

	@Override
	public void setRelativePosition(int relativePosition) {
		this.relativePosition = relativePosition;
	}

	private void evaluateLevel() {
		if (getParent() == null) {
			level = 0;
		}
		else {
			level = getParent().getLevel() + 1; 
		}
	}

}
