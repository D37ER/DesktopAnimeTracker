package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controler.Controler;
import controler.Tools;
import model.Anime;
import view.assets.ArrowsFocus;
import view.assets.IconButton;
import view.assets.TextButton;
import view.dialogs.AddEditAnimeDialog;

public class MainFrame extends JFrame implements ActionListener, HomePanel.AnimeListener, KeyEventDispatcher{
	private static final long serialVersionUID = -3707705450374935164L;
	private JPanel pLeftMenu, pTopMenu, pContent;
	private HomePanel homePanel;
	private SettingsPanel settingsPanel;
	private AnimeEpisodesPanel animeEpisodesPanel;
	private JLabel lLocation;
	private LeftMenuButton bHome, bSettings;
	private TopMenuButton bAddAnime, bEditAnime, bDeleteAnime;
	
	public MainFrame() {
		super("Anime Tracker");
		Controler.readSettings();
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0, 0));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setFocusable(true);
		requestFocus();
		
		pLeftMenu = new JPanel(new FlowLayout());
		pLeftMenu.setPreferredSize(new Dimension(150, 0));
		pLeftMenu.setBackground(new Color(200, 200, 200));
		add(pLeftMenu, BorderLayout.WEST);
		
		bHome = new LeftMenuButton("HOME");
		bHome.addActionListener(this);
		pLeftMenu.add(bHome);
		
		bSettings = new LeftMenuButton("SETTINGS");
		bSettings.addActionListener(this);
		pLeftMenu.add(bSettings);
		
		JPanel pLeftMenuFill = new JPanel(new BorderLayout(0,0));
		add(pLeftMenuFill, BorderLayout.CENTER);
		
		pTopMenu = new JPanel();
		pTopMenu.setLayout(new BoxLayout(pTopMenu, BoxLayout.X_AXIS));
		pTopMenu.setPreferredSize(new Dimension(0, 50));
		pTopMenu.setBackground(new Color(230, 230, 230));
		pTopMenu.setAlignmentY(CENTER_ALIGNMENT);
		pTopMenu.setBorder(new EmptyBorder(0, 10, 0, 0));
		pLeftMenuFill.add(pTopMenu, BorderLayout.NORTH);
		
		lLocation = new JLabel("Anime List");
		lLocation.setPreferredSize(new Dimension(400, 40));
		lLocation.setFont(new Font("Inter", Font.BOLD, 20));
		pTopMenu.add(lLocation);
		pTopMenu.add(Box.createHorizontalGlue());
		
		bAddAnime = new TopMenuButton("+");
		bAddAnime.addActionListener(this);
		pTopMenu.add(bAddAnime);
		bEditAnime = new TopMenuButton("✎");
		bEditAnime.addActionListener(this);
		bEditAnime.setVisible(false);
		pTopMenu.add(bEditAnime);
		pTopMenu.add(Box.createRigidArea(new Dimension(5,5))); 
		bDeleteAnime = new TopMenuButton("🗑");
		bDeleteAnime.addActionListener(this);
		bDeleteAnime.setVisible(false);
		pTopMenu.add(bDeleteAnime);
		pTopMenu.add(Box.createRigidArea(new Dimension(10,5))); 
		
		pContent = new JPanel(new BorderLayout(0,0));
		pContent.setBorder(new EmptyBorder(10, 10, 10, 10));
		pContent.setBackground(new Color(255, 255, 255));
		pLeftMenuFill.add(pContent, BorderLayout.CENTER);
		
		homePanel = new HomePanel();
		homePanel.setAnimeListener(this);
		pContent.add(homePanel, BorderLayout.CENTER);
		
		settingsPanel = new SettingsPanel();
		settingsPanel.setApplylistener(this);
		
		bHome.setNeighbours(null, bSettings, null, homePanel);
		bSettings.setNeighbours(bHome, null, null, homePanel);
		bAddAnime.setNeighbours(null, homePanel, homePanel, null);
		homePanel.setNeighbours(bAddAnime, null, bHome, bAddAnime);
		bEditAnime.setRightNeighbour(bDeleteAnime);
		bDeleteAnime.setLeftNeighbour(bEditAnime);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bHome) {
			if(animeEpisodesPanel != null)
				animeEpisodesPanel.abort();
			settingsPanel.cancel();
			bHome.setRightNeighbour(homePanel);
			bSettings.setRightNeighbour(homePanel);
			bAddAnime.setVisible(true);
			bEditAnime.setVisible(false);
			bDeleteAnime.setVisible(false);
			pContent.removeAll();
			lLocation.setText("Anime List");
			pContent.add(homePanel, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == bSettings) {
			if(animeEpisodesPanel != null)
				animeEpisodesPanel.abort();
			bAddAnime.setVisible(false);
			bEditAnime.setVisible(false);
			bDeleteAnime.setVisible(false);
			pContent.removeAll();
			lLocation.setText("Settings");
			pContent.add(settingsPanel, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == bAddAnime) {
			AddEditAnimeDialog addEditAnimeDialog = new AddEditAnimeDialog(this, AddEditAnimeDialog.ADD, "", "");
			addEditAnimeDialog.setVisible(true);
			String animeName = addEditAnimeDialog.getAnimeName();
			String animeDubName = addEditAnimeDialog.getAnimeDubName();
			boolean resultToSave = addEditAnimeDialog.isResultToSave();
			if(resultToSave) {
				Anime anime = new Anime(animeName, animeDubName);
				Controler.addAnime(anime);
				homePanel.addAnime(anime);
			}
		}
		else if(e.getSource() == bEditAnime) {
			if(animeEpisodesPanel != null)
				animeEpisodesPanel.abort();
			Anime anime = animeEpisodesPanel.getAnime();
			AddEditAnimeDialog addEditAnimeDialog = new AddEditAnimeDialog(this, AddEditAnimeDialog.EDIT, anime.getName(), anime.getDubName());
			addEditAnimeDialog.setVisible(true);
			String animeDubName = addEditAnimeDialog.getAnimeDubName();
			boolean resultToSave = addEditAnimeDialog.isResultToSave();
			if(resultToSave) {
				anime.setDubName(animeDubName);
				Controler.editAnime(anime);
				animeEpisodesPanel.refresh();
			}
		}
		else if(e.getSource() == bDeleteAnime) {
			if(animeEpisodesPanel != null)
				animeEpisodesPanel.abort();
			Anime anime = animeEpisodesPanel.getAnime();
			Controler.deleteAnime(anime);
			homePanel.deleteAnime(anime);
			actionPerformed(new ActionEvent(bHome, 0, ""));
		}
		else if(e.getSource() == settingsPanel) {
			homePanel.refreshAnimes();
		}
	}

	public void animeChoosen(Anime anime) {
		bAddAnime.setVisible(false);
		bEditAnime.setVisible(true);
		bDeleteAnime.setVisible(true);
		pContent.removeAll();
		lLocation.setText(Tools.beautify(anime.getName()));
		animeEpisodesPanel = new AnimeEpisodesPanel(this, anime);
		pContent.add(animeEpisodesPanel, BorderLayout.CENTER);
		bHome.setRightNeighbour(animeEpisodesPanel);
		bSettings.setRightNeighbour(animeEpisodesPanel);
		bEditAnime.setBottomNeighbour(animeEpisodesPanel);
		bEditAnime.setLeftNeighbour(animeEpisodesPanel);
		bDeleteAnime.setBottomNeighbour(animeEpisodesPanel);
		animeEpisodesPanel.setNeighbours(null, bEditAnime, bHome, bEditAnime);
		revalidate();
		repaint();
	}

	public boolean dispatchKeyEvent(KeyEvent e) {
        if (
        		e.getID() == KeyEvent.KEY_RELEASED 
				&&(
        				e.getKeyCode() == KeyEvent.VK_UP || 
        				e.getKeyCode() == KeyEvent.VK_DOWN ||
        				e.getKeyCode() == KeyEvent.VK_LEFT || 
        				e.getKeyCode() == KeyEvent.VK_RIGHT
        		)
        	) 
        {
	    	try {
	        	if(getFocusOwner() == this) {
	        		homePanel.requestFocus();
	        		animeEpisodesPanel.requestFocus();
	        		settingsPanel.requestFocus();
	        	}
	        	ArrowsFocus inFocus = (ArrowsFocus) getFocusOwner();
	    		switch(e.getKeyCode()) {
	    		case KeyEvent.VK_UP:
					ArrowsFocus upNeighbour = (ArrowsFocus) inFocus.getTopNeighbour();
					upNeighbour.getMostBottomComponent().requestFocus();
	    			break;
				case KeyEvent.VK_DOWN:
					ArrowsFocus downNeighbour = (ArrowsFocus) inFocus.getBottomNeighbour();
					downNeighbour.getMostTopComponent().requestFocus();
	    			break;
				case KeyEvent.VK_LEFT:
					ArrowsFocus leftNeighbour = (ArrowsFocus) inFocus.getLeftNeighbour();
					leftNeighbour.getMostRightComponent().requestFocus();
					break;
				case KeyEvent.VK_RIGHT:
					ArrowsFocus rightNeighbour = (ArrowsFocus) inFocus.getRightNeighbour();
					rightNeighbour.getMostLeftComponent().requestFocus();
	    		}
	    	}catch(Exception exc ) {};
        }
        return false;
    }
}

class LeftMenuButton extends TextButton {
	private static final long serialVersionUID = 7154873367876134791L;

	public LeftMenuButton(String text) {
		super(text);
		setSizes(140, 40);
	}
}
class TopMenuButton extends IconButton {
	private static final long serialVersionUID = 3282349222534048182L;

	public TopMenuButton(String icon) {
		super(icon);
		setSizes(40, 40);
	}
}
