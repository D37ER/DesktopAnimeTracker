package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controler.Controler;

public class SettingsPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = -8882511024011206401L;
	private JTextField tLocalPath, tSiteURL;
	private JButton bSetDefault, bApply, bCancel;
	private ActionListener applylistener;

	public SettingsPanel() {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout(5, 5));
		
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
		pCenter.setBackground(new Color(255, 255, 255));
		add(pCenter, BorderLayout.CENTER);
		
		JPanel pContent = new JPanel();
		pContent.setLayout(new GridLayout(2, 2, 5, 5));
		pContent.setBackground(new Color(255, 255, 255));
		pContent.setMinimumSize(new Dimension(10000, 60));
		pContent.setPreferredSize(new Dimension(10000, 60));
		pContent.setMaximumSize(new Dimension(10000, 60));
		pCenter.add(pContent);
		pCenter.add(Box.createVerticalGlue());
		
		JLabel lLocalPath = new JLabel("Local path", SwingConstants.RIGHT);
		lLocalPath.setFont(new Font("Inter", Font.PLAIN, 15));
		lLocalPath.setMinimumSize(new Dimension(150, 20));
		lLocalPath.setPreferredSize(new Dimension(150, 20));
		lLocalPath.setMaximumSize(new Dimension(150, 20));
		pContent.add(lLocalPath);
		
		tLocalPath = new JTextField(Controler.getLocalPath());
		tLocalPath.setFont(new Font("Inter", Font.PLAIN, 15));
		tLocalPath.setMinimumSize(new Dimension(150, 20));
		tLocalPath.setPreferredSize(new Dimension(150, 20));
		tLocalPath.setMaximumSize(new Dimension(150, 20));
		pContent.add(tLocalPath);
		
		JLabel lSiteURL = new JLabel("Site URL", SwingConstants.RIGHT);
		lSiteURL.setFont(new Font("Inter", Font.PLAIN, 15));
		lSiteURL.setMinimumSize(new Dimension(150, 20));
		lSiteURL.setPreferredSize(new Dimension(150, 20));
		lSiteURL.setMaximumSize(new Dimension(150, 20));
		pContent.add(lSiteURL);
		
		tSiteURL = new JTextField(Controler.getSiteURL());
		tSiteURL.setFont(new Font("Inter", Font.PLAIN, 15));
		tSiteURL.setMinimumSize(new Dimension(150, 20));
		tSiteURL.setPreferredSize(new Dimension(150, 20));
		tSiteURL.setMaximumSize(new Dimension(150, 20));
		pContent.add(tSiteURL);
		
		JPanel pBottomButtons = new JPanel();
		pBottomButtons.setLayout(new BoxLayout(pBottomButtons, BoxLayout.X_AXIS));
		pBottomButtons.setBackground(new Color(255, 255, 255));
		add(pBottomButtons, BorderLayout.SOUTH);
		pBottomButtons.add(Box.createHorizontalGlue());
		
		bSetDefault = new JButton("Set Default");
		bSetDefault.setFont(new Font("Inter", Font.PLAIN, 15));
		bSetDefault.setMinimumSize(new Dimension(115, 30));
		bSetDefault.setPreferredSize(new Dimension(115, 30));
		bSetDefault.setMaximumSize(new Dimension(115, 30));
		bSetDefault.setBackground(new Color(240, 240, 240));
		bSetDefault.setFocusable(false);
		bSetDefault.addActionListener(this);
		pBottomButtons.add(bSetDefault);
		pBottomButtons.add(Box.createRigidArea(new Dimension(5,5)));
		
		bApply = new JButton("Apply");
		bApply.setFont(new Font("Inter", Font.PLAIN, 15));
		bApply.setMinimumSize(new Dimension(115, 30));
		bApply.setPreferredSize(new Dimension(115, 30));
		bApply.setMaximumSize(new Dimension(115, 30));
		bApply.setBackground(new Color(240, 240, 240));
		bApply.setFocusable(false);
		bApply.addActionListener(this);
		pBottomButtons.add(bApply);
		pBottomButtons.add(Box.createRigidArea(new Dimension(5,5)));
		
		bCancel = new JButton("Cancel");
		bCancel.setFont(new Font("Inter", Font.PLAIN, 15));
		bCancel.setMinimumSize(new Dimension(115, 30));
		bCancel.setPreferredSize(new Dimension(115, 30));
		bCancel.setMaximumSize(new Dimension(115, 30));
		bCancel.setBackground(new Color(240, 240, 240));
		bCancel.setFocusable(false);
		bCancel.addActionListener(this);
		pBottomButtons.add(bCancel);
		pBottomButtons.add(Box.createRigidArea(new Dimension(5,5)));
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bSetDefault) {
			tLocalPath.setText(Controler.getLocalPathDefault());
			tSiteURL.setText(Controler.getSiteURLDefault());
		} else if(e.getSource() == bApply) {
			Controler.setLocalPath(tLocalPath.getText());
			Controler.setSiteURL(tSiteURL.getText());
			applylistener.actionPerformed(new ActionEvent(this, 0, ""));
		} else if(e.getSource() == bCancel) {
			cancel();
		}
	}
	
	public void cancel() {
		tLocalPath.setText(Controler.getLocalPath());
		tSiteURL.setText(Controler.getSiteURL());
	}
	
	
	public void setApplylistener(ActionListener applylistener) {
		this.applylistener = applylistener;
	}
}
