package iconcerto.wiki.domain;

import static org.junit.Assert.*;

import org.junit.*;

public class PageTest {

	private Page page;
	
	@Before
	public void setUp() {
		page = new Page();
	}
	
	@Test
	public void initialStateShouldBeNew() {
		assertTrue(page.isNew());
	}
	
	@Test
	public void initialStateShouldBeUnchanged() {
		assertFalse(page.isChanged());
	}
	
	@Test
	public void stateShouldBeChancgedAfterCodeSetting() {
		page.setCode("new code");
		assertTrue(page.isChanged());
	}
	
	@Test
	public void stateShouldBeChancgedAfterXHTMLSetting() {
		page.setXHTML("<new>value</new>");
		assertTrue(page.isChanged());
	}

}
