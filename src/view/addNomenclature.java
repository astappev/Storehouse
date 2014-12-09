package view;

import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class addNomenclature extends JFrame {
	Model model;
	Integer id;

	String name = null;
	Integer purchase_price = null;
	Integer sale_price = null;
	String rate = null;
	String notes = null;

	public addNomenclature(Model model) {
		this.model = model;
		initUI();
	}

	public addNomenclature(Model model, int id) {
		this.model = model;
		this.id = id;
		Vector<Vector<Object>> data = this.model.select_table("SELECT name,purchase_price,sale_price,rate,notes FROM nomenclature WHERE id=" + id + ";");
		this.name = (String) data.get(0).get(0);
		this.purchase_price = Integer.parseInt(data.get(0).get(1).toString());
		this.sale_price = Integer.parseInt(data.get(0).get(2).toString());
		this.rate = (String) data.get(0).get(3);
		this.notes = (String) data.get(0).get(4);

		initUI();
	}

	public void initUI() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		add(panel);
		panel.setLayout(null);

		JLabel nameLabel = new JLabel("Название");
		nameLabel.setBounds(10, 10, 80, 25);
		panel.add(nameLabel);

		final JTextField nameText = new JTextField();
		nameText.setDocument(new JTextFieldLimit(64));
		if (name != null) nameText.setText(name.trim());
		nameText.setBounds(100, 10, 325, 25);
		nameLabel.setLabelFor(nameText);
		panel.add(nameText);

		JLabel purchaseLabel = new JLabel("Цена покупки");
		purchaseLabel.setBounds(10, 40, 80, 25);
		panel.add(purchaseLabel);

		final JTextField purchaseText = new JTextField();
		purchaseText.setDocument(new JTextFieldLimit(10));
		if (purchase_price != null) purchaseText.setText(purchase_price.toString());
		purchaseText.setBounds(100, 40, 325, 25);
		purchaseLabel.setLabelFor(purchaseText);
		panel.add(purchaseText);

		JLabel saleLabel = new JLabel("Цена продажи");
		saleLabel.setBounds(10, 70, 80, 25);
		panel.add(saleLabel);

		final JTextField saleText = new JTextField();
		saleText.setDocument(new JTextFieldLimit(10));
		if (sale_price != null) saleText.setText(sale_price.toString());
		saleText.setBounds(100, 70, 325, 25);
		saleLabel.setLabelFor(saleText);
		panel.add(saleText);

		JLabel notesLabel = new JLabel("Примечание");
		notesLabel.setBounds(10, 100, 80, 25);
		panel.add(notesLabel);

		final JTextField notesText = new JTextField();
		notesText.setDocument(new JTextFieldLimit(128));
		if (notes != null) notesText.setText(notes.trim());
		notesText.setBounds(100, 100, 325, 25);
		notesLabel.setLabelFor(notesText);
		panel.add(notesText);

		JLabel agentLabel = new JLabel("Качество");
		agentLabel.setBounds(10, 130, 80, 25);
		panel.add(agentLabel);

		final JTextField rateText = new JTextField();
		rateText.setDocument(new JTextFieldLimit(64));
		if (rate != null) rateText.setText(rate.trim());
		rateText.setBounds(100, 130, 325, 25);
		saleLabel.setLabelFor(rateText);
		panel.add(rateText);

		JButton loginButton = new JButton((id == null) ? "Добавить" : "Обновить");
		loginButton.setBounds(10, 160, 200, 25);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nameString = nameText.getText();
				if (nameString.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Название товара не заполнено");
					return;
				}
				String purchaseString = purchaseText.getText();
				String saleString = saleText.getText();
				String notesString = notesText.getText();
				String rateString = rateText.getText();
				if (id == null) {
					if (model.execute("INSERT INTO nomenclature(name,purchase_price,sale_price,rate,notes) VALUES ('" + nameString + "', " + purchaseString + ", " + saleString + ", '" + rateString + "', '" + notesString + "');")) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
					}
				} else {
					if (model.execute("UPDATE nomenclature SET name='" + nameString + "', notes='" + notesString + "', rate='" + rateString + "', purchase_price=" + purchaseString + ", sale_price=" + saleString + " WHERE id=" + id + ";")) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Произошла ошибка!");
					}
				}
			}
		});
		panel.add(loginButton);

		JButton registerButton = new JButton((id == null) ? "Отмена" : "Удалить");
		registerButton.setBounds(220, 160, 200, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (id == null) {
					dispose();
				} else {
					if (model.execute("DELETE FROM nomenclature WHERE id=" + id + ";")) {
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
		setSize(450, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}