package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UiUtils {
	public static void setFontRecursive(JComponent c, Font font) {
		Arrays.stream(c.getComponents()).forEach(child -> {
			child.setFont(font);
			if (child instanceof JComponent) {
				setFontRecursive((JComponent) child, font);
			}
			if (child instanceof Container) {
				setFontRecursive((Container) child, font);
			}
		});
	}

	public static void setFontRecursive(Container c, Font font) {
		Arrays.stream(c.getComponents()).forEach(child -> {
			child.setFont(font);
			if (child instanceof JComponent) {
				setFontRecursive((JComponent) child, font);
			}
			if (child instanceof Container) {
				setFontRecursive((Container) child, font);
			}
		});
	}
}
