package Szinkodolo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Segment;
import org.eclipse.jface.viewers.StructuredViewerInternals;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * A token maker that turns text into a linked list of <code>Token</code>s for
 * syntax highlighting Microsoft Windows batch files.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public class SajatNyelvTokenMaker extends AbstractTokenMaker
{

	private int currentTokenStart;
	private int currentTokenType;

	/**
	 * Constructor.
	 */
	public SajatNyelvTokenMaker()
	{
		super(); // Initializes tokensToHighlight.
	}

	/**
	 * Checks the token to give it the exact ID it deserves before being passed
	 * up to the super method.
	 *
	 * @param segment
	 *            <code>Segment</code> to get text from.
	 * @param start
	 *            Start offset in <code>segment</code> of token.
	 * @param end
	 *            End offset in <code>segment</code> of token.
	 * @param tokenType
	 *            The token's type.
	 * @param startOffset
	 *            The offset in the document at which the token occurs.
	 */
	@Override
	public void addToken(Segment segment, int start, int end, int tokenType, int startOffset)
	{
		switch(tokenType)
		{
			// Since reserved words, functions, and data types are all passed
			// into here as "identifiers," we have to see what the token
			// really is...
			case SajatTokenTypes.Alap:
				int value = wordsToHighlight.get(segment, start, end);
				if(value != -1)
					tokenType = value;
				break;
		}

		super.addToken(segment, start, end, tokenType, startOffset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex)
	{
		return new String[] { "rem ", null };
	}

	/**
	 * Returns whether tokens of the specified type should have "mark
	 * occurrences" enabled for the current programming language.
	 *
	 * @param type
	 *            The token type.
	 * @return Whether tokens of this type should have "mark occurrences"
	 *         enabled.
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type)
	{
		//return type == SajatTokenTypes.Azonosito || type == SajatTokenTypes.VARIABLE;
		return type == SajatTokenTypes.Alap;
	}

	/**
	 * Returns the words to highlight for Windows batch files.
	 *
	 * @return A <code>TokenMap</code> containing the words to highlight for
	 *         Windows batch files.
	 * @see org.fife.ui.rsyntaxtextarea.AbstractTokenMaker#getWordsToHighlight
	 */
	@Override
	public TokenMap getWordsToHighlight()
	{

		TokenMap tokenMap = new TokenMap();

		tokenMap.put("Bekapcs", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Kikapcs", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("KockaFrissít", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("KonzolÍr", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("EmailKüld", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Várj", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Nyitva", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Zárva", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Óra", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Perc", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Másodperc", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Év", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Hónap", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("HónapNapja", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("HétNapja", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Gyök", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("Hatvány", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatIndít", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatLeállít", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatÚjraindít", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatSzinkronVégrehajt", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatFut", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatFolytat", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatFelfüggeszt", SajatTokenTypes.Fuggvenyhivas);
		tokenMap.put("FolyamatSzünetel", SajatTokenTypes.Fuggvenyhivas);

		tokenMap.put("szám", SajatTokenTypes.ValtozoTipus);
		tokenMap.put("Szám", SajatTokenTypes.ValtozoTipus);
		//tokenMap.put("szám:", SajatTokenTypes.ValtozoTipus);-->TOKENIZÁLVA
		//tokenMap.put("Szám:", SajatTokenTypes.ValtozoTipus);-->TOKENIZÁLVA

		tokenMap.put("Kilép", SajatTokenTypes.Kilep);

		tokenMap.put("Ha", SajatTokenTypes.Ciklus);
		tokenMap.put("Egyébként", SajatTokenTypes.Ciklus);
		tokenMap.put("Amíg", SajatTokenTypes.Ciklus);
		tokenMap.put("Ismételd", SajatTokenTypes.Ciklus);

		tokenMap.put("Tovább", SajatTokenTypes.Kulcsszo);
		tokenMap.put("Törd", SajatTokenTypes.Kulcsszo);
		tokenMap.put("Igaz", SajatTokenTypes.Modosito);
		tokenMap.put("Hamis", SajatTokenTypes.Modosito);
		tokenMap.put("igaz", SajatTokenTypes.Modosito);
		tokenMap.put("hamis", SajatTokenTypes.Modosito);

		return tokenMap;

	}

	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text
	 *            The text to break into tokens.
	 * @param startTokenType
	 *            The token with which to start tokenizing.
	 * @param startOffset
	 *            The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	@Override
	public Token getTokenList(Segment text, int startTokenType, final int startOffset)
	{
		resetTokenList();

		List<String> valtozonevek = GetValtozoNevek();

		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		// Token starting offsets are always of the form:
		// 'startOffset + (currentTokenStart-offset)', but since startOffset and
		// offset are constant, tokens' starting positions become:
		// 'newStartOffset+currentTokenStart'.
		int newStartOffset = startOffset - offset;

		currentTokenStart = offset;
		currentTokenType = startTokenType;

		for(int i = offset; i < end; i++)
		{

			char c = array[i];

			switch(currentTokenType)
			{

				case SajatTokenTypes.NULL:

					currentTokenStart = i; // Starting a new token here.

					switch(c)
					{
						case ' ':
						case '\t':
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case '{':
						case '}':
						{
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}
						case ',':
						{
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}

						default:

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(end > i + 4 && (array[i] == 'S' || array[i] == 's') && array[i + 1] == 'z'
									&& array[i + 2] == 'á' && array[i + 3] == 'm' && array[i + 4] == ':')
							{
								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.ValtozoTipus;
								i += 4;
								if(end > i + 1)
									++i;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								--i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as an identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;

					} // End of switch (c).

					break;

				case SajatTokenTypes.Terkoz:

					switch(c)
					{

						case ' ':
						case '\t':
							break; // Still whitespace.

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}

						default: // Add the whitespace token and start anew.

							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(end > i + 4 && (array[i] == 'S' || array[i] == 's') && array[i + 1] == 'z'
									&& array[i + 2] == 'á' && array[i + 3] == 'm' && array[i + 4] == ':')
							{
								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.ValtozoTipus;
								i += 4;
								if(end > i + 1)
									++i;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								--i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;

					} // End of switch (c).

					break;

				default: // Should never happen
				case SajatTokenTypes.Alap:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						default:
						{
							if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;
						}
					} // End of switch (c).

					break;

				case SajatTokenTypes.KapcsosZarojel:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '/':
							if(i + 1 < end && array[i + 1] == '/')
							{
								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);
								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Komment;
								break;
							}

						default:
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(end > i + 4 && (array[i] == 'S' || array[i] == 's') && array[i + 1] == 'z'
									&& array[i + 2] == 'á' && array[i + 3] == 'm' && array[i + 4] == ':')
							{
								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.ValtozoTipus;
								i += 4;
								if(end > i + 1)
									++i;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								--i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;
						}
					} // End of switch (c).

					break;
				case SajatTokenTypes.Zarojel:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}
						default:
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{

								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;
						}
					} // End of switch (c).

					break;

				case SajatTokenTypes.Elvalaszto:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}

						default:
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;
						}
					} // End of switch (c).

					break;

				case SajatTokenTypes.Pontosvesszo:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}

						default:
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(end > i + 4 && (array[i] == 'S' || array[i] == 's') && array[i + 1] == 'z'
									&& array[i + 2] == 'á' && array[i + 3] == 'm' && array[i + 4] == ':')
							{
								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.ValtozoTipus;
								i += 4;
								if(end > i + 1)
									++i;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								--i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								currentTokenType = SajatTokenTypes.Szam;
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;
						}
					} // End of switch (c).

					break;

				case SajatTokenTypes.Szam:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}
						default:

							if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								break; // Still a literal number.
							}
							else if(RSyntaxUtilities.isLetter(c))
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}
							// Otherwise, remember this was a number and start over.
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							i--;
							currentTokenType = SajatTokenTypes.NULL;

					} // End of switch (c).
					break;

				case SajatTokenTypes.ValtozoTipus:

					switch(c)
					{
						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						default:

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}

					} // End of switch (c).
					break;

				case SajatTokenTypes.Operator:

					switch(c)
					{

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Terkoz;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szoveg;
							break;
						case ',':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Elvalaszto;
							break;
						}
						case ';':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Pontosvesszo;
							break;
						}
						case '(':
						case ')':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Zarojel;
							break;
						}
						case '{':
						case '}':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.KapcsosZarojel;
							break;
						}
						case '*':
						case '/':
						case '%':
						case '+':
						case '-':
						case '<':
						case '>':
						case '=':
						case '!':
						case '&':
						case '|':
						{
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							if(i + 1 < end && array[i] == '/' && array[i + 1] == '/')
							{
								currentTokenType = SajatTokenTypes.Komment;
							}
							else
							{
								currentTokenType = SajatTokenTypes.Operator;
							}
							break;
						}

						default:
							addToken(text, currentTokenStart, i - 1, currentTokenType,
									newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = SajatTokenTypes.Szam;

							if(TokenValtozonevTeszt(array, i, end, valtozonevek) > -1)
							{

								currentTokenStart = i;
								currentTokenType = SajatTokenTypes.Valtozo;
								i += TokenValtozonevTeszt(array, i, end, valtozonevek) - 1;

								addToken(text, currentTokenStart, i - 1, currentTokenType,
										newStartOffset + currentTokenStart);//Mostani token mentése
								currentTokenStart = i;
								break;
							}
							else if(RSyntaxUtilities.isDigit(c) || c == '.')
							{
								break;
							}
							else if(RSyntaxUtilities.isLetter(c) || c == '_')
							{
								currentTokenType = SajatTokenTypes.Alap;
								break;
							}

							// Anything not currently handled - mark as identifier
							currentTokenType = SajatTokenTypes.Alap;
							break;

					} // End of switch (c).

					break;

				case SajatTokenTypes.Komment:
					i = end - 1;
					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
					// We need to set token type to null so at the bottom we don't add one more token.
					currentTokenType = SajatTokenTypes.NULL;
					break;

				case SajatTokenTypes.Szoveg:
					if(c == '"')
					{
						if(!(i - 1 >= offset && array[i - 1] == '\\'))
						{
							addToken(text, currentTokenStart, i, SajatTokenTypes.Szoveg,
									newStartOffset + currentTokenStart);
							currentTokenType = SajatTokenTypes.NULL;
						}
					}
					break;

			} // End of switch (currentTokenType).

		} // End of for (int i=offset; i<end; i++).

		switch(currentTokenType)
		{

			// Remember what token type to begin the next line with.
			case SajatTokenTypes.Szoveg:
				addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
				break;

			// Do nothing if everything was okay.
			case SajatTokenTypes.NULL:
				addNullToken();
				break;

			// All other token types don't continue to the next line...
			default:
				addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
				addNullToken();

		}

		// Return the first token in our linked list.
		return firstToken;

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//		resetTokenList();
		//
		//char[] tomb = text.array;
		//		int offset = text.offset;
		//		int count = text.count;
		//		int end = offset + count;
		//
		//		// See, when we find a token, its starting position is always of the form:
		//		// 'startOffset + (currentTokenStart-offset)'; but since startOffset and
		//		// offset are constant, tokens' starting positions become:
		//		// 'newStartOffset+currentTokenStart' for one less subtraction operation.
		//		int newStartOffset = startOffset - offset;

		//		currentTokenStart = offset;
		//		currentTokenType = startTokenType;
		//		currentTokenType = SajatTokenTypes.Alap;
		//		int i;
		//		for(i = offset; i < end; i++)
		//		{
		//
		//			if(i + 1 < end && tomb[i] == '/' && tomb[i + 1] == '/')
		//			{
		//				if(currentTokenType != SajatTokenTypes.Komment)
		//				{
		//					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
		//					currentTokenStart = i;
		//					currentTokenType = SajatTokenTypes.Komment;
		//					//i = end;
		//					break;//kiugrás a sor végére
		//				}
		//			}
		//			else if(tomb[i] == '/')
		//			{
		//				if(currentTokenType != SajatTokenTypes.Operator)
		//				{
		//					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
		//					currentTokenStart = i;
		//					currentTokenType = SajatTokenTypes.Operator;
		//				}
		//			}
		//			else if(tomb[i] == ' ' || tomb[i] == '\t')
		//			{
		//				if(currentTokenType != SajatTokenTypes.Terkoz)
		//				{
		//					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
		//					currentTokenStart = i;
		//					currentTokenType = SajatTokenTypes.Terkoz;
		//				}
		//
		//			}
		//			else
		//			{
		//				if(currentTokenType != SajatTokenTypes.Alap)
		//				{
		//					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
		//					currentTokenStart = i;
		//					currentTokenType = SajatTokenTypes.Alap;
		//				}
		//
		//			}
		//		}
		//		if(i < end || offset == end)
		//		{
		//			addToken(text, i, end, SajatTokenTypes.Alap, newStartOffset + currentTokenStart);
		//		}
		//		//		//beginning:
		//		//		for(int i = offset; i < end; i++)
		//		//		{
		//		//			char c = tomb[i];
		//		//
		//		//			switch(currentTokenType)
		//		//			{
		//		//				case SajatTokenTypes.NULL:
		//		//				{
		//		//					switch(c)
		//		//					{
		//		//						case ' ':
		//		//						case '\t':
		//		//						{
		//		//							currentTokenType = SajatTokenTypes.Terkoz;
		//		//							currentTokenStart = i;
		//		//							break;
		//		//						}
		//		//						case '/':
		//		//						{
		//		//							if(i + 1 < end && tomb[i + 1] == '/')
		//		//							{
		//		//								currentTokenType = SajatTokenTypes.Komment;
		//		//								currentTokenStart = i;
		//		//
		//		//								i = end;//hogy kilépjünk a sor végére
		//		//							}
		//		//							else
		//		//							{
		//		//								currentTokenType = SajatTokenTypes.Alap;
		//		//								currentTokenStart = i;
		//		//							}
		//		//							break;
		//		//						}
		//		//						default:
		//		//						{
		//		//							currentTokenType = SajatTokenTypes.Alap;
		//		//							break;
		//		//						}
		//		//					}
		//		//					break;
		//		//				}
		//		//				case SajatTokenTypes.Terkoz:
		//		//				{
		//		//					switch(c)
		//		//					{
		//		//						case ' ':
		//		//						case '\t':
		//		//						{
		//		//							break;//továbbra is térköz
		//		//						}
		//		//						case '/':
		//		//						{
		//		//							addToken(text, currentTokenStart, i, currentTokenType, offset);
		//		//
		//		//							if(i + 1 < end && tomb[i + 1] == '/')
		//		//							{
		//		//								currentTokenType = SajatTokenTypes.Komment;
		//		//								currentTokenStart = i;
		//		//
		//		//								i = end;//hogy kilépjünk a sor végére
		//		//							}
		//		//							else
		//		//							{
		//		//								addToken(text, i, i + 1, SajatTokenTypes.Operator, offset);
		//		//
		//		//								if(i + 1 < end)
		//		//									++i;
		//		//
		//		//								currentTokenType = SajatTokenTypes.Alap;
		//		//								currentTokenStart = i;
		//		//							}
		//		//							break;
		//		//						}
		//		//						default:
		//		//						{
		//		//							addToken(text, currentTokenStart, i, currentTokenType, offset);
		//		//							currentTokenType = SajatTokenTypes.Alap;
		//		//							//currentTokenStart = i;
		//		//							break;
		//		//						}
		//		//					}
		//		//					break;
		//		//				}
		//		//			}
		//		//		}
		//		//		addToken(text, currentTokenStart, end - 1, currentTokenType, offset);
		//		//		currentTokenType = SajatTokenTypes.NULL;
		//		return firstToken;
	}

	int TokenValtozonevTeszt(char[] array, int i, int end, List<String> valtozonevek)
	{
		for(String valtnev : valtozonevek)
		{
			boolean egyezik = true;
			for(int x = 0; x < valtnev.length(); ++x)
			{
				if(i + x >= end)
				{
					egyezik = false;
					break;
				}
				if(array[i + x] != valtnev.charAt(x))
				{
					egyezik = false;
					break;
				}
			}
			if(egyezik)
			{
				if(i + valtnev.length() < end && !RSyntaxUtilities.isLetterOrDigit(array[i + valtnev.length()]))
				{
					return valtnev.length();
				}
				else if(i + valtnev.length() >= end)
				{
					return valtnev.length();
				}
			}
		}
		return -1;
	}

	List<String> GetValtozoNevek()//TODO: IDE A KÓDFELDOLGOZÁSHOZ MINDIG UGYAN AZ A METÓDUS KELL, MINT AMI A SZERVER OLDALI KÓDFELDOLGOZÓBAN VAN!!!
	{
		List<String> Valtozonevek = new ArrayList<String>();
		try
		{
			String kod = FoPackage.FoClass.FolySzerkPeldany.textAreaKod.getText();

			List<Character> KodList = new ArrayList<Character>();

			KodList.add('{');//Hogy indexelésnél az egyel kisebb indexbõl ne legyen Exception

			for(int i = 0; i < kod.length(); ++i)
			{
				KodList.add(kod.charAt(i));
			}

			boolean Stringbenvan = false;

			for(int i = 0; i < KodList.size(); ++i)
			{
				if(KodList.size() > i + 7)
				{
					if(KodList.get(i) == '<' && KodList.get(i + 1) == 'f' && KodList.get(i + 2) == 'e'
							&& KodList.get(i + 3) == 'j' && KodList.get(i + 4) == 'l' && KodList.get(i + 5) == 'é'
							&& KodList.get(i + 6) == 'c' && KodList.get(i + 7) == '>')
					{
						while(KodList.get(i) != ';')
						{
							KodList.remove(i);
						}
						KodList.remove(i);
					}
				}

				if(KodList.get(i) == '"')
				{

					if(Stringbenvan)
					{
						if(KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
						else
						{}
					}
					else
					{
						Stringbenvan = true;
					}
				}

				if(!Stringbenvan)
				{
					if(KodList.size() > i + 1)
					{
						if(KodList.get(i) == '/' && KodList.get(i + 1) == '/')
						{
							while(KodList.size() > i && KodList.get(i) != '\n')
							{
								KodList.remove(i);
							}
						}
					}
					if(KodList.size() > i && (KodList.get(i) == ' ' || KodList.get(i) == '\t' || KodList.get(i) == '\n'
							|| KodList.get(i) == '\r'))
					{
						KodList.remove(i);
						--i;
					}
				}
			}

			boolean StringbenVan = false;
			for(int i = 0; i < KodList.size(); ++i)
			{
				if(KodList.get(i) == '"')
				{
					if(i == 0)
					{
						StringbenVan = true;
					}
					else
					{
						if(KodList.get(i - 1) != '\\')
						{
							StringbenVan = !StringbenVan;
						}
					}
				}
				if(StringbenVan)
					continue;
				// !<<Szám:>>!!!////////////////////////////////////////////////////////////////////////////////////////////( szám:név1,név2,név3; )
				if(KodList.size() > i + 4)
				{
					if((KodList.get(i) == 'S' || KodList.get(i) == 's') && KodList.get(i + 1) == 'z'
							&& KodList.get(i + 2) == 'á' && KodList.get(i + 3) == 'm' && KodList.get(i + 4) == ':')
					{
						boolean ervenyes = false;
						if(i != 0)
						{
							if(KodList.get(i - 1) == ' ' || KodList.get(i - 1) == '{' || KodList.get(i - 1) == '}'
									|| KodList.get(i - 1) == ';')
							{
								ervenyes = true;
							}
						}
						else
						{
							ervenyes = true;
						}

						if(ervenyes)
						{
							i += 5;

							String Parameter = "";

							while(KodList.get(i) != ';')
							{
								Parameter += KodList.get(i);
								++i;
							}

							String[] nevek = Parameter.split(",");
							
							String nev = "";
							for(String item : nevek)
							{
								if(item.contains("="))
								{
									try
									{
										String[] par = item.split("=");
										nev = par[0];
									}
									catch (Exception e)
									{}
								}
								else
								{
									nev = item;
								}
								Valtozonevek.add(nev);
							}
						}
					}
				}
				// !<<Ismételd()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
				if(KodList.size() > i + 7)
				{
					if(KodList.get(i) == 'I' && KodList.get(i + 1) == 's' && KodList.get(i + 2) == 'm'
							&& KodList.get(i + 3) == 'é' && KodList.get(i + 4) == 't' && KodList.get(i + 5) == 'e'
							&& KodList.get(i + 6) == 'l' && KodList.get(i + 7) == 'd')
					{
						i += 8;
						if(KodList.get(i) == '(')
						{
							FoPackage.Epar buff = FoPackage.Eszkozok.FgvHivasbolParamKiszedo(KodList, i);
							String parameter = buff.EString;
							i = buff.EInt;
							++i;

							List<String> parameterek = FoPackage.Eszkozok.ParameterSzetvalaszto(parameter);
							if(parameterek.size() >= 2)
							{
								Valtozonevek.add(parameterek.get(1));
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return Valtozonevek;

	}

	//@Override
	//	public Token getTokenList(Segment text, int startTokenType, final int startOffset)
	//	{
	//
	//		resetTokenList();
	//
	//		char[] array = text.array;
	//		int offset = text.offset;
	//		int count = text.count;
	//		int end = offset + count;
	//
	//		// See, when we find a token, its starting position is always of the form:
	//		// 'startOffset + (currentTokenStart-offset)'; but since startOffset and
	//		// offset are constant, tokens' starting positions become:
	//		// 'newStartOffset+currentTokenStart' for one less subtraction operation.
	//		int newStartOffset = startOffset - offset;
	//
	//		currentTokenStart = offset;
	//		currentTokenType = startTokenType;
	//
	//		//beginning:
	//		for(int i = offset; i < end; i++)
	//		{
	//
	//			char c = array[i];
	//
	//			switch(currentTokenType)
	//			{
	//
	//				case SajatTokenTypes.NULL:
	//
	//					currentTokenStart = i; // Starting a new token here.
	//
	//					switch(c)
	//					{
	//
	//						case ' ':
	//						case '\t':
	//							currentTokenType = SajatTokenTypes.WHITESPACE;
	//							break;
	//
	//						case '"':
	//							currentTokenType = SajatTokenTypes.ERROR_STRING_DOUBLE;
	//							break;
	//
	//						case '%':
	//							currentTokenType = SajatTokenTypes.VARIABLE;
	//							break;
	//
	//						// The "separators".
	//						case '(':
	//						case ')':
	//						case '{':
	//						case '}':
	//							addToken(text, currentTokenStart, i, SajatTokenTypes.SEPARATOR,
	//									newStartOffset + currentTokenStart);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						// The "separators2".
	//						case ',': case ';':
	//						case ';':
	//							addToken(text, currentTokenStart, i, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						// Newer version of EOL comments, or a label
	//						case '/':
	//							// If this will be the first token added, it is
	//							// a new-style comment or a label
	//							if(firstToken == null)
	//							{
	//								if(i < end - 1 && array[i + 1] == '/')
	//								{ // new-style comment
	//									currentTokenType = SajatTokenTypes.Komment;
	//								}
	//								else if(i != offset + 1)
	//								{ // Label
	//									currentTokenType = SajatTokenTypes.Operator;
	//								}
	//							}
	//							else
	//							{ // Just a colon
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//							}
	//							break;
	//
	//						default:
	//
	//							// Just to speed things up a tad, as this will usually be the case (if spaces above failed).
	//							if(RSyntaxUtilities.isLetterOrDigit(c) || c == '\\')
	//							{
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//								break;
	//							}
	//
	//							int indexOf = operators.indexOf(c, 0);
	//							if(indexOf > -1)
	//							{
	//								addToken(text, currentTokenStart, i, SajatTokenTypes.Operator,
	//										newStartOffset + currentTokenStart);
	//								currentTokenType = SajatTokenTypes.NULL;
	//								break;
	//							}
	//							else
	//							{
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//								break;
	//							}
	//
	//					} // End of switch (c).
	//
	//					break;
	//
	//				case SajatTokenTypes.WHITESPACE:
	//
	//					switch(c)
	//					{
	//
	//						case ' ':
	//						case '\t':
	//							break; // Still whitespace.
	//
	//						case '"':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							currentTokenType = SajatTokenTypes.ERROR_STRING_DOUBLE;
	//							break;
	//
	//						case '%':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							currentTokenType = SajatTokenTypes.VARIABLE;
	//							break;
	//
	//						// The "separators".
	//						case '(':
	//						case ')':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							addToken(text, i, i, SajatTokenTypes.SEPARATOR, newStartOffset + i);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						// The "separators2".
	//						case ',': case ';':
	//						case ';':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							addToken(text, i, i, SajatTokenTypes.Azonosito, newStartOffset + i);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						// Newer version of EOL comments, or a label
	//						case '/':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							// If the previous (whitespace) token was the first token
	//							// added, this is a new-style comment or a label
	//							if(firstToken.getNextToken() == null)
	//							{
	//								if(i < end - 1 && array[i + 1] == '/')
	//								{ // new-style comment
	//									currentTokenType = SajatTokenTypes.Komment;
	//								}
	//								else
	//								{ // Label
	//									currentTokenType = SajatTokenTypes.Operator;
	//								}
	//							}
	//							else
	//							{ // Just a colon
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//							}
	//							break;
	//
	//						default: // Add the whitespace token and start anew.
	//
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.WHITESPACE,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//
	//							// Just to speed things up a tad, as this will usually be the case (if spaces above failed).
	//							if(RSyntaxUtilities.isLetterOrDigit(c) || c == '\\')
	//							{
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//								break;
	//							}
	//
	//							int indexOf = operators.indexOf(c, 0);
	//							if(indexOf > -1)
	//							{
	//								addToken(text, currentTokenStart, i, SajatTokenTypes.Operator,
	//										newStartOffset + currentTokenStart);
	//								currentTokenType = SajatTokenTypes.NULL;
	//								break;
	//							}
	//							else
	//							{
	//								currentTokenType = SajatTokenTypes.Azonosito;
	//							}
	//
	//					} // End of switch (c).
	//
	//					break;
	//
	//				default: // Should never happen
	//				case SajatTokenTypes.Azonosito:
	//
	//					switch(c)
	//					{
	//
	//						case ' ':
	//						case '\t':
	//							// Check for REM comments.
	//							if(i - currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R')
	//									&& (array[i - 2] == 'e' || array[i - 2] == 'E')
	//									&& (array[i - 1] == 'm' || array[i - 1] == 'M'))
	//							{
	//								currentTokenType = SajatTokenTypes.Komment;
	//								break;
	//							}
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							currentTokenType = SajatTokenTypes.WHITESPACE;
	//							break;
	//
	//						case '"':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							currentTokenType = SajatTokenTypes.ERROR_STRING_DOUBLE;
	//							break;
	//
	//						case '%':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							currentTokenStart = i;
	//							currentTokenType = SajatTokenTypes.VARIABLE;
	//							break;
	//
	//						// Should be part of identifiers, but not at end of "REM".
	//						case '\\':
	//							// Check for REM comments.
	//							if(i - currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R')
	//									&& (array[i - 2] == 'e' || array[i - 2] == 'E')
	//									&& (array[i - 1] == 'm' || array[i - 1] == 'M'))
	//							{
	//								currentTokenType = SajatTokenTypes.Komment;
	//							}
	//							break;
	//
	//						case ',': case ';':
	//						case '_':
	//							break; // Characters good for identifiers.
	//
	//						// The "separators".
	//						case '(':
	//						case ')':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							addToken(text, i, i, SajatTokenTypes.SEPARATOR, newStartOffset + i);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						// The "separators2".
	//						case ',': case ';':
	//						case ';':
	//							addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//									newStartOffset + currentTokenStart);
	//							addToken(text, i, i, SajatTokenTypes.Azonosito, newStartOffset + i);
	//							currentTokenType = SajatTokenTypes.NULL;
	//							break;
	//
	//						default:
	//
	//							// Just to speed things up a tad, as this will usually be the case.
	//							if(RSyntaxUtilities.isLetterOrDigit(c) || c == '\\')
	//							{
	//								break;
	//							}
	//
	//							int indexOf = operators.indexOf(c);
	//							if(indexOf > -1)
	//							{
	//								addToken(text, currentTokenStart, i - 1, SajatTokenTypes.Azonosito,
	//										newStartOffset + currentTokenStart);
	//								addToken(text, i, i, SajatTokenTypes.Operator, newStartOffset + i);
	//								currentTokenType = SajatTokenTypes.NULL;
	//								break;
	//							}
	//
	//							// Otherwise, fall through and assume we're still okay as an IDENTIFIER...
	//
	//					} // End of switch (c).
	//
	//					break;
	//
	//				case SajatTokenTypes.Komment:
	//					i = end - 1;
	//					addToken(text, currentTokenStart, i, SajatTokenTypes.Komment, newStartOffset + currentTokenStart);
	//					// We need to set token type to null so at the bottom we don't add one more token.
	//					currentTokenType = SajatTokenTypes.NULL;
	//					break;
	//
	//				case SajatTokenTypes.PREPROCESSOR: // Used for labels
	//					i = end - 1;
	//					addToken(text, currentTokenStart, i, SajatTokenTypes.PREPROCESSOR,
	//							newStartOffset + currentTokenStart);
	//					// We need to set token type to null so at the bottom we don't add one more token.
	//					currentTokenType = SajatTokenTypes.NULL;
	//					break;
	//
	//				case SajatTokenTypes.ERROR_STRING_DOUBLE:
	//
	//					if(c == '"')
	//					{
	//						addToken(text, currentTokenStart, i, SajatTokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
	//								newStartOffset + currentTokenStart);
	//						currentTokenStart = i + 1;
	//						currentTokenType = SajatTokenTypes.NULL;
	//					}
	//					// Otherwise, we're still an unclosed string...
	//
	//					break;
	//
	//				case SajatTokenTypes.VARIABLE:
	//
	//					if(i == currentTokenStart + 1)
	//					{ // first character after '%'.
	//						varType = VariableType.NORMAL_VAR;
	//						switch(c)
	//						{
	//							case '{':
	//								varType = VariableType.BRACKET_VAR;
	//								break;
	//							case '~':
	//								varType = VariableType.TILDE_VAR;
	//								break;
	//							case '%':
	//								varType = VariableType.DOUBLE_PERCENT_VAR;
	//								break;
	//							default:
	//								if(RSyntaxUtilities.isLetter(c) || c == '_' || c == ' ')
	//								{ // No tab, just space; spaces are okay in variable names.
	//									break;
	//								}
	//								else if(RSyntaxUtilities.isDigit(c))
	//								{ // Single-digit command-line argument ("%1").
	//									addToken(text, currentTokenStart, i, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart);
	//									currentTokenType = SajatTokenTypes.NULL;
	//									break;
	//								}
	//								else
	//								{ // Anything else, ???.
	//									addToken(text, currentTokenStart, i - 1, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart); // ???
	//									i--;
	//									currentTokenType = SajatTokenTypes.NULL;
	//									break;
	//								}
	//						} // End of switch (c).
	//					}
	//					else
	//					{ // Character other than first after the '%'.
	//						switch(varType)
	//						{
	//							case BRACKET_VAR:
	//								if(c == '}')
	//								{
	//									addToken(text, currentTokenStart, i, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart);
	//									currentTokenType = SajatTokenTypes.NULL;
	//								}
	//								break;
	//							case TILDE_VAR:
	//								if(!RSyntaxUtilities.isLetterOrDigit(c))
	//								{
	//									addToken(text, currentTokenStart, i - 1, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart);
	//									i--;
	//									currentTokenType = SajatTokenTypes.NULL;
	//								}
	//								break;
	//							case DOUBLE_PERCENT_VAR:
	//								// Can be terminated with "%%", or (essentially) a space.
	//								// substring chars are valid
	//								if(c == '%')
	//								{
	//									if(i < end - 1 && array[i + 1] == '%')
	//									{
	//										i++;
	//										addToken(text, currentTokenStart, i, SajatTokenTypes.VARIABLE,
	//												newStartOffset + currentTokenStart);
	//										currentTokenType = SajatTokenTypes.NULL;
	//									}
	//								}
	//								else if(!RSyntaxUtilities.isLetterOrDigit(c) && c != ':' && c != '~' && c != ','
	//										&& c != '-')
	//								{
	//									addToken(text, currentTokenStart, i - 1, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart);
	//									currentTokenType = SajatTokenTypes.NULL;
	//									i--;
	//								}
	//								break;
	//							default:
	//								if(c == '%')
	//								{
	//									addToken(text, currentTokenStart, i, SajatTokenTypes.VARIABLE,
	//											newStartOffset + currentTokenStart);
	//									currentTokenType = SajatTokenTypes.NULL;
	//								}
	//								break;
	//						}
	//					}
	//					break;
	//
	//			} // End of switch (currentTokenType).
	//
	//		} // End of for (int i=offset; i<end; i++).
	//
	//		// Deal with the (possibly there) last token.
	//		if(currentTokenType != SajatTokenTypes.NULL)
	//		{
	//
	//			// Check for REM comments.
	//			if(end - currentTokenStart == 3 && (array[end - 3] == 'r' || array[end - 3] == 'R')
	//					&& (array[end - 2] == 'e' || array[end - 2] == 'E')
	//					&& (array[end - 1] == 'm' || array[end - 1] == 'M'))
	//			{
	//				currentTokenType = SajatTokenTypes.Komment;
	//			}
	//
	//			addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
	//		}
	//
	//		addNullToken();
	//
	//		// Return the first token in our linked list.
	//		return firstToken;
	//	}

}