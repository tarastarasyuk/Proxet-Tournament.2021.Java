package proxet.tournament.generator.dto;

public class Player {

    private final String nickname;
    private final int vehicleType;
    //
    private final int waitTime;

    public Player(String nickname, int waitTime, int vehicleType) {
        this.nickname = nickname;
        this.waitTime = waitTime;
        this.vehicleType = vehicleType;
    }

    public String getNickname() {
        return nickname;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public int getWaitTime() {
        return waitTime;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", vehicleType=" + vehicleType +
                ", waitTime=" + waitTime +
                '}';
    }
}
