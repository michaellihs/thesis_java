package de.mknoll.thesis.framework.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Class implements builder for framework configuration
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class ConfigurationBuilder {

	/**
	 * Holds path to configuration file
	 */
	private String configurationPath;
	
	
	
	/**
	 * Holds yaml configuration object
	 */
	private Map yaml;
	

	
	/**
	 * Constructor for configuration builder
	 * 
	 * @param configurationPath Path to configuration file
	 */
	public ConfigurationBuilder(String configurationPath) {
		this.configurationPath = configurationPath;
		this.loadYaml();
	}
	
	
	
	/**
	 * Initializes yaml configuration object
	 */
	private void loadYaml() {
		try {
			InputStream input = new FileInputStream(new File(this.configurationPath));
			Yaml yaml = new Yaml();
			this.yaml = (Map)yaml.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Returns configuration object
	 * 
	 * @return Framework configuration
	 */
	public Configuration buildConfiguration() {
		Configuration configuration = new Configuration();
		
		List tests = (List) this.yaml.get("Tests");
		String loggerClass = (String) this.yaml.get("LoggerClass");
		String baseDirectory = (String) this.yaml.get("BaseDirectory");
		TestSuiteConfiguration testSuiteConfiguration = new TestSuiteConfiguration();
		
		for (Map yamlTestConfiguration : (List<Map>)tests) {
			TestConfiguration testConfiguration = new TestConfiguration(yamlTestConfiguration);
			testSuiteConfiguration.add(testConfiguration);
		}
		
		configuration.setTestSuiteConfiguration(testSuiteConfiguration);
		configuration.setLoggerClass(loggerClass);
		configuration.setBaseDirectory(baseDirectory);
		
		return configuration;
	}

}
