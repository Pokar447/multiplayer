package multiplayer.client;

/**
 * History class to create entries in the Statistics section
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class History {
    public int id;
    public int userId;
    public int opponentId;
    public int userHp;
    public int opponentHp;
    public String date;
    public String opponentName;

    /**
     * History constructor
     *
     * @param id Unique id of the history
     * @param userId User id the history belongs to
     * @param opponentId User id of the opponent
     * @param userHp Health points of the user
     * @param opponentHp Health points of the opponent
     * @param date Date and time when the game took place
     * @param opponentName User name of the opponent
     */
    public History(int id, int userId, int opponentId, int userHp, int opponentHp, String date, String opponentName) {
        this.id = id;
        this.userId = userId;
        this.opponentId = opponentId;
        this.userHp = userHp;
        this.opponentHp = opponentHp;
        this.date = date;
        this.opponentName = opponentName;
    }

    /**
     * @return userHp Health Points of the user
     */
    public int getUserHp() {
        return userHp;
    }

    /**
     * @return opponentName Name of the opponent
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * @return opponentId User id of the opponent
     */
    public int getOpponentId() {
        return opponentId;
    }

    /**
     * @return opponentHp Health points of the opponent
     */
    public int getOpponentHp() {
        return opponentHp;
    }

    /**
     * @return date Date and time when the game took place
     */
    public String getDate() {
        return date;
    }
}
