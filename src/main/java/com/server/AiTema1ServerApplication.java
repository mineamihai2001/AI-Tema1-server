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
        System.out.println("LIST: " + list);

        long n = 0, m = 0, k = 0;
        int iterations = 0, depth = 0, improvements = 0;
        String strategyType = new String("");
        for (var item : list) {
            var id = item.get("id");
            var value = item.get("value");
            switch (item.get("id")) {
                case "container-1":
                    n = Long.parseLong(value);
                    break;
                case "container-2":
                    m = Long.parseLong(value);
                    break;
                case "capacity":
                    k = Long.parseLong(value);
                    break;
                case "strategy":
                    strategyType = value;
                    break;
                case "depth":
                    depth = Integer.parseInt(value);
                    break;
                case "improvements":
                    improvements = Integer.parseInt(value);
                    break;
                case "iterations":
                    iterations = Integer.parseInt(value);
                    break;
            }
        }


        List<Map<Move, State>> result = new ArrayList<>();
        switch (strategyType) {
            case "Backtracking":
                result = Main.executeBacktrackingV2Strategy(State.getInitialState(n, m, k), iterations);
                break;
            case "A*":
//                Main.executeAStarStrategy(State.getInitialState(n, m, k), new Heuristic2());
                break;
            case "BFS":
                result = Main.executeBFSV2Strategy(State.getInitialState(n, m, k));
                break;
            case "HillClimb":
                result = Main.executeGreedyHillClimbingStrategy(State.getInitialState(n, m, k), new Heuristic1(), iterations, depth, improvements);
                break;
        }
        System.out.println(result);
        return result;
    }
}
