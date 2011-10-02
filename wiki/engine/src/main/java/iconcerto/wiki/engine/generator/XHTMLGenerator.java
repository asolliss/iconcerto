package iconcerto.wiki.engine.generator;

import iconcerto.wiki.engine.parser.Cell;
import iconcerto.wiki.engine.parser.Element;
import iconcerto.wiki.engine.parser.Header;
import iconcerto.wiki.engine.parser.Link;
import iconcerto.wiki.engine.parser.Paragraph;
import iconcerto.wiki.engine.parser.Row;
import iconcerto.wiki.engine.parser.Span;
import iconcerto.wiki.engine.parser.Table;

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
	public void visit(Span span) {
		append("<span class=\"").append(span.getType().toString().toLowerCase()).append("\">");
		if (span.getChildren().size() > 0) {
			int beginIndex = 0;			
			for (Element element: span.getChildren()) {
				append(span.getText().substring(beginIndex, element.getRelativePosition()));
				beginIndex = element.getRelativePosition();
				element.accept(this);
			}
			append(span.getText().substring(beginIndex, span.getText().length()));
		}
		else {
			append(span.getText());
		}		
		append("</span>");
	}

	@Override
	public void visit(Table table) {
		append("<table>");
		if (table.getChildren().size() > 0) {
			for (Element element: table.getChildren()) {
				element.accept(this);
			}
		}
		append("</table>");
	}

	@Override
	public void visit(Row row) {
		append("<tr>");
		if (row.getChildren().size() > 0) {
			for (Element element: row.getChildren()) {
				element.accept(this);
			}
		}
		append("</tr>");
	}

	@Override
	public void visit(Cell cell) {
		append("<td>");
		if (cell.getChildren().size() > 0) {
			for (Element element: cell.getChildren()) {
				element.accept(this);
			}
		}
		append("</td>");
	}

	@Override
	public boolean isOnlyRoot() {
		return true;
	}

}
