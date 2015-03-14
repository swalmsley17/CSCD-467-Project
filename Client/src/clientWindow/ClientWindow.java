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
	private JTextField serverIPTextField;
	private JTextField serverPortTextField;
	private JTextArea serverMessagesTextArea;

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
		setBounds(100, 100, 800, 500);

		// MENU STUFF
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

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

		JScrollPane codeScrollPane = new JScrollPane();
		codeScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		codeScrollPane.setBounds(301, 96, 473, 333);
		contentPane.add(codeScrollPane);

		final JTextArea insertCodeTextArea = new JTextArea();
		insertCodeTextArea.setTabSize(3);
		codeScrollPane.setViewportView(insertCodeTextArea);

		JLabel insertCodeHereLabel = new JLabel("Your Code Goes Here:");
		insertCodeHereLabel.setBounds(301, 71, 132, 14);
		contentPane.add(insertCodeHereLabel);

		JLabel serverMessagesLabel = new JLabel("Server Messages:");
		serverMessagesLabel.setBounds(10, 199, 171, 14);
		contentPane.add(serverMessagesLabel);

		JScrollPane selectFileScrollPane = new JScrollPane();
		selectFileScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		selectFileScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		selectFileScrollPane.setBounds(10, 144, 281, 44);
		contentPane.add(selectFileScrollPane);

		final JTextArea javaFileOpenedTextArea = new JTextArea();
		javaFileOpenedTextArea.setEditable(false);
		javaFileOpenedTextArea.setTabSize(3);
		selectFileScrollPane.setViewportView(javaFileOpenedTextArea);

		// SAVE BUTTON
		JButton saveButton = new JButton("Save Selected File");
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textPostEdit = insertCodeTextArea.getText();
				saveFile();
			}
		});
		saveButton.setBounds(65, 40, 171, 23);
		contentPane.add(saveButton);

		// SAVE AND SUBMIT BUTTON
		JButton submitCodeButton = new JButton("Save & Submit File");
		submitCodeButton.setEnabled(false);
		submitCodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPostEdit = insertCodeTextArea.getText();
				saveFile();
				sendToServer();
			}
		});
		submitCodeButton.setBounds(65, 74, 171, 37);
		contentPane.add(submitCodeButton);

		JMenuItem mntmSave = new JMenuItem("Save...");
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textPostEdit = insertCodeTextArea.getText();
				saveFile();
			}
		});

		JMenuItem mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.setEnabled(false);
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});

		JMenuItem mntmOpen = new JMenuItem("Open File...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedFile = openFile();
				javaFileOpenedTextArea.append(selectedFile.getAbsolutePath());
				insertCodeTextArea.setText("");
				textPreEdit = parseFile(selectedFile);
				insertCodeTextArea.append(textPreEdit);
				mntmSave.setEnabled(true);
				mntmSaveAs.setEnabled(true);
				saveButton.setEnabled(true);
				submitCodeButton.setEnabled(true);
			}
		});

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		mnFile.add(mntmOpen);
		mnFile.add(mntmSave);
		mnFile.add(mntmSaveAs);
		mnFile.add(mntmExit);

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
				saveButton.setEnabled(true);
				submitCodeButton.setEnabled(true);
				mntmSave.setEnabled(true);
				mntmSaveAs.setEnabled(true);
			}
		});

		openFileButton.setBounds(65, 11, 171, 23);
		contentPane.add(openFileButton);

		JLabel lblSelectedFile = new JLabel("Selected File:");
		lblSelectedFile.setBounds(10, 122, 171, 14);
		contentPane.add(lblSelectedFile);

		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setBounds(301, 11, 61, 14);
		contentPane.add(lblServerIp);

		serverIPTextField = new JTextField();
		serverIPTextField.setText("127.0.0.1");
		serverIPTextField.setBounds(301, 36, 171, 20);
		contentPane.add(serverIPTextField);
		serverIPTextField.setColumns(10);

		JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setBounds(482, 11, 79, 14);
		contentPane.add(lblServerPort);

		serverPortTextField = new JTextField();
		serverPortTextField.setText("15001");
		serverPortTextField.setBounds(482, 36, 86, 20);
		contentPane.add(serverPortTextField);
		serverPortTextField.setColumns(10);

		JScrollPane serverMessagesScrollPane = new JScrollPane();
		serverMessagesScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		serverMessagesScrollPane.setBounds(10, 224, 281, 164);
		contentPane.add(serverMessagesScrollPane);

		serverMessagesTextArea = new JTextArea();
		serverMessagesTextArea.setEditable(false);
		serverMessagesTextArea.setTabSize(3);
		serverMessagesScrollPane.setViewportView(serverMessagesTextArea);

		JButton btnClearServerMessages = new JButton("Clear Server Messages...");
		btnClearServerMessages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverMessagesTextArea.setText("");
			}
		});
		btnClearServerMessages.setBounds(10, 399, 281, 30);
		contentPane.add(btnClearServerMessages);
	}

	/***********************************************************************************************************************/

	// OPEN FILE METHOD
	private File openFile() {
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
	private String parseFile(File file) {
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
	private void saveFile() {
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

	private void saveFileAs() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(this);
	}

	// SEND SELECTED FILE METHOD
	private void sendToServer() {
		client = new Client();
		client.connectToServer(serverIPTextField.getText(),
				serverPortTextField.getText(), parseFile(selectedFile), this);
	}

	// SET SERVER MESSAGE BOX
	public void receiveServerMessage(String text) {
		serverMessagesTextArea.append(text + "\n");
	}
}
