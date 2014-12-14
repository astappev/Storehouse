package view;

import model.Model;
import model.MyComboBox;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class addStorage extends JFrame {
	Model model;
	String name;
	String rate;
	String notes;
	Integer purchase_price = null;
	Integer sale_price = null;
	Integer nomenclature;
	Integer price = 0;
	Integer total;
	Integer quantity = 0;

	JTextField quantityText;
	JTextField priceText;
	JTextField totalText;

	public addStorage(Model model, int id) {
		this.model = model;
		this.nomenclature = id;

		Vector<Vector<Object>> data = this.model.select_table("SELECT name,rate,notes,purchase_price,sale_price,quantity,total,avg_price FROM nomenclature INNER JOIN storage ON nomenclature.id = storage.nomenclature_id WHERE nomenclature.id = " + nomenclature + ";");
		this.name = (String) data.get(0).get(0);
		this.rate = (String) data.get(0).get(1);
		this.notes = (String) data.get(0).get(2);
		this.purchase_price = Integer.parseInt(data.get(0).get(3).toString());
		this.sale_price = Integer.parseInt(data.get(0).get(4).toString());
		this.quantity = Integer.parseInt(data.get(0).get(5).toString());
		this.price = Integer.parseInt(data.get(0).get(6).toString());
		this.total = Integer.parseInt(data.get(0).get(7).toString());

		initUI();
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

		JLabel nameLabel = new JLabel("Наименование");
		nameLabel.setBounds(10, 10, 100, 25);
		panel.add(nameLabel);

		final JTextField nameText = new JTextField();
		if (name != null) nameText.setText(name.trim());
		nameText.setEditable(false);
		nameText.setBounds(120, 10, 305, 25);
		nameLabel.setLabelFor(nameText);
		panel.add(nameText);

		JLabel rateLabel = new JLabel("Качество");
		rateLabel.setBounds(10, 40, 100, 25);
		panel.add(rateLabel);

		final JTextField rateText = new JTextField();
		if (rate != null) rateText.setText(rate.trim());
		rateText.setEditable(false);
		rateText.setBounds(120, 40, 305, 25);
		rateLabel.setLabelFor(rateText);
		panel.add(rateText);

		JLabel notesLabel = new JLabel("Примечание");
		notesLabel.setBounds(10, 70, 100, 25);
		panel.add(notesLabel);

		final JTextField notesText = new JTextField();
		if (notes != null) notesText.setText(notes.trim());
		notesText.setEditable(false);
		notesText.setBounds(120, 70, 305, 25);
		notesLabel.setLabelFor(notesText);
		panel.add(notesText);

		JLabel ppriceLabel = new JLabel("Цена покупки");
		ppriceLabel.setBounds(10, 100, 100, 25);
		panel.add(ppriceLabel);

		final JTextField ppriceText = new JTextField();
		if (purchase_price != null) ppriceText.setText(purchase_price.toString());
		ppriceText.setEditable(false);
		ppriceText.setBounds(120, 100, 305, 25);
		ppriceLabel.setLabelFor(ppriceText);
		panel.add(ppriceText);

		JLabel spriceLabel = new JLabel("Цена прожажи");
		spriceLabel.setBounds(10, 130, 100, 25);
		panel.add(spriceLabel);

		final JTextField spriceText = new JTextField();
		if (sale_price != null) spriceText.setText(sale_price.toString());
		spriceText.setEditable(false);
		spriceText.setBounds(120, 130, 305, 25);
		spriceLabel.setLabelFor(spriceText);
		panel.add(spriceText);

		JLabel quantityLabel = new JLabel("Количество");
		quantityLabel.setBounds(10, 160, 100, 25);
		panel.add(quantityLabel);

		quantityText = new JTextField();
		quantityText.setDocument(new JTextFieldLimit(10));
		if (quantity != null) quantityText.setText(quantity.toString());
		quantityText.setBounds(120, 160, 305, 25);
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
		priceLabel.setBounds(10, 190, 100, 25);
		panel.add(priceLabel);

		priceText = new JTextField();
		priceText.setDocument(new JTextFieldLimit(10));
		if (price != null) priceText.setText(price.toString());
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
		priceText.setBounds(120, 190, 305, 25);
		priceLabel.setLabelFor(priceText);
		panel.add(priceText);

		JLabel totalLabel = new JLabel("Сумма документа");
		totalLabel.setBounds(10, 220, 100, 25);
		panel.add(totalLabel);

		totalText = new JTextField();
		totalText.setDocument(new JTextFieldLimit(10));
		if (total != null) totalText.setText(total.toString());
		totalText.setEditable(false);
		totalText.setBounds(120, 220, 305, 25);
		totalLabel.setLabelFor(totalText);
		panel.add(totalText);

		JButton loginButton = new JButton("Обновить");
		loginButton.setBounds(10, 260, 200, 25);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(price == null) {
					JOptionPane.showMessageDialog(null, "Стоимость задана не верно!");
					return;
				}
				if(quantity == null) {
					JOptionPane.showMessageDialog(null, "Количество задано не верно!");
					return;
				}
				if(total == null) {
					JOptionPane.showMessageDialog(null, "Ошибка подсчета суммы!");
					return;
				}
				if (model.execute("UPDATE storage SET quantity = " + quantity + ", total = " + total + ", avg_price = " + price + " WHERE nomenclature_id = " + nomenclature + ";")) {
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Произошла ошибка!");
				}

				dispose();
			}
		});
		panel.add(loginButton);

		JButton registerButton = new JButton("Отмена");
		registerButton.setBounds(220, 260, 200, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(registerButton);

		pack();
		setTitle("Добавить контрагента");
		setSize(450, 340);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}