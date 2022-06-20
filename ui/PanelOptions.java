package ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class PanelOptions extends JPanel {

	@Serial
	private static final long serialVersionUID = -4763875452164030755L;
	private final DameFenetre fenetre;
	private final JButton replayBtn;

	public PanelOptions(DameFenetre fenetre) {
		super(new GridLayout(0, 1));
		
		this.fenetre = fenetre;
		
		PanelOptionsListner listner = new PanelOptionsListner();
		final String[] JoueurTypeOpts = {"Humain"};
		JComboBox<String> p1Combo = new JComboBox<>(JoueurTypeOpts);
		JComboBox<String> p2Combo = new JComboBox<>(JoueurTypeOpts);

		p1Combo.setEnabled(false);
		p2Combo.setEnabled(false);

		this.replayBtn = new JButton("Recommencer");

		this.replayBtn.addActionListener(listner);


		JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel milieu = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));


		JLabel p1label = new JLabel("(Blanc) Joueur 1: ");
		milieu.add(p1label);
		milieu.add(p1Combo);

		JLabel p2label = new JLabel("(Noir) Joueur 2: ");
		bas.add(p2label);
		bas.add(p2Combo);

		haut.add(replayBtn);


		this.add(haut);
		this.add(milieu);
		this.add(bas);

	}


	private class PanelOptionsListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();

			if (src == replayBtn) {
				fenetre.restart();
			}

		}
	}
}

