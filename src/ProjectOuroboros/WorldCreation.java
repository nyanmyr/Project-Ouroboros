package ProjectOuroboros;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.JLabel;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class WorldCreation extends javax.swing.JFrame {

    //    <editor-fold desc="variables">
    // gets the screen size
    Dimension screenSize;
    int screenWidth;
    int screenHeight;

    // these variable comments may be outdated
    // 5 (30s to 1m), 5 (15s to 45s), 3 (5s to 20s)
    // larger maps = longer generation time
    // more population = longer generation time
    // 5 massive: 61 / 33
    // 4 large: 49 / 27
    // 3 medium: 37 / 21
    int mapSize = 3;
    int xMax;
    int yMax;

    // 12 = rare
    // 8 = normal
    // 4 = common
    int mountainSpawnRate;
    int lakeSpawnRate;
    int forestSpawnRate;

    // 3 = rare
    // 2 = normal
    // 1 = common
    int riverSources;
    int forestSources;
    int hillBranches;
    int forestBranches;

    // 3 overpopulated
    // 2 normal
    // 1 underpopulated
    float population = 1;
    int cities;
    int towns;
    int villages;

    int[] xMaxArray = new int[xMax + 1];
    int[] yMaxArray = new int[yMax + 1];
    int coordinatesX = xMaxArray.length / 2;
    int coordinatesY = yMaxArray.length / 2;

    HashMap<String, JLabel> backgroundTileMap = new HashMap<>();
    HashMap<String, JLabel> tileMap = new HashMap<>();
    HashMap<String, JLabel> riverTileMap = new HashMap<>();
    HashMap<String, JLabel> settlementsTileMap = new HashMap<>();
    HashMap<String, JLabel> settlementsNameTileMap = new HashMap<>();

    Random randomizer = new Random();

    int tileScale;
    float zoomScale = 2;
    float zoomScaleMax = 3;
    float zoomScaleSensitivity = 0.25f;

    // used to select the northern/ southern/ western/ eastern tiles of a selected tile
    int[][] pairs = {
        {0, -1},
        {0, 1},
        {-1, 0},
        {1, 0}
    };

    HashMap<String, Image> scaledSprites = new HashMap<>();

    boolean loaded = false;

    final String worldSettingsFilepath = "src\\ProjectOuroboros\\WorldSettings.json";
    JSONParser parser = new JSONParser();
    JSONArray worldSettings = new JSONArray();

    JSONArray settlementPrefixes = new JSONArray();
    JSONArray settlementSufixes = new JSONArray();

//    </editor-fold>
    public WorldCreation() {

        linebreak(3);

        initComponents();
        screenSize = panel_Mainpanel.getSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        setExtendedState(WorldCreation.MAXIMIZED_BOTH);

        LoadWorldSettings();
        loadSettlementNames();
        GenerateWorld();
        updateDashboard();
//        System.out.println("Size: " + panel_Mainpanel.getComponents().length); check the size of the mainpanel

    }

    public void GenerateWorld() {

        System.out.println("cities: " + cities);
        System.out.println("towns: " + towns);
        System.out.println("villages: " + villages);
        System.out.println();

        System.out.println("xMapSize: " + xMax);
        System.out.println("yMapSize: " + yMax);
        System.out.println();

        System.out.println("xStartingCoordinates: " + xMaxArray.length / 2);
        System.out.println("yStartingCoordinates: " + yMaxArray.length / 2);
        System.out.println();

        System.out.println("River Sources: " + riverSources);
        System.out.println("Forests: " + forestSources);
        System.out.println("Cities: " + cities);
        System.out.println("Towns: " + towns);
        System.out.println("Villages: " + villages);

        linebreak(1);

        // generates the background ocean layer
        for (int y = -20; y <= yMax + 19; y++) {

            for (int x = -20; x <= xMax + 19; x++) {

                String tileCoordinates = "x" + x + "y" + y;

                JLabel newTile = generateProtoTile(tileCoordinates);

                selectBiome(newTile, "Oc");
                tileMapAddition(newTile, tileCoordinates, backgroundTileMap);

            }

        }

        // -----------------------------------------------------------------------------------------------------------
        while (true) {

//         Prints out all the key/ value pairs in the tileMap Hashmap.
//         Use for debugging
//            for (String key : tileMap.keySet()) {
//
//                JLabel label = tileMap.get(key);
//                System.out.println("Key: " + key + ", Value: " + label.getName());
//
//            }
            int riverSourcesGenerated = 0;
            int riversGenerated = 0;
            int forestsGenerated = 0;
            boolean capitalGenerated = false;
            int citiesGenerated = 0;
            int townsGenerated = 0;
            int villagesGenerated = 0;

            // turn this boolean into an int
            int generationPhase = 0;
            boolean generatingHillBranches = false;
            int hillBranchesGenerated = 0;
            boolean generatingRiverSources = false;
            boolean generatingRivers = false;
            boolean generatingForestBranches = false;
            int forestBranchesGenerated = 0;

            // generates the tiles without biomes
            while (generationPhase != 2) {

                for (int y = 1; y <= yMax; y++) {

                    for (int x = 1; x <= xMax; x++) {

                        String tileCoordinates = "x" + x + "y" + y;
                        JLabel newTile = new JLabel();

                        // generates the prot tiles (i.e. tiles without biomes)
                        if (generationPhase == 0) {

                            newTile = generateProtoTile(tileCoordinates);

                            // generations the first shoreline layer
                        } else if (generationPhase == 1) {

                            newTile = tileMap.get(tileCoordinates);

                            if (x <= 1 || y <= 1 || x >= xMax || y >= yMax) {

                                selectBiome(newTile, "Sh");

                            } else if (x <= 2 || y <= 2 || x >= xMax - 1 || y >= yMax - 1) {

                                if ((x == 2 && y == yMax - 1) || (x == xMax - 1 && y == 2) || (x == 2 && y == 2) || (x == xMax - 1 && y == yMax - 1)) {

                                    selectBiome(newTile, "Sh");

                                } else if (randomizer.nextInt(3) == 0) {

                                    selectBiome(newTile, "Sh");

                                } else {

                                    selectBiome(newTile, "Be");

                                }

                            }

                        }

                        tileMapAddition(newTile, tileCoordinates, tileMap);

                    }

                }

                generationPhase++;

            }

            // generates the biomes and the rivers
            // starting from the beaches, to the hills, lakes, rivers, and lastly forestSources
            while (generationPhase <= 11) {

                // add a check when a river source is destroyed to restart the generation
                if (!generatingRiverSources) {

                    OUTER:
                    for (int y = 3; y <= yMax - 2; y++) {

                        for (int x = 3; x <= xMax - 2; x++) {

                            String tileCoordinates = "x" + x + "y" + y;
                            JLabel newTile;

                            newTile = tileMap.get(tileCoordinates);

                            switch (generationPhase) {
                                case 2:
                                    for (int[] pair : pairs) {

                                        JLabel adjacentTile = tileMap.get("x" + (x + pair[0]) + "y" + (y + pair[1]));
                                        if (adjacentTile.getName().equals("Shore")) {

                                            selectBiome(newTile, "Be");

                                        } else {

                                            if (!newTile.getName().equals("Beach")) {

                                                selectBiome(newTile, "Pl");

                                            }

                                        }

                                    }
                                    break;
                                case 3:
                                    if (!newTile.getName().equals("Beach")) {

                                        if ((randomizer.nextInt(mountainSpawnRate - 2) == 0)) {

                                            selectBiome(newTile, "Mt");

                                        }

                                    }
                                    break;
                                // generate the foothills of the mountains
                                case 4:
                                    for (int[] pair : pairs) {

                                        JLabel adjacentTile = tileMap.get("x" + (x + pair[0]) + "y" + (y + pair[1]));
                                        if (adjacentTile.getName().equals("Mt")
                                                && !newTile.getName().equals("Beach")
                                                && !newTile.getName().equals("Mt")) {

                                            selectBiome(newTile, "Ht");

                                        }

                                    }
                                    break;
                                case 5:
                                    generatingHillBranches = true;

                                    for (int[] pair : pairs) {

                                        JLabel adjacentTile = tileMap.get("x" + (x + pair[0]) + "y" + (y + pair[1]));
                                        if (adjacentTile.getName().equals("Hill")
                                                && !newTile.getName().equals("Beach")
                                                && !newTile.getName().equals("Mt")) {

                                            if ((randomizer.nextInt(mountainSpawnRate + 4) == 0)) {

                                                selectBiome(newTile, "Ht");

                                            }

                                        }

                                    }
                                    break;
                                case 6:
                                    if (!newTile.getName().equals("Hill")
                                            && !newTile.getName().equals("Beach")) {

                                        if ((randomizer.nextInt(lakeSpawnRate) == 0)) {

                                            selectBiome(newTile, "Lk");

                                        }

                                    }
                                    break;
                                // this phase is called after the river sources are generated
                                case 8:

                                    if (forestsGenerated < forestSources) {

//                                        System.out.println("phase: " + generationPhase);
                                        JLabel currentRiverTile = riverTileMap.get(tileCoordinates);

                                        try {

                                            if ((newTile.getName().equals("Plain")) && (currentRiverTile.getName().equals("River"))) {

                                                if (randomizer.nextInt(forestSpawnRate / 6) == 0 && forestsGenerated <= forestSources) {
//                                                    System.out.println("New Forest: " + tileCoordinates);
                                                    selectBiome(newTile, "Fr");
                                                    forestsGenerated++;

                                                }

                                            }

                                        } catch (Exception e) {

                                        }

                                        break;

                                    }

                                case 9:

                                    for (int[] pair : pairs) {

                                        JLabel adjacentTile = tileMap.get("x" + (x + pair[0]) + "y" + (y + pair[1]));
                                        if (adjacentTile.getName().equals("Forest")
                                                && !newTile.getName().equals("Lake")
                                                && !newTile.getName().equals("Beach")
                                                && !newTile.getName().equals("Mt")
                                                && !newTile.getName().equals("Hill")) {

                                            selectBiome(newTile, "Frr");
//                                            System.out.println("Forest Found: " + ("x" + (x + pair[0]) + "y" + (y + pair[1])));

                                        }

                                    }

                                    break;
                                case 10:
                                    generatingForestBranches = true;

                                    for (int[] pair : pairs) {

                                        JLabel adjacentTile = tileMap.get("x" + (x + pair[0]) + "y" + (y + pair[1]));
                                        if (adjacentTile.getName().equals("Frr")
                                                && !newTile.getName().equals("Lake")
                                                && !newTile.getName().equals("Beach")
                                                && !newTile.getName().equals("Mt")
                                                && !newTile.getName().equals("Hill")) {

                                            if ((randomizer.nextInt(forestSpawnRate / 6) == 0)) {

                                                selectBiome(newTile, "Frr");

                                            }

                                        }

                                    }

                                    break;
                                default:
                                    break;
                            }

                            tileMapAddition(newTile, tileCoordinates, tileMap);

                        }

                    }

                } else {

                    // generates the river sources and the river paths
                    while (true) {

                        // this for loop is here in the case o
                        SEARCH:
                        for (int y = yMax - 6; y > 6; y--) {

                            for (int x = xMax - 6; x > 6; x--) {

                                String tileCoordinates = "x" + x + "y" + y;

                                JLabel currentTile = tileMap.get(tileCoordinates);
                                JLabel currentRiverTile = riverTileMap.get(tileCoordinates);

                                if (!generatingRivers) {

                                    boolean generateWaterSource = true;

                                    // Checks whether there's currently a water source in the picked coordinates
                                    try {

//                                    System.out.println("currentRiverTile: " + currentRiverTile.getName());
//                                    System.out.println("River Source Found");
                                        if (currentRiverTile.getName().equals("Rs")) {
                                            generateWaterSource = false;
                                        }

                                    } catch (Exception e) {

                                    }

                                    if (generateWaterSource) {

                                        if (currentTile.getName().equals("Hill")) {

                                            for (int[] pair : pairs) {

                                                String adjacentTileName = "x" + (x + pair[0]) + "y" + (y + pair[1]);

//                                            System.out.println("Searched: " + adjacentTileName);
                                                JLabel adjacentTile = tileMap.get(adjacentTileName);
                                                JLabel adjacentRiverTile = riverTileMap.get(adjacentTileName);

                                                try {

//                                                    System.out.println("adjacentRiverTile: " + adjacentRiverTile.getName());
                                                    if (adjacentTile.getName().equals("Lake")
                                                            || adjacentRiverTile.getName().equals("Rs")) {

//                                                        System.out.println("Nearby Water: " + adjacentTileName);
                                                        generateWaterSource = false;
                                                        break;

                                                    }

                                                } catch (Exception e) {

                                                }

                                            }

//                                        System.out.println("River Source Location: " + tileCoordinates);
//                                        System.out.println();
                                            if (generateWaterSource) {

                                                JLabel newRiverTile = generateProtoTile(tileCoordinates);
                                                selectBiome(newRiverTile, "Rs");
                                                tileMapAddition(newRiverTile, tileCoordinates, riverTileMap);
                                                riverSourcesGenerated++;
                                                break SEARCH;

                                            }

                                        }

                                    }

                                } else {

                                    try {

//                                        System.out.println("currentRiverTile: " + currentRiverTile.getName());
//                                        System.out.println("River Source Found");
                                        if (currentRiverTile.getName().equals("Rs")) {
//                                            System.out.println("River Source Found: " + tileCoordinates);
//
                                            int pairChosen = randomizer.nextInt(2);
                                            int tileX = x;
                                            int tileY = y;
                                            boolean phase = false;
//
                                            OUTER:
                                            while (true) {
                                                if (!phase) {
                                                    tileX += pairs[pairChosen][0];
                                                    tileY += pairs[pairChosen][1];
                                                } else {
                                                    tileX += pairs[pairChosen][1];
                                                    tileY += pairs[pairChosen][0];
                                                }
//
                                                String riverTileCoordinates = "x" + tileX + "y" + tileY;
                                                String directionIconFilePath = "";
//                                                currentTile = tileMap.get(riverTileCoordinates);
                                                boolean continueRiver = true;
//                                                // checks whether the river has met a body of water
                                                for (int[] pair : pairs) {

                                                    String adjacentTileName = "x" + (tileX + pair[0]) + "y" + (tileY + pair[1]);
                                                    JLabel adjacentTile = tileMap.get(adjacentTileName);

                                                    if (adjacentTile.getName().equals("Lake") || adjacentTile.getName().equals("Shore")) {

//                                            System.out.println("pairChosen: " + pairChosen);
                                                        int xDifference = (tileX + pair[0]) - tileX;
                                                        int yDifference = (tileY + pair[1]) - tileY;
//                                            System.out.println("New Direction: x" + tileX + "y" + tileY);
//                                            System.out.println("Close Water Source Found: " + adjacentTileName);
//                                            System.out.println("Difference: x" + xDifference + "y" + yDifference);
//                                            System.out.println("Phase: " + phase);
//                                            System.out.prinRtln();

// figure out river endings
                                                        switch (pairChosen) {

                                                            case (0):
                                                                if (!phase) {
                                                                    if (xDifference < 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/sw_ws.png";
                                                                    } else if (yDifference < 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ns_sn.png";
                                                                    }
                                                                } else {
                                                                    if (xDifference < 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/we_ew.png";
                                                                    } else if (yDifference < 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png";
                                                                    }
                                                                }
                                                                break;
                                                            //
                                                            case (1):
                                                                if (!phase) {
                                                                    if (xDifference > 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png";
                                                                    } else if (yDifference > 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ns_sn.png";
                                                                    }
                                                                } else {
                                                                    if (xDifference > 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/we_ew.png";
                                                                    } else if (yDifference > 0) {
                                                                        directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/sw_ws.png";
                                                                    }
                                                                }
                                                                break;

                                                        }

                                                        continueRiver = false;
                                                        break;

                                                    }

                                                }
// destroys the hills and mountains in the river's path (unrealistic ik)
//                                                cheating solution but solution nonetheless
                                                JLabel currentcurrentRiverTile = tileMap.get(riverTileCoordinates);
//                                                System.out.println("currentTile: " + currentcurrentRiverTile.getName());
                                                switch (currentcurrentRiverTile.getName()) {
                                                    case "Hill":
                                                    case "Mountain":
//                                                        System.out.println("Mountain destroyed!");
                                                        currentcurrentRiverTile = generateProtoTile(riverTileCoordinates);
                                                        selectBiome(currentcurrentRiverTile, "Pl");
                                                        tileMapAddition(currentcurrentRiverTile, riverTileCoordinates, tileMap);
                                                        break;
                                                    default:
                                                        //                                    System.out.println("newTile Location: " + riverTileCoordinates);
//                                    System.out.println("newTile: " + currentTile.getName());
                                                        break;
                                                }

                                                if (continueRiver) {
                                                    switch (pairChosen) {

                                                        case (0):
                                                            if (!phase) {
                                                                directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/sw_ws.png";
                                                            } else {
                                                                directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png";
                                                            }
                                                            break;
                                                        case (1):
                                                            if (!phase) {
                                                                directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png";
                                                            } else {
                                                                directionIconFilePath = "/ProjectOuroboros/Images/Sprites/Rivers/sw_ws.png";
                                                            }

                                                    }

                                                }

                                                try {

                                                    JLabel riverTile = riverTileMap.get(riverTileCoordinates);

                                                    // I cheated again!  aww crud!
                                                    if (riverTile.getName().equals("Rs") || riverTile.getName().equals("River")) {

//                                                        System.out.println("River Source Removed");
                                                        riverSourcesGenerated--;
                                                        generationPhase = 11;
                                                        break SEARCH;

                                                    }

                                                } catch (Exception e) {

                                                }

                                                JLabel riverTile = generateProtoTile(riverTileCoordinates);
                                                riverTile.setIcon(new javax.swing.ImageIcon(getClass().getResource(directionIconFilePath)));
                                                selectBiome(riverTile, "Rv");
                                                tileMapAddition(riverTile, riverTileCoordinates, riverTileMap);

                                                phase = !phase;

                                                if (!continueRiver) {

                                                    riversGenerated++;
//                                                    System.out.println("River generated");
//                                                    System.out.println("Loops: " + loops);
                                                    break;
//
                                                }

                                            }

                                        }

                                    } catch (Exception e) {

                                    }

                                }
                            }

                        }

                        if (generationPhase == 11 || (riversGenerated >= riverSourcesGenerated && generatingRivers)) {
                            break;
                        } else if (riverSourcesGenerated >= riverSources) {
                            generatingRivers = true;
                        }

                    }

                    if (riverSourcesGenerated < riverSources) {
                        generationPhase = 11;
                    }
                    generatingRiverSources = false;

                }

                if (generatingHillBranches) {
                    hillBranchesGenerated++;
                } else if (generatingForestBranches) {
                    forestBranchesGenerated++;
                }

                if (hillBranchesGenerated >= hillBranches && generationPhase == 5) {
//                    System.out.println("hillBranchesGenerated: " + hillBranchesGenerated);
                    generatingHillBranches = false;
                } else if (forestBranchesGenerated >= forestBranches && generationPhase == 10) {
//                    System.out.println("forestBranchesGenerated: " + forestBranchesGenerated);
                    generatingForestBranches = false;
                }

                if (generationPhase == 6) {

                    // switches to the river generation phase
                    generatingRiverSources = true;

                }

                if (!generatingHillBranches && !generatingForestBranches) {

//                    System.out.println("generationPhase: " + generationPhase);
//                    linebreak(0);
                    generationPhase++;

                }

            }

            // generates the settlements at random points in the map
            while (generationPhase <= 12) {
                int loops = 0;

                OUTER:
                while (loops <= (xMax * yMax) / 4) {
//                    System.out.println("Loops: " + loops);
//                    (int) (Math.random() * (max - min + 1)) + min;
                    boolean generateSettlement = false;
                    int margin = capitalGenerated ? 4 : 2;

                    int randomX = (int) (Math.random() * (xMax - 2 * margin + 1) + margin);
                    int randomY = (int) (Math.random() * (yMax - 2 * margin + 1) + margin);
                    String randomTileCoordinates = "x" + randomX + "y" + randomY;
                    JLabel currentTile = tileMap.get(randomTileCoordinates);
                    JLabel settlementTile = generateProtoTile(randomTileCoordinates);
                    JLabel settlementNameTile = generateProtoTile(randomTileCoordinates);

                    int searchMultiplier = 0;
                    int searchX = 0;
                    int searchY = 0;

                    if (citiesGenerated < cities) {

                        searchMultiplier = (mapSize * 2);
                        searchX = randomX + searchMultiplier;
                        searchY = randomY + searchMultiplier;

                    } else if (townsGenerated < towns) {

                        searchMultiplier = (mapSize);
                        searchX = randomX + searchMultiplier;
                        searchY = randomY + searchMultiplier;

                    } else if (villagesGenerated < villages) {

                        searchMultiplier = mapSize - (int) (mapSize * 0.48);
                        searchX = randomX + searchMultiplier;
                        searchY = randomY + searchMultiplier;

                    }

                    if (currentTile.getName().equals("Beach")
                            && !capitalGenerated) {

                        generateSettlement = true;
                        selectBiome(settlementTile, "Ca");
//                        System.out.println("Capital City Location: " + randomTileCoordinates);
                        capitalGenerated = true;

                    } else if (capitalGenerated
                            && !currentTile.getName().equals("City")
                            && !currentTile.getName().equals("Mt")
                            && !currentTile.getName().equals("Lake")
                            && citiesGenerated < cities) {

                        boolean suitable = true;

//                        System.out.println("NEW CITY LOCATION: " + randomTileCoordinates);
                        SEARCH:
                        for (int startSearchX = (randomX - searchMultiplier); startSearchX <= searchX; startSearchX++) {

                            for (int startSearchY = (randomY - searchMultiplier); startSearchY <= searchY; startSearchY++) {

                                String adjacentTileName = "x" + startSearchX + "y" + startSearchY;
//                                System.out.println("Searched: " + adjacentTileName);

                                try {

                                    JLabel adjacentTile = settlementsTileMap.get(adjacentTileName);
//                                    System.out.println("Tile: " + adjacentTile.getName());

                                    if (adjacentTile.getName().equals("Capital")
                                            || adjacentTile.getName().equals("City")) {
                                        suitable = false;
//                                        System.out.println("Found Nearby Settlement: " + adjacentTileName);
                                        break SEARCH;
                                    } else {
                                    }

                                } catch (Exception e) {

                                }

                            }

                        }

                        if (suitable) {

                            generateSettlement = true;
                            selectBiome(settlementTile, "Ct");
//                            System.out.println("City Location: " + randomTileCoordinates);
                            citiesGenerated++;
                            loops = 0;

                        }

                    } else if (citiesGenerated >= cities
                            && !currentTile.getName().equals("City")
                            && !currentTile.getName().equals("Capital")
                            && !currentTile.getName().equals("Mt")
                            && !currentTile.getName().equals("Lake")
                            && townsGenerated < towns) {

                        boolean suitable = true;

//                        System.out.println("NEW TOWN LOCATION: " + randomTileCoordinates);
                        SEARCH:
                        for (int startSearchX = (randomX - searchMultiplier); startSearchX <= searchX; startSearchX++) {

                            for (int startSearchY = (randomY - searchMultiplier); startSearchY <= searchY; startSearchY++) {

                                String adjacentTileName = "x" + startSearchX + "y" + startSearchY;
//                                System.out.println("Searched: " + adjacentTileName);

                                try {

                                    JLabel adjacentTile = settlementsTileMap.get(adjacentTileName);
//                                    System.out.println("Tile: " + adjacentTile.getName());

                                    if (adjacentTile.getName().equals("Capital")
                                            || adjacentTile.getName().equals("City")
                                            || adjacentTile.getName().equals("Town")) {
                                        suitable = false;
//                                        System.out.println("Found Nearby Settlement: " + adjacentTileName);
                                        break SEARCH;
                                    } else {
                                    }

                                } catch (Exception e) {

                                }

                            }

                        }

                        if (suitable) {

                            generateSettlement = true;
                            selectBiome(settlementTile, "Tw");
//                            System.out.println("Town Location: " + randomTileCoordinates);
                            townsGenerated++;
                            loops = 0;

                        }

                    } else if (townsGenerated >= towns
                            && !currentTile.getName().equals("Town")
                            && !currentTile.getName().equals("City")
                            && !currentTile.getName().equals("Capital")
                            && !currentTile.getName().equals("Mt")
                            && !currentTile.getName().equals("Lake")
                            && villagesGenerated < villages) {

//                        System.out.println("searchMultiplier: " + searchMultiplier);
//                        System.out.println("searchX: " + searchMultiplier);
                        boolean suitable = true;

                        SEARCH:
                        for (int startSearchX = (randomX - searchMultiplier); startSearchX <= searchX; startSearchX++) {

                            for (int startSearchY = (randomY - searchMultiplier); startSearchY <= searchY; startSearchY++) {

                                String adjacentTileName = "x" + startSearchX + "y" + startSearchY;
//                                System.out.println("Searched: " + adjacentTileName);

                                try {

                                    JLabel adjacentTile = settlementsTileMap.get(adjacentTileName);
//                                    System.out.println("Tile: " + adjacentTile.getName());

                                    if (adjacentTile.getName().equals("Capital")
                                            || adjacentTile.getName().equals("City")
                                            || adjacentTile.getName().equals("Town")
                                            || adjacentTile.getName().equals("Village")) {
                                        suitable = false;
//                                        System.out.println("Found Nearby Settlement: " + adjacentTileName);
                                        break SEARCH;
                                    } else {
                                    }

                                } catch (Exception e) {

                                }

                            }

                        }

                        if (suitable) {

                            generateSettlement = true;
                            selectBiome(settlementTile, "Vl");
//                            System.out.println("Village Location: " + randomTileCoordinates);
                            villagesGenerated++;
                            loops = 0;

                        }

                    }

                    if (generateSettlement) {
                        tileMapAddition(settlementTile, randomTileCoordinates, settlementsTileMap);
                        generateSettlementName(randomTileCoordinates, settlementNameTile);
                    }
                    loops++;

                }

                generationPhase++;

            }

            if (riverSourcesGenerated >= riverSources
                    && forestsGenerated >= forestSources
                    && citiesGenerated >= cities
                    && townsGenerated >= towns
                    && villagesGenerated >= villages) {
                System.out.println("River Sources Generated: " + riverSourcesGenerated + " / " + riverSources);
                System.out.println("Forests Generated: " + forestsGenerated + " / " + forestSources);
                System.out.println("Cities Generated: " + citiesGenerated + " / " + cities);
                System.out.println("Towns Generated: " + townsGenerated + " / " + towns);
                System.out.println("Villages Generated: " + villagesGenerated + " / " + villages);
                linebreak(2);
                break;

            } else {

                System.out.println("Regenerating");
                if (riverSourcesGenerated < riverSources) {
                    System.out.println("River Sources Generated: " + riverSourcesGenerated + " / " + riverSources);
                } else if (forestsGenerated < forestSources) {
                    System.out.println("Forests Generated: " + forestsGenerated + " / " + forestSources);
                } else if (citiesGenerated < cities) {
                    System.out.println("Cities Generated: " + citiesGenerated + " / " + cities);
                } else if (townsGenerated < towns) {
                    System.out.println("Towns Generated: " + townsGenerated + " / " + towns);
                } else if (villagesGenerated < villages) {
                    System.out.println("Villages Generated: " + villagesGenerated + " / " + villages);
                }

                for (String key : riverTileMap.keySet()) {

                    panel_Mainpanel.remove(riverTileMap.get(key));

                }
                for (String key : tileMap.keySet()) {

                    panel_Mainpanel.remove(tileMap.get(key));

                }
                for (String key : settlementsTileMap.keySet()) {

                    panel_Mainpanel.remove(settlementsTileMap.get(key));

                }

                tileMap.clear();
                riverTileMap.clear();
                settlementsTileMap.clear();

                linebreak(1);
            }

        }

        loaded = true;
        panel_Mainpanel.revalidate();
        panel_Mainpanel.repaint();

        // changes the placeholder biome "Frr" to Fr
        for (String key : tileMap.keySet()) {

            JLabel tile = tileMap.get(key);
            if (tile.getName().equals("Frr")) {
                selectBiome(tile, "Fr");
            }

        }

        // <editor-fold desc="adjusts component z order and window focus">
        // Puts the rivertiles in a higher order
        for (String key : riverTileMap.keySet()) {

            JLabel riverTile = riverTileMap.get(key);
            if (riverTile.getName().equals("River")) {
                riverTile.setBorder(null);
                riverTile.setText("");
                riverTile.setOpaque(false);
                panel_Mainpanel.setComponentZOrder(riverTile, 0);
            }

        }

        // Puts the rivertiles in a higher order
        for (String key : settlementsTileMap.keySet()) {

            JLabel settlementTile = settlementsTileMap.get(key);
            try {

                if (settlementTile.getName().equals("Capital")
                        || settlementTile.getName().equals("City")
                        || settlementTile.getName().equals("Town")
                        || settlementTile.getName().equals("Village")) {
                    panel_Mainpanel.setComponentZOrder(settlementTile, 0);
                }

            } catch (Exception e) {

            }

        }

        // Puts the rivertiles in a higher order
        for (String key : settlementsNameTileMap.keySet()) {

            JLabel settlementNameTile = settlementsNameTileMap.get(key);
            try {

                if (!settlementNameTile.getName().isEmpty()) {
                    panel_Mainpanel.setComponentZOrder(settlementNameTile, 0);
                }

            } catch (Exception e) {

            }

        }

        panel_Mainpanel.setComponentZOrder(label_Player, 0);
        panel_Mainpanel.setComponentZOrder(label_ZoomScale, 0);
        panel_Mainpanel.setComponentZOrder(label_Coordinates, 0);
        panel_Mainpanel.setComponentZOrder(label_Biome, 0);
        panel_Mainpanel.setComponentZOrder(label_River, 0);
        panel_Mainpanel.setComponentZOrder(label_SettlementType, 0);
        panel_Mainpanel.setComponentZOrder(label_SettlementName, 0);
        panel_Mainpanel.setComponentZOrder(button_Generate, 0);

        panel_Mainpanel.setComponentZOrder(label_TileScale, 0);
        panel_Mainpanel.setComponentZOrder(slider_TileScale, 0);
        panel_Mainpanel.setComponentZOrder(label_MapSize, 0);
        panel_Mainpanel.setComponentZOrder(slider_MapSize, 0);
        panel_Mainpanel.setComponentZOrder(label_Population, 0);
        panel_Mainpanel.setComponentZOrder(slider_Population, 0);
        panel_Mainpanel.setComponentZOrder(label_MountainSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(slider_MountainSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(label_LakeSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(slider_LakeSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(label_ForestSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(slider_ForestSpawnRate, 0);
        panel_Mainpanel.setComponentZOrder(label_RiverSources, 0);
        panel_Mainpanel.setComponentZOrder(slider_RiverSources, 0);
        panel_Mainpanel.setComponentZOrder(label_ForestSources, 0);
        panel_Mainpanel.setComponentZOrder(slider_ForestSources, 0);
        panel_Mainpanel.setComponentZOrder(label_HillBranches, 0);
        panel_Mainpanel.setComponentZOrder(slider_HillBranches, 0);
        panel_Mainpanel.setComponentZOrder(label_ForestBranches, 0);
        panel_Mainpanel.setComponentZOrder(slider_ForestBranches, 0);

        // Makes sure that the main panel is the focus
        panel_Mainpanel.requestFocusInWindow();
        // </editor-fold>

    }

    // might want to work a system to make prefixes more accurate
    public void generateSettlementName(String tileCoordinates, JLabel settlementNameTile) {

        JSONObject biomeObj;
        JSONArray prefixArray;
        JSONObject prefixObj;
        JSONArray sufixArray;
        JSONObject sufixObj;

        String biome = tileMap.get(tileCoordinates).getName();
        try {
            if (riverTileMap.get(tileCoordinates).getName().equals("River")) {
                biome = riverTileMap.get(tileCoordinates).getName();
            }
        } catch (Exception e) {

        }

        int biomeIndex = 0;
        switch (biome) {
            case "Beach":
                biomeIndex = 0;
                break;
            case "Plain":
                biomeIndex = 1;
                break;
            case "Hill":
                biomeIndex = 2;
                break;
            case "Frr":
                biome = "Forest";
                biomeIndex = 3;
                break;
            case "River":
                biomeIndex = 4;
                break;
        }

        if (randomizer.nextInt(3) == 0) {

            biomeObj = (JSONObject) settlementPrefixes.get(biomeIndex);
            prefixArray = (JSONArray) biomeObj.get(biome);
            prefixObj = (JSONObject) prefixArray.get((int) (Math.random() * ((prefixArray.size() - 1) - 0 + 1)) + 0);

            biomeObj = (JSONObject) settlementSufixes.get(5);
            sufixArray = (JSONArray) biomeObj.get("Generic");
            sufixObj = (JSONObject) sufixArray.get((int) (Math.random() * ((sufixArray.size() - 1) - 0 + 1)) + 0);

        } else {

            biomeObj = (JSONObject) settlementPrefixes.get(5);
            prefixArray = (JSONArray) biomeObj.get("Generic");
            prefixObj = (JSONObject) prefixArray.get((int) (Math.random() * ((prefixArray.size() - 1) - 0 + 1)) + 0);

            biomeObj = (JSONObject) settlementSufixes.get(biomeIndex);
            sufixArray = (JSONArray) biomeObj.get(biome);
            sufixObj = (JSONObject) sufixArray.get((int) (Math.random() * ((sufixArray.size() - 1) - 0 + 1)) + 0);

        }

//        System.out.println("prefixObj: " + prefixObj);
//        System.out.println("sufixObj: " + sufixObj);
//        System.out.println();
        String generatedName = prefixObj.get("").toString();
        generatedName = generatedName.concat(sufixObj.get("").toString());
        JLabel settlementTile = generateProtoTile(tileCoordinates);
        settlementTile.setText(generatedName);
        settlementTile.setName(generatedName);
        panel_Mainpanel.add(settlementTile);
        settlementTile.setVisible(true);
        settlementsNameTileMap.put(tileCoordinates, settlementTile);
    }

    public void tileMapAddition(JLabel newTile, String tileCoordinates, HashMap<String, JLabel> tileMap) {

        panel_Mainpanel.add(newTile);
        panel_Mainpanel.setComponentZOrder(newTile, 0);
        newTile.setVisible(true);
        tileMap.put(tileCoordinates, newTile);

    }

    private void updateDashboard() {
        //         Prints out all the key/ value pairs in the tileMap Hashmap.
//         Use for debugging
//        for (String key : tileMap.keySet()) {
//
//            JLabel label = tileMap.get(key);
//            System.out.println("Key: " + key + ", Value: " + label.getName());
//
//        }

        // Sets the text of the following Jlabels to the proper coords and biome
        label_Coordinates.setText("Location: " + "x" + coordinatesX + "y" + coordinatesY);
        label_Biome.setText("Biome: " + tileMap.get("x" + coordinatesX + "y" + coordinatesY).getName());
        label_ZoomScale.setText("ZoomScale: " + Float.toString(zoomScale));

        try {

            label_River.setText("River: " + riverTileMap.get("x" + coordinatesX + "y" + coordinatesY).getName());

        } catch (Exception e) {

            label_River.setText("River: " + "");

        }

        try {

            label_SettlementType.setText("Settlement Type: " + settlementsTileMap.get("x" + coordinatesX + "y" + coordinatesY).getName());
            label_SettlementName.setText("Settlement Name: " + settlementsNameTileMap.get("x" + coordinatesX + "y" + coordinatesY).getName());

        } catch (Exception e) {

            label_SettlementType.setText("Settlement Type: " + "");
            label_SettlementName.setText("Settlement Name: " + "");

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Mainpanel = new javax.swing.JPanel();
        label_ProtoTile = new javax.swing.JLabel();
        label_Player = new javax.swing.JLabel();
        label_ZoomScale = new javax.swing.JLabel();
        label_SettlementName = new javax.swing.JLabel();
        label_SettlementType = new javax.swing.JLabel();
        label_River = new javax.swing.JLabel();
        label_Biome = new javax.swing.JLabel();
        label_Coordinates = new javax.swing.JLabel();
        label_ProtoTile2 = new javax.swing.JLabel();
        label_ForestBranches = new javax.swing.JLabel();
        slider_ForestBranches = new javax.swing.JSlider();
        label_HillBranches = new javax.swing.JLabel();
        slider_HillBranches = new javax.swing.JSlider();
        label_ForestSources = new javax.swing.JLabel();
        slider_ForestSources = new javax.swing.JSlider();
        label_RiverSources = new javax.swing.JLabel();
        slider_RiverSources = new javax.swing.JSlider();
        label_ForestSpawnRate = new javax.swing.JLabel();
        slider_ForestSpawnRate = new javax.swing.JSlider();
        label_LakeSpawnRate = new javax.swing.JLabel();
        slider_LakeSpawnRate = new javax.swing.JSlider();
        label_MountainSpawnRate = new javax.swing.JLabel();
        slider_MountainSpawnRate = new javax.swing.JSlider();
        label_Population = new javax.swing.JLabel();
        slider_Population = new javax.swing.JSlider();
        label_MapSize = new javax.swing.JLabel();
        slider_MapSize = new javax.swing.JSlider();
        label_TileScale = new javax.swing.JLabel();
        slider_TileScale = new javax.swing.JSlider();
        button_Save = new javax.swing.JButton();
        button_Generate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setSize(new java.awt.Dimension(1920, 1080));
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });

        panel_Mainpanel.setBackground(new java.awt.Color(0, 1, 2));
        panel_Mainpanel.setToolTipText(null);
        panel_Mainpanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panel_Mainpanel.setMaximumSize(new java.awt.Dimension(3840, 2160));
        panel_Mainpanel.setMinimumSize(new java.awt.Dimension(1280, 720));
        panel_Mainpanel.setName(""); // NOI18N
        panel_Mainpanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panel_Mainpanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panel_MainpanelComponentResized(evt);
            }
        });
        panel_Mainpanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panel_MainpanelKeyPressed(evt);
            }
        });
        panel_Mainpanel.setLayout(null);

        label_ProtoTile.setForeground(new java.awt.Color(0, 0, 0));
        label_ProtoTile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_ProtoTile.setText("Player");
        label_ProtoTile.setToolTipText(null);
        label_ProtoTile.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.black), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 51))));
        label_ProtoTile.setFocusable(false);
        label_ProtoTile.setOpaque(true);
        panel_Mainpanel.add(label_ProtoTile);
        label_ProtoTile.setBounds(90, 280, 75, 75);

        label_Player.setBackground(new java.awt.Color(102, 102, 102));
        label_Player.setForeground(new java.awt.Color(255, 255, 102));
        label_Player.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Player.setText("Player");
        label_Player.setToolTipText(null);
        label_Player.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.black), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 51))));
        label_Player.setFocusable(false);
        label_Player.setOpaque(true);
        panel_Mainpanel.add(label_Player);
        label_Player.setBounds(230, 300, 75, 75);

        label_ZoomScale.setBackground(new java.awt.Color(51, 51, 51));
        label_ZoomScale.setForeground(new java.awt.Color(255, 255, 255));
        label_ZoomScale.setText("jLabel1");
        label_ZoomScale.setToolTipText(null);
        label_ZoomScale.setOpaque(true);
        panel_Mainpanel.add(label_ZoomScale);
        label_ZoomScale.setBounds(340, 30, 150, 25);

        label_SettlementName.setBackground(new java.awt.Color(51, 51, 51));
        label_SettlementName.setForeground(new java.awt.Color(255, 255, 255));
        label_SettlementName.setText("jLabel1");
        label_SettlementName.setToolTipText(null);
        label_SettlementName.setOpaque(true);
        panel_Mainpanel.add(label_SettlementName);
        label_SettlementName.setBounds(30, 150, 300, 25);

        label_SettlementType.setBackground(new java.awt.Color(51, 51, 51));
        label_SettlementType.setForeground(new java.awt.Color(255, 255, 255));
        label_SettlementType.setText("jLabel1");
        label_SettlementType.setToolTipText(null);
        label_SettlementType.setOpaque(true);
        panel_Mainpanel.add(label_SettlementType);
        label_SettlementType.setBounds(30, 120, 300, 25);

        label_River.setBackground(new java.awt.Color(51, 51, 51));
        label_River.setForeground(new java.awt.Color(255, 255, 255));
        label_River.setText("jLabel1");
        label_River.setToolTipText(null);
        label_River.setOpaque(true);
        panel_Mainpanel.add(label_River);
        label_River.setBounds(30, 90, 300, 25);

        label_Biome.setBackground(new java.awt.Color(51, 51, 51));
        label_Biome.setForeground(new java.awt.Color(255, 255, 255));
        label_Biome.setText("jLabel1");
        label_Biome.setToolTipText(null);
        label_Biome.setOpaque(true);
        panel_Mainpanel.add(label_Biome);
        label_Biome.setBounds(30, 60, 300, 25);

        label_Coordinates.setBackground(new java.awt.Color(51, 51, 51));
        label_Coordinates.setForeground(new java.awt.Color(255, 255, 255));
        label_Coordinates.setText("jLabel1");
        label_Coordinates.setToolTipText(null);
        label_Coordinates.setOpaque(true);
        panel_Mainpanel.add(label_Coordinates);
        label_Coordinates.setBounds(30, 30, 300, 25);

        label_ProtoTile2.setBackground(new java.awt.Color(40, 40, 46));
        label_ProtoTile2.setForeground(new java.awt.Color(0, 0, 0));
        label_ProtoTile2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_ProtoTile2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png"))); // NOI18N
        label_ProtoTile2.setText("Rv");
        label_ProtoTile2.setToolTipText(null);
        label_ProtoTile2.setAlignmentY(0.0F);
        label_ProtoTile2.setFocusable(false);
        label_ProtoTile2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panel_Mainpanel.add(label_ProtoTile2);
        label_ProtoTile2.setBounds(20, 290, 50, 50);

        label_ForestBranches.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_ForestBranches.setText("Forest Branches");
        label_ForestBranches.setToolTipText(null);
        label_ForestBranches.setOpaque(true);
        panel_Mainpanel.add(label_ForestBranches);
        label_ForestBranches.setBounds(710, 590, 200, 20);

        slider_ForestBranches.setBackground(new java.awt.Color(51, 51, 51));
        slider_ForestBranches.setForeground(new java.awt.Color(102, 102, 102));
        slider_ForestBranches.setMajorTickSpacing(2);
        slider_ForestBranches.setMaximum(5);
        slider_ForestBranches.setMinimum(1);
        slider_ForestBranches.setMinorTickSpacing(1);
        slider_ForestBranches.setPaintLabels(true);
        slider_ForestBranches.setPaintTicks(true);
        slider_ForestBranches.setSnapToTicks(true);
        slider_ForestBranches.setToolTipText(null);
        slider_ForestBranches.setOpaque(true);
        slider_ForestBranches.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_ForestBranches);
        slider_ForestBranches.setBounds(710, 610, 200, 44);

        label_HillBranches.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_HillBranches.setText("Hill Branches");
        label_HillBranches.setToolTipText(null);
        label_HillBranches.setOpaque(true);
        panel_Mainpanel.add(label_HillBranches);
        label_HillBranches.setBounds(710, 520, 200, 20);

        slider_HillBranches.setBackground(new java.awt.Color(51, 51, 51));
        slider_HillBranches.setForeground(new java.awt.Color(102, 102, 102));
        slider_HillBranches.setMajorTickSpacing(2);
        slider_HillBranches.setMaximum(5);
        slider_HillBranches.setMinimum(1);
        slider_HillBranches.setMinorTickSpacing(1);
        slider_HillBranches.setPaintLabels(true);
        slider_HillBranches.setPaintTicks(true);
        slider_HillBranches.setSnapToTicks(true);
        slider_HillBranches.setToolTipText(null);
        slider_HillBranches.setOpaque(true);
        slider_HillBranches.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_HillBranches);
        slider_HillBranches.setBounds(710, 540, 200, 44);

        label_ForestSources.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_ForestSources.setText("Forests");
        label_ForestSources.setToolTipText(null);
        label_ForestSources.setOpaque(true);
        panel_Mainpanel.add(label_ForestSources);
        label_ForestSources.setBounds(710, 450, 200, 20);

        slider_ForestSources.setBackground(new java.awt.Color(51, 51, 51));
        slider_ForestSources.setForeground(new java.awt.Color(102, 102, 102));
        slider_ForestSources.setMajorTickSpacing(2);
        slider_ForestSources.setMaximum(5);
        slider_ForestSources.setMinimum(1);
        slider_ForestSources.setMinorTickSpacing(1);
        slider_ForestSources.setPaintLabels(true);
        slider_ForestSources.setPaintTicks(true);
        slider_ForestSources.setSnapToTicks(true);
        slider_ForestSources.setToolTipText(null);
        slider_ForestSources.setOpaque(true);
        slider_ForestSources.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_ForestSources);
        slider_ForestSources.setBounds(710, 470, 200, 44);

        label_RiverSources.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_RiverSources.setText("Rivers");
        label_RiverSources.setToolTipText(null);
        label_RiverSources.setOpaque(true);
        panel_Mainpanel.add(label_RiverSources);
        label_RiverSources.setBounds(710, 380, 200, 20);

        slider_RiverSources.setBackground(new java.awt.Color(51, 51, 51));
        slider_RiverSources.setForeground(new java.awt.Color(102, 102, 102));
        slider_RiverSources.setMajorTickSpacing(2);
        slider_RiverSources.setMaximum(5);
        slider_RiverSources.setMinimum(1);
        slider_RiverSources.setMinorTickSpacing(1);
        slider_RiverSources.setPaintLabels(true);
        slider_RiverSources.setPaintTicks(true);
        slider_RiverSources.setSnapToTicks(true);
        slider_RiverSources.setToolTipText(null);
        slider_RiverSources.setOpaque(true);
        slider_RiverSources.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_RiverSources);
        slider_RiverSources.setBounds(710, 400, 200, 44);

        label_ForestSpawnRate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_ForestSpawnRate.setText("Forest Spawn Rate");
        label_ForestSpawnRate.setToolTipText(null);
        label_ForestSpawnRate.setOpaque(true);
        panel_Mainpanel.add(label_ForestSpawnRate);
        label_ForestSpawnRate.setBounds(710, 310, 200, 20);

        slider_ForestSpawnRate.setBackground(new java.awt.Color(51, 51, 51));
        slider_ForestSpawnRate.setForeground(new java.awt.Color(102, 102, 102));
        slider_ForestSpawnRate.setMajorTickSpacing(2);
        slider_ForestSpawnRate.setMaximum(5);
        slider_ForestSpawnRate.setMinimum(1);
        slider_ForestSpawnRate.setMinorTickSpacing(1);
        slider_ForestSpawnRate.setPaintLabels(true);
        slider_ForestSpawnRate.setPaintTicks(true);
        slider_ForestSpawnRate.setSnapToTicks(true);
        slider_ForestSpawnRate.setToolTipText(null);
        slider_ForestSpawnRate.setOpaque(true);
        slider_ForestSpawnRate.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_ForestSpawnRate);
        slider_ForestSpawnRate.setBounds(710, 330, 200, 44);

        label_LakeSpawnRate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_LakeSpawnRate.setText("Lake Spawn Rate");
        label_LakeSpawnRate.setToolTipText(null);
        label_LakeSpawnRate.setOpaque(true);
        panel_Mainpanel.add(label_LakeSpawnRate);
        label_LakeSpawnRate.setBounds(710, 240, 200, 20);

        slider_LakeSpawnRate.setBackground(new java.awt.Color(51, 51, 51));
        slider_LakeSpawnRate.setForeground(new java.awt.Color(102, 102, 102));
        slider_LakeSpawnRate.setMajorTickSpacing(2);
        slider_LakeSpawnRate.setMaximum(5);
        slider_LakeSpawnRate.setMinimum(1);
        slider_LakeSpawnRate.setMinorTickSpacing(1);
        slider_LakeSpawnRate.setPaintLabels(true);
        slider_LakeSpawnRate.setPaintTicks(true);
        slider_LakeSpawnRate.setSnapToTicks(true);
        slider_LakeSpawnRate.setToolTipText(null);
        slider_LakeSpawnRate.setOpaque(true);
        slider_LakeSpawnRate.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_LakeSpawnRate);
        slider_LakeSpawnRate.setBounds(710, 260, 200, 44);

        label_MountainSpawnRate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_MountainSpawnRate.setText("Mountain Spawn Rate");
        label_MountainSpawnRate.setToolTipText(null);
        label_MountainSpawnRate.setOpaque(true);
        panel_Mainpanel.add(label_MountainSpawnRate);
        label_MountainSpawnRate.setBounds(710, 170, 200, 20);

        slider_MountainSpawnRate.setBackground(new java.awt.Color(51, 51, 51));
        slider_MountainSpawnRate.setForeground(new java.awt.Color(102, 102, 102));
        slider_MountainSpawnRate.setMajorTickSpacing(2);
        slider_MountainSpawnRate.setMaximum(5);
        slider_MountainSpawnRate.setMinimum(1);
        slider_MountainSpawnRate.setMinorTickSpacing(1);
        slider_MountainSpawnRate.setPaintLabels(true);
        slider_MountainSpawnRate.setPaintTicks(true);
        slider_MountainSpawnRate.setSnapToTicks(true);
        slider_MountainSpawnRate.setToolTipText(null);
        slider_MountainSpawnRate.setOpaque(true);
        slider_MountainSpawnRate.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_MountainSpawnRate);
        slider_MountainSpawnRate.setBounds(710, 190, 200, 44);

        label_Population.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Population.setText("Population");
        label_Population.setToolTipText(null);
        label_Population.setOpaque(true);
        panel_Mainpanel.add(label_Population);
        label_Population.setBounds(710, 100, 200, 20);

        slider_Population.setBackground(new java.awt.Color(51, 51, 51));
        slider_Population.setForeground(new java.awt.Color(102, 102, 102));
        slider_Population.setMajorTickSpacing(1);
        slider_Population.setMaximum(3);
        slider_Population.setMinimum(1);
        slider_Population.setMinorTickSpacing(1);
        slider_Population.setPaintLabels(true);
        slider_Population.setPaintTicks(true);
        slider_Population.setSnapToTicks(true);
        slider_Population.setToolTipText(null);
        slider_Population.setOpaque(true);
        slider_Population.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_Population);
        slider_Population.setBounds(710, 120, 200, 44);

        label_MapSize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_MapSize.setText("Size");
        label_MapSize.setToolTipText(null);
        label_MapSize.setOpaque(true);
        panel_Mainpanel.add(label_MapSize);
        label_MapSize.setBounds(710, 30, 200, 20);

        slider_MapSize.setBackground(new java.awt.Color(51, 51, 51));
        slider_MapSize.setForeground(new java.awt.Color(102, 102, 102));
        slider_MapSize.setMajorTickSpacing(1);
        slider_MapSize.setMaximum(3);
        slider_MapSize.setMinimum(1);
        slider_MapSize.setMinorTickSpacing(1);
        slider_MapSize.setPaintLabels(true);
        slider_MapSize.setPaintTicks(true);
        slider_MapSize.setSnapToTicks(true);
        slider_MapSize.setToolTipText(null);
        slider_MapSize.setOpaque(true);
        slider_MapSize.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_MapSize);
        slider_MapSize.setBounds(710, 50, 200, 44);

        label_TileScale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TileScale.setText("Tile Scale");
        label_TileScale.setToolTipText(null);
        label_TileScale.setOpaque(true);
        panel_Mainpanel.add(label_TileScale);
        label_TileScale.setBounds(500, 30, 200, 20);

        slider_TileScale.setBackground(new java.awt.Color(51, 51, 51));
        slider_TileScale.setForeground(new java.awt.Color(102, 102, 102));
        slider_TileScale.setMajorTickSpacing(25);
        slider_TileScale.setMinimum(25);
        slider_TileScale.setPaintLabels(true);
        slider_TileScale.setPaintTicks(true);
        slider_TileScale.setSnapToTicks(true);
        slider_TileScale.setToolTipText(null);
        slider_TileScale.setOpaque(true);
        slider_TileScale.setRequestFocusEnabled(false);
        panel_Mainpanel.add(slider_TileScale);
        slider_TileScale.setBounds(500, 50, 200, 44);

        button_Save.setForeground(new java.awt.Color(0, 0, 0));
        button_Save.setText("Save");
        button_Save.setToolTipText(null);
        button_Save.setRequestFocusEnabled(false);
        button_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_Save);
        button_Save.setBounds(30, 400, 72, 23);

        button_Generate.setForeground(new java.awt.Color(0, 0, 0));
        button_Generate.setText("Generate");
        button_Generate.setToolTipText(null);
        button_Generate.setRequestFocusEnabled(false);
        button_Generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_GenerateActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_Generate);
        button_Generate.setBounds(30, 370, 77, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_Mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_Mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panel_MainpanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panel_MainpanelComponentResized

//        System.out.println("icon: " + image);
//        riverTile.setIcon(new javax.swing.ImageIcon(getClass().getResource(directionIconFilePath)));
//        Image image = (ii).getImage().getScaledInstance((int) (zoomScale * 75), (int) (zoomScale * 75), Image.SCALE_FAST);
        if (loaded) {
            adjustUIComponents();
        }

    }//GEN-LAST:event_panel_MainpanelComponentResized

    private void adjustUIComponents() {

        screenSize = panel_Mainpanel.getSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        //        <editor-fold desc="dashboard">
        label_SettlementName.setBounds(25,
                (int) (screenHeight * 0.225),
                300, 25);

        label_SettlementType.setBounds(25,
                (int) (screenHeight * 0.175),
                300, 25);

        label_River.setBounds(25,
                (int) (screenHeight * 0.125),
                300, 25);

        label_Biome.setBounds(25,
                (int) (screenHeight * 0.075),
                300, 25);

        label_Coordinates.setBounds(25,
                (int) (screenHeight * 0.025),
                300, 25);

        label_ZoomScale.setBounds(350,
                (int) (screenHeight * 0.025),
                150, 25);

        //        </editor-fold>
        //        <editor-fold desc="sliders"> 
        label_TileScale.setBounds((int) (screenWidth - 650),
                (int) (screenHeight * 0.025),
                300, 20);
        slider_TileScale.setBounds((int) (screenWidth - 650),
                (int) (screenHeight * 0.05),
                300, 40);
        label_MapSize.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025),
                300, 20);
        slider_MapSize.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05),
                300, 40);
        label_Population.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 1),
                300, 20);
        slider_Population.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 1),
                300, 40);
        label_MountainSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 2),
                300, 20);
        slider_MountainSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 2),
                300, 40);
        label_LakeSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 3),
                300, 20);
        slider_LakeSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 3),
                300, 40);
        label_ForestSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 4),
                300, 20);
        slider_ForestSpawnRate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 4),
                300, 40);
        label_RiverSources.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 5),
                300, 20);
        slider_RiverSources.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 5),
                300, 40);
        label_ForestSources.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 6),
                300, 20);
        slider_ForestSources.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 6),
                300, 40);
        label_HillBranches.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 7),
                300, 20);
        slider_HillBranches.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 7),
                300, 40);
        label_ForestBranches.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.025 + 75 * 8),
                300, 20);
        slider_ForestBranches.setBounds((int) (screenWidth - 325),
                (int) (screenHeight * 0.05 + 75 * 8),
                300, 40);
//        </editor-fold>

        button_Generate.setBounds((int) (screenWidth - 325),
                (int) (screenHeight - 75),
                300, 50);

        label_Player.setBounds((int) (screenWidth * 0.5) - ((int) (zoomScale * tileScale) / 2),
                (int) (screenHeight * 0.5) - ((int) (zoomScale * tileScale) / 2),
                (int) (zoomScale * tileScale), (int) (zoomScale * tileScale));

        // adjusts the positions of the tiles
        if (loaded) {

            //        <editor-fold desc="rescales the river sprites">
            String[] directionsSpritesFilePath = {
                "/ProjectOuroboros/Images/Sprites/Rivers/ne_en.png",
                "/ProjectOuroboros/Images/Sprites/Rivers/ns_sn.png",
                "/ProjectOuroboros/Images/Sprites/Rivers/nw_wn.png",
                "/ProjectOuroboros/Images/Sprites/Rivers/se_es.png",
                "/ProjectOuroboros/Images/Sprites/Rivers/sw_ws.png",
                "/ProjectOuroboros/Images/Sprites/Rivers/we_ew.png"};

            for (String str : directionsSpritesFilePath) {

                Icon icon = new javax.swing.ImageIcon(getClass().getResource(str));
                ImageIcon ii = (ImageIcon) icon;
                Image image = (ii).getImage().getScaledInstance((int) (zoomScale * 125), (int) (zoomScale * 125), Image.SCALE_FAST);
                scaledSprites.put(str, image);

            }
//      </editor-fold>

            // deals with the background tiles
            for (int y = -20; y <= yMax + 19; y++) {

                for (int x = -20; x <= xMax + 19; x++) {

                    JLabel backgroundTile = backgroundTileMap.get("x" + x + "y" + y);

                    backgroundTile.setBounds(((((int) (screenWidth * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (x * (zoomScale * tileScale)) - (int) (coordinatesX * (zoomScale * tileScale)),
                            ((((int) (screenHeight * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (y * (zoomScale * tileScale)) - (int) (coordinatesY * (zoomScale * tileScale)),
                            (int) (zoomScale * tileScale), (int) (zoomScale * tileScale));

                }

            }

            if (!tileMap.isEmpty()) {
                // deals with the biome, river, and settlement tiles
                for (int y = 1; y <= yMax; y++) {

                    for (int x = 1; x <= xMax; x++) {

                        //              <editor-fold desc="deals with the biome tiles">
                        JLabel tile = tileMap.get("x" + x + "y" + y);

                        tile.setBounds(((((int) (screenWidth * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (x * (zoomScale * tileScale)) - (int) (coordinatesX * (zoomScale * tileScale)),
                                ((((int) (screenHeight * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (y * (zoomScale * tileScale)) - (int) (coordinatesY * (zoomScale * tileScale)),
                                (int) (zoomScale * tileScale), (int) (zoomScale * tileScale));
                        //              </editor-fold>

                        //              <editor-fold desc="deals with the river tiles">
                        try {

                            JLabel riverTile = riverTileMap.get(("x" + x + "y" + y));

                            if (riverTile.getName().equals("River")) {
                                //                            System.out.println("River Source Found: " + ("x" + x + "y" + y));
                                riverTile.setBounds(((((int) (screenWidth * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (x * (zoomScale * tileScale)) - (int) (coordinatesX * (zoomScale * tileScale)),
                                        ((((int) (screenHeight * 0.5)) - (int) (zoomScale * tileScale) + (int) (zoomScale * (tileScale / 2)))) + (int) (y * (zoomScale * tileScale)) - (int) (coordinatesY * (zoomScale * tileScale)),
                                        (int) (zoomScale * tileScale), (int) (zoomScale * tileScale));

                                // <editor-fold desc="Handles picking river sprite and scaling">
                                Icon icon = riverTile.getIcon();
                                String riverDirection = "";

                                if (icon.toString().contains("/ne_en")) {
                                    riverDirection = directionsSpritesFilePath[0];
                                } else if (icon.toString().contains("/ns_sn")) {
                                    riverDirection = directionsSpritesFilePath[1];
                                } else if (icon.toString().contains("/nw_wn")) {
                                    riverDirection = directionsSpritesFilePath[2];
                                } else if (icon.toString().contains("/se_es")) {
                                    riverDirection = directionsSpritesFilePath[3];
                                } else if (icon.toString().contains("/sw_ws")) {
                                    riverDirection = directionsSpritesFilePath[4];
                                } else if (icon.toString().contains("/we_ew")) {
                                    riverDirection = directionsSpritesFilePath[5];
                                }
                                riverTile.setIcon(new ImageIcon(scaledSprites.get(riverDirection)));
                                // </editor-fold>
                            }

                        } catch (Exception e) {

                        }
                        //                </editor-fold>

                        //              <editor-fold desc="deals with the settlements tiles">
                        try {

                            JLabel settlementTile = settlementsTileMap.get("x" + x + "y" + y);

                            if (settlementTile.getName().equals("Capital")
                                    || settlementTile.getName().equals("City")
                                    || settlementTile.getName().equals("Town")
                                    || settlementTile.getName().equals("Village")) {

                                //                        System.out.println("Found: " + ("x" + x + "y" + y));w
                                settlementTile.setBounds(((((int) (screenWidth * 0.5)) - (int) (zoomScale * tileScale) + (int) ((zoomScale * tileScale * 3) / 4))) + (int) (x * (zoomScale * tileScale)) - (int) (coordinatesX * (zoomScale * tileScale)),
                                        ((((int) (screenHeight * 0.5)) - (int) (zoomScale * tileScale) + (int) ((zoomScale * tileScale * 3) / 4))) + (int) (y * (zoomScale * tileScale)) - (int) (coordinatesY * (zoomScale * tileScale)),
                                        (int) (zoomScale * (tileScale / 2)), (int) (zoomScale * (tileScale / 2)));

                            }

                        } catch (Exception e) {

                        }
                        //              </editor-fold>

                        //              <editor-fold desc="deals with the settlement names tiles">
                        try {

                            JLabel settlementNameTile = settlementsNameTileMap.get("x" + x + "y" + y);
                            if (!settlementNameTile.getName().isEmpty()) {
                                settlementNameTile.setFont(new java.awt.Font("Segoe UI", 0, (int) (6 + (zoomScale * 4))));
                                settlementNameTile.setBounds(((((int) (screenWidth * 0.5)) - (int) (zoomScale * tileScale) + (int) ((zoomScale * tileScale * 3) / 5))) + (int) (x * (zoomScale * tileScale)) - (int) (coordinatesX * (zoomScale * tileScale)) - (int) ((tileScale * ((zoomScale - 3) * -1)) / 20),
                                        ((((int) (screenHeight * 0.5)) - (int) (zoomScale * tileScale) + (int) ((zoomScale * tileScale * 4) / 7))) + (int) (y * (zoomScale * tileScale)) - (int) (coordinatesY * (zoomScale * tileScale)),
                                        (int) (zoomScale * (tileScale / 1.25)) + (int) ((tileScale * ((zoomScale - 3) * -1)) / 10), (int) (zoomScale * (tileScale / 5)));
                            }

                        } catch (Exception e) {

                        }
                        //              </editor-fold>

                    }

                }
            }

        }

    }

    private void panel_MainpanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panel_MainpanelKeyPressed

        if (loaded) {
            switch (evt.getKeyCode()) {

                case KeyEvent.VK_W:

                    if (coordinatesY > 1) {

                        coordinatesY--;

                    }
                    break;

                case KeyEvent.VK_S:

                    if (coordinatesY < yMax) {

                        coordinatesY++;

                    }
                    break;

                case KeyEvent.VK_A:

                    if (coordinatesX > 1) {

                        coordinatesX--;

                    }
                    break;

                case KeyEvent.VK_D:

                    if (coordinatesX < xMax) {

                        coordinatesX++;

                    }
                    break;
                case KeyEvent.VK_EQUALS:
                    zoomScale -= (-1 * zoomScaleSensitivity);
                    break;
                case KeyEvent.VK_MINUS:
                    zoomScale -= (1 * zoomScaleSensitivity);
                    break;

            }

            zoomScaleAdjusted();

            updateDashboard();

            adjustUIComponents();
        }

    }//GEN-LAST:event_panel_MainpanelKeyPressed

    private void zoomScaleAdjusted() {

        if (loaded) {
            if (zoomScale > zoomScaleMax) {
                zoomScale = zoomScaleMax;
            } else if (zoomScale < 1) {
                zoomScale = 1;
            }
        }
    }

// This method implements the zoom in/ out feature
    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved

        // 0.25 adjusts the sensitivity of the zoom
        zoomScale -= (evt.getWheelRotation() * zoomScaleSensitivity);

        zoomScaleAdjusted();

        adjustUIComponents();

    }//GEN-LAST:event_formMouseWheelMoved

    private void button_GenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_GenerateActionPerformed

        SaveWorldSettings();
        dispose();
        new LoadingSequence().setVisible(true);

    }//GEN-LAST:event_button_GenerateActionPerformed

    private void button_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveActionPerformed


    }//GEN-LAST:event_button_SaveActionPerformed

    private void loadSettlementNames() {

        try (FileReader reader = new FileReader("src\\ProjectOuroboros\\SettlementNames.json")) {

            System.out.println("[Loading Settlement Names]");

            JSONObject loadCurrent = (JSONObject) parser.parse(reader);
            JSONArray loadCurrentArray = (JSONArray) loadCurrent.get("Taleria");

            JSONObject settlementNamesObj = (JSONObject) loadCurrentArray.get(0);
            settlementPrefixes = (JSONArray) settlementNamesObj.get("prefix");
            System.out.println("settlementPrefixes: " + settlementPrefixes.size());

            settlementNamesObj = (JSONObject) loadCurrentArray.get(1);
            settlementSufixes = (JSONArray) settlementNamesObj.get("sufix");
            System.out.println("settlementSufixes: " + settlementSufixes.size());

            linebreak(1);

        } catch (Exception e) {

        }

    }

    private void LoadWorldSettings() {

        try (FileReader reader = new FileReader(worldSettingsFilepath)) {

            System.out.println("[Loading World Settings]");

            JSONObject loadCurrent = (JSONObject) parser.parse(reader);
            JSONArray loadCurrentArray = (JSONArray) loadCurrent.get("current");

            JSONObject tileScaleObj = (JSONObject) loadCurrentArray.get(0);
            String tileScaleStr = (String) tileScaleObj.get("tileScale");
            slider_TileScale.setValue(Integer.parseInt(tileScaleStr));
            tileScale = Integer.parseInt(tileScaleStr);
            System.out.println("tileScale: " + tileScale);

            JSONObject mapSizeObj = (JSONObject) loadCurrentArray.get(1);
            String mapSizeStr = (String) mapSizeObj.get("mapsize");
            slider_MapSize.setValue(Integer.parseInt(mapSizeStr));
            mapSize = Integer.parseInt(mapSizeStr) + 2;
            xMax = mapSize * 12 + 1;
            yMax = (int) (mapSize * 6 + 3);
            xMaxArray = new int[xMax + 1];
            yMaxArray = new int[yMax + 1];
            coordinatesX = xMaxArray.length / 2;
            coordinatesY = yMaxArray.length / 2;
            System.out.println("mapSize: " + mapSize);

            JSONObject populationObj = (JSONObject) loadCurrentArray.get(2);
            String populationStr = (String) populationObj.get("population");
            slider_Population.setValue((int) Float.parseFloat(populationStr));
            population = Float.parseFloat(populationStr);
            cities = mapSize - 1 + (int) (population * 1) - 1;
            towns = (int) (cities * 1.4) + (int) (mapSize * 0.8) + (int) (mapSize * 0.4) + ((int) (mapSize * 0.2) * 2);
            villages = (int) (towns * 1.6) + (int) (mapSize * 0.8) + (int) (mapSize * 0.4) + ((int) (mapSize * 0.2) * 2);
            System.out.println("population: " + population);

            JSONObject mountainSpawnRateObj = (JSONObject) loadCurrentArray.get(3);
            String mountainSpawnRateStr = (String) mountainSpawnRateObj.get("mountainSpawnRate");
            slider_MountainSpawnRate.setValue(Integer.parseInt(mountainSpawnRateStr));
            mountainSpawnRate = mapSize * ((Integer.parseInt(mountainSpawnRateStr) * 2));
            System.out.println("mountainSpawnRate: " + mountainSpawnRate);

            JSONObject lakeSpawnRateObj = (JSONObject) loadCurrentArray.get(4);
            String lakeSpawnRateStr = (String) lakeSpawnRateObj.get("lakeSpawnRate");
            slider_LakeSpawnRate.setValue(Integer.parseInt(lakeSpawnRateStr));
            lakeSpawnRate = mapSize * ((Integer.parseInt(lakeSpawnRateStr) * 2));
            System.out.println("lakeSpawnRate: " + lakeSpawnRate);

            JSONObject forestSpawnRateObj = (JSONObject) loadCurrentArray.get(5);
            String forestSpawnRateStr = (String) forestSpawnRateObj.get("forestSpawnRate");
            slider_ForestSpawnRate.setValue(Integer.parseInt(forestSpawnRateStr));
            forestSpawnRate = mapSize * ((Integer.parseInt(forestSpawnRateStr) * 2));
            System.out.println("forestSpawnRate: " + forestSpawnRate);

            JSONObject riverSourcesObj = (JSONObject) loadCurrentArray.get(6);
            String riverSourcesStr = (String) riverSourcesObj.get("riverSources");
            slider_RiverSources.setValue(Integer.parseInt(riverSourcesStr));
            riverSources = mapSize + (mapSize / (Integer.parseInt(riverSourcesStr))) - (Integer.parseInt(riverSourcesStr) / 6);
            System.out.println("riverSources: " + riverSources);

            JSONObject forestSourcesObj = (JSONObject) loadCurrentArray.get(7);
            String forestSourcesStr = (String) forestSourcesObj.get("forestSources");
            slider_ForestSources.setValue(Integer.parseInt(forestSourcesStr));
            forestSources = mapSize + (mapSize / (Integer.parseInt(forestSourcesStr))) - (Integer.parseInt(forestSourcesStr) / 6);
            System.out.println("forestSources: " + forestSources);

            JSONObject hillBranchesObj = (JSONObject) loadCurrentArray.get(8);
            String hillBranchesStr = (String) hillBranchesObj.get("hillBranches");
            slider_HillBranches.setValue(Integer.parseInt(hillBranchesStr));
            hillBranches = mapSize + (mapSize / (Integer.parseInt(hillBranchesStr))) - (Integer.parseInt(hillBranchesStr) / 6);
            System.out.println("hillBranches: " + hillBranches);

            JSONObject forestBranchesObj = (JSONObject) loadCurrentArray.get(9);
            String forestBranchesStr = (String) forestBranchesObj.get("forestBranches");
            slider_ForestBranches.setValue(Integer.parseInt(forestBranchesStr));
            forestBranches = mapSize + (mapSize / (Integer.parseInt(forestBranchesStr))) - (Integer.parseInt(forestBranchesStr) / 6);
            System.out.println("forestBranches: " + forestBranches);

        } catch (Exception e) {

        }

        linebreak(1);
    }

    private void SaveWorldSettings() {

        try (FileWriter writer = new FileWriter(worldSettingsFilepath)) {

            System.out.println("[Saving World Settings]");

            JSONObject saveCurrent = new JSONObject();
            saveCurrent.put("current", worldSettings);
            JSONArray saveCurrentArray = (JSONArray) saveCurrent.get("current");

            JSONObject saveTileScale = new JSONObject();
            saveTileScale.put("tileScale", Integer.toString(slider_TileScale.getValue()));
            saveCurrentArray.add(0, saveTileScale);

            JSONObject saveMapSize = new JSONObject();
            saveMapSize.put("mapsize", Integer.toString(slider_MapSize.getValue()));
            saveCurrentArray.add(1, saveMapSize);

            JSONObject savePopulation = new JSONObject();
            savePopulation.put("population", Float.toString(slider_Population.getValue()));
            saveCurrentArray.add(2, savePopulation);

            JSONObject saveMountainSpawnRate = new JSONObject();
            saveMountainSpawnRate.put("mountainSpawnRate", Integer.toString(slider_MountainSpawnRate.getValue()));
            saveCurrentArray.add(3, saveMountainSpawnRate);

            JSONObject saveLakeSpawnRate = new JSONObject();
            saveLakeSpawnRate.put("lakeSpawnRate", Integer.toString(slider_LakeSpawnRate.getValue()));
            saveCurrentArray.add(4, saveLakeSpawnRate);

            JSONObject saveForestSpawnRate = new JSONObject();
            saveForestSpawnRate.put("forestSpawnRate", Integer.toString(slider_ForestSpawnRate.getValue()));
            saveCurrentArray.add(5, saveForestSpawnRate);

            JSONObject saveRiverSources = new JSONObject();
            saveRiverSources.put("riverSources", Integer.toString(slider_RiverSources.getValue()));
            saveCurrentArray.add(6, saveRiverSources);

            JSONObject saveForestSources = new JSONObject();
            saveForestSources.put("forestSources", Integer.toString(slider_ForestSources.getValue()));
            saveCurrentArray.add(7, saveForestSources);

            JSONObject saveHillBranches = new JSONObject();
            saveHillBranches.put("hillBranches", Integer.toString(slider_HillBranches.getValue()));
            saveCurrentArray.add(8, saveHillBranches);

            JSONObject saveForestBranches = new JSONObject();
            saveForestBranches.put("forestBranches", Integer.toString(slider_ForestBranches.getValue()));
            saveCurrentArray.add(9, saveForestBranches);

            saveCurrent.put("current", saveCurrentArray);
            writer.write(saveCurrent.toJSONString());

        } catch (Exception e) {

        }

        linebreak(1);
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WorldCreation().setVisible(true);
            }
        });

    }

    //    <editor-fold desc="Java swing variable declarations">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_Generate;
    private javax.swing.JButton button_Save;
    private javax.swing.JLabel label_Biome;
    private javax.swing.JLabel label_Coordinates;
    private javax.swing.JLabel label_ForestBranches;
    private javax.swing.JLabel label_ForestSources;
    private javax.swing.JLabel label_ForestSpawnRate;
    private javax.swing.JLabel label_HillBranches;
    private javax.swing.JLabel label_LakeSpawnRate;
    private javax.swing.JLabel label_MapSize;
    private javax.swing.JLabel label_MountainSpawnRate;
    private javax.swing.JLabel label_Player;
    private javax.swing.JLabel label_Population;
    private javax.swing.JLabel label_ProtoTile;
    private javax.swing.JLabel label_ProtoTile2;
    private javax.swing.JLabel label_River;
    private javax.swing.JLabel label_RiverSources;
    private javax.swing.JLabel label_SettlementName;
    private javax.swing.JLabel label_SettlementType;
    private javax.swing.JLabel label_TileScale;
    private javax.swing.JLabel label_ZoomScale;
    private javax.swing.JPanel panel_Mainpanel;
    private javax.swing.JSlider slider_ForestBranches;
    private javax.swing.JSlider slider_ForestSources;
    private javax.swing.JSlider slider_ForestSpawnRate;
    private javax.swing.JSlider slider_HillBranches;
    private javax.swing.JSlider slider_LakeSpawnRate;
    private javax.swing.JSlider slider_MapSize;
    private javax.swing.JSlider slider_MountainSpawnRate;
    private javax.swing.JSlider slider_Population;
    private javax.swing.JSlider slider_RiverSources;
    private javax.swing.JSlider slider_TileScale;
    // End of variables declaration//GEN-END:variables
//    </editor-fold>

    // <editor-fold desc="Handles terrain generation">
    private JLabel generateProtoTile(String tileCoordinates) {

        JLabel label = new javax.swing.JLabel();

        label.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setToolTipText(null);

        label.setBackground(new java.awt.Color(255, 255, 255));
        label.setForeground(new java.awt.Color(0, 0, 0));
        label.setOpaque(true);

        label.setText(tileCoordinates);
        label.setName(tileCoordinates);

        label.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.black), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        return label;

    }

    private JLabel selectBiome(JLabel label, String biome) {

        switch (biome) {
            case "Oc":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(0, 0, 100));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Oc");
                label.setName("Oc");
                break;
            case "Sh":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(0, 50, 205));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Sh");
                label.setName("Shore");
                break;
            case "Be":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(255, 255, 153));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Be");
                label.setName("Beach");
                break;
            case "Pl":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(22, 109, 22));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Pl");
                label.setName("Plain");
                break;
            case "Mt":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(20, 20, 26));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Mt");
                label.setName("Mt");
                break;
            case "Ht":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(40, 40, 46));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Ht");
                label.setName("Hill");
                break;
            case "Lk":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(15, 80, 245));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Lk");
                label.setName("Lake");
                break;
            case "Rs":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(10, 50, 205));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Rs");
                label.setName("Rs");
                break;
            case "Rv":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(10, 50, 255));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setName("River");
                break;
            case "Fr":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(22, 44, 22));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Fr");
                label.setName("Forest");
                break;
            case "Frr":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(22, 44, 22));
                label.setForeground(new java.awt.Color(255, 255, 255));
                label.setText("Frr");
                label.setName("Frr");
                break;
            case "Capital":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(255, 205, 44));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Ca");
                label.setName("Capital");
                break;
            case "Ct":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(155, 105, 44));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Ct");
                label.setName("City");
                break;
            case "Tw":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(66, 44, 22));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Tw");
                label.setName("Town");
                break;
            case "Vl":
                label.setFont(new java.awt.Font("Segoe UI", 0, 24));
                label.setBackground(new java.awt.Color(44, 22, 11));
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText("Vl");
                label.setName("Village");
                break;
            default:
                break;
        }

        return label;

    }
// </editor-fold>

    public static void linebreak(int type) {

        switch (type) {

            case 0:

                System.out.println("-------------");
                System.out.println();
                break;

            case 1:

                System.out.println("------------------------------");
                System.out.println();
                break;

            case 2:

                System.out.println("------------------------------");
                System.out.println("------------------------------");
                System.out.println();
                break;

            case 3:

                System.out.println("------------------------------");
                System.out.println("------------------------------");
                System.out.println("------------------------------");
                System.out.println();
                break;

            case 4:

                System.out.println();

        }

    }

}
