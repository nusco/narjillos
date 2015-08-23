package org.nusco.narjillos.persistence.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;
import org.nusco.narjillos.genomics.DNA;

public class Database {

	private final String name;
	private final Connection connection;

	public Database(String name) {
		this.name = name + ".history";
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + getName());
			createTables();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void createTables() throws SQLException {
		Statement stmt = connection.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS STATS "
				+ "(TICKS                   INT PRIMARY KEY     NOT NULL,"
				+ " RUNNING_TIME            INT                 NOT NULL,"
				+ " NUMBER_OF_NARJILLOS     INT                 NOT NULL,"
				+ " NUMBER_OF_FOOD_PELLETS  INT                 NOT NULL,"
				+ " CURRENT_POOL_SIZE       INT                 NOT NULL,"
				+ " HISTORYCAL_POOL_SIZE    INT                 NOT NULL,"
				+ " AVERAGE_GENERATION      DOUBLE              NOT NULL,"
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
		stmt.executeUpdate(sql);
		stmt.close();
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void newDNA(DNA dna) {
		// Document doc = new Document()
		// .append("id", dna.getId())
		// .append("genes", dna.toString());
		//
		// MongoCollection<Document> collection = db.getCollection(GENEPOOL);
		// collection.insertOne(doc);
	}

	public DNA getDNA(int dnaId) {
		// MongoCollection<Document> collection = db.getCollection(GENEPOOL);
		// Document doc = collection.find(eq("id", dnaId)).first();
		// if (doc == null)
		// return null;

		// return new DNA(doc.getLong("id"), doc.getString("genes"));
		return null;
	}

	public void updateStatsOf(Experiment experiment) {
		Stat stats = new Stat(experiment);
	    try {
	    	Statement stmt = connection.createStatement();
	    	String sql = "INSERT INTO STATS (TICKS, RUNNING_TIME, " +
	    				 "NUMBER_OF_NARJILLOS, NUMBER_OF_FOOD_PELLETS, " +
	    				 "CURRENT_POOL_SIZE, HISTORYCAL_POOL_SIZE, AVERAGE_GENERATION, " +
	    				 "OXYGEN, HYDROGEN, NITROGEN, " + 
	    				 "O2H, O2N, H2O, H2N, N2O, N2H, Z2O, Z2H, Z2N) VALUES (" + 
	    				 stats.ticks + ", " +
	    				 stats.runningTime + ", " +
	    				 stats.numberOfNarjillos + ", " +
	    				 stats.numberOfFoodPellets + ", " +
	    				 stats.currentPoolSize + ", " +
	    				 stats.historicalPoolSize + ", " +
	    				 stats.averageGeneration + ", " +
	    				 stats.oxygen + ", " +
	    				 stats.hydrogen + ", " +
	    				 stats.nitrogen + ", " +
	    				 stats.o2h + ", " +
	    				 stats.o2n + ", " +
	    				 stats.h2o + ", " +
	    				 stats.h2n + ", " +
	    				 stats.n2o + ", " +
	    				 stats.n2h + ", " +
	    				 stats.z2o + ", " +
	    				 stats.z2h + ", " +
	    				 stats.z2n + ");"; 
	    	System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Stat getLatestStats() {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM STATS WHERE TICKS = (SELECT MAX(TICKS) FROM STATS);");
			if (!rs.next())
				return null;

			return toStat(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Stat toStat(ResultSet rs) throws SQLException {
		return new Stat(rs.getInt("TICKS"),
						rs.getInt("RUNNING_TIME"),
						rs.getInt("NUMBER_OF_NARJILLOS"),
						rs.getInt("NUMBER_OF_FOOD_PELLETS"),
						rs.getInt("CURRENT_POOL_SIZE"),
						rs.getInt("HISTORYCAL_POOL_SIZE"),
						rs.getDouble("AVERAGE_GENERATION"),
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

	public void printHistory() {
		// FIXME Auto-generated method stub
	}

	public String getName() {
		return name;
	}

	void delete() {
		new File(getName()).delete();
	}
}
