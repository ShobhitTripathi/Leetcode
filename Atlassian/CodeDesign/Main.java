package atlassian.codeDesign;

/*

Imagine we have a customer support ticketing system.
The system allows customers to rate the replies the support agent gives them out of 5.
To start with, write a function which accepts a rating, and another which will tell me the average rating for each agent,
ordered highest to lowest.
 */

import java.util.List;
import java.util.Map;

public class Main {

    // write your code here



    public static void main(String[] args) {
        System.out.println("hello world");

        Agent agent1 = new Agent(1, "ABC");
        Agent agent2 = new Agent(2, "DEF");
        Agent agent3 = new Agent(3, "GHI");

        AgentService agentService = new AgentService();
        agentService.addAgent(agent1);
        agentService.addAgent(agent2);
        agentService.addAgent(agent3);

        agentService.updateRating(agent1, 4);
        agentService.updateRating(agent1, 4);
        agentService.updateRating(agent1, 3);
        agentService.updateRating(agent1, 2);
        agentService.updateRating(agent1, 5);

        agentService.updateRating(agent2, 4);
        agentService.updateRating(agent2, 5);
        agentService.updateRating(agent2, 5);
        agentService.updateRating(agent2, 2);
        agentService.updateRating(agent2, 5);

        agentService.updateRating(agent3, 5);
        agentService.updateRating(agent3, 5);
        agentService.updateRating(agent3, 5);
        agentService.updateRating(agent3, 4);
        agentService.updateRating(agent3, 5);

        System.out.println("avg rating1: " + agentService.getAvgRating(agent1));
        System.out.println("avg rating2: " + agentService.getAvgRating(agent2));
        System.out.println("avg rating3: " + agentService.getAvgRating(agent3));

        List<Map.Entry<Agent, Float>> entries = agentService.getAllRatings();


        for (Map.Entry<Agent, Float> entry : entries) {
            System.out.println(entry.getKey().getName() + "  " +  entry.getValue());
        }

    }
}
