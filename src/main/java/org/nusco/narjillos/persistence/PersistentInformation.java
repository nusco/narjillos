package org.nusco.narjillos.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class PersistentInformation {

	private final String name;
	private final Connection connection;

	public PersistentInformation(String name) {
		this.name = name + ".history";
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete() {
		new File(getName()).delete();
	}

	private String getName() {
		return name;
	}

	protected Connection getConnection() {
		return connection;
	}
}