package view.assets;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class Panel extends JPanel implements ArrowsFocus{
	private static final long serialVersionUID = -8537438046793343538L;
	private Component topNeighbour, bottomNeighbour, leftNeighbour, rightNeighbour;

	public Panel() {
		super();
	}
	
	public Panel(LayoutManager layout) {
		super(layout);
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
}
