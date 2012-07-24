package de.mknoll.thesis.framework.configuration;

import java.util.Map;



/**
 * Class implements object for test configuration
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TestConfiguration {

	/**
	 * Holds yaml test configuration object for this test configuration.
	 */
	@SuppressWarnings("unchecked")
	private Map yamlTestConfiguration;
	
	
	
	/**
	 * Constructor for testConfiguration
	 * 
	 * @param yamlTestConfiguration
	 */
	@SuppressWarnings("unchecked")
	public TestConfiguration(Map yamlTestConfiguration) {
		this.yamlTestConfiguration = yamlTestConfiguration;
	}
	
	
	
	/**
	 * Returns true, if test is activated. Else returns false.
	 * 
	 * @return
	 */
	public Boolean isActivated() {
		if (((Integer)this.yamlTestConfiguration.get("Activated")) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * Returns class name of test class for given test configuration
	 * 
	 * @return
	 */
	public String getTestClassName() {
		return this.getConfigurationForKey("Class");
	}
	
	
	
	/**
	 * Returns postgres dsn of given test configuration
	 * 
	 * @return
	 */
	public String getPostgresDsn() {
		return this.getConfigurationForKey("PostgresDsn");
	}
	
	
	
	/**
	 * Returns postgres username of given test configuration
	 * @return
	 */
	public String getPostgresUser() {
		return this.getConfigurationForKey("PostgresUser");
	}
	
	
	
	/**
	 * Returns postgres password of given test configuration
	 * 
	 * @return
	 */
	public String getPostgresPassword() {
		return this.getConfigurationForKey("PostgresPassword");
	}
	
	
	
	/**
	 * Returns name of recommendations table
	 * 
	 * @return
	 */
	public String getPostgresRecTable() {
		return this.getConfigurationForKey("PostgresRecTable");
	}
	
	
	
	/**
	 * Returns name of descriptions table
	 * 
	 * @return
	 */
	public String getPostgresDescTable() {
		return this.getConfigurationForKey("PostgresDescTable");
	}
	
	
	
	/**
	 * Returns url for neo4j rest service
	 * 
	 * @return
	 */
	public String getNeo4jUrl() {
		return this.getConfigurationForKey("Neo4jUrl");
	}
	
	
	
	/**
	 * Returns yaml configuration map for this test configuration
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getYamlConfiguration() {
		return this.yamlTestConfiguration;
	}
	
	

	/**
	 * Returns yaml setting of graph name in current test configuration
	 * 
	 * @return Graph name of current test configuration
	 */
	public String getGraphName() {
		return (String) this.yamlTestConfiguration.get("Graph");
	}
	
	
	
	private String getConfigurationForKey(String key) {
		return (String) this.yamlTestConfiguration.get(key);
	}
	
}
