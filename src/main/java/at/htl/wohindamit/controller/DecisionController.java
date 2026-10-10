
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.DecisionRequest;
import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.service.DecisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decisions")
public class DecisionController {

    private final DecisionService decisionService;

    public DecisionController(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @PostMapping
    public ResponseEntity<DecisionResponse> evaluate(
            @RequestBody DecisionRequest request) {

        return decisionService.evaluate(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}