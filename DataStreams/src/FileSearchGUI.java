import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class FileSearchGUI extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private Path filePath;

    public FileSearchGUI() {
        setTitle("File Search GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addComponents();
    }

    private void initComponents() {
        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        searchField = new JTextField(20);

        loadButton = new JButton("Load File");
        loadButton.addActionListener(e -> loadFile());

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchFile());

        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);

        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originalScrollPane, filteredScrollPane), BorderLayout.CENTER);

        add(panel);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            try {
                String fileContent = Files.readString(filePath);
                originalTextArea.setText(fileContent);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        if (filePath == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String searchString = searchField.getText();
        try {
            Stream<String> lines = Files.lines(filePath);
            String filteredContent = lines.filter(line -> line.contains(searchString)).collect(Collectors.joining("\n"));
            filteredTextArea.setText(filteredContent);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error searching file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileSearchGUI gui = new FileSearchGUI();
            gui.setVisible(true);
        });
    }
}
