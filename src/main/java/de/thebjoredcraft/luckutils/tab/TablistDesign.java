package de.thebjoredcraft.luckutils.tab;

import de.thebjoredcraft.luckutils.LuckUtils;

public enum TablistDesign {
    MODERN("<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>Dein Rang: <bold>%prefix%</bold>", "%prefix% <dark_gray>|</dark_gray> %player% %registered%","<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>Dein Ping:<bold> %ping%</bold>", "%prefix% <dark_gray>|</dark_gray> %player% %registered%"),
    CLEAN("<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>Dein Rang: <bold>%prefix%</bold>", "%prefix% <dark_gray>|</dark_gray> %player%","<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>Dein Ping:<bold> %ping%</bold>", "%prefix% <dark_gray>|</dark_gray> %player%"),
    ONLY_NAME("", "", "%player%","", "", "%player%"),
    STANDARD("<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>LuckUtils v11", "%prefix% <dark_gray>|</dark_gray> %player% %registered%","<color:#3b92d1>" + LuckUtils.getInstance().getConfig().getString("ServerName", ""), "<color:#40d1db>LuckUtils v11", "%prefix% <dark_gray>|</dark_gray> %player% %registered%"),
    CUSTOM(LuckUtils.getInstance().getConfig().getString("TablistHeader", ""), LuckUtils.getInstance().getConfig().getString("TablistFooter", ""), LuckUtils.getInstance().getConfig().getString("TablistName", ""), LuckUtils.getInstance().getConfig().getString("AnimatedTablistHeader", ""),LuckUtils.getInstance().getConfig().getString("AnimatedTablistFooter", ""), LuckUtils.getInstance().getConfig().getString("AnimatedTablistName", ""));

    public final String header;
    public final String footer;
    public final String pName;
    public final String header2;
    public final String footer2;
    public final String pName2;
    TablistDesign(String header, String footer, String pName, String header2, String footer2, String pName2){
        this.footer = footer;
        this.header = header;
        this.pName = pName;
        this.footer2 = footer2;
        this.header2 = header2;
        this.pName2 = pName2;
    }

    public String getFooter() {
        return footer;
    }

    public String getHeader() {
        return header;
    }

    public String getPName() {
        return pName;
    }

    public String getFooter2() {
        return footer2;
    }

    public String getHeader2() {
        return header2;
    }

    public String getPName2() {
        return pName2;
    }
}
