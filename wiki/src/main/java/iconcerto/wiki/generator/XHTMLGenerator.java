package iconcerto.wiki.generator;

import iconcerto.wiki.ejb.XHTML;
import iconcerto.wiki.parser.Element;
import iconcerto.wiki.parser.Header;
import iconcerto.wiki.parser.Link;
import iconcerto.wiki.parser.Paragraph;

@XHTML
public class XHTMLGenerator extends AbstractGenerator {

	/**
	 * Internal link pattern parameter. 
	 * Example: /wiki/{value}
	 */
	public static final String INTERNAL_LINK_PATTERN = "INTERNAL_LINK_PATTERN";

	public XHTMLGenerator() {
		super();
	}

	@Override
	public void visit(Header header) {
		String headerTagName = header.getType().toString().toLowerCase();
		append('<').append(headerTagName).append('>');
		append(header.getText());
		append("</").append(headerTagName).append('>');		
	}

	@Override
	public void visit(Link link) {
		String url = "";
		if (Link.Type.EXTERNAL.equals(link.getType())) {
			url = link.getUrl();
		}
		else if (Link.Type.INTERNAL.equals(link.getType())) {
			//slow prototype code
			url = getParameter(INTERNAL_LINK_PATTERN, "/{value}").replace("{value}", link.getUrl());
		}
		append("<a href=\"").append(url).append("\">");
		append(link.getTitle());
		append("</a>");
	}

	@Override
	public void visit(Paragraph paragraph) {
		append("<p>");
		if (paragraph.getChildren().size() > 0) {
			int beginIndex = 0;			
			for (Element element: paragraph.getChildren()) {
				append(paragraph.getText().substring(beginIndex, element.getRelativePosition()));
				beginIndex = element.getRelativePosition();
				element.accept(this);
			}
			append(paragraph.getText().substring(beginIndex, paragraph.getText().length()));
		}
		else {
			append(paragraph.getText());
		}		
		append("</p>");
	}

	@Override
	public boolean isOnlyRoot() {
		return true;
	}

}
