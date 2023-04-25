package view.assets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.KeyStroke;

public class TextButton extends JButton implements FocusListener, ArrowsFocus{
	private static final long serialVersionUID = 8459059177730419604L;
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color GREY = new Color(240, 240, 240);
	public static final Color RED = new Color(255, 180, 180);
	public static final Color GREEN = new Color(180, 255, 180);
	public static final Color BLUE = new Color(180, 180, 255);
	private Color background;
	private Component topNeighbour, bottomNeighbour, leftNeighbour, rightNeighbour;
	
	public TextButton(String text) {
		super(text);
		this.background = WHITE;
		super.setBackground(background);
		setFont(new Font("Inter", Font.PLAIN, 15));
		setMargin(new Insets(0,0,0,0));
		setMinimumSize(new Dimension(150, 30));
		setPreferredSize(new Dimension(150, 30));
		setMaximumSize(new Dimension(150, 30));
		getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), getInputMap().get(KeyStroke.getKeyStroke("pressed SPACE")));
		getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), getInputMap().get(KeyStroke.getKeyStroke("released SPACE")));
		getInputMap().put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "none");
		addFocusListener(this);
		setFocusPainted(false);
	}
	
	public void setSizes(int width, int height) {
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
	}
	
	public void setFontSize(int size) {
		setFont(new Font("Inter", Font.PLAIN, size));
	}
	
	@Override
	public void setBackground(Color color) {
		background = color;
		super.setBackground(background);
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.setBackground(BLUE);
	}

	@Override
	public void focusLost(FocusEvent e) {
		super.setBackground(background);
	}

	@Override
	public void setNeighbours(Component topNeighbour, Component bottomNeighbour, Component leftNeighbour, Component rightNeighbour) {
		setTopNeighbour(topNeighbour);
		setBottomNeighbour(bottomNeighbour);
		setLeftNeighbour(leftNeighbour);
		setRightNeighbour(rightNeighbour);
	}
	
	@Override
	public Component getTopNeighbour() {
		return topNeighbour;
	}

	@Override
	public void setTopNeighbour(Component topNeighbour) {
		this.topNeighbour = topNeighbour;
	}

	@Override
	public Component getBottomNeighbour() {
		return bottomNeighbour;
	}

	@Override
	public void setBottomNeighbour(Component bottomNeighbour) {
		this.bottomNeighbour = bottomNeighbour;
	}

	@Override
	public Component getLeftNeighbour() {
		return leftNeighbour;
	}

	@Override
	public void setLeftNeighbour(Component leftNeighbour) {
		this.leftNeighbour = leftNeighbour;
	}

	@Override
	public Component getRightNeighbour() {
		return rightNeighbour;
	}

	@Override
	public void setRightNeighbour(Component rightNeighbour) {
		this.rightNeighbour = rightNeighbour;
	}

	@Override
	public Component getMostTopComponent() {
		return this;
	}

	@Override
	public Component getMostBottomComponent() {
		return this;
	}

	@Override
	public Component getMostLeftComponent() {
		return this;
	}

	@Override
	public Component getMostRightComponent() {
		return this;
	}
	
	@Override
	public String toString() {
		return getText();
	}
}