package org.nusco.narjillos.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.persistence.serialization.JSON;

public class ExperimentLog extends PersistentInformation {

	public ExperimentLog(String name) {
		super(name);
		createExperimentTable();
	}

	public void save(Experiment experiment) {
    	Statement statement = createStatement();
	    try {
	    	String sql = "INSERT INTO EXPERIMENT (JSON) VALUES ('" + JSON.toJson(experiment, Experiment.class) + "');";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	    cleanOldExperiments();
	}

	public Experiment load() {
		return JSON.fromJson(getBlob(), Experiment.class);
	}

	private void createExperimentTable() {
		Statement statement = createStatement();
		try {
			String sql = "CREATE TABLE IF NOT EXISTS EXPERIMENT "
					+ "(ID        INTEGER	PRIMARY KEY AUTOINCREMENT,"
					+ " JSON      BLOB      NOT NULL)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private String getBlob() {
		Statement statement = createStatement();
		try {
			// If there is more than one experiment, then some save
			// operation failed before completing (because save() cleans
			// up old experiments on exit). So load the oldest one first,
			// which should always be valid.
			ResultSet rs = statement.executeQuery("SELECT * FROM EXPERIMENT ORDER BY ID;");
			rs.next();
			return rs.getString("JSON");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private void cleanOldExperiments() {
		Statement statement = createStatement();
		try {
			statement.executeUpdate("DELETE FROM EXPERIMENT WHERE ID < " + getLatestId() + ";");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private long getLatestId() {
		Statement statement = createStatement();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM EXPERIMENT ORDER BY ID DESC;");
			rs.next();
			return rs.getLong("ID");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}
}
