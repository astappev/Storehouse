package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class Model {
	private Connection c = null;

	public Model() {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/labs", "lab_user", "root");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public boolean execute(String sql) {
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			//c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
			//System.exit(0);
		}
		return true;
	}

	public void execute_all(String[] sql) {
		try {
			c.setAutoCommit(false);
			Statement stmt = c.createStatement();
			for (int i = sql.length - 1; i >= 0; --i) {
				stmt.executeUpdate(sql[i]);
			}
			stmt.close();
			c.commit();
			c.close();
			c.setAutoCommit(true);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public Vector<Vector<Object>> select_table(String sql) {
		Vector<Vector<Object>> data = null;

		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
			//Vector<String> columnNames = new Vector<String>();
			int columnCount = metaData.getColumnCount();
			/*for (int column = 1; column <= columnCount; column++) {
				columnNames.add(metaData.getColumnName(column));
            }*/

			// data of the table
			data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}

			rs.close();
			stmt.close();
			//c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return data;
	}

	public void select(String sql) {
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.print(rs);
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String address = rs.getString("address");
				float salary = rs.getFloat("salary");
				System.out.println("ID = " + id);
				System.out.println("NAME = " + name);
				System.out.println("AGE = " + age);
				System.out.println("ADDRESS = " + address);
				System.out.println("SALARY = " + salary);
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void updateDataModel(DefaultTableModel dataModel, String sql) {
		Vector<Vector<Object>> data = this.select_table(sql);
		dataModel.setRowCount(0);
		for(int i = 0; i < data.size(); ++i) {
			dataModel.addRow(data.get(i));
		}
		dataModel.fireTableDataChanged();
	}
}
