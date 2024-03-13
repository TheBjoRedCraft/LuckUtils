package de.thebjoredcraft.luckutils.tab;

public enum TablistDesign {
    MODERN("", "", "","", "", ""),
    RUSTICALLY("", "", "","", "", ""),
    CLEAN("", "", "","", "", ""),
    ONLY_NAME("", "", "","", "", ""),
    STANDARD("", "", "","", "", ""),
    CUSTOM("", "", "","", "", "");

    public final String header;
    public final String footer;
    public final String pName;

    TablistDesign(String header, String footer, String pName, String header2, String footer2, String pName2){
        this.footer = footer;
        this.header = header;
        this.pName = pName;
    }
}
