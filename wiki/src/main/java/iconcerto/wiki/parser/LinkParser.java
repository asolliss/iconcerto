package iconcerto.wiki.parser;

import java.util.Arrays;

public class LinkParser extends AbstractElementParsers {

	@Override
	public Elements parse(ParseBundle parseBundle) {
		Links link = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			char c1 = ca.getChar();
			char c2 = ca.getChar();
			if (c1 == '[' && c2 == '[') {
				int firstCharIndex = ca.getIndex() - 2;
				ca.lookForAtSingleLine("]]".toCharArray());
				int lastCharIndex = ca.getIndex()-1;
				char[] meta = ca.getRange(firstCharIndex+2, lastCharIndex-1);
				int i = 0;
				for (char c: meta) {				
					if (c == '|') break;		
					i++; 
				}
				link = new Links();
				link.setParent(getCurrentParent());
				link.setFirstCharIndex(firstCharIndex);
				link.setLastCharIndex(lastCharIndex);			
				link.setUrl(new String(Arrays.copyOf(meta, i)));
				
				if (link.getUrl().contains("://")) {
					link.setType(Links.Types.EXTERNAL);
				}
				else {
					link.setType(Links.Types.INTERNAL);
				}
				
				if ((meta.length) > (i+1)) {
					link.setTitle(new String(Arrays.copyOfRange(meta, i+1, meta.length)));
				}
				else {
					link.setTitle(link.getUrl());
				}
			}
			else {
				ca.returnChar();
				ca.returnChar();
			}
		}
		catch (CharAccessorIndexOutOfBoundsException e) {
			ca.setIndex(caIndex);
		}
		
		return link;
	}

}
