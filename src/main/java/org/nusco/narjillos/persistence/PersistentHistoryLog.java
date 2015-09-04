package org.nusco.narjillos.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;

public class PersistentHistoryLog extends PersistentInformation implements HistoryLog {

	public PersistentHistoryLog(String name) {
		super(name);
		createEntriesTable();
	}

	@Override
	public void saveEntries(Experiment experiment) {
		ExperimentHistoryEntry entry = new ExperimentHistoryEntry(experiment);
		if (contains(entry))
			return;
		Statement statement = createStatement();
		try {
	    	String sql = "INSERT INTO HISTORY_ENTRIES (TICKS, RUNNING_TIME, " +
	    				 "NUMBER_OF_NARJILLOS, NUMBER_OF_FOOD_PELLETS, DNA_POOL_SIZE, " +
	    				 "OXYGEN, HYDROGEN, NITROGEN, " + 
	    				 "O2H, O2N, H2O, H2N, N2O, N2H, Z2O, Z2H, Z2N) VALUES (" + 
	    				 entry.ticks + ", " +
	    				 entry.runningTime + ", " +
	    				 entry.numberOfNarjillos + ", " +
	    				 entry.numberOfFoodPellets + ", " +
	    				 entry.dnaPoolSize + ", " +
	    				 entry.oxygen + ", " +
	    				 entry.hydrogen + ", " +
	    				 entry.nitrogen + ", " +
	    				 entry.o2h + ", " +
	    				 entry.o2n + ", " +
	    				 entry.h2o + ", " +
	    				 entry.h2n + ", " +
	    				 entry.n2o + ", " +
	    				 entry.n2h + ", " +
	    				 entry.z2o + ", " +
	    				 entry.z2h + ", " +
	    				 entry.z2n + ");";
	    	statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public ExperimentHistoryEntry getLatestEntry() {
		Statement statement = createStatement();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM HISTORY_ENTRIES WHERE TICKS = (SELECT MAX(TICKS) FROM HISTORY_ENTRIES);");
			if (!rs.next())
				return null;
			return toHistoryEntry(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public List<ExperimentHistoryEntry> getEntries() {
		Statement statement = createStatement();
		try {
			List<ExperimentHistoryEntry> result = new LinkedList<>();
			ResultSet rs = statement.executeQuery("SELECT * FROM HISTORY_ENTRIES ORDER BY TICKS;");
			while (rs.next())
				result.add(toHistoryEntry(rs));
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	@Override
	public void clear() {
		super.clear();
		createEntriesTable();
	}

	private void createEntriesTable() {
		Statement statement = createStatement();
		try {
			String sql = "CREATE TABLE IF NOT EXISTS HISTORY_ENTRIES "
					+ "(TICKS                   INT PRIMARY KEY     NOT NULL,"
					+ " RUNNING_TIME            INT                 NOT NULL,"
					+ " NUMBER_OF_NARJILLOS     INT                 NOT NULL,"
					+ " NUMBER_OF_FOOD_PELLETS  INT                 NOT NULL,"
					+ " DNA_POOL_SIZE           INT                 NOT NULL,"
					+ " OXYGEN                  DOUBLE              NOT NULL,"
					+ " HYDROGEN                DOUBLE              NOT NULL,"
					+ " NITROGEN                DOUBLE              NOT NULL,"
					+ " O2H                     INT                 NOT NULL,"
					+ " O2N                     INT                 NOT NULL,"
					+ " H2O                     INT                 NOT NULL,"
					+ " H2N                     INT                 NOT NULL,"
					+ " N2O                     INT                 NOT NULL,"
					+ " N2H                     INT                 NOT NULL,"
					+ " Z2O                     INT                 NOT NULL,"
					+ " Z2H                     INT                 NOT NULL,"
					+ " Z2N                     INT                 NOT NULL)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private boolean contains(ExperimentHistoryEntry entry) {
		Statement statement = createStatement();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM HISTORY_ENTRIES WHERE TICKS = " + entry.ticks + ";");
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement);
		}
	}

	private ExperimentHistoryEntry toHistoryEntry(ResultSet rs) throws SQLException {
		return new ExperimentHistoryEntry(rs.getInt("TICKS"),
						rs.getInt("RUNNING_TIME"),
						rs.getInt("NUMBER_OF_NARJILLOS"),
						rs.getInt("NUMBER_OF_FOOD_PELLETS"),
						rs.getInt("DNA_POOL_SIZE"),
						rs.getDouble("OXYGEN"),
						rs.getDouble("HYDROGEN"),
						rs.getDouble("NITROGEN"),
						rs.getInt("O2H"),
						rs.getInt("O2N"),
						rs.getInt("H2O"),
						rs.getInt("H2N"),
						rs.getInt("N2O"),
						rs.getInt("N2H"),
						rs.getInt("Z2O"),
						rs.getInt("Z2H"),
						rs.getInt("Z2N"));
	}
}
