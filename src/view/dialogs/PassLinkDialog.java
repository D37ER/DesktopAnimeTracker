package view.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import controler.Controler;
import model.Episode;

public class PassLinkDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = -8001655860598031727L;
	private Episode episode;
	private JButton bOpen, bOK, bCancel;
	private JTextArea taLink;
	private ActionListener listener;

	public PassLinkDialog(JFrame parent, Episode episode){
		super(parent, episode.getAnime().getName() + " ep " + episode.getNumber(), true);
		this.episode = episode;

		setSize(400, 300);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(parent);
		bOpen = new JButton("Open download site in browser");
		bOpen.setBounds(10, 10, 360, 40);
		bOpen.setBackground(new Color(255,255,255));
		bOpen.setFocusable(false);
		bOpen.addActionListener(this);
		add(bOpen);
		JLabel lPasteBelow = new JLabel("Paste link below :");
		lPasteBelow.setBounds(10, 50, 360, 40);
		add(lPasteBelow);
		taLink = new JTextArea();
		taLink.setBounds(10, 90, 360, 120);
		taLink.setLineWrap(true);
		add(taLink);
		bOK = new JButton("OK");
		bOK.setBounds(150, 220, 100, 25);
		bOK.setBackground(new Color(255,255,255));
		bOK.setFocusable(false);
		bOK.addActionListener(this);
		add(bOK);
		getRootPane().setDefaultButton(bOK);
		bCancel = new JButton("Cancel");
		bCancel.setBounds(270, 220, 100, 25);
		bCancel.setBackground(new Color(255,255,255));
		bCancel.setFocusable(false);
		bCancel.addActionListener(this);
		add(bCancel);
	}

	public void setConfirmListener(ActionListener listener) {
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bOpen)
			Controler.openDownloadSite(episode);
		if(e.getSource() == bOK) {
			try {
				episode.setFileLink(new URL(taLink.getText()));
				episode.setStatus(Episode.FILE_LINK_PASSED);
				dispose();
				listener.actionPerformed(new ActionEvent(this, 0, "confirm"));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == bCancel)
			dispose();
	}
}
