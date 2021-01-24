package multiplayer.client;

public class History {
    public int id;
    public int userId;
    public int opponentId;
    public int userHp;
    public int opponentHp;
    public String date;

    public History(int id, int userId, int opponentId, int userHp, int opponentHp, String date) {
        this.id = id;
        this.userId = userId;
        this.opponentId = opponentId;
        this.userHp = userHp;
        this.opponentHp = opponentHp;
        this.date = date;
    }

    public int getUserHp() {
        return userHp;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public int getOpponentHp() {
        return opponentHp;
    }

    public String getDate() {
        return date;
    }
}
