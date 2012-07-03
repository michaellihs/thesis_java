package de.mknoll.thesis.tests.framework.database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;



/**
 * Class implements some test methods for JDBC connection to postgres.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class PostgresConnectionTest {

	@Test
	public void connectToDatabase() {
		String url = "jdbc:postgresql://localhost/recdata";
		Properties props = new Properties();
		props.setProperty("user","bibtip");
		props.setProperty("password","bibtip");
		try {
			Connection postgresqlConnection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			fail("Connection to database could not be established: " + e.getMessage());
			// e.printStackTrace();
		}
	}
	
}
