package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controler.Controler;
import controler.Tools;
import model.Anime;
import view.assets.Panel;
import view.assets.TextButton;

public class HomePanel extends Panel{
	private static final long serialVersionUID = 9132473904670547273L;
	private ArrayList<AnimePanel> animePanels;
	private AnimeListener animeListener;

	public HomePanel() {
		animePanels = new ArrayList<AnimePanel>();
		setBackground(new Color(255, 255, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		refreshAnimes();
		setElementsOrder();
	}
	
	public void refreshAnimes() {
		removeAll();
		ArrayList<Anime> animes = Controler.getAnimeList();
		for(Anime anime : animes)
			addAnime(anime);
	}
	
	public void setAnimeListener(AnimeListener animeListener) {
		this.animeListener = animeListener;
		for(AnimePanel animePanel : animePanels) {
			animePanel.setAnimeListener(animeListener);
		}
	}
	
	public void addAnime(Anime anime) {
		AnimePanel animePanel = new AnimePanel(anime);
		animePanel.setAnimeListener(animeListener);
		animePanels.add(animePanel);
		add(animePanel);
		add(Box.createRigidArea(new Dimension(5,5))); 
		revalidate();
		repaint();
	}

	public void deleteAnime(Anime anime) {
		for(AnimePanel animePanel : animePanels)
			if(animePanel.getAnime() == anime)
				for(int i=0; i<getComponentCount(); i++)
					if(getComponent(i) == animePanel) {
						remove(i);
						remove(i);
					}
		revalidate();
		repaint();
	}
	
	
	
	private void setElementsOrder() {
		ArrayList<TextButton> animeButtons = new ArrayList<TextButton>(animePanels.size());
		for(AnimePanel animePanel : animePanels)
			animeButtons.add(animePanel.getAnimeButton());
		for(int i=0; i<animeButtons.size(); i++) {
			if(i>0)
				animeButtons.get(i).setTopNeighbour(animeButtons.get(i-1));
			if(i<animeButtons.size()-1)
				animeButtons.get(i).setBottomNeighbour(animeButtons.get(i+1));
		}
	}
	
	@Override
	public void requestFocus() {
		if(!animePanels.isEmpty())
			animePanels.get(0).requestFocus();
	}
	
	@Override
	public void setNeighbours(Component topNeighbour, Component bottomNeighbour, Component leftNeighbour, Component rightNeighbour) {
		setTopNeighbour(topNeighbour);
		setBottomNeighbour(bottomNeighbour);
		setLeftNeighbour(leftNeighbour);
		setRightNeighbour(rightNeighbour);
	}
	
	@Override
	public void setTopNeighbour(Component c) {
		super.setTopNeighbour(c);
		if(!animePanels.isEmpty())
			animePanels.get(0).getAnimeButton().setTopNeighbour(c);
	}
	
	@Override
	public void setBottomNeighbour(Component c) {
		super.setBottomNeighbour(c);
		if(!animePanels.isEmpty())
			animePanels.get(animePanels.size()-1).getAnimeButton().setBottomNeighbour(c);
	}
	
	@Override
	public void setLeftNeighbour(Component c) {
		super.setLeftNeighbour(c);
		for(AnimePanel animePanel : animePanels)
			animePanel.getAnimeButton().setLeftNeighbour(c);
	}
	
	@Override
	public void setRightNeighbour(Component c) {
		super.setRightNeighbour(c);
		for(AnimePanel animePanel : animePanels)
			animePanel.getAnimeButton().setRightNeighbour(c);
	}
	
	@Override
	public Component getMostTopComponent() {
		if(!animePanels.isEmpty())
			return animePanels.get(0).getAnimeButton();
		return getBottomNeighbour();
	}

	@Override
	public Component getMostBottomComponent() {
		if(!animePanels.isEmpty())
			return animePanels.get(animePanels.size()-1).getAnimeButton();
		return getTopNeighbour();
	}

	@Override
	public Component getMostLeftComponent() {
		if(!animePanels.isEmpty())
			return animePanels.get(0).getAnimeButton();
		return getRightNeighbour();
	}

	@Override
	public Component getMostRightComponent() {
		if(!animePanels.isEmpty())
			return animePanels.get(0).getAnimeButton();
		return getLeftNeighbour();
	}
	
	public interface AnimeListener {
		public void animeChoosen(Anime anime);
	}
}

class AnimePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -7965727054177717481L;
	private Anime anime;
	private TextButton bAnimeName;
	private HomePanel.AnimeListener animeListener;
	
	public AnimePanel(Anime anime) {
		this.anime = anime;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setMaximumSize(new Dimension(10000, 40));
		setBackground(new Color(255, 255, 255));
		
		bAnimeName = new TextButton(Tools.beautify(anime.getName()));
		bAnimeName.setMinimumSize(new Dimension(40, 40));
		bAnimeName.setPreferredSize(new Dimension(10000, 40));
		bAnimeName.setMaximumSize(new Dimension(10000, 40));
		bAnimeName.setHorizontalAlignment(SwingConstants.LEFT);
		bAnimeName.setBackground(TextButton.GREY);
		bAnimeName.setMargin(new Insets(0,10,0,10));
		bAnimeName.addActionListener(this);
		add(bAnimeName);
	}
	
	public Anime getAnime() {
		return anime;
	}
	
	public void setAnimeListener(HomePanel.AnimeListener animeListener) {
		this.animeListener = animeListener;
	}
	
	public TextButton getAnimeButton() {
		return bAnimeName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(animeListener != null)
			animeListener.animeChoosen(anime);
	}
	
	@Override
	public void requestFocus() {
		bAnimeName.requestFocus();
	}
}