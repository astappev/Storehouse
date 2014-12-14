package view;

import model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Vector;

public class transactionType extends JFrame {
	private Model model;
	private DefaultTableModel dataModel;
	private String sqlToTable = "SELECT accountd.name, accountc.name, documentt.name, agentt.name\n" +
			"  FROM transaction_type, account AS accountd, account AS accountc, document_type AS documentt, agent_type AS agentt\n" +
			"  WHERE transaction_type.account_debit = accountd.id AND transaction_type.account_credit = accountc.id AND documentt.id = transaction_type.document_type AND agentt.id = documentt.agent_type;";

	public transactionType(Model m) {
		this.model = m;
		initUI();
	}

	public void initUI() {
		Vector<String> columnNames = new Vector<String>();
		//columnNames.add("id");
		columnNames.add("Счет Дебит");
		columnNames.add("Счет Кредит");
		columnNames.add("Тип документа");
		columnNames.add("Тип контрагента");

		Vector<Vector<Object>> data = this.model.select_table(sqlToTable);
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

		pack();
		setTitle("План счетов");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}