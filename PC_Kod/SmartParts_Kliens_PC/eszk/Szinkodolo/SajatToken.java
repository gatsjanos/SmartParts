package Szinkodolo;

import java.awt.Rectangle;
import javax.swing.text.TabExpander;
import org.fife.ui.rsyntaxtextarea.Token;

public interface SajatToken extends Token
{
	int getListOffset(SajatRSyntaxTextArea textArea, TabExpander e,
			float x0, float x);
	
	float getWidth(SajatRSyntaxTextArea textArea, TabExpander e, float x0);
	
	float getWidthUpTo(int numChars, SajatRSyntaxTextArea textArea,
			TabExpander e, float x0);
	
	Rectangle listOffsetToView(SajatRSyntaxTextArea textArea, TabExpander e,
			int pos, int x0, Rectangle rect);
}
