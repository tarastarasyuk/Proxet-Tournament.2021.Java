package proxet.tournament.generator;

import proxet.tournament.generator.dto.Player;
import proxet.tournament.generator.dto.TeamGeneratorResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TeamGenerator {

    private static final int VEHICLE_TYPE_1 = 1;
    private static final int VEHICLE_TYPE_2 = 2;
    private static final int VEHICLE_TYPE_3 = 3;
    private static final int AMOUNT_OF_ONE_TYPE_VEHICLE_ON_ONE_TEAM = 3;
    private static final int TEAM_QUANTITY = 2;
    private static final int TEAM_CAPACITY = 9;
    private static final int TOTAL_AMOUNT_OF_ONE_TYPE_VEHICLE = AMOUNT_OF_ONE_TYPE_VEHICLE_ON_ONE_TEAM * TEAM_QUANTITY;

    private static final String FILE_DATA_DELIMITER = "\t";

    private final Random randomGenerator = new Random();

    /**
     * @param filePath - String value with file location
     * @return an instance of TeamGeneratorResult that contains two lists of players
     */
    public TeamGeneratorResult generateTeams(String filePath) {
        List<Player> playerList = fullFillListWithPlayers(filePath);
        List<List<Player>> listsOfConcretePlayersVehicle = getListsOfConcretePlayersVehicle(playerList);
        return generateResultFromSample(listsOfConcretePlayersVehicle);
    }

    /**
     *
     * @param listsOfConcretePlayersVehicle - list of lists of players with concrete type of vehicle
     * @return lists of l
     */
    private TeamGeneratorResult generateResultFromSample(List<List<Player>> listsOfConcretePlayersVehicle) {
        List<Player> firstList = new ArrayList<>(TEAM_CAPACITY - 1);
        List<Player> secondList = new ArrayList<>(TEAM_CAPACITY - 1);
        int k = 0;
        while (k != listsOfConcretePlayersVehicle.size()) {
            int i = 0;
            Set<Integer> alreadyGotIndexes = new HashSet<>();
            while (i != AMOUNT_OF_ONE_TYPE_VEHICLE_ON_ONE_TEAM) {
                int index = getRandomIndex(alreadyGotIndexes);
                alreadyGotIndexes.add(index);
                firstList.add(listsOfConcretePlayersVehicle.get(k).get(index));
                index = getRandomIndex(alreadyGotIndexes);
                alreadyGotIndexes.add(index);
                secondList.add(listsOfConcretePlayersVehicle.get(k).get(index));
                i++;
            }
            k++;
        }
        return new TeamGeneratorResult(firstList, secondList);
    }


    /**
     * @param alreadyGotIndexes - already taken indexes
     * @return a new random index
     */
    private int getRandomIndex(Set<Integer> alreadyGotIndexes) {
        int index = randomGenerator.nextInt(TOTAL_AMOUNT_OF_ONE_TYPE_VEHICLE);
        while (alreadyGotIndexes.size() != TOTAL_AMOUNT_OF_ONE_TYPE_VEHICLE && alreadyGotIndexes.contains(index)) {
            index = randomGenerator.nextInt(TOTAL_AMOUNT_OF_ONE_TYPE_VEHICLE);
        }
        return index;
    }

    /**
     * Method that choose required players from the sample => 3 vehicles of each of 3 possible vehicle types
     * So that we get three sample of each type of vehicle for further actions
     * @param playerList - list of all players
     * @return list of lists of concrete players vehicle
     */
    private List<List<Player>> getListsOfConcretePlayersVehicle(List<Player> playerList) {
        List<Player> playersWithTypeVehicle1 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_1, playerList);
        List<Player> playersWithTypeVehicle2 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_2, playerList);
        List<Player> playersWithTypeVehicle3 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_3, playerList);
        return Arrays.asList(playersWithTypeVehicle1, playersWithTypeVehicle2, playersWithTypeVehicle3);
    }

    /**
     *
     * @param vehicleType - required type vehicle
     * @param playerList - list of all players
     * @return list of players with max waiting time and required type vehicle
     */
    private List<Player> getPlayersWithConcreteVehicleType(int vehicleType, List<Player> playerList) {
        return playerList.stream()
                .filter(player -> player.getVehicleType() == vehicleType)
                .sorted(Comparator.comparing(Player::getWaitTime).reversed())
                .limit(TOTAL_AMOUNT_OF_ONE_TYPE_VEHICLE)
                .collect(Collectors.toList());
    }

    /**
     *
     * Getting data from file located by value of a required parameter
     * Used java.nio BufferedReader for better performance
     * @param filePath - String value with file location
     * @return - list of {@link Player} instances of all players in a file
     */
    private List<Player> fullFillListWithPlayers(String filePath) {
        List<Player> playerList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                playerList.add(createPlayer(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    /**
     * Split line by needed data and create an new player
     * @param line - String value from file
     * @return a new instance of class {@link Player}
     */
    private Player createPlayer(String line) {
        String[] playerData = line.split(FILE_DATA_DELIMITER);
        String name = playerData[0].trim();
        int waitTime = Integer.parseInt(playerData[1].trim());
        int vehicleType = Integer.parseInt(playerData[2].trim());
        return new Player(name, waitTime, vehicleType);
    }


}
