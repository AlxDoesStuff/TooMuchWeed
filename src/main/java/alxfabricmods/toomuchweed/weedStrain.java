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
    public weedStrain (String Name, int Type, int MaxPotentialTHC, int MaxPotentialCBD, int NumeralID){
        name = Name;
        type = Type;
        maxPotentialTHC = MaxPotentialTHC;
        maxPotentialCBD = MaxPotentialCBD;
        numeralID = NumeralID;
    }

    public String getName() {
        return name;
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
}
