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

	public int insert(String sql) {
		Integer id = 0;
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();


			if ( rs.next() ) {
				id = rs.getInt(1);
			}
			stmt.close();
			//c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		if(id != 0) return id;
		return 0;
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

	public Vector<Object> select(String sql) {
		Vector<Object> vector = new Vector<Object>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
			}

			rs.close();
			stmt.close();
			//c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return vector;
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
