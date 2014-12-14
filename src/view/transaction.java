package view;

import model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.Vector;

public class transaction extends JFrame {
	private Model model;
	private Integer operation_id = null;
	private DefaultTableModel dataModel;
	private String sqlToTable = "SELECT transaction_log.id, transaction_log.operation_id, documentt.name, accountd.name, accountc.name, transaction_log.total\n" +
			"FROM transaction_log, transaction_type, account AS accountd, account AS accountc, document_type AS documentt\n" +
			"WHERE transaction_type.account_debit = accountd.id AND transaction_type.account_credit = accountc.id AND documentt.id = transaction_type.document_type AND transaction_log.transaction_type = transaction_type.id;";

	public transaction(Model m) {
		this.model = m;
		initUI(sqlToTable);

		this.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				model.updateDataModel(dataModel, sqlToTable);
			}
		});
	}

	public transaction(Model m, int op) {
		this.operation_id = op;
		this.model = m;
		initUI("SELECT transaction_log.id, transaction_log.operation_id, documentt.name, accountd.name, accountc.name, transaction_log.total\n" +
				"FROM transaction_log, transaction_type, account AS accountd, account AS accountc, document_type AS documentt\n" +
				"WHERE transaction_type.account_debit = accountd.id AND transaction_type.account_credit = accountc.id AND documentt.id = transaction_type.document_type AND transaction_log.transaction_type = transaction_type.id AND transaction_log.operation_id = " + operation_id + ";");
	}

	public void initUI(String sql) {
		System.out.println(operation_id);
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Номер");
		columnNames.add("Номер операции");
		columnNames.add("Тип документа");
		columnNames.add("Счет дебит");
		columnNames.add("Счет кредит");
		columnNames.add("Сумма проводки");

		Vector<Vector<Object>> data = this.model.select_table(sql);
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

		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Поиск:");
		panel.add(label, BorderLayout.WEST);
		final JTextField filterText = new JTextField();
		panel.add(filterText, BorderLayout.CENTER);
		JButton button = new JButton("Искать");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = filterText.getText();
				if (text.length() == 0) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter(text));
				}
			}
		});
		panel.add(button, BorderLayout.EAST);
		add(panel, BorderLayout.NORTH);

		if(operation_id != null) {
			JButton addButton = new JButton("Удалить");
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int dialogResult = JOptionPane.showConfirmDialog(null, "Вы действительно хотите удалить операцию?", "Запрос на удаление", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) {
						if(!model.execute("DELETE FROM operation_log WHERE id = " + operation_id + ";")) {
							JOptionPane.showMessageDialog(null, "Произошла ошибка при удалении операции!");
							return;
						}
						JOptionPane.showMessageDialog(null, "Операция удалена!");
						dispose();
					}
				}
			});
			add(addButton, BorderLayout.SOUTH);
		}

		pack();
		setTitle("Журнал проводок");
		if(operation_id != null)
			setSize(600, 200);
		else
			setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}