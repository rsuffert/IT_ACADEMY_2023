import java.util.ArrayList;

/**
 * Represents the transportations made by the company, which are made up by instances of the {@code Item} class.
 */
public class Transportation {
    private ArrayList<String> cities;
    private ArrayList<Item> items;
    private ArrayList<Integer> distancesList;

    public Transportation(String[] cities, Item[] items) {
        this.cities = new ArrayList<>();
        for (String c : cities) {
            this.cities.add(c.trim());
        }

        this.items = new ArrayList<>();
        for (Item i : items) {
            this.items.add(i);
        }

        this.distancesList = new ArrayList<>();
        calculateDistances();
    }

    /**
     * Tells how many cities are in this transportation's route.
     * @return the number of cities in this transportation's route.
     */
    public int getCitiesCount() { return cities.size(); }

    /**
     * Tells how many items are in this transportation.
     * @return the number of items in this transportation.
     */
    public int getItemsCount() { 
        int nItems = 0;
        for (Item i : items) {
            nItems += i.getQuantity();
        }

        return nItems;
    }

    /**
     * Returns a string representation of the cities in this transportation.
     * @return a string representation of the cities in this transportation.
     */
    public String getCitiesString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(cities.get(0));
        for (int i=1; i<cities.size(); i++) {
            sb.append(" - ");
            sb.append(cities.get(i));
        }

        return sb.toString();
    }

    /**
     * Returns a string representation of the items in this transportation.
     * @return a string representation of the items in this transportation.
     */
    public String getItemsString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s (%dx)", items.get(0).getItemName(), items.get(0).getQuantity()));
        for (int i=1; i<items.size(); i++) {
            Item item = items.get(i);
            sb.append(String.format(" - %s (%dx)", item.getItemName(), item.getQuantity()));
        }

        return sb.toString();
    }

    /**
     * Returns a string representation of this transportation.
     * @return a string representation of this transportation.
     */
    public String getTransportationString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Custo total: R$ %.2f | ", this.getTotalCost()));
        
        
        sb.append("Custo por trecho:\n");
        int[] nTrucks = this.getNTrucks();
        int i = 0;
        for (Integer dist : distancesList) {
            double cost = dist * (App.SMALL_TRUCK_PRICE*nTrucks[0] + App.MEDIUM_TRUCK_PRICE*nTrucks[1] + App.LARGE_TRUCK_PRICE*nTrucks[2]);
            sb.append(String.format("\t- %s a %s: R$ %.2f\n", cities.get(i), cities.get(i+1), cost));
            i++;
        }

        int totalDist = this.getDistance();
        
        sb.append(String.format("Custo médio por km: R$ %.2f | ", getTotalCost()/getDistance()));
        sb.append(String.format("Em média, cada tipo de produto custou R$ %.2f\n", getTotalCost()/items.size()));
        
        sb.append(String.format("Custo por cada modalidade de transporte:\n"));
        double smallTruckCost = App.SMALL_TRUCK_PRICE * totalDist * nTrucks[0];
        double mediumTruckCost = App.MEDIUM_TRUCK_PRICE * totalDist * nTrucks[1];
        double largeTruckCost = App.LARGE_TRUCK_PRICE * totalDist * nTrucks[2];
        sb.append(String.format("\t- Pequeno porte: R$ %.2f\n", smallTruckCost));
        sb.append(String.format("\t- Médio porte: R$ %.2f\n", mediumTruckCost));
        sb.append(String.format("\t- Grande porte: R$ %.2f\n", largeTruckCost));
        sb.append(String.format("Número total de veículos deslocados: %d\n", nTrucks[0]+nTrucks[1]+nTrucks[2]));
        sb.append(String.format("Total de itens transportados: %d", getItemsCount()));

        return sb.toString();
    }

    /**
     * Returns the total weight of the items in this transportation.
     * @return the sum of the weights of all items in this transportation.
     */
    public double getTotalWeight() {
        double totalWeight = 0;
        for (Item i : items) {
            totalWeight += i.getWeight() * i.getQuantity();
        }

        return totalWeight;
    }

    /**
     * Returns the total cost of this transportation.
     * @return the total cost of this transportation.
     */
    public double getTotalCost() {
        int[] nTrucks = this.getNTrucks();
        int distance = this.getDistance();

        double smallTruckCost = App.SMALL_TRUCK_PRICE * distance * nTrucks[0];
        double mediumTruckCost = App.MEDIUM_TRUCK_PRICE * distance * nTrucks[1];
        double largeTruckCost = App.LARGE_TRUCK_PRICE * distance * nTrucks[2];

        return smallTruckCost + mediumTruckCost + largeTruckCost;
    }

    /**
     * Returns how many trucks of each type will need to be used to deliver this transportation.
     * @return an array containing the number of small, medium and large trucks necessary for this transportation, respectively.
     */
    public int[] getNTrucks() {
        double totalWeight = this.getTotalWeight();

        int nLargeTrucks = (int) totalWeight / App.MAX_WEIGHT_LARGE_TRUCK;
        int nMediumTrucks = (int) totalWeight % App.MAX_WEIGHT_LARGE_TRUCK / App.MAX_WEIGHT_MEDIUM_TRUCK;
        
        double remainingWeight = totalWeight - (nLargeTrucks*App.MAX_WEIGHT_LARGE_TRUCK + nMediumTrucks*App.MAX_WEIGHT_MEDIUM_TRUCK);
        int nSmallTrucks = (int) Math.ceil(remainingWeight / App.MAX_WEIGHT_LARGE_TRUCK);

        int[] nTrucks = {nSmallTrucks, nMediumTrucks, nLargeTrucks};
        return nTrucks;
    }

    /**
     * Tells the average cost of this transportation, considering each type of item.
     * @return the average cost of this transportation.
     */
    public double getAverageCost() { return getTotalCost() / getItemsCount(); }

    /**
     * Tells the total distance of this transportation.
     * @return the total distance of this transportation.
     */
    public int getDistance() {
        int sum = 0;
        for (Integer dist : distancesList) {
            sum += dist;
        }

        return sum;
    }

    /**
     * Calculates the distances between the cities in the route of the transportation.
     */
    private void calculateDistances() {
        for (int i=0; i<cities.size()-1; i++) {
            int initCityIdx = -1;
            int finalCityIdx = -1;
            String initialCity = cities.get(i);
            String finalCity = cities.get(i+1);
            for (int j=0; j<App.cities.length; j++) {
                if (App.cities[j].equals(initialCity)) initCityIdx = j;
                else if (App.cities[j].equals(finalCity)) finalCityIdx = j;
            }
            distancesList.add(App.distances[initCityIdx][finalCityIdx]);
        }
    }
}
