package at.fhooe.mc.android.applicationandroid;

public class SpinnerData {
    private int icon;
    private String IconName;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconName() {
        return IconName;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }

    public SpinnerData(int icon, String iconName) {
        this.icon = icon;
        IconName = iconName;
    }
}
