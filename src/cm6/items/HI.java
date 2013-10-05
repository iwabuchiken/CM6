package cm6.items;


public class HI {

	long dbId;
	long createdAt;
	long modifiedAt;
	long aiId;
	String aiTableName;
	
	public HI(Builder builder) {
//
		dbId = builder.dbId;
		createdAt = builder.createdAt;
		modifiedAt = builder.modifiedAt;
		aiId = builder.aiId;
		aiTableName = builder.aiTableName;

	}//public HI(Builder builder)
	
	
	public long getDbId() {
		return dbId;
	}


	public long getCreatedAt() {
		return createdAt;
	}


	public long getModifiedAt() {
		return modifiedAt;
	}


	public long getAiId() {
		return aiId;
	}


	public String getAiTableName() {
		return aiTableName;
	}


	public void setDbId(long dbId) {
		this.dbId = dbId;
	}


	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}


	public void setAiId(long aiId) {
		this.aiId = aiId;
	}


	public void setAiTableName(String aiTableName) {
		this.aiTableName = aiTableName;
	}


	public static class Builder {

		private long dbId;
		private long createdAt;
		private long modifiedAt;
		private long aiId;
		private String aiTableName;
		
		public HI build() {
			
			return new HI(this);
		}

		public Builder setAiId(long val) {
			
			aiId = val;	return this;
			
		}

		public Builder setAiTableName(String val) {
			
			aiTableName = val;	return this;
			
		}

		
		public Builder setModifiedAt(long val) {
			
			modifiedAt = val;	return this;
			
		}

		public Builder setCreatedAt(long val) {
			
			createdAt = val;	return this;
			
		}

		public Builder setDbId(long val) {
			
			dbId = val;	return this;
			
		}
		
		
		
	}//public static class Builder
	
}//public class HI
