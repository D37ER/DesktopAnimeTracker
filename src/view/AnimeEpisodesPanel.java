package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

import controler.CallbackByteChannel;
import controler.Controler;
import model.Anime;
import model.Episode;
import view.assets.Panel;
import view.assets.CheckBox;
import view.assets.IconButton;
import view.assets.TextButton;
import view.dialogs.PassLinkDialog;

public class AnimeEpisodesPanel extends Panel{
	private static final long serialVersionUID = -7020922392757332608L;
	private JFrame frame;
	private Anime anime;
	private JPanel spContent;
	private ArrayList<EpisodePanel> episodesPanels;
	
	public AnimeEpisodesPanel(JFrame frame, Anime anime) {
		super();
		this.frame = frame;
		this.anime = anime;
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout(0,0));
		
		JPanel pHeaders = new JPanel();
		pHeaders.setLayout(new BoxLayout(pHeaders, BoxLayout.X_AXIS));
		pHeaders.setOpaque(true);
		pHeaders.setBackground(new Color(0, 0, 0, 0));
		pHeaders.setMaximumSize(new Dimension(10000, 30));
		add(pHeaders, BorderLayout.NORTH);
		pHeaders.add(Box.createRigidArea(new Dimension(25,5))); 
		
		JLabel lEpisode = new JLabel("Episode");
		lEpisode.setFont(new Font("Inter", Font.PLAIN, 15));
		lEpisode.setForeground(new Color(120, 120, 120));
		lEpisode.setPreferredSize(new Dimension(200, 30));
		pHeaders.add(lEpisode);
		pHeaders.add(Box.createHorizontalGlue());
		
		JLabel lWatched = new JLabel("Watched", SwingConstants.CENTER);
		lWatched.setFont(new Font("Inter", Font.PLAIN, 15));
		lWatched.setForeground(new Color(120, 120, 120));
		lWatched.setMinimumSize(new Dimension(84, 30));
		lWatched.setPreferredSize(new Dimension(84, 30));
		lWatched.setMaximumSize(new Dimension(84, 30));
		pHeaders.add(lWatched);
		
		JLabel lSUB = new JLabel("SUB", SwingConstants.CENTER);
		lSUB.setFont(new Font("Inter", Font.PLAIN, 15));
		lSUB.setForeground(new Color(120, 120, 120));
		lSUB.setMinimumSize(new Dimension(150, 30));
		lSUB.setPreferredSize(new Dimension(150, 30));
		lSUB.setMaximumSize(new Dimension(150, 30));
		pHeaders.add(lSUB);
		pHeaders.add(Box.createRigidArea(new Dimension(5,5)));
		
		JLabel lDUB = new JLabel("DUB", SwingConstants.CENTER);
		lDUB.setFont(new Font("Inter", Font.PLAIN, 15));
		lDUB.setForeground(new Color(120, 120, 120));
		lDUB.setMinimumSize(new Dimension(150, 30));
		lDUB.setPreferredSize(new Dimension(150, 30));
		lDUB.setMaximumSize(new Dimension(150, 30));
		pHeaders.add(lDUB);
		pHeaders.add(Box.createRigidArea(new Dimension(5,5)));
		
		spContent = new JPanel();
		JScrollPane sp = new JScrollPane(spContent);
		sp.setBorder(null);
		sp.getVerticalScrollBar().setUnitIncrement(16);
		add(sp, BorderLayout.CENTER);
		spContent.setBackground(new Color(255,255,255));
		spContent.setLayout(new BoxLayout(spContent, BoxLayout.Y_AXIS));
		
		episodesPanels = new ArrayList<EpisodePanel>();
		Thread checkEpisodesThred = new Thread(new Runnable() {
			public void run() {
				checkEpisodes();
			}
		});
		checkEpisodesThred.start();
	}
	
	public Anime getAnime() {
		return anime;
	}
	
	private void checkEpisodes() {
		for (int episodeNumber = 1; episodeNumber <= 40; episodeNumber++) {
			EpisodePanel episodePanel = new EpisodePanel(frame);
			episodePanel.setNeighbours(getTopNeighbour(), getBottomNeighbour(), getLeftNeighbour(), getRightNeighbour());
			if(episodesPanels.size()>0) {
				episodePanel.setTopNeighbour(episodesPanels.get(episodesPanels.size()-1));
				episodesPanels.get(episodesPanels.size()-1).setBottomNeighbour(episodePanel);
			}
			episodesPanels.add(episodePanel);
			spContent.add(episodePanel);
			spContent.add(Box.createRigidArea(new Dimension(5,5)));
			revalidate();
			repaint();
			
			Episode subEpisode = new Episode(anime, Episode.SUB, episodeNumber);
			Controler.checkEpisodeStatus(subEpisode);
			if(subEpisode.getStatus() <= Episode.NOT_EXIST) {
				spContent.remove(spContent.getComponentCount()-1);
				spContent.remove(spContent.getComponentCount()-1);
				revalidate();
				repaint();
				break;
			}
			episodePanel.setSubEpisode(subEpisode);
		}
		for (int episodeNumber = 1; episodeNumber <= 40; episodeNumber++) {
			Episode dubEpisode = new Episode(anime, Episode.DUB, episodeNumber);
			Controler.checkEpisodeStatus(dubEpisode);
			if(dubEpisode.getStatus() <= Episode.NOT_EXIST)
				break;
			episodesPanels.get(episodeNumber-1).setDubEpisode(dubEpisode);
		}
	}

	public void refresh() {
		episodesPanels.clear();
		spContent.removeAll();
		revalidate();
		repaint();
		Thread checkEpisodesThred = new Thread(new Runnable() {
			public void run() {
				checkEpisodes();
			}
		});
		checkEpisodesThred.start();
	}
	
	public void abort() {
		for(EpisodePanel episodePanel : episodesPanels) {
			episodePanel.abort();
		}
	}
	
	@Override
	public void requestFocus() {
		if(!episodesPanels.isEmpty())
			episodesPanels.get(0).requestFocus();
	}
	
	@Override
	public void setTopNeighbour(Component c) {
		super.setTopNeighbour(c);
		if(!episodesPanels.isEmpty())
			episodesPanels.get(0).setTopNeighbour(c);
	}
	
	@Override
	public void setBottomNeighbour(Component c) {
		super.setBottomNeighbour(c);
		if(!episodesPanels.isEmpty())
			episodesPanels.get(episodesPanels.size()-1).setBottomNeighbour(c);
	}
	
	@Override
	public void setLeftNeighbour(Component c) {
		super.setLeftNeighbour(c);
		for(EpisodePanel episodePanel : episodesPanels)
			episodePanel.setLeftNeighbour(c);
	}
	
	@Override
	public void setRightNeighbour(Component c) {
		super.setRightNeighbour(c);
		for(EpisodePanel episodePanel : episodesPanels)
			episodePanel.setRightNeighbour(c);
	}
	
	@Override
	public Component getMostTopComponent() {
		if(!episodesPanels.isEmpty())
			return episodesPanels.get(0).getMostTopComponent();
		return getBottomNeighbour();
	}

	@Override
	public Component getMostBottomComponent() {
		if(!episodesPanels.isEmpty())
			return episodesPanels.get(episodesPanels.size()-1).getMostBottomComponent();
		return getTopNeighbour();
	}

	@Override
	public Component getMostLeftComponent() {
		if(!episodesPanels.isEmpty())
			return episodesPanels.get(0).getMostLeftComponent();
		return getRightNeighbour();
	}

	@Override
	public Component getMostRightComponent() {
		if(!episodesPanels.isEmpty())
			return episodesPanels.get(0).getMostRightComponent();
		return getLeftNeighbour();
	}
}






class EpisodePanel extends Panel implements ActionListener {
	private static final long serialVersionUID = -2850779357965679671L;
	private JFrame frame;
	private Episode subEpisode;
	private JLabel lEpisode;
	private CheckBox cbWatched;
	private DownloadButton subButton;
	private DownloadButton dubButton;
	
	public EpisodePanel(JFrame frame) {
		super();
		this.frame = frame;
		subEpisode = null;
		subButton = null;
		dubButton = null;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(new Color(240, 240, 240));
		setMaximumSize(new Dimension(10000, 40));
		
		add(Box.createRigidArea(new Dimension(10,5)));
		lEpisode = new JLabel("Episode XXX");
		lEpisode.setFont(new Font("Inter", Font.PLAIN, 15));
		lEpisode.setPreferredSize(new Dimension(100, 30));
		add(lEpisode);
		add(Box.createHorizontalGlue());
		cbWatched = new CheckBox();
		cbWatched.setSizes(24, 24);
		cbWatched.setFontSize(15);
		cbWatched.setNeighbours(getTopNeighbour(), getBottomNeighbour(), getLeftNeighbour(), getRightNeighbour());
		cbWatched.addActionListener(this);
		add(cbWatched);
		add(Box.createRigidArea(new Dimension(30,5)));
		add(Box.createRigidArea(new Dimension(310,5)));
	}
	
	public void setSubEpisode(Episode episode) {
		subEpisode = episode;
		lEpisode.setText(String.format("Episode %03d", episode.getNumber()));
		subButton = new DownloadButton(frame, episode);
		subButton.setNeighbours(getTopNeighbour(), getBottomNeighbour(), cbWatched, getRightNeighbour());
		cbWatched.setRightNeighbour(subButton);
		if(dubButton != null) {
			subButton.setRightNeighbour(dubButton);
			dubButton.setLeftNeighbour(subButton);
		}
		setBackground(subEpisode.isWatched() ? new Color(240, 240, 240) : new Color(240, 220, 240));
		cbWatched.setText(subEpisode.isWatched() ? "✔" : "");
		remove(getComponentCount()-1);
		add(subButton);
		add(Box.createRigidArea(new Dimension(5,5)));
		add(Box.createRigidArea(new Dimension(155,5)));
		revalidate();
		repaint();
	}
	
	public void setDubEpisode(Episode episode) {
		dubButton = new DownloadButton(frame, episode);
		dubButton.setNeighbours(getTopNeighbour(), getBottomNeighbour(), cbWatched, getRightNeighbour());
		if(subButton != null) {
			dubButton.setLeftNeighbour(subButton);
			subButton.setRightNeighbour(dubButton);
		}
		remove(getComponentCount()-1);
		add(dubButton);
		add(Box.createRigidArea(new Dimension(5,5)));
		revalidate();
		repaint();
	}
	
	public void abort() {
		if(subButton != null)
			subButton.abort();
		if(dubButton != null)
			dubButton.abort();
	}

	@Override
	public void requestFocus() {
		if(subButton != null)
			subButton.requestFocus();
		else if(dubButton != null)
			dubButton.requestFocus();
		else
			cbWatched.requestFocus();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		subEpisode.setWatched(!subEpisode.isWatched());
		Controler.episodeWatched(subEpisode, subEpisode.isWatched());
		setBackground(subEpisode.isWatched() ? new Color(240, 240, 240) : new Color(240, 220, 240));
		cbWatched.setText(subEpisode.isWatched() ? "✔" : "");
	}
	
	@Override
	public void setTopNeighbour(Component c) {
		super.setTopNeighbour(c);
		cbWatched.setTopNeighbour(c);
		if(subButton != null)
			subButton.setTopNeighbour(c);
		if(dubButton != null)
			dubButton.setTopNeighbour(c);
	}
	
	@Override
	public void setBottomNeighbour(Component c) {
		super.setBottomNeighbour(c);
		cbWatched.setBottomNeighbour(c);
		if(subButton != null)
			subButton.setBottomNeighbour(c);
		if(dubButton != null)
			dubButton.setBottomNeighbour(c);
	}
	
	@Override
	public void setLeftNeighbour(Component c) {
		super.setLeftNeighbour(c);
		cbWatched.setLeftNeighbour(c);
	}
	
	@Override
	public void setRightNeighbour(Component c) {
		super.setRightNeighbour(c);
		if(dubButton != null)
			dubButton.setRightNeighbour(c);
		else if(subButton != null)
			subButton.setRightNeighbour(c);
		else
			cbWatched.setRightNeighbour(c);
	}
	
	@Override
	public Component getMostTopComponent() {
		if(subButton != null)
			return subButton.getMostTopComponent();
		if(dubButton != null)
			return dubButton.getMostTopComponent();
		return cbWatched;
	}

	@Override
	public Component getMostBottomComponent() {
		if(subButton != null)
			return subButton.getMostBottomComponent();
		if(dubButton != null)
			return dubButton.getMostBottomComponent();
		return cbWatched;
	}

	@Override
	public Component getMostLeftComponent() {
		return cbWatched;
	}

	@Override
	public Component getMostRightComponent() {
		if(dubButton != null)
			return dubButton.getMostRightComponent();
		if(subButton != null)
			return subButton.getMostRightComponent();
		return cbWatched;
	}
}






class DownloadButton extends Panel implements MouseListener, ActionListener, Controler.ProgressListener {
	private static final long serialVersionUID = -4403319938892522279L;
	private JFrame frame;
	private Episode episode;
	private JLabel lNotAvaible, lAborted;
	private TextButton bDownload, bPlay;
	private IconButton bDelete;
	private JProgressBar pbDownloading;
	private PassLinkDialog passLinkDialog;
	private Thread downloadThread;
	
	public DownloadButton(JFrame frame, Episode episode) {
		this.frame = frame;
		this.episode = episode;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setMinimumSize(new Dimension(150, 40));
		setPreferredSize(new Dimension(150, 40));
		setMaximumSize(new Dimension(150, 40));
		setOpaque(true);
		setBackground(new Color(0, 0, 0, 0));
		
		bDownload = new TextButton("Download");
		bDownload.setSizes(150, 30);
		bDownload.addActionListener(this);
		bDownload.setNeighbours(getTopNeighbour(), getBottomNeighbour(), getLeftNeighbour(), getRightNeighbour());
		
		pbDownloading = new JProgressBar();
		pbDownloading.setValue(100);
		pbDownloading.setStringPainted(true);
		pbDownloading.setFont(new Font("Inter", Font.PLAIN, 15));
		pbDownloading.setMinimumSize(new Dimension(115, 30));
		pbDownloading.setPreferredSize(new Dimension(115, 30));
		pbDownloading.setMaximumSize(new Dimension(115, 30));
		pbDownloading.setBackground(new Color(255, 255, 255));
		pbDownloading.setForeground(new Color(180, 180, 255));
		pbDownloading.setString("loading...");
		pbDownloading.addMouseListener(this);
		pbDownloading.setUI(new BasicProgressBarUI() {
		      protected Color getSelectionBackground() { return Color.black; }
		      protected Color getSelectionForeground() { return Color.black; }
		});
		
		bPlay = new TextButton("Play");
		bPlay.setSizes(115, 30);
		bPlay.setBackground(TextButton.GREEN);
		bPlay.addActionListener(this);
		bPlay.setNeighbours(getTopNeighbour(), getBottomNeighbour(), getLeftNeighbour(), null);
		
		bDelete = new IconButton("🗑");
		bDelete.setSizes(30, 30);
		bDelete.setBackground(TextButton.RED);
		bDelete.addActionListener(this);
		bPlay.setNeighbours(getTopNeighbour(), getBottomNeighbour(), bPlay, getRightNeighbour());
		bPlay.setRightNeighbour(bDelete);
		
		lNotAvaible = new JLabel("Not Avaible", SwingConstants.CENTER);
		lNotAvaible.setFont(new Font("Inter", Font.PLAIN, 15));
		lNotAvaible.setMinimumSize(new Dimension(150, 30));
		lNotAvaible.setPreferredSize(new Dimension(150, 30));
		lNotAvaible.setMaximumSize(new Dimension(150, 30));
		
		lAborted = new JLabel("Aborted", SwingConstants.CENTER);
		lAborted.setFont(new Font("Inter", Font.PLAIN, 15));
		lAborted.setMinimumSize(new Dimension(115, 30));
		lAborted.setPreferredSize(new Dimension(115, 30));
		lAborted.setMaximumSize(new Dimension(115, 30));
		
		refreshStatus();
	}
	
	public void refreshStatus() {
		removeAll();
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			add(lNotAvaible);
			break;
		case Episode.AVAIBLE:
			add(bDownload);
			break;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			add(pbDownloading);
			add(Box.createRigidArea(new Dimension(5,5)));
			add(bDelete);
			bDelete.setLeftNeighbour(pbDownloading);
			break;
		case Episode.ABORT:
			add(lAborted);
			add(Box.createRigidArea(new Dimension(5,5)));
			add(bDelete);
			bDelete.setLeftNeighbour(getLeftNeighbour());
			break;
		case Episode.IN_FILES:
			add(bPlay);
			add(Box.createRigidArea(new Dimension(5,5)));
			add(bDelete);
			bDelete.setLeftNeighbour(bPlay);
			break;
		}
		revalidate();
		repaint();
		if(getParent() != null) {
			getParent().revalidate();
			getParent().repaint();
		}
	}
	
	public void abort() {
		if(downloadThread != null) {
			downloadThread.interrupt();
			try {downloadThread.join();} catch (InterruptedException e1) {e1.printStackTrace();}
		}
	}
	
	@Override
	public void requestFocus() {
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			break;
		case Episode.AVAIBLE:
			bDownload.requestFocus();
			break;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			pbDownloading.requestFocus();
			break;
		case Episode.ABORT:
			bDelete.requestFocus();
			break;
		case Episode.IN_FILES:
			bPlay.requestFocus();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == passLinkDialog) {
			refreshStatus();
			downloadThread = new Thread(new Runnable() {
				public void run() 
				{
					Controler.downloadEpisode(episode, DownloadButton.this);
					refreshStatus();
				}
			});
			downloadThread.start();
		}
		else if(e.getSource() == bDownload) {
			passLinkDialog = new PassLinkDialog(frame, episode);
			passLinkDialog.setConfirmListener(this);
			passLinkDialog.setVisible(true);
		}
		else if(e.getSource() == bPlay)
			Controler.playEpisode(episode);
		else if(e.getSource() == bDelete) {
			if(downloadThread != null) {
				downloadThread.interrupt();
				try {downloadThread.join();} catch (InterruptedException e1) {e1.printStackTrace();}
			}
			Controler.deleteEpisode(episode);
			refreshStatus();
			Thread checkEpisodeThred = new Thread(new Runnable() {
				public void run() {
					Controler.checkEpisodeStatus(episode);
					refreshStatus();
				}
			});
			checkEpisodeThred.start();
		}
	}
	
	@Override
	public void progressChanged(CallbackByteChannel rbc, Episode episode, int progress, int eta) {
		String text;
		if(eta < 120)
			text = "%.1f%% %ds";
		else {
			eta /= 60;
			eta += 1;
			text = "%.1f%% %dmin";
		}
		pbDownloading.setValue(progress/10);
		pbDownloading.setString(String.format(text, (float)(progress/10.0), eta));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Controler.playEpisode(episode);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pbDownloading.setBackground(new Color(240, 240, 240));
		pbDownloading.setForeground(new Color(170, 170, 245));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pbDownloading.setBackground(new Color(255, 255, 255));
		pbDownloading.setForeground(new Color(180, 180, 255));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pbDownloading.setBackground(new Color(255, 255, 255));
		pbDownloading.setForeground(new Color(180, 180, 255));
	}

	@Override
	public void setTopNeighbour(Component c) {
		super.setTopNeighbour(c);
		bDownload.setTopNeighbour(c);
		bDelete.setTopNeighbour(c);
		bPlay.setTopNeighbour(c);
	}
	
	@Override
	public void setBottomNeighbour(Component c) {
		super.setBottomNeighbour(c);
		bDownload.setBottomNeighbour(c);
		bDelete.setBottomNeighbour(c);
		bPlay.setBottomNeighbour(c);
	}
	
	@Override
	public void setLeftNeighbour(Component c) {
		super.setLeftNeighbour(c);
		bDownload.setLeftNeighbour(c);
		bPlay.setLeftNeighbour(c);
	}
	
	@Override
	public void setRightNeighbour(Component c) {
		super.setRightNeighbour(c);
		bDownload.setRightNeighbour(c);
		bDelete.setRightNeighbour(c);
	}
	
	@Override
	public Component getMostTopComponent() {
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			return getBottomNeighbour();
		case Episode.AVAIBLE:
			return bDownload;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			return pbDownloading;
		case Episode.ABORT:
			return bDelete;
		case Episode.IN_FILES:
			return bPlay;
		}
		return getBottomNeighbour();
	}

	@Override
	public Component getMostBottomComponent() {
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			return getTopNeighbour();
		case Episode.AVAIBLE:
			return bDownload;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			return pbDownloading;
		case Episode.ABORT:
			return bDelete;
		case Episode.IN_FILES:
			return bPlay;
		}
		return getTopNeighbour();
	}

	@Override
	public Component getMostLeftComponent() {
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			return getRightNeighbour();
		case Episode.AVAIBLE:
			return bDownload;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			return pbDownloading;
		case Episode.ABORT:
			return bDelete;
		case Episode.IN_FILES:
			return bPlay;
		}
		return getRightNeighbour();
	}

	@Override
	public Component getMostRightComponent() {
		switch(episode.getStatus()) {
		case Episode.ERROR:
		case Episode.UNKNOWN:
		case Episode.DELETED:
		case Episode.NOT_EXIST:
			return getLeftNeighbour();
		case Episode.AVAIBLE:
			return bDownload;
		case Episode.FILE_LINK_PASSED:
		case Episode.DOWNLOADING:
			return bDelete;
		case Episode.ABORT:
			return bDelete;
		case Episode.IN_FILES:
			return bDelete;
		}
		return getLeftNeighbour();
	}
}

