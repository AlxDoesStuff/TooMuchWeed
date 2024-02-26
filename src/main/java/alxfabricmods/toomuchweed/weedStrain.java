package alxfabricmods.toomuchweed;

public class weedStrain {
    //Strain Name
    String name;

    //0 = sativa, 1 = indica 2 = hybrid
    int type;
    //ID of the maximal potential (optimal) THC content
    int maxPotentialTHC;

    //ID of the maximal potential (optimal) CBD content
    int maxPotentialCBD;

    int numeralID;

    int maxPotentialYield;
    String displayName;

    public weedStrain (String Name, int Type, int MaxPotentialTHC, int MaxPotentialCBD, int NumeralID, int MaxPotentialYield, String DisplayName){
        name = Name;
        type = Type;
        maxPotentialTHC = MaxPotentialTHC;
        maxPotentialCBD = MaxPotentialCBD;
        numeralID = NumeralID;
        maxPotentialYield = MaxPotentialYield;
        displayName = DisplayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    public int getMaxPotentialCBD() {
        return maxPotentialCBD;
    }

    public int getMaxPotentialTHC() {
        return maxPotentialTHC;
    }
    public int getNumeralID() { return numeralID; }
    public int getMaxPotentialYield() {return maxPotentialYield;}
}
