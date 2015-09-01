package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import javax.swing.InputVerifier;

import org.tribot.api.DynamicClicking;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.GUI.ActionMode;

/**
 * Created by fsfdsdf on 8/23/2015.
 */
@ScriptManifest(category = "Moneymaking", name = "AIOBuyer", authors = {"Peticca10"})
public class Main extends Script implements Painting{

	public static boolean guiComplete = false;
	public static boolean freeToPlay = false;
	public static boolean worldHop = false;
	public static String npcName;
	public static String npcOption;
	public static Set<Item> items = new HashSet<>();
	protected static boolean openPacks;
	protected static boolean bank;
	RSTile lastNPCPos;
    enum State {
    	NONE,
    	BANK,
        ClICK_SHOPNPC,
        BUYITEMS,
        WORLDHOP
    }

    @Override
    public void run() {
    	
    	new GUI().main(new String[0]);
    	
        while (true){

            setAIAntibanState(true);

            if(guiComplete){
            	
            	System.out.println(state());
            	
            	switch(state()) {
            	case ClICK_SHOPNPC:
            		
            		if(Banking.isBankScreenOpen()){
            			
            			Banking.close();
            			
            		}
            		
            		RSNPC[] npcs = NPCs.find(npcName);
            		
            		if(npcs != null && npcs.length > 0){
            			
            			if(npcs[0].isOnScreen()){
            				if(DynamicClicking.clickRSNPC(npcs[0], npcOption)){
            					lastNPCPos = npcs[0].getPosition();
            					Timing.waitCondition(new Condition() {
									
									@Override
									public boolean active() {
										return Shop.isShopOpen();
									}
								}, 10000);
            				}
            			}else{
            				PathFinding.aStarWalk(npcs[0].getPosition());
            			}
            			
            		}else if (lastNPCPos != null) {
            			WebWalking.walkTo(lastNPCPos);
            		}else{
            			System.out.println("cant find npc");
            		}
            		
            		break;
            	case BUYITEMS:
            		
            		for(Item item : items){
            			
            			int stock = Shop.getStock(item.ID);
            			
            			if(stock > item.lowestStock){
            					
                				if (stock - item.lowestStock > 0) {
    								
                       				RSItem[] items = Inventory.find("Coins");
                    				
                  					int amount = 0;
                					
                					if(stock - item.lowestStock >= 10){
                						amount = 10;
                					}else if(stock - item.lowestStock >= 5){
                						amount = 5;
                					}else{
                						amount = 1;
                					}
                					
                					if(items != null && Shop.getPrice(item.ID) > -1 &&  items[0].getStack() - (Shop.getPrice(item.ID) * amount) > 0){
                						
                						Shop.buy(item.ID, amount);
                        				sleep(400,700);
                						
                					}
                					
    							}
            					
            			}
            			
            		}
            		
            		
            		break;
            	case BANK:
            		
            		if(Shop.isShopOpen()){
               			Shop.close();
            			Timing.waitCondition(new Condition() {
    						
    						@Override
    						public boolean active() {
    							return !Shop.isShopOpen();
    						}
    						
    					}, 10000);
            		}
            		
            		if(openPacks){
            			
            			for(Item item : items){
            				
            				RSItem[] rsItem = Inventory.find(item.ID);
            				
            				if(rsItem != null && rsItem.length > 0){
            				
            					if(rsItem[0].click("Open")){
            						
            	        			Timing.waitCondition(new Condition() {
            							
            							@Override
            							public boolean active() {
            								return Inventory.find(item.ID) == null;
            							}
            							
            						}, 30000);
            	        			
            					}
            					
            				}
            				
            			}
            			
            		}
            		
        			if(bank && Inventory.isFull()){
        				
        				if(Banking.isInBank()){
        					
        					if(!Banking.isBankScreenOpen()){
        						
        						Banking.openBank();
        						
        						Timing.waitCondition(new Condition() {
        							
        							@Override
        							public boolean active() {
        								return Banking.isBankScreenOpen();
        							}
        						}, 10000);
        						
        					}else{
        						Banking.depositAllExcept("Coins");
        					}
        					
        				}else{
        					
        					RSObject[] banks =  Objects.find(50, "bank chest");
        					
        					if(banks != null && banks.length > 0){
        						
        						if(!banks[0].isOnScreen()){
        							PathFinding.aStarWalk(banks[0].getPosition());
        							
        							Timing.waitCondition(new Condition() {
										
										@Override
										public boolean active() {
											return banks[0].isOnScreen();
										}
									}, 10000);
        							
        						}
        						
        						DynamicClicking.clickRSObject(banks[0], "open");
        						
        					}else{WebWalking.walkToBank();}
        					
        				}
        				
        			}
            			
            		
            		break;
            	case WORLDHOP:
            		
            		WorldHopper.changeWorld(WorldHopper.getRandomWorld(!freeToPlay));
            		
            		break;
            	default:
            		state();
            		break;
            	}
            	
            }
            


            sleep(200,400);

        }

    }

    public static Item getItem(int id){
    	
		for(Item item : items){
			
			if(item.ID == id){
				return item;
			}
			
		}
		
		return null;
    }
    
    private boolean checkStocks() {
    	
		for(Item item : items){
			
			int stock = Shop.getStock(item.ID);
			if(stock != -1 && stock > item.lowestStock){
				return true;
			}
			
		}
		
		return false;
    	
	}

	private State state() {
    	
    	if(Login.getLoginState() != Login.STATE.INGAME){
    		return State.NONE;
    	}
    	
    	if(Inventory.isFull()){
    		return State.BANK;
    	}
    	
    	if(!Shop.isShopOpen() && !(Inventory.isFull())){
    		return State.ClICK_SHOPNPC;
    	}
    	
    	if(Shop.isShopOpen() && Inventory.find("Coins") != null && checkStocks()){
    		return State.BUYITEMS;
    	}
    	
    	if(!checkStocks() && worldHop){
    		return State.WORLDHOP;
    	}
    	
		return State.NONE;
    	
    }

	@Override
	public void onPaint(Graphics g) {
		
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(5, 5, 130, 20 + (15 * items.size()));
		g.setColor(new Color(255,255,255));
		g.drawRect(5, 5, 130, 20 + (15 * items.size()));
		g.drawString("Runtime: " + Timing.msToString(getRunningTime()), 7, 20);
		
		int i = 0;
		for(Item item : items){
			g.drawString(item.ID + ": " + item.currentlyBought, 7, 35 + (15 * i));
			i++;
		}
		
	}

}
