package cm6.items;

public class BM {

	long	position;
	String	title;
	String	memo;
	long	aiId;
	String	aiTableName;
	
	long	dbId;
	
	public BM() {
		
	}


	
	public BM(Builder builder) {
		// TODO Auto-generated constructor stub
		position = builder.position;
		title = builder.title;
		memo = builder.memo;
		aiId = builder.aiId;
		aiTableName = builder.aiTableName;
		
		dbId = builder.dbId;

	}//public BM(Builder builder)



	public long getPosition() {
		return position;
	}

	public String getTitle() {
		return title;
	}

	public String getMemo() {
		return memo;
	}

	public long getAiId() {
		return aiId;
	}

	public String getAiTableName() {
		return aiTableName;
	}

	public void setPosition(long point) {
		this.position = point;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setAiId(long aiId) {
		this.aiId = aiId;
	}

	public void setAiTableName(String aiTableName) {
		this.aiTableName = aiTableName;
	}
	
	public static class Builder {

		private long	position;
		private String	title;
		private String	memo;
		private long	aiId;
		private String	aiTableName;
		private long	dbId;
		
		
		public BM build() {
			return new BM(this);
		}



		public Builder setPosition(long val) {
			position = val;	return this;
		}



		public Builder setTitle(String val) {
			title = val;	return this;
		}



		public Builder setMemo(String val) {
			memo = val;	return this;
		}



		public Builder setAiId(long val) {
			aiId = val;	return this;
		}



		public Builder setAiTableName(String val) {
			this.aiTableName = val;	return this;
		}

		public Builder setDbId(long val) {
			this.dbId = val;	return this;
		}

		
	}//public static class Builder

	public long getDbId() {
		return dbId;
	}



	public void setDbId(long dbId) {
		this.dbId = dbId;
	}
	
	
}//public class BM
