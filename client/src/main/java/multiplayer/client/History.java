package multiplayer.client;

public class History {
    public int id;
    public int userId;
    public int opponentId;
    public int userHp;
    public int opponentHp;
    public String date;
    public String opponentname;

    public History(int id, int userId, int opponentId, int userHp, int opponentHp, String date, String opponentname) {
        this.id = id;
        this.userId = userId;
        this.opponentId = opponentId;
        this.userHp = userHp;
        this.opponentHp = opponentHp;
        this.date = date;
        this.opponentname = opponentname;
    }

    public int getUserHp() {
        return userHp;
    }

    public String getOpponentname() {
        return opponentname;
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
