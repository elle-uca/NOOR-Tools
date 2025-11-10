package org.ln.noortools.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.formdev.flatlaf.ui.FlatButtonUI;

public class HoverButtonUI extends FlatButtonUI {

    protected HoverButtonUI(boolean shared) {
		super(shared);
	}

	private static final Color hoverLight = new Color(230, 230, 230);
    private static final Color hoverDark  = new Color(60, 60, 60);

    @Override
    protected void paintBackground(Graphics g, JComponent c) {
        if (!c.isOpaque())
            return;

        ButtonModel model = ((AbstractButton)c).getModel();
        Color bg = c.getBackground();

        if (model.isRollover()) {
            // auto-detect Light / Dark theme by brightness
            boolean dark = UIManager.getColor("Panel.background").getRed() < 128;
            bg = dark ? hoverDark : hoverLight;
        }

        g.setColor(bg);
        g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
    }
}
