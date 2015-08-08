package org.nusco.narjillos.persistence.db;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Sorts.descending;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

	private static final String GENEPOOL = "genepool";
	private static final String STATS = "stats";

	private final String name;
	private final MongoClient mongoClient;
	private final MongoDatabase db;

	public Database(String name) {
		this.name = name.replaceAll("\\.", "_");
		configureLogging();
		mongoClient = new MongoClient(Configuration.DATABASE_HOST, Configuration.DATABASE_PORT);
		db = mongoClient.getDatabase(name);
	}

	private void configureLogging() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
	}

	Mongo getMongoClient() {
		return mongoClient;
	}

	public void close() {
		mongoClient.close();
	}

	public void newDNA(DNA dna) {
		Document doc = new Document()
			.append("id", dna.getId())
			.append("genes", dna.toString());
		
		MongoCollection<Document> collection = db.getCollection(GENEPOOL);
		collection.insertOne(doc);
	}

	public DNA getDNA(int dnaId) {
		MongoCollection<Document> collection = db.getCollection(GENEPOOL);
		Document doc = collection.find(eq("id", dnaId)).first();
		if (doc == null)
			return null;
		
		return new DNA(doc.getLong("id"), doc.getString("genes"));
	}

	public void updateStatsFor(Experiment experiment) {
		Document doc = new Document()
			.append("ticks", experiment.getTicksChronometer().getTotalTicks())
			.append("runningTime", experiment.getTotalRunningTimeInSeconds())
			.append("numberOfNarjillos", experiment.getEcosystem().getNumberOfNarjillos())
			.append("numberOfFoodPieces", experiment.getEcosystem().getNumberOfFoodPieces())
			.append("currentPoolSize", experiment.getGenePool().getCurrentPool().size())
			.append("historicalPoolSize", experiment.getGenePool().getHistoricalPool().size())
			.append("averageGeneration", experiment.getGenePool().getAverageGeneration())
			.append("oxygen", experiment.getEcosystem().getAtmosphere().getDensityOf(OXYGEN))
			.append("hydrogen", experiment.getEcosystem().getAtmosphere().getDensityOf(HYDROGEN))
			.append("nitrogen", experiment.getEcosystem().getAtmosphere().getDensityOf(NITROGEN));

		Map<String, Integer> chemicalCycles = getChemicalCycles(experiment.getEcosystem());
		for (String chemicalCycle : Element.CYCLES)
			doc.append(chemicalCycle, chemicalCycles.get(chemicalCycle));
		
		db.getCollection(STATS).insertOne(doc);
	}

	public Map<String, Integer> getChemicalCycles(Ecosystem ecosystem) {
		Map<String, Integer> result = new LinkedHashMap<>();
		
		for (String chemicalCycle : Element.CYCLES)
			result.put(chemicalCycle, 0);

		for (Narjillo narjillo : ecosystem.getNarjillos()) {
			String cycle = "" + narjillo.getBreathedElement() + "->" + narjillo.getByproduct();
			result.put(cycle, result.get(cycle) + 1);
		}
		
		return result;
	}

	public Document getLatestStats() {
		return db.getCollection(STATS).find(exists("ticks")).sort(descending("ticks")).first();
	}

	public void printHistory() {
		// FIXME Auto-generated method stub
	}

	public String getName() {
		return name;
	}
}
