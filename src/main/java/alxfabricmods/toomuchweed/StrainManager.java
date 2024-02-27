package alxfabricmods.toomuchweed;


import java.util.HashMap;

public class StrainManager {

    //NBT Keys
    public static final String NBTKeyItemStrain = "itemStrain";
    public static final String NBTKeyItemType = "itemStrainType";
    public static final String NBTKeyItemTHC = "itemTHC";
    public static final String NBTKeyItemCBD = "itemCBD";
    public static final String NBTKeyItemYield = "branchYield";
    public static final String NBTKeyBlockStrain = "plantStrain";
    public static final String NBTKeyBlockType = "plantStrainType";
    public static final String NBTKeyBlockMaxTHC = "plantMaxTHC";
    public static final String NBTKeyBlockMaxCBD = "plantMaxCBD";
    public static final String NBTKeyBlockCurrentTHC = "plantCurrentTHC";
    public static final String NBTKeyBlockYieldPer = "yieldPer";
    public static final String NBTKeyBlockCurrentCBD = "plantCurrentCBD";
    public static final String NBTKeyBlockHasUV = "hasUV";
    public static final String NBTKeyBlockAge = "age";
    public static final String NBTKeyBlockMaxYield = "maxYield";
    public static final String NBTKeyBlockYield = "yield";

    //StrainMap
    private final static HashMap<String,weedStrain> StrainMap = new HashMap<String,weedStrain>();

    //Strains
    public static weedStrain ERR_STRAIN  = new weedStrain("ERR_STRAIN", 2, 420, 420, 0,420, "ERROR");
    public static weedStrain SOUR_DIESEL = new weedStrain("SOUR_DIESEL", 2, 19, 0, 1,700, "Sour Diesel");
    public static weedStrain ACAPULCO_GOLD = new weedStrain("ACAPULCO_GOLD", 0, 18, 1, 2,500, "Acapulco Gold");
    public static weedStrain ICE_CREAM_CAKE = new weedStrain("ICE_CREAM_CAKE", 1, 22, 1, 3,650, "Ice Cream Cake");

    public StrainManager(){
        //Assign all strains to string IDs
        StrainMap.put("ERR_STRAIN",ERR_STRAIN);
        StrainMap.put("SOUR_DIESEL",SOUR_DIESEL);
        StrainMap.put("ACAPULCO_GOLD",ACAPULCO_GOLD);
        StrainMap.put("ICE_CREAM_CAKE",ICE_CREAM_CAKE);
    }

    //Get Strain based on identifier
    public static weedStrain getStrain(String identifier){
        if(StrainMap.get(identifier) != null) {
            return StrainMap.get(identifier);
        }else{
            return ERR_STRAIN;
        }
    }
    //Get the name of the strain from the ID
    public static String strainTypeToString(int type){
        switch (type){
            case 0:
                return "Sativa";
            case 1:
                return "Indica";
            case 2:
                return "Hybrid";
            default:
                return "Unknown";
        }
    }
}
