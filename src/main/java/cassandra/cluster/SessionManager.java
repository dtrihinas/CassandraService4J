package cassandra.cluster;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.UUIDs;

import cassandra.config.DBConfigProperties;
import cassandra.model.Entity;

public class SessionManager {
	//schema
	private static final String CREATE_ENTITY_TABLE = "CREATE TABLE entity_table (" + 
			 										  " entityID varchar," +
			 										  " createdAt timeuuid," +
			 										  " log varchar," +
			 										  " PRIMARY KEY (entityID)" +
			 										  ");";
	private static final String DROP_ENTITY_TABLE = "DROP TABLE IF EXISTS entity_table";

	//queries
	private static final String INSERT_ENTITY = "INSERT INTO entity_table (entityID, createdAt, log) VALUES (?,?,?)";
	private static final String GET_ENTITY = "SELECT * FROM entity_table WHERE entityID=?";
	
	//prepared statements
	private PreparedStatement insertEntityStmt;
	private PreparedStatement getEntityStmt;
	
	private DBConfigProperties config;
	private Session session;
	
	public SessionManager(DBConfigProperties config, Session session) {
		this.config = config;
		this.session = session;
	}
	
	public void init() {
		if (config.isFlushdb()) {
			Statement s = new SimpleStatement(DROP_ENTITY_TABLE); 
			this.session.execute(s);
			
			System.out.println("Flushing schema...");
			
			s = new SimpleStatement(CREATE_ENTITY_TABLE); 
			this.session.execute(s);
			
			System.out.println("Updated schema...");
		}
		
		//prepare query structures
		this.insertEntityStmt = session.prepare(INSERT_ENTITY);
		this.getEntityStmt = session.prepare(GET_ENTITY);
		
		System.out.println("Schema ready to accept queries...");
	}
	
	public void insertEntity(Entity entity) {
		try{
			BoundStatement bs = this.insertEntityStmt.bind();
			
			bs.setString("entityID", entity.getEntityID());
			bs.setUUID("createdAt", UUIDs.endOf(entity.getCreatedAt()));
			bs.setString("log", entity.getLog());
			
			session.execute(bs);
		}
		catch(Exception e) {
		    e.printStackTrace();
		}	
	}
	
	public Entity getEntity(String entityID) {
		try{

			BoundStatement bs = this.getEntityStmt.bind();
			
			bs.setString("entityID", entityID);
			
			ResultSet rs = session.execute(bs);		
	        Row row = rs.one();
	        if (row != null)
	        	return new Entity(row.getString("entityID"), 
	        					  UUIDs.unixTimestamp(row.getUUID("createdAt")),
	        					  row.getString("log")
	        					 );
		}
		catch(Exception e) {
		    e.printStackTrace();
		}	
		return null;
	}
}
