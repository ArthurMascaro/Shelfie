package br.com.nexushub.web.controller;

import br.com.nexushub.domain.Subject;
import br.com.nexushub.domain.SubjectColor;
import br.com.nexushub.usecases.subject.SubjectCRUD;
import br.com.nexushub.web.model.subject.request.SubjectUpdateRequest;
import br.com.nexushub.web.model.subject.response.SubjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/subjects")
@RestController
public class SubjectController {

    private final SubjectCRUD subjectCRUD;

    public SubjectController(SubjectCRUD subjectCRUD) {
        this.subjectCRUD = subjectCRUD;
    }

    @PostMapping("/save")
    public ResponseEntity<SubjectResponse> createNewSubject(
            @RequestParam("name") String name,
            @RequestParam("difficulty") int difficulty,
            @RequestParam("color") String color) {
        Subject subject = subjectCRUD.createNewSubject(name, difficulty, SubjectColor.valueOf(color.toUpperCase()));
        return ResponseEntity.ok(SubjectResponse.createFromSubject(subject));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectResponse>> findAllSubjects() {
        ArrayList<Subject> subjects = subjectCRUD.findAllSubjects();
        return ResponseEntity.ok(subjects.stream()
                .map(SubjectResponse::createFromSubject)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> findSubjectById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(SubjectResponse.createFromSubject(subjectCRUD.findSubjectById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubjectById(@PathVariable("id") UUID id,
                                                              @RequestParam("name") String name,
                                                              @RequestParam("difficulty") int difficulty,
                                                              @RequestParam("color") String color) {
        Subject subject = subjectCRUD.updateSubjectById(id, SubjectUpdateRequest.createWithAllFields(name, difficulty, SubjectColor.valueOf(color.toUpperCase())));
        return ResponseEntity.ok(SubjectResponse.createFromSubject(subject));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteSubjectById(@PathVariable("id") UUID id) {
        subjectCRUD.deleteSubjectById(id);
        return ResponseEntity.ok(id);
    }
}
