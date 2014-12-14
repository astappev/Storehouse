package view;

import model.DateModel;
import model.Model;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Properties;
import java.util.Vector;

public class report extends JFrame {
	private Model model;
	private DefaultTableModel dataModel;

	JDatePickerImpl datePickerFrom;
	JDatePickerImpl datePickerTo;

	public report(Model m) {
		this.model = m;
		initUI();
	}

	public void initUI() {
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("СчетИН");
		columnNames.add("Сальдо Н_Д");
		columnNames.add("Сально Н_К");
		columnNames.add("Обороты Д");
		columnNames.add("Обороты К");
		columnNames.add("Сальдо К_Д");
		columnNames.add("Сальдо К_К");

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		dataModel = new DefaultTableModel(data, columnNames);

		final JTable table = new JTable(dataModel) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dataModel) {
			@Override
			public Comparator<?> getComparator(int column) {
				if (column == 0) {
					return new Comparator<String>() {
						@Override
						public int compare(String s1, String s2) {
							return Integer.parseInt(s1) - Integer.parseInt(s2);
						}
					};
				}
				return super.getComparator(column);
			}
		};
		table.setRowSorter(sorter);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);



		JPanel panel = new JPanel() {
			public Dimension getPreferredSize() {
				return new Dimension(800, 40);
			};
		};
		panel.setLayout(null);
		JLabel label = new JLabel("Укажите дату:");
		label.setBounds(10, 5, 100, 30);
		panel.add(label);

		Properties p = new Properties();
		p.put("text.today", "Сегодня");
		p.put("text.month", "Месяц");
		p.put("text.year", "Год");

		Calendar calend = Calendar.getInstance();
		UtilDateModel dateModelTo = new UtilDateModel();
		dateModelTo.setDate(calend.get(Calendar.YEAR), calend.get(Calendar.MONTH), calend.get(Calendar.DAY_OF_MONTH));
		dateModelTo.setSelected(true);

		calend.add(Calendar.DAY_OF_MONTH, -14);
		UtilDateModel dateModelFrom = new UtilDateModel();
		dateModelFrom.setDate(calend.get(Calendar.YEAR), calend.get(Calendar.MONTH), calend.get(Calendar.DAY_OF_MONTH));
		dateModelFrom.setSelected(true);

		JDatePanelImpl datePanelFrom = new JDatePanelImpl(dateModelFrom, p);
		datePickerFrom = new JDatePickerImpl(datePanelFrom, new DateModel());
		datePickerFrom.setBounds(210, 5, 200, 30);
		panel.add(datePickerFrom);

		JDatePanelImpl datePanelTo = new JDatePanelImpl(dateModelTo, p);
		datePickerTo = new JDatePickerImpl(datePanelTo, new DateModel());
		datePickerTo.setBounds(440, 5, 200, 30);
		panel.add(datePickerTo);

		JButton button = new JButton("Посчитать");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dateFrom = datePickerFrom.getModel().getYear() + "-" + (datePickerFrom.getModel().getMonth() + 1) + "-" + datePickerFrom.getModel().getDay();
				String dateTo = datePickerTo.getModel().getYear() + "-" + (datePickerTo.getModel().getMonth() + 1) + "-" + datePickerTo.getModel().getDay();

				if (dateFrom.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Дата начала не выбрана!");
					return;
				}
				if (dateTo.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Дата окончания не выбрана!");
					return;
				}

				if (!model.execute("DELETE FROM turn_sheets;")) {
					JOptionPane.showMessageDialog(null, "Произошла ошибка при очистке данных!");
					return;
				}
				if (!model.execute("INSERT INTO turn_sheets (account_id, saldo_beg_debit, saldo_beg_credit, turn_debit, turn_credit) SELECT\n" +
						"account.id,\n" +
						"COALESCE((SELECT SUM(transaction_log.total) FROM transaction_log INNER JOIN operation_log ON transaction_log.operation_id = operation_log.id INNER JOIN transaction_type ON transaction_log.transaction_type = transaction_type.id WHERE operation_log.data < '" + dateTo + "' AND transaction_type.account_debit = account.id), 0),\n" +
						"COALESCE((SELECT SUM(transaction_log.total) FROM transaction_log INNER JOIN operation_log AS operation_log ON transaction_log.operation_id = operation_log.id INNER JOIN transaction_type ON transaction_log.transaction_type = transaction_type.id WHERE operation_log.data < '" + dateTo + "' AND transaction_type.account_credit = account.id), 0),\n" +
						"COALESCE((SELECT SUM(transaction_log.total) FROM transaction_log INNER JOIN operation_log ON transaction_log.operation_id = operation_log.id INNER JOIN transaction_type ON transaction_log.transaction_type = transaction_type.id WHERE operation_log.data BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND transaction_type.account_debit = account.id), 0),\n" +
						"COALESCE((SELECT SUM(transaction_log.total) FROM transaction_log INNER JOIN operation_log ON transaction_log.operation_id = operation_log.id INNER JOIN transaction_type ON transaction_log.transaction_type = transaction_type.id WHERE operation_log.data BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND transaction_type.account_credit = account.id), 0)\n" +
						"FROM account;")) {
					JOptionPane.showMessageDialog(null, "Произошла ошибка занесении данных!");
					return;
				}
				if (!model.execute("UPDATE turn_sheets SET\n" +
						"saldo_fin_debit = CASE WHEN (saldo_beg_debit-saldo_beg_credit+turn_debit-turn_credit) >= 0 THEN (saldo_beg_debit-saldo_beg_credit+turn_debit-turn_credit) ELSE 0 END,\n" +
						"saldo_fin_credit = CASE WHEN (saldo_beg_debit-saldo_beg_credit+turn_debit-turn_credit) < 0 THEN (saldo_beg_debit-saldo_beg_credit+turn_debit-turn_credit)*(-1) ELSE 0 END;\n")) {
					JOptionPane.showMessageDialog(null, "Произошла ошибка при вычислении данных!");
					return;
				}
				model.updateDataModel(dataModel, "SELECT account.name, turn_credit, turn_debit, saldo_beg_credit, saldo_beg_debit, saldo_fin_credit, saldo_fin_debit FROM turn_sheets INNER JOIN account ON turn_sheets.account_id = account.id UNION SELECT 'Итого:', SUM(turn_credit), SUM(turn_debit), SUM(saldo_beg_credit), SUM(saldo_beg_debit), SUM(saldo_fin_credit), SUM(saldo_fin_debit) FROM turn_sheets ORDER BY 1;");
			}
		});
		button.setBounds(670, 5, 100, 30);
		panel.add(button);
		add(panel, BorderLayout.NORTH);

		pack();
		setTitle("Журнал операций");
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}