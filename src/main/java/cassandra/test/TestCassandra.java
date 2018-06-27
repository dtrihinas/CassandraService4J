package cassandra.test;

import java.util.Arrays;
import java.util.UUID;

import cassandra.cluster.ClusterConnector;
import cassandra.cluster.SessionManager;
import cassandra.config.DBConfigProperties;
import cassandra.model.Entity;


public class TestCassandra {

	public static void main(String[] args) {
		DBConfigProperties config = new DBConfigProperties();
		config.setEndpoints(Arrays.asList("127.0.0.1"));
		config.setKeyspace("mydb");
		config.setFlushdb(false);
		
		ClusterConnector cc =  new ClusterConnector(config);
		cc.connect();
		
		SessionManager sm = new SessionManager(config, cc.getSession());
		sm.init();
		
		Entity e1 = new Entity(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(), "hello");
		Entity e2 = new Entity(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(), "world");
		
		sm.insertEntity(e1);
		sm.insertEntity(e2);
		
		e1 = sm.getEntity(e2.getEntityID());
		System.out.println(e1.getLog());

		cc.close();
		
	}
}
