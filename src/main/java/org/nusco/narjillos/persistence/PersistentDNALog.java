package org.nusco.narjillos.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

public class PersistentDNALog extends PersistentInformation implements DNALog {

	public PersistentDNALog(String name) {
		super(name);
		createDnaTable();
	}

	@Override
	public void save(DNA dna) {
	    try {
			if (contains(dna))
				return;
	    	Statement stmt = getConnection().createStatement();
	    	String sql = "INSERT INTO DNA (ID, GENES, PARENT_ID) VALUES (" + 
	    				 dna.getId() + ", " +
	    				 "'" + dna.toString() + "', " +
	    				 dna.getParentId() + ");";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DNA getDNA(long id) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM DNA WHERE ID = " + id + ";");
			if (!rs.next())
				return null;
			return toDNA(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DNA> getAllDNA() {
		try {
			List<DNA> result = new LinkedList<>();
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM DNA ORDER BY ID;");
			while (rs.next())
				result.add(toDNA(rs));
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createDnaTable() {
		try {
			Statement stmt = getConnection().createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS DNA "
					+ "(ID                   INT PRIMARY KEY     NOT NULL,"
					+ " GENES                STRING              NOT NULL,"
					+ " PARENT_ID            INT                 NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean contains(DNA dna) {
		return getDNA(dna.getId()) != null;
	}

	private DNA toDNA(ResultSet rs) throws SQLException {
		return new DNA(rs.getInt("ID"),
					   rs.getString("GENES"),
					   rs.getInt("PARENT_ID"));
	}
}
