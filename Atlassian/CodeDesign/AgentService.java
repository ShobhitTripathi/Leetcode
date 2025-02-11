package atlassian.codeDesign;

import java.awt.desktop.SystemEventListener;
import java.util.*;

public class AgentService {
    private Map<Agent, Float> ratingMap;
    private List<Agent> agentsList;

    AgentService() {
        ratingMap = new HashMap<>();
        agentsList = new ArrayList<>();
    }

    public void addAgent(Agent agent) {
        agentsList.add(agent);
        ratingMap.put(agent, agent.getAvg_rating());
    }

    public void updateRating(Agent a, int rating) {
        if (!agentsList.contains(a)) {
            System.out.println("Agent not present in the system.");
        }
        if (rating > 5 || rating < 0) {
            System.out.println("Provide values from 1 - 5");
        }
        float currentAvgRating = ratingMap.get(a);
        int oldNumber = a.getNumberOfRating();
        a.setNumberOfRating(oldNumber + 1);

        ratingMap.put(a, ((currentAvgRating * oldNumber) + rating)/ (a.getNumberOfRating()));
    }

    public float getAvgRating(Agent a) {
        return ratingMap.get(a);
    }

//    public List<Map.Entry<Agent, Float>> getAllRatings() {
//        PriorityQueue<Map.Entry<Agent, Float>> q = new PriorityQueue<>(
//                (a, b) -> Float.compare(ratingMap.get(b), ratingMap.get(a)));
//        List<Map.Entry<Agent, Float>> result = new ArrayList<>();
//
//        for (Map.Entry<Agent, Float> entry : ratingMap.entrySet()) {
//            q.offer(entry);
//        }
//
//        while (!q.isEmpty()) {
//            result.add(q.poll());
//        }
//        return result;
//    }

    public List<Map.Entry<Agent, Float>> getAllRatings() {
        List<Map.Entry<Agent, Float>> result = new ArrayList<>();
        result.addAll(ratingMap.entrySet());


        Collections.sort(result, (a, b)-> b.getValue().compareTo(a.getValue()));


        return result;
    }


}
