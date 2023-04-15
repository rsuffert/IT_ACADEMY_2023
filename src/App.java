import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Main class of the program. This class carries out the communication (input/output) between the pgoram and the user, and this is the class that should be executed to run the program.
 */
public class App {
    public static final int CITY_COUNT = 24;
    public static final double SMALL_TRUCK_PRICE = 4.87;
    public static final double MEDIUM_TRUCK_PRICE = 11.92;
    public static final double LARGE_TRUCK_PRICE = 27.44;
    public static final int MAX_WEIGHT_SMALL_TRUCK = 1000;
    public static final int MAX_WEIGHT_MEDIUM_TRUCK = 4000;
    public static final int MAX_WEIGHT_LARGE_TRUCK = 10000;

    public static int[][] distances = new int[CITY_COUNT][CITY_COUNT];
    public static String[] cities = new String[CITY_COUNT];
    public static ArrayList<Transportation> transportations = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        readCsv("DNIT-Distancias.csv");
        askForInputAndHandle();
    }

    /**
     * Reads the CSV file containing the distances between the cities.
     * @param file the path of the file to be read.
     */
    public static void readCsv(String file) {
        Path path = Paths.get(file);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("utf8"))) {
            String line = reader.readLine();
            cities = line.split(";");

            int count = 0;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                for (int i=0; i<data.length; i++) {
                    distances[count][i] = Integer.parseInt(data[i]);
                }
                count++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Asks the user for input (1, 2, 3 or 4) and invokes the method handleOption to handle the selected option.
     */
    public static void askForInputAndHandle() {
        int option = 0;
        boolean done = false;
        do {
            String optionS = JOptionPane.showInputDialog(null, "Digite a opção desejada, de acordo com o menu:\n\t1 - Consultar trechos x modalidade;\n\t2 - Cadastrar transporte;\n\t3 - Dados estatísticos;\n\t4 - Finalizar o programa.", "DIGITE A OPÇÃO DESEJADA", JOptionPane.INFORMATION_MESSAGE);
            optionS = optionS.trim();

            boolean displayErrorMessage = false;
            if (optionS.length() == 1) {
                char optionC = optionS.charAt(0);

                if (Character.isDigit(optionC)) {
                    option = Integer.parseInt(optionS);

                    if (option >= 1 && option <= 4) done = true;
                    else displayErrorMessage = true;
                }
                else displayErrorMessage = true;
            }
            else displayErrorMessage = true;

            if (displayErrorMessage)
                JOptionPane.showMessageDialog(null, "Opção inválida! Consulte o menu e tente novamente.", "OPÇÃO INVÁLIDA", JOptionPane.ERROR_MESSAGE);
        } while (!done && option != 4);

        handleOption(option);
    }

    /**
     * Invokes the appropriate method to handle the option selected by the user.
     * @param option option selected by the user.
     */
    public static void handleOption(int option) {
        switch(option) {
            case 1: executeOptionOne();   askForInputAndHandle();  break;
            case 2: executeOptionTwo();   askForInputAndHandle();  break;
            case 3: executeOptionThree(); askForInputAndHandle();  break;
            case 4: executeOptionFour();  askForInputAndHandle();  break;
        }
    }

    /**
     * This method is invoked when the user selects option 1.
     */
    public static void executeOptionOne() {
        String cityNames = JOptionPane.showInputDialog(null, "Digite o nome da cidade de origem e da cidade de destino, respectivamente, separados por \"-\":", "DIGITE OS NOMES DAS CIDADES", JOptionPane.INFORMATION_MESSAGE);
        cityNames = cityNames.trim().toUpperCase();
        String[] listOfCities = cityNames.split("-");

        if (listOfCities.length != 2) {
            JOptionPane.showMessageDialog(null, "Você deve digitar duas cidades válidas. Retornando ao menu inicial.", "DIGITE DUAS CIDADES VÁLIDAS", JOptionPane.ERROR_MESSAGE);
            return;
        }

        listOfCities[0] = listOfCities[0].trim();
        listOfCities[1] = listOfCities[1].trim();

        if (!citiesAreValid(listOfCities)) {
            JOptionPane.showMessageDialog(null, "As cidades digitadas não constam no sistema. Retornando ao menu inicial.", "CIDADE INVÁLIDA", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int initCityIdx = -1;
        int finalCityIdx = -1;
        for (int i=0; i<cities.length; i++) {
            if (cities[i].equals(listOfCities[0])) initCityIdx = i;
            else if (cities[i].equals(listOfCities[1])) finalCityIdx = i;
        }

        int distance = 0;
        if ((initCityIdx >= 0 && initCityIdx <= CITY_COUNT) && 
            (finalCityIdx >= 0 && finalCityIdx <= CITY_COUNT)) {
            distance = distances[initCityIdx][finalCityIdx];
        }
        else {
            JOptionPane.showMessageDialog(null, "As cidades digitadas não constam no sistema. Retornando ao menu inicial.", "CIDADE INVÁLIDA!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String transpOptionS = JOptionPane.showInputDialog(null, "Selecione a opção de transporte desejada, de acordo com o menu:\n\t1 - Pequeno porte;\n\t2 - Médio porte;\n\t3 - Grande porte.", "OPÇÃO DE TRANSPORTE", JOptionPane.INFORMATION_MESSAGE);
        
        int transpOption = 0;
        if (Character.isDigit(transpOptionS.charAt(0)))
            transpOption = Integer.parseInt(transpOptionS);
        else {
            JOptionPane.showMessageDialog(null, "A opção escolhida é inválida. Retornando ao menu inicial.", "OPÇÃO INVÁLIDA", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String transportation = "";
        if (transpOption >= 1 && transpOption <= 3) {
            switch (transpOption) {
                case 1: transportation = "CAMINHÃO DE PEQUENO PORTE"; break;
                case 2: transportation = "CAMINHÃO DE MÉDIO PORTE";   break;
                case 3: transportation = "CAMINHÃO DE GRANDE PORTE";  break;
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "A opção escolhida é inválida. Retornando ao menu inicial.", "OPÇÃO INVÁLIDA", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalCost = 0;
        switch (transpOption) {
            case 1: totalCost = distance * SMALL_TRUCK_PRICE;  break;
            case 2: totalCost = distance * MEDIUM_TRUCK_PRICE; break;
            case 3: totalCost = distance * LARGE_TRUCK_PRICE;  break;
        }

        JOptionPane.showMessageDialog(null, String.format("De %s a %s, a distância é de %d km e, utilizando um %s, o custo será de R$ %.2f.", listOfCities[0], listOfCities[1], distance, transportation, totalCost), "RESULTADO DA CONSULTA", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is invoked when the user selects option 2.
     */
    public static void executeOptionTwo() {
        String seqCities = JOptionPane.showInputDialog(null, "Digite as cidades que irão compor o trajeto, respectivamente, separadas por \"-\":", "DIGITE AS CIDADES", JOptionPane.INFORMATION_MESSAGE);
        String listOfCities[] = seqCities.trim().toUpperCase().split("-");

        if (listOfCities.length < 2) {
            JOptionPane.showMessageDialog(null, "Você deve digitar ao menos duas cidades válidas. Retornando ao menu inicial.", "DIGITE AO MENOS DUAS CIDADES", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!citiesAreValid(listOfCities)) {
            JOptionPane.showMessageDialog(null, "As cidades digitadas não constam no sistema. Retornando ao menu inicial.", "CIDADE INVÁLIDA!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String seqItems = JOptionPane.showInputDialog(null, "Digite o nome do item, seu peso (em kg) e sua quantidade, separando essas informações por \"/\". Para incluir uma sequência de itens, separe-os por \"-\" (ex.: celular/0,5/2 - tablet/1,2/5).", "DIGITE OS ITENS", JOptionPane.INFORMATION_MESSAGE);
        String[] items = seqItems.trim().toUpperCase().split("-");

        Item[] itemsArray = new Item[items.length];
        for (int i=0; i<items.length; i++) {
            String itemName = items[i].substring(0, items[i].indexOf("/")).trim();
            String itemWeight = items[i].substring(items[i].indexOf("/")+1, items[i].lastIndexOf("/")).trim();
            String itemQuantity = items[i].substring(items[i].lastIndexOf("/")+1).trim();

            double itemWeightD = 0.0;
            int itemQuantityI = 0;
            try {
                itemWeightD = Double.parseDouble(itemWeight.replace(',', '.'));
                itemQuantityI = Integer.parseInt(itemQuantity);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Utilize apenas valores reais para o peso do item e valores inteiros para a quantidade. Retornando ao menu inicial.", "FORMATO INVÁLIDO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            itemsArray[i] = new Item(itemName, itemWeightD, itemQuantityI);
        }

        Transportation transp = new Transportation(listOfCities, itemsArray);
        transportations.add(transp);

        int distance = transp.getDistance();
        double totalCost = transp.getTotalCost();
        double averageCost = transp.getAverageCost();
        int[] nTrucks = transp.getNTrucks();

        JOptionPane.showMessageDialog(null, 
                                        String.format("O percurso %s terá uma distância de %d km.\nPara transportar os produtos %s, o custo será de R$ %.2f (custo unitário médio de R$ %.2f). Para isso, serão necessários:\n- %d caminhão(ões) de pequeno porte;\n- %d caminhão(ões) de médio porte;\n- %d caminhão(ões) de grande porte;", transp.getCitiesString(), distance, transp.getItemsString(), totalCost, averageCost, nTrucks[0], nTrucks[1], nTrucks[2]), 
                                        "RESULTADO DA SIMULAÇÃO", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is invoked when the user selects option 3.
     */
    public static void executeOptionThree() {
        StringBuilder sb = new StringBuilder();

        if (transportations.size() == 0) {
            sb.append("Nenhum transporte cadastrado!");
        }
        else {
            int i = 0;
            for (Transportation t : transportations) {
                i++;
                sb.append(String.format("TRANSPORTE %d:\n", i));
                sb.append(t.getTransportationString()+"\n\n");
            }
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "DADOS ESTATÍSTICOS", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is invoked when the user selects option 4.
     */
    public static void executeOptionFour() {
        JOptionPane.showMessageDialog(null, "Obrigado! O programa será encerrado.", "ENCERRAR PROGRAMA", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * Determines whether the cities passed as parameter are in the array of cities loaded from the CSV file.
     * @param listOfCities
     * @return
     */
    private static boolean citiesAreValid(String[] listOfCities) {
        boolean valid = false;
        for (String lc : listOfCities) {
            valid = false;
            lc = lc.trim();
            for (String c : cities) {
                if (lc.equals(c)) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                valid = false;
                break;
            }
        }

        return valid;
    }
}
