package de.mknoll.thesis.datastructures.graph.reader;

import org.mcavallo.opencloud.Tag;
import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.Stemmer;
import de.mknoll.thesis.datastructures.tagcloud.StemmerWrapper;
import de.mknoll.thesis.datastructures.tagcloud.TagExtractor;
import de.mknoll.thesis.datastructures.tagcloud.TagStemMap;
import de.mknoll.thesis.framework.configuration.TestConfiguration;
import de.mknoll.thesis.framework.logger.LoggerInterface;

import java.sql.*;
import java.util.List;
import java.util.Properties;



/**
 * Class implements a graph reader that reads recommendation graph from postgres database.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class PostgresReader implements GraphReader {
	
	/**
	 * Holds logger
	 */
	@Inject private LoggerInterface logger;
	
	
	
	@Inject private IdNodeMap idNodeMap;
	
	
	
	@Inject private TestConfiguration testConfiguration;
	
	
	
	@Inject private TagStemMap tagStemMap;
	
	
	
	@Inject private Stemmer stemmer;
	
	
	
	/**
	 * Holds JDBC connection to database server
	 */
	private Connection postgresqlConnection;
	
	
	
	/**
	 * Holds sql string used for querying database
	 */
	private String sqlQuery;

	

	/**
	 * Initialize graph reader
	 * 
	 * @Override
	 */
	public void init() {
		String url = this.testConfiguration.getPostgresDsn();
		Properties props = new Properties();
		props.setProperty("user", this.testConfiguration.getPostgresUser());
		props.setProperty("password", this.testConfiguration.getPostgresPassword());
		try {
			logger.log("Initializing database with URL: " + url);
			this.postgresqlConnection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			logger.log("Could not initialize PostgresReader. Connection to database could not be established: " + e.getMessage());
		}
		
		this.sqlQuery = this.buildSqlQuery();
	}
	
	
	
	/**
	 * Reads recommendation graph from postgres database with
	 * configuration given in injected testConfiguration
	 * 
	 * @Override
	 */
	public RecommendationGraph read() throws Exception {
		RecommendationGraph g = new RecommendationGraph(this.idNodeMap);
		
		this.init();
		this.insertDbResultsIntoGraph(g);
		
		return g;
	}



	/**
	 * Adds recommendations from given database connection into given recommendation graph
	 * 
	 * @param g Recommendation graph to add recommendations to
	 * @throws Exception 
	 */
	protected void insertDbResultsIntoGraph(RecommendationGraph g) throws Exception {
		Statement st;
		try {
			st = this.postgresqlConnection.createStatement();
			ResultSet rs = st.executeQuery(this.sqlQuery);
			while (rs.next()) {
				//logger.log("Adding recommendation : " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(4));
				RecommenderObject source = new RecommenderObject(rs.getString(1), rs.getString(3), rs.getString(4));
				//logger.log("Adding recommendation : " + rs.getString(2) + " " + rs.getString(5) + " " + rs.getString(6));
				RecommenderObject target = new RecommenderObject(rs.getString(2), rs.getString(5), rs.getString(6));
				
				this.buildTagCloudForRecommenderObject(source);
				this.buildTagCloudForRecommenderObject(target);
				
				// TODO find out, in which column actual weight (quality) of recommendation is stored
				g.addRecommendation(source, target,1);
				
				//System.out.print("Column 1 returned ");
				//System.out.println(rs.getString(1));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			this.logger.log("Error whent trying to read recommendation data from database: " + e.getMessage());
			e.printStackTrace();
		}
	}



	private void buildTagCloudForRecommenderObject(RecommenderObject recommenderObject) {
		TagExtractor extractor = new TagExtractor();	
		DefaultTagCloud cloud = new DefaultTagCloud();
		List<Tag> unstemmedTags = extractor.extractTags(recommenderObject.getDescription());
		StemmerWrapper stemmerWrapper = new StemmerWrapper(this.tagStemMap);
		List<Tag> stemmedTags = stemmerWrapper.stemTags(unstemmedTags);
		recommenderObject.setTags(stemmedTags);
	}



	private String buildSqlQuery() {
		String recommendationsTable = this.testConfiguration.getPostgresRecTable();
		String descriptionsTable = this.testConfiguration.getPostgresDescTable();
		String sqlQuery = "SELECT r.docid, r.docid_rec, docdesc.description, docdesc.isbn12, recdesc.description, recdesc.isbn12 " + 
						  "FROM " + recommendationsTable + " r " +
						  "JOIN " + descriptionsTable + " docdesc ON docdesc.nd = r.docid " +
						  "JOIN " + descriptionsTable + " recdesc ON recdesc.nd = r.docid_rec";
		return sqlQuery;
	}

}
