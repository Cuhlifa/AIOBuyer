package scripts;

import scripts.GUI.ActionMode;

public class Item {

	int ID;
	int lowestStock;
	public int currentlyBought;
	
	public Item(int ID, int lowestStock) {
		this.ID = ID;
		this.lowestStock = lowestStock;
	}
	
}
