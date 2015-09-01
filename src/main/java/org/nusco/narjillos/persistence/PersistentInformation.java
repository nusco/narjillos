package org.nusco.narjillos.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class PersistentInformation {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final String name;
	private Connection connection;

	public PersistentInformation(String name) {
		this.name = name + ".history";
		open();
	}

	public void close() {
		try {
			if (!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public final void open() {
		try {
			if (connection != null && !connection.isClosed())
				return;
			connection = DriverManager.getConnection("jdbc:sqlite:" + getName());
		} catch (Exception e) {
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