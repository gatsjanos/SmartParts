package Szinkodolo;

import java.awt.Container;
import java.awt.Rectangle;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class SajatRSyntaxTextArea extends RSyntaxTextArea
{
	protected void doBracketMatching() {

		// We always need to repaint the "matched bracket" highlight if it
		// exists.
		if (match!=null) {
			repaint(match);
			if (dotRect!=null) {
				repaint(dotRect);
			}
		}

		// If a matching bracket is found, get its bounds and paint it!
		int lastCaretBracketPos = bracketInfo==null ? -1 : bracketInfo.x;
		bracketInfo = SajatRSyntaxUtilities.getMatchingBracketPosition(this,
				bracketInfo);
		if (bracketInfo.y>-1 &&
				(bracketInfo.y!=lastBracketMatchPos ||
				 bracketInfo.x!=lastCaretBracketPos)) {
			try {
				match = modelToView(bracketInfo.y);
				if (match!=null) { // Happens if we're not yet visible
					if (getPaintMatchedBracketPair()) {
						dotRect = modelToView(bracketInfo.x);
					}
					else {
						dotRect = null;
					}
					if (getAnimateBracketMatching()) {
						bracketRepaintTimer.restart();
					}
					repaint(match);
					if (dotRect!=null) {
						repaint(dotRect);
					}

					if (getShowMatchedBracketPopup()) {
						Container parent = getParent();
						if (parent instanceof JViewport) {
							Rectangle visibleRect = this.getVisibleRect();
							if (match.y + match.height < visibleRect.getY()) {
								if (matchedBracketPopupTimer == null) {
									matchedBracketPopupTimer =
											new MatchedBracketPopupTimer();
								}
								matchedBracketPopupTimer.restart(bracketInfo.y);
							}
						}
					}

				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Shouldn't happen.
			}
		}
		else if (bracketInfo.y==-1) {
			// Set match to null so the old value isn't still repainted.
			match = null;
			dotRect = null;
			bracketRepaintTimer.stop();
		}
		lastBracketMatchPos = bracketInfo.y;

	}

}