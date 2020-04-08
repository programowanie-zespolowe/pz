package sample.Structs;

public class OutdoorGamePath {
    int idQuestionPoint;
    int idOutdoorGame;
    int idPoint;
    String question;
    String answer;
    Integer idNextPoint;
    Integer idHintPoint;

    public int getIdQuestionPoint() {
        return idQuestionPoint;
    }

    public void setIdQuestionPoint(int idQuestionPoint) {
        this.idQuestionPoint = idQuestionPoint;
    }

    public int getIdOutdoorGame() {
        return idOutdoorGame;
    }

    public void setIdOutdoorGame(int idOutdoorGame) {
        this.idOutdoorGame = idOutdoorGame;
    }

    public int getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(int idPoint) {
        this.idPoint = idPoint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getIdNextPoint() {
        return idNextPoint;
    }

    public void setIdNextPoint(Integer idNextPoint) {
        this.idNextPoint = idNextPoint;
    }

    public Integer getIdHintPoint() {
        return idHintPoint;
    }

    public void setIdHintPoint(Integer idHintPoint) {
        this.idHintPoint = idHintPoint;
    }
}
