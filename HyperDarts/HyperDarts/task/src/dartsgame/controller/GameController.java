package dartsgame.controller;

import dartsgame.DTO.StatusResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    Map<String, String> status = Collections.singletonMap("status", "Under construction!");

    @PostMapping("create")
    public Map<String, String> createGame() {
        return status;
    }

    @GetMapping("list")
    public Map<String, String> getCurrentGames() {
        return status;
    }

    @GetMapping("join")
    public Map<String, String> joinGame() {
        return status;
    }

    @GetMapping("status")
    public Map<String, String> getGameStatus() {
        return status;
    }

    @PostMapping("throws")
    public Map<String, String> throwDart() {
        return status;
    }

}
