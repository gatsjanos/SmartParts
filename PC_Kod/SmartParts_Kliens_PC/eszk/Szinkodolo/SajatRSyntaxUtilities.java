package Szinkodolo;

/*
 * 08/06/2004
 *
 * RSyntaxUtilities.java - Utility methods used by RSyntaxTextArea and its
 * views.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */

import java.awt.Point;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;

/**
 * Utility methods used by <code>RSyntaxTextArea</code> and its associated
 * classes.
 *
 * @author Robert Futrell
 * @version 0.2
 */
public class SajatRSyntaxUtilities extends RSyntaxUtilities
{

	private SajatRSyntaxUtilities()
	{
	}
	public static Point getMatchingBracketPosition(SajatRSyntaxTextArea textArea, Point input)
	{

		if(input == null)
		{
			input = new Point();
		}
		input.setLocation(-1, -1);

		try
		{

			// Actually position just BEFORE caret.
			int caretPosition = textArea.getCaretPosition() - 1;
			RSyntaxDocument doc = (RSyntaxDocument) textArea.getDocument();
			char bracket = 0;

			// If the caret was at offset 0, we can't check "to its left."
			if(caretPosition >= 0)
			{
				bracket = doc.charAt(caretPosition);
			}

			// Try to match a bracket "to the right" of the caret if one
			// was not found on the left.
			int index = BRACKETS.indexOf(bracket);
			if(index == -1 && caretPosition < doc.getLength() - 1)
			{
				bracket = doc.charAt(++caretPosition);
			}

			// First, see if the char was a bracket (one of "{[()]}").
			if(index == -1)
			{
				index = BRACKETS.indexOf(bracket);
				if(index == -1)
				{
					return input;
				}
			}

			// If it was, then make sure this bracket isn't sitting in
			// the middle of a comment or string.  If it isn't, then
			// initialize some stuff so we can continue on.
			char bracketMatch;
			boolean goForward;
			Element map = doc.getDefaultRootElement();
			int curLine = map.getElementIndex(caretPosition);
			Element line = map.getElement(curLine);
			int start = line.getStartOffset();
			int end = line.getEndOffset();
			Token token = doc.getTokenListForLine(curLine);
			token = RSyntaxUtilities.getTokenAtOffset(token, caretPosition);
			// All brackets are always returned as "separators."
			if(token.getType() != SajatTokenTypes.Zarojel && token.getType() != SajatTokenTypes.KapcsosZarojel)
			{
				return input;
			}
			int languageIndex = token.getLanguageIndex();
			if(index < 3)
			{ // One of "{[("
				goForward = true;
				bracketMatch = BRACKETS.charAt(index + 3);
			}
			else
			{ // One of ")]}"
				goForward = false;
				bracketMatch = BRACKETS.charAt(index - 3);
			}

			if(goForward)
			{

				int lastLine = map.getElementCount();

				// Start just after the found bracket since we're sure
				// we're not in a comment.
				start = caretPosition + 1;
				int numEmbedded = 0;
				boolean haveTokenList = false;

				while(true)
				{

					doc.getText(start, end - start, charSegment);
					int segOffset = charSegment.offset;

					for(int i = segOffset; i < segOffset + charSegment.count; i++)
					{

						char ch = charSegment.array[i];

						if(ch == bracket)
						{
							if(!haveTokenList)
							{
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i - segOffset);
							token = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if((token.getType() == SajatTokenTypes.Zarojel || token.getType() == SajatTokenTypes.KapcsosZarojel) && token.getLanguageIndex() == languageIndex)
							{
								numEmbedded++;
							}
						}

						else if(ch == bracketMatch)
						{
							if(!haveTokenList)
							{
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i - segOffset);
							token = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if((token.getType() == SajatTokenTypes.Zarojel || token.getType() == SajatTokenTypes.KapcsosZarojel) && token.getLanguageIndex() == languageIndex)
							{
								if(numEmbedded == 0)
								{
									if(textArea.isCodeFoldingEnabled()
											&& textArea.getFoldManager().isLineHidden(curLine))
									{
										return input; // Match hidden in a fold
									}
									input.setLocation(caretPosition, offset);
									return input;
								}
								numEmbedded--;
							}
						}

					} // End of for (int i=segOffset; i<segOffset+charSegment.count; i++).

					// Bail out if we've gone through all lines and
					// haven't found the match.
					if(++curLine == lastLine)
					{
						return input;
					}

					// Otherwise, go through the next line.
					haveTokenList = false;
					line = map.getElement(curLine);
					start = line.getStartOffset();
					end = line.getEndOffset();

				} // End of while (true).

			} // End of if (goForward).

			// Otherwise, we're going backward through the file
			// (since we found '}', ')' or ']').
			else
			{ // goForward==false

				// End just before the found bracket since we're sure
				// we're not in a comment.
				end = caretPosition;// - 1;
				int numEmbedded = 0;
				boolean haveTokenList = false;
				Token t2;

				while(true)
				{

					doc.getText(start, end - start, charSegment);
					int segOffset = charSegment.offset;
					int iStart = segOffset + charSegment.count - 1;

					for(int i = iStart; i >= segOffset; i--)
					{

						char ch = charSegment.array[i];

						if(ch == bracket)
						{
							if(!haveTokenList)
							{
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i - segOffset);
							t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if((t2.getType() == SajatTokenTypes.Zarojel || t2.getType() == SajatTokenTypes.KapcsosZarojel) && token.getLanguageIndex() == languageIndex)
							{
								numEmbedded++;
							}
						}

						else if(ch == bracketMatch)
						{
							if(!haveTokenList)
							{
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i - segOffset);
							t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if((t2.getType() == SajatTokenTypes.Zarojel || t2.getType() == SajatTokenTypes.KapcsosZarojel) && token.getLanguageIndex() == languageIndex)
							{
								if(numEmbedded == 0)
								{
									input.setLocation(caretPosition, offset);
									return input;
								}
								numEmbedded--;
							}
						}

					}

					// Bail out if we've gone through all lines and
					// haven't found the match.
					if(--curLine == -1)
					{
						return input;
					}

					// Otherwise, get ready for going through the
					// next line.
					haveTokenList = false;
					line = map.getElement(curLine);
					start = line.getStartOffset();
					end = line.getEndOffset();

				} // End of while (true).

			} // End of else.

		}
		catch (BadLocationException ble)
		{
			// Shouldn't ever happen.
			ble.printStackTrace();
		}

		// Something went wrong...
		return input;

	}
}