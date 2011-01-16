package iconcerto.wiki.generators;

import iconcerto.wiki.parser.Headers;
import iconcerto.wiki.parser.Links;
import iconcerto.wiki.parser.Root;
import iconcerto.wiki.parser.Texts;

public class XHTMLGenerator extends AbstractGenerator {

	/**
	 * Internal link pattern parameter. 
	 * Example: /wiki/{value}
	 */
	public static final String INTERNAL_LINK_PATTERN = "INTERNAL_LINK_PATTERN";
	
	@Override
	public void begin() {

	}

	@Override
	public void end() {
		
	}
	
	@Override
	public void visit(Headers header) {
		String headerTagName = header.getType().toString().toLowerCase();
		append('<').append(headerTagName).append('>');
		append(header.getText());
		append("</").append(headerTagName).append('>');
		append('\n');
		
	}

	@Override
	public void visit(Links link) {		
		String url = "";
		if (Links.Types.EXTERNAL.equals(link.getType())) {
			url = link.getUrl();
		}
		else if (Links.Types.INTERNAL.equals(link.getType())) {
			//slow prototype code
			url = getParameter(INTERNAL_LINK_PATTERN, "/{value}").replace("{value}", link.getUrl());
		}
		append("<a href=\"").append(url).append("\">");
		append(link.getTitle());
		append("</a>");
	}

	@Override
	public void visit(Texts text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Root root) {
		// TODO Auto-generated method stub

	}

}
