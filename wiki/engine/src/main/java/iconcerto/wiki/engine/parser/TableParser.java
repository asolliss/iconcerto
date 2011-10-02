package iconcerto.wiki.engine.parser;

import iconcerto.wiki.engine.parser.exceptions.ParserException;
import iconcerto.wiki.engine.parser.exceptions.ParsingException;

public class TableParser extends AbstractElementParser {

	public TableParser() {
		addDelimiter(new Delimiter(Delimiter.DEFAULT, "||".toCharArray(), "||".toCharArray()));
		addElementParser(new ParagraphParser());
	}

	@Override
	public Element parse(ParseBundle parseBundle) {
		Table table = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			if (!(ca.isFirstCharOfLine() && ca.match(getDefaultDelimiter().getLeftDelimiter()))) return table;
			
			int index = ca.getIndex();
			ca.lookForAtSingleLine(getDefaultDelimiter().getRightDelimiter());
			ca.setIndex(index);
			
			
			table = new Table();
			table.setParent(getCurrentParent());
			table.setFirstCharIndex(ca.getIndex() - getDefaultDelimiter().getLeftDelimiter().length);

			ca.setIndex(ca.getIndex() - getDefaultDelimiter().getLeftDelimiter().length);

			while (ca.isFirstCharOfLine() && ca.match(getDefaultDelimiter().getLeftDelimiter())) {
				index = ca.getIndex();
				try {
					ca.lookForAtSingleLine(getDefaultDelimiter().getRightDelimiter());
				}
				catch (ParsingException e) {
					index = index - getDefaultDelimiter().getLeftDelimiter().length;
					break;
				}
				finally {
					ca.setIndex(index);
				}
				
				Row row = new Row();
				row.setParent(table);
				row.setFirstCharIndex(ca.getIndex()-getDefaultDelimiter().getLeftDelimiter().length);

				ca.setIndex(ca.getIndex() - getDefaultDelimiter().getLeftDelimiter().length);

				while (ca.match(getDefaultDelimiter().getLeftDelimiter())) {
					if (ca.isEmptyTillEndOfLine()) {
						break;
					}

					Cell cell = new Cell();
					cell.setFirstCharIndex(ca.getIndex());

					Element element = null;
					try {
						ca.pushStopSequence(getDefaultDelimiter().getRightDelimiter());
						for (ElementParsers elementParser: getElementsParsers()) {
							elementParser.setCurrentParent(cell);
							element = elementParser.parse(parseBundle);
							if (element != null) break;
						}
					}
					finally {
						ca.popStopSequence();
					}

					if (!ca.match(getDefaultDelimiter().getLeftDelimiter())) {							
						break;
					}
					ca.setIndex(ca.getIndex() - getDefaultDelimiter().getLeftDelimiter().length);

					cell.setParent(row);
					cell.setLastCharIndex(ca.getIndex()-1);
				}

				row.setLastCharIndex(ca.getIndex()-1);

			}
			table.setLastCharIndex(ca.getIndex()-1);

		}
		catch (ParserException e) {
			ca.setIndex(caIndex);
			table = null;
		}
		
		return table;
	}

}
