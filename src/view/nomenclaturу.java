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

public class nomenclaturу extends JFrame {
	private Model model;
	private DefaultTableModel dataModel;
	private String sqlToTable = "SELECT id,name,purchase_price,sale_price,rate,notes FROM nomenclature;";

	public nomenclaturу(Model m) {
		this.model = m;
		initUI();

		this.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				model.updateDataModel(dataModel, sqlToTable);
			}
		});
	}

	public void initUI() {
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("id");
		columnNames.add("Название");
		columnNames.add("Цена покупки");
		columnNames.add("Цена продажи");
		columnNames.add("Сорт");
		columnNames.add("Примечание");

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
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					addNomenclature addWindow = new addNomenclature(model, Integer.parseInt(table.getValueAt(row, 0).toString()));
					addWindow.setVisible(true);
				}
			}
		});
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

		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNomenclature addWindow = new addNomenclature(model);
				addWindow.setVisible(true);
			}
		});
		add(addButton, BorderLayout.SOUTH);

		pack();
		setTitle("Номенклатура товара");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}