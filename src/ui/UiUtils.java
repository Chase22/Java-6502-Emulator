package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class UiUtils {
	public static void applyToAllComponentsRecursively(Container c, Consumer<JComponent> block) {
		Arrays.stream(c.getComponents()).forEach(child -> {
			if (child instanceof JComponent) {
				block.accept((JComponent) child);
			}
			if (child instanceof Container) {
				applyToAllComponentsRecursively((Container) child, block);
			}
		});
	}

	public static void setFontRecursive(Container c, Font font) {
		applyToAllComponentsRecursively(c, (comp) -> comp.setFont(font));
	}

	public static void setBackgroundColorRecursively(Container c, Color color) {
		applyToAllComponentsRecursively(c, (comp) -> comp.setBackground(color));
	}

	public static void setForegroundColorRecursively(Container c, Color color) {
		applyToAllComponentsRecursively(c, (comp) -> comp.setForeground(color));
	}
}
