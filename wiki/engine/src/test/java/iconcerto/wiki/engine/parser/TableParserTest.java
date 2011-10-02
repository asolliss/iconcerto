package iconcerto.wiki.engine.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

public class TableParserTest {

	private TableParser tableParser;
	
	@Before
	public void setUp() {
		tableParser = new TableParser();
	}
	
	@Test
	public void testOneCellTable() {
		String cellText = "first cell";
		String code = "||"+cellText+"||";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		tableParser.setCurrentParent(mock(Element.class));
		Element element = tableParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Table.class, element.getClass());
		
		Table table = (Table) element;
		assertNotNull(table.getChildren());
		assertEquals(1, table.getChildren().size());
		Element tableChild = table.getChildren().get(0);
		assertNotNull(tableChild);
		assertEquals(Row.class, tableChild.getClass());
		
		Row row = (Row) tableChild;
		assertNotNull(row.getChildren());
		assertEquals(1, row.getChildren().size());
		assertEquals(0, row.getFirstCharIndex());
		assertEquals(code.length()-1, row.getLastCharIndex());
		Element rowChild = row.getChildren().get(0);
		assertNotNull(rowChild);
		assertEquals(Cell.class, rowChild.getClass());
		
		Cell cell = (Cell) rowChild;
		assertNotNull(cell.getChildren());
		assertEquals(1, cell.getChildren().size());
		assertEquals(2, cell.getFirstCharIndex());
		assertEquals(code.length()-3, cell.getLastCharIndex());
		Element cellChild = cell.getChildren().get(0);
		assertNotNull(cellChild);
		assertEquals(Paragraph.class, cellChild.getClass());
		
		Paragraph paragraph = (Paragraph) cellChild;
		assertEquals(cellText, paragraph.getText());
		assertEquals(2, paragraph.getFirstCharIndex());
		assertEquals(code.length()-3, paragraph.getLastCharIndex());
	}
	
	@Test
	public void testTwoCellsOneRowTable() {
		String cell1Text = "first cell";
		String cell2Text = "second cell";
		String code = "||"+cell1Text+"||"+cell2Text+"||";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		tableParser.setCurrentParent(mock(Element.class));
		Element element = tableParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Table.class, element.getClass());
		
		Table table = (Table) element;
		assertNotNull(table.getChildren());
		assertEquals(1, table.getChildren().size());
		Element tableChild = table.getChildren().get(0);
		assertNotNull(tableChild);
		assertEquals(Row.class, tableChild.getClass());
		
		Row row = (Row) tableChild;
		assertNotNull(row.getChildren());
		assertEquals(2, row.getChildren().size());
		
		assertEquals(0, row.getFirstCharIndex());
		assertEquals(code.length()-1, row.getLastCharIndex());
		
		//first cell
		Element row1Child = row.getChildren().get(0);
		assertNotNull(row1Child);
		assertEquals(Cell.class, row1Child.getClass());
		
		Cell cell1 = (Cell) row1Child;
		assertNotNull(cell1.getChildren());
		assertEquals(1, cell1.getChildren().size());
		
		assertEquals(2, cell1.getFirstCharIndex());
		assertEquals(1+cell1Text.length(), cell1.getLastCharIndex());
		
		Element cell1Child = cell1.getChildren().get(0);
		assertNotNull(cell1Child);
		assertEquals(Paragraph.class, cell1Child.getClass());
		
		Paragraph paragraph1 = (Paragraph) cell1Child;
		assertEquals(cell1Text, paragraph1.getText());
		
		//second cell
		Element row2Child = row.getChildren().get(1);
		assertNotNull(row2Child);
		assertEquals(Cell.class, row2Child.getClass());
		
		Cell cell2 = (Cell) row2Child;
		assertNotNull(cell2.getChildren());
		assertEquals(1, cell2.getChildren().size());
		
		assertEquals(4+cell1Text.length(), cell2.getFirstCharIndex());
		assertEquals(3+cell1Text.length()+cell2Text.length(), cell2.getLastCharIndex());
		
		Element cell2Child = cell2.getChildren().get(0);
		assertNotNull(cell2Child);
		assertEquals(Paragraph.class, cell2Child.getClass());
		
		Paragraph paragraph2 = (Paragraph) cell2Child;
		assertEquals(cell2Text, paragraph2.getText());
	}
	
	@Test
	public void testTwoCellsTwoRowsTable() {
		String cell1Text = "first cell";
		String cell2Text = "second cell";
		String code = "||"+cell1Text+"||\n||"+cell2Text+"||";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		tableParser.setCurrentParent(mock(Element.class));
		Element element = tableParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Table.class, element.getClass());		
		Table table = (Table) element;
		assertNotNull(table.getChildren());
		assertEquals(2, table.getChildren().size());
		
		//first row
		Element table1Child = table.getChildren().get(0);
		assertNotNull(table1Child);
		assertEquals(Row.class, table1Child.getClass());
		Row row1 = (Row) table1Child;
		assertNotNull(row1.getChildren());
		assertEquals(1, row1.getChildren().size());
		
		assertEquals(0, row1.getFirstCharIndex());
		assertEquals(4+cell1Text.length(), row1.getLastCharIndex());
		
		//first cell
		Element row1Child = row1.getChildren().get(0);
		assertNotNull(row1Child);
		assertEquals(Cell.class, row1Child.getClass());
		
		Cell cell1 = (Cell) row1Child;
		assertNotNull(cell1.getChildren());
		assertEquals(1, cell1.getChildren().size());
		
		assertEquals(2, cell1.getFirstCharIndex());
		assertEquals(1+cell1Text.length(), cell1.getLastCharIndex());
		
		Element cell1Child = cell1.getChildren().get(0);
		assertNotNull(cell1Child);
		assertEquals(Paragraph.class, cell1Child.getClass());
		
		Paragraph paragraph1 = (Paragraph) cell1Child;
		assertEquals(cell1Text, paragraph1.getText());
		
		//second row
		Element table2Child = table.getChildren().get(1);
		assertNotNull(table2Child);
		assertEquals(Row.class, table2Child.getClass());
		Row row2 = (Row) table2Child;
		assertNotNull(row2.getChildren());
		assertEquals(1, row2.getChildren().size());
		
		assertEquals(5+cell1Text.length(), row2.getFirstCharIndex());
		assertEquals(code.length()-1, row2.getLastCharIndex());
		
		//second cell
		Element row2Child = row2.getChildren().get(0);
		assertNotNull(row2Child);
		assertEquals(Cell.class, row2Child.getClass());
		
		Cell cell2 = (Cell) row2Child;
		assertNotNull(cell2.getChildren());
		assertEquals(1, cell2.getChildren().size());
		
		assertEquals(7+cell1Text.length(), cell2.getFirstCharIndex());
		assertEquals(6+cell1Text.length()+cell2Text.length(), cell2.getLastCharIndex());
		
		Element cell2Child = cell2.getChildren().get(0);
		assertNotNull(cell2Child);
		assertEquals(Paragraph.class, cell2Child.getClass());
		
		Paragraph paragraph2 = (Paragraph) cell2Child;
		assertEquals(cell2Text, paragraph2.getText());
	}
	
	@Test
	public void testStringWithFirstOrSymbols() {
		String notCell1Text = "first not cell";
		String code = "||"+notCell1Text;
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		tableParser.setCurrentParent(mock(Element.class));
		Element element = tableParser.parse(mockedParseBundle);
		
		assertNull(element);
	}
	
	@Test
	public void testOneRowTableAndStringWithFirstOrSymbol() {
		String cell1Text = "first cell";
		String cell2Text = "second cell";
		String code = "||"+cell1Text+"||"+cell2Text+"||\n||not table";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		tableParser.setCurrentParent(mock(Element.class));
		Element element = tableParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(cell1Text.length()+cell2Text.length()+6, element.getLastCharIndex());		
		assertEquals(Table.class, element.getClass());
		
		Table table = (Table) element;
		assertNotNull(table.getChildren());
		assertEquals(1, table.getChildren().size());
		Element tableChild = table.getChildren().get(0);
		assertNotNull(tableChild);
		assertEquals(Row.class, tableChild.getClass());
		
		Row row = (Row) tableChild;
		assertNotNull(row.getChildren());
		assertEquals(2, row.getChildren().size());
		
		assertEquals(0, row.getFirstCharIndex());
		assertEquals(cell1Text.length()+cell2Text.length()+6, row.getLastCharIndex());
		
		//first cell
		Element row1Child = row.getChildren().get(0);
		assertNotNull(row1Child);
		assertEquals(Cell.class, row1Child.getClass());
		
		Cell cell1 = (Cell) row1Child;
		assertNotNull(cell1.getChildren());
		assertEquals(1, cell1.getChildren().size());
		
		assertEquals(2, cell1.getFirstCharIndex());
		assertEquals(1+cell1Text.length(), cell1.getLastCharIndex());
		
		Element cell1Child = cell1.getChildren().get(0);
		assertNotNull(cell1Child);
		assertEquals(Paragraph.class, cell1Child.getClass());
		
		Paragraph paragraph1 = (Paragraph) cell1Child;
		assertEquals(cell1Text, paragraph1.getText());
		
		//second cell
		Element row2Child = row.getChildren().get(1);
		assertNotNull(row2Child);
		assertEquals(Cell.class, row2Child.getClass());
		
		Cell cell2 = (Cell) row2Child;
		assertNotNull(cell2.getChildren());
		assertEquals(1, cell2.getChildren().size());
		
		assertEquals(4+cell1Text.length(), cell2.getFirstCharIndex());
		assertEquals(3+cell1Text.length()+cell2Text.length(), cell2.getLastCharIndex());
		
		Element cell2Child = cell2.getChildren().get(0);
		assertNotNull(cell2Child);
		assertEquals(Paragraph.class, cell2Child.getClass());
		
		Paragraph paragraph2 = (Paragraph) cell2Child;
		assertEquals(cell2Text, paragraph2.getText());
	}
	
}
