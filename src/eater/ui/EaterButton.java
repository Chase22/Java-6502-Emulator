package eater.ui;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Simple wrapper class for {@link JButton} that allows to set an {@link ActionListener} on construction
 */
public class EaterButton extends JButton {
	public EaterButton(ActionListener action) {
		this(null, null, action);
	}

	public EaterButton(Icon icon, ActionListener action) {
		this(null, icon, action);
	}

	public EaterButton(String text, ActionListener action) {
		this(text, null, action);
	}

	public EaterButton(String text, Icon icon, ActionListener action) {
		super(text, icon);
		addActionListener(action);
		setBorderPainted(false);
	}
}
