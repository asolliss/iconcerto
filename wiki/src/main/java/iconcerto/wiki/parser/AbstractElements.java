package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractElements implements Elements {

	private Elements parent;
	private List<Elements> children = new ArrayList<Elements>();
	private List<Elements> unmodifiableChildren = Collections.unmodifiableList(children);
	private int firstCharIndex;
	private int lastCharIndex;
	private int level;
	
	@Override
	public Elements getParent() {
		return parent;
	}

	@Override
	public void setParent(Elements parent) {
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
	public List<Elements> getChildren() {
		return unmodifiableChildren;
	}

	@Override
	public void addChild(Elements child) {
		children.add(child);
	}

	@Override
	public void removeChild(Elements child) {
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
	
	private void evaluateLevel() {
		if (getParent() == null) {
			level = 0;
		}
		else {
			level = getParent().getLevel() + 1; 
		}
	}

}
