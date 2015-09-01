package scripts;

import java.awt.Point;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.CustomRet_0P;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.Main.State;

public class Shop {

    public static int getSlot(int itemId) {
        if (isShopOpen()) {
            RSItem[] items = getItems();
            for (int i = 0; i < getStockLength(); i++) {
                if (items[i] != null && itemId == items[i].getID())
                    return i;
            }
        }
        return -1;
    }

    public static boolean contains(int id) {
        if (isShopOpen()) {
            for (RSItem r : Interfaces.get(300, 75).getItems()) {
                if (r.getID() == id)
                    return true;
            }
        }
        return false;
    }

    public static int getPrice(int id){
    	
    	int index = getIndex(id);
    	
    	if(index == -1){
    		return -1;
    	}
    	
    	RSItem item = Shop.getItems()[index];
    	
    	if(item != null){
    		return item.getDefinition().getValue();
    	}
    	
    	return -1;
    	
    }
    
    public static int getStock(int id){
    	
    	int index = getIndex(id);
    	
    	if(index == -1){
    		return -1;
    	}
    	
    	RSItem item = Shop.getItems()[index];
    	
    	if(item != null){
    		return item.getStack();
    	}
    	
    	return -1;
    	
    }
    
    public static int getIndex(int id){
    	
    	if (!isShopOpen())
    		return -1;
    		
        for (int i = 0; i < Shop.getItems().length; i++) {
            if (Shop.getItems()[i].getID() == id) {
                return i;
            }
        }
        
		return -1;
    	
    }
    
    public static void massBuy(int id,int amount){
    	
    	int ten = amount / 10;
    	int five = (amount - (ten * 10)) / 5;
    	int one = (amount - (ten * 10) - (five * 5));
    	
    	for(int i = 1;i <= ten;i++){
    		
    		if(Inventory.getAll().length + 10 <= 28){
    			buy(id, 10);
    		}
    		
    	}
    	
    	for(int j = 1;j <= five;j++){
    		
    		if(Inventory.getAll().length + 5 <= 28){
    			buy(id, 5);
    		}
    		
    	}
    	
    	for(int k = 1;k <= one;k++){
    		
    		if(Inventory.getAll().length + 1 <= 28){
    			buy(id, 1);
    		}
    		
    	}
    	
    }
    
    static public void buy(int id,int amount) {
        if (Shop.isShopOpen()) {
        	
        	int index = getIndex(id);
            if (index == -1)
                return;
            int itemX = (int) Math.ceil((index) % 8);
            int itemY = (int) ((Math.floor(index) / 8) % 5);
            int y = (itemY * 45) + 82;
            int x = (itemX * 46) + 97;

            DynamicClicking.clickPoint(new CustomRet_0P<Point>() {
				
				@Override
				public Point ret() {
					return new Point(x, y);
				}
				
			}, 3);
            
            
            
            if (ChooseOption.isOpen() && ChooseOption.isOptionValid("Buy " + amount)) {
            	
                if(ChooseOption.select("Buy " + amount)){
                	
                    Item item = Main.getItem(id);
                	
                    if(item != null){
                    	item.currentlyBought += amount;
                    }
                	
                }
            }
        }
    }

    public static boolean isShopOpen() {
        return Interfaces.get(300, 75) != null;
    }

    public String getShopName() {
        if (Interfaces.get(300, 76) != null)
            return Interfaces.get(300, 76).getText();

        return null;
    }

    public static boolean close() {
        if (Interfaces.get(300, 91) != null) {
            if (Interfaces.get(300, 91).click("Close")) {
                General.sleep(600);
                return true;
            }
        }
        return false;
    }

    static int getStockLength() {
        if (isShopOpen())
            return Interfaces.get(300, 75).getItems().length;

        return 0;
    }

    public static boolean sell(int id, int count) {
        if (isShopOpen()) {
            RSItem[] item = Inventory.find(id);
            if (item.length > 0) {
                if (count == 0 || count > 10) {
                    if (item[0].click("Sell 10")) {
                        General.sleep(200);
                        return true;
                    }
                } else {
                    if (item[0].click("Sell " + count)) {
                        General.sleep(200);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static RSItem[] getItems() {
        if (Interfaces.get(300, 75) != null)
            return Interfaces.get(300, 75).getItems();

        return null;
    }


}
