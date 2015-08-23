package org.nusco.narjillos.persistence.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
		Statement statement = connection.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS STATS " +
					 "(TICKS                   INT PRIMARY KEY     NOT NULL," +
					 " RUNNING_TIME            INT                 NOT NULL," +
					 " NUMBER_OF_NARJILLOS     INT                 NOT NULL," +
					 " NUMBER_OF_FOOD_PELLETS  INT                 NOT NULL," +
					 " CURRENT_POOL_SIZE       INT                 NOT NULL," +
					 " HISTORYCAL_POOL_SIZE    INT                 NOT NULL," +
					 " AVERAGE_GENERATION      REAL                NOT NULL," +
					 " OXYGEN                  REAL                NOT NULL," +
					 " HYDROGEN                REAL                NOT NULL," +
					 " NITROGEN                REAL                NOT NULL," +
					 " O2H                     INT                 NOT NULL," +
					 " O2N                     INT                 NOT NULL," +
					 " H2O                     INT                 NOT NULL," +
					 " H2N                     INT                 NOT NULL," +
					 " N2O                     INT                 NOT NULL," +
					 " N2H                     INT                 NOT NULL," +
					 " Z2O                     INT                 NOT NULL," +
					 " Z2H                     INT                 NOT NULL," +
					 " Z2N                     INT                 NOT NULL)";
		statement.executeUpdate(sql);
		statement.close();
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
		//
		// db.getCollection(STATS).insertOne(doc);
	}

	public Stat getLatestStats() {
		// return
		// db.getCollection(STATS).find(exists("ticks")).sort(descending("ticks")).first();
		return null;
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
