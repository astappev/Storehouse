package view;

import model.JTextFieldLimit;
import model.Model;
import model.MyComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class addAgent extends JFrame {
	Model model;
	Integer id;

	String fio = null;
	String notes = null;
	Integer type = null;

	public addAgent(Model model) {
		this.model = model;
		initUI();
	}

	public addAgent(Model model, int type) {
		this.model = model;
		this.type = type;
		initUI();
	}

	public addAgent(Model model, int type, int id) {
		this.model = model;
		this.type = type;
		this.id = id;
		Vector<Vector<Object>> data = this.model.select_table("SELECT name,notes,agent_type FROM agent WHERE id=" + id + " AND agent_type=" + type + ";");
		this.fio = (String) data.get(0).get(0);
		this.notes = (String) data.get(0).get(1);
		this.type = Integer.parseInt(data.get(0).get(2).toString());

		initUI();
	}

	public void initUI() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		add(panel);
		panel.setLayout(null);

		JLabel fioLabel = new JLabel("ФИО");
		fioLabel.setBounds(10, 10, 80, 25);
		panel.add(fioLabel);

		final JTextField fioText = new JTextField();
		fioText.setDocument(new JTextFieldLimit(64));
		if (fio != null) fioText.setText(fio.trim());
		fioText.setBounds(100, 10, 325, 25);
		fioLabel.setLabelFor(fioText);
		panel.add(fioText);

		JLabel notesLabel = new JLabel("Примечание");
		notesLabel.setBounds(10, 40, 80, 25);
		panel.add(notesLabel);

		final JTextField notesText = new JTextField();
		notesText.setDocument(new JTextFieldLimit(128));
		if (notes != null) notesText.setText(notes.trim());
		notesText.setBounds(100, 40, 325, 25);
		notesLabel.setLabelFor(notesText);
		panel.add(notesText);

		JLabel agentLabel = new JLabel("Тип агента");
		agentLabel.setBounds(10, 70, 80, 25);
		panel.add(agentLabel);

		Vector<Vector<Object>> data = this.model.select_table("SELECT id,name FROM agent_type;");
		final MyComboBox typeBox = new MyComboBox(data);
		if (type != null) typeBox.setSelectedItem(type);
		typeBox.setBounds(100, 70, 325, 25);
		typeBox.setBackground(Color.blue);
		agentLabel.setLabelFor(typeBox);
		panel.add(typeBox);

		JButton loginButton = new JButton((id == null) ? "Добавить" : "Обновить");
		loginButton.setBounds(10, 110, 200, 25);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fioString = fioText.getText();
				if (fioString.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Фио пользователя не заполнено!");
					return;
				}
				String notesString = notesText.getText();
				int typeNumber = typeBox.getSelectedItem().getValue();
				if (id == null) {
					if (model.execute("INSERT INTO agent(name,notes,agent_type) VALUES ('" + fioString + "', '" + notesString + "', " + typeNumber + ");")) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
					}
				} else {
					if (model.execute("UPDATE agent SET name='" + fioString + "', notes='" + notesString + "', agent_type=" + typeNumber + " WHERE id=" + id + ";")) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
					}
				}
			}
		});
		panel.add(loginButton);

		JButton registerButton = new JButton((id == null) ? "Отмена" : "Удалить");
		registerButton.setBounds(220, 110, 200, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (id == null) {
					dispose();
				} else {
					if (model.execute("DELETE FROM agent WHERE id=" + id + ";")) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
					}
				}
			}
		});
		panel.add(registerButton);

		pack();
		setTitle("Добавить контрагента");
		setSize(450, 180);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}