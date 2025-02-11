package atlassian.codeDesign;

public class Agent{
    private int id;
    private String name;
    private int numberOfRating;
    private float avg_rating;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Agent(int id, String name) {
        this.id = id;
        this.name = name;
        this.numberOfRating = 0;
        this.avg_rating = 0;
    }

//    public void updateRating(Agent a, int rating) {
//        a.numberOfRating = rating;
//    }


    public float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(int avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }


}

