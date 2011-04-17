package iconcerto.wiki.parser;

import iconcerto.wiki.parser.exceptions.ParsingIndexOutOfBoundsException;

import java.util.Arrays;

public class LinkParser extends AbstractElementParser {

	public LinkParser() {
		addDelimiter(new Delimiter(Delimiter.DEFAULT, "[[".toCharArray(), "]]".toCharArray()));
	}

	@Override
	public Element parse(ParseBundle parseBundle) {
		Link link = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			if (ca.match(getDefaultDelimiter().getLeftDelimiter())) {
				int firstCharIndex = ca.getIndex() - 2;
				ca.lookForAtSingleLine(getDefaultDelimiter().getRightDelimiter());
				int lastCharIndex = ca.getIndex()-1;
				char[] meta = ca.getRange(firstCharIndex+2, lastCharIndex-1);
				int i = 0;
				for (char c: meta) {				
					if (c == '|') break;		
					i++; 
				}
				link = new Link();
				link.setParent(getCurrentParent());
				link.setFirstCharIndex(firstCharIndex);
				link.setLastCharIndex(lastCharIndex);			
				link.setUrl(new String(Arrays.copyOf(meta, i)));
				
				if (link.getUrl().contains("://")) {
					link.setType(Link.Type.EXTERNAL);
				}
				else {
					link.setType(Link.Type.INTERNAL);
				}
				
				if ((meta.length) > (i+1)) {
					link.setTitle(new String(Arrays.copyOfRange(meta, i+1, meta.length)));
				}
				else {
					link.setTitle(link.getUrl());
				}
			}
		}
		catch (ParsingIndexOutOfBoundsException e) {
			ca.setIndex(caIndex);
		}
		
		return link;
	}

}
