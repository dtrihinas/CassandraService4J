package cassandra.model;

public class Entity {

	private String entityID;
	private long createdAt;
	private String log;
	
	public Entity(String entityID, long createdAt, String log) {
		this.entityID = entityID;
		this.createdAt = createdAt;
		this.log = log;
	}

	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}