/*
 Author Name : Shubham Pareek
 Class function : Blueprint of a review object and everything that it contains
 Project Number : 1
*/

package ServerPackage.InvertedIndex;

import java.util.Arrays;

public class Review {
    private String reviewerID;
    private String asin;
    private String reviewerName;
    private String [] helpful;
    private String reviewText;
    private String overall;
    private String summary;
    private String unixReviewTime;
    private String reviewTime;

    public Review(String reviewerID, String asin, String reviewerName, String[] helpful, String reviewText, String overall, String summary, String unixReviewTime, String reviewTime) {
        //constructor initializing the variables
        this.reviewerID = reviewerID;
        this.asin = asin;
        this.reviewerName = reviewerName;
        this.helpful = helpful;
        this.reviewText = reviewText;
        this.overall = overall;
        this.summary = summary;
        this.unixReviewTime = unixReviewTime;
        this.reviewTime = reviewTime;
    }

    //getters start
    public String getReviewerID() {
        return reviewerID;
    }

    public String getAsin() {
        return asin;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String[] getHelpful() {
        return helpful;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getOverall() {
        return overall;
    }

    public String getSummary() {
        return summary;
    }

    public String getUnixReviewTime() {
        return unixReviewTime;
    }

    public String getReviewTime() {
        return reviewTime;
    }
    //getters end

    @Override
    public String toString() {
        //string representation of obejct
        return  "reviewerID: '" + reviewerID + '\'' +
                ", asin: '" + asin + '\'' +
                ", reviewerName: '" + reviewerName + '\'' +
                ", helpful: " + Arrays.toString(helpful) +
                ", reviewText: '" + reviewText + '\'' +
                ", overall: '" + overall + '\'' +
                ", summary: '" + summary + '\'' +
                ", unixReviewTime: '" + unixReviewTime + '\'' +
                ", reviewTime: '" + reviewTime + '\'';
    }
}
