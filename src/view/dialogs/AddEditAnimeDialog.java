package view.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddEditAnimeDialog extends JDialog implements ActionListener, DocumentListener{
	private static final long serialVersionUID = 7291520474298056678L;
	public static final int ADD = 0, EDIT = 1;
	private JButton bOK, bCancel;
	private JTextArea tAnimeName, tAnimeDubName;
	private boolean resultToSave;
	
	public AddEditAnimeDialog(JFrame parent, int type, String animeName, String animeDubName){
		super(parent, (type == ADD ? "Add" : "Edit") +" anime", true);
		resultToSave = false;
		setSize(400, 200);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(parent);
		
		JLabel lAnimeName = new JLabel("Anime name :");
		lAnimeName.setBounds(10, 10, 360, 20);
		add(lAnimeName);
		
		tAnimeName = new JTextArea(animeName);
		tAnimeName.setBounds(10, 35, 360, 20);
		tAnimeName.getDocument().addDocumentListener(this);
		tAnimeName.setEnabled(type == ADD);
		add(tAnimeName);
		
		JLabel lAnimeDubName = new JLabel("Anime dub name :");
		lAnimeDubName.setBounds(10, 70, 360, 20);
		add(lAnimeDubName);
		
		tAnimeDubName = new JTextArea(animeDubName);
		tAnimeDubName.setBounds(10, 95, 360, 20);
		add(tAnimeDubName);
		
		bOK = new JButton(type == ADD ? "Add" : "Edit");
		bOK.setBounds(150, 130, 100, 25);
		bOK.setBackground(new Color(255,255,255));
		bOK.setFocusable(false);
		bOK.addActionListener(this);
		add(bOK);
		getRootPane().setDefaultButton(bOK);
		
		bCancel = new JButton("Cancel");
		bCancel.setBounds(270, 130, 100, 25);
		bCancel.setBackground(new Color(255,255,255));
		bCancel.setFocusable(false);
		bCancel.addActionListener(this);
		add(bCancel);
	}
	
	public String getAnimeName() {
		return tAnimeName.getText();
	}
	
	public String getAnimeDubName() {
		return tAnimeDubName.getText();
	}
	
	public boolean isResultToSave() {
		return resultToSave;
	}
	
	private void updateDubName()
	{
		if(tAnimeName.getText().isEmpty())
			tAnimeDubName.setText("");
		else
			tAnimeDubName.setText(tAnimeName.getText() + "-dub");
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bOK) {
			if(!tAnimeName.getText().isEmpty())
				resultToSave = true;
			dispose();
		} else if(e.getSource() == bCancel)
			dispose();
	}

	public void insertUpdate(DocumentEvent e) {
		updateDubName();
	}

	public void removeUpdate(DocumentEvent e) {
		updateDubName();
	}

	public void changedUpdate(DocumentEvent e) {
		updateDubName();
	}
}
