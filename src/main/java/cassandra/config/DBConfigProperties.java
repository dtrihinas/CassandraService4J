package cassandra.config;

import java.util.List;

public class DBConfigProperties {
	private static final String DEFAULT_CLUSTER_NAME = "mycluster";
	private static final String DEFAULT_USERNAME = "cassandra";
	private static final String DEFAULT_PASSWD = "cassandra";
	private static final int DEFAULT_TTL = 2592000; //ttl of a month in seconds
	private static final int DEFAULT_PORT = 9042;
	private static final boolean DEFAULT_FLUSH_DB = false;
	private static final long DEFAULT_RECONNECT_TIME = 60000; //10mins in seconds
	
	private List<String> endpoints;
	private String keyspace;
	private String clusterName = DEFAULT_CLUSTER_NAME;
	private String username = DEFAULT_USERNAME;
	private String passwd = DEFAULT_PASSWD;
	private int ttl = DEFAULT_TTL;
	private int port = DEFAULT_PORT;
	private boolean flushdb = DEFAULT_FLUSH_DB;
	private long reconnectTime = DEFAULT_RECONNECT_TIME;
	
	public List<String> getEndpoints() {
		return endpoints;
	}
	
	public void setEndpoints(List<String> endpoints) {
		this.endpoints = endpoints;
	}
	
	public boolean isFlushdb() {
		return flushdb;
	}
	
	public void setFlushdb(boolean flushdb) {
		this.flushdb = flushdb;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getReconnectTime() {
		return reconnectTime;
	}

	public void setReconnectTime(long reconnectTime) {
		this.reconnectTime = reconnectTime;
	}
}