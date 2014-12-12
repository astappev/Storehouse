package view;

import model.Model;

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

		AgentType agentTypes[] = new AgentType[data.size()];
		for (int i = 0; i < data.size(); ++i) {
			agentTypes[i] = new AgentType(Integer.parseInt(data.get(i).get(0).toString()), data.get(i).get(1).toString());
		}
		final JComboBox typeBox = new JComboBox(agentTypes);
		if (type != null) typeBox.setSelectedIndex(type - 1);
		typeBox.setBounds(100, 70, 325, 25);
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
				int typeNumber = ((AgentType) typeBox.getSelectedItem()).getId();
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

	class AgentType {
		private String name;
		private int id;

		public AgentType(int id, String name) {
			this.name = name;
			this.id = id;
		}

		public String toString() {
			return getName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}