package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import spell.ISpellCorrector.NoSimilarWordFoundException;
import spell.SpellCorrector;

public class SpellCorrectorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	SpellCorrector corrector = new SpellCorrector();

	@Before
	public void setUp() throws Exception {
		String dict = "test" + File.separator + "dict.txt";
		corrector.useDictionary(dict);
	}

	@After
	public void tearDown() throws Exception {
		corrector.clear();
	}

	@Test
	public void testCorrectWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("remember");
		assertEquals(1, words.size());
	}
	
	@Test
	public void testOneDistAlterationWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("friendli");
		assertTrue(words.contains("friendly"));
	}
	
	@Test
	public void testOneDistDeletionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("adventur");
		assertTrue(words.contains("adventure"));
	}
	@Test
	public void testOneDistInsertionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("jumpss");
		assertTrue(words.contains("jumps"));
	}
	@Test
	public void testOneDistTranspositionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("littel");
		assertTrue(words.contains("little"));
	}
	
	@Test
	public void testTwoDistAlterationWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("prbctide");
		assertTrue(words.contains("practice"));
	}
	
	@Test
	public void testTwoDistDeletionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("expla");
		assertTrue(words.contains("explain"));
	}
	@Test
	public void testTwoDistInsertionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("himmsself");
		assertTrue(words.contains("himself"));
	}
	@Test
	public void testTwoDistTranspositionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("porbabyl");
		assertTrue(words.contains("probably"));
	}
	@Test
	public void testNoAlterationWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("imposzibru");
		assertEquals(0, words.size());
	}
	@Test
	public void testNoDeletionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("sssstronger");
		assertEquals(0, words.size());
	}
	@Test
	public void testNoInsertionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("nonrecognit");
		assertEquals(0, words.size());
	}
	@Test
	public void testNoTranspositionWord() throws NoSimilarWordFoundException {
		Set<String> words = corrector.suggestSimilarWords("xetrarodinariyl");
		assertEquals(0, words.size());
	}
	
	

}
