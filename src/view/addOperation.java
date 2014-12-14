package view;

import model.*;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

public class addOperation extends JFrame {
	Model model;
	Integer id;
	String data = null;
	Integer price = 0;
	Integer total;
	Integer quantity = 0;
	Integer agent_id;
	Integer nomenclature_id;
	Integer document_type;

	MyComboBox agentBox = null;
	MyComboBox nomenclatureBox = null;
	JTextField quantityText;
	JTextField priceText;
	JTextField totalText;
	MyComboBox typeBox;
	JDatePickerImpl datePicker;

	public addOperation(Model model) {
		this.model = model;
		this.document_type = 1;
		initUI();
	}

	public addOperation(Model model, int id) {
		this.model = model;
		this.id = id;
		Vector<Vector<Object>> data = this.model.select_table("SELECT data, price, quantity, total, document_type, agent_id, nomenclature_id FROM operation_log WHERE id=" + id + ";");
		this.data = (String) data.get(0).get(0).toString();
		this.price = Integer.parseInt(data.get(0).get(1).toString());
		this.quantity = Integer.parseInt(data.get(0).get(2).toString());
		this.total = Integer.parseInt(data.get(0).get(3).toString());
		this.document_type = Integer.parseInt(data.get(0).get(4).toString());
		this.agent_id = Integer.parseInt(data.get(0).get(5).toString());
		this.nomenclature_id = Integer.parseInt(data.get(0).get(6).toString());

		initUI();
	}

	private void updateAgent() {
		Vector<Vector<Object>> data = this.model.select_table("SELECT agent.id, agent.name FROM agent INNER JOIN document_type ON document_type.agent_type = agent.agent_type WHERE document_type.id = " + document_type + ";");
		if(agentBox == null) {
			agentBox = new MyComboBox(data);
			if (agent_id != null) agentBox.setSelectedItem(agent_id);
		} else {
			agentBox.update(data);
		}
	}

	private void updateNomenclature() {
		Vector<Vector<Object>> data;
		if (document_type == 2) data = this.model.select_table("SELECT id, name || ' (' || quantity || ')' FROM nomenclature INNER JOIN storage ON nomenclature.id = storage.nomenclature_id  WHERE quantity > 0;");
		else data = this.model.select_table("SELECT id,name FROM nomenclature;");
		nomenclatureBox.update(data);
	}

	private void updateTotal() {
		try {
			this.quantity = Integer.parseInt(quantityText.getText());
		} catch (Exception e) {
			this.quantity = 0;
		}

		try {
			this.price = Integer.parseInt(priceText.getText());
		} catch (Exception e) {
			this.price = 0;
		}

		this.total = this.quantity * this.price;
		totalText.setText(total.toString());
	}

	public void initUI() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		add(panel);
		panel.setLayout(null);

		JLabel documentLabel = new JLabel("Тип документа");
		documentLabel.setBounds(10, 10, 100, 25);
		panel.add(documentLabel);

		Vector<Vector<Object>> data = this.model.select_table("SELECT id,name FROM document_type WHERE id IN(1,2);");
		typeBox = new MyComboBox(data);
		if (document_type != null) typeBox.setSelectedItem(document_type);
		typeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				document_type = typeBox.getSelectedItem().getValue();
				updateAgent();
				updateNomenclature();
			}
		});
		typeBox.setBounds(120, 10, 305, 25);
		documentLabel.setLabelFor(typeBox);
		panel.add(typeBox);

		JLabel dataLabel = new JLabel("Дата");
		dataLabel.setBounds(10, 40, 100, 25);
		panel.add(dataLabel);

		UtilDateModel dateModel = new UtilDateModel();
		dateModel.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		Properties p = new Properties();
		p.put("text.today", "Сегодня");
		p.put("text.month", "Месяц");
		p.put("text.year", "Год");
		dateModel.setSelected(true);
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
		datePicker = new JDatePickerImpl(datePanel, new DateModel());
		datePicker.setBounds(120, 40, 305, 25);
		dataLabel.setLabelFor(datePicker);
		panel.add(datePicker);

		JLabel agentLabel = new JLabel("Контр агент");
		agentLabel.setBounds(10, 70, 100, 25);
		panel.add(agentLabel);

		updateAgent();
		agentBox.setBounds(120, 70, 305, 25);
		agentLabel.setLabelFor(agentBox);
		panel.add(agentBox);

		JLabel nomenclatureLabel = new JLabel("Номенклатура");
		nomenclatureLabel.setBounds(10, 100, 100, 25);
		panel.add(nomenclatureLabel);

		Vector<Vector<Object>> dataNomenclature = this.model.select_table("SELECT id,name FROM nomenclature;");
		nomenclatureBox = new MyComboBox(dataNomenclature);
		if (nomenclature_id != null) nomenclatureBox.setSelectedItem(nomenclature_id);
		nomenclatureBox.setBounds(120, 100, 305, 25);
		agentLabel.setLabelFor(nomenclatureBox);
		panel.add(nomenclatureBox);

		JLabel quantityLabel = new JLabel("Количество");
		quantityLabel.setBounds(10, 130, 100, 25);
		panel.add(quantityLabel);

		quantityText = new JTextField();
		quantityText.setDocument(new JTextFieldLimit(10));
		quantityText.setBounds(120, 130, 305, 25);
		quantityText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTotal();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTotal();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTotal();
			}
		});
		quantityLabel.setLabelFor(quantityText);
		panel.add(quantityText);

		JLabel priceLabel = new JLabel("Цена");
		priceLabel.setBounds(10, 160, 100, 25);
		panel.add(priceLabel);

		priceText = new JTextField();
		priceText.setDocument(new JTextFieldLimit(10));
		priceText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTotal();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTotal();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTotal();
			}
		});
		priceText.setBounds(120, 160, 305, 25);
		priceLabel.setLabelFor(priceText);
		panel.add(priceText);

		JLabel totalLabel = new JLabel("Сумма документа");
		totalLabel.setBounds(10, 190, 100, 25);
		panel.add(totalLabel);

		totalText = new JTextField();
		totalText.setDocument(new JTextFieldLimit(10));
		totalText.setEditable(false);
		totalText.setBounds(120, 190, 305, 25);
		totalLabel.setLabelFor(totalText);
		panel.add(totalText);

		JButton loginButton = new JButton("Добавить");
		loginButton.setBounds(10, 230, 200, 25);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dateString = datePicker.getModel().getYear() + "-" + (datePicker.getModel().getMonth() + 1) + "-" + datePicker.getModel().getDay();
				if (dateString.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Дата проводки не выбрана!");
					return;
				}
				if(price == 0 || price == null) {
					JOptionPane.showMessageDialog(null, "Стоимость задана не верно!");
					return;
				}
				if(quantity == 0 || quantity == null) {
					JOptionPane.showMessageDialog(null, "Количество задано не верно!");
					return;
				}
				if(total == 0 || total == null) {
					JOptionPane.showMessageDialog(null, "Ошибка подсчета суммы!");
					return;
				}
				try {
					agent_id = agentBox.getSelectedItem().getValue();
					nomenclature_id = nomenclatureBox.getSelectedItem().getValue();
					document_type = typeBox.getSelectedItem().getValue();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Одно из полей не выбрано!");
					return;
				}

				if (document_type == 1) {
					if (!model.execute("UPDATE storage SET quantity =  quantity + " + quantity + ", total = total + " + price + " WHERE nomenclature_id = " + nomenclature_id + ";\n" +
							"INSERT INTO storage (nomenclature_id, quantity, total) SELECT " + nomenclature_id + ", " + quantity + ", " + price + " WHERE NOT EXISTS (SELECT nomenclature_id FROM storage WHERE nomenclature_id = " + nomenclature_id + ");" +
							"UPDATE storage SET avg_price = total/quantity WHERE nomenclature_id = " + nomenclature_id + ";")) {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
						return;
					}
				} else if(document_type == 2) {
					Vector<Object> count = model.select("SELECT quantity FROM storage WHERE nomenclature_id = " + nomenclature_id + ";");
					if(Integer.parseInt(count.get(0).toString())-quantity >= 0) {
						if (!model.execute("UPDATE storage SET quantity = quantity - " + quantity + " WHERE nomenclature_id = " + nomenclature_id + ";\n" +
								"UPDATE storage SET total = (total - (" + quantity + "*avg_price)) WHERE nomenclature_id = " + nomenclature_id + ";")) {
							JOptionPane.showMessageDialog(null, "Произошла ошибка!");
							return;
						}
					} else {
						JOptionPane.showMessageDialog(null, "Недостаточно товара на складе!");
						return;
					}
				}
				Integer operation;
				if((operation = model.insert("INSERT INTO operation_log (data, price, quantity, total, document_type, agent_id, nomenclature_id) VALUES('" + dateString + "', " + price + ", " + quantity + ", " + total + ", " + document_type + ", " + agent_id + ", " + nomenclature_id + ");")) == 0) {
					JOptionPane.showMessageDialog(null, "Произошла ошибка при добавлении операции!");
					return;
				}
				if(!model.execute("INSERT INTO transaction_log (transaction_type, operation_id, total) SELECT transaction_type.id, operation_log.id, CASE WHEN transaction_type.account_debit = 11 THEN storage.avg_price*operation_log.quantity ELSE operation_log.total END FROM operation_log, transaction_type, storage WHERE operation_log.document_type = transaction_type.document_type AND operation_log.nomenclature_id = storage.nomenclature_id AND operation_log.id = " + operation + ";")) {
					JOptionPane.showMessageDialog(null, "Произошла ошибка при добавлении проводки!");
					return;
				}
				JOptionPane.showMessageDialog(null, "Проводка успешна");
				dispose();
			}
		});
		panel.add(loginButton);

		JButton registerButton = new JButton("Отмена");
		registerButton.setBounds(220, 230, 200, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(registerButton);

		pack();
		setTitle("Работа с торговой операцией");
		setSize(450, 310);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	int toTimestamp(int year, int month, int day, int hour, int minute) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return (int) (c.getTimeInMillis() / 1000L);
	}
}