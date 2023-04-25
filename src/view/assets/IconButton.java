package view.assets;

public class IconButton extends TextButton {
	private static final long serialVersionUID = 702820800328472441L;

	public IconButton(String icon) {
		super(icon);
		setSizes(30, 30);
		setFontSize(25);
	}
}