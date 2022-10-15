package com.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Move;
import core.State;
import heuristics.Heuristic1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@SpringBootApplication
public class AiTema1ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTema1ServerApplication.class, args);
    }
    @PostMapping("/request")
    public List<Map<Move, State>> request(@RequestBody String body) throws JsonProcessingException {
        ArrayList<HashMap<String, String>> list = new ObjectMapper().readValue(body, new TypeReference<ArrayList<HashMap<String, String>>>() {
        });

        var nObj = list.get(0);
        var mObj = list.get(1);
        var kObj = list.get(2);
        var strategy = list.get(3);

        long n = Long.parseLong(nObj.get("value"));
        long m = Long.parseLong(mObj.get("value"));
        long k = Long.parseLong(kObj.get("value"));
        String strategyType = strategy.get("value");

        int[] typeOperation = new int[]{0, 2, 1, 2, 0, 2};
        int[] vaseNumber = new int[]{0, 0, 1, 0, 0, 0};

        System.out.println(strategyType);

        List<Map<Move, State>> result = new ArrayList<>();
        switch (strategyType) {
            case "Backtracking":
//                result = Main.executeBacktrackingV2Strategy(State.getInitialState(n, m, k));
                break;
            case "A*":
//                Main.executeAStarStrategy(State.getInitialState(n, m, k), new Heuristic2());
                break;
            case "BFS":
                Main.executeBFSV2Strategy(State.getInitialState(n, m, k));
                break;
            case "HillClimb":
                Main.executeGreedyHillClimbingStrategy(State.getInitialState(n, m, k), new Heuristic1(), 5000, 5, 20);
                break;
        }
        System.out.println(result);
        return result;
    }
}
