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
import java.util.stream.Stream;

public class TeamGenerator {

    private static final int VEHICLE_TYPE_1 = 1;
    private static final int VEHICLE_TYPE_2 = 2;
    private static final int VEHICLE_TYPE_3 = 3;
//    private static final int

    private List<Player> playerList = new ArrayList<>();
    private Random randomGenerator = new Random();

    public TeamGeneratorResult generateTeams(String filePath) {
        playerList = fullFillListWithPlayers(filePath);
        List<List<Player>> listsOfConcretePlayersVehicle = getListsOfConcretePlayersVehicle(playerList);
        List<List<Player>> lists = generateTwoListsFromSample(listsOfConcretePlayersVehicle);



        return new TeamGeneratorResult(
                lists.get(0), lists.get(1)
        );
    }

    private List<List<Player>> generateTwoListsFromSample(List<List<Player>> listsOfConcretePlayersVehicle) {
        List<Player> firstList = new ArrayList<>(8);
        List<Player> secondList = new ArrayList<>(8);
        List<List<Player>> lists = new ArrayList<>();
        int k = 0;
        while (k != 3) {
            int i = 0;
            Set<Integer> alreadyGotIndexes = new HashSet<>();
            while (i != 3) {
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
        lists.add(firstList);
        lists.add(secondList);
        return lists;
    }

    private int getRandomIndex(Set<Integer> alreadyGotIndexes) {
        int index = randomGenerator.nextInt(6);
        while (alreadyGotIndexes.size()!=6 && alreadyGotIndexes.contains(index)) {
            index = randomGenerator.nextInt(6);
        }
        return index;
    }


    /**
     * TODO: MAIN CONDITION: THERE SHOULD BE SIX PLAYERS WITH 1, WITH 2, WITH 3 TYPE OF VEHICLES TO FOLLOW THE GIVEN CONDITION
     */
    private List<List<Player>> getListsOfConcretePlayersVehicle(List<Player> playerList) {
        List<Player> playersWithTypeVehicle1 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_1, playerList);
        List<Player> playersWithTypeVehicle2 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_2, playerList);
        List<Player> playersWithTypeVehicle3 = getPlayersWithConcreteVehicleType(VEHICLE_TYPE_3, playerList);
        return Arrays.asList(playersWithTypeVehicle1, playersWithTypeVehicle2, playersWithTypeVehicle3);
    }

    private List<Player> getPlayersWithConcreteVehicleType(int vehicleType, List<Player> playerList) {
        return playerList.stream()
                .filter(player -> player.getVehicleType()==vehicleType)
                .sorted(Comparator.comparing(Player::getWaitTime).reversed())
                .limit(6)
                .collect(Collectors.toList());
    }

    /**
     * \
     * Used java.nio for better performance
     *
     * @param filePath
     * @return
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

    private Player createPlayer(String line) {
        String[] playerData = line.split("\t");
        String name = playerData[0].trim();
        int waitTime = Integer.parseInt(playerData[1].trim());
        int vehicleType = Integer.parseInt(playerData[2].trim());
        return new Player(name, waitTime, vehicleType);
    }


}
