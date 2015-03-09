package clientWindow;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;

public class ClientWindow extends JFrame {

	private JPanel contentPane;
	private JTextField insertCodeTextField;
	private JTextField serverMessagesTextfield;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow frame = new ClientWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 370);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel javaFilesLabel = new JLabel("Java Files:");
		javaFilesLabel.setBounds(10, 11, 53, 14);
		contentPane.add(javaFilesLabel);
		
		JLabel insertCodeHereLabel = new JLabel("Insert Code Here:");
		insertCodeHereLabel.setBounds(191, 11, 96, 14);
		contentPane.add(insertCodeHereLabel);
		
		insertCodeTextField = new JTextField();
		insertCodeTextField.setBounds(191, 36, 403, 263);
		contentPane.add(insertCodeTextField);
		insertCodeTextField.setColumns(10);
		
		JButton submitCodeButton = new JButton("Submit Question");
		submitCodeButton.setBounds(10, 169, 132, 37);
		contentPane.add(submitCodeButton);
		
		JLabel serverMessagesLabel = new JLabel("Server Messages:");
		serverMessagesLabel.setBounds(10, 217, 86, 14);
		contentPane.add(serverMessagesLabel);
		
		serverMessagesTextfield = new JTextField();
		serverMessagesTextfield.setBounds(10, 242, 171, 57);
		contentPane.add(serverMessagesTextfield);
		serverMessagesTextfield.setColumns(10);
		
		JComboBox javaFilesComboBox = new JComboBox();
		javaFilesComboBox.setBounds(10, 36, 132, 20);
		contentPane.add(javaFilesComboBox);
		
		JButton nextButton = new JButton("Next Question");
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null, "Hello World");
			}
		});
		nextButton.setBounds(10, 67, 132, 23);
		contentPane.add(nextButton);
		
		JButton previousQuestionButton = new JButton("Previous Question");
		previousQuestionButton.setBounds(10, 101, 132, 23);
		contentPane.add(previousQuestionButton);
		
		JButton skipButton = new JButton("Skip Question");
		skipButton.setBounds(10, 135, 132, 23);
		contentPane.add(skipButton);
	}
}