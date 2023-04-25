package view.assets;

import java.awt.Component;

public interface ArrowsFocus {
	public void setNeighbours(Component upElement, Component downElement, Component leftComponent, Component rightComponent);
	public Component getTopNeighbour();
	public void setTopNeighbour(Component upElement);
	public Component getBottomNeighbour();
	public void setBottomNeighbour(Component downElement);
	public Component getLeftNeighbour();
	public void setLeftNeighbour(Component leftElement);
	public Component getRightNeighbour();
	public void setRightNeighbour(Component rightElement);
	public Component getMostTopComponent();
	public Component getMostBottomComponent();
	public Component getMostLeftComponent();
	public Component getMostRightComponent();
}
