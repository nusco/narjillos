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
		if (contains(dna))
			return;
		Statement statement = createStatement();
		try {
			String sql = "INSERT INTO DNA (ID, GENES, PARENT_ID, IS_DEAD) VALUES (" +
				dna.getId() + ", " +
				"'" + dna + "', " +
				dna.getParentId() + ", 0);";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public DNA getDna(long id) {
		Statement statement = createStatement();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM DNA WHERE ID = " + id + ";");
			if (!rs.next())
				return null;
			return toDNA(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public void markAsDead(long id) {
		Statement statement = createStatement();
		try {
			statement.executeUpdate("UPDATE DNA SET IS_DEAD = 1 WHERE ID = " + id + ";");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public List<DNA> getAllDna() {
		Statement statement = createStatement();
		try {
			List<DNA> result = new LinkedList<>();
			ResultSet rs = statement.executeQuery("SELECT * FROM DNA ORDER BY ID;");
			while (rs.next())
				result.add(toDNA(rs));
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public List<DNA> getLiveDna() {
		Statement statement = createStatement();
		try {
			List<DNA> result = new LinkedList<>();
			ResultSet rs = statement.executeQuery("SELECT * FROM DNA WHERE IS_DEAD = 0 ORDER BY ID;");
			while (rs.next())
				result.add(toDNA(rs));
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private void createDnaTable() {
		Statement statement = createStatement();
		try {
			String sql = "CREATE TABLE IF NOT EXISTS DNA "
				+ "(ID                   INT PRIMARY KEY     NOT NULL,"
				+ " GENES                STRING              NOT NULL,"
				+ " PARENT_ID            INT                 NOT NULL,"
				+ " IS_DEAD              INT                 NOT NULL)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private boolean contains(DNA dna) {
		return getDna(dna.getId()) != null;
	}

	private DNA toDNA(ResultSet rs) throws SQLException {
		return new DNA(rs.getLong("ID"),
			rs.getString("GENES"),
			rs.getInt("PARENT_ID"));
	}
}
