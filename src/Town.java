/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String treasure;
    private boolean treasureSearched;
    private boolean goldDug;
    private boolean mode;
    private boolean itemsDoNotBreak;
    private boolean secretMode;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean itemsDoNotBreak, boolean secretMode) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.itemsDoNotBreak = itemsDoNotBreak;
        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";
        this.secretMode = secretMode;

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        goldDug = false;
        treasureSearched = false;

        // 25% chance for a given treasure to be chosen.
        int treasureNum = ((int) (Math.random() * 4));
        if (treasureNum == 0) {
            treasure = "crown";
        } else if (treasureNum == 1) {
            treasure = "trophy";
        } else if (treasureNum == 2) {
            treasure = "gem";
        } else {
            treasure = "dust";
        }
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    public void huntForTreasure() {
        if (treasureSearched) {
            printMessage = "You have already searched this town.";
        } else {
            if (treasure.equals("dust")) {
                printMessage = "You found dust. It was not added to your treasures.";
            } else {
                if (hunter.addTreasure(treasure)) {
                    printMessage = "You found " + treasure + " and it was added to your treasures!";
                } else {
                    printMessage = "You found " + treasure + " but it was already in your inventory of treasures.";
                }
            }
            treasureSearched = true;
        }
    }

    public void digForGold() {
        if (!hunter.hasItemInKit("shovel")) {
            printMessage = "You can't dig for gold without a shovel.";
        } else if (goldDug) {
            printMessage = "You already dug for gold in this town.";
        } else {
            double chance = Math.random();
            if (chance < 0.5) {
                int goldFound = (int) (Math.random() * 20) + 1;
                printMessage = "You dug up " + goldFound + " gold!";
                hunter.changeGold(goldFound);
            } else {
                printMessage = "You dug but only found dirt.";
            }
            goldDug = true;
        }

    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak() && !itemsDoNotBreak) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage =Colors.RED+ "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("sword")){
                printMessage += Colors.CYAN + "IS THAT A SWORD. Never bring fists to a sword fight 😭" + Colors.RESET;
                printMessage += Colors.RED + "\nYou won the brawl and receive " +Colors.YELLOW + goldDiff + " gold." +Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                if (Math.random() > noTroubleChance) {
                    printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < (1.0/6)) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < (2.0/6)) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < (3.0/6)) {
            return new Terrain( "Plains", "Horse");
        } else if (rnd < (4.0/6)) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 5.0/6){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}