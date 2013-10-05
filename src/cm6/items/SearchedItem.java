package cm6.items;

import java.util.ArrayList;
import java.util.List;

public class SearchedItem {

	private String tableName;
	
	private List<Long> ids;

	public SearchedItem(){
		
		this.ids = new ArrayList<Long>();
	}
	
	public String getTableName() {
		return tableName;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
	public void setId(long id) {
		
		this.ids.add(id);
		
	}
	
}//public class SearchedItem
