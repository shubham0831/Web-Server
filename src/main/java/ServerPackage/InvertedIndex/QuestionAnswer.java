/*
 Author Name : Shubham Pareek
 Class function : Blueprint of a QuestionAnswer object and everything that object contains
 Project Number : 1
*/

package ServerPackage.InvertedIndex;

public class QuestionAnswer {
    private String questionType;
    private String asin;
    private String answerTime;
    private String unixTime;
    private String question;
    private String answerType;
    private String answer;

    public QuestionAnswer(String questionType, String asin, String answerTime, String unixTime, String question, String answerType, String answer) {
        //constructor initializing the variables
        this.questionType = questionType;
        this.asin = asin;
        this.answerTime = answerTime;
        this.unixTime = unixTime;
        this.question = question;
        this.answerType = answerType;
        this.answer = answer;
    }


    //getters start
    public String getQuestionType() {
        return questionType;
    }

    public String getAsin() {
        return asin;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerType() {
        return answerType;
    }

    public String getAnswer() {
        return answer;
    }
    //getters end

    @Override
    public String toString() {
        //toString method, to represent the object in string
        if (answerType == null){
            //do not return the answer type if the answerType is null
            //need it since answerType is an optional field only present when the
            //questionType == 'yes/no'
            return "questionType: '" + questionType + '\'' +
                    ", asin: '" + asin + '\'' +
                    ", answerTime: '" + answerTime + '\'' +
                    ", unixTime: '" + unixTime + '\'' +
                    ", question: '" + question + '\'' +
                    ", answer: '" + answer + '\'';
        }
        return  "questionType: '" + questionType + '\'' +
                ", asin: '" + asin + '\'' +
                ", answerTime: '" + answerTime + '\'' +
                ", unixTime: '" + unixTime + '\'' +
                ", question: '" + question + '\'' +
                ", answerType: '" + answerType + '\'' +
                ", answer: '" + answer + '\'';
    }

}
