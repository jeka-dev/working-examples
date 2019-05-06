package client.swing;

import core.MyCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final MyCore core = new MyCore();
	
	public MainFrame() {
		setTitle("Magic Formula");
		setSize(300, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createLayout();
	}

	private void createLayout() {
		Container pane = getContentPane();
		FlowLayout flowLayout = new FlowLayout();
		pane.setLayout(flowLayout);
		pane.add(new JLabel("Input number : "));
		final JTextField textField = new JTextField(8);
		pane.add(textField);
		JButton go = new JButton("Go");
		pane.add(go);
		final JLabel result = new JLabel();
		result.setPreferredSize(new Dimension(100, 30));
		pane.add(result);
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputText = textField.getText();
				int value = Integer.parseInt(inputText);
				int magic = core.magicFormula(value);
				result.setText(Integer.toString(magic));
			}
		});
	}

}
