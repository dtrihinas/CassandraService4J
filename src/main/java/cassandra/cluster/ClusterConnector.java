package cassandra.cluster;

import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AuthenticationException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.policies.Policies;

import cassandra.config.DBConfigProperties;

public class ClusterConnector implements IDBConnector {

	private Cluster cluster;
	private Session session;	
	
	private DBConfigProperties config;	
	
	private List<String> endpoints;
	private String clustername;
	private String keyspace;
	private String username;
	private String passwd;
	private int port;
	private long reconnectTime;
	
	public ClusterConnector(DBConfigProperties config) {
		this.config = config;
		this.parseConfig();
	}
	
	private void parseConfig() {
		this.endpoints = this.config.getEndpoints();
		this.port = this.config.getPort();
		this.clustername = this.config.getClusterName();
		this.keyspace = this.config.getKeyspace();
		this.reconnectTime = this.config.getReconnectTime(); 
		this.username = this.config.getUsername();
		this.passwd = this.config.getPasswd();
	}
	
	public void connect() {
		boolean conn = false;
		int t=0, interval = 20000;
		while (conn == false && t < this.reconnectTime) {
			try {
				try2connect();
			}
			catch(NoHostAvailableException e) {
				//TODO log event
				System.out.println("No Database backend available, retry to connect in " + interval/1000 + " seconds");
				
				t += interval;
				try {
					Thread.sleep(interval);
				} 
				catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue;
			}
			catch(AuthenticationException e) {
				System.out.println("Cannot authenticate connection... closing connection...");
			}
			
			conn = true;
		}
		
		//if there is still no connection after max reconnect time, try again and this time don't catch the error
		//TODO throw exception
		if (conn == false)
			this.try2connect();
	}

	private void try2connect() {
		//connect to cluster
		
		this.cluster = Cluster.builder()
				.addContactPoints(this.endpoints.toArray(new String[this.endpoints.size()]))
				.withPort(this.port)
				.withCredentials(this.username, this.passwd)
				.withRetryPolicy(Policies.defaultRetryPolicy())
				.withReconnectionPolicy(Policies.defaultReconnectionPolicy())
				.withClusterName(this.clustername)
				.build();
		
		System.out.println("Successfully connected to cluster: " + this.cluster.getMetadata().getClusterName());
	
		//lookup keyspace
		boolean found = false;
		for(KeyspaceMetadata k : cluster.getMetadata().getKeyspaces())
			if (keyspace.equals(k.getName()))
				found = true;
		
		if (!found) {
			//establish a driver session with the cluster
			session = cluster.connect(); 
			String cql = "CREATE KEYSPACE "+ keyspace +" WITH " + 
					     "replication = {'class':'SimpleStrategy', 'replication_factor' : 3}";
			//create new keyspace
			session.execute(cql); 
			
			System.out.println("Successfully created new keyspace: " + keyspace);
		}
		session = cluster.connect(keyspace);
		
		System.out.println("Successfully connected and created a new driver session");
	}
	
	public void close() {
		this.cluster.close();
		System.out.println("Closing connection to cluster...");
	}
	
	public Session getSession() {
		return this.session;
	}
}