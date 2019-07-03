package at.krmmr.promillometer;

public class SpinnerDataDrinks {
    private String DrinkName;
    private double val;


    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public double getDrinkval() {
        return val;
    }

    public void setDrinkval(int value) {
        val = value;
    }



    public SpinnerDataDrinks(double value, String drinkName) {
        val = value;
        DrinkName = drinkName;
    }
}
