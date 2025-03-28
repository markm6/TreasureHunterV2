/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private String[] treasures;
    private int gold;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[8]; // only 8 possible items can be stored in kit
        treasures = new String[3]; // 3 required treasures to collect
        gold = startingGold;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    public int getGold() {
        return gold;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (hasItemInKit("sword") && item.equals("sword")){
            System.out.println("SO greedy... YOU ALREADY HAVE ONE");
            return false;
        }
        if (costOfItem < 0){
            System.out.println("We don't sell that here!");
            return false;
        }
        if (hasItemInKit("sword") && gold < costOfItem){
            System.out.println(Colors.YELLOW + "0_0. You know, you don't have enough, but don't sweat it haha... it's on the house" + Colors.RESET);
            addItem(item);
            return true;
        } else if (hasItemInKit("sword") && gold >= costOfItem) {
            System.out.println(Colors.YELLOW + "0_0. Is that a sword. You know what, you can just have it" + Colors.RESET);
            addItem(item);
            return true;
        } else {
            if (gold < costOfItem || hasItemInKit(item)) {
                return false;
            }
            gold -= costOfItem;
            addItem(item);
            return true;
        }
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Attempts to add a treasure to the treasure Array
     * @return If the hunter successfully collected the treasure.
     * False if treasure is in treasures Array already
     * or treasures Array is full, else true.
     */
    public boolean addTreasure(String treasure) {
        int pos = emptyPositionInTreasures();
        if (!hasTreasure(treasure)) {
            treasures[pos] = treasure;
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }



    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the treasure Array has the specified item.
     *
     * @param treasure The search item
     * @return true if the treasure is found.
     */
    public boolean hasTreasure(String treasure) {
        for (String t : treasures) {
            if (treasure.equals(t)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit =Colors.PURPLE + "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item +"," + space;
            }
        }
        printableKit +=Colors.RESET;
        return printableKit;
    }

    /**
     * Returns a printable representation of the hunter's treasures, which
     * is a list of the items in treasures.
     *
     * @return The printable String representation of the treasures Array.
     */
    public String treasuresInfoString() {
        String printableTreasures = "Treasures found: " + Colors.YELLOW + "";
        String space = " ";
        int items = 0;
        for (String item : treasures) {
            if (item != null) {
                items++;
                printableTreasures += "a " + item + "," + space;
            }
        }
        printableTreasures += Colors.RESET;

        if (items == 0) {
            return "Treasures found: none";
        } else {
            return printableTreasures;
        }
    }

    /**
     *
     */
    public boolean allTreasuresCollected() {
        return hasTreasure("trophy") && hasTreasure("crown") && hasTreasure("gem");
    }

    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " +Colors.YELLOW + gold + " gold" +Colors.RESET;
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }
        return str;
    }

    /**
     * Populates kit with all items obtainable from the shop.
     * Should only be called when initiating test mode.
     */
    public void addTestKit() {
        kit = new String[]{"water", "rope", "machete", "horse", "boat", "boots"};
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value in kit.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Finds the first index where there is a null value in treasures.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInTreasures() {
        for (int i = 0; i < treasures.length; i++) {
            if (treasures[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}