import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class GUI extends JFrame implements ActionListener {
    Parser data = new Parser();
    JTextField text = new JTextField(250);
    JLabel lab = new JLabel("Enter the city name:");
    JButton but = new JButton("  OK  ");
    JButton but2 = new JButton("Clean");
    JLabel error = new JLabel();
    JLabel temperature = new JLabel();
    JLabel wind = new JLabel();
    JLabel preassure = new JLabel();
    JLabel hum = new JLabel();
    JLabel water = new JLabel();
    JPanel panel = new JPanel();
    JSeparator separator = new JSeparator();
    JSeparator separator1 = new JSeparator();
    JProgressBar progressBar = new JProgressBar();
    Component[] components;

    public GUI() {
        super("Weather in Armenia");
        setLayout(null);
        setSize(new Dimension(400, 150));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        text.setMaximumSize(text.getPreferredSize());
        text.setAlignmentX(LEFT_ALIGNMENT);
        JPanel butPanel = new JPanel();
        butPanel.setAlignmentX(LEFT_ALIGNMENT);
        butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.X_AXIS));
        butPanel.add(but);
        butPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        butPanel.add(but2);
        separator.setVisible(false);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(122, 138, 153));
        separator1.setForeground(new Color(122, 138, 153));
        separator1.setVisible(false);
        progressBar.setAlignmentX(LEFT_ALIGNMENT);
        progressBar.setVisible(false);
//        UIManager.put("ProgressBar.repaintInterval", 1);
//        UIManager.put("ProgressBar.cycleTime", 400);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(padding);
        setContentPane(panel);
        text.addActionListener(this);
        but.addActionListener(this);
        but2.addActionListener(new ListenToClean());

        components = new Component[] {
            lab, text, butPanel, progressBar, separator, temperature,
            wind, preassure, hum, water, error, separator1
        };

        for(Component component : components) {
            if(component instanceof JProgressBar)
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(component);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(text.getText().isEmpty())
            JOptionPane.showMessageDialog(this, "Enter the city name.", "Empty field.", JOptionPane.WARNING_MESSAGE);
        else {
            new Thread(() -> {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                int length = components.length-2;
                boolean error = false;
                List<Component> resultComponents =  Arrays.asList(components).subList(5, length);
                List<String> res = data.getData(text.getText());
                separator.setVisible(true);
                separator1.setVisible(true);
                setSize(new Dimension(400, 250));
                for(int i = 0; i < resultComponents.size(); i++) {
                    try {
                        JLabel tmp = (JLabel) resultComponents.get(i);
                        tmp.setText(res.get(i));
                        progressBar.setVisible(false);
                    }   catch (NullPointerException  ex) {
                        progressBar.setVisible(false);
                        separator.setVisible(false); separator1.setVisible(false);
                        error = true;
                        cleanResult(resultComponents);
                    }
                }
                if(error) {
                    setSize(new Dimension(400, 150));
                    JOptionPane.showMessageDialog(this, "Error: City not found!", "Error",  JOptionPane.ERROR_MESSAGE);
                }

            }).start();
        }

    }

    private void cleanResult(List<Component> components) {
        for(Component c : components) {
            ((JLabel) c).setText("");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException  | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        GUI app = new GUI();
        app.setVisible(true);
    }

    private class ListenToClean implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for(Component c : Arrays.asList(components).subList(4, components.length)) {
                if (c instanceof JLabel) {
                    ((JLabel) c).setText("");
                }   else {
                        c.setVisible(false);
                }
            }
            text.setText("");
            setSize(new Dimension(400, 150));
        }
    }
}