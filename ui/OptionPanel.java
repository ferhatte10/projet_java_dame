package ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.HumanPlayer;
import model.Player;

/**
 * The {@code OptionPanel} class provides a user interface component to control
 * options for the game of checkers being played in the window.
 */
public class OptionPanel extends JPanel {

	private static final long serialVersionUID = -4763875452164030755L;

	private DameFenetre window;
	
	private JButton restartBtn;
	
	private JComboBox<String> player1Opts;


	private JButton player1Btn;

	private JLabel statB;

	private JLabel statW;
	private JComboBox<String> player2Opts;


	private JButton player2Btn;
	

	public OptionPanel(DameFenetre window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components
		OptionListener ol = new OptionListener();
		final String[] playerTypeOpts = {"Humain"}; // on peut rajouter un mode computer ou jouer sur le reseau par exemple
		this.restartBtn = new JButton("Recommencer");
		this.player1Opts = new JComboBox<>(playerTypeOpts);
		this.player2Opts = new JComboBox<>(playerTypeOpts);
		this.restartBtn.addActionListener(ol);

		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

		
		// Add components to the layout
		top.add(restartBtn);
		middle.add(new JLabel("(Blanc) Joueur 1: "));
		middle.add(player1Opts);

		bottom.add(new JLabel("(Noir) Joueur 2: "));
		bottom.add(player2Opts);


		this.add(top);
		this.add(middle);
		this.add(bottom);
	}

	public DameFenetre getWindow() {
		return window;
	}

	public void setWindow(DameFenetre window) {
		this.window = window;
	}


	

	private static Player getPlayer() {
			return new HumanPlayer();
	}
	

	private class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// No window to update
			if (window == null) {
				return;
			}
			
			Object src = e.getSource();



			if (src == restartBtn) {
				window.restart();
			}

		}
	}
}

