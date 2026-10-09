package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.Rule;
import at.htl.wohindamit.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }

    public Optional<Rule> findById(Long id) {
        return ruleRepository.findById(id);
    }

    public Rule save(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void deleteById(Long id) {
        ruleRepository.deleteById(id);
    }
}