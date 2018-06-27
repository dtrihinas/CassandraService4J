package cassandra.cluster;

import com.datastax.driver.core.Session;

public interface IDBConnector {

	public void connect();
	public void close();
	
	public Session getSession();
}
