package clientWindow;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.ScrollPaneConstants;

public class ClientWindow extends JFrame {

	private JPanel contentPane;
	private ArrayList<File> sourceFiles = new ArrayList<File>();
	private String textPreEdit = "";
	private String textPostEdit = "";
	private File selectedFile;
	private Client client;

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

	/***********************************************************************************************************************/

	/**
	 * Create the frame.
	 */
	public ClientWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 370);

		// MENU STUFF
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "About");
			}
		});
		mnHelp.add(mntmAbout);

		// MAIN CONTENT PANEL
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// INSERT CODE TEXT AREA + SCROLL PANE
		JTextArea insertCodeTextArea = new JTextArea();
		insertCodeTextArea.setTabSize(3);
		insertCodeTextArea.setBounds(111, 41, 403, 259);
		insertCodeTextArea.setBorder(BorderFactory
				.createLineBorder(Color.black));
		contentPane.add(insertCodeTextArea);

		JScrollPane codeScrollPane = new JScrollPane(insertCodeTextArea);
		codeScrollPane.setBounds(191, 40, 403, 259);
		contentPane.add(codeScrollPane);

		JLabel insertCodeHereLabel = new JLabel("Your Code Goes Here:");
		insertCodeHereLabel.setBounds(191, 15, 132, 14);
		contentPane.add(insertCodeHereLabel);

		// SAVE AND SUBMIT BUTTON
		JButton submitCodeButton = new JButton("Save & Submit File");
		submitCodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
				sendToServer();
			}
		});
		submitCodeButton.setBounds(10, 147, 171, 37);
		contentPane.add(submitCodeButton);

		JLabel serverMessagesLabel = new JLabel("Server Messages:");
		serverMessagesLabel.setBounds(10, 195, 171, 14);
		contentPane.add(serverMessagesLabel);

		// SERVER MESSAGE TEXT AREA
		JTextArea serverMessagesTextArea = new JTextArea();
		serverMessagesTextArea.setTabSize(4);
		serverMessagesTextArea.setEditable(false);
		serverMessagesTextArea.setBounds(10, 220, 171, 79);
		serverMessagesTextArea.setBorder(BorderFactory
				.createLineBorder(Color.black));
		contentPane.add(serverMessagesTextArea);

		// OPENED JAVA FILE TEXT AREA + SCROLL PANE
		JTextArea javaFileOpenedTextArea = new JTextArea();
		javaFileOpenedTextArea.setBounds(10, 80, 169, 35);
		contentPane.add(javaFileOpenedTextArea);
		javaFileOpenedTextArea.setEditable(false);
		javaFileOpenedTextArea.setBorder(BorderFactory
				.createLineBorder(Color.black));

		JScrollPane selectFileScrollPane = new JScrollPane(
				javaFileOpenedTextArea);
		selectFileScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		selectFileScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		selectFileScrollPane.setBounds(10, 58, 171, 44);
		contentPane.add(selectFileScrollPane);

		// OPEN FILE BUTTON
		// reference:http://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser
		JButton openFileButton = new JButton("Open Java File");
		openFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedFile = openFile();
				javaFileOpenedTextArea.append(selectedFile.getAbsolutePath());
				insertCodeTextArea.setText("");
				textPreEdit = parseFile(selectedFile);
				insertCodeTextArea.append(textPreEdit);
			}
		});
		openFileButton.setBounds(10, 11, 171, 23);
		contentPane.add(openFileButton);

		// SAVE BUTTON
		JButton saveButton = new JButton("Save Selected File");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textPostEdit = insertCodeTextArea.getText();
				saveFile();
			}
		});
		saveButton.setBounds(10, 113, 171, 23);
		contentPane.add(saveButton);

		JLabel lblSelectedFile = new JLabel("Selected File:");
		lblSelectedFile.setBounds(10, 36, 171, 14);
		contentPane.add(lblSelectedFile);
	}

	/***********************************************************************************************************************/

	// OPEN FILE METHOD
	public File openFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System
				.getProperty("user.home")));

		fileChooser.setFileFilter(new FileNameExtensionFilter("Java Files",
				"java"));

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}
		return null;
	}

	// FILE PARSER METHOD
	public String parseFile(File file) {
		String contents = "";
		String line;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				contents += line + "\n";
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contents;
	}

	// SAVE FILE METHOD
	public void saveFile() {
		int compareVal = textPreEdit.compareTo(textPostEdit);
		if (compareVal != 0) {
			BufferedWriter out;
			try {
				out = new BufferedWriter(new FileWriter(selectedFile));
				out.write(textPostEdit);
				out.close();
				JOptionPane.showMessageDialog(null, "File Saved");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// SEND SELECTED FILE METHOD
	public void sendToServer() {
		client = new Client();
		boolean connected = client.connect();
		client.sendData();
	}
}
