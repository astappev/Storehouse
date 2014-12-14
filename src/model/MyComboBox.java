package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class MyComboBox extends JPanel {
	private TreeMap<Integer, String> categoryMap = new TreeMap<Integer, String>();
	private JComboBox comboCategory = new JComboBox();

	public MyComboBox(Vector<Vector<Object>> data) {
		for(int i = 0, size = data.size(); i < size; ++i) {
			Integer id = Integer.parseInt(data.get(i).get(0).toString());
			String name = (String) data.get(i).get(1);

			comboCategory.addItem(name);
			categoryMap.put(id, name);
		}

		setLayout(new BorderLayout(0, 0));
		add(comboCategory);
	}

	public void setSelectedItem(Object anObject) {
		if(anObject instanceof Integer) {
			for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
				if (entry.getKey().equals(anObject)) {
					comboCategory.setSelectedItem(entry.getValue());
					return;
				}
			}
		}
		comboCategory.setSelectedItem(anObject);
	}

	private int getKeyForValue(String value) {
		for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	public String toString() {
		return comboCategory.getItemAt(0).toString();
	}

	public ComboItem getSelectedItem() {
		String item = comboCategory.getSelectedItem().toString();
		return new ComboItem(getKeyForValue(item), item);
	}

	public class ComboItem {
		private Integer value;
		private String label;

		public ComboItem(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
		public void setLabel(String label) {
			this.label = label;
		}

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	public void addActionListener(ActionListener l) {
		comboCategory.addActionListener(l);
	}

	public void update(Vector<Vector<Object>> data) {
		comboCategory.removeAllItems();
		categoryMap.clear();
		for(int i = 0, size = data.size(); i < size; ++i) {
			Integer id = Integer.parseInt(data.get(i).get(0).toString());
			String name = (String) data.get(i).get(1);

			comboCategory.addItem(name);
			categoryMap.put(id, name);
		}

		comboCategory.revalidate();
		comboCategory.repaint();
	}
}